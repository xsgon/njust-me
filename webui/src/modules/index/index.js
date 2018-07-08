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
// import Mock from '../../mock'

// Mock.bootstrap();
import 'font-awesome/css/font-awesome.min.css'

Vue.use(ElementUI);
Vue.use(Vuex);

window.common = util;
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

