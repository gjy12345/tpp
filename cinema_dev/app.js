// app.js
import {
  loginWithToken
} from '/api/user'
App({
  onLaunch() {
    const token = wx.getStorageSync('token')
    this.globalData.position = wx.getStorageSync('position')
    this.globalData.showPosition = wx.getStorageSync('showPosition')
    this.globalData.positionObj = wx.getStorageSync('positionObj')
    if (this.globalData.showPosition === '' || this.globalData.positionObj === null) {
      //默认地区
      this.globalData.showPosition = '临安区'
      this.globalData.positionObj = {
        code: "330112",
        id: 918,
        level: 2,
        name: "临安区",
        pid: 908,
      }
      this.globalData.position = '浙江省杭州市临安区'
      wx.setStorageSync('position', this.globalData.position)
      wx.setStorageSync('showPosition', this.globalData.showPosition)
      wx.setStorageSync('positionObj', this.globalData.positionObj)
    }
    if (token) {
      wx.showLoading({
        title: '登录中...',
        mask: 'true'
      })
      loginWithToken(token).then(res => {
        this.globalData.userInfo = res.data
        this.globalData.token = res.token
      }).catch(_ => {
        wx.removeStorageSync('token')
      }).finally(_ => {
        wx.hideLoading()
        this.globalData.loadingUserInfo = false
      })
    } else {
      this.globalData.loadingUserInfo = false
    }
  },
  globalData: {
    BASE_SERVER_URL: 'http://127.0.0.1:8080',
    userInfo: null,
    resourceUrl: 'http://127.0.0.1:8080/res/file/',
    loadingUserInfo: true,
    position: null, //所在区域
    showPosition: null,
    positionObj: null
  }
})