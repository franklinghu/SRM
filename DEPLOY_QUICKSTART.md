# SRM 阿里云部署 - 快速开始

## 🚀 方式一：自动部署（推荐）

### 1 步完成部署

登录阿里云轻应用服务器后，执行：

```bash
# 下载并运行自动部署脚本
cd /opt
git clone https://github.com/franklinghu/SRM.git
cd SRM
bash deploy-aliyun.sh
```

## 📝 方式二：手动部署

### 1. 安装 Docker

```bash
# Ubuntu/Debian
apt update
apt install -y apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/trusted.gpg.d/docker.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/trusted.gpg.d/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
apt update
apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
systemctl start docker
systemctl enable docker
```

### 2. 获取项目代码

```bash
cd /opt
git clone https://github.com/franklinghu/SRM.git
cd SRM
```

### 3. 启动服务

```bash
docker compose up -d --build
```

### 4. 访问系统

打开浏览器访问：`http://your-server-ip

## 🔧 常用命令

### 服务管理

```bash
# 查看状态
docker compose ps

# 查看日志
docker compose logs -f

# 重启服务
docker compose restart

# 停止服务
docker compose down

# 更新代码并重新部署
cd /opt/SRM
git pull
docker compose up -d --build
```

### 查看特定服务日志

```bash
# 后端日志
docker compose logs -f backend

# 前端日志
docker compose logs -f frontend

# 数据库日志
docker compose logs -f mysql
```

## 📊 服务架构

| 服务 | 端口 | 说明 |
|------|------|------|
| 前端 | 80 | Nginx 静态服务器 |
| 后端 | 8080 | Spring Boot API |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存服务 |

## 🔒 安全建议

1. 修改默认密码
2. 配置防火墙
3. 启用 HTTPS
4. 定期更新系统

## 📚 详细文档

- 完整部署指南：[ALIYUN_DEPLOY.md](./ALIYUN_DEPLOY.md)
- Docker 配置：[docker-compose.yml](./docker-compose.yml)

## 🆘 故障排查

### 端口被占用

```bash
# 检查端口
netstat -tlnp

# 释放端口
pkill -9 nginx  # 如果被 Nginx 占用
```

### 容器启动失败

```bash
# 查看日志
docker compose logs [service-name]

# 重启服务
docker compose restart
```

## 📞 技术支持

如有问题，请查看：
- 项目仓库：https://github.com/franklinghu/SRM
- 详细文档：[ALIYUN_DEPLOY.md](./ALIYUN_DEPLOY.md)
