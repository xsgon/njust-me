import axios from 'axios'

const apiMethods = {
    methods: {
        apiGet(url, data) {
            return new Promise((resolve, reject) => {
                axios.get(url, data).then(response => {
                    resolve(response.data)
                }, response => {
                    reject(response)
                    // _g.closeGlobalLoading()
                    common.toastMsg('请求超时，请检查网络', 'warn')
                })
            })
        },
        apiPost(url, data) {
            // url += '?X-LORDAR-TOKEN=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMDk5ODg3NyIsIm5hbWUiOiLnjovlt6flubQiLCJleHAiOjE1Mzc3NjY3MjJ9.ZdIWVunCRt86JM7xOzmasKgk9_NzhXYLiGdfJwxNL8w'
            return new Promise((resolve, reject) => {
                axios.post(url, data
                    // , {
                    //     headers: { 'X-LORDAR-TOKEN': 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMDk5ODg3NyIsIm5hbWUiOiLnjovlt6flubQiLCJleHAiOjE1Mzc3NjY3MjJ9.ZdIWVunCRt86JM7xOzmasKgk9_NzhXYLiGdfJwxNL8w' }
                    // }
                ).then(response => {
                    resolve(response.data)
                }).catch(response => {
                    reject(response)
                    common.toastMsg('请求超时，请检查网络', 'warn')
                })
            })
        },
        apiDelete(url, id) {
            return new Promise((resolve, reject) => {
                axios.delete(url + id).then(response => {
                    resolve(response.data)
                }, response => {
                    reject(response)
                    // _g.closeGlobalLoading()
                    common.toastMsg('请求超时，请检查网络', 'warn')
                })
            })
        },
        apiPut(url, id, obj) {
            return new Promise((resolve, reject) => {
                axios.put(url + id, obj).then(response => {
                    resolve(response.data)
                }, response => {
                    reject(response)
                    // _g.closeGlobalLoading()
                    common.toastMsg('请求超时，请检查网络', 'warn')
                })
            })
        },
        handelResponse(res, cb, errCb) {
            // _g.closeGlobalLoading()
            if (res.code === 200) {
                cb(res.data)
            } else {
                if (typeof errCb == 'function') {
                    errCb()
                }
                this.handleError(res)
            }
        },
        handleError(res) {
            if (res.code) {
                switch (res.code) {
                    case 101:
                        console.log('cookie = ', Cookies.get('rememberPwd'))
                        if (Cookies.get('rememberPwd')) {
                            const data = {
                                rememberKey: Lockr.get('rememberKey')
                            }
                            this.reAjax('admin/relogin', data).then(res => {
                                this.handelResponse(res, data => {
                                    this.resetCommonData(data)
                                })
                            })
                        } else {
                            // _g.toastMsg('error', res.error)
                            setTimeout(() => {
                                router.replace('/')
                            }, 1500)
                        }
                        break
                    case 103:
                        // _g.toastMsg('error', res.error)
                        setTimeout(() => {
                            router.replace('/')
                        }, 1500)
                        break
                    default:
                        // _g.toastMsg('error', res.error)
                }
            } else {
                console.log('default error')
            }
        },
        resetCommonData(data) {
            console.log('...')
        },
        reAjax(url, data) {
            return new Promise((resolve, reject) => {
                axios.post(url, data).then(response => {
                    resolve(response.data)
                }, response => {
                    reject(response)
                    common.toastMsg('请求超时，请检查网络', 'warn')
                })
            })
        }
    },
    computed: {
        showLoading() {
            return store.state.globalLoading
        }
    }
}

export default apiMethods