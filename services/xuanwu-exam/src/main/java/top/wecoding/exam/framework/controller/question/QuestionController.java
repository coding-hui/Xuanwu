package top.wecoding.exam.framework.controller.question;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.exam.application.usecase.QuestionUseCase;
import top.wecoding.exam.domain.question.Question;
import top.wecoding.exam.framework.dto.question.CreateQuestionRequest;
import top.wecoding.exam.framework.dto.question.QuestionResponse;
import top.wecoding.xuanwu.core.base.R;

/**
 * Question API Controller. Handles HTTP requests for Question management and delegates to
 * the use case.
 */
@RequiredArgsConstructor
@RestController("questionController.v1")
@RequestMapping(value = "/questions")
public class QuestionController {

    private final QuestionUseCase questionUseCase;

    /**
     * Creates a new question.
     * @param req the request body containing question details
     * @return the created question response
     */
    @PostMapping(version = "1")
    public R<QuestionResponse> createQuestion(@RequestBody @Validated CreateQuestionRequest req) {
        Question q = new Question(null, req.getContent(), req.getType(), req.getDifficulty(), req.getScore(), null,
                null);
        Question saved = questionUseCase.createQuestion(q);
        return R.ok(QuestionResponse.from(saved));
    }

    /**
     * Retrieves a question by its ID.
     * @param id the ID of the question
     * @return the question response if found, or 404 Not Found
     */
    @GetMapping(value = "/{id}", version = "2")
    public R<QuestionResponse> getQuestion(@PathVariable Long id) {
        return questionUseCase.getQuestion(id)
            .map(q -> R.ok(QuestionResponse.from(q)))
            .orElse(R.error("Question not found"));
    }

}
