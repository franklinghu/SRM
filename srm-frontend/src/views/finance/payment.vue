<template>
  <div class="payment-list">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>付款申请</span>
          <el-button type="primary" @click="handleCreate">新增申请</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="申请编号">
          <el-input v-model="queryForm.requestNo" placeholder="请输入申请编号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="待审核" value="pending" />
            <el-option label="已审核" value="approved" />
            <el-option label="已驳回" value="rejected" />
            <el-option label="已支付" value="paid" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="requestNo" label="申请编号" width="140" />
        <el-table-column prop="amount" label="付款金额" width="140">
          <template #default="{ row }">
            ¥{{ row.amount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="paymentDate" label="付款日期" width="120" />
        <el-table-column prop="paymentMethod" label="付款方式" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryForm.pageNum"
        v-model:page-size="queryForm.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleQuery"
        @current-change="handleQuery"
      />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请编号" prop="requestNo">
              <el-input v-model="form.requestNo" placeholder="请输入申请编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款金额" prop="amount">
              <el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="付款日期" prop="paymentDate">
              <el-date-picker v-model="form.paymentDate" type="date" placeholder="选择付款日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款方式" prop="paymentMethod">
              <el-select v-model="form.paymentMethod" placeholder="请选择付款方式" style="width: 100%">
                <el-option label="银行转账" value="bank" />
                <el-option label="现金" value="cash" />
                <el-option label="支票" value="check" />
                <el-option label="在线支付" value="online" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="待审核" value="pending" />
                <el-option label="已审核" value="approved" />
                <el-option label="已驳回" value="rejected" />
                <el-option label="已支付" value="paid" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPaymentList, createPayment, updatePayment, deletePayment } from '@/api/finance'

const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => form.id ? '编辑付款申请' : '新增付款申请')

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10,
  requestNo: '',
  status: ''
})

const form = reactive({
  id: null,
  requestNo: '',
  supplierId: null,
  invoiceId: null,
  orderId: null,
  amount: null,
  paymentDate: null,
  paymentMethod: '',
  status: 'pending',
  remarks: ''
})

const rules = {
  requestNo: [{ required: true, message: '请输入申请编号', trigger: 'blur' }],
  amount: [{ required: true, message: '请输入付款金额', trigger: 'blur' }],
  paymentMethod: [{ required: true, message: '请选择付款方式', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const getStatusName = (status) => {
  const statusMap = {
    pending: '待审核',
    approved: '已审核',
    rejected: '已驳回',
    paid: '已支付'
  }
  return statusMap[status] || '-'
}

const getStatusType = (status) => {
  const typeMap = {
    pending: 'warning',
    approved: 'success',
    rejected: 'danger',
    paid: 'primary'
  }
  return typeMap[status] || 'info'
}

const loadData = async () => {
  try {
    const res = await getPaymentList(queryForm)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载付款申请列表失败:', error)
  }
}

const handleQuery = () => {
  loadData()
}

const handleReset = () => {
  queryForm.requestNo = ''
  queryForm.status = ''
  queryForm.pageNum = 1
  loadData()
}

const handleCreate = () => {
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该付款申请吗？', '提示', {
      type: 'warning'
    })
    await deletePayment(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除付款申请失败:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await updatePayment(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createPayment(form)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    requestNo: '',
    supplierId: null,
    invoiceId: null,
    orderId: null,
    amount: null,
    paymentDate: null,
    paymentMethod: '',
    status: 'pending',
    remarks: ''
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.payment-list {
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

.el-pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
