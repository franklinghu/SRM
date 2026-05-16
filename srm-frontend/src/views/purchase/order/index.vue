<template>
  <div class="order-list">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>采购订单管理</span>
          <el-button type="primary" @click="handleCreate">新增订单</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="订单编号">
          <el-input v-model="queryForm.orderNo" placeholder="请输入订单编号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="待处理" value="pending" />
            <el-option label="处理中" value="processing" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="orderNo" label="订单编号" width="150" />
        <el-table-column prop="supplierId" label="供应商ID" width="100" />
        <el-table-column prop="orderType" label="订单类型" width="100" />
        <el-table-column prop="orderAmount" label="订单金额" width="120">
          <template #default="{ row }">
            ¥{{ row.orderAmount?.toFixed(2) || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="付款状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getPaymentStatusType(row.paymentStatus)">
              {{ getPaymentStatusName(row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deliveryStatus" label="交货状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getDeliveryStatusType(row.deliveryStatus)">
              {{ getDeliveryStatusName(row.deliveryStatus) }}
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
        <el-table-column prop="expectedDate" label="期望交货日期" width="120" />
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
import { getOrderList, deleteOrder } from '@/api/purchase/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const queryForm = reactive({
  orderNo: '',
  status: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])

const getPaymentStatusName = (status) => {
  const statuses = {
    unpaid: '未付款',
    partial: '部分付款',
    paid: '已付款'
  }
  return statuses[status] || '-'
}

const getPaymentStatusType = (status) => {
  const types = {
    unpaid: 'danger',
    partial: 'warning',
    paid: 'success'
  }
  return types[status] || 'info'
}

const getDeliveryStatusName = (status) => {
  const statuses = {
    undelivered: '未交货',
    partial: '部分交货',
    delivered: '已交货'
  }
  return statuses[status] || '-'
}

const getDeliveryStatusType = (status) => {
  const types = {
    undelivered: 'danger',
    partial: 'warning',
    delivered: 'success'
  }
  return types[status] || 'info'
}

const getStatusName = (status) => {
  const statuses = {
    pending: '待处理',
    processing: '处理中',
    completed: '已完成',
    cancelled: '已取消'
  }
  return statuses[status] || '-'
}

const getStatusType = (status) => {
  const types = {
    pending: 'info',
    processing: 'warning',
    completed: 'success',
    cancelled: 'default'
  }
  return types[status] || 'info'
}

const loadData = async () => {
  try {
    const res = await getOrderList({
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
  queryForm.orderNo = ''
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
    await ElMessageBox.confirm('确认删除该采购订单吗？', '提示', {
      type: 'warning'
    })
    await deleteOrder(row.id)
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
.order-list {
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
