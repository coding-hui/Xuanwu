package top.wecoding.xuanwu.ai.minimax;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi;

/**
 * @author wecoding
 * @since 0.10
 */
@SpringBootConfiguration
public class MiniMaxAiTestConfiguration {

	@Bean
	public MiniMaxAiApi miniMaxAiApi() {
		return new MiniMaxAiApi();
	}

	@Bean
	public MiniMaxAiChatClient miniMaxAiChatClient(MiniMaxAiApi miniMaxAiApi) {
		return new MiniMaxAiChatClient(miniMaxAiApi);
	}

}
