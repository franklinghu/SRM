import request from '@/utils/request'

export function getTemplateList(params) {
  return request({
    url: '/evaluation/templates',
    method: 'get',
    params
  })
}

export function getTemplateDetail(id) {
  return request({
    url: `/evaluation/templates/${id}`,
    method: 'get'
  })
}

export function createTemplate(data) {
  return request({
    url: '/evaluation/templates',
    method: 'post',
    data
  })
}

export function updateTemplate(id, data) {
  return request({
    url: `/evaluation/templates/${id}`,
    method: 'put',
    data
  })
}

export function deleteTemplate(id) {
  return request({
    url: `/evaluation/templates/${id}`,
    method: 'delete'
  })
}

export function getEvaluationList(params) {
  return request({
    url: '/evaluation/evaluations',
    method: 'get',
    params
  })
}

export function getEvaluationDetail(id) {
  return request({
    url: `/evaluation/evaluations/${id}`,
    method: 'get'
  })
}

export function createEvaluation(data) {
  return request({
    url: '/evaluation/evaluations',
    method: 'post',
    data
  })
}

export function updateEvaluation(id, data) {
  return request({
    url: `/evaluation/evaluations/${id}`,
    method: 'put',
    data
  })
}

export function deleteEvaluation(id) {
  return request({
    url: `/evaluation/evaluations/${id}`,
    method: 'delete'
  })
}
