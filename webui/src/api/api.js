// import cryptoWrapper from 'common/js/cryptoWrapper'
import axios from 'axios'

let urlHost = 'http://localhost:9119';    // local spring boot server

let URL_SESSION = '/session';
// let urlProd = '';                          // product spring boot server
// let urlMock = '';                          // mock to simulate http server
// let host = 'http://localhost:8080/Urban/rest';
var rootIP = process.env.API_ROOT;

// export const requestLogin = params => { return axios.post(`${urlMock}/login`, params).then(res => res.data); };
export const requestLogin = (params) => {
    // return axios.post(urlHost + '/session', params).then(res => res);
    //return http.apiPost(url, params);
    return axios.post(urlHost + URL_SESSION, params);
};

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
    return axios.get(`${urlMock}/user/add`, {params: params});
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