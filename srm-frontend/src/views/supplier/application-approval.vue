<template>
  <div class="application-approval">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>供应商注册审批</span>
          <el-button type="primary" @click="loadData">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="供应商名称">
          <el-input v-model="queryForm.supplierName" placeholder="请输入供应商名称" clearable />
        </el-form-item>
        <el-form-item label="审批状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="待审批" value="pending" />
            <el-option label="已通过" value="approved" />
            <el-option label="已拒绝" value="rejected" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="申请ID" width="80" />
        <el-table-column prop="supplierName" label="供应商名称" min-width="180" />
        <el-table-column prop="supplierType" label="类型" width="100" />
        <el-table-column prop="industry" label="行业" width="100" />
        <el-table-column prop="contactName" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="username" label="注册账号" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="申请时间" width="170">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看详情</el-button>
            <template v-if="row.status === 'pending'">
              <el-button type="success" link @click="handleApprove(row)">通过</el-button>
              <el-button type="danger" link @click="handleReject(row)">拒绝</el-button>
            </template>
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

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="供应商注册申请详情" width="700px">
      <el-descriptions :column="2" border v-if="currentApplication">
        <el-descriptions-item label="申请ID">{{ currentApplication.id }}</el-descriptions-item>
        <el-descriptions-item label="注册账号">{{ currentApplication.username }}</el-descriptions-item>
        <el-descriptions-item label="供应商名称" :span="2">{{ currentApplication.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ currentApplication.supplierType }}</el-descriptions-item>
        <el-descriptions-item label="行业">{{ currentApplication.industry }}</el-descriptions-item>
        <el-descriptions-item label="所在地区">{{ currentApplication.region }}</el-descriptions-item>
        <el-descriptions-item label="详细地址" :span="2">{{ currentApplication.address }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ currentApplication.contactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentApplication.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="电子邮箱">{{ currentApplication.contactEmail }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ currentApplication.contactPosition }}</el-descriptions-item>
        <el-descriptions-item label="营业执照号">{{ currentApplication.businessLicense }}</el-descriptions-item>
        <el-descriptions-item label="注册资本">{{ currentApplication.registeredCapital }}万元</el-descriptions-item>
        <el-descriptions-item label="主营产品" :span="2">{{ currentApplication.mainProducts }}</el-descriptions-item>
        <el-descriptions-item label="年营业额">{{ currentApplication.annualRevenue }}万元</el-descriptions-item>
        <el-descriptions-item label="公司简介" :span="2">{{ currentApplication.description }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ formatDate(currentApplication.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="getStatusType(currentApplication.status)">
            {{ getStatusName(currentApplication.status) }}
          </el-tag>
        </el-descriptions-item>
        <template v-if="currentApplication.approvalRecords && currentApplication.approvalRecords.length > 0">
          <el-descriptions-item label="审批记录" :span="2">
            <el-timeline>
              <el-timeline-item
                v-for="(record, index) in currentApplication.approvalRecords"
                :key="index"
                :timestamp="formatDate(record.createTime)"
                :color="record.status === 2 ? '#67C23A' : record.status === 3 ? '#F56C6C' : '#409EFF'"
              >
                <p><strong>{{ record.stepName }}</strong> - {{ record.approverName }}</p>
                <p>{{ record.comment }}</p>
              </el-timeline-item>
            </el-timeline>
          </el-descriptions-item>
        </template>
      </el-descriptions>
      <template #footer v-if="currentApplication && (currentApplication.status === 0 || currentApplication.status === 1)">
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="danger" @click="handleReject(currentApplication)">拒绝</el-button>
        <el-button type="success" @click="handleApprove(currentApplication)">批准</el-button>
      </template>
      <template #footer v-else>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog v-model="approvalDialogVisible" :title="approvalTitle" width="500px">
      <el-form :model="approvalForm" :rules="approvalRules" ref="approvalFormRef" label-width="100px">
        <el-form-item label="审批意见" prop="comment">
          <el-input
            v-model="approvalForm.comment"
            type="textarea"
            :rows="4"
            :placeholder="approvalType === 'approve' ? '请输入批准意见（可选）' : '请输入拒绝原因'"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApproval" :loading="submitLoading">
          确认{{ approvalType === 'approve' ? '批准' : '拒绝' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getApplicationList, getApplicationDetail, approveApplication, rejectApplication } from '@/api/supplier'

const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const detailVisible = ref(false)
const approvalDialogVisible = ref(false)
const submitLoading = ref(false)
const currentApplication = ref(null)
const approvalType = ref('approve')
const approvalFormRef = ref(null)

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10,
  supplierName: '',
  status: null
})

const approvalForm = reactive({
  comment: ''
})

const approvalRules = {
  comment: [{ required: approvalType.value === 'reject', message: '请输入拒绝原因', trigger: 'blur' }]
}

const approvalTitle = ref('审批')

const getStatusName = (status) => {
  const statusMap = {
    'pending': '待审批',
    'approved': '已通过',
    'rejected': '已拒绝'
  }
  return statusMap[status] || '-'
}

const getStatusType = (status) => {
  const typeMap = {
    'pending': 'warning',
    'approved': 'success',
    'rejected': 'danger'
  }
  return typeMap[status] || 'info'
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getApplicationList(queryForm)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载申请列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryForm.pageNum = 1
  loadData()
}

const handleReset = () => {
  queryForm.supplierName = ''
  queryForm.status = null
  queryForm.pageNum = 1
  loadData()
}

const handleView = async (row) => {
  try {
    const res = await getApplicationDetail(row.id)
    currentApplication.value = res.data
    detailVisible.value = true
  } catch (error) {
    console.error('加载详情失败:', error)
    ElMessage.error('加载详情失败')
  }
}

const handleApprove = (row) => {
  approvalType.value = 'approve'
  approvalTitle.value = '批准申请'
  approvalForm.comment = ''
  currentApplication.value = row
  detailVisible.value = false
  approvalDialogVisible.value = true
}

const handleReject = (row) => {
  approvalType.value = 'reject'
  approvalTitle.value = '拒绝申请'
  approvalForm.comment = ''
  currentApplication.value = row
  detailVisible.value = false
  approvalDialogVisible.value = true
}

const submitApproval = async () => {
  if (approvalType.value === 'reject' && !approvalForm.comment.trim()) {
    ElMessage.warning('请输入拒绝原因')
    return
  }

  submitLoading.value = true
  try {
    if (approvalType.value === 'approve') {
      await approveApplication(currentApplication.value.id, { comment: approvalForm.comment })
      ElMessage.success('已批准该供应商注册申请！系统已自动创建登录账号。')
    } else {
      await rejectApplication(currentApplication.value.id, { comment: approvalForm.comment })
      ElMessage.success('已拒绝该供应商注册申请')
    }
    approvalDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('审批失败:', error)
    ElMessage.error(error.message || '审批操作失败')
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.application-approval {
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
