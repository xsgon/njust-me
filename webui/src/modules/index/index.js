// import babelpolyfill from 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import store from '../../vuex/store'
import Vuex from 'vuex'
//import NProgress from 'nprogress'
//import 'nprogress/nprogress.css'
import router from '../../route/routes'
import util from '../../common/js/util'
import role from '../../common/role-define'
import session from '../../common/js/session'
// import Mock from '../../mock'

// Mock.bootstrap();
import 'font-awesome/css/font-awesome.min.css'

Vue.use(ElementUI);
Vue.use(Vuex);

// Date
Date.prototype.Format = function (fmt) { //author: meizz
    let o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (let k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

// public modules
window.common = util;
window.ROLE = role;
window.SESSION = session;

//NProgress.configure({ showSpinner: false });

router.beforeEach((to, from, next) => {
    //NProgress.start();
    // 超级简单的判断是否登录，有极大危险
    // 可以在这里增加逻辑判断？
    console.log("before router");
    if (to.path === '/login') {
        // sessionStorage.removeItem('user');
        sessionStorage.removeItem('user');
        sessionStorage.removeItem('token');
    }
    // let user = JSON.parse(sessionStorage.getItem('user'));
    if (!sessionStorage.getItem('token') && to.path !== '/login') {
        next({path: '/login'});
    } else {
        next();
    }
});

//router.afterEach(transition => {
//NProgress.done();
//});

const bus = new Vue({
    //el: '#app',
    //template: '<App/>',
    router,
    store,
    //components: { App }
    render: h => h(App)
}).$mount('#app')

window.bus = bus;

