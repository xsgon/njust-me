import util from 'common/js/util'
// import 'common/js/crypto-js/aes'
// import 'common/js/crypto-js/core-min'
// import CryptoJS from 'aes'
//
import CryptoJS from 'crypto-js'
// require('common/js/crypto-js/core-min') ;
// const CryptoJS = require('common/js/crypto-js/aes') ;
export default {
    encryptData: function (data) {
        let token = util.getRandomStringWithLength(16);
        var key = "NopgpCSccv9YQ7lq";
        let encrypted = CryptoJS.enc.Utf8.parse(data.param + token);
        key = CryptoJS.enc.Utf8.parse(key);
        encrypted = CryptoJS.AES.encrypt(encrypted, key, {iv: key, mode: CryptoJS.mode.CBC}).toString();
        data.param = encrypted;
        data.token = token;
        return data;
    }
}