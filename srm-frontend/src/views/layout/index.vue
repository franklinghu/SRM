<template>
  <div class="layout-container">
    <el-container>
      <el-aside width="200px">
        <div class="logo">SRM系统</div>
        <el-menu
          :default-active="activeMenu"
          router
        >
          <el-menu-item index="/dashboard">
            <span>工作台</span>
          </el-menu-item>
          <el-menu-item index="/supplier">
            <span>供应商管理</span>
          </el-menu-item>
          <el-sub-menu index="purchase">
            <template #title>
              <span>采购管理</span>
            </template>
            <el-menu-item index="/purchase/request">
              <span>采购需求</span>
            </el-menu-item>
            <el-menu-item index="/purchase/order">
              <span>采购订单</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/contract">
            <span>合同管理</span>
          </el-menu-item>
          <el-sub-menu index="evaluation">
            <template #title>
              <span>绩效评估</span>
            </template>
            <el-menu-item index="/evaluation/template">
              <span>评估模板</span>
            </el-menu-item>
            <el-menu-item index="/evaluation/evaluation">
              <span>供应商评估</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="finance">
            <template #title>
              <span>财务管理</span>
            </template>
            <el-menu-item index="/finance/invoice">
              <span>发票管理</span>
            </el-menu-item>
            <el-menu-item index="/finance/payment">
              <span>付款申请</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                {{ userStore.userInfo?.realName || '用户' }}
                <el-icon><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/modules/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  background: #545c64;
  color: #fff;
  font-size: 18px;
}

.el-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
}

.el-dropdown-link {
  cursor: pointer;
}
</style>
