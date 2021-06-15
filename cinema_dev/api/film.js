import request from '../utils/request-util'

export function getFilmInfo(uid) {
    return request({
        url: '/film/info',
        method: 'get',
        data: {
            uid: uid
        },
        isJson: false
    })
}