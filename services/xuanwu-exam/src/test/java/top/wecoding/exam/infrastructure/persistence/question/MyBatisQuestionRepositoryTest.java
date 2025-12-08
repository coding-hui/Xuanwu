package top.wecoding.exam.infrastructure.persistence.question;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import top.wecoding.exam.domain.question.Question;
import top.wecoding.exam.domain.question.QuestionRepository;
import top.wecoding.exam.domain.question.QuestionStatus;
import top.wecoding.exam.domain.question.QuestionType;
import top.wecoding.xuanwu.core.exception.ServerException;

@SpringBootTest
@Transactional
class MyBatisQuestionRepositoryTest {

  @Autowired private QuestionRepository questionRepository;

  @Test
  void testSaveAndFind() {
    // 1. Create a new question
    Question question = new Question();
    question.setTitle("Test Question for MyBatis");
    question.setContent("This is a test content.");
    question.setType(QuestionType.SINGLE_CHOICE);
    question.setDifficulty(1);
    question.setScore(5);
    question.setStatus(QuestionStatus.DRAFT);
    question.setMetaInfo("{\"options\": [\"A\", \"B\"]}");
    question.setTags("test,unit");
    question.setCreatedAt(LocalDateTime.now());
    question.setUpdatedAt(LocalDateTime.now());

    // 2. Save
    Question savedQuestion = questionRepository.save(question);
    Assertions.assertNotNull(savedQuestion.getId(), "Saved question ID should not be null");

    // 3. Find by ID
    Optional<Question> foundQuestion = questionRepository.findById(savedQuestion.getId());
    Assertions.assertTrue(foundQuestion.isPresent(), "Question should be found");
    Assertions.assertEquals(savedQuestion.getTitle(), foundQuestion.get().getTitle());
    Assertions.assertEquals(savedQuestion.getType(), foundQuestion.get().getType());

    // 4. Update
    savedQuestion.setTitle("Updated Title");
    Question updatedQuestion = questionRepository.save(savedQuestion);
    Assertions.assertEquals("Updated Title", updatedQuestion.getTitle());

    Optional<Question> foundUpdated = questionRepository.findById(savedQuestion.getId());
    Assertions.assertTrue(foundUpdated.isPresent());
    Assertions.assertEquals("Updated Title", foundUpdated.get().getTitle());

    // 5. Delete
    questionRepository.deleteById(savedQuestion.getId());
    Optional<Question> deletedQuestion = questionRepository.findById(savedQuestion.getId());
    Assertions.assertFalse(deletedQuestion.isPresent(), "Question should be deleted");
  }

  @Test
  void testBatchDelete() {
    // 1. Create multiple questions
    Question q1 = createQuestion("Q1");
    Question q2 = createQuestion("Q2");
    Question q3 = createQuestion("Q3");

    questionRepository.save(q1);
    questionRepository.save(q2);
    questionRepository.save(q3);

    Assertions.assertNotNull(q1.getId());
    Assertions.assertNotNull(q2.getId());
    Assertions.assertNotNull(q3.getId());

    // 2. Batch delete
    List<Long> idsToDelete = Arrays.asList(q1.getId(), q2.getId());
    questionRepository.batchDelete(idsToDelete);

    // 3. Verify deletion
    Assertions.assertFalse(
        questionRepository.findById(q1.getId()).isPresent(), "Q1 should be deleted");
    Assertions.assertFalse(
        questionRepository.findById(q2.getId()).isPresent(), "Q2 should be deleted");
    Assertions.assertTrue(questionRepository.findById(q3.getId()).isPresent(), "Q3 should remain");
  }

  @Test
  void testUpdateNonExistent() {
    Question question = createQuestion("Non-existent");
    question.setId(999999L); // Assume this ID does not exist

    Assertions.assertThrows(
        ServerException.class,
        () -> {
          questionRepository.save(question);
        });
  }

  private Question createQuestion(String title) {
    Question question = new Question();
    question.setTitle(title);
    question.setContent("Content for " + title);
    question.setType(QuestionType.SINGLE_CHOICE);
    question.setDifficulty(1);
    question.setScore(5);
    question.setStatus(QuestionStatus.DRAFT);
    question.setMetaInfo("{}");
    question.setTags("test");
    question.setCreatedAt(LocalDateTime.now());
    question.setUpdatedAt(LocalDateTime.now());
    return question;
  }
}
