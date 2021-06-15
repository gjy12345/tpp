import request from '../utils/request'

export function getOrderCommentList(param) {
  return request({
    url: '/order/comment/list',
    method: 'get',
    params: param
  })
}

export function deleteComment(id) {
  return request({
    url: '/order/comment/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}
