# SRM 系统部署指南

## 快速开始

### 前置要求

- Docker
- Docker Compose

### 一键部署

```bash
cd /workspace/SRM
docker-compose up -d
```

部署完成后访问：
- 前端：http://localhost
- 后端 API：http://localhost:8080

### 项目结构

```
/workspace/SRM/
├── srm-backend/     # 后端项目（Spring Boot）
├── srm-frontend/    # 前端项目（Vue 3 + Element Plus）
├── docker-compose.yml
└── DEPLOY.md
```

## 服务说明

### MySQL
- 端口：3306
- 数据库：srm_db
- 用户名：root
- 密码：srm_password
- 初始化脚本：srm-backend/src/main/resources/db/init.sql

### Redis
- 端口：6379

### 后端
- 端口：8080
- 环境：prod

### 前端
- 端口：80
- 代理配置：nginx

## 常用命令

### 启动服务
```bash
docker-compose up -d
```

### 查看日志
```bash
docker-compose logs -f
```

### 停止服务
```bash
docker-compose down
```

### 重新构建
```bash
docker-compose build --no-cache
docker-compose up -d
```

### 清理数据（谨慎使用）
```bash
docker-compose down -v
```

## 开发环境

### 后端开发
```bash
cd srm-backend
mvn spring-boot:run
```

### 前端开发
```bash
cd srm-frontend
npm install
npm run dev
```
