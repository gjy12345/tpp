import request from '../utils/request-util'

export function getHotFilmList(offset,pageSize){
    return request({
        url: '/home/hots',
        method: 'get',
        data:{
            offset: offset,
            pageSize: pageSize
        },
        isJson:false
    })
}


export function getWillFilmList(offset,pageSize){
    return request({
        url: '/home/willShow',
        method: 'get',
        data:{
            offset: offset,
            pageSize: pageSize
        },
        isJson:false
    })
}