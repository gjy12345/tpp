import request from '../utils/request-util'

export function createOrder(data) {
    return request({
        url: '/order/createOrder',
        isJson: true,
        method: 'post',
        data:data
    })
}

export function pay(orderNum) {
    return request({
        url: '/order/pay',
        method: 'post',
        data:{
            orderNum: orderNum
        }
    })
}

export function getOrderList(params) {
    return request({
        url: '/order/list',
        method: 'get',
        data: params
    })
}

export function getOrderInfo(orderNum) {
    return request({
        url: '/order/getOrderInfo',
        method: 'get',
        data: {
            orderNum: orderNum
        }
    })
}