# 模块：考试（Exam）

> 当前模块代码尚未实现，以下为设计规范草案（TODO）。

## 目标
- 管理考试排期、参与对象、开始/结束状态与扩展设置。

## 领域模型
- `Exam(id,title,paperId,startTime,endTime,durationMinutes,status,settings,createdBy)`。
- `ExamParticipant(id,examId,subjectType,subjectId,required)`。

## 数据库
- 表：`exam`、`exam_participant`。
- 索引：`exam_participant(exam_id,subject_type,subject_id)`。

## 仓储
- `ExamRepository`、`ExamParticipantRepository`。

## 应用用例
- `ExamUseCase`：创建考试、绑定参与者、开始/结束考试、生成作答入口令牌。

## API 端点
- 考试 CRUD：`/exams`。
- 参与者管理：`/exams/{id}/participants`。
- 开始/结束：`/exams/{id}/start|end`。

## 测试建议
- 时窗校验、参与者绑定与权限；启动/结束状态机与幂等。
