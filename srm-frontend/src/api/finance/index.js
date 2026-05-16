import request from '@/utils/request'

export function getInvoiceList(params) {
  return request({
    url: '/finance/invoices',
    method: 'get',
    params
  })
}

export function getInvoiceDetail(id) {
  return request({
    url: `/finance/invoices/${id}`,
    method: 'get'
  })
}

export function createInvoice(data) {
  return request({
    url: '/finance/invoices',
    method: 'post',
    data
  })
}

export function updateInvoice(id, data) {
  return request({
    url: `/finance/invoices/${id}`,
    method: 'put',
    data
  })
}

export function deleteInvoice(id) {
  return request({
    url: `/finance/invoices/${id}`,
    method: 'delete'
  })
}

export function getPaymentList(params) {
  return request({
    url: '/finance/payments',
    method: 'get',
    params
  })
}

export function getPaymentDetail(id) {
  return request({
    url: `/finance/payments/${id}`,
    method: 'get'
  })
}

export function createPayment(data) {
  return request({
    url: '/finance/payments',
    method: 'post',
    data
  })
}

export function updatePayment(id, data) {
  return request({
    url: `/finance/payments/${id}`,
    method: 'put',
    data
  })
}

export function deletePayment(id) {
  return request({
    url: `/finance/payments/${id}`,
    method: 'delete'
  })
}
