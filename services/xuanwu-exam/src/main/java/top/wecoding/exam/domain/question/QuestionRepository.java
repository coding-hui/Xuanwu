package top.wecoding.exam.domain.question;

import java.util.Optional;

/**
 * Question Repository Interface
 */
public interface QuestionRepository {

    /**
     * Save a question
     */
    Question save(Question question);

    /**
     * Find a question by ID
     */
    Optional<Question> findById(Long id);

    /**
     * Delete a question by ID
     */
    void deleteById(Long id);

    /**
     * Batch delete questions by IDs
     */
    void batchDelete(Iterable<Long> ids);

}
