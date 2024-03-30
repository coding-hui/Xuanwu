package top.wecoding.xuanwu.ai.minimax.testutils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * @author wecoding
 * @since 0.10
 */
@Slf4j
public abstract class AbstractTest {

	@Autowired
	protected ChatClient chatClient;

	@Autowired
	protected StreamingChatClient streamingChatClient;

	@Value("classpath:/prompts/eval/qa-evaluator-accurate-answer.st")
	protected Resource qaEvaluatorAccurateAnswerResource;

	@Value("classpath:/prompts/eval/qa-evaluator-not-related-message.st")
	protected Resource qaEvaluatorNotRelatedResource;

	@Value("classpath:/prompts/eval/qa-evaluator-fact-based-answer.st")
	protected Resource qaEvaluatorFactBasedAnswerResource;

	@Value("classpath:/prompts/eval/user-evaluator-message.st")
	protected Resource userEvaluatorResource;

	protected void evaluateQuestionAndAnswer(String question, ChatResponse response, boolean factBased) {
		assertThat(response).isNotNull();
		String answer = response.getResult().getOutput().getContent();
		log.info("Question: " + question);
		log.info("Answer:" + answer);
		PromptTemplate userPromptTemplate = new PromptTemplate(userEvaluatorResource,
				Map.of("question", question, "answer", answer));
		SystemMessage systemMessage;
		if (factBased) {
			systemMessage = new SystemMessage(qaEvaluatorFactBasedAnswerResource);
		}
		else {
			systemMessage = new SystemMessage(qaEvaluatorAccurateAnswerResource);
		}
		Message userMessage = userPromptTemplate.createMessage();
		Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
		String yesOrNo = chatClient.call(prompt).getResult().getOutput().getContent();
		log.info("Is Answer related to question: " + yesOrNo);
		if (yesOrNo.equalsIgnoreCase("no")) {
			SystemMessage notRelatedSystemMessage = new SystemMessage(qaEvaluatorNotRelatedResource);
			prompt = new Prompt(List.of(userMessage, notRelatedSystemMessage));
			String reasonForFailure = chatClient.call(prompt).getResult().getOutput().getContent();
			fail(reasonForFailure);
		}
		else {
			log.info("Answer is related to question.");
			assertThat(yesOrNo).isEqualTo("YES");
		}
	}

}
