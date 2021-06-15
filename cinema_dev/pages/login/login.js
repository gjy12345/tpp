// pages/login/login.js
import { bindAndSet } from '../../utils/util'
import { userLogin } from '../../api/user'
Page({

    /**
     * 页面的初始数据
     */
    data: {
      phone: '15957544980',
      password: '956115488'
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
      getApp().globalData.userInfo = null
    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },
    quickLogin(e){
      if(this.data.phone===null||this.data.phone===''){
        wx.showToast({
          title: '请输入手机号！',//提示文字
          duration:1000,//显示时长
          mask:true,//是否显示透明蒙层，防止触摸穿透，默认：false  
          icon:'error', //图标，支持"success"、"loading"  
        })
        return
      }
      if(this.data.password===null||this.data.password===''){
        wx.showToast({
          title: '请输入密码！',//提示文字
          duration:1000,//显示时长
          mask:true,//是否显示透明蒙层，防止触摸穿透，默认：false  
          icon:'error', //图标，支持"success"、"loading"  
        })
        return
      }
      userLogin(this.data.phone,this.data.password).then(res=>{
          const data = res.data
          console.log(res)
          //登录成功
          getApp().globalData.userInfo = data
          getApp().globalData.token = data.token
          wx.setStorageSync('token', data.token)
          wx.setStorageSync('info', data)
          const pl = getCurrentPages().length
          if(pl>2)
            wx.navigateBack()
          else
            wx.switchTab({
              url: '/pages/home/home',
            })
      })
    },
    noLogin(){
        wx.switchTab({
          url: '/pages/me/me',
        })
    },
    onInputChange(e){
      bindAndSet(e,this)
    },
    jumpReister(){
      wx.redirectTo({
        url: '/pages/register/register',
      })
  }
})