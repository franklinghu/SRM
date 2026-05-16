-- 创建数据库
CREATE DATABASE IF NOT EXISTS srm_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE srm_db;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    status INT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    tenant_id BIGINT COMMENT '租户ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入测试用户 (密码: admin123)
INSERT INTO sys_user (username, password, real_name, email, status, tenant_id)
VALUES ('admin', '$2a$10$XQnS4f8L7Z5mQvJ1wY1kKO5g8zX0kY2sD3p6wR9xB2mN4cV5uJ6fG', '系统管理员', 'admin@example.com', 1, 1);
