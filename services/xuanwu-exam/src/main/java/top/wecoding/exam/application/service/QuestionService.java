package top.wecoding.exam.application.service;

import lombok.RequiredArgsConstructor;
import top.wecoding.exam.application.usecase.QuestionUseCase;
import top.wecoding.exam.domain.question.Question;
import top.wecoding.exam.domain.question.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for Question use cases. Implements the high-level business logic
 * defined in the use case interface.
 */
@Service
@RequiredArgsConstructor
public class QuestionService implements QuestionUseCase {

    private final QuestionRepository questionRepository;

    /**
     * Creates a new question.
     * @param question the question to create
     * @return the created question with generated ID
     */
    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    /**
     * Retrieves a question by its ID.
     * @param id the ID of the question
     * @return an Optional containing the question if found, or empty otherwise
     */
    @Override
    public Optional<Question> getQuestion(Long id) {
        return questionRepository.findById(id);
    }

}
