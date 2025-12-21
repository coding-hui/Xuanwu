CREATE TABLE IF NOT EXISTS `category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Category ID',
    `name` varchar(100) NOT NULL COMMENT 'Category Name',
    `parent_id` bigint(20) DEFAULT NULL COMMENT 'Parent Category ID',
    `path` varchar(255) NOT NULL COMMENT 'Hierarchy Path',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_parent_name` (`parent_id`, `name`),
    KEY `idx_path` (`path`),
    KEY `idx_parent` (`parent_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Category';

CREATE TABLE IF NOT EXISTS `tag` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Tag ID',
    `name` varchar(100) NOT NULL COMMENT 'Tag Name',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Tag';

CREATE TABLE IF NOT EXISTS `question_tag` (
    `question_id` bigint(20) NOT NULL COMMENT 'Question ID',
    `tag_id` bigint(20) NOT NULL COMMENT 'Tag ID',
    PRIMARY KEY (`question_id`, `tag_id`),
    KEY `idx_tag` (`tag_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Question-Tag Relation';

