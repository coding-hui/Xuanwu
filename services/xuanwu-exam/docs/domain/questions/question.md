# 模块：题库（Question）

## 目标
- 管理题目生命周期：草稿/发布/归档；支持类型、难度、标签与分类。
- 提供检索、过滤、分页、版本化与附件扩展位。

## 领域模型
- `Question(id,title,content,type,difficulty,score,status,metaInfo,tags,categoryId,creatorId,createdAt,updatedAt)`。
- 类型：`QuestionType`（单选/多选/判断/简答/编程）。
- 状态：`QuestionStatus`（草稿/发布/归档）。

## 数据库
- 表：`question`（见 `db/migration/V1.0.0_1__init_question_schema.sql`）。
- 索引：`idx_type`、`idx_category`；可增加 `idx_tags`（前缀匹配）与全文索引按需。

## 仓储与持久化
- 仓储接口：`domain/question/QuestionRepository.java`。
- MyBatis：`infrastructure/persistence/question/mapper/QuestionMapper.java` + `mapper/QuestionMapper.xml`。
- Converter：`infrastructure/persistence/question/converter/QuestionConverter.java`。

## 应用用例
- 接口：`application/usecase/QuestionUseCase.java`。
- 实现：`application/service/QuestionService.java`（默认状态草稿）。

## API 端点
- 基础路径与版本：`/v1/questions`（`application.yml` 已启用 API 版本 `1`）。
- 创建题目：`POST /v1/questions`。
- 更新题目：`PUT /v1/questions/{id}`。
- 查询题目：`GET /v1/questions/{id}`。
- 批量删除：`DELETE /v1/questions?ids=...`。
- 后续：分页检索与过滤（类型/难度/标签/分类）。

### 示例
- 创建
  - 请求
    ```json
    {
      "title": "判断题示例",
      "content": "Java 17 支持虚拟线程？",
      "type": "TRUE_FALSE",
      "difficulty": 2,
      "score": 5,
      "metaInfo": "{\"answer\":true}",
      "tags": "java,thread",
      "categoryId": 1001
    }
    ```
  - cURL：`curl -X POST http://localhost:6789/v1/questions -H 'Content-Type: application/json' -d '@create.json'`
  - 响应（简化）
    ```json
    {
      "code": 0,
      "data": {
        "id": 1,
        "title": "判断题示例",
        "type": "TRUE_FALSE",
        "difficulty": 2,
        "score": 5,
        "status": 1,
        "categoryId": 1001
      }
    }
    ```
- 更新
  - `curl -X PUT http://localhost:6789/v1/questions/1 -H 'Content-Type: application/json' -d '@update.json'`
- 查询
  - `curl http://localhost:6789/v1/questions/1`
- 批量删除
  - `curl -X DELETE 'http://localhost:6789/v1/questions?ids=1&ids=2&ids=3'`

## DTO 与校验
- Create/Update/Response DTO；Jakarta Validation 提示使用 i18n 配置。

### 字段摘要
- `title`：必填，非空。
- `content`：必填，非空。
- `type`：必填，枚举 `SINGLE_CHOICE|MULTIPLE_CHOICE|TRUE_FALSE|SHORT_ANSWER|CODING`。
- `difficulty`：必填，范围 `1..5`。
- `score`：必填，`>= 0`。
- `categoryId`：必填，分类 ID。
- `metaInfo/tags`：选填，结构与格式由具体题型约定。

## 错误码
- `ExamErrorCode.QUESTION_NOT_FOUND`；系统数据库错误使用 `SystemErrorCode.DATABASE_ERROR`。
 - 校验错误：按字段校验返回 i18n 提示（`errors_zh_CN.properties`/`errors_en.properties`）。

## 测试建议
- Repository CRUD 集成测试（Flyway + MySQL 容器）。
- Service 用例与 Assembler 映射单测。
