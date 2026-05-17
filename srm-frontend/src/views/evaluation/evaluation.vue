<template>
  <div class="supplier-evaluation">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>供应商评估</span>
          <el-button type="primary" @click="handleCreate">新增评估</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="draft" />
            <el-option label="进行中" value="ongoing" />
            <el-option label="已完成" value="completed" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="evaluationNo" label="评估编号" width="140" />
        <el-table-column prop="supplierName" label="供应商" width="200" />
        <el-table-column prop="evaluationPeriod" label="评估周期" width="140" />
        <el-table-column prop="evaluationDate" label="评估日期" width="120" />
        <el-table-column prop="totalScore" label="总分" width="100">
          <template #default="{ row }">
            {{ row.totalScore }}
          </template>
        </el-table-column>
        <el-table-column prop="level" label="等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.level)">
              {{ row.level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="evaluator" label="评估人" width="120" />
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

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="供应商" prop="supplierId">
          <el-select v-model="form.supplierId" placeholder="请选择供应商" style="width: 100%" clearable>
            <el-option 
              v-for="supplier in supplierList" 
              :key="supplier.id" 
              :label="supplier.supplierName" 
              :value="supplier.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="评估编号" prop="evaluationNo">
          <el-input v-model="form.evaluationNo" placeholder="请输入评估编号" />
        </el-form-item>
        <el-form-item label="评估周期" prop="evaluationPeriod">
          <el-input v-model="form.evaluationPeriod" placeholder="如：2024年第一季度" />
        </el-form-item>
        <el-form-item label="评估日期" prop="evaluationDate">
          <el-date-picker v-model="form.evaluationDate" type="date" placeholder="选择评估日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="总分" prop="totalScore">
          <el-input-number v-model="form.totalScore" :min="0" :max="100" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="等级" prop="level">
          <el-select v-model="form.level" placeholder="请选择等级" style="width: 100%">
            <el-option label="A" value="A" />
            <el-option label="B" value="B" />
            <el-option label="C" value="C" />
            <el-option label="D" value="D" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估人" prop="evaluator">
          <el-input v-model="form.evaluator" placeholder="请输入评估人" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="草稿" value="draft" />
            <el-option label="进行中" value="ongoing" />
            <el-option label="已完成" value="completed" />
          </el-select>
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
import { getEvaluationList, createEvaluation, updateEvaluation, deleteEvaluation } from '@/api/evaluation'
import { getSupplierList } from '@/api/supplier'

const tableData = ref([])
const supplierList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => form.id ? '编辑评估' : '新增评估')

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10,
  status: ''
})

const form = reactive({
  id: null,
  supplierId: null,
  templateId: null,
  evaluationNo: '',
  evaluationPeriod: '',
  evaluationDate: null,
  totalScore: null,
  level: '',
  evaluator: '',
  remarks: '',
  status: 'draft'
})

const rules = {
  evaluationNo: [{ required: true, message: '请输入评估编号', trigger: 'blur' }],
  supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }]
}

const getStatusName = (status) => {
  const statusMap = {
    draft: '草稿',
    ongoing: '进行中',
    completed: '已完成'
  }
  return statusMap[status] || '-'
}

const getStatusType = (status) => {
  const typeMap = {
    draft: 'info',
    ongoing: 'warning',
    completed: 'success'
  }
  return typeMap[status] || 'info'
}

const getLevelType = (level) => {
  const typeMap = {
    A: 'success',
    B: 'primary',
    C: 'warning',
    D: 'danger'
  }
  return typeMap[level] || 'info'
}

const loadSuppliers = async () => {
  try {
    const res = await getSupplierList({ pageNum: 1, pageSize: 1000 })
    supplierList.value = res.data.records || []
  } catch (error) {
    console.error('加载供应商列表失败:', error)
  }
}

const loadData = async () => {
  try {
    const res = await getEvaluationList(queryForm)
    const records = res.data.records || []
    tableData.value = records.map(item => {
      const supplier = supplierList.value.find(s => s.id === item.supplierId)
      return {
        ...item,
        supplierName: supplier?.supplierName || '-'
      }
    })
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载评估列表失败:', error)
  }
}

const handleQuery = () => {
  loadData()
}

const handleReset = () => {
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
    await ElMessageBox.confirm('确定要删除该评估吗？', '提示', {
      type: 'warning'
    })
    await deleteEvaluation(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除评估失败:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await updateEvaluation(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createEvaluation(form)
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
    supplierId: null,
    templateId: null,
    evaluationNo: '',
    evaluationPeriod: '',
    evaluationDate: null,
    totalScore: null,
    level: '',
    evaluator: '',
    remarks: '',
    status: 'draft'
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  loadSuppliers().then(() => loadData())
})
</script>

<style scoped>
.supplier-evaluation {
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
