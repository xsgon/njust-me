import Login from '../modules/index/views/Login.vue'
import NotFound from '../modules/index/views/404.vue'
import Home from '../modules/index/views/Home.vue'
import Table from '../modules/index/views/nav1/Table.vue'
import Info from '../modules/index/views/nav1/Info.vue'
import Form from '../modules/index/views/nav1/Form.vue'
import Page4 from '../modules/index/views/nav2/Page4.vue'
import TelManage from '../modules/index/views/nav2/TelManage.vue'
import Page5 from '../modules/index/views/nav2/Page5.vue'
import Page6 from '../modules/index/views/nav3/Page6.vue'
import echarts from '../modules/index/views/charts/echarts.vue'

import ExpManage from '../modules/index/views/experiment/exp-manage.vue'
import MeasManage from '../modules/index/views/measurement/meas-manage.vue'
import PlatManage from '../modules/index/views/platform/platform-manage.vue'
import SamManage from '../modules/index/views/sample/sam-manage.vue'
import User from '../modules/index/views/users/users.vue'

import HistoryView from '../modules/index/views/HistoryView.vue'

import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routers = new VueRouter({
    // mode: 'history',
    routes: [
        {
            path: '/login',
            component: Login,
            name: '',
            hidden: true
        },
        /*{
            path: '/',
            redirect: '/table',
            hidden: true
        },*/
        {
            path: '/404',
            component: NotFound,
            name: '',
            hidden: true
        },

        {
            path: '/',
            component: Home,
            name: '实验管理',
            iconCls: 'fa fa-tasks',//图标样式class
            children: [
                {path: '/experiment-list', component: ExpManage, name: '实验列表', iconCls: 'fa fa-cubes'},
                {path: '/platform-list', component: PlatManage, name: '实验设备列表', iconCls: 'fa fa-truck'},
                {path: '/measurement-list', component: MeasManage, name: '性能指标列表', iconCls: 'fa fa-thermometer-full'},
                {path: '/sample-list', component: SamManage, name: '样本列表', iconCls: 'fa fa-tag'},
            ]
        },


        {
            path: '/',
            component: Home,
            name: '投诉管理',
            iconCls: 'el-icon-message',//图标样式class
            children: [
                {path: '/table', component: Table, name: 'Table'},
                {path: '/info', component: Info, name: 'Info列表'},
                {path: '/form', component: Form, name: 'Form'},
            ]
        },
        {
            path: '/',
            component: Home,
            name: '后台编辑',
            iconCls: 'fa fa-id-card-o',
            children: [
                {path: '/page4', component: TelManage, name: '联系我们'},
                {path: '/page5', component: Page5, name: '公园广场'},
                {path: '/page5', component: Page4, name: '公厕'},
                {path: '/historyview', component: HistoryView, name: '历史告警'}
            ]
        },
        {
            path: '/',
            component: Home,
            name: '',
            iconCls: 'fa fa-address-card',
            leaf: true,//只有一个节点
            children: [
                {path: '/user', component: User, name: '用户管理'}
            ]
        },
        {
            path: '/',
            component: Home,
            name: '订阅号管理',
            iconCls: 'fa fa-bar-chart',
            children: [
                {path: '/echarts', component: echarts, name: '微矩阵管理'}
            ]
        },
        {
            path: '/',
            component: Home,
            name: 'Charts1',
            iconCls: 'fa fa-bar-chart',
            children: [
                {path: '/echarts', component: echarts, name: 'echarts'},
                {path: '/echarts2', component: echarts, name: 'echarts2'}
            ]
        }
    ]
})

export default routers;