<template>
  <div class="contract-list">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>合同管理</span>
          <el-button type="primary" @click="handleCreate">新增合同</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="合同名称">
          <el-input v-model="queryForm.contractName" placeholder="请输入合同名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="draft" />
            <el-option label="执行中" value="active" />
            <el-option label="已完成" value="completed" />
            <el-option label="已终止" value="terminated" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="contractNo" label="合同编号" width="140" />
        <el-table-column prop="contractName" label="合同名称" min-width="200" />
        <el-table-column prop="contractType" label="合同类型" width="120" />
        <el-table-column prop="totalAmount" label="合同金额" width="140">
          <template #default="{ row }">
            ¥{{ row.totalAmount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="partyB" label="乙方" width="180" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model="form.contractNo" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同类型" prop="contractType">
              <el-select v-model="form.contractType" placeholder="请选择类型" style="width: 100%">
                <el-option label="采购合同" value="purchase" />
                <el-option label="服务合同" value="service" />
                <el-option label="框架协议" value="framework" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同金额" prop="totalAmount">
              <el-input-number v-model="form.totalAmount" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="选择开始日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" placeholder="选择结束日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="甲方" prop="partyA">
              <el-input v-model="form.partyA" placeholder="请输入甲方名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="乙方" prop="partyB">
              <el-input v-model="form.partyB" placeholder="请输入乙方名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="合同内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入合同内容" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="草稿" value="draft" />
                <el-option label="执行中" value="active" />
                <el-option label="已完成" value="completed" />
                <el-option label="已终止" value="terminated" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
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
import { getContractList, createContract, updateContract, deleteContract } from '@/api/contract'

const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => form.id ? '编辑合同' : '新增合同')

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10,
  contractName: '',
  status: ''
})

const form = reactive({
  id: null,
  contractNo: '',
  contractName: '',
  supplierId: null,
  contractType: '',
  totalAmount: null,
  startDate: null,
  endDate: null,
  signDate: '',
  partyA: '',
  partyB: '',
  content: '',
  attachmentUrl: '',
  status: 'draft'
})

const rules = {
  contractNo: [{ required: true, message: '请输入合同编号', trigger: 'blur' }],
  contractName: [{ required: true, message: '请输入合同名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const getStatusName = (status) => {
  const statusMap = {
    draft: '草稿',
    active: '执行中',
    completed: '已完成',
    terminated: '已终止'
  }
  return statusMap[status] || '-'
}

const getStatusType = (status) => {
  const typeMap = {
    draft: 'info',
    active: 'success',
    completed: 'primary',
    terminated: 'danger'
  }
  return typeMap[status] || 'info'
}

const loadData = async () => {
  try {
    const res = await getContractList(queryForm)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载合同列表失败:', error)
  }
}

const handleQuery = () => {
  loadData()
}

const handleReset = () => {
  queryForm.contractName = ''
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
    await ElMessageBox.confirm('确定要删除该合同吗？', '提示', {
      type: 'warning'
    })
    await deleteContract(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除合同失败:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await updateContract(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createContract(form)
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
    contractNo: '',
    contractName: '',
    supplierId: null,
    contractType: '',
    totalAmount: null,
    startDate: null,
    endDate: null,
    signDate: '',
    partyA: '',
    partyB: '',
    content: '',
    attachmentUrl: '',
    status: 'draft'
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.contract-list {
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
