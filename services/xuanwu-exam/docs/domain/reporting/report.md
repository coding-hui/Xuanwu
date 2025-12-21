# 模块：报表与分析（Report）

> 当前模块代码尚未实现，以下为设计规范草案（TODO）。

## 目标
- 对考试/试卷/题目维度进行统计分析与导出。

## 范围
- 指标：分数分布、正确率、用时统计、难度与得分相关性等。

## 数据来源
- `Attempt` 与 `Answer` 聚合，结合 `Exam`/`Paper` 元数据。

## API 端点
- `/reports/exams/{id}`、`/reports/papers/{id}`、`/reports/questions/{id}`。
- 导出：CSV/Excel。

## 性能建议
- 聚合查询 + 适当物化视图或缓存；分页与分批导出。

## 测试建议
- 统计口径一致性；大数据量性能基准。
