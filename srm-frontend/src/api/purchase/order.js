import request from '@/utils/request'

export function getOrderList(params) {
  return request({
    url: '/purchase/orders',
    method: 'get',
    params
  })
}

export function getOrderDetail(id) {
  return request({
    url: `/purchase/orders/${id}`,
    method: 'get'
  })
}

export function createOrder(data) {
  return request({
    url: '/purchase/orders',
    method: 'post',
    data
  })
}

export function updateOrder(id, data) {
  return request({
    url: `/purchase/orders/${id}`,
    method: 'put',
    data
  })
}

export function deleteOrder(id) {
  return request({
    url: `/purchase/orders/${id}`,
    method: 'delete'
  })
}
