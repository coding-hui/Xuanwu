# 模块：作答与答案（Attempt & Answer）

## 目标
- 管理考生作答会话与答案记录，支持自动保存与提交。

## 领域模型
- `Attempt(id,examId,userId,status,startedAt,submittedAt,clientInfo)`。
- `Answer(id,attemptId,questionId,content,autoScore,manualScore,finalScore)`。

## 数据库
- 表：`attempt`、`answer`。
- 索引：`attempt(exam_id,user_id)`、`answer(attempt_id,question_id)`。

## 仓储
- `AttemptRepository`、`AnswerRepository`。

## 应用用例
- `AttemptUseCase`：开始作答、保存答案、提交、超时处理。

## API 端点
- 作答：`/attempts`。
- 答案管理：`/attempts/{id}/answers`、`/attempts/{id}/submit`。

## 测试建议
- 自动保存幂等；提交事务一致性；并发与超时处理。

