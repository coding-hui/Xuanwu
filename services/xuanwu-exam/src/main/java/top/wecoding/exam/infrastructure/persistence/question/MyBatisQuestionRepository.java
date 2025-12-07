package top.wecoding.exam.infrastructure.persistence.question;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import top.wecoding.exam.domain.question.Question;
import top.wecoding.exam.domain.question.QuestionRepository;
import top.wecoding.exam.infrastructure.persistence.question.converter.QuestionConverter;
import top.wecoding.exam.infrastructure.persistence.question.mapper.QuestionMapper;
import top.wecoding.exam.infrastructure.persistence.question.po.QuestionPO;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * MyBatis implementation of QuestionRepository
 */
@Primary
@Repository
@RequiredArgsConstructor
public class MyBatisQuestionRepository implements QuestionRepository {

    private final QuestionMapper questionMapper;
    private final QuestionConverter questionConverter;

    @Override
    public Question save(Question question) {
        QuestionPO po = questionConverter.toPO(question);
        if (po.getId() == null) {
            po.setCreatedAt(LocalDateTime.now());
            po.setUpdatedAt(LocalDateTime.now());
            questionMapper.insert(po);
            question.setId(po.getId());
        } else {
            po.setUpdatedAt(LocalDateTime.now());
            questionMapper.update(po);
        }
        return questionConverter.toDomain(po);
    }

    @Override
    public Optional<Question> findById(Long id) {
        return questionMapper.selectById(id).map(questionConverter::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        questionMapper.deleteById(id);
    }

    @Override
    public void batchDelete(Iterable<Long> ids) {
        if (ids != null && ids.iterator().hasNext()) {
            questionMapper.deleteByIds(ids);
        }
    }
}
