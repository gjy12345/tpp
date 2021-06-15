import request from '../utils/request-util'

export function getProvinces() {
    return request({
      url: '/common/position/provinces',
      method: 'get'
    })
  }
  
  export function getCities() {
    return request({
      url: '/common/position/cities',
      method: 'get'
    })
  }
  
  export function getCountries() {
    return request({
      url: '/common/position/countries',
      method: 'get'
    })
  }
  