// pages/register/register.js
import {
    bindAndSet
} from '../../utils/util'
import {
    register
} from '../../api/user'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        rePassword: null,
        password: null,
        phone: null
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
    jumpLogin() {
        wx.redirectTo({
            url: '/pages/login/login',
        })
    },
    quickRegister() {
        const phone = this.data.phone
        const pwd = this.data.password
        if (!phone) {
            wx.showToast({
                title: '请输入手机号!',
                icon: 'none'
            })
            return
        }
        if (!pwd || !this.data.rePassword) {
            wx.showToast({
                title: '请输入密码!',
                icon: 'none'
            })
            return
        }
        if (pwd != this.data.rePassword) {
            wx.showToast({
                title: '前后密码不一致!',
                icon: 'none'
            })
            return
        }
        wx.showLoading({
            title: '注册中',
            mask: true
        })
        register({
            phone: phone,
            password: pwd
        }).then(res => {
            wx.showToast({
                title: '注册成功!',
                complete: () => {
                    setTimeout(()=>{
                        wx.redirectTo({
                            url: '/pages/login/login',
                        })
                    },1000)
                }
            })
        }).finally(_ => {
            wx.hideLoading()
        })
    },
    noRegister() {
        wx.navigateBack()
    },
    onInputChange(e) {
        bindAndSet(e, this)
    }
})