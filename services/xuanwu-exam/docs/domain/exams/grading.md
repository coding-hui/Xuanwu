# 模块：评分与阅卷（Grading）

> 当前模块代码尚未实现，以下为设计规范草案（TODO）。

## 目标
- 客观题自动评分；主观题阅卷任务与评分表（Rubric）。

## 领域模型
- `GradingTask(id,attemptId,questionId,assigneeId,status,score,comment,gradedAt)`。
- `Rubric(id,questionId,rules)`（维度与权重）。

## 仓储
- `GradingTaskRepository`、`RubricRepository`。

## 评分策略
- 客观题：与标准答案对比，按题目 `score` 计分。
- 主观题：Rubric 维度评分合成为 `manualScore`。
- 最终分：`finalScore = autoScore + manualScore`（可加权）。

## API 端点
- 阅卷任务：`/grading-tasks`、`/grading-tasks/{id}/grade`。

## 测试建议
- 自动评分正确性；Rubric 解析与评分一致性；并发阅卷冲突控制。
