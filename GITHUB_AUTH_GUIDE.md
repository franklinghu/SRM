# GitHub 认证指南

## 当前状态
✅ GitHub 认证已正常工作！

## GitHub 个人访问令牌 (PAT) 说明

### 为什么需要 PAT？
GitHub 不再支持密码认证，必须使用个人访问令牌。

### 如何生成新的 PAT

1. 访问：https://github.com/settings/tokens
2. 点击 "Generate new token" → "Generate new token (classic)"
3. 设置 token 名称和过期时间
4. 勾选必要的权限：
   - `repo` (完全控制仓库)
   - `workflow` (如果需要 GitHub Actions)
5. 点击 "Generate token"
6. **重要：立即复制保存 token，只显示一次！**

### 当前仓库配置

```bash
# 查看当前远程仓库配置
git remote -v
```

当前配置：
- 远程仓库：`https://github.com/franklinghu/SRM.git`
- 认证方式：PAT 嵌入 URL

## 使用 Git 的最佳实践

### 方法 1：使用 credential helper（推荐）

```bash
# 配置 credential helper 缓存密码
git config --global credential.helper cache

# 或者永久保存（仅在安全环境使用）
git config --global credential.helper store
```

### 方法 2：使用 SSH 密钥（更安全）

```bash
# 生成 SSH 密钥
ssh-keygen -t ed25519 -C "your-email@example.com"

# 复制公钥到 GitHub
cat ~/.ssh/id_ed25519.pub
# 然后在 https://github.com/settings/keys 添加 SSH key

# 更改远程仓库为 SSH 方式
git remote set-url origin git@github.com:franklinghu/SRM.git
```

### 方法 3：使用环境变量

```bash
# 设置 GitHub token 环境变量
export GITHUB_TOKEN="your-token-here"

# 使用时
git clone https://$GITHUB_TOKEN@github.com/franklinghu/SRM.git
```

## 部署脚本中的令牌处理

我们的 `deploy-aliyun.sh` 脚本使用公共 HTTPS URL，不包含硬编码的令牌，这样更安全。

### 服务器上的认证选项

**选项 A：在服务器上配置 credential helper**
```bash
# 在服务器上执行
git config --global credential.helper store

# 第一次 push 时输入 token，之后会保存
git push
```

**选项 B：使用 SSH（推荐用于长期部署）**
```bash
# 服务器上生成 SSH key 并添加到 GitHub
ssh-keygen
cat ~/.ssh/id_rsa.pub
# 在 GitHub 添加 SSH key
```

**选项 C：单次使用令牌**
```bash
# 克隆时使用令牌
git clone https://your-token@github.com/franklinghu/SRM.git
```

## 安全提示

⚠️ **重要安全提示：**
1. 不要将 PAT 提交到代码仓库
2. 定期轮换 PAT
3. 使用最小权限原则
4. 设置合理的过期时间
5. 不要在共享环境中使用 `store` 模式

## 故障排查

### 如果遇到认证错误：

1. 检查 token 是否过期
2. 确认 token 有正确的权限
3. 检查远程仓库 URL 格式
4. 尝试重新生成 token

```bash
# 验证 token 是否有效
curl -H "Authorization: token YOUR_TOKEN" https://api.github.com/user
```

## 相关文档

- GitHub 官方文档：https://docs.github.com/en/authentication
- 创建 PAT：https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token
