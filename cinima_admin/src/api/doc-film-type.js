import request from '@/utils/request'

export function getFilmTypeList(params) {
  return request({
    url: '/doc/film-type/list',
    params: params
  })
}

export function createFilmType(data) {
  return request({
    url: '/doc/film-type/create',
    method: 'post',
    data: data
  })
}

export function updateFilmType(data) {
  return request({
    url: '/doc/film-type/update',
    method: 'post',
    data: data
  })
}

export function deleteFilmType(id) {
  return request({
    url: '/doc/film-type/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

export function getAllSimpleTypes() {
  return request({
    url: '/doc/film-type/all',
    method: 'get'
  })
}
