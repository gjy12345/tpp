import request from '@/utils/request'

export function getOrderList(query) {
  return request({
    url: '/order/list',
    params: query,
    method: 'get'
  })
}

export function returnMoney(id) {
  return request({
    url: '/order/returnMoney',
    params: {
      id: id
    },
    method: 'post'
  })
}
