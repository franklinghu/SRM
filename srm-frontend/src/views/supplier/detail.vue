<template>
  <div class="supplier-detail">
    <el-page-header @back="goBack" content="供应商详情" />
    
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="供应商编码">{{ supplier.supplierCode || '-' }}</el-descriptions-item>
            <el-descriptions-item label="供应商名称">{{ supplier.supplierName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="简称">{{ supplier.shortName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="类型">{{ getTypeName(supplier.supplierType) }}</el-descriptions-item>
            <el-descriptions-item label="所属行业">{{ supplier.industry || '-' }}</el-descriptions-item>
            <el-descriptions-item label="信用等级">{{ supplier.creditLevel || '-' }}</el-descriptions-item>
            <el-descriptions-item label="联系人">{{ supplier.contactName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ supplier.contactPhone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="联系邮箱">{{ supplier.contactEmail || '-' }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusType(supplier.status)">
                {{ getStatusName(supplier.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="国家">{{ supplier.country || '-' }}</el-descriptions-item>
            <el-descriptions-item label="省份">{{ supplier.province || '-' }}</el-descriptions-item>
            <el-descriptions-item label="城市">{{ supplier.city || '-' }}</el-descriptions-item>
            <el-descriptions-item label="官网">{{ supplier.website || '-' }}</el-descriptions-item>
            <el-descriptions-item label="税号">{{ supplier.taxNumber || '-' }}</el-descriptions-item>
            <el-descriptions-item label="开户银行">{{ supplier.bankName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="银行账号">{{ supplier.bankAccount || '-' }}</el-descriptions-item>
            <el-descriptions-item label="员工人数">{{ supplier.employeeCount || '-' }}</el-descriptions-item>
            <el-descriptions-item label="详细地址" :span="2">{{ supplier.address || '-' }}</el-descriptions-item>
            <el-descriptions-item label="公司简介" :span="2">{{ supplier.intro || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <!-- 联系人管理 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>联系人管理</span>
              <el-button type="primary" size="small" @click="handleAddContact">新增联系人</el-button>
            </div>
          </template>
          <el-table :data="contacts" border stripe>
            <el-table-column prop="contactName" label="姓名" width="120" />
            <el-table-column prop="department" label="部门" width="120" />
            <el-table-column prop="position" label="职位" width="120" />
            <el-table-column prop="phone" label="电话" width="130" />
            <el-table-column prop="mobile" label="手机" width="130" />
            <el-table-column prop="email" label="邮箱" />
            <el-table-column prop="isPrimary" label="主联系人" width="100">
              <template #default="{ row }">
                <el-tag v-if="row.isPrimary" type="success">是</el-tag>
                <el-tag v-else>否</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleEditContact(row)">编辑</el-button>
                <el-button type="danger" link @click="handleDeleteContact(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 资质证书管理 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>资质证书管理</span>
              <el-button type="primary" size="small" @click="handleAddCertificate">新增证书</el-button>
            </div>
          </template>
          <el-table :data="certificates" border stripe>
            <el-table-column prop="certType" label="资质类型" width="120" />
            <el-table-column prop="certName" label="证书名称" />
            <el-table-column prop="certNumber" label="证书编号" width="150" />
            <el-table-column prop="issueDate" label="发证日期" width="120" />
            <el-table-column prop="expireDate" label="到期日期" width="120" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                  {{ row.status === 1 ? '有效' : '失效' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleEditCertificate(row)">编辑</el-button>
                <el-button type="danger" link @click="handleDeleteCertificate(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 联系人对话框 -->
    <el-dialog
      v-model="contactDialogVisible"
      :title="contactDialogTitle"
      width="600px"
      @close="resetContactForm"
    >
      <el-form ref="contactFormRef" :model="contactForm" :rules="contactRules" label-width="100px">
        <el-form-item label="姓名" prop="contactName">
          <el-input v-model="contactForm.contactName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input v-model="contactForm.department" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="contactForm.position" placeholder="请输入职位" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="contactForm.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="手机" prop="mobile">
          <el-input v-model="contactForm.mobile" placeholder="请输入手机" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="contactForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="主联系人" prop="isPrimary">
          <el-switch v-model="contactForm.isPrimary" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="contactDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitContact" :loading="contactSubmitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 资质证书对话框 -->
    <el-dialog
      v-model="certificateDialogVisible"
      :title="certificateDialogTitle"
      width="600px"
      @close="resetCertificateForm"
    >
      <el-form ref="certificateFormRef" :model="certificateForm" :rules="certificateRules" label-width="100px">
        <el-form-item label="资质类型" prop="certType">
          <el-input v-model="certificateForm.certType" placeholder="请输入资质类型" />
        </el-form-item>
        <el-form-item label="证书名称" prop="certName">
          <el-input v-model="certificateForm.certName" placeholder="请输入证书名称" />
        </el-form-item>
        <el-form-item label="证书编号" prop="certNumber">
          <el-input v-model="certificateForm.certNumber" placeholder="请输入证书编号" />
        </el-form-item>
        <el-form-item label="发证日期" prop="issueDate">
          <el-date-picker
            v-model="certificateForm.issueDate"
            type="date"
            placeholder="请选择发证日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="到期日期" prop="expireDate">
          <el-date-picker
            v-model="certificateForm.expireDate"
            type="date"
            placeholder="请选择到期日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="文件URL" prop="fileUrl">
          <el-input v-model="certificateForm.fileUrl" placeholder="请输入文件URL" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="certificateForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="有效" :value="1" />
            <el-option label="失效" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="certificateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitCertificate" :loading="certificateSubmitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getSupplierDetail,
  getSupplierContacts,
  createSupplierContact,
  updateSupplierContact,
  deleteSupplierContact,
  getSupplierCertificates,
  createSupplierCertificate,
  updateSupplierCertificate,
  deleteSupplierCertificate
} from '@/api/supplier'

const router = useRouter()
const route = useRoute()

const supplier = ref({})
const contacts = ref([])
const certificates = ref([])

const contactDialogVisible = ref(false)
const contactSubmitLoading = ref(false)
const contactFormRef = ref(null)
const certificateDialogVisible = ref(false)
const certificateSubmitLoading = ref(false)
const certificateFormRef = ref(null)

const contactForm = reactive({
  id: null,
  supplierId: null,
  contactName: '',
  department: '',
  position: '',
  phone: '',
  mobile: '',
  email: '',
  isPrimary: 0
})

const certificateForm = reactive({
  id: null,
  supplierId: null,
  certType: '',
  certName: '',
  certNumber: '',
  issueDate: '',
  expireDate: '',
  fileUrl: '',
  status: 1
})

const contactRules = {
  contactName: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
}

const certificateRules = {
  certName: [{ required: true, message: '请输入证书名称', trigger: 'blur' }]
}

const contactDialogTitle = computed(() => contactForm.id ? '编辑联系人' : '新增联系人')
const certificateDialogTitle = computed(() => certificateForm.id ? '编辑证书' : '新增证书')

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

const loadSupplierDetail = async () => {
  try {
    const id = route.params.id
    const res = await getSupplierDetail(id)
    supplier.value = res.data
  } catch (error) {
    console.error('加载供应商详情失败:', error)
  }
}

const loadContacts = async () => {
  try {
    const id = route.params.id
    const res = await getSupplierContacts(id)
    contacts.value = res.data || []
  } catch (error) {
    console.error('加载联系人失败:', error)
  }
}

const loadCertificates = async () => {
  try {
    const id = route.params.id
    const res = await getSupplierCertificates(id)
    certificates.value = res.data || []
  } catch (error) {
    console.error('加载资质证书失败:', error)
  }
}

const goBack = () => {
  router.push('/supplier')
}

const handleAddContact = () => {
  resetContactForm()
  contactForm.supplierId = route.params.id
  contactDialogVisible.value = true
}

const handleEditContact = (row) => {
  Object.assign(contactForm, row)
  contactDialogVisible.value = true
}

const handleDeleteContact = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该联系人吗？', '提示', {
      type: 'warning'
    })
    await deleteSupplierContact(row.id)
    ElMessage.success('删除成功')
    loadContacts()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除联系人失败:', error)
    }
  }
}

const handleSubmitContact = async () => {
  try {
    await contactFormRef.value.validate()
    contactSubmitLoading.value = true
    
    if (contactForm.id) {
      await updateSupplierContact(contactForm.id, contactForm)
      ElMessage.success('更新成功')
    } else {
      await createSupplierContact(route.params.id, contactForm)
      ElMessage.success('创建成功')
    }
    
    contactDialogVisible.value = false
    loadContacts()
  } catch (error) {
    console.error('提交联系人失败:', error)
  } finally {
    contactSubmitLoading.value = false
  }
}

const resetContactForm = () => {
  Object.assign(contactForm, {
    id: null,
    supplierId: null,
    contactName: '',
    department: '',
    position: '',
    phone: '',
    mobile: '',
    email: '',
    isPrimary: 0
  })
  contactFormRef.value?.resetFields()
}

const handleAddCertificate = () => {
  resetCertificateForm()
  certificateForm.supplierId = route.params.id
  certificateDialogVisible.value = true
}

const handleEditCertificate = (row) => {
  Object.assign(certificateForm, row)
  certificateDialogVisible.value = true
}

const handleDeleteCertificate = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该证书吗？', '提示', {
      type: 'warning'
    })
    await deleteSupplierCertificate(row.id)
    ElMessage.success('删除成功')
    loadCertificates()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除证书失败:', error)
    }
  }
}

const handleSubmitCertificate = async () => {
  try {
    await certificateFormRef.value.validate()
    certificateSubmitLoading.value = true
    
    if (certificateForm.id) {
      await updateSupplierCertificate(certificateForm.id, certificateForm)
      ElMessage.success('更新成功')
    } else {
      await createSupplierCertificate(route.params.id, certificateForm)
      ElMessage.success('创建成功')
    }
    
    certificateDialogVisible.value = false
    loadCertificates()
  } catch (error) {
    console.error('提交证书失败:', error)
  } finally {
    certificateSubmitLoading.value = false
  }
}

const resetCertificateForm = () => {
  Object.assign(certificateForm, {
    id: null,
    supplierId: null,
    certType: '',
    certName: '',
    certNumber: '',
    issueDate: '',
    expireDate: '',
    fileUrl: '',
    status: 1
  })
  certificateFormRef.value?.resetFields()
}

onMounted(() => {
  loadSupplierDetail()
  loadContacts()
  loadCertificates()
})
</script>

<style scoped>
.supplier-detail {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
