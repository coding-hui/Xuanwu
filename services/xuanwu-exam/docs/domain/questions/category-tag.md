# 模块：分类与标签（Category & Tag）

## 目标
- 提供题目与试卷的组织方式：树形分类与自由标签。
- 支持按分类/标签过滤与统计。

## 领域模型
- `Category(id,name,parentId,path,createdAt,updatedAt)`。
- `Tag(id,name,createdAt,updatedAt)`。

## 数据库
- `category`：层级树，`path` 存储层级路径（如 `/root/a/b`）。
- `tag`：唯一名称索引。
- 交叉绑定：题目与试卷关联字段或中间表（按需）。

## 仓储
- `CategoryRepository`、`TagRepository`（接口）。
- MyBatis Mapper/PO/XML 与 Converter。

## API 端点
- 分类 CRUD、树查询：`/categories`。
- 标签 CRUD：`/tags`。

## 测试建议
- 分类树增删改与路径维护；标签唯一约束。
