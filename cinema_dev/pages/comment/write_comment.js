// pages/comment/write_comment.js
import { uploadComment } from '../../api/comment'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        hx: '/images/icon/hx.png',
        ax: '/images/icon/ax.png',
        scoreChoses:['/images/icon/hx.png','/images/icon/hx.png','/images/icon/hx.png','/images/icon/hx.png','/images/icon/hx.png'],
        score: 0,
        content: '',
        orderNum: null,
        hasSubmit: false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.data.orderNum = options.orderNum
        wx.setNavigationBarTitle({
          title: '写评论 * '+options.filmName,
        })
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
    //点击评分
    clickIcon(e){
        const v = e.currentTarget.dataset.value
        const arr = this.data.scoreChoses
        for(let i=0;i<arr.length;i++){
            arr[i]= (i<=v)?this.data.ax:this.data.hx
        }
        this.setData({
            scoreChoses: arr,
            score: Number(v) + 1
        })
    },
    inputComment(e){
        this.data.content = e.detail.value
    },
    submitContent(){
        const content = this.data.content.replace(/(^\s*)|(\s*$)/g,"")
        const score = this.data.score
        if(content===''){
            wx.showToast({
              title: '请输入评论',
              icon: 'error'
            })
            return
        }
        if(score===0){
            wx.showToast({
                title: '请选择评分',
                icon: 'error'
              })
              return
        }
        wx.showLoading({
          title: '提交中...',
        })
        uploadComment({
            score: score,
            content: content,
            orderNum: this.data.orderNum
        }).then(res=>{
            wx.showToast({
              title: '提交成功!'
            })
            this.setData({
                hasSubmit: true
            })
        }).finally(_=>{
            wx.hideLoading({
              success: (res) => {},
            })
        })
    }
})