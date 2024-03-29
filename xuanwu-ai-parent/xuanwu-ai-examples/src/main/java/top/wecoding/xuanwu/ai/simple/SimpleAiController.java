package top.wecoding.xuanwu.ai.simple;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
public class SimpleAiController {

	private final ChatClient chatClient;

	@Autowired
	public SimpleAiController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/ai/simple")
	public Map<String, String> completion(
			@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		SystemMessage system = new SystemMessage("扮演一位二次元小助手，始终以二次元的语气回答用户的问题！");
		UserMessage userMessage = new UserMessage(message);
		Prompt prompt = new Prompt(Arrays.asList(system, userMessage));
		return Map.of("generation", chatClient.call(prompt).getResult().getOutput().getContent());
	}

}
