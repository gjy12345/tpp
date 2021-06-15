import {
    getCinemaList
} from '../../api/cinema'
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
        height: 1000,
        resourceUrl: getApp().globalData.resourceUrl,
        region: [
            [],
            [],
            []
        ],
        allCities: [],
        allCountries: [],
        regionFinish: 0,
        showPosition: '',
        listQuery:{
            area:null,
            offset:0,
            pageSize:10
        },
        dialogVisible: false,
        top_layout_height: 0,
        aveaItems:[],
        nowChoseAvea: null,
        cinemaList: [],
        isLoading: false,
        currentAreaId: 0
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.loadData()
        this.loadRegionData()
        this.calcHeight()
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
        }else if(this.data.regionFinish===3){
            //都加在结束后
            this.loadAreaItems()
        }
        if(this.data.cinemaList.length===0||this.data.currentAreaId!==getApp().globalData.positionObj.id){
            this.data.offset = 0
            this.data.cinemaList = []
            this.loadData()
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
        if(this.data.isLoading){
            return
        }
        wx.showLoading({
          title: '加载影院中',
        })
        this.data.isLoading = true
        let params = JSON.parse(JSON.stringify(this.data.listQuery))
        const position = getApp().globalData.positionObj
        this.data.currentAreaId = position.id
        if(params.area===null){
            //如果是三级分类，那么展示所有此二级分类下所有的城市
            params.area = (position.level===2?position.pid:position.id)
        }
        getCinemaList(params).then(res=>{
            if(res.data.data.length===0){
                wx.showToast({
                  title: '没有更多了!',
                  icon: 'error'
                })
                return
            }
            this.data.listQuery.offset=res.data.offset
            this.setData({
                cinemaList: this.data.cinemaList.concat(res.data.data),
            })
        }).finally(_=>{
            this.data.isLoading = false
            wx.hideLoading()
        })
    },
    //动态计算scroll-view的高度
    calcHeight() {
        const query = this.createSelectorQuery()
        query.select('#top').boundingClientRect()
        query.select('#top_line').boundingClientRect()
        query.exec(res => {
            const height = res[0].height + res[1].height
            this.setData({
                height: wx.getSystemInfoSync().windowHeight-height,
                top_layout_height: height
            })
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
            //在第一次加载结束后计算
            this.loadAreaItems()
        }
    },
    onRegionChange(e){
        const data = e.detail.value
        const region = this.data.region
        let position = region[0][data[0]].name
        let showPosition = position
        let area = region[0][data[0]]//区域信息
        //防止数组越界
        if(region[1].length>0){
            position = position + region[1][data[1]].name
            showPosition = region[1][data[1]].name
            area = region[1][data[1]].name
        }
        if(region[2].length>0){
            position = position + region[2][data[2]].name
            showPosition = region[2][data[2]].name
            area = region[2][data[2]]
        }
        console.log(area)
        //保存地址信息
        wx.setStorageSync('position', position)
        wx.setStorageSync('showPosition', showPosition)
        wx.setStorageSync('positionObj', area)
        getApp().globalData.position = position
        getApp().globalData.showPosition = showPosition
        getApp().globalData.positionObj = area
        this.setData({
            showPosition:showPosition
        })
        this.loadAreaItems()
    },
    //重置区域选项
    loadAreaItems(){
        const area = getApp().globalData.positionObj
        if(area.level===1||area.level===2){
            //县或市
            this.data.aveaItems = (area.level===1?this.data.allCities:this.data.allCountries)
            .filter(value=>{
                return value.pid === area.pid
            })
            //解决flex布局最后一行如果不满时，位置错乱
            const len = this.data.aveaItems.length + 1 //+1:默认的item
            if(Math.ceil(len/4)!==Math.floor(len/4)){
                //不能整除
                const c = Math.ceil(len/4) * 4 - len
                for(let i = 0; i < c; i++ ){
                    this.data.aveaItems.push({id:-99999})
                }
            }
            this.setData({
                aveaItems: this.data.aveaItems
            })
        }else{
            this.data.aveaItems = []
        }
    },
    selectAvea(){
        this.setData({
            dialogVisible: !this.data.dialogVisible
        })
    },
    choseAvea(e){
        let area = e.currentTarget.dataset.value
        let nowChoseAvea = null
        if(area!=null){
            nowChoseAvea = area.name
            area = area.id
        }
        this.data.listQuery.area = area
        this.setData({
            listQuery:this.data.listQuery,
            dialogVisible:false,
            nowChoseAvea: nowChoseAvea
        })
        this.data.listQuery.offset = 0
        this.setData({
            cinemaList: []
        })
        this.loadData()
    },
    goTicket(e){
        const cinema = e.currentTarget.dataset.value
        wx.navigateTo({
          url: '/pages/chose_ticket/chose_ticket?cinemaUid='+cinema
        })
    }
})