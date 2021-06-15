import request from '../utils/request-util'

export function uploadComment(data) {
    return request({
        method: 'post',
        url: '/comment/create',
        data: data,
        isJson: true
    })
}

export function getFilmRecentComment(filmUid) {
    return request({
        method: 'get',
        url: '/comment/filmRecentComments',
        data: {
            filmUid: filmUid
        },
        isJson: true
    })
}