import request from '../utils/request-util'

export function userLogin(phone, password) {
    return request({
        method: 'post',
        url: '/user/login',
        data: {
            phone: phone,
            password: password
        },
        isJson: true
    })
}

export function loginWithToken(token) {
    return request({
        method: 'get',
        url: '/user/checkToken',
        data: {
            token: token
        },
        isJson: false
    })
}

export function updateUserInfo(data) {
    return request({
        method: 'post',
        url: '/user/updateInfo',
        data: data,
        isJson: true
    })
}

export function register(data) {
    return request({
        method: 'post',
        url: '/user/register',
        data: data,
        isJson: true
    })
}