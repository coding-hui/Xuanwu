package top.wecoding.xuanwu.ai.minimax;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.AbstractFunctionCallSupport;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletion;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletion.Choice;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletionChunk;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletionFinishReason;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletionMessage;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletionMessage.Role;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletionMessage.ToolCall;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletionRequest;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.FunctionTool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.DEFAULT_CHAT_MODEL;

/**
 * @author wecoding
 * @since 0.10
 */
@Slf4j
public class MiniMaxAiChatClient extends
		AbstractFunctionCallSupport<ChatCompletionMessage, ChatCompletionRequest, ResponseEntity<ChatCompletion>>
		implements ChatClient, StreamingChatClient {

	/**
	 * The retry template used to retry the MiniMax API calls.
	 */
	public final RetryTemplate retryTemplate;

	/**
	 * Low-level access to the MiniMax API.
	 */
	private final MiniMaxAiApi miniMaxAiApi;

	/**
	 * The default options used for the chat completion requests.
	 */
	private MiniMaxAiChatOptions defaultOptions;

	/**
	 * Creates an instance of the MiniMaxAiChatClient.
	 * @param miniMaxAiApi The MiniMaxAiApi instance to be used for interacting with the
	 * MiniMax Chat API.
	 * @throws IllegalArgumentException if miniMaxAiApi is null
	 */
	public MiniMaxAiChatClient(MiniMaxAiApi miniMaxAiApi) {
		this(miniMaxAiApi, MiniMaxAiChatOptions.builder().model(DEFAULT_CHAT_MODEL).temperature(0.7f).build());
	}

	/**
	 * Initializes an instance of the MiniMaxAiChatClient.
	 * @param miniMaxAiApi The MiniMaxAiApi instance to be used for interacting with the
	 * MiniMax Chat API.
	 * @param options The MiniMaxAiChatOptions to configure the chat client.
	 */
	public MiniMaxAiChatClient(MiniMaxAiApi miniMaxAiApi, MiniMaxAiChatOptions options) {
		this(miniMaxAiApi, options, null, RetryUtils.DEFAULT_RETRY_TEMPLATE);
	}

	/**
	 * Initializes an instance of the MiniMaxAiChatClient.
	 * @param miniMaxAiApi The MiniMaxAiApi instance to be used for interacting with the
	 * MiniMax Chat API.
	 * @param options The MiniMaxAiChatOptions to configure the chat client.
	 * @param functionCallbackContext The function callback context.
	 * @param retryTemplate The retry template.
	 */
	public MiniMaxAiChatClient(MiniMaxAiApi miniMaxAiApi, MiniMaxAiChatOptions options,
			FunctionCallbackContext functionCallbackContext, RetryTemplate retryTemplate) {
		super(functionCallbackContext);
		Assert.notNull(miniMaxAiApi, "MiniMaxAiApi must not be null");
		Assert.notNull(options, "Options must not be null");
		Assert.notNull(retryTemplate, "RetryTemplate must not be null");
		this.miniMaxAiApi = miniMaxAiApi;
		this.defaultOptions = options;
		this.retryTemplate = retryTemplate;
	}

	@Override
	public ChatResponse call(Prompt prompt) {

		ChatCompletionRequest request = createRequest(prompt, false);

		return this.retryTemplate.execute(ctx -> {

			ResponseEntity<ChatCompletion> completionEntity = this.callWithFunctionSupport(request);

			var chatCompletion = completionEntity.getBody();
			if (chatCompletion == null) {
				log.warn("No chat completion returned for prompt: {}", prompt);
				return new ChatResponse(List.of());
			}

			// RateLimit rateLimits =
			// OpenAiResponseHeaderExtractor.extractAiResponseHeaders(completionEntity);

			List<Generation> generations = chatCompletion.choices()
				.stream()
				.map(choice -> new Generation(choice.message().content(), toMap(chatCompletion.id(), choice))
					.withGenerationMetadata(ChatGenerationMetadata.from(choice.finishReason().name(), null)))
				.toList();

			return new ChatResponse(generations);
		});
	}

	private Map<String, Object> toMap(String id, ChatCompletion.Choice choice) {
		Map<String, Object> map = new HashMap<>();

		var message = choice.message();
		if (message.role() != null) {
			map.put("role", message.role().name());
		}
		if (choice.finishReason() != null) {
			map.put("finishReason", choice.finishReason().name());
		}
		map.put("id", id);
		return map;
	}

	@Override
	public Flux<ChatResponse> stream(Prompt prompt) {

		ChatCompletionRequest request = createRequest(prompt, true);

		return this.retryTemplate.execute(ctx -> {

			Flux<ChatCompletionChunk> completionChunks = this.miniMaxAiApi.chatCompletionV2Stream(request);

			// For chunked responses, only the first chunk contains the choice role.
			// The rest of the chunks with same ID share the same role.
			ConcurrentHashMap<String, String> roleMap = new ConcurrentHashMap<>();

			// Convert the ChatCompletionChunk into a ChatCompletion to be able to reuse
			// the function call handling logic.
			return completionChunks.map(this::chunkToChatCompletion).map(chatCompletion -> {
				try {
					chatCompletion = handleFunctionCallOrReturn(request, ResponseEntity.of(Optional.of(chatCompletion)))
						.getBody();

					@SuppressWarnings("null")
					String id = chatCompletion.id();

					List<Generation> generations = chatCompletion.choices().stream().map(choice -> {
						if (choice.message() != null && choice.message().role() != null) {
							roleMap.putIfAbsent(id, choice.message().role().name());
						}
						String finish = (choice.finishReason() != null ? choice.finishReason().name() : "");
						String content = (choice.message() != null ? choice.message().content() : "");
						var generation = new Generation(content,
								Map.of("id", id, "role", roleMap.get(id), "finishReason", finish));
						if (choice.finishReason() != null) {
							generation = generation.withGenerationMetadata(
									ChatGenerationMetadata.from(choice.finishReason().name(), null));
						}
						return generation;
					}).toList();

					return new ChatResponse(generations);
				}
				catch (Exception e) {
					log.error("Error processing chat completion", e);
					return new ChatResponse(List.of());
				}

			});
		});
	}

	/**
	 * Convert the ChatCompletionChunk into a ChatCompletion. The Usage is set to null.
	 * @param chunk the ChatCompletionChunk to convert
	 * @return the ChatCompletion
	 */
	private ChatCompletion chunkToChatCompletion(ChatCompletionChunk chunk) {
		List<Choice> choices = chunk.choices()
			.stream()
			.map(cc -> new Choice(cc.finishReason(), cc.index(), cc.delta()))
			.toList();

		return new ChatCompletion(chunk.id(), choices, chunk.created(), chunk.model(), chunk.systemFingerprint(),
				"chat.completion", null);
	}

	/**
	 * Accessible for testing.
	 */
	ChatCompletionRequest createRequest(Prompt prompt, boolean stream) {

		Set<String> functionsForThisRequest = new HashSet<>();

		List<ChatCompletionMessage> chatCompletionMessages = prompt.getInstructions()
			.stream()
			.map(m -> new ChatCompletionMessage(m.getContent(),
					ChatCompletionMessage.Role.valueOf(m.getMessageType().name())))
			.toList();

		ChatCompletionRequest request = new ChatCompletionRequest(chatCompletionMessages, stream);

		if (prompt.getOptions() != null) {
			if (prompt.getOptions() instanceof ChatOptions runtimeOptions) {
				MiniMaxAiChatOptions updatedRuntimeOptions = ModelOptionsUtils.copyToTarget(runtimeOptions,
						ChatOptions.class, MiniMaxAiChatOptions.class);

				Set<String> promptEnabledFunctions = this.handleFunctionCallbackConfigurations(updatedRuntimeOptions,
						IS_RUNTIME_CALL);
				functionsForThisRequest.addAll(promptEnabledFunctions);

				request = ModelOptionsUtils.merge(updatedRuntimeOptions, request, ChatCompletionRequest.class);
			}
			else {
				throw new IllegalArgumentException("Prompt options are not of type ChatOptions: "
						+ prompt.getOptions().getClass().getSimpleName());
			}
		}

		if (this.defaultOptions != null) {

			Set<String> defaultEnabledFunctions = this.handleFunctionCallbackConfigurations(this.defaultOptions,
					!IS_RUNTIME_CALL);

			functionsForThisRequest.addAll(defaultEnabledFunctions);

			request = ModelOptionsUtils.merge(request, this.defaultOptions, ChatCompletionRequest.class);
		}

		// Add the enabled functions definitions to the request's tools parameter.
		if (!CollectionUtils.isEmpty(functionsForThisRequest)) {

			request = ModelOptionsUtils.merge(
					MiniMaxAiChatOptions.builder().tools(this.getFunctionTools(functionsForThisRequest)).build(),
					request, ChatCompletionRequest.class);
		}

		return request;
	}

	private List<FunctionTool> getFunctionTools(Set<String> functionNames) {
		return this.resolveFunctionCallbacks(functionNames).stream().map(functionCallback -> {
			var function = new FunctionTool.Function(functionCallback.getDescription(), functionCallback.getName(),
					functionCallback.getInputTypeSchema());
			return new FunctionTool(function);
		}).toList();
	}

	@Override
	protected ChatCompletionRequest doCreateToolResponseRequest(ChatCompletionRequest previousRequest,
			ChatCompletionMessage responseMessage, List<ChatCompletionMessage> conversationHistory) {

		// Every tool-call item requires a separate function call and a response (TOOL)
		// message.
		for (ToolCall toolCall : responseMessage.toolCalls()) {

			var functionName = toolCall.function().name();
			String functionArguments = toolCall.function().arguments();

			if (!this.functionCallbackRegister.containsKey(functionName)) {
				throw new IllegalStateException("No function callback found for function name: " + functionName);
			}

			String functionResponse = this.functionCallbackRegister.get(functionName).call(functionArguments);

			// Add the function response to the conversation.
			conversationHistory.add(new ChatCompletionMessage(functionResponse, Role.TOOL, List.of(toolCall)));
		}

		// Recursively call chatCompletionWithTools until the model doesn't call a
		// functions anymore.
		ChatCompletionRequest newRequest = new ChatCompletionRequest(conversationHistory, false);
		newRequest = ModelOptionsUtils.merge(newRequest, previousRequest, ChatCompletionRequest.class);

		return newRequest;
	}

	@Override
	protected List<ChatCompletionMessage> doGetUserMessages(ChatCompletionRequest request) {
		return request.messages();
	}

	@Override
	protected ChatCompletionMessage doGetToolResponseMessage(ResponseEntity<ChatCompletion> response) {
		return response.getBody().choices().iterator().next().message();
	}

	@Override
	protected ResponseEntity<ChatCompletion> doChatCompletion(ChatCompletionRequest request) {
		return this.miniMaxAiApi.chatCompletionV2Entity(request);
	}

	@Override
	protected boolean isToolFunctionCall(ResponseEntity<ChatCompletion> chatCompletion) {
		var body = chatCompletion.getBody();
		if (body == null) {
			return false;
		}

		var choices = body.choices();
		if (CollectionUtils.isEmpty(choices)) {
			return false;
		}

		var choice = choices.get(0);
		return choice.message() != null && !CollectionUtils.isEmpty(choice.message().toolCalls())
				&& choice.finishReason() == ChatCompletionFinishReason.TOOL_CALLS;
	}

}
