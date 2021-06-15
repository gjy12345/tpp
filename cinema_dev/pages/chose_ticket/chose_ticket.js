import {
    formatMonthDay,
    formatDate
} from '../../utils/util'
import {
    getCinemaInfo,
    getCinemaScheduleFilms
} from '../../api/cinema'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        resourceUrl:getApp().globalData.resourceUrl,
        showDays: [],//界面展示用
        queryDays: [], //传递后台使用
        currentDay: 0 ,//数组下标,
        filmIndex: 0,
        films: [],
        schedule:[],
        currentFilm:null,
        currentSchedules: [],
        currentSchedule: [],
        scheduleHeight: 500,
        cinemaUid: 0,
        filmUid: null,
        cinema: null
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        wx.getSystemInfo({
            success:res=>{
                this.setData({
                    covers_margin: res.windowWidth / 2
                })
            }
        })
        this.data.cinemaUid = options.cinemaUid
        this.data.filmUid = options.filmUid
        this.loadCinemaScheduleFilms()
        this.loadCinemaInfo()
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
    changeDay(e){
        this.setData({
            currentDay: e.currentTarget.dataset.value
        })
        this.changeDayFilmSchedule()
    },
    clickFilm(e){
        const x = e.currentTarget.dataset.value
        if(x!==this.data.filmIndex){
            this.setData({
                filmIndex: x
            })
            //加载数据
            this.loadFilmSchedule()
        }
    },
    loadCinemaScheduleFilms(){
        wx.showLoading({
          title: '加载中',
        })
        getCinemaScheduleFilms({
            cinemaUid: this.data.cinemaUid
        }).then(res=>{
            const data = res.data
            const films = []
            const schedule = []
            for(const item of data){
                films.push(item.film)
                schedule.push(item.days)
            }
            if(this.data.filmUid){
                this.data.filmIndex = films.findIndex(value=>{
                    return value.uid === this.data.filmUid
                })
                if(!this.data.filmUid)
                    this.data.filmIndex = 0
            }
            this.setData({
                films:films,
                schedule: schedule,
                filmIndex: this.data.filmIndex
            })
            this.loadFilmSchedule()
        }).finally(_=>{
            wx.hideLoading()
        })
    },
    //加载影片的排片记录
    loadFilmSchedule(){
        const currentIndex = this.data.filmIndex
        const schedule = this.data.schedule[currentIndex]
        this.setData({
            currentFilm: this.data.films[currentIndex],
            currentSchedules: schedule,
            currentDay: 0
        })
        this.changeDayFilmSchedule()
    },
    //切换影片的排片记录
    changeDayFilmSchedule(){
        this.setData({
            currentSchedule:this.data.currentSchedules[this.data.currentDay].scheduleList
        })
    },
    loadCinemaInfo(){
        getCinemaInfo(this.data.cinemaUid).then(res=>{
            this.setData({
                cinema:res.data
            })
        })
    },
    goChoseSite(e){
        wx.navigateTo({
          url: '/pages/chose/chose?uid='+e.currentTarget.dataset.value+'&hall='+e.currentTarget.dataset.hall+'&begin='+e.currentTarget.dataset.begin+'&day='+this.data.currentSchedules[this.data.currentDay].day
        })
    }
})