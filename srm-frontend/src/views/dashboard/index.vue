<template>
  <div class="dashboard">
    <h1>工作台</h1>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <template #header>待审批</template>
          <div class="stat-number">5</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>待付款</template>
          <div class="stat-number">12</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>合同到期</template>
          <div class="stat-number">3</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>供应商总数</template>
          <div class="stat-number">128</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/store/modules/user'

const userStore = useUserStore()

onMounted(async () => {
  await userStore.getUserInfo()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  text-align: center;
  padding: 20px 0;
  color: #409eff;
}
</style>
