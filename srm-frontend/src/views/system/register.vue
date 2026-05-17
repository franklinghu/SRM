<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="header">
          <h2>供应商自助注册</h2>
          <el-tag type="info">提交注册申请，等待审批通过后即可登录</el-tag>
        </div>
      </template>
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="120px"
        size="large"
      >
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入公司全称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
                <el-option label="生产型供应商" value="生产型" />
                <el-option label="贸易型供应商" value="贸易型" />
                <el-option label="服务型供应商" value="服务型" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属行业" prop="industry">
              <el-select v-model="form.industry" placeholder="请选择行业" style="width: 100%">
                <el-option label="电子元器件" value="电子元器件" />
                <el-option label="机械设备" value="机械设备" />
                <el-option label="化工材料" value="化工材料" />
                <el-option label="五金工具" value="五金工具" />
                <el-option label="办公用品" value="办公用品" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在地区" prop="region">
              <el-input v-model="form.region" placeholder="如：广东省深圳市" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="公司简介" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请简单介绍公司主营业务、产品或服务"
          />
        </el-form-item>

        <el-divider content-position="left">联系人信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人姓名" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
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
            <el-form-item label="电子邮箱" prop="contactEmail">
              <el-input v-model="form.contactEmail" placeholder="请输入电子邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位" prop="contactPosition">
              <el-input v-model="form.contactPosition" placeholder="请输入联系人职位" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">资质信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="营业执照号" prop="businessLicense">
              <el-input v-model="form.businessLicense" placeholder="请输入统一社会信用代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注册资本" prop="registeredCapital">
              <el-input v-model="form.registeredCapital" placeholder="请输入注册资本（万元）">
                <template #append>万元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主营产品" prop="mainProducts">
              <el-input v-model="form.mainProducts" placeholder="请输入主营产品/服务" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年营业额" prop="annualRevenue">
              <el-input v-model="form.annualRevenue" placeholder="请输入年营业额（万元）">
                <template #append>万元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">账号信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="设置登录用户名">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="设置登录密码"
                show-password
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            show-password
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" style="width: 200px" @click="handleSubmit">
            提交注册申请
          </el-button>
          <el-button @click="handleBack">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { submitSupplierApplication } from '@/api/supplier'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  name: '',
  type: '',
  industry: '',
  region: '',
  address: '',
  description: '',
  contactName: '',
  contactPhone: '',
  contactEmail: '',
  contactPosition: '',
  businessLicense: '',
  registeredCapital: '',
  mainProducts: '',
  annualRevenue: '',
  username: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const validatePhone = (rule, value, callback) => {
  const phoneReg = /^1[3-9]\d{9}$/
  if (!value) {
    callback(new Error('请输入联系电话'))
  } else if (!phoneReg.test(value)) {
    callback(new Error('请输入正确的手机号码'))
  } else {
    callback()
  }
}

const rules = {
  name: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择供应商类型', trigger: 'change' }],
  industry: [{ required: true, message: '请选择所属行业', trigger: 'change' }],
  region: [{ required: true, message: '请输入所在地区', trigger: 'blur' }],
  contactName: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }],
  contactPhone: [{ required: true, validator: validatePhone, trigger: 'blur' }],
  contactEmail: [
    { required: true, message: '请输入电子邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  businessLicense: [{ required: true, message: '请输入营业执照号', trigger: 'blur' }],
  username: [
    { required: true, message: '请设置用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '用户名长度4-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请设置密码', trigger: 'blur' },
    { min: 6, message: '密码至少6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const submitData = { ...form }
    delete submitData.confirmPassword
    await submitSupplierApplication(submitData)
    ElMessage.success('注册申请提交成功！请等待管理员审批。')
    router.push('/login')
  } catch (error) {
    ElMessage.error(error.message || '提交失败，请重试')
  } finally {
    loading.value = false
  }
}

const handleBack = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  padding: 40px 20px;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-card {
  width: 900px;
  max-width: 100%;
}

.header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.header h2 {
  margin: 0;
}

:deep(.el-divider__text) {
  font-weight: bold;
  color: #409eff;
}
</style>
