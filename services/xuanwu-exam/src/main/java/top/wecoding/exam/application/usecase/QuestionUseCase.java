package top.wecoding.exam.application.usecase;

import top.wecoding.exam.domain.question.Question;

import java.util.Optional;

/**
 * Use Case interface for Question management. Defines the high-level business operations
 * available for Questions.
 */
public interface QuestionUseCase {

    /**
     * Creates a new question.
     * @param question the question to be created
     * @return the created question with its new ID
     */
    Question createQuestion(Question question);

    /**
     * Retrieves a question by its unique identifier.
     * @param id the unique identifier of the question
     * @return an Optional containing the question if found, or empty if not
     */
    Optional<Question> getQuestion(Long id);

}
