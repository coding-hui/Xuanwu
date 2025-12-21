CREATE TABLE IF NOT EXISTS `question` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Question ID',
    `title` varchar(255) NOT NULL COMMENT 'Question Title',
    `content` text COMMENT 'Question Description/Content',
    `type` varchar(50) NOT NULL COMMENT 'Question Type: SINGLE_CHOICE, MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER, CODING',
    `difficulty` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'Difficulty Level: 1-5',
    `score` int(11) NOT NULL DEFAULT '0' COMMENT 'Default Score',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'Status: 1-Draft, 2-Published, 3-Archived',
    `meta_info` json COMMENT 'Type-specific Metadata (Options, Answers, TestCases)',
    `tags` varchar(255) DEFAULT NULL COMMENT 'Tags (Comma separated)',
    `category_id` bigint(20) DEFAULT NULL COMMENT 'Category ID',
    `creator_id` bigint(20) DEFAULT NULL COMMENT 'Creator ID',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_category` (`category_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Question Library';