package top.wecoding.exam.api.controller.question;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.wecoding.exam.api.converter.QuestionAssembler;
import top.wecoding.exam.api.dto.question.CreateQuestionRequest;
import top.wecoding.exam.api.dto.question.QuestionResponse;
import top.wecoding.exam.api.dto.question.UpdateQuestionRequest;
import top.wecoding.exam.application.usecase.QuestionUseCase;
import top.wecoding.exam.domain.exception.ExamErrorCode;
import top.wecoding.exam.domain.question.Question;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.core.exception.BaseUncheckedException;

/**
 * Question API Controller. Handles HTTP requests for Question management and delegates to the use
 * case.
 */
@RequiredArgsConstructor
@RestController("questionController.v1")
@RequestMapping(value = "/questions")
public class QuestionController {

  private final QuestionUseCase questionUseCase;

  private final QuestionAssembler questionAssembler;

  /**
   * Creates a new question.
   *
   * @param req the request body containing question details
   * @return the created question response
   */
  @PostMapping(version = "1")
  public R<QuestionResponse> createQuestion(@RequestBody @Validated CreateQuestionRequest req) {
    Question q = questionAssembler.toDomain(req);
    return R.ok(questionAssembler.toResponse(questionUseCase.createQuestion(q)));
  }

  /**
   * Updates an existing question.
   *
   * @param id the ID of the question to update
   * @param req the request body containing question details
   * @return the updated question response
   */
  @PutMapping(value = "/{id}", version = "1")
  public R<QuestionResponse> updateQuestion(
      @PathVariable Long id, @RequestBody @Validated UpdateQuestionRequest req) {
    req.setId(id);
    Question q = questionAssembler.toDomain(req);
    return R.ok(questionAssembler.toResponse(questionUseCase.updateQuestion(q)));
  }

  /**
   * Retrieves a question by its ID.
   *
   * @param id the ID of the question
   * @return the question response if found, or 404 Not Found
   */
  @GetMapping(value = "/{id}", version = "1")
  public R<QuestionResponse> getQuestion(@PathVariable Long id) {
    return questionUseCase
        .getQuestion(id)
        .map(q -> R.ok(questionAssembler.toResponse(q)))
        .orElseThrow(() -> new BaseUncheckedException(ExamErrorCode.QUESTION_NOT_FOUND));
  }

  /**
   * Batch delete questions.
   *
   * @param ids the list of question IDs to delete
   * @return success response
   */
  @DeleteMapping(version = "1")
  public R<Void> batchDelete(@RequestParam("ids") List<Long> ids) {
    questionUseCase.batchDeleteQuestions(ids);
    return R.ok();
  }
}
