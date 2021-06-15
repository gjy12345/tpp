// pages/use_order/use_order.js
import {
    getOrderInfo
} from '../../api/order'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        serverUrl: getApp().globalData.BASE_SERVER_URL,
        orderNum: null,
        cinemaDto: null,
        filmInfoDto: null,
        order: null,
        orderItems: [],
        resourceUrl: getApp().globalData.resourceUrl,
        hallName: null,
        begin: null,
        end: null,
        msg: '',
        expire: null,
        showItemsDilaog: false,
        isShowing: true
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setData({
            orderNum: options.orderNum
        })
        this.loadData()
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {},

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {
        this.data.isShowing = false
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
    loadData() {
        wx.showLoading({
            title: '加载中',
            mask: true
        })
        getOrderInfo(this.data.orderNum).then(res => {
            console.log(res)
            const data = res.data
            if (data.order.status === 1 || data.order.status === 2) {
                this.setMsg(data.begin, data.end)
            } else {
                this.setNotice(data.order.status, data.order.createTime)
            }
            if (data.end) {
                data.end = data.end.split(" ")[1]
            }
            this.setData({
                order: data.order,
                orderItems: data.items,
                filmInfoDto: data.filmInfoDto,
                cinemaDto: data.cinemaDto,
                hallName: data.hallName,
                begin: data.begin,
                end: data.end
            })
        }).finally(_ => {
            wx.hideLoading()
        })
    },
    setMsg(begin, end) {
        const now = new Date().getTime()
        const b = new Date(begin.replace(/-/g, '/')).getTime();
        const e = new Date(end.replace(/-/g, '/')).getTime();
        console.log(new Date())
        console.log(new Date(begin.replace(/-/g, '/')))
        console.log(new Date(end.replace(/-/g, '/')))
        let msg, expire
        if (now >= b && now < e) {
            msg = '电影正在放映中'
            expire = false
        } else if (now >= e) {
            msg = '电影已放映'
            expire = true
        } else {
            msg = '请在 ' + begin + ' 前往观看'
            expire = false
        }
        this.setData({
            msg: msg,
            expire: expire
        })
    },
    setNotice(status, createTime) {
        let msg
        if (status === 0) {
            const begin = new Date(createTime.replace(/-/g, '/')).getTime();
            const now = new Date().getTime()
            const ex = (begin + 15 * 60 * 1000 - now)/1000
            let m = Math.floor(ex / 60)
            let s = Math.floor(ex % 60)
            if (m < 10)
                m = '0' + m
            if (s < 10)
                s = '0' + s
            if(ex<=0){
                this.setNotice(-2)
                //隐藏界面需付金额
                this.data.order.status = -2
                this.setData({
                    order:this.data.order
                })
                return
            }
            msg = '等待支付 , 剩余时间:'+m+'分'+s+'秒'
            setTimeout(() => {
                if(this.data.isShowing)
                    this.setNotice(status,createTime)
            }, 1000);
        } else if (status === -2) {
            msg = '订单已超时自动取消'
        } else if (status === -1) {
            msg = '已退款'
        } else {
            msg = ''
        }
        this.setData({
            msg: msg
        })
    },
    showItems() {
        this.setData({
            showItemsDilaog: !this.data.showItemsDilaog
        })
    }
})