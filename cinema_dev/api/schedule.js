import request from '.././utils/request-util'

export function getScheduleSites(uid) {
    return request({
        url: '/schedule/sites',
        method: 'get',
        data: {
            uid: uid
        }
    })
}

export function getSchduleInfo(uid) {
    return request({
        url: '/schedule/info',
        method: 'get',
        data: {
            uid: uid
        }
    })
}