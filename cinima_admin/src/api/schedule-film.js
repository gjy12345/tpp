import request from '@/utils/request'

export function getScheduleFilmList(query) {
  return request({
    url: '/schedule/film/list',
    method: 'get',
    params: query
  })
}

export function loadSite(id) {
  return request({
    url: '/schedule/film/site/get',
    method: 'get',
    params: {
      id: id
    }
  })
}

export function deleteScheduleFilm(id) {
  return request({
    url: '/schedule/film/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

export function uploadScheduleData(data) {
  return request({
    url: '/schedule/film/create',
    method: 'post',
    data: data
  })
}
