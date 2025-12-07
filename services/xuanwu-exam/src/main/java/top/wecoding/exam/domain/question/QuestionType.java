package top.wecoding.exam.domain.question;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Question Type Enum
 */
@Getter
@AllArgsConstructor
public enum QuestionType {

    SINGLE_CHOICE("Single Choice"), MULTIPLE_CHOICE("Multiple Choice"), TRUE_FALSE("True/False"),
    SHORT_ANSWER("Short Answer");

    private final String description;

}
