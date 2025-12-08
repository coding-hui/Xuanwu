package top.wecoding.exam.infrastructure.persistence.question;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import top.wecoding.exam.domain.exception.ExamErrorCode;
import top.wecoding.exam.domain.question.Question;
import top.wecoding.exam.domain.question.QuestionRepository;
import top.wecoding.exam.infrastructure.persistence.question.converter.QuestionConverter;
import top.wecoding.exam.infrastructure.persistence.question.mapper.QuestionMapper;
import top.wecoding.exam.infrastructure.persistence.question.po.QuestionPO;
import top.wecoding.xuanwu.core.exception.ServerException;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;

/** MyBatis implementation of QuestionRepository */
@Primary
@Repository
@RequiredArgsConstructor
public class MyBatisQuestionRepository implements QuestionRepository {

  private final QuestionMapper questionMapper;

  private final QuestionConverter questionConverter;

  @Override
  public Question save(Question question) {
    if (question.getId() != null) {
      return this.update(question);
    }
    QuestionPO po = questionConverter.toPO(question);
    po.setCreatedAt(LocalDateTime.now());
    po.setUpdatedAt(LocalDateTime.now());
    int rows = questionMapper.insert(po);
    if (rows == 0) {
      throw new ServerException(SystemErrorCode.DATABASE_ERROR);
    }
    question.setId(po.getId());
    return questionConverter.toDomain(po);
  }

  @Override
  public Question update(Question question) {
    QuestionPO po = questionConverter.toPO(question);
    po.setUpdatedAt(LocalDateTime.now());
    int rows = questionMapper.update(po);
    if (rows == 0) {
      throw new ServerException(ExamErrorCode.QUESTION_NOT_FOUND);
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
