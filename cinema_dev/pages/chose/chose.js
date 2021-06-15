// pages/chose/chose.js
import {
    getScheduleSites
} from '../../api/schedule'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        height: 200,
        mvWidth: 200,
        mvHeight: 200,
        contentHeight: 200,
        contentWidth: 200,
        iconW: 20,
        iconH: 20,
        siteInfo: [],
        siteImages: [],
        normal: '/images/img/wx.png',
        saleNormal: '/images/img/ys.png',
        loverL: '/images/img/qlz_01.png',
        loverR: '/images/img/qlz_02.png',
        saleLoverL: '/images/img/yx_qlz_01.png',
        saleLoverR: '/images/img/yx_qlz_02.png',
        gd: '/images/img/gd.png',
        bad: '/images/img/gz.png',
        disNormal: '/images/img/normal_jz.png',
        disLoverL: '/images/img/qlz_jz_01.png',
        disLoverR: '/images/img/qlz_jz_02.png',
        choseSites: [],
        normalChose: '/images/img/yx.png',
        loverLChose:'/images/img/chose_qlz_01.png',
        loverRChose:'/images/img/chose_qlz_02.png',
        maxCanBuy: 5
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.data.scheduleUid = options.uid
        this.setData({
            day: options.day,
            begin: options.begin,
            hall: options.hall
        })
        this.loadSites()
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
        this.calcHeight()
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
    calcHeight() {
        const query = wx.createSelectorQuery()
        query.select('#top').boundingClientRect()
        query.select('#s').boundingClientRect()
        query.select('#btn').boundingClientRect()
        query.select('#icon_tips').boundingClientRect()
        query.select('#screen').boundingClientRect()
        query.exec(res => {
            const height = res[0].height + res[1].height + res[2].height + res[3].height + res[4].height
            this.setData({
                height: wx.getSystemInfoSync().windowHeight - height,
                contentHeight: height,
                contentWidth: wx.getSystemInfoSync().windowWidth,
                mvHeight: height,
                mvWidth: wx.getSystemInfoSync().windowWidth
            })
        })
    },
    onScale(e) {
        console.log(e)
        const v = e.detail.scale
        this.setData({
            mvHeight: this.data.contentHeight * v,
            mvWidth: this.data.contentWidth * v
        })
    },
    calcIcon(n) {
        let w = Math.floor((wx.getSystemInfoSync().windowWidth - 20) / n)
        if (w > 40)
            w = 40
        this.setData({
            iconH: w,
            iconW: w
        })
    },
    loadSites() {
        wx.showLoading({
          title: '加载座位中',
        })
        getScheduleSites(this.data.scheduleUid).then(res => {
            const data = res.data
            const iconArr = []
            let count = 0
            for (const item of data) {
                if (item.length > count)
                    count = item.length
                const row = []
                for (const site of item) {
                    site.chose = false
                    row.push(this.switchSiteIcon(site))
                }
                iconArr.push(row)
            }
            this.data.siteInfo = data
            this.calcIcon(count)
            this.setData({
                siteImages: iconArr
            })
        }).finally(_=>{
            setTimeout(()=>{
                wx.hideLoading()
            },500)
        })
    },
    switchSiteIcon(item) {
        let src
        switch (item.type) {
            case -1:
                src = this.data.bad
                break
            case 0:
                src = '' //过道不加载
                break
            case 1:
                src = item.sale ? this.data.saleNormal : this.data.normal
                src = item.enable ? src : this.data.disNormal
                src = item.chose ? this.data.normalChose:src
                break
            case 2:
                src = item.sale ? this.data.saleLoverL : this.data.loverL
                src = item.enable ? src : this.data.disLoverL
                src = item.chose ? this.data.loverLChose:src
                break
            case 3:
                src = item.sale ? this.data.saleLoverR : this.data.loverR
                src = item.enable ? src : this.data.disLoverR
                src = item.chose ? this.data.loverRChose:src
                break
        }
        return src
    },
    choseSite(e) {
        const x = Number(e.currentTarget.dataset.x)
        const y = Number(e.currentTarget.dataset.y)
        const site = this.data.siteInfo[x][y]
        if (!site.enable || site.type < 0) {
            wx.showToast({
                title: '此位置不可购买!',
                icon: 'error'
            })
            return
        }
        if (site.type == 0) //过道不需要提示
            return
        if (site.sale) {
            wx.showToast({
                title: '此位置已有人购买!',
                icon: 'error'
            })
            return
        }
        site.chose = !site.chose
        const count = this.data.choseSites.length
        site.x = x
        site.y = y
        if(site.chose&&this.data.choseSites.length+1>this.data.maxCanBuy){
            wx.showToast({
                title: '一次最多可以购买'+this.data.maxCanBuy+'张票!',
                icon: 'none'
            })
            return
        }
        if(site.type===2||site.type===3){
            const other = this.data.siteInfo[x][site.type===2?y+1:y-1]
            other.x = x
            other.y = site.type===2?y+1:y-1
            site.chose?this.data.choseSites.push(other):this.data.choseSites.splice(this.data.choseSites.indexOf(other),1)
            other.chose = site.chose
            console.log(other)
            this.data.siteImages[other.x][other.y] = this.switchSiteIcon(other)
        }
        if(site.chose){
            this.data.choseSites.push(site)
        }else{
            this.data.choseSites.splice(this.data.choseSites.indexOf(site),1)
        }
        this.data.siteImages[x][y] = this.switchSiteIcon(site)
        this.setData({
            siteImages: this.data.siteImages,
            choseSites: this.data.choseSites
        })
        if(count===0||this.data.choseSites.length===0)
        this.calcHeight()
    },
    //确认选座
    submitTickets(){
        const sites = this.data.choseSites
        if(sites.length===0){
            wx.showToast({
                title: '当前未选座',
                icon: 'error'
            })
            return
        }
        wx.navigateTo({
          url: '/pages/pay/pay?scheduleUid='+this.data.scheduleUid+"&ticket="+JSON.stringify(this.data.choseSites)
        })
    }
})