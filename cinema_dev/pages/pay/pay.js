// pages/pay/pay.js
import {
    createOrder,pay
} from '../../api/order'
import {
    getSchduleInfo
} from '../../api/schedule'
import {
    bindAndSet
} from '../../utils/util'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        scheduleUid: null,
        choseSites: [],
        schduleInfo: null,
        dialogVisible: false,
        tempInput: '',
        resourceUrl: getApp().globalData.resourceUrl,
        payed: false,
        msg: ''
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        let price = 0
        const tickets = JSON.parse(options.ticket)
        for (const ticket of tickets) {
            price += Number(ticket.price)
        }
        this.setData({
            choseSites: tickets,
            scheduleUid: options.scheduleUid,
            price: price,
            phone: getApp().globalData.userInfo.phone
        })
        this.loadSchduleInfos(options.scheduleUid)
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
    createPayOrder(e) {
        const type = e.currentTarget.dataset.value
        wx.showLoading({
            title: '创建订单中..',
        })
        const data = {
            scheduleUid: this.data.scheduleUid,
            sites: this.data.choseSites,
            phone: this.data.phone
        }
        //生成订单，锁座位
        createOrder(data).then(res => {
            console.log(res)
            if(type===0||type==='0'){
                //不支付
                this.setData({
                    payed: true,
                    msg: '请在15分钟内支付订单'
                })
            }else{
                this.payOrder(res.data)
            }
        }).finally(_ => {
            if(type===0||type==='0')
                wx.hideLoading()
        })
    },
    payOrder(orderNum){
        pay(orderNum).then(res=>{
            //跳去订单详情
            console.log(res)
            this.setData({
                payed: true,
                msg: '购买成功'
            })
            wx.showToast({
              title: '购买成功!',
              complete:()=>{
                  setTimeout(() => {
                      wx.redirectTo({
                        url: '/pages/use_order/use_order?orderNum='+orderNum,
                      })
                  }, 1000);
              }
            })
        }).finally(_=>{
            wx.hideLoading()
        })
    },
    loadSchduleInfos(uid) {
        getSchduleInfo(uid).then(res => {
            console.log(res)
            this.setData({
                schduleInfo: res.data
            })
        })
    },
    editPhone(){
        this.setData({
            dialogVisible: !this.data.dialogVisible
        })
    },
    onInputPhoneChange(e){
        bindAndSet(e, this)
    },
    changePhone(){
        if(!this.data.tempInput||this.data.tempInput.length===0||this.data.tempInput===''){
            wx.showToast({
                title: '手机号码不能为空!',
                icon: 'none'
            })
            return
        }
        this.setData({
            phone: this.data.tempInput
        })
        this.editPhone()
    }
})