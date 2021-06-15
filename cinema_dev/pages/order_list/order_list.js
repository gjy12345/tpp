// pages/order_list/order_list.js
import { getOrderList,pay } from '../../api/order'
import { formatTime } from '../../utils/util'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        orderList: [],
        offset: 0,
        pageSize: 10,
        msg: ''
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
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
        wx.stopPullDownRefresh()
        this.data.offset = 0
        this.setData({
            orderList: []
        })
        this.loadData()
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {
        this.loadData()
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },
    loadData(){
        wx.showLoading({
          title: '加载数据中..',
        })
        getOrderList({
            offset: this.data.offset,
            pageSize: this.data.pageSize
        }).then(res=>{
            const data = res.data
            if(data.data.length===0&&this.data.orderList.length>0){
                wx.showToast({
                  title: '没有更多数据了',
                  icon: 'none'
                })
                return
            }else if(this.data.orderList.length===0&&data.data.length===0){
                this.setData({
                    msg:'暂无订单，快去挑选喜欢看的电影吧！'
                })
                return
            }
            this.data.offset =  data.offset
            this.preHandleData(data.data)
            this.setData({
                orderList: this.data.orderList.concat(data.data)
            })
            console.log(this.data)
        }).finally(_=>{
            wx.hideLoading()
        })
    },
    preHandleData(data) {
        const now = new Date()
        for(const item of data){
            let end = item.end
            end = new Date(end.replace(/-/g, '/'));
            item.expire = (now.getTime()>end.getTime())
        }
    },
    goPay(e){
        const item = e.currentTarget.dataset.value
        const index = Number(e.currentTarget.dataset.index)
        wx.showLoading({
          title: '支付中...',
        })
        pay(item.orderNum).then(res=>{
            item.status = 1
            item.payTime = formatTime(new Date())
            this.data.orderList[index] = item
            this.setData({
                orderList: this.data.orderList
            })
        }).finally(_=>{
            wx.hideLoading()
        })
    },
    goUse(e){
        const item = e.currentTarget.dataset.value
        wx.navigateTo({
          url: '/pages/use_order/use_order?orderNum='+item.orderNum,
        })
    },
    goComment(e){
        const item = e.currentTarget.dataset.value
        wx.navigateTo({
          url: '/pages/comment/write_comment?orderNum='+item.orderNum+'&filmName='+item.filmName,
        })
    }
})