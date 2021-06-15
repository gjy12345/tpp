const BASE_SERVER_URL = 'http://127.0.0.1:8080'
export default function request(args) {
    var header = { // 设置请求头
        'content-type': args.isJson ? 'application/json' : 'application/x-www-form-urlencoded',
        "Token": wx.getStorageSync('token') || ''
    };
    console.log(args.url)
    wx.showNavigationBarLoading();
    return new Promise((resolve, reject) => { // 返回一个Promise
        wx.request({
            url: BASE_SERVER_URL + args.url, // 请求地址
            data: args.data,
            header: header,
            method: args.method,
            success: function (res) {
                const data = res.data
                if(data.code===60204){
                    //需要登录
                    wx.redirectTo({
                        url: '/pages/login/login'
                    })
                    console.log('未登录')
                    wx.removeStorageSync('token')
                    console.log(wx)
                    reject(new Error(res.data.msg || '未登录'))
                    return
                }
                if(res.data.code !== 20000){
                    reject(new Error(res.data.msg || 'Error'))
                    return
                }
                resolve(res.data)
            },
            fail: function (err) {
                reject(err)
            },
            enableCache: true,
            timeout: 20000,
            complete: function (vv) { // 接口调用结束的回调函数（调用成功、失败都会执行）
                // console.log(vv);
                wx.hideNavigationBarLoading()
            }
        })
    }).catch(err => {
        wx.showToast({
          title: '错误:'+err.message,
          icon:'none',
          duration:1000
        })
        console.log(err);
        return Promise.reject(error)
    })
}