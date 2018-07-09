<template>
    <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="0px"
             class="demo-ruleForm login-container">
        <h3 class="title">系统登录</h3>
        <el-form-item prop="account">
            <el-input type="text" v-model="ruleForm2.account" auto-complete="off" placeholder="账号"></el-input>
        </el-form-item>
        <el-form-item prop="checkPass">
            <el-input type="password" v-model="ruleForm2.checkPass" auto-complete="off" placeholder="密码"></el-input>
        </el-form-item>
        <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>
        <el-form-item style="width:100%;">
            <el-button type="primary" style="width:100%;" @click.native.prevent="handleSubmit2" :loading="logining">登录
            </el-button>
            <!--<el-button @click.native.prevent="handleReset2">重置</el-button>-->
        </el-form-item>
    </el-form>
</template>

<script>
    import api from 'api/api';
    //import NProgress from 'nprogress'
    export default {
        data() {
            return {
                logining: false,
                ruleForm2: {
                    account: 'admin',
                    checkPass: '123456'
                },
                rules2: {
                    account: [
                        {required: true, message: '请输入账号', trigger: 'blur'},
                        //{ validator: validaePass }
                    ],
                    checkPass: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        //{ validator: validaePass2 }
                    ]
                },
                checked: true
            };
        },
        methods: {
            handleReset2() {
                this.$refs.ruleForm2.resetFields();
            },
            handleSubmit2(ev) {
                let _this = this;
                this.$refs.ruleForm2.validate((valid) => {
                    if (valid) {
                        //_this.$router.replace('/table');
                        this.logining = true;
                        //NProgress.start();
                        api.requestLogin({username: this.ruleForm2.account, password: this.ruleForm2.checkPass})
                            .then((res) => {
                                this.logining = false;

                                sessionStorage.setItem('token', res.data.body.token);
                                sessionStorage.setItem('user', common.obj2s(res.data.body.user));
                                this.$router.push({path: '/experiment-list'});
                            })
                            .catch(error => {
                                this.logining = false;
                                // if (error.response) {
                                //     common.toastMsg('登录失败：\nCode: '
                                //         + data.code + '\nMsg: '
                                //         + data.message, 'error');
                                // } else if (error.request) {
                                //     common.toastMsg('登录失败：\n网络故障: ' + error.request, 'error');
                                // } else {
                                //     common.toastMsg('登录失败：\n网络故障: ' + error.message, 'error');
                                // }
                                common.handleNWException(error);
                            });
                        // let url = 'http://localhost:9119/session';
                        // axios.post(url, loginParams)
                        //     .then(res => {
                        //         console.log(res);
                        //     })
                        //     .catch(error => {
                        //         console.log(error.response);
                        //     });
                        // axios.post(url, loginParams
                        //     // , {
                        //     //     headers: { 'X-LORDAR-TOKEN': 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMDk5ODg3NyIsIm5hbWUiOiLnjovlt6flubQiLCJleHAiOjE1Mzc3NjY3MjJ9.ZdIWVunCRt86JM7xOzmasKgk9_NzhXYLiGdfJwxNL8w' }
                        //     // }
                        //
                        // ).then(response => {
                        //     resolve(response.data)
                        // }).catch(response => {
                        //     reject(response)
                        //     common.toastMsg('请求超时，请检查网络', 'warn')
                        // })
                        // requestLogin(loginParams).then(data => {
                        //     this.logining = false;
                        //     //NProgress.done();
                        //     let {msg, code, user} = data;
                        //     if (code !== 200) {
                        //         this.$message({
                        //             message: msg,
                        //             type: 'error'
                        //         });
                        //     } else {
                        //         sessionStorage.setItem('user', JSON.stringify(user));
                        //         this.$router.push({path: '/experiment-list'});
                        //     }
                        // });
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        }
    }

</script>

<style lang="scss" scoped>
    .login-container {
        /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
        -webkit-border-radius: 5px;
        border-radius: 5px;
        -moz-border-radius: 5px;
        background-clip: padding-box;
        margin: 180px auto;
        width: 350px;
        padding: 35px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;
        .title {
            margin: 0px auto 40px auto;
            text-align: center;
            color: #505458;
        }
        .remember {
            margin: 0px 0px 35px 0px;
        }
    }
</style>