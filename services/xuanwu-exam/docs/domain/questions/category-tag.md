# 模块：分类与标签（Category & Tag）

> 设计已完成，代码实现未开始（TODO）。

## 目标与范围
- 为题目与试卷提供组织方式：树形分类与自由标签。
- 支持按分类/标签进行过滤、统计与权限控制扩展。

## 领域模型
- `Category(id,name,parentId,path,createdAt,updatedAt)`
  - 约束：同一父节点下 `name` 唯一；`path` 记录从根到节点的层级路径（如 `/root/java/basic`）。
  - 行为：创建/重命名/移动节点；维护 `path` 与子节点路径。
- `Tag(id,name,createdAt,updatedAt)`
  - 约束：`name` 全局唯一；不区分大小写（存储统一小写）。
- 关联（按需落地）：
  - 题目与标签：`question_tag(question_id, tag_id)` 多对多；`UNIQUE(question_id, tag_id)`。
  - 试卷与标签：`paper_tag(paper_id, tag_id)` 多对多；`UNIQUE(paper_id, tag_id)`。
  - 题目与分类：沿用 `question.category_id` 单一主分类；如需多分类可引入 `question_category(question_id, category_id)`。
  - 试卷与分类：后续在 `paper` 聚合中加入 `category_id` 或中间表。

## 数据库设计（Flyway 迁移 V2）
- `category`
  - 字段：`id BIGINT PK`，`name VARCHAR(100) NOT NULL`，`parent_id BIGINT NULL`，`path VARCHAR(255) NOT NULL`，`created_at DATETIME NOT NULL`，`updated_at DATETIME NOT NULL`。
  - 索引：`UNIQUE(parent_id, name)`；`INDEX(path)`；`INDEX(parent_id)`。
- `tag`
  - 字段：`id BIGINT PK`，`name VARCHAR(100) NOT NULL`，`created_at DATETIME NOT NULL`，`updated_at DATETIME NOT NULL`。
  - 索引：`UNIQUE(name)`。
- `question_tag`
  - 字段：`question_id BIGINT NOT NULL`，`tag_id BIGINT NOT NULL`；`PRIMARY KEY(question_id, tag_id)`。
  - 外键（逻辑约束）：指向 `question(id)` 与 `tag(id)`（MySQL 可选 FK）。
- 迁移文件：`src/main/resources/db/migration/V1.0.0_2__init_category_tag.sql`（计划）。

## 仓储与持久化
- 仓储接口
  - `CategoryRepository`
    - `save/update/deleteById/findById/findChildren(parentId)/findTree(rootId)`。
  - `TagRepository`
    - `save/update/deleteById/findById/findByName/list(page, filter)`。
- 持久化
  - MyBatis Mapper：`CategoryMapper`、`TagMapper`（含分页与树查询）。
  - PO 与 Converter：`CategoryPO/TagPO` 与 MapStruct 转换。
  - 路径维护：在 Service 层处理节点移动时批量更新子节点 `path`。

## API 设计（v1）
- 分类 `/v1/categories`
  - `POST /v1/categories` 创建分类（`name,parentId`）。
  - `PUT /v1/categories/{id}` 重命名/移动（`name,parentId`）。
  - `GET /v1/categories/{id}` 查询详情。
  - `GET /v1/categories/tree?rootId=...` 树查询。
  - `DELETE /v1/categories/{id}` 删除（若存在子节点或被引用则提示不可删除）。
- 标签 `/v1/tags`
  - `POST /v1/tags` 创建标签（`name`）。
  - `PUT /v1/tags/{id}` 更新标签名。
  - `GET /v1/tags/{id}` 查询详情。
  - `GET /v1/tags?page=1&size=20&keyword=java` 分页与关键字过滤。
  - `DELETE /v1/tags/{id}` 删除（若被引用则提示不可删除）。
- 绑定（后续按聚合提供）
  - 题目绑定标签：`POST /v1/questions/{id}/tags`（`tagIds`）。
  - 题目解绑标签：`DELETE /v1/questions/{id}/tags?tagIds=...`。

## DTO 与校验
- `CreateCategoryRequest(name,parentId)`：`name` 非空，长度 `<=100`；`parentId` 可空。
- `UpdateCategoryRequest(id,name,parentId)`：同上，且 `id` 必填。
- `CategoryResponse(id,name,parentId,path,createdAt,updatedAt)`。
- `CreateTagRequest(name)`：`name` 非空，长度 `<=100`；保存为小写；唯一。
- `UpdateTagRequest(id,name)`；`TagResponse(id,name,createdAt,updatedAt)`。
- 错误提示走 i18n 配置（`errors_*.properties`）。

## 错误码（建议）
- `CATEGORY_NOT_FOUND`、`TAG_NOT_FOUND`。
- `CATEGORY_NAME_CONFLICT`（同父重复）、`TAG_NAME_CONFLICT`（全局重复）。
- `CATEGORY_HAS_CHILDREN`、`CATEGORY_IN_USE`、`TAG_IN_USE`（被引用不可删）。

## 示例
- 创建分类
  - 请求
    ```json
    {"name":"Java","parentId":null}
    ```
  - 响应（简化）
    ```json
    {"code":0,"data":{"id":101,"name":"Java","path":"/Java"}}
    ```
- 创建标签
  - 请求
    ```json
    {"name":"spring"}
    ```
  - 响应（简化）
    ```json
    {"code":0,"data":{"id":201,"name":"spring"}}
    ```

## 测试建议
- 分类树：创建/重命名/移动/删除的路径维护与约束校验。
- 标签：唯一性、大小写规范、分页与过滤。
- 绑定关系：题目-标签的幂等绑定与删除；引用约束删除失败用例。
