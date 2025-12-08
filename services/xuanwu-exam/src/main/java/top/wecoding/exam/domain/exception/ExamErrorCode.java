package top.wecoding.exam.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import top.wecoding.xuanwu.core.exception.ErrorCode;

/** Exam Error Code */
@Getter
@AllArgsConstructor
public enum ExamErrorCode implements ErrorCode {
  QUESTION_NOT_FOUND(200001),
  BATCH_DELETE_LIMIT_EXCEEDED(200002),
  ;

  private final Integer code;
}
