import request from '@/utils/request'

export function getHallList(query) {
  return request({
    url: '/doc/hall/list',
    method: 'get',
    params: query
  })
}

export function createHall(data) {
  return request({
    url: '/doc/hall/create',
    method: 'post',
    data: data
  })
}

export function updateHall(data) {
  return request({
    url: '/doc/hall/update',
    method: 'post',
    data: data
  })
}

export function deleteHall(id) {
  return request({
    url: '/doc/hall/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

export function uploadSite(data) {
  return request({
    url: '/doc/hall/site/update',
    method: 'post',
    data: data
  })
}

export function loadSite(hallId) {
  return request({
    url: '/doc/hall/site/get',
    method: 'get',
    params: {
      hallId: hallId
    }
  })
}

export function getAllSimpleHall(cinemaId) {
  return request({
    url: '/doc/hall/simpleAll',
    method: 'get',
    params: {
      cinemaId: cinemaId
    }
  })
}
