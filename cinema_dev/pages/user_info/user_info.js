// pages/user_info/user_info.js
import {
    bindAndSet,formatDate
} from '../../utils/util'
import {updateUserInfo} from '../../api/user'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        userInfo: null,
        dialogVisible: false,
        tempInput: null,
        inputPlaceholder: null,
        inputVal: null,
        inputType: null,
        inputDataType: null,
        sexArray: ['女', '男'],
        years:[1,2,3,4,5,6,7,8,9,10,11,12],
        months: [1,2,3,4,5],
        day:[1,2,3,4,5],
        today:null
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        //深拷贝
        const customer = JSON.parse(JSON.stringify(getApp().globalData.userInfo))
        let avatar = '/images/icon/head.jpg'
        if (customer && customer.avatar) {
            avatar = getApp().globalData.resourceUrl + customer.avatar
        }
        console.log(customer)
        this.setData({
            userInfo: customer,
            avatar: avatar,
            today: formatDate(new Date)
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
    inputCancel() {
        this.setData({
            dialogVisible: false
        })
    },
    inputConfim() {
        const customer = this.data.userInfo
        customer[this.data.inputType] = this.data.tempInput
        this.setData({
            dialogVisible: false,
            userInfo: customer
        })
        console.log(this.data)
        console.log(this.data.inputType)
    },
    onInputChange(e) {
        bindAndSet(e, this)
    },
    showInputDialog(e) {
        const data = e.currentTarget.dataset
        this.setData({
            inputPlaceholder: data.pla,
            inputVal: data.type,
            dialogVisible: true,
            tempInput: data.value,
            inputType: data.type,
            inputDataType: data.inputDataType
        })
    },
    bindSexChange(e) {
        const customer = this.data.userInfo
        customer.sex = e.detail.value
        this.setData({
            userInfo: customer
        })
    },
    changeAvatar() {
        wx.showActionSheet({
            itemList: ['从相册中选择', '拍照'],
            itemColor: "#f7982a",
            success: (res) => {
                if (!res.cancel) {
                    if (res.tapIndex == 0) {
                        //从相册中选择
                        // console.log(res.tapIndex);
                        // 调用函数
                        this.chooseWxImageShop('album');
                    } else if (res.tapIndex == 1) {
                        //手机拍照
                        // console.log(res.tapIndex);
                        // 调用函数
                        this.chooseWxImageShop('camera');
                    }
                }
            }
        })
    },
    chooseWxImageShop: function (type) {
        wx.chooseImage({
            count: 1,
            sizeType: ['original', 'compressed'],
            sourceType: [type],
            success: (res) => {
                const tempFilePath = res.tempFilePaths[0]
                console.log(tempFilePath) //slice
                wx.uploadFile({
                    filePath: tempFilePath,
                    name: 'file',
                    url: getApp().globalData.BASE_SERVER_URL + '/common/file/uploadImages',
                    header: {
                        'content-type': 'multipart/form-data',
                        'token': wx.getStorageSync('token')
                    },
                    success: res => {
                        // const data = res.data
                        const data = JSON.parse(res.data)
                        if (data.code === 20000) {
                            let customer = this.data.userInfo
                            customer.avatar = data.data
                            this.setData({
                                userInfo: customer,
                                avatar: getApp().globalData.resourceUrl + customer.avatar
                            })
                            wx.showToast({
                                title: '上传成功',
                            })
                        } else {
                            wx.showToast({
                                icon: 'none',
                                title: '上传失败',
                            })
                        }
                    },
                    fail: res => {
                        wx.showToast({
                            icon: 'none',
                            title: '上传失败',
                        })
                    }
                })
            }
        })

    },
    updateUserInfo() {
        wx.showLoading({
          title: '更新资料中...',
        })
        const customer = this.data.userInfo
        customer.sex = Number(customer.sex)
        updateUserInfo(customer).then(res=>{
            wx.showToast({
              title: '更新成功!'
            })
            getApp().globalData.userInfo = this.data.userInfo
        }).finally(_=>{
            wx.hideLoading()
        })
    },
    bindBirthChange(e){
        console.log(e)
        const customer = this.data.userInfo
        customer.birthday = e.detail.value
        this.setData({
            userInfo: customer
        })
    },
    logout(){
        getApp().globalData.userInfo = null
        wx.removeStorageSync('token')
        wx.navigateBack()
    }
})