<template>
  <div class="request-list">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>采购需求管理</span>
          <el-button type="primary" @click="handleCreate">新增需求</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="需求标题">
          <el-input v-model="queryForm.title" placeholder="请输入需求标题" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="draft" />
            <el-option label="需求确认" value="demand" />
            <el-option label="询价中" value="quote" />
            <el-option label="已审批" value="approved" />
            <el-option label="已关闭" value="closed" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="requestNo" label="需求单号" width="150" />
        <el-table-column prop="title" label="需求标题" min-width="200" />
        <el-table-column prop="requestType" label="需求类型" width="100" />
        <el-table-column prop="department" label="需求部门" width="120" />
        <el-table-column prop="totalAmount" label="预算金额" width="120">
          <template #default="{ row }">
            ¥{{ row.totalAmount?.toFixed(2) || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80">
          <template #default="{ row }">
            <el-tag :type="getPriorityType(row.priority)">
              {{ getPriorityName(row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expectedDate" label="期望交付日期" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getRequestList, deleteRequest } from '@/api/purchase/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const queryForm = reactive({
  title: '',
  status: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])

const getPriorityName = (priority) => {
  const names = { 1: '紧急', 2: '普通', 3: '低' }
  return names[priority] || '-'
}

const getPriorityType = (priority) => {
  const types = { 1: 'danger', 2: 'primary', 3: 'info' }
  return types[priority] || 'info'
}

const getStatusName = (status) => {
  const statuses = {
    draft: '草稿',
    demand: '需求确认',
    quote: '询价中',
    approved: '已审批',
    closed: '已关闭'
  }
  return statuses[status] || '-'
}

const getStatusType = (status) => {
  const types = {
    draft: 'info',
    demand: 'warning',
    quote: 'primary',
    approved: 'success',
    closed: 'default'
  }
  return types[status] || 'info'
}

const loadData = async () => {
  try {
    const res = await getRequestList({
      ...queryForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

const handleQuery = () => {
  pagination.pageNum = 1
  loadData()
}

const handleReset = () => {
  queryForm.title = ''
  queryForm.status = ''
  handleQuery()
}

const handleCreate = () => {
  ElMessage.info('新增功能待实现')
}

const handleView = (row) => {
  ElMessage.info('查看功能待实现')
}

const handleEdit = (row) => {
  ElMessage.info('编辑功能待实现')
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该采购需求吗？', '提示', {
      type: 'warning'
    })
    await deleteRequest(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  loadData()
}

const handleCurrentChange = (val) => {
  pagination.pageNum = val
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.request-list {
  padding: 20px;
}

.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.query-form {
  margin-bottom: 20px;
}
</style>
