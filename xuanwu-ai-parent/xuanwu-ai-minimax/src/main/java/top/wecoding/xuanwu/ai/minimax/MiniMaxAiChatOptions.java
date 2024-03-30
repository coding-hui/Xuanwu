package top.wecoding.xuanwu.ai.minimax;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallingOptions;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import top.wecoding.xuanwu.ai.minimax.api.MiniMaxAiApi.FunctionTool;

import java.util.List;
import java.util.Set;

/**
 * @author wecoding
 * @since 0.10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class MiniMaxAiChatOptions implements FunctionCallingOptions, ChatOptions {

	/**
	 * ID of the model to use.
	 */
	@JsonProperty("model")
	private String model;

	/**
	 * The maximum number of tokens to generate in the chat completion. The total length
	 * of texts tokens and generated tokens is limited by the model's context length.
	 */
	@JsonProperty("max_tokens")
	private Integer maxTokens;

	/**
	 * What sampling temperature to use, between 0 and 1. Higher values like 0.8 will make
	 * the output more random, while lower values like 0.2 will make it more focused and
	 * deterministic. We generally recommend altering this or top_p but not both.
	 */
	@JsonProperty("temperature")
	private Float temperature;

	/**
	 * An alternative to sampling with temperature, called nucleus sampling, where the
	 * model considers the results of the tokens with top_p probability mass. So 0.1 means
	 * only the tokens comprising the top 10% probability mass are considered. We
	 * generally recommend altering this or temperature but not both.
	 */
	@JsonProperty("top_p")
	private Float topP;

	/**
	 * A list of tools the model may call. Currently, only functions are supported as a
	 * tool. Use this to provide a list of functions the model may generate JSON inputs
	 * for.
	 */
	@JsonProperty("tools")
	@NestedConfigurationProperty
	private List<FunctionTool> tools;

	/**
	 * Controls which (if any) function is called by the model. none means the model will
	 * not call a function and instead generates a message. auto means the model can pick
	 * between generating a message or calling a function. Specifying a particular
	 * function via {"type: "function", "function": {"name": "my_function"}} forces the
	 * model to call that function. none is the default when no functions are present.
	 * auto is the default if functions are present. Use the {@link ToolChoiceBuilder} to
	 * create a tool choice object.
	 */
	@JsonProperty("tool_choice")
	private String toolChoice;

	@Override
	public List<FunctionCallback> getFunctionCallbacks() {
		return null;
	}

	@Override
	public void setFunctionCallbacks(List<FunctionCallback> functionCallbacks) {

	}

	@Override
	public Set<String> getFunctions() {
		return null;
	}

	@Override
	public void setFunctions(Set<String> functions) {

	}

	@Override
	@JsonIgnore
	public Integer getTopK() {
		throw new UnsupportedOperationException("Unimplemented method 'getTopK'");
	}

	@JsonIgnore
	public void setTopK(Integer topK) {
		throw new UnsupportedOperationException("Unimplemented method 'setTopK'");
	}

}
