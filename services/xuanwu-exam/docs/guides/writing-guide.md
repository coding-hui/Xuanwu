# 文档编写规范

## 目标
- 统一文档结构与命名，降低维护成本，方便后续模块扩展。

## 命名与路径
- 文件名使用小写短横线：如 `attempt-answer.md`、`category-tag.md`。
- 按领域分组：放入 `domain/*`、跨系统能力放入 `integration/*`。
- 架构与总览类文档放在 `architecture/*`。

## 内容模板
- 模块文档建议包含：目标、领域模型、数据库、仓储、应用用例、API 端点、DTO/校验、错误码、测试建议。
- 工作流文档建议包含：目标、范围、领域模型、API 端点、测试建议。
- 参见 `guides/templates/*`。

## 链接规范
- 使用相对路径链接同目录或上层文档：`../exams/exam.md`。
- 跨目录链接以项目根 `docs/` 为基准的相对路径：`../integration/iam-audit.md`。

## 提交建议
- 在 PR 中附带文档结构变更说明与导航更新。
- 保持与代码实现同步；重要设计变更更新 `architecture/tech-design.md`。
