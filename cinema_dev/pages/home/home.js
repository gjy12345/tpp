import {
    getHotFilmList,
    getWillFilmList
} from '../../api/home'
import {
    getProvinces,
    getCities,
    getCountries
} from '../../api/region'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        currentItem: 0,
        indicatorDots: false,
        autoplay: true,
        interval: 5000,
        duration: 1000,
        imgUrls: [
            '/images/haibao/1.jpg',
            '/images/haibao/2.jpg',
            '/images/haibao/3.jpg',
            '/images/haibao/4.jpg'
        ],
        willHeight: 1000,
        hotHeight: 1000,
        height: 1000,
        hotOffset: 0,
        hotPageSize: 10,
        willOffset: 0,
        willPageSize: 10,
        hotList: [],
        resourceUrl: getApp().globalData.resourceUrl,
        willDate: [],
        willList: [],
        isLoadingWill: false,
        isLoadingHot: false,
        region: [
            [],
            [],
            []
        ],
        allCities: [],
        allCountries: [],
        regionFinish: 0,
        showPosition: ''
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.loadData()
        this.loadRegionData()
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
        this.data.showPosition = getApp().globalData.showPosition
        if(this.data.showPosition===null){
            this.data.showPosition = '未选择'
        }
        this.setData({
            showPosition:this.data.showPosition
        })
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
     * 监听下拉到顶部
     */
    onPullDownRefresh: function () {
        wx.stopPullDownRefresh()
        if (this.data.currentItem === 0 || this.data.currentItem === '0') {
            this.data.hotOffset = 0
            this.data.hotList = []
        } else {
            this.data.willList = []
            this.data.willDate = []
            this.data.willOffset = 0
        }
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
    changeTab(e) {
        let current
        if (e.currentTarget.dataset.value === undefined) {
            current = e.detail.current
        } else {
            current = e.currentTarget.dataset.value
        }
        this.setData({
            currentItem: current,
            height: (current === 0 || current === '0') ? this.data.hotHeight : this.data.willHeight
        })
        const dataLen = (current === 0 || current === '0') ? this.data.hotList.length : this.data.willList.length
        //切换界面懒加载
        if (dataLen === 0) {
            console.log('懒加载')
            this.loadData()
        }
    },
    loadData() {
        const type = this.data.currentItem
        if (type === 0 || type === '0' && !this.data.isLoadingHot) {
            this.data.isLoadingHot = true
            wx.showLoading({
                title: '加载中',
            })
            getHotFilmList(this.data.hotOffset, this.data.hotPageSize)
                .then(res => {
                    if (res.data.data.length === 0) {
                        wx.showToast({
                            title: '没有更多数据了!',
                            icon: 'error'
                        })
                        return
                    }
                    this.setData({
                        hotList: this.data.hotList.concat(res.data.data)
                    })
                    this.data.hotOffset = this.data.hotList.length
                    this.calcHeight()
                }).finally(_ => {
                    wx.hideLoading()
                    this.data.isLoadingHot = false
                })
        } else {
            if (this.data.isLoadingWill)
                return
            wx.showLoading({
                title: '加载中',
            })
            this.data.isLoadingWill = true
            wx.showNavigationBarLoading()
            getWillFilmList(this.data.willOffset, this.data.willPageSize)
                .then(res => {
                    if (res.data.data.length === 0) {
                        wx.showToast({
                            title: '没有更多数据了!',
                            icon: 'error'
                        })
                        return
                    }
                    //根据日期放置数据
                    this.putWillListData(res.data.data)
                    this.data.willOffset = this.data.willOffset + res.data.data.length
                    this.calcHeight()
                }).finally(_ => {
                    wx.hideLoading()
                    this.data.isLoadingWill = false
                })
        }
    },
    calcHeight() {
        const query = this.createSelectorQuery()
        if (this.data.currentItem === 0 || this.data.currentItem === '0') {
            query.select('#hot_content').boundingClientRect()
            query.select('#banner0').boundingClientRect()
            query.exec(res => {
                const height = res[0].height + res[1].height
                this.setData({
                    hotHeight: height,
                    height: height
                })
            })
        } else {
            query.select('#will_content').boundingClientRect()
            query.exec(res => {
                const height = res[0].height
                this.setData({
                    willHeight: height,
                    height: height
                })
            })
        }
    },
    putWillListData(needPut) {
        const times = this.data.willDate
        const list = this.data.willList
        for (const i in needPut) {
            const item = needPut[i]
            let showTime = item.showTime
            let index = showTime.indexOf(' ')
            if (index !== -1) {
                showTime = showTime.substring(0, index)
            }
            if (times.length === 0 || times[times.length - 1] !== showTime) {
                index = times.length
                times.push(showTime)
                list.push([])
            } else {
                index = times.length - 1
            }
            list[index].push(item)
        }
        this.setData({
            willDate: times,
            willList: list
        })
    },
    loadRegionData() {
        getProvinces().then(res => {
            this.data.region[0] = res.data
            this.setData({
                region: this.data.region
            })
            this.data.regionFinish = this.data.regionFinish+1
            this.initRegion()
        })
        getCities().then(res => {
            this.data.allCities = res.data
            this.data.regionFinish = this.data.regionFinish+1
            this.initRegion()
        })
        getCountries().then(res => {
            this.data.allCountries = res.data
            this.data.regionFinish = this.data.regionFinish+1
            this.initRegion()
        })
    },
    onRegionColumnChange(e) {
        const index = e.detail.value
        const column = e.detail.column
        const region = this.data.region
        switch (column) {
            case 0:
                region[2] = []
                region[1] = this.data.allCities.filter(value => {
                    return value.pid === region[0][index].id
                })
                if(region[1].length>0){
                    region[2] = this.data.allCountries.filter(value => {
                        return value.pid === region[1][0].id
                    })
                }
                break
            case 1:
                region[2] = this.data.allCountries.filter(value => {
                    return value.pid === region[1][index].id
                })
                break
        }
        this.setData({
            region:region
        })
    },
    //初始化默认的城市列表 
    initRegion(){
        if(this.data.regionFinish === 3){
            const region = this.data.region
            region[1] = this.data.allCities.filter(value => {
                return value.pid === region[0][0].id
            })
            if(region[1].length>0){
                region[2] = this.data.allCountries.filter(value => {
                    return value.pid === region[1][0].id
                })
            }
            this.setData({
                region:region
            })
        }
    },
    onRegionChange(e){
        const data = e.detail.value
        const region = this.data.region
        let position = region[0][data[0]].name
        let showPosition = position
        let avea = region[0][data[0]]//区域信息
        //防止数组越界
        if(region[1].length>0){
            position = position + region[1][data[1]].name
            showPosition = region[1][data[1]].name
            avea = region[1][data[1]].name
        }
        if(region[2].length>0){
            position = position + region[2][data[2]].name
            showPosition = region[2][data[2]].name
            avea = region[2][data[2]]
        }
        console.log(position)
        //保存地址信息
        wx.setStorageSync('position', position)
        wx.setStorageSync('showPosition', showPosition)
        wx.setStorageSync('positionObj', avea)
        getApp().globalData.position = position
        getApp().globalData.showPosition = showPosition
        getApp().globalData.positionObj = avea
        this.setData({
            showPosition:showPosition
        })
    },
    //跳转电影详情
    showFilm(e){
        const uid = e.currentTarget.dataset.value
        wx.navigateTo({
          url: this.data.currentItem==0?'/pages/film/film?showBuy=true&uid='+uid:'/pages/film/film?uid='+uid,
        })
    },
    goBuy(e){
        const uid = e.currentTarget.dataset.value
        wx.navigateTo({
          url: '/pages/chose_cinema/chose_cinema?uid='+uid
        })
    }
})