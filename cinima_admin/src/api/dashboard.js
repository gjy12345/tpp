import request from '@/utils/request'

export function getDashData() {
  return request({
    url: '/dashboard/data',
    method: 'get'
  })
}
