package top.wecoding.xuanwu.ai.minimax.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletion;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletionChunk;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletionMessage;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletionMessage.Role;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatCompletionRequest;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.EmbeddingList;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.EmbeddingRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.ChatModel.ABAB5_5S_CHAT;

/**
 * @author wecoding
 * @since 0.10
 */
class MiniMaxAiApiTest {

	MiniMaxAiApi miniMaxAiApi = new MiniMaxAiApi();

	@Test
	void chatCompletionV2Entity() {
		ChatCompletionMessage chatCompletionMessage = new ChatCompletionMessage("Hello world", Role.USER);
		ResponseEntity<ChatCompletion> response = miniMaxAiApi.chatCompletionV2Entity(
				new ChatCompletionRequest(List.of(chatCompletionMessage), ABAB5_5S_CHAT.getValue(), 0.8f, false));

		assertThat(response).isNotNull();
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().choices()).isNotNull();
	}

	@Test
	void chatCompletionStream() {
		ChatCompletionMessage chatCompletionMessage = new ChatCompletionMessage("Hello world", Role.USER);
		Flux<ChatCompletionChunk> response = miniMaxAiApi.chatCompletionV2Stream(
				new ChatCompletionRequest(List.of(chatCompletionMessage), ABAB5_5S_CHAT.getValue(), 0.8f, true));

		assertThat(response).isNotNull();
		assertThat(response.collectList().block()).isNotNull();
	}

	@Test
	void embeddings() {
		ResponseEntity<EmbeddingList> response = miniMaxAiApi
			.embeddings(new EmbeddingRequest<>(List.of("Hello World", "Hi"), EmbeddingRequest.EmbeddingType.QUERY));

		assertThat(response).isNotNull();
		assertThat(response.getBody().vectors()).isNotNull();
		assertThat(response.getBody().vectors()).hasSize(2);
		assertThat(response.getBody().vectors().get(0)).hasSize(1536);
	}

}