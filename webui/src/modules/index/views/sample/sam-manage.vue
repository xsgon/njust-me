<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.testId" placeholder="样本编号"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" v-on:click="getSamples">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>



		<!--列表-->
		<el-table :data="samples" highlight-current-row stripe v-loading="listLoading" @selection-change="selsChange"
				  style="width: 100%;">
            <el-table-column type="selection" width="55">
            </el-table-column>

            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item label="样件编号">
                            <span>{{ props.row.sampleId }}</span>
                        </el-form-item>
                        <el-form-item label="实验编号">
                            <span>{{ props.row.testId }}</span>
                        </el-form-item>
                        <el-form-item label="厂商">
                            <span>{{ props.row.companyName }}</span>
                        </el-form-item>
                        <el-form-item label="型号">
                            <span>{{ props.row.modelName }}</span>
                        </el-form-item>
                        <el-form-item label="采样方法">
                            <span>{{ props.row.sampleWay }}</span>
                        </el-form-item>
                        <el-form-item label="螺纹长度">
                            <span>{{ props.row.threadLen }}</span>
                        </el-form-item>
                        <el-form-item label="总长度">
                            <span>{{ props.row.wholeLen }}</span>
                        </el-form-item>
                        <el-form-item label="公称直径">
                            <span>{{ props.row.d0 }}</span>
                        </el-form-item>
                        <el-form-item label="导程">
                            <span>{{ props.row.ph0 }}</span>
                        </el-form-item>
                        <el-form-item label="最小底径">
                            <span>{{ props.row.d1 }}</span>
                        </el-form-item>
                        <el-form-item label="钢球直径">
                            <span>{{ props.row.dw }}</span>
                        </el-form-item>
                        <el-form-item label="外径">
                            <span>{{ props.row.d2 }}</span>
                        </el-form-item>
                        <el-form-item label="反向结构数量">
                            <span>{{ props.row.ballCircle }}</span>
                        </el-form-item>
                        <el-form-item label="滚动体有效圈数">
                            <span>{{ props.row.ballList }}</span>
                        </el-form-item>
                        <el-form-item label="螺纹升角">
                            <span>{{ props.row.beta }}</span>
                        </el-form-item>
                        <el-form-item label="丝杠滚道设计半径">
                            <span>{{ props.row.r }}</span>
                        </el-form-item>
                        <el-form-item label="接触角">
                            <span>{{ props.row.alfa }}</span>
                        </el-form-item>
                        <el-form-item label="螺母体外径">
                            <span>{{ props.row.nutDg6 }}</span>
                        </el-form-item>
                        <el-form-item label="螺母法兰外径">
                            <span>{{ props.row.nutD1 }}</span>
                        </el-form-item>
                        <el-form-item label="螺母法兰厚度">
                            <span>{{ props.row.nutB }}</span>
                        </el-form-item>
                        <el-form-item label="螺母总长度">
                            <span>{{ props.row.nutL1 }}</span>
                        </el-form-item>
                        <el-form-item label="螺钉孔尺寸">
                            <span>{{ props.row.nutM }}</span>
                        </el-form-item>
                        <el-form-item label="额定动载荷">
                            <span>{{ props.row.ca }}</span>
                        </el-form-item>
                        <el-form-item label="额定静载荷">
                            <span>{{ props.row.coa }}</span>
                        </el-form-item>
                        <el-form-item label="标称刚性">
                            <span>{{ props.row.k }}</span>
                        </el-form-item>
                        <el-form-item label="DN值">
                            <span>{{ props.row.dn0 }}</span>
                        </el-form-item>
                        <el-form-item label="循环类型">
                            <span>{{ props.row.cycleModel }}</span>
                        </el-form-item>
                        <el-form-item label="螺母类型">
                            <span>{{ props.row.nutType }}</span>
                        </el-form-item>
                        <el-form-item label="预紧">
                            <span>{{ props.row.preload }}</span>
                        </el-form-item>
                        <el-form-item label="自润滑">
                            <span>{{ props.row.selfLub }}</span>
                        </el-form-item>
                        <el-form-item label="其它说明">
                            <span>{{ props.row.special }}</span>
                        </el-form-item>
                        <el-form-item label="采样日期" :formatter="timeFormatter">
                            <span>{{ props.row.sampleTime }}</span>
                        </el-form-item>
                        <el-form-item label="录入日期" :formatter="timeFormatter">
                            <span>{{ props.row.createTime }}</span>
                        </el-form-item>
                    </el-form>
                </template>
            </el-table-column>

            <el-table-column prop="sampleId" label="样件编号" width="60">
            </el-table-column>
            <el-table-column prop="testId" label="实验编号" width="60">
			</el-table-column>
			<el-table-column prop="sampleWay" label="采样方式" width="100">
			</el-table-column>
			<el-table-column prop="companyName" label="厂商" width="150" sortable>
			</el-table-column>
			<el-table-column prop="modelName" label="型号" width="100">
			</el-table-column>
			<el-table-column prop="sampleTime" label="采样日期" :formatter="timeFormatter" sortable width="150">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" :formatter="timeFormatter" sortable>
			</el-table-column>
			<el-table-column label="操作" width="250"  fixed="right">
				<template scope="scope">
					<el-button size="small" type="text" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
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
                samples: [],
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
            //时间转换
            timeFormatter: (row, column, cellValue, index) => {
                if (cellValue !== undefined) {
                    return new Date(cellValue).Format("yyyy-MM-dd hh:mm:ss");
                } else {
                    return "未知";
                }
            },

            handleCurrentChange(val) {
                this.page = val;
                this.getSamples();
            },
            handleSizeChange(val) {
                this.pageSize = val;
                console.log('每页 ${val} 条');
            },
            onRadioChange(val) {
                console.log('radio changed to ' + val);
            },
            //获取样件列表
            getSamples() {
                let para = {
                    page: this.page - 1,  // start from 0
                    //name: this.filters.name
                };
                this.listLoading = true;
                api.getSampleList(para)
                    .then((res) => {
                        common.handleReturn(res, (res) => {
                            this.total = res.data.total;
                            this.samples = res.data.body;
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
                        this.getSamples();
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
                                this.getSamples();
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

                                        this.getSamples();
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
                        this.getSamples();
                    });
                }).catch(() => {

                });
            }
        },
        mounted() {
            this.getSamples();
            this.myRole = SESSION.getUserRole();
        }
    }

</script>

<style scoped>
    .demo-table-expand {
        font-size: 0;
    }
    .demo-table-expand label {
        width: 90px;
        color: #99a9bf;
    }
    .demo-table-expand .el-form-item {
        margin-right: 0;
        margin-bottom: 0;
        width: 50%;
    }
</style>