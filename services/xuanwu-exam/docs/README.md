# xuanwu-exam 技术文档总览

## 架构与分层
- API 层：REST 控制器与 DTO/Assembler，接口版本化（如 `@PostMapping(version = "1")`）。
- Application 层：UseCase 接口与 Service 实现，事务编排与业务流程。
- Domain 层：聚合根、值对象、仓储接口，面向领域行为。
- Infrastructure 层：MyBatis Mapper/PO/XML、MapStruct Converter、Flyway 迁移与外部适配。

## 依赖与配置
- 依赖：Spring Boot、MyBatis、Flyway、MapStruct、`xuanwu-starter-core`、`xuanwu-starter-mybatis`、`mysql-connector-j`。
- 数据库迁移：`src/main/resources/db/migration`（示例：`V1__init_question_schema.sql`）。
- 国际化错误：`src/main/resources/i18n/errors_*.properties`。

## 目录结构约定
- `api/controller/*`：REST 控制器（版本化）。
- `api/dto/*`：请求/响应模型。
- `api/converter/*`：Assembler（MapStruct）。
- `application/usecase/*` 与 `application/service/*`：用例与实现。
- `domain/*`：聚合根、枚举、仓储接口。
- `infrastructure/persistence/*`：PO/Mapper/Converter 与 XML。
- `docs/modules/*`：各模块技术文档。

## 模块导航
- 题库（Question）：`modules/Question.md`
- 分类与标签（Category & Tag）：`modules/CategoryTag.md`
- 试卷（Paper）：`modules/Paper.md`
- 考试（Exam）：`modules/Exam.md`
- 作答与答案（Attempt & Answer）：`modules/AttemptAnswer.md`
- 评分与阅卷（Grading）：`modules/Grading.md`
- 报表与分析（Report）：`modules/Report.md`
- 导入与导出（Import/Export）：`modules/ImportExport.md`
- 发布与审批（Workflow）：`modules/Workflow.md`
- 权限与审计（IAM & Audit）：`modules/IamAudit.md`

## 版本化与错误处理
- 路由版本：控制器注解的 `version` 字段；返回体使用 `R<T>`（统一响应）。
- 错误码：领域错误码在 `domain/exception/*`；抛出 `BaseUncheckedException` 或 `ServerException`。

## 非功能要求
- 性能：分页与索引；批量查询避免 N+1。
- 可观测性：记录关键事件与慢 SQL。
- 可靠性：事务与幂等；Flyway 管理结构变更。
- 安全：鉴权与审计、输入校验、避免注入。

## 测试策略
- 单元测试：Assembler 映射与 Service 用例；Repository 使用 Mapper stub。
- 集成测试：MySQL 测试容器 + Flyway；覆盖创建试卷→发布考试→作答→评分→报表流程。

