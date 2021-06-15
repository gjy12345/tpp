import request from '../utils/request-util'

export function getCinemaList(listQuery){
    return request({
        url: '/cinema/list',
        method: 'get',
        data:listQuery,
        isJson:false
    })
}

export function getHasScheduleCinemaList(listQuery){
    return request({
        url: '/cinema/hasScheduleCinemaList',
        method: 'get',
        data:listQuery,
        isJson:false
    })
}

export function getCinemaScheduleFilms(params) {
    return request({
        url: '/cinema/scheduleFilms',
        method: 'get',
        data:params,
        isJson:false
    })
}

export function getCinemaInfo(uid) {
    return request({
        url: '/cinema/info',
        method: 'get',
        data:{
            uid: uid
        },
        isJson:false
    })
}