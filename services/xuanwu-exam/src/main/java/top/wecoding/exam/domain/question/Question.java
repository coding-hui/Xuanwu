package top.wecoding.exam.domain.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Question Aggregate Root
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Question ID
     */
    private Long id;

    /**
     * Question Title/Content
     */
    private String content;

    /**
     * Question Type (e.g., Single Choice, Multiple Choice, True/False, Short Answer)
     */
    private QuestionType type;

    /**
     * Difficulty Level (1-5)
     */
    private Integer difficulty;

    /**
     * Score
     */
    private Integer score;

    /**
     * Creation Time
     */
    private LocalDateTime createdAt;

    /**
     * Update Time
     */
    private LocalDateTime updatedAt;

}
