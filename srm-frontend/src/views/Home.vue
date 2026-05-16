<template>
  <div class="home-container">
    <el-container>
      <el-header class="header">
        <h1>SRM供应商关系管理系统</h1>
      </el-header>
      <el-main class="main">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-card class="welcome-card">
              <h2>欢迎使用SRM系统</h2>
              <p>项目脚手架已成功创建！</p>
              <div class="tech-stack">
                <el-tag type="primary" size="large">Spring Boot 3</el-tag>
                <el-tag type="success" size="large">Vue 3</el-tag>
                <el-tag type="info" size="large">MySQL 8</el-tag>
                <el-tag type="warning" size="large">Redis</el-tag>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
</script>

<style scoped>
.home-container {
  height: 100%;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.main {
  padding: 40px;
  background: #f0f2f5;
}

.welcome-card {
  text-align: center;
  padding: 40px;
}

.welcome-card h2 {
  margin-bottom: 20px;
  color: #333;
}

.welcome-card p {
  font-size: 18px;
  color: #666;
  margin-bottom: 30px;
}

.tech-stack .el-tag {
  margin: 0 10px;
}
</style>
