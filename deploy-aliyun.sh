#!/bin/bash

# SRM 系统 - 阿里云轻应用服务器自动部署脚本
# 使用方法: bash deploy-aliyun.sh

set -e

echo "=========================================="
echo "  SRM 系统 - 阿里云部署脚本"
echo "=========================================="
echo ""

# 检查是否为 root 用户
if [ "$EUID" -ne 0 ]; then 
    echo "请使用 root 用户运行此脚本"
    echo "使用: sudo bash $0"
    exit 1
fi

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查操作系统
check_os() {
    echo "检测操作系统..."
    if [ -f /etc/os-release ]; then
        . /etc/os-release
        OS=$ID
        VERSION=$VERSION_ID
        echo -e "${GREEN}检测到: $OS $VERSION${NC}"
    else
        echo -e "${RED}无法检测操作系统${NC}"
        exit 1
    fi
}

# 安装 Docker
install_docker() {
    echo ""
    echo "=========================================="
    echo "  步骤 1: 安装 Docker 和 Docker Compose"
    echo "=========================================="
    
    if command -v docker &> /dev/null; then
        echo -e "${GREEN}Docker 已安装${NC}"
        docker --version
    else
        echo "正在安装 Docker..."
        
        if [ "$OS" = "ubuntu" ] || [ "$OS" = "debian" ]; then
            # Ubuntu/Debian 安装
            apt update
            apt install -y apt-transport-https ca-certificates curl software-properties-common
            curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/trusted.gpg.d/docker.gpg
            echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/trusted.gpg.d/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
            apt update
            apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
        elif [ "$OS" = "centos" ] || [ "$OS" = "rhel" ]; then
            # CentOS/RHEL 安装
            yum install -y yum-utils
            yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
            yum install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
        else
            echo -e "${RED}不支持的操作系统: $OS${NC}"
            exit 1
        fi
    fi
    
    # 启动 Docker
    systemctl start docker
    systemctl enable docker
    
    # 验证安装
    echo ""
    echo "Docker 版本:"
    docker --version
    docker compose version
    echo -e "${GREEN}Docker 安装完成${NC}"
}

# 克隆项目
clone_project() {
    echo ""
    echo "=========================================="
    echo "  步骤 2: 获取项目代码"
    echo "=========================================="
    
    cd /opt
    
    if [ -d "SRM" ]; then
        echo -e "${YELLOW}SRM 目录已存在，正在更新代码...${NC}"
        cd SRM
        git pull
    else
        echo "正在克隆项目..."
        git clone https://github.com/franklinghu/SRM.git
        cd SRM
    fi
    
    echo -e "${GREEN}项目代码已准备${NC}"
}

# 配置环境
configure_environment() {
    echo ""
    echo "=========================================="
    echo "  步骤 3: 配置生产环境"
    echo "=========================================="
    
    # 询问是否需要修改配置
    read -p "是否需要修改数据库密码和 JWT 密钥？(y/n): " modify_config
    
    if [ "$modify_config" = "y" ] || [ "$modify_config" = "Y" ]; then
        # 生成随机密码
        MYSQL_PASSWORD=$(openssl rand -hex 16)
        JWT_SECRET=$(openssl rand -hex 32)
        
        echo -e "${YELLOW}生成的密码（请保存）:${NC}"
        echo "MySQL 密码: $MYSQL_PASSWORD"
        echo "JWT 密钥: $JWT_SECRET"
        echo ""
        
        # 创建环境变量文件
        cat > .env << EOF
MYSQL_ROOT_PASSWORD=$MYSQL_PASSWORD
JWT_SECRET=$JWT_SECRET
EOF
        
        echo -e "${GREEN}环境配置已保存到 .env${NC}"
    fi
}

# 部署服务
deploy_services() {
    echo ""
    echo "=========================================="
    echo "  步骤 4: 启动服务"
    echo "=========================================="
    
    # 停止现有服务（如果有）
    if [ -f "docker-compose.yml" ]; then
        echo "停止现有服务..."
        docker compose down || true
    fi
    
    # 构建并启动服务
    echo "构建并启动服务..."
    docker compose up -d --build
    
    echo ""
    echo "等待服务启动..."
    sleep 30
    
    # 检查服务状态
    echo ""
    echo "服务状态:"
    docker compose ps
    
    echo ""
    echo -e "${GREEN}服务启动完成！${NC}"
}

# 显示访问信息
show_access_info() {
    echo ""
    echo "=========================================="
    echo "  部署完成！"
    echo "=========================================="
    echo ""
    echo "请通过以下地址访问系统："
    echo ""
    
    # 获取服务器 IP
    SERVER_IP=$(hostname -I | awk '{print $1}')
    if [ -z "$SERVER_IP" ]; then
        SERVER_IP="your-server-ip"
    fi
    
    echo -e "${GREEN}前端界面: http://$SERVER_IP${NC}"
    echo -e "${GREEN}后端 API: http://$SERVER_IP/api${NC}"
    echo ""
    echo "查看日志: docker compose logs -f"
    echo "停止服务: docker compose down"
    echo ""
}

# 主函数
main() {
    check_os
    install_docker
    clone_project
    configure_environment
    deploy_services
    show_access_info
}

# 运行主函数
main
