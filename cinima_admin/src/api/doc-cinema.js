import request from '@/utils/request'

export function getCinemaList(query) {
  return request({
    url: '/doc/cinema/list',
    params: query
  })
}

export function deleteCinema(id) {
  return request({
    url: '/doc/cinema/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

export function createCinema(data) {
  return request({
    url: '/doc/cinema/create',
    method: 'post',
    data: data
  })
}

export function updateCinema(data) {
  return request({
    url: '/doc/cinema/update',
    method: 'post',
    data: data
  })
}
