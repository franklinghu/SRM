import request from '@/utils/request'

export function getRequestList(params) {
  return request({
    url: '/purchase/requests',
    method: 'get',
    params
  })
}

export function getRequestDetail(id) {
  return request({
    url: `/purchase/requests/${id}`,
    method: 'get'
  })
}

export function createRequest(data) {
  return request({
    url: '/purchase/requests',
    method: 'post',
    data
  })
}

export function updateRequest(id, data) {
  return request({
    url: `/purchase/requests/${id}`,
    method: 'put',
    data
  })
}

export function deleteRequest(id) {
  return request({
    url: `/purchase/requests/${id}`,
    method: 'delete'
  })
}
