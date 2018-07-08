var SIGN_REGEXP = /([yMdhsm])(\1*)/g;
var DEFAULT_PATTERN = 'yyyy-MM-dd';
function padding(s, len) {
    var len = len - (s + '').length;
    for (var i = 0; i < len; i++) { s = '0' + s; }
    return s;
};

export default {
    getQueryStringByName: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        var context = "";
        if (r != null)
            context = r[2];
        reg = null;
        r = null;
        return context == null || context == "" || context == "undefined" ? "" : context;
    },

    formatDate: {
        format: function (date, pattern) {
            pattern = pattern || DEFAULT_PATTERN;
            return pattern.replace(SIGN_REGEXP, function ($0) {
                switch ($0.charAt(0)) {
                    case 'y': return padding(date.getFullYear(), $0.length);
                    case 'M': return padding(date.getMonth() + 1, $0.length);
                    case 'd': return padding(date.getDate(), $0.length);
                    case 'w': return date.getDay() + 1;
                    case 'h': return padding(date.getHours(), $0.length);
                    case 'm': return padding(date.getMinutes(), $0.length);
                    case 's': return padding(date.getSeconds(), $0.length);
                }
            });
        },
        parse: function (dateString, pattern) {
            var matchs1 = pattern.match(SIGN_REGEXP);
            var matchs2 = dateString.match(/(\d)+/g);
            if (matchs1.length == matchs2.length) {
                var _date = new Date(1970, 0, 1);
                for (var i = 0; i < matchs1.length; i++) {
                    var _int = parseInt(matchs2[i]);
                    var sign = matchs1[i];
                    switch (sign.charAt(0)) {
                        case 'y': _date.setFullYear(_int); break;
                        case 'M': _date.setMonth(_int - 1); break;
                        case 'd': _date.setDate(_int); break;
                        case 'h': _date.setHours(_int); break;
                        case 'm': _date.setMinutes(_int); break;
                        case 's': _date.setSeconds(_int); break;
                    }
                }
                return _date;
            }
            return null;
        }
    },

    getRandomStringWithLength: function(len) {
        let chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890';
        let maxPos = chars.length;
        let pwd = '';
        for (let i = 0; i < len; i++) {
            pwd += chars.charAt(Math.floor(Math.random() * maxPos));
        }
        return pwd;
    },

    obj2s: (obj) => {
        return JSON.stringify(obj)
    },

    cloneJson: (obj) => {
        return JSON.parse(JSON.stringify(obj))
    },

    closeGlobalLoading: () => {
        setTimeout(() => {
            store.dispatch('showLoading', false)
        }, 0)
    },

    openGlobalLoading: () => {
        setTimeout(() => {
            store.dispatch('showLoading', true)
        }, 0)
    },

    // 删除左右两端的空格
    trim: (str) => {
        return str.replace(/(^\s*)|(\s*$)/g, '')
    },

    toastMsg: (msg, type) => {
        let position = 'middle'
        switch (type) {
            case 'normal':
                // bus.$vux.toast.text(msg, position)
                bus.$message.info(msg);
                break
            case 'success':
                // bus.$vux.toast.show({
                //     text: msg,
                //     type: 'success',
                //     position: position
                // })
                bus.$message.success(msg);
                break
            case 'warning':
                // bus.$vux.toast.show({
                //     text: msg,
                //     type: 'warn',
                //     position: position
                // })
                bus.$message.warning(msg);
                break
            case 'error':
                // bus.$vux.toast.show({
                //     text: msg,
                //     type: 'cancel',
                //     position: position
                // })
                bus.$message.error(msg);
                break
            default:
                // bus.$vux.toast.text(msg, position)
                bus.$message.info(msg);
                break
        }
    },

    checkEmpty: function(v) {
        return !!v.trim()
    },

    checkReg(v, reg) {
        // var reg = {
        //     ImgCode: '/^[0-9a-zA-Z]{4}$/',
        //     SmsCode: '/^\d{4}$/',
        //     MailCode: '/^\d{4}$/',
        //     UserName: '/^[\w|\d]{4,16}$/',
        //     Password: '/^[\w!@#$%^&*.]{6,16}$/',
        //     Mobile: '/^1[3|4|5|7|8]\d{9}$/',
        //     RealName: '/^[\u4e00-\u9fa5|·]{2,16}$|^[a-zA-Z|\s]{2,20}$/',
        //     BankNum: '/^\d{10,19}$/',
        //     Money: '/^([1-9]\d*|[0-9]\d*\.\d{1,2}|0)$/',
        //     Answer: '/^\S+$/',
        //     Mail: '/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/'
        // }

        return reg.test(v)
    },

    validate(value, type, attText) {
        let att = ''
        let text = ''
        if (attText != undefined) {
            att = attText + '不能为空'
        } else {
            att = '内容不能为空'
        }
        switch (type) {
            case 'empty':
                text = (!!value.trim() === false) ? att : ''
                break
            case 'mail':
                text = (/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(value) == false) ? '请输入正确邮箱格式' : ''
                text = (!!value.trim() === false) ? '邮箱内容不能为空' : text
                break
            case 'phone':
                text = (/^1(3|4|5|7|8)\d{9}$/.test(value) === false) ? '请输入正确手机号码格式' : ''
                text = (!!value.trim() === false) ? '手机内容不能为空' : text
                break
            default:
                return true
        }
        if (text !== '') {
            common.toastMsg(text)
            return false
        }
        return true
    },

    // 日期补0
    formatDateNum: function(value) {
        if (parseInt(value) < 10) {
            return '0' + parseInt(value)
        } else {
            return parseInt(value) + ''
        }
    },

    sessionSet(name, obj) {
        return sessionStorage.setItem(name, JSON.stringify(obj))
    },

    sessionGet(name) {
        return JSON.parse(sessionStorage.getItem(name))
    }

};
