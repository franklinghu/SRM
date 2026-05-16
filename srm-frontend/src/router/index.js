import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/modules/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/system/login.vue')
  },
  {
    path: '/',
    component: () => import('@/views/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue')
      },
      {
        path: 'supplier',
        name: 'Supplier',
        component: () => import('@/views/supplier/index.vue')
      },
      {
        path: 'supplier/:id',
        name: 'SupplierDetail',
        component: () => import('@/views/supplier/detail.vue')
      },
      {
        path: 'purchase/request',
        name: 'PurchaseRequest',
        component: () => import('@/views/purchase/request/index.vue')
      },
      {
        path: 'purchase/order',
        name: 'PurchaseOrder',
        component: () => import('@/views/purchase/order/index.vue')
      },
      {
        path: 'contract',
        name: 'Contract',
        component: () => import('@/views/contract/index.vue')
      },
      {
        path: 'evaluation/template',
        name: 'EvaluationTemplate',
        component: () => import('@/views/evaluation/template.vue')
      },
      {
        path: 'evaluation/evaluation',
        name: 'SupplierEvaluation',
        component: () => import('@/views/evaluation/evaluation.vue')
      },
      {
        path: 'finance/invoice',
        name: 'Invoice',
        component: () => import('@/views/finance/invoice.vue')
      },
      {
        path: 'finance/payment',
        name: 'Payment',
        component: () => import('@/views/finance/payment.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.path !== '/login' && !userStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
