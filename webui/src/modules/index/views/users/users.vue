<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input v-model="filters.name" placeholder="姓名"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="getUsers">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleAdd">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="users" highlight-current-row v-loading="listLoading" @selection-change="selsChange"
                  style="width: 100%;">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column type="index" width="60">
            </el-table-column>
            <el-table-column prop="id" label="账号" width="100">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="150" sortable>
            </el-table-column>
            <el-table-column prop="role" label="角色" width="100" :formatter="formatRole">
            </el-table-column>
            <el-table-column prop="phone" label="手机" width="150" sortable>
            </el-table-column>
            <el-table-column label="操作" fixed="right">
                <template scope="scope">
                    <el-button size="small" type="text" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button size="small" type="text" @click="handlePasswordReset(scope.$index, scope.row)">重置密码
                    </el-button>
                    <el-button type="text" style="color: red;" @click="handleDel(scope.$index, scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--工具条-->
        <el-col :span="24" class="toolbar">
            <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="page"
                    :page-sizes="[100, 200, 300, 400]"
                    :page-size="pageSize"
                    style="float:right"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </el-col>

        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="editForm.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model="editForm.sex">
                        <el-radio class="radio" :label="1">男</el-radio>
                        <el-radio class="radio" :label="0">女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="年龄">
                    <el-input-number v-model="editForm.age" :min="0" :max="200"></el-input-number>
                </el-form-item>
                <el-form-item label="生日">
                    <el-date-picker type="date" placeholder="选择日期" v-model="editForm.birth"></el-date-picker>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input type="textarea" v-model="editForm.addr"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
            </div>
        </el-dialog>

        <!--新增界面-->
        <el-dialog title="添加用户" :visible.sync="addFormVisible" :close-on-click-modal="false">
            <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
                <el-form-item label="账号" prop="id">
                    <el-input v-model="addForm.id" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input type="password" v-model="addForm.password" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input type="password" v-model="addForm.confirmPassword" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="addForm.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="角色">
                    <el-radio-group v-model="addForm.role" @change="onRadioChange">
                        <el-radio v-if="myRole=='ROLE_SUPER_ADMIN'" class="radio" :label="'ROLE_ADMIN'">管理员</el-radio>
                        <el-radio class="radio" :label="'ROLE_NORMAL'">一般用户</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input prefix-icon="el-icon-phone" v-model="addForm.phone"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>
    </section>
</template>

<script>
    //import NProgress from 'nprogress'
    import api from 'api/api';
    import ROLE from "../../../../common/role-define";
    import util from "../../../../common/js/util";

    export default {
        data() {
            let confirmPasswordValidator = (rule, value, callback) => {
                if (value !== this.addForm.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                filters: {
                    name: ''
                },
                myRole: '',
                users: [],
                total: 0,
                page: 1,
                pageSize: 100,
                listLoading: false,
                sels: [],//列表选中列

                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'}
                    ]
                },
                //编辑界面数据
                editForm: {
                    id: 0,
                    name: '',
                    sex: -1,
                    age: 0,
                    birth: '',
                    addr: ''
                },

                addFormVisible: false,//新增界面是否显示
                addLoading: false,

                //新增界面数据
                addForm: {
                    name: '',
                    phone: '',
                    userRole: 'ROLE_NORMAL',
                    id: '',
                    password: '',
                    confirmPassword: ''
                },

                // 新增界面的数据校验
                addFormRules: {
                    // 姓名校验
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'}
                    ],
                    // 手机号校验
                    phone: [
                        {required: true, validator: common.phoneValidator, trigger: 'blur'}
                    ],
                    // 登录名校验
                    id: [
                        {required: true, validator: common.idValidator, trigger: 'blur'}
                    ],
                    // 密码校验
                    password: [
                        {required: true, validator: common.passwordValidator, trigger: 'blur'}
                    ],
                    // 重复密码校验
                    confirmPassword: [
                        {required: true, validator: confirmPasswordValidator, trigger: 'blur'}
                    ],
                },

            }
        },
        methods: {
            //角色显示转换
            formatRole: function (row, column) {
                return ROLE[row.role];
            },
            handleCurrentChange(val) {
                this.page = val;
                this.getUsers();
            },
            handleSizeChange(val) {
                this.pageSize = val;
                console.log('每页 ${val} 条');
            },
            onRadioChange(val) {
                console.log('radio changed to ' + val);
            },
            //获取用户列表
            getUsers() {
                let para = {
                    page: this.page - 1,  // start from 0
                    //name: this.filters.name
                };
                this.listLoading = true;
                //NProgress.start();
                // getUserListPage(para).then((res) => {
                // 	this.total = res.data.total;
                // 	this.users = res.data.users;
                // 	this.listLoading = false;
                // 	//NProgress.done();
                // });
                api.getUserList(para)
                    .then((res) => {
                        common.handleReturn(res, (res) => {
                            this.total = res.data.total;
                            this.users = res.data.body;
                            this.listLoading = false;
                        })
                    })
                    .catch((err) => {
                        common.handleNWException(err);
                    });
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                    let para = {id: row.id};
                    removeUser(para).then((res) => {
                        this.listLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getUsers();
                    });
                }).catch(() => {

                });
            },
            // 重置密码
            handlePasswordReset: (index, row) => {

            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);
            },
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
                this.addForm = {
                    name: '',
                    phone: '',
                    role: 'ROLE_NORMAL',
                    id: '',
                    password: '',
                    confirmPassword: ''
                };
            },
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true;
                            //NProgress.start();
                            let para = Object.assign({}, this.editForm);
                            editUser(para).then((res) => {
                                this.editLoading = false;
                                //NProgress.done();
                                this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                this.$refs['editForm'].resetFields();
                                this.editFormVisible = false;
                                this.getUsers();
                            });
                        });
                    }
                });
            },
            //新增
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            //NProgress.start();
                            let para = Object.assign({}, this.addForm);
                            api.addUser(para).then((res) => {
                                this.addLoading = false;
                                common.handleReturn(res,
                                    (res) => {
                                        this.addFormVisible = false;
                                        this.$refs['addForm'].resetFields();
                                        common.toastMsg('用户添加成功', 'success');

                                        this.getUsers();
                                    })
                            }).catch((error) => {
                                this.addLoading = false;
                                common.handleNWException(error)
                            });
                        });
                    }
                });
            },
            selsChange: function (sels) {
                this.sels = sels;
            },
            //批量删除
            batchRemove: function () {
                var ids = this.sels.map(item => item.id).toString();
                this.$confirm('确认删除选中记录吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                    let para = {ids: ids};
                    batchRemoveUser(para).then((res) => {
                        this.listLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getUsers();
                    });
                }).catch(() => {

                });
            }
        },
        mounted() {
            this.getUsers();
            this.myRole = SESSION.getUserRole();
        }
    }

</script>

<style scoped>

</style>