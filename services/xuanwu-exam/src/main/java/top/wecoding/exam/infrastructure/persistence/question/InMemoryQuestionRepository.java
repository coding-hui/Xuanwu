package top.wecoding.exam.infrastructure.persistence.question;

import top.wecoding.exam.domain.question.Question;
import top.wecoding.exam.domain.question.QuestionRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-Memory Question Repository Implementation
 */
@Repository
public class InMemoryQuestionRepository implements QuestionRepository {

    private final Map<Long, Question> store = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public Question save(Question question) {
        if (question.getId() == null) {
            question.setId(idGenerator.incrementAndGet());
        }
        store.put(question.getId(), question);
        return question;
    }

    @Override
    public Optional<Question> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

}
