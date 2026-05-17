# 阿里云轻应用服务器部署指南

## 部署概览

本指南将帮助您将 SRM 系统部署到阿里云轻应用服务器。

## 前置准备

### 1. 服务器要求
- 阿里云轻应用服务器（推荐配置：2核4G及以上
- 操作系统：Ubuntu 22.04 LTS / CentOS 7+
- 已开放端口：80, 8080, 22 (SSH)

### 2. 本地准备
- GitHub 仓库地址：https://github.com/franklinghu/SRM
- GitHub 访问令牌（用于克隆代码）

## 部署步骤

### 第一步：登录服务器

```bash
# 使用 SSH 连接到服务器
ssh root@your-server-ip
```

### 第二步：安装 Docker 和 Docker Compose

#### Ubuntu/Debian 系统：

```bash
# 更新系统
apt update && apt upgrade -y

# 安装必要工具
apt install -y apt-transport-https ca-certificates curl software-properties-common

# 添加 Docker 官方 GPG 密钥
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/trusted.gpg.d/docker.gpg

# 添加 Docker 仓库
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/trusted.gpg.d/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# 安装 Docker
apt update
apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# 启动 Docker 服务
systemctl start docker
systemctl enable docker

# 验证安装
docker --version
docker compose version
```

#### CentOS/RHEL 系统：

```bash
# 更新系统
yum update -y

# 安装必要工具
yum install -y yum-utils

# 添加 Docker 仓库
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

# 安装 Docker
yum install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# 启动 Docker 服务
systemctl start docker
systemctl enable docker

# 验证安装
docker --version
docker compose version
```

### 第三步：克隆项目代码

```bash
# 进入工作目录
cd /opt

# 克隆项目（使用您的 GitHub 令牌）
git clone https://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@github.com/franklinghu/SRM.git

# 进入项目目录
cd SRM
```

### 第四步：配置生产环境（可选）

#### 修改生产环境配置（根据需要）：

```bash
# 编辑 docker-compose.yml
nano docker-compose.yml
```

建议修改以下内容：
```yaml
# 修改 MySQL 密码
MYSQL_ROOT_PASSWORD: your-strong-password-here

# 修改 JWT 密钥
JWT_SECRET: your-random-256-bit-secret-key-here
```

### 第五步：启动服务

```bash
# 构建并启动所有服务
docker compose up -d --build

# 查看服务状态
docker compose ps

# 查看日志
docker compose logs -f
```

### 第六步：验证部署

等待约 3-5 分钟，服务启动完成后：

- 访问前端：http://your-server-ip
- 验证后端：http://your-server-ip/api

## 常用运维命令

### 查看服务状态

```bash
cd /opt/SRM
docker compose ps
```

### 查看日志

```bash
# 查看所有服务日志
docker compose logs -f

# 查看特定服务日志
docker compose logs -f backend
docker compose logs -f frontend
docker compose logs -f mysql
```

### 重启服务

```bash
# 重启所有服务
docker compose restart

# 重启特定服务
docker compose restart backend
```

### 停止服务

```bash
# 停止所有服务
docker compose down

# 停止并删除数据卷（谨慎操作）
docker compose down -v
```

### 更新代码更新部署

```bash
cd /opt/SRM

# 拉取最新代码
git pull

# 重新构建并启动
docker compose up -d --build
```

## 备份和数据持久化

数据通过 Docker volumes 自动持久化：

- MySQL 数据：`mysql_data` volume
- Redis 数据：`redis_data` volume

### 备份数据库

```bash
# 进入 MySQL 容器
docker exec -it srm-mysql bash

# 备份数据库
mysqldump -u root -psrm_password srm_db > /backup/srm_backup_$(date +%Y%m%d).sql
```

### 恢复数据库

```bash
# 复制备份文件到容器
docker cp srm_backup.sql srm-mysql:/tmp/

# 进入容器并恢复
docker exec -it srm-mysql bash
mysql -u root -psrm_password srm_db < /tmp/srm_backup.sql
```

## 安全建议

1. **配置防火墙

```bash
# Ubuntu/Debian
ufw allow 22/tcp
ufw allow 80/tcp
ufw allow 443/tcp
ufw enable
```

2. **配置 HTTPS（可选，推荐）

可以使用 Let's Encrypt 免费 SSL 证书或阿里云 SSL 证书

3. **定期更新系统和 Docker 镜像

```bash
# 更新系统
apt update && apt upgrade -y

# 拉取最新镜像
docker compose pull
docker compose up -d
```

## 故障排查

### 端口被占用

```bash
# 查看端口占用
netstat -tlnp

# 如果 80 端口被占用，检查服务
lsof -i :80
```

### 容器启动失败

```bash
# 查看容器日志
docker compose logs [service-name]

# 检查容器状态
docker ps -a
```

### 数据库连接问题

```bash
# 检查 MySQL 是否正常运行
docker exec srm-mysql mysqladmin ping -u root -psrm_password

# 检查网络连接
docker network ls
docker network inspect srm_srm-network
```

## 服务架构说明

### 服务端口

- **前端 (Nginx)**: 80端口
- **后端 (Spring Boot)**: 8080端口
- **MySQL**: 3306端口
- **Redis**: 6379端口

### 访问路径

- 前端界面：`http://your-server-ip/
- 后端 API：`http://your-server-ip/api
