# 模块：题库（Question）

## 目标
- 管理题目生命周期：草稿/发布/归档；支持类型、难度、标签与分类。
- 提供检索、过滤、分页、版本化与附件扩展位。

## 领域模型
- `Question(id,title,content,type,difficulty,score,status,metaInfo,tags,categoryId,creatorId,createdAt,updatedAt)`。
- 类型：`QuestionType`（单选/多选/判断/简答/编程）。
- 状态：`QuestionStatus`（草稿/发布/归档）。

## 数据库
- 表：`question`（见 `db/migration/V1__init_question_schema.sql`）。
- 索引：`idx_type`、`idx_category`；可增加 `idx_tags`（前缀匹配）与全文索引按需。

## 仓储与持久化
- 仓储接口：`domain/question/QuestionRepository.java`。
- MyBatis：`infrastructure/persistence/question/mapper/QuestionMapper.java` + `mapper/QuestionMapper.xml`。
- Converter：`infrastructure/persistence/question/converter/QuestionConverter.java`。

## 应用用例
- 接口：`application/usecase/QuestionUseCase.java`。
- 实现：`application/service/QuestionService.java`（默认状态草稿）。

## API 端点
- 创建题目：`POST /questions` v1。
- 更新题目：`PUT /questions/{id}` v1。
- 查询题目：`GET /questions/{id}` v1。
- 批量删除：`DELETE /questions?ids=...` v1。
- 后续：分页检索与过滤（类型/难度/标签/分类）。

## DTO 与校验
- Create/Update/Response DTO；Jakarta Validation 提示使用 i18n 配置。

## 错误码
- `ExamErrorCode.QUESTION_NOT_FOUND`；系统数据库错误使用 `SystemErrorCode.DATABASE_ERROR`。

## 测试建议
- Repository CRUD 集成测试（Flyway + MySQL 容器）。
- Service 用例与 Assembler 映射单测。
