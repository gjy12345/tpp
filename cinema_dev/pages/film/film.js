import { getFilmInfo } from '../../api/film'
import { getFilmRecentComment } from '../../api/comment'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        resourceUrl: getApp().globalData.resourceUrl,
        showFull: false,
        uid: null,
        film: null,
        stars: [],
        showBuy: false,
        comments: []
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        const uid = options.uid
        // const uid = '7a8431ee-7ed0-4f35-8ef1-3aabb85eadf3'
        this.data.uid = uid
        if(options.showBuy){
            this.setData({
                showBuy: true
            })
        }
        console.log(uid)
        this.loadData()
        this.loadComment()
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {
        const query = wx.createSelectorQuery()
        query.select('#buy').boundingClientRect()
        query.select('#blank').boundingClientRect()
        query.exec(res => {
            const height = this.data.showBuy?res[0].height+res[1].height:0
            wx.getSystemInfo({
                success:res=>{
                    this.setData({
                        contentHeight: res.windowHeight - height
                    })
                }
            })
        })
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
    //点击查看详细的介绍
    clickShowFullDes(){
        this.setData({
            showFull: !this.data.showFull
        })
    },
    loadData(){
        wx.showLoading({
          title: '加载电影信息中...',
        })
        getFilmInfo(this.data.uid).then(res=>{
            const data = res.data
            wx.setNavigationBarTitle({
                title: data.name
            })
            this.setData({
                film: data,
                stars: this.cutStar(data.star)
            })
        }).finally(_=>{
            wx.hideLoading()
        })
    },
    cutStar(star){
        let arr = []
        if(star){
            arr = star.split(',')
        }
        return arr
    },
    go_buy(){
        wx.navigateTo({
          url: '/pages/chose_cinema/chose_cinema?uid='+this.data.film.uid,
        })
    },
    loadComment(){
        getFilmRecentComment(this.data.uid).then(res=>{
            console.log(res)
            this.setData({
                comments: res.data
            })
        })
    }
})