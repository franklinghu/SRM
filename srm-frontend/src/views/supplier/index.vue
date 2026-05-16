<template>
  <div class="supplier-list">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>供应商管理</span>
          <el-button type="primary" @click="handleCreate">新增供应商</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="供应商名称">
          <el-input v-model="queryForm.supplierName" placeholder="请输入供应商名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
            <el-option label="待审核" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="supplierCode" label="供应商编码" width="120" />
        <el-table-column prop="supplierName" label="供应商名称" min-width="200" />
        <el-table-column prop="shortName" label="简称" width="120" />
        <el-table-column prop="supplierType" label="类型" width="100">
          <template #default="{ row }">
            {{ getTypeName(row.supplierType) }}
          </template>
        </el-table-column>
        <el-table-column prop="industry" label="行业" width="120" />
        <el-table-column prop="contactName" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="creditLevel" label="信用等级" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
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
            <el-form-item label="供应商编码" prop="supplierCode">
              <el-input v-model="form.supplierCode" placeholder="请输入供应商编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商名称" prop="supplierName">
              <el-input v-model="form.supplierName" placeholder="请输入供应商名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="简称" prop="shortName">
              <el-input v-model="form.shortName" placeholder="请输入简称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型" prop="supplierType">
              <el-select v-model="form.supplierType" placeholder="请选择类型" style="width: 100%">
                <el-option label="制造商" :value="1" />
                <el-option label="代理商" :value="2" />
                <el-option label="服务商" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属行业" prop="industry">
              <el-input v-model="form.industry" placeholder="请输入所属行业" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="信用等级" prop="creditLevel">
              <el-select v-model="form.creditLevel" placeholder="请选择信用等级" style="width: 100%">
                <el-option label="A" value="A" />
                <el-option label="B" value="B" />
                <el-option label="C" value="C" />
                <el-option label="D" value="D" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
                <el-option label="待审核" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="国家" prop="country">
              <el-input v-model="form.country" placeholder="请输入国家" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="省份" prop="province">
              <el-input v-model="form.province" placeholder="请输入省份" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="城市" prop="city">
              <el-input v-model="form.city" placeholder="请输入城市" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="官网" prop="website">
              <el-input v-model="form.website" placeholder="请输入官网" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" type="textarea" :rows="2" placeholder="请输入详细地址" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="税号" prop="taxNumber">
              <el-input v-model="form.taxNumber" placeholder="请输入税号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开户银行" prop="bankName">
              <el-input v-model="form.bankName" placeholder="请输入开户银行" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="银行账号" prop="bankAccount">
              <el-input v-model="form.bankAccount" placeholder="请输入银行账号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="员工人数" prop="employeeCount">
              <el-input-number v-model="form.employeeCount" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="公司简介" prop="intro">
          <el-input v-model="form.intro" type="textarea" :rows="3" placeholder="请输入公司简介" />
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
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSupplierList, createSupplier, updateSupplier, deleteSupplier } from '@/api/supplier'

const router = useRouter()

const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => form.id ? '编辑供应商' : '新增供应商')

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10,
  supplierName: '',
  status: null
})

const form = reactive({
  id: null,
  supplierCode: '',
  supplierName: '',
  shortName: '',
  supplierType: null,
  industry: '',
  country: '',
  province: '',
  city: '',
  address: '',
  contactName: '',
  contactPhone: '',
  contactEmail: '',
  website: '',
  taxNumber: '',
  bankName: '',
  bankAccount: '',
  creditLevel: '',
  status: 1,
  registerCapital: null,
  employeeCount: null,
  intro: ''
})

const rules = {
  supplierCode: [{ required: true, message: '请输入供应商编码', trigger: 'blur' }],
  supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const getTypeName = (type) => {
  const typeMap = {
    1: '制造商',
    2: '代理商',
    3: '服务商'
  }
  return typeMap[type] || '-'
}

const getStatusName = (status) => {
  const statusMap = {
    0: '禁用',
    1: '启用',
    2: '待审核'
  }
  return statusMap[status] || '-'
}

const getStatusType = (status) => {
  const typeMap = {
    0: 'danger',
    1: 'success',
    2: 'warning'
  }
  return typeMap[status] || 'info'
}

const loadData = async () => {
  try {
    const res = await getSupplierList(queryForm)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载供应商列表失败:', error)
  }
}

const handleQuery = () => {
  loadData()
}

const handleReset = () => {
  queryForm.supplierName = ''
  queryForm.status = null
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

const handleView = (row) => {
  router.push(`/supplier/${row.id}`)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该供应商吗？', '提示', {
      type: 'warning'
    })
    await deleteSupplier(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除供应商失败:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await updateSupplier(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createSupplier(form)
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
    supplierCode: '',
    supplierName: '',
    shortName: '',
    supplierType: null,
    industry: '',
    country: '',
    province: '',
    city: '',
    address: '',
    contactName: '',
    contactPhone: '',
    contactEmail: '',
    website: '',
    taxNumber: '',
    bankName: '',
    bankAccount: '',
    creditLevel: '',
    status: 1,
    registerCapital: null,
    employeeCount: null,
    intro: ''
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.supplier-list {
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
