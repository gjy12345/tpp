import request from '@/utils/request'

export function getCustomerList(query) {
  return request({
    url: '/customer/list',
    method: 'get',
    params: query
  })
}

export function resetPassword(id, pwd) {
  return request({
    url: '/customer/resetPwd',
    method: 'post',
    params: {
      id: id,
      password: pwd
    }
  })
}

export function updateCustomer(data) {
  return request({
    url: '/customer/update',
    method: 'post',
    data: data
  })
}

export function deleteCustomer(id) {
  return request({
    url: '/customer/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}
