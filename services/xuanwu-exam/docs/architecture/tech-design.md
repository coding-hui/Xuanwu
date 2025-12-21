# xuanwu-exam 后续功能模块技术设计

## 目标与范围
- 面向企业考试场景，完善从题库、试卷、考试、作答、评分、分析的闭环能力。
- 保持现有分层架构（API / Application / Domain / Infrastructure）与依赖（Spring Boot、MyBatis、Flyway、MapStruct、xuanwu-starter-*）。
- 与 Xuanwu IAM 集成做授权与审计；维持资源版本化与多语言错误提示。

## 已实现范围（代码落地）
- 题库基础能力：`Question` 聚合、CRUD API（v1）、DTO/Assembler、MyBatis 持久化、Flyway 初始化。
- 错误与国际化：统一返回体 `R<T>` 与 i18n 错误消息。

## 其余模块状态
- 以下模块为设计草案，尚未实现，后续迭代按里程碑推进：分类与标签、试卷、考试、作答与答案、评分与阅卷、报表与分析、导入与导出、工作流、IAM & 审计。

## 总体架构
- API 层：REST 接口与 DTO/Assembler，沿用 `@RequestMapping` + 版本标识模式（如 `@PostMapping(version = "1")`）。
- Application 层：UseCase + Service 编排业务流程、事务边界、调用领域与仓储。
- Domain 层：聚合根与值对象，行为驱动；仓储接口定义持久化边界。
- Infrastructure 层：MyBatis Mapper、PO/Converter、Flyway SQL；必要的外部系统适配器（如 IAM Client）。

## 模块划分与边界（详见 domain/* 与 integration/*）
1. 题库（Question Library）：`../domain/questions/question.md`
   - 增强：分页检索、组合过滤（类型/难度/标签/分类）、版本历史、附件（图片/代码片段）、引用分析（被试卷引用次数）。
2. 分类与标签（Category & Tag）：`../domain/questions/category-tag.md`
   - 分类树结构（父子层级）、标签管理；题目与试卷可双向绑定分类/标签。
3. 试卷（Paper）：`../domain/papers/paper.md`
   - 结构：试卷（Paper）- 分卷/章节（Section）- 题目项（Item）。支持固定与规则（随机抽题、难度/类型占比、标签筛选）。
4. 考试（Exam）：`../domain/exams/exam.md`
   - 排期（开始/结束/时长）、参与对象（用户/角色/群组）、防作弊（IP/设备指纹/拍照可扩展）。
5. 作答与答案（Attempt & Answer）：`../domain/exams/attempt-answer.md`
   - 考试作答会话（Attempt），题目答案（Answer），过程状态（进行中/提交/超时），自动保存。
6. 评分与阅卷（Scoring & Grading）：`../domain/exams/grading.md`
   - 客观题自动评分规则；主观题阅卷任务（GradingTask）与评分表（Rubric）。
7. 报表与分析（Reporting）：`../domain/reporting/report.md`
   - 考试/试卷/题目维度的统计分析，导出报表（CSV/Excel）。
8. 导入与导出（Import/Export）：`../integration/import-export.md`
   - 题库批量导入（CSV/JSON）、试卷模板导入导出，数据校验与回滚。
9. 发布与审批（Workflow）：`../domain/workflow/workflow.md`
   - 草稿/待审/已发布/归档；审批流对试卷与考试生效。
10. 权限与审计（IAM & Audit）：`../integration/iam-audit.md`
   - 接入 Xuanwu IAM 做 RBAC；审计日志记录关键操作与结果。

## 领域模型（Domain）
- `Question`（已存在）：增强 `attachments`（可选，引用外部对象存储）、`version`、`revisionNote`。
- `Category`：`id,name,parentId,path,createdAt,updatedAt`。
- `Tag`：`id,name,createdAt,updatedAt`。
- `Paper`：`id,title,description,status,version,createdBy,createdAt,updatedAt`。
- `PaperSection`：`id,paperId,title,order,ruleType,fixedItemCount,randomRule(meta: type/difficulty/tags/categoryId/count)`。
- `PaperItem`：`id,sectionId,questionId,score,order`。
- `Exam`：`id,title,paperId,startTime,endTime,durationMinutes,status,settings(json),createdBy`。
- `ExamParticipant`：`id,examId,subjectType(user/role/group),subjectId,required`。
- `Attempt`：`id,examId,userId,status(started/submitted/expired),startedAt,submittedAt,clientInfo(meta)`。
- `Answer`：`id,attemptId,questionId,content(json),autoScore,manualScore,finalScore`。
- `GradingTask`：`id,attemptId,questionId,assigneeId,status,score,comment,gradedAt`。
- `Rubric`：`id,questionId,rules(json)`。

## 仓储与持久化（Repository & Persistence）
- 约定路径：
  - `domain/*/*.java`：聚合根与仓储接口，例如 `domain.paper.PaperRepository`。
  - `infrastructure/persistence/*/mapper/*.java|xml`：MyBatis Mapper。
  - `infrastructure/persistence/*/po/*.java`：PO。
  - `infrastructure/persistence/*/converter/*.java`：MapStruct 转换器。
- 每个聚合根提供 `Repository` 接口：`save/update/find/delete/batch` 与必要的查询（分页/过滤）。

## 数据库与迁移（Flyway）
- 迁移文件位于 `src/main/resources/db/migration`，按版本递增：
  - `V2__init_category_tag.sql`
  - `V3__init_paper_tables.sql`
  - `V4__init_exam_tables.sql`
  - `V5__init_attempt_answer.sql`
  - `V6__init_grading_rubric.sql`
- 示例字段（简化）：
  - `category(id BIGINT PK, name VARCHAR(100), parent_id BIGINT, path VARCHAR(255), created_at, updated_at)`
  - `tag(id BIGINT PK, name VARCHAR(100), created_at, updated_at)`
  - `paper(id BIGINT PK, title VARCHAR(255), description TEXT, status TINYINT, version INT, created_by BIGINT, created_at, updated_at)`
  - `paper_section(id BIGINT PK, paper_id BIGINT, title VARCHAR(255), sort INT, rule_type VARCHAR(20), fixed_item_count INT, random_rule JSON)`
  - `paper_item(id BIGINT PK, section_id BIGINT, question_id BIGINT, score INT, sort INT)`
  - `exam(id BIGINT PK, title VARCHAR(255), paper_id BIGINT, start_time DATETIME, end_time DATETIME, duration_minutes INT, status TINYINT, settings JSON, created_by BIGINT)`
  - `exam_participant(id BIGINT PK, exam_id BIGINT, subject_type VARCHAR(20), subject_id BIGINT, required TINYINT)`
  - `attempt(id BIGINT PK, exam_id BIGINT, user_id BIGINT, status VARCHAR(20), started_at DATETIME, submitted_at DATETIME, client_info JSON)`
  - `answer(id BIGINT PK, attempt_id BIGINT, question_id BIGINT, content JSON, auto_score INT, manual_score INT, final_score INT)`
  - `grading_task(id BIGINT PK, attempt_id BIGINT, question_id BIGINT, assignee_id BIGINT, status VARCHAR(20), score INT, comment TEXT, graded_at DATETIME)`
  - `rubric(id BIGINT PK, question_id BIGINT, rules JSON)`
- 索引建议：
  - `paper_item(section_id, question_id)`、`exam_participant(exam_id, subject_type, subject_id)`、`attempt(exam_id, user_id)`、`answer(attempt_id, question_id)`。

## API 设计（REST）
- 路径与版本遵循现有风格：
  - 分类：`/categories`（增删改查、树查询）
  - 标签：`/tags`
  - 试卷：`/papers`、`/papers/{id}/sections`、`/papers/{id}/publish`
  - 考试：`/exams`、`/exams/{id}/participants`、`/exams/{id}/start|end`
  - 作答：`/attempts`、`/attempts/{id}/answers`、`/attempts/{id}/submit`
  - 阅卷：`/grading-tasks`、`/grading-tasks/{id}/grade`
  - 报表：`/reports/exams/{id}`、`/reports/papers/{id}`、`/reports/questions/{id}`
- DTO 示例（简述）：
  - `CreatePaperRequest/UpdatePaperRequest/PaperResponse`
  - `CreateExamRequest/UpdateExamRequest/ExamResponse`
  - `CreateAttemptRequest/SubmitAttemptRequest/AttemptResponse`
  - `CreateGradingTaskRequest/GradeRequest/GradingTaskResponse`
- Assembler：MapStruct 映射，枚举与状态码转换参照 `QuestionAssembler`。

## 应用服务与用例（Application）
- 为每个模块定义 `UseCase` 接口与 `Service` 实现，例如：
  - `PaperUseCase`：创建/更新试卷、添加章节与题目项、按规则生成试卷快照。
  - `ExamUseCase`：创建考试、分配参与者、开始/结束考试、生成作答入口令牌。
  - `AttemptUseCase`：开始作答、保存答案、提交、超时处理。
  - `GradingUseCase`：创建阅卷任务、评分、汇总成绩。
  - `ReportUseCase`：查询统计数据、导出报表。
- 事务边界：
  - 试卷结构编辑与保存（同一事务）；考试发布与参与者绑定（同一事务）；作答提交与评分（提交为一事务，自动评分可异步）。

## 评分策略（Scoring）
- 客观题：
  - 单选/多选/判断：对比标准答案（`question.metaInfo.answers`），按题目设定 `score` 给分。
- 主观题：
  - 根据 `Rubric.rules` 定义维度与权重；阅卷人录入分数与评语。
- 最终分：`finalScore = autoScore + manualScore`，可加权与四舍五入策略。

## 与 IAM 的集成（权限与审计）
- 在 API 层做鉴权（基于 Xuanwu IAM Client），限制试卷编辑、考试发布、阅卷权限。
- 审计日志记录关键操作：创建/发布/提交/评分；与 `xuanwu-starter-core` 的异常体与多语言保持一致。

## 非功能需求
- 性能：分页 + 索引；长列表分页拉取，避免 N+1（适配 Mapper 层批量查询）。
- 可观测性：记录慢 SQL、关键事件（开始/提交/评分）。
- 可靠性：作答自动保存（幂等），事务一致性，Flyway 管理结构变更。
- 安全：输入校验（Jakarta Validation）、防注入（MyBatis 参数绑定）、审计追踪。

## 代码结构约定（包结构）
- `api/controller/*`：`CategoryController`、`PaperController`、`ExamController`、`AttemptController`、`GradingController`、`ReportController`。
- `api/dto/*`：各模块的请求/响应 DTO。
- `api/converter/*`：Assembler（MapStruct）。
- `application/usecase/*` 与 `application/service/*`：用例接口与实现。
- `domain/*`：聚合根、枚举、仓储接口。
- `infrastructure/persistence/*`：PO/Mapper/Converter 与 SQL。

## 迭代与里程碑（建议）
1. V2：分类/标签 + 题库检索增强。
2. V3：试卷结构（Section/Item）与规则抽题。
3. V4：考试排期与参与者绑定；启动/结束考试。
4. V5：作答与答案存储；客观题自动评分。
5. V6：阅卷任务与评分表；报表导出与分析。

## 测试与验证
- 单元测试：Repository（Mapper stub）、Service（用例）、Assembler（MapStruct 映射）。
- 集成测试：Flyway 迁移 + MySQL 测试容器，覆盖核心业务流（创建试卷→发布考试→作答→评分→报表）。

---
本文档将作为后续编码的蓝图；所有模块新增遵循现有风格与依赖约束，避免引入不必要的第三方库。
