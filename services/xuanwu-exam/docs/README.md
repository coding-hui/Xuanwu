# xuanwu-exam 文档总览

## 文档结构
- `architecture/tech-design.md`：整体技术设计与架构分层。
- `domain/questions/*`：题库与分类标签相关文档。
- `domain/papers/*`：试卷结构与规则抽题。
- `domain/exams/*`：考试、作答、评分等。
- `domain/reporting/*`：报表与分析。
- `domain/workflow/*`：发布与审批流程。
- `integration/*`：与外部系统的集成（IAM、导入导出）。
- `guides/*`：文档编写规范与模板，便于后续扩展。

## 快速导航
- 架构设计：`architecture/tech-design.md`
- 题库：`domain/questions/question.md`
- 分类与标签：`domain/questions/category-tag.md`
- 试卷：`domain/papers/paper.md`
- 考试：`domain/exams/exam.md`
- 作答与答案：`domain/exams/attempt-answer.md`
- 评分与阅卷：`domain/exams/grading.md`
- 报表与分析：`domain/reporting/report.md`
- 发布与审批：`domain/workflow/workflow.md`
- 权限与审计：`integration/iam-audit.md`
- 导入与导出：`integration/import-export.md`

## 扩展规范
- 命名约定：文件使用小写短横线，如 `attempt-answer.md`；目录按领域分组。
- 新增模块：优先放入对应 `domain/*` 子目录；跨系统能力放入 `integration/*`。
- 提供文档模板：见 `guides/templates`，包含模块与工作流模板。
- 链接规范：使用相对路径链接同目录或上层文档，避免绝对路径。

## 依赖与配置（摘要）
- 依赖：Spring Boot、MyBatis、Flyway、MapStruct、`xuanwu-starter-*`、`mysql-connector-j`。
- 数据库迁移：`src/main/resources/db/migration`。
- 国际化错误：`src/main/resources/i18n/errors_*.properties`。

## 测试策略（摘要）
- 单元测试：Assembler 映射与 Service 用例。
- 集成测试：MySQL 测试容器 + Flyway，覆盖创建试卷→发布考试→作答→评分→报表。
