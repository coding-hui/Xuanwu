# xuanwu-exam TODO 跟踪

集中跟踪未落地功能与迭代事项；与架构与模块设计文档保持一致（相对路径链接）。

## 已实现
- 题库（Question）：领域模型、DTO/Assembler、MyBatis 仓储、Flyway 初始化、v1 API CRUD、校验与 i18n 错误。

## 待实现模块
- [✅] 分类与标签（Category & Tag）—— 领域与 API
  - 设计：`domain/questions/category-tag.md`
- [ ] 试卷（Paper）—— 结构与规则抽题、发布
  - 设计：`domain/papers/paper.md`
- [ ] 考试（Exam）—— 排期/参与者管理/开始结束
  - 设计：`domain/exams/exam.md`
- [ ] 作答与答案（Attempt & Answer）—— 会话与答案存储、提交
  - 设计：`domain/exams/attempt-answer.md`
- [ ] 评分与阅卷（Grading）—— 自动评分与阅卷任务、Rubric
  - 设计：`domain/exams/grading.md`
- [ ] 报表与分析（Report）—— 统计与导出
  - 设计：`domain/reporting/report.md`
- [ ] 导入与导出（Import/Export）—— 批量导入/导出与校验
  - 设计：`integration/import-export.md`
- [ ] 发布与审批（Workflow）—— 状态机与审批
  - 设计：`domain/workflow/workflow.md`
- [ ] 权限与审计（IAM & Audit）—— RBAC 集成与审计日志
  - 设计：`integration/iam-audit.md`

## 题库增强 TODO（Question）
- [ ] 分页检索与组合过滤（类型/难度/标签/分类）。
- [ ] 版本历史与修订说明（version/revisionNote）。
- [ ] 附件扩展位（图片/代码片段，外部对象存储）。
- [ ] 引用分析（题目被试卷引用次数）。

## 数据库迁移计划（Flyway）
- [ ] V2：`category`、`tag` 基础表。
- [ ] V3：`paper`、`paper_section`、`paper_item`。
- [ ] V4：`exam`、`exam_participant`。
- [ ] V5：`attempt`、`answer`。
- [ ] V6：`grading_task`、`rubric`。

## API 与用例待实现（按架构风格）
- [ ] 控制器：`CategoryController`、`TagController`、`PaperController`、`ExamController`、`AttemptController`、`GradingController`、`ReportController`。
- [ ] UseCase/Service：为各聚合提供用例接口与实现，事务边界与编排。
- [ ] Assembler/DTO：MapStruct 映射与请求/响应 DTO。

## 测试与验证
- [ ] Repository 集成测试：MySQL 测试容器 + Flyway。
- [ ] Service 用例单测：状态机与边界校验。
- [ ] API 集成测试：主要业务流（创建试卷→发布考试→作答→评分→报表）。

## 维护与跟踪规范
- 统一在本文件勾选进度；完成后关联提交或文档更新说明。
- 文档链接使用相对路径；新增模块需先完善对应设计文档再编码。
