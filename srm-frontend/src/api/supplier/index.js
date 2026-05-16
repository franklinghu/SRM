import request from '@/utils/request'

export function getSupplierList(params) {
  return request({
    url: '/suppliers',
    method: 'get',
    params
  })
}

export function getSupplierDetail(id) {
  return request({
    url: `/suppliers/${id}`,
    method: 'get'
  })
}

export function createSupplier(data) {
  return request({
    url: '/suppliers',
    method: 'post',
    data
  })
}

export function updateSupplier(id, data) {
  return request({
    url: `/suppliers/${id}`,
    method: 'put',
    data
  })
}

export function deleteSupplier(id) {
  return request({
    url: `/suppliers/${id}`,
    method: 'delete'
  })
}

export function getSupplierContacts(id) {
  return request({
    url: `/suppliers/${id}/contacts`,
    method: 'get'
  })
}

export function createSupplierContact(id, data) {
  return request({
    url: `/suppliers/${id}/contacts`,
    method: 'post',
    data
  })
}

export function updateSupplierContact(id, data) {
  return request({
    url: `/suppliers/contacts/${id}`,
    method: 'put',
    data
  })
}

export function deleteSupplierContact(id) {
  return request({
    url: `/suppliers/contacts/${id}`,
    method: 'delete'
  })
}

export function getSupplierCertificates(id) {
  return request({
    url: `/suppliers/${id}/certificates`,
    method: 'get'
  })
}

export function createSupplierCertificate(id, data) {
  return request({
    url: `/suppliers/${id}/certificates`,
    method: 'post',
    data
  })
}

export function updateSupplierCertificate(id, data) {
  return request({
    url: `/suppliers/certificates/${id}`,
    method: 'put',
    data
  })
}

export function deleteSupplierCertificate(id) {
  return request({
    url: `/suppliers/certificates/${id}`,
    method: 'delete'
  })
}

export function getExpiringCertificates(params) {
  return request({
    url: '/suppliers/certificates/expiring',
    method: 'get',
    params
  })
}
