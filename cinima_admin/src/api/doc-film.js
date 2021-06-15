import request from '@/utils/request'

export function getFilmList(query) {
  return request({
    url: '/doc/film/list',
    method: 'get',
    params: query
  })
}

export function createFilm(data) {
  return request({
    url: '/doc/film/create',
    method: 'post',
    data: data
  })
}

export function updateFilm(data) {
  return request({
    url: '/doc/film/update',
    method: 'post',
    data: data
  })
}

export function deleteFilm(id) {
  return request({
    url: '/doc/film/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}
