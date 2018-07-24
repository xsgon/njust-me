// import cryptoWrapper from 'common/js/cryptoWrapper'
import axios from 'axios'
/*
normal page param format
private int page;
private int pageSize;
private Object body;
 */

let genHeader = () => {
    return {
        headers: {'Content-Type': 'application/json', 'Authorization': sessionStorage.getItem('token')}
    };
};

let urlHost = 'http://localhost:9119';    // local spring boot server
let URL_SESSION = '/session';

let URL_MAIN_TEST_READ_ALL = '/main_test/read/all';

let URL_USER_READ_ALL = '/user/read/all';
let URL_USER_UPDATE_SELF_PROFILE = '/user/update/self';
let URL_USER_ADD_NEW = '/user/add/new_user';

let URL_EXP_ADD_NEW = '/main_test/add/test';

let URL_SAMPLE_READ_ALL = '/sample/read/all';

let rootIP = process.env.API_ROOT;

let api = {};

// export const requestLogin = params => { return axios.post(`${urlMock}/login`, params).then(res => res.data); };
api.requestLogin = (params) => {
    return axios.post(urlHost + URL_SESSION, params);
};

api.requestLogout = () => {
    let user = JSON.parse(sessionStorage.getItem('user'));
    if (user !== null) {
        return axios.post(urlHost + URL_SESSION, {'username': user.id, 'logout': ''})
    }
};

api.getMainTest = (params) => {
    return axios.post(urlHost + URL_MAIN_TEST_READ_ALL, params, genHeader());
};

api.getUserList = (params) => {
    return axios.post(urlHost + URL_USER_READ_ALL, params, genHeader());
};

api.addUser = (params) => {
    return axios.post(urlHost + URL_USER_ADD_NEW, params, genHeader());
};

api.updateSelf = (params) => {
    return axios.post(urlHost + URL_USER_UPDATE_SELF_PROFILE, params, genHeader());
};

api.getSampleList = (params) => {
    return axios.post(urlHost + URL_SAMPLE_READ_ALL, params, genHeader());
};

api.addExp = (params) => {
    return axios.post(urlHost + URL_EXP_ADD_NEW, params, genHeader());
};

export default api;

export const getUserList = params => {
    return axios.get(`${urlMock}/user/list`, {params: params});

};

//真实环境时，禁掉mock，请求真实后台地址
export const getUserList1 = params => {
    return axios.get(host + `/user/queryAllUser`);
};

export const getUserList2 = params => {
    return axios.get(`/user/queryAllUser`);
};

export const getUserListPage = params => {
    return axios.get(`${urlMock}/user/listpage`, {params: params});
};

export const removeUser = params => {
    return axios.get(`${urlMock}/user/remove`, {params: params});
};

export const batchRemoveUser = params => {
    return axios.get(`${urlMock}/user/batchremove`, {params: params});
};

export const editUser = params => {
    return axios.get(`${urlMock}/user/edit`, {params: params});
};

export const addUser = params => {
    return axios.get(`${urlMock}/add/new_user`, {params: params});
};

export const getInfoList = params => {
    return axios.get(`${urlMock}/info/list`, {params: params});
};
// 联系我们
export const getTelephone = params => {
    return axios.get(`${urlMock}/telephone/list`, {params: params});
};

// // add by wulin
// export const wJsonPost = (url, postData) => {
//     return new Promise((resolve, reject) => {
//
//         $.ajax({
//             dataType: 'json',
//             type: 'POST',
//             url: url,
//             data: cryptoWrapper.encryptData(postData),
//             success: function (data) {
//                 resolve(data)
//             },
//             error: function (jqXHR, textStatus, errorThrown) {
//                 reject(jqXHR, textStatus, errorThrown)
//             }
//         });
//     })
// };