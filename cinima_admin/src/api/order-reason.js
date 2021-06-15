import request from '@/utils/request'

// 获取退款理由列表
export function getCancelReasonList(data) {
  return request(
    {
      url: '/order/reason/list',
      method: 'get',
      params: data
    }
  )
}

export function updateReason(data) {
  return request({
    url: '/order/reason/update',
    method: 'post',
    data: data
  })
}

export function deleteReason(id) {
  return request({
    url: '/order/reason/delete',
    method: 'post',
    params: { id: id }
  })
}

export function createReason(data) {
  return request({
    url: '/order/reason/create',
    method: 'post',
    data: data
  })
}
