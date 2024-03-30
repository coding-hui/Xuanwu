package top.wecoding.xuanwu.ai.minimax.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static top.wecoding.xuanwu.ai.minimax.api.ApiUtils.DEFAULT_BASE_URL;

/**
 * @author wecoding
 * @since 0.10
 */
public class MiniMaxAiApi {

	public static final String DEFAULT_CHAT_MODEL = ChatModel.ABAB5_5_CHAT.getValue();

	public static final String DEFAULT_EMBEDDING_MODEL = EmbeddingModel.EMBO_01.getValue();

	private static final String GROUP_ID_PARAM = "group_id";

	private static final Predicate<String> SSE_DONE_PREDICATE = "[DONE]"::equals;

	private final RestClient restClient;

	private final WebClient webClient;

	private MiniMaxAiStreamFunctionCallingHelper chunkMerger = new MiniMaxAiStreamFunctionCallingHelper();

	/**
	 * Create an new chat completion api with base URL set to https://api.minimax.chat
	 */
	public MiniMaxAiApi() {
		this(DEFAULT_BASE_URL, null, null, RestClient.builder());
	}

	/**
	 * Create an new chat completion api with base URL set to https://api.minimax.chat
	 * @param apiKey MiniMax apiKey.
	 * @param groupId MiniMax groupId.
	 */
	public MiniMaxAiApi(String apiKey, String groupId) {
		this(DEFAULT_BASE_URL, apiKey, groupId, RestClient.builder());
	}

	/**
	 * Create a new chat completion api.
	 * @param baseUrl api base URL.
	 * @param apiKey MiniMax apiKey.
	 * @param groupId MiniMax groupId.
	 */
	public MiniMaxAiApi(String baseUrl, String apiKey, String groupId) {
		this(baseUrl, apiKey, groupId, RestClient.builder());
	}

	/**
	 * Create a new chat completion api.
	 * @param baseUrl api base URL.
	 * @param apiKey MiniMax apiKey.
	 * @param groupId MiniMax groupId.
	 * @param restClientBuilder RestClient builder.
	 */
	public MiniMaxAiApi(String baseUrl, String apiKey, String groupId, RestClient.Builder restClientBuilder) {
		this(baseUrl, apiKey, groupId, restClientBuilder, RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
	}

	/**
	 * Create a new chat completion api.
	 * @param baseUrl api base URL.
	 * @param apiKey MiniMax apiKey.
	 * @param groupId MiniMax groupId.
	 * @param restClientBuilder RestClient builder.
	 * @param responseErrorHandler Response error handler.
	 */
	public MiniMaxAiApi(String baseUrl, String apiKey, String groupId, RestClient.Builder restClientBuilder,
			ResponseErrorHandler responseErrorHandler) {

		if (!StringUtils.hasText(apiKey)) {
			apiKey = System.getenv("MINIMAX_API_KEY");
		}

		if (!StringUtils.hasText(groupId)) {
			groupId = System.getenv("MINIMAX_GROUP_ID");
		}

		Assert.hasText(apiKey, "The apiKey can not be null.");
		Assert.hasText(groupId, "The groupId can not be null.");

		Map<String, String> uriParams = Map.of(GROUP_ID_PARAM, groupId);

		this.restClient = restClientBuilder.baseUrl(baseUrl)
			.defaultHeaders(ApiUtils.getJsonContentHeaders(apiKey))
			.defaultStatusHandler(responseErrorHandler)
			.defaultUriVariables(uriParams)
			.build();

		this.webClient = WebClient.builder()
			.baseUrl(baseUrl)
			.defaultHeaders(ApiUtils.getJsonContentHeaders(apiKey))
			.defaultUriVariables(uriParams)
			.build();
	}

	/**
	 * Creates a streaming chat response for the given chat conversation.
	 * @param chatRequest The chat completion request. Must have the stream property set
	 * to true.
	 * @return Returns a {@link Flux} stream from chat completion chunks.
	 */
	public Flux<ChatCompletionChunk> chatCompletionV2Stream(ChatCompletionRequest chatRequest) {

		Assert.notNull(chatRequest, "The request body can not be null.");
		Assert.isTrue(chatRequest.stream(), "Request must set the steam property to true.");

		AtomicBoolean isInsideTool = new AtomicBoolean(false);

		return this.webClient.post()
			.uri("/v1/text/chatcompletion_v2")
			.body(Mono.just(chatRequest), ChatCompletionRequest.class)
			.retrieve()
			.bodyToFlux(String.class)
			// cancels the flux stream after the "[DONE]" is received.
			.takeUntil(SSE_DONE_PREDICATE)
			// filters out the "[DONE]" message.
			.filter(SSE_DONE_PREDICATE.negate())
			.map(content -> ModelOptionsUtils.jsonToObject(content, ChatCompletionChunk.class))
			// Detect is the chunk is part of a streaming function call.
			.map(chunk -> {
				if (this.chunkMerger.isStreamingToolFunctionCall(chunk)) {
					isInsideTool.set(true);
				}
				return chunk;
			})
			// Group all chunks belonging to the same function call.
			// Flux<ChatCompletionChunk> -> Flux<Flux<ChatCompletionChunk>>
			.windowUntil(chunk -> {
				if (isInsideTool.get() && this.chunkMerger.isStreamingToolFunctionCallFinish(chunk)) {
					isInsideTool.set(false);
					return true;
				}
				return !isInsideTool.get();
			})
			// Merging the window chunks into a single chunk.
			// Reduce the inner Flux<ChatCompletionChunk> window into a single
			// Mono<ChatCompletionChunk>,
			// Flux<Flux<ChatCompletionChunk>> -> Flux<Mono<ChatCompletionChunk>>
			.concatMapIterable(window -> {
				Mono<ChatCompletionChunk> monoChunk = window.reduce(
						new ChatCompletionChunk(null, null, null, null, null, null),
						(previous, current) -> this.chunkMerger.merge(previous, current));
				return List.of(monoChunk);
			})
			// Flux<Mono<ChatCompletionChunk>> -> Flux<ChatCompletionChunk>
			.flatMap(mono -> mono);
	}

	/**
	 * Creates a model response for the given chat conversation.
	 * @param chatRequest The chat completion request.
	 * @return Entity response with {@link ChatCompletion} as a body and HTTP status code
	 * and headers.
	 */
	public ResponseEntity<ChatCompletion> chatCompletionV2Entity(ChatCompletionRequest chatRequest) {

		Assert.notNull(chatRequest, "The request body can not be null.");
		Assert.isTrue(!chatRequest.stream(), "Request must set the steam property to false.");

		return this.restClient.post()
			.uri("/v1/text/chatcompletion_v2")
			.body(chatRequest)
			.retrieve()
			.toEntity(ChatCompletion.class);
	}

	/**
	 * Creates an embedding vector representing the texts text or token array.
	 * @param embeddingRequest The embedding request.
	 * @return Returns list of {@link Embedding} wrapped in {@link EmbeddingList}.
	 * @param <T> Type of the entity in the vectors list. Can be a {@link String} or
	 * {@link List} of tokens (e.g. Integers). For embedding multiple inputs in a single
	 * request, You can pass a {@link List} of {@link String} or {@link List} of
	 * {@link List} of tokens. For example:
	 *
	 * <pre>{@code List.of("text1", "text2", "text3") or List.of(List.of(1, 2, 3), List.of(3, 4, 5))} </pre>
	 */
	public <T> ResponseEntity<EmbeddingList> embeddings(EmbeddingRequest<T> embeddingRequest) {

		Assert.notNull(embeddingRequest, "The request body can not be null.");

		// Input text to embed, encoded as a string or array of tokens. To embed multiple
		// inputs in a single
		// request, pass an array of strings or array of token arrays.
		Assert.notNull(embeddingRequest.texts(), "The texts can not be null.");
		Assert.isTrue(embeddingRequest.texts() instanceof String || embeddingRequest.texts() instanceof List,
				"The texts must be either a String, or a List of Strings or List of List of integers.");

		// The input must not an empty string, and any array must be 1024 dimensions or
		// less.
		if (embeddingRequest.texts() instanceof List list) {
			Assert.isTrue(!CollectionUtils.isEmpty(list), "The input list can not be empty.");
			Assert.isTrue(list.size() <= 1024, "The list must be 1024 dimensions or less");
			Assert.isTrue(list.get(0) instanceof String || list.get(0) instanceof List,
					"The input must be either a String, or a List of Strings.");
		}

		EmbeddingRequest finalRequest = embeddingRequest;
		if (embeddingRequest.texts() instanceof String) {
			finalRequest = new EmbeddingRequest(embeddingRequest.model(), List.of(embeddingRequest.texts()),
					embeddingRequest.type());
		}

		return this.restClient.post()
			.uri("/v1/embeddings?GroupId={group_id}")
			.body(finalRequest)
			.retrieve()
			.toEntity(new ParameterizedTypeReference<>() {
			});
	}

	public enum ChatModel {

		ABAB6_CHAT("abab6-chat"),

		ABAB5_5_CHAT("abab5.5-chat"),

		ABAB5_5S_CHAT("abab5.5s-chat");

		public final String value;

		ChatModel(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

	/**
	 * MiniMax Embeddings Models: <a href=
	 * "https://www.minimaxi.com/document/guides/Embeddings?id=6464722084cdc277dfaa966a">Embeddings</a>.
	 */
	public enum EmbeddingModel {

		EMBO_01("embo-01");

		public final String value;

		EmbeddingModel(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

	/**
	 * The reason the model stopped generating tokens.
	 */
	public enum ChatCompletionFinishReason {

		/**
		 * The model hit a natural stop point or a provided stop sequence.
		 */
		@JsonProperty("stop")
		STOP,
		/**
		 * The maximum number of tokens specified in the request was reached.
		 */
		@JsonProperty("length")
		LENGTH,
		/**
		 * The model called a tool.
		 */
		@JsonProperty("tool_calls")
		TOOL_CALLS,

	}

	/**
	 * Represents a tool the model may call. Currently, only functions are supported as a
	 * tool.
	 *
	 * @param type The type of the tool. Currently, only 'function' is supported.
	 * @param function The function definition.
	 */
	@JsonInclude(Include.NON_NULL)
	public record FunctionTool(@JsonProperty("type") Type type, @JsonProperty("function") Function function) {

		/**
		 * Create a tool of type 'function' and the given function definition.
		 * @param function function definition.
		 */
		@ConstructorBinding
		public FunctionTool(Function function) {
			this(Type.FUNCTION, function);
		}

		/**
		 * Create a tool of type 'function' and the given function definition.
		 */
		public enum Type {

			/**
			 * Function tool type.
			 */
			@JsonProperty("function")
			FUNCTION

		}

		/**
		 * Function definition.
		 *
		 * @param description A description of what the function does, used by the model
		 * to choose when and how to call the function.
		 * @param name The name of the function to be called. Must be a-z, A-Z, 0-9, or
		 * contain underscores and dashes, with a maximum length of 64.
		 * @param parameters The parameters the functions accepts, described as a JSON
		 * Schema object. To describe a function that accepts no parameters, provide the
		 * value {"type": "object", "properties": {}}.
		 */
		public record Function(
		// @formatter:off
				@JsonProperty("description") String description,
				@JsonProperty("name") String name,
				@JsonProperty("parameters") Map<String, Object> parameters) {
			// @formatter:on

			/**
			 * Create tool function definition.
			 * @param description tool function description.
			 * @param name tool function name.
			 * @param jsonSchema tool function schema as json.
			 */
			@ConstructorBinding
			public Function(String description, String name, String jsonSchema) {
				this(description, name, ModelOptionsUtils.jsonToMap(jsonSchema));
			}
		}
	}

	@JsonInclude(Include.NON_NULL)
	public record ChatCompletionRequest(
	// @formatter:off
			@JsonProperty("model") String model,
			@JsonProperty("stream") Boolean stream,
			@JsonProperty("max_tokens") Integer maxTokens,
			@JsonProperty("temperature") Float temperature,
			@JsonProperty("top_p") Float topP,
			@JsonProperty("messages") List<ChatCompletionMessage> messages,
			@JsonProperty("tool_choice") String toolChoice,
			@JsonProperty("tools") List<FunctionTool> tools) {
		// @formatter:on

		/**
		 * Shortcut constructor for a chat completion request with the given messages and
		 * model.
		 * @param messages The prompt(s) to generate completions for, encoded as a list of
		 * dict with role and content. The first prompt role should be user or system.
		 * @param model ID of the model to use.
		 */
		public ChatCompletionRequest(List<ChatCompletionMessage> messages, String model) {
			this(model, false, null, 0.7f, 1f, messages, null, null);
		}

		/**
		 * Shortcut constructor for a chat completion request with the given messages,
		 * model and temperature.
		 * @param messages The prompt(s) to generate completions for, encoded as a list of
		 * dict with role and content. The first prompt role should be user or system.
		 * @param model ID of the model to use.
		 * @param temperature What sampling temperature to use, between 0.0 and 1.0.
		 * @param stream Whether to stream back partial progress. If set, tokens will be
		 * sent
		 */
		public ChatCompletionRequest(List<ChatCompletionMessage> messages, String model, Float temperature,
				boolean stream) {
			this(model, stream, null, temperature, 1f, messages, null, null);
		}

		/**
		 * Shortcut constructor for a chat completion request with the given messages,
		 * model and temperature.
		 * @param messages The prompt(s) to generate completions for, encoded as a list of
		 * dict with role and content. The first prompt role should be user or system.
		 * @param model ID of the model to use.
		 * @param temperature What sampling temperature to use, between 0.0 and 1.0.
		 *
		 */
		public ChatCompletionRequest(List<ChatCompletionMessage> messages, String model, Float temperature) {
			this(model, false, null, temperature, 1f, messages, null, null);
		}

		/**
		 * Shortcut constructor for a chat completion request with the given messages,
		 * model, tools and tool choice. Streaming is set to false, temperature to 0.8 and
		 * all other parameters are null.
		 * @param messages A list of messages comprising the conversation so far.
		 * @param model ID of the model to use.
		 * @param tools A list of tools the model may call. Currently, only functions are
		 * supported as a tool.
		 * @param toolChoice Controls which (if any) function is called by the model.
		 */
		public ChatCompletionRequest(List<ChatCompletionMessage> messages, String model, List<FunctionTool> tools,
				String toolChoice) {
			this(model, false, null, 0.7f, 1f, messages, toolChoice, tools);
		}

		/**
		 * Shortcut constructor for a chat completion request with the given messages and
		 * stream.
		 */
		public ChatCompletionRequest(List<ChatCompletionMessage> messages, Boolean stream) {
			this(DEFAULT_CHAT_MODEL, stream, null, 0.7f, 1f, messages, null, null);
		}

		/**
		 * Helper factory that creates a tool_choice of type 'none', 'auto' or selected
		 * function by name.
		 */
		public static class ToolChoiceBuilder {

			/**
			 * Model can pick between generating a message or calling a function.
			 */
			public static final String AUTO = "auto";

			/**
			 * Model will not call a function and instead generates a message
			 */
			public static final String NONE = "none";

			/**
			 * Specifying a particular function forces the model to call that function.
			 */
			public static String FUNCTION(String functionName) {
				return ModelOptionsUtils
					.toJsonString(Map.of("type", "function", "function", Map.of("name", functionName)));
			}

		}

		/**
		 * An object specifying the format that the model must output.
		 *
		 * @param type Must be one of 'text' or 'json_object'.
		 */
		@JsonInclude(Include.NON_NULL)
		public record ResponseFormat(@JsonProperty("type") String type) {
		}
	}

	@JsonInclude(Include.NON_NULL)
	public record ChatCompletionMessage(@JsonProperty("content") Object rawContent, @JsonProperty("role") Role role,
			@JsonProperty("tool_calls") List<ToolCall> toolCalls) {

		/**
		 * Create a chat completion message with the given content and role. All other
		 * fields are null.
		 * @param content The contents of the message.
		 * @param role The role of the author of this message.
		 */
		public ChatCompletionMessage(Object content, Role role) {
			this(content, role, null);
		}

		/**
		 * Get message content as String.
		 */
		public String content() {
			if (this.rawContent == null) {
				return null;
			}
			if (this.rawContent instanceof String text) {
				return text;
			}
			throw new IllegalStateException("The content is not a string!");
		}

		/**
		 * The role of the author of this message.
		 */
		public enum Role {

			/**
			 * System message.
			 */
			@JsonProperty("system")
			SYSTEM,
			/**
			 * User message.
			 */
			@JsonProperty("user")
			USER,
			/**
			 * Assistant message.
			 */
			@JsonProperty("assistant")
			ASSISTANT,
			/**
			 * Tool message.
			 */
			@JsonProperty("tool")
			TOOL

		}

		/**
		 * The relevant tool call.
		 *
		 * @param id The ID of the tool call. This ID must be referenced when you submit
		 * the tool outputs in using the Submit tool outputs to run endpoint.
		 * @param type The type of tool call the output is required for. For now, this is
		 * always function.
		 * @param function The function definition.
		 */
		@JsonInclude(Include.NON_NULL)
		public record ToolCall(
		// @formatter:off
				@JsonProperty("id") String id,
				@JsonProperty("type") String type,
				@JsonProperty("function") ChatCompletionFunction function) {
			// @formatter:on
		}

		/**
		 * The function definition.
		 *
		 * @param name The name of the function.
		 * @param arguments The arguments that the model expects you to pass to the
		 * function.
		 */
		@JsonInclude(Include.NON_NULL)
		public record ChatCompletionFunction(
		// @formatter:off
				@JsonProperty("name") String name,
				@JsonProperty("arguments") String arguments) {
			// @formatter:on
		}
	}

	/**
	 * Represents a chat completion response returned by model, based on the provided
	 * texts.
	 *
	 * @param id A unique identifier for the chat completion.
	 * @param choices A list of chat completion choices. Can be more than one if n is
	 * greater than 1.
	 * @param created The Unix timestamp (in seconds) of when the chat completion was
	 * created.
	 * @param model The model used for the chat completion.
	 * @param systemFingerprint This fingerprint represents the backend configuration that
	 * the model runs with. Can be used in conjunction with the seed request parameter to
	 * understand when backend changes have been made that might impact determinism.
	 * @param object The object type, which is always chat.completion.
	 * @param usage Usage statistics for the completion request.
	 */
	@JsonInclude(Include.NON_NULL)
	public record ChatCompletion(
	// @formatter:off
			@JsonProperty("id") String id,
			@JsonProperty("choices") List<Choice> choices,
			@JsonProperty("created") Long created,
			@JsonProperty("model") String model,
			@JsonProperty("system_fingerprint") String systemFingerprint,
			@JsonProperty("object") String object,
			@JsonProperty("usage") Usage usage) {
		// @formatter:on

		/**
		 * Chat completion choice.
		 *
		 * @param finishReason The reason the model stopped generating tokens.
		 * @param index The index of the choice in the list of choices.
		 * @param message A chat completion message generated by the model.
		 */
		@JsonInclude(Include.NON_NULL)
		public record Choice(
		// @formatter:off
				@JsonProperty("finish_reason") ChatCompletionFinishReason finishReason,
				@JsonProperty("index") Integer index,
				@JsonProperty("message") ChatCompletionMessage message) {
			// @formatter:on

		}
	}

	// Embeddings API

	/**
	 * Usage statistics for the completion request.
	 *
	 * @param completionTokens Number of tokens in the generated completion. Only
	 * applicable for completion requests.
	 * @param promptTokens Number of tokens in the prompt.
	 * @param totalTokens Total number of tokens used in the request (prompt +
	 * completion).
	 */
	@JsonInclude(Include.NON_NULL)
	public record Usage(@JsonProperty("completion_tokens") Integer completionTokens,
			@JsonProperty("prompt_tokens") Integer promptTokens, @JsonProperty("total_tokens") Integer totalTokens) {

	}

	/**
	 * Represents a streamed chunk of a chat completion response returned by model, based
	 * on the provided texts.
	 *
	 * @param id A unique identifier for the chat completion. Each chunk has the same ID.
	 * @param choices A list of chat completion choices. Can be more than one if n is
	 * greater than 1.
	 * @param created The Unix timestamp (in seconds) of when the chat completion was
	 * created. Each chunk has the same timestamp.
	 * @param model The model used for the chat completion.
	 * @param systemFingerprint This fingerprint represents the backend configuration that
	 * the model runs with. Can be used in conjunction with the seed request parameter to
	 * understand when backend changes have been made that might impact determinism.
	 * @param object The object type, which is always 'chat.completion.chunk'.
	 */
	@JsonInclude(Include.NON_NULL)
	public record ChatCompletionChunk(
	// @formatter:off
			@JsonProperty("id") String id,
			@JsonProperty("choices") List<ChunkChoice> choices,
			@JsonProperty("created") Long created,
			@JsonProperty("model") String model,
			@JsonProperty("system_fingerprint") String systemFingerprint,
			@JsonProperty("object") String object) {
			// @formatter:on

		/**
		 * Chat completion choice.
		 *
		 * @param finishReason The reason the model stopped generating tokens.
		 * @param index The index of the choice in the list of choices.
		 * @param delta A chat completion delta generated by streamed model responses.
		 */
		@JsonInclude(Include.NON_NULL)
		public record ChunkChoice(
		// @formatter:off
				@JsonProperty("finish_reason") ChatCompletionFinishReason finishReason,
				@JsonProperty("index") Integer index,
				@JsonProperty("delta") ChatCompletionMessage delta) {
				// @formatter:on
		}
	}

	/**
	 * Represents an embedding vector returned by embedding endpoint.
	 *
	 * @param index The index of the embedding in the list of embeddings.
	 * @param embedding The embedding vector, which is a list of floats. The length of
	 * vector depends on the model.
	 * @param object The object type, which is always 'embedding'.
	 */
	@JsonInclude(Include.NON_NULL)
	public record Embedding(
	// @formatter:off
			@JsonProperty("index") Integer index,
			@JsonProperty("embedding") List<Double> embedding,
			@JsonProperty("object") String object) {
			// @formatter:on

		/**
		 * Create an embedding with the given index, embedding and object type set to
		 * 'embedding'.
		 * @param index The index of the embedding in the list of embeddings.
		 * @param embedding The embedding vector, which is a list of floats. The length of
		 * vector depends on the model.
		 */
		public Embedding(Integer index, List<Double> embedding) {
			this(index, embedding, "embedding");
		}
	}

	/**
	 * Creates an embedding vector representing the texts text.
	 *
	 * @param texts 长度上限为4096个token
	 * @param model ID of the model to use.
	 * @param type db，用于构建向量库，生成向量存储到库中（作为被检索文本） query，用于生成查询用的向量（作为检索文本时）
	 */
	@JsonInclude(Include.NON_NULL)
	public record EmbeddingRequest<T>(
	// @formatter:off
			@JsonProperty("model") String model,
			@JsonProperty("texts") T texts,
			@JsonProperty("type") EmbeddingType type) {
			// @formatter:on

		/**
		 * Create an embedding request with the given texts, model and encoding format set
		 * to float.
		 * @param texts Input text to embed.
		 * @param model ID of the model to use.
		 */
		public EmbeddingRequest(T texts, String model) {
			this(model, texts, EmbeddingType.DB);
		}

		/**
		 * Create an embedding request with the given texts. Encoding format is set to
		 * float and user is null and the model is set to 'text-embedding-ada-002'.
		 * @param texts Input text to embed.
		 */
		public EmbeddingRequest(T texts) {
			this(DEFAULT_EMBEDDING_MODEL, texts, EmbeddingType.DB);
		}

		/**
		 * Create an embedding request with the given texts. Encoding format is set to
		 * float and user is null and the model is set to 'text-embedding-ada-002'.
		 * @param texts Input text to embed.
		 * @param type db，用于构建向量库，生成向量存储到库中（作为被检索文本） query，用于生成查询用的向量（作为检索文本时）
		 */
		public EmbeddingRequest(T texts, EmbeddingType type) {
			this(DEFAULT_EMBEDDING_MODEL, texts, type);
		}

		public enum EmbeddingType {

			@JsonProperty("db")
			DB,

			@JsonProperty("query")
			QUERY

		}
	}

	/**
	 * List of multiple embedding responses.
	 *
	 * @param vectors 一个文本对应一个float32数组，长度为1536.
	 * @param totalTokens 消耗的token数量.
	 * @param baseResp 如果请求出错，对应的错误状态码和详情.
	 */
	@JsonInclude(Include.NON_NULL)
	public record EmbeddingList(
	// @formatter:off
			@JsonProperty("vectors") List<List<Float>> vectors,
			@JsonProperty("total_tokens") Integer totalTokens,
			@JsonProperty("base_resp") Object baseResp) {
			// @formatter:on
	}

}
