// pages/me/me.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        avatar: '/images/icon/head.jpg',
        customer: null
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.loadInfo()
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
        this.reloadUserInfo()
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
    login: function () {
        wx.navigateTo({
            url: '../login/login'
        })
    },
    reloadUserInfo(){
        const customer = getApp().globalData.userInfo
        let avatar = '/images/icon/head.jpg'
        if (customer && customer.avatar) {
            avatar = getApp().globalData.resourceUrl + customer.avatar
        }
        this.setData({
            customer: getApp().globalData.userInfo,
            avatar: avatar
        })
    },
    loadInfo(){
        if(getApp().globalData.loadingUserInfo){
            setTimeout(() => {
                this.loadInfo()
            }, 100);
        }else{
            this.reloadUserInfo()
        }
    },
    showInfo(){
        wx.navigateTo({
            url:'../user_info/user_info'
        })
    },
    toOrderList(e){
        if(!getApp().globalData.userInfo){
            this.login()
            return
        }
        wx.navigateTo({
          url: '/pages/order_list/order_list'
        })
    }
})