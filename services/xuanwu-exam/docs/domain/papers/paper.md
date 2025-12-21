# 模块：试卷（Paper）

## 目标
- 组织试卷结构与抽题规则，支持固定与随机组合。

## 领域模型
- `Paper(id,title,description,status,version,createdBy,createdAt,updatedAt)`。
- `PaperSection(id,paperId,title,order,ruleType,fixedItemCount,randomRule)`。
- `PaperItem(id,sectionId,questionId,score,order)`。

## 数据库
- 表：`paper`、`paper_section`、`paper_item`。
- 索引：`paper_item(section_id,question_id)`。

## 仓储
- `PaperRepository`、`PaperSectionRepository`、`PaperItemRepository`。
- MyBatis Mapper/PO/XML 与 Converter。

## 应用用例
- `PaperUseCase`：创建/更新试卷，维护章节与题目项；按规则生成试卷快照。

## API 端点
- 试卷 CRUD：`/papers`。
- 章节管理：`/papers/{id}/sections`。
- 发布：`/papers/{id}/publish`。

## 测试建议
- 规则抽题正确性与边界；发布状态流转与版本管理。
