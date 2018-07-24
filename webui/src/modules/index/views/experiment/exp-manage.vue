<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input v-model="filters.name" placeholder="实验编号"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="handleAdd">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="mainTest" highlight-current-row v-loading="listLoading" @selection-change="selsChange"
                  style="width: 100%;">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column type="index" width="60">
            </el-table-column>
            <el-table-column prop="testId" label="实验编号" width="120" sortable>
            </el-table-column>
            <el-table-column prop="testObj" label="实验目的" width="100">
            </el-table-column>
            <el-table-column prop="sampNum" label="样件数量" width="100">
            </el-table-column>
            <el-table-column prop="sampIds" label="样件编号" width="120">
            </el-table-column>
            <el-table-column prop="testStart" label="开始日期" min-width="180">
            </el-table-column>
            <el-table-column prop="testEnd" label="结束日期" min-width="180">
            </el-table-column>
            <el-table-column prop="testEqp" label="实验设备" min-width="180">
            </el-table-column>
            <el-table-column prop="testPpl" label="实验人员" min-width="180">
            </el-table-column>
            <el-table-column prop="testLoc" label="实验地点" min-width="180">
            </el-table-column>
            <el-table-column prop="testConcl" label="实验结论" min-width="180">
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
                <template scope="scope">
                    <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--工具条-->
        <el-col :span="24" class="toolbar">
            <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
            <!--<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">-->
            <!--</el-pagination>-->
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
                <el-form-item label="实验编号" prop="testId">
                    <el-input v-model="editForm.testId" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="实验目的">
                     <el-input v-model="editForm.testId" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="样件数量">
                    <el-input-number v-model="editForm.sampNum" :min="0" :max="200"></el-input-number>
                </el-form-item>
                <el-form-item label="样件编号">
                       <el-select
                                    v-model="editForm.sampIds"
                                    multiple
                                    filterable
                                    remote
                                    reserve-keyword
                                    placeholder="请输入样件编码"
                                    :remote-method="remoteMethod"
                                    :loading="loading">
                                    <el-option
                                      v-for="item in options4"
                                      :key="item.value"
                                      :label="item.label"
                                      :value="item.value">
                                    </el-option>
                        </el-select>
                 </el-form-item>
                <el-form-item label="开始日期">
                    <el-date-picker type="date" placeholder="选择日期" v-model="editForm.testStart"></el-date-picker>
                </el-form-item>
                <el-form-item label="结束日期">
                    <el-date-picker type="date" placeholder="选择日期" v-model="editForm.endStart"></el-date-picker>
                </el-form-item>
                <el-form-item label="实验设备">
                    <el-input v-model="editForm.testEqp" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="实验人员">
                    <el-input v-model="editForm.testPpl" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="实验地点">
                    <el-input v-model="editForm.testLoc" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="实验结论">
                    <el-input v-model="editForm.testConcl" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
            </div>
        </el-dialog>

        <!--新增界面-->
        <el-dialog title="添加实验大纲" :visible.sync="addFormVisible" :close-on-click-modal="false">
            <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
                <el-form-item label="实验编号" prop="testId">
                                    <el-input v-model="editForm.testId" auto-complete="off"></el-input>
                                </el-form-item>
                                <el-form-item label="实验目的">
                                     <el-input v-model="editForm.testObj" auto-complete="off"></el-input>
                                </el-form-item>
                                <el-form-item label="样件编号">
                                <el-select
                                                                                    v-model="editForm.sampleList"
                                                                                    multiple
                                                                                    filterable
                                                                                    remote
                                                                                    reserve-keyword
                                                                                    placeholder="请输入样件编码"
                                                                                    :remote-method="remoteMethod"
                                                                                    :loading="loading">
                                                                                    <el-option
                                                                                      v-for="item in options4"
                                                                                      :key="item.value"
                                                                                      :label="item.label"
                                                                                      :value="item.value">
                                                                                    </el-option>
                                </el-select>
                                 </el-form-item>
                                <el-form-item label="开始日期">
                                    <el-date-picker type="date" placeholder="选择日期" v-model="editForm.testStart"></el-date-picker>
                                </el-form-item>
                                <el-form-item label="结束日期">
                                    <el-date-picker type="date" placeholder="选择日期" v-model="editForm.testEnd"></el-date-picker>
                                </el-form-item>
                                <el-form-item label="实验设备">
                                    <el-input v-model="editForm.platformList" auto-complete="off"></el-input>
                                </el-form-item>
                                <el-form-item label="实验人员">
                                    <el-input v-model="editForm.testPeopleList" auto-complete="off"></el-input>
                                </el-form-item>
                                <el-form-item label="实验地点">
                                    <el-input v-model="editForm.testLocation" auto-complete="off"></el-input>
                                </el-form-item>
                                <el-form-item label="实验结论">
                                    <el-input v-model="editForm.testAttachment" auto-complete="off"></el-input>
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
    import api from 'api/api';
    import util from "../../../../common/js/util";


    export default {
        data() {
            return {
                options4: [],
                sampleIds: [],
                list: [],
                        loading: false,
                        states: ["YZ001", "LS001", "LS002",
                        "LS003"],
                filters: {
                    testObj: ''
                },
                mainTest: [],
                total: 0,
                page: 1,
                pageSize: 100,
                listLoading: false,
                sels: [],//列表选中列

                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    name: [
                        {required: true, message: '请输入实验目标', trigger: 'blur'}
                    ]
                },
                //编辑界面数据
                editForm: {
                      testId:'',
                      testObj:'',
                      sampleList:'',
                      testStart:'',
                      testEnd:'',
                      testLocation:'',
                      testAttachment:'',
                      testPeopleList:'',
                      platformList:''
                },

                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    name: [
                        {required: true, message: '请输入实验目的', trigger: 'blur'}
                    ]
                },
                //新增界面数据
                addForm: {
                    testId:'',
                    testObj:'',
                    sampleList:'',
                    testStart:'',
                    testEnd:'',
                    testLocation:'',
                    testAttachment:'',
                    testPeopleList:'',
                    platformList:''
                }




            }
        },
        methods: {
            handleCurrentChange(val) {
                this.page = val;
                this.getMainTest();
            },
            handleSizeChange(val) {
                this.pageSize = val;
                console.log(`每页 ${val} 条`);
            },
            //获取用户列表
            getMainTest() {
                let para = {
                    page: this.page,
                    testId: this.filters.testId
                };
                this.listLoading = true;
                //NProgress.start();
                api.getMainTest(para)
                    .then((res) => {
                        this.total = res.data.total;
                        this.mainTest = res.data.body;
                        this.listLoading = false;
                        //NProgress.done();
                    })
                    .catch((error) => {
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
                    let para = {id: row.testId};
                    removeUser(para).then((res) => {
                        this.listLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getMainTest();
                    });
                }).catch(() => {

                });
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
                     testId:'',
                     testObj:'',
                     platformList:'',
                     testStart:'',
                     testEnd:'',
                     testPeopleList:'',
                     testLocation:'',
                     testAttachment:'',
                     sampleList:''
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
                            para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
                            editUser(para).then((res) => {
                                this.editLoading = false;
                                //NProgress.done();
                                this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                this.$refs['editForm'].resetFields();
                                this.editFormVisible = false;
                                this.getMainTest();
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
                            para.testStart = (!para.testStart || para.testStart == '') ? '' : util.formatDate.format(new Date(para.testStart), 'yyyy-MM-dd');
                            para.testEnd = (!para.testEnd || para.testEnd == '') ? '' : util.formatDate.format(new Date(para.testEnd), 'yyyy-MM-dd');

                            api.addExp(para).then((res) => {
                                this.addLoading = false;
                                //NProgress.done();
                                this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                this.$refs['addForm'].resetFields();
                                this.addFormVisible = false;
                                this.getMainTest();
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
                var ids = this.sels.map(item => item.testId).toString();
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
                        this.getMainTest();
                    });
                }).catch(() => {

                });
            },

                        remoteMethod(query) {
                                if (query !== '') {
                                  this.loading = true;
                                  setTimeout(() => {
                                    this.loading = false;
                                    this.options4 = this.list.filter(item => {
                                      return item.label.toLowerCase()
                                        .indexOf(query.toLowerCase()) > -1;
                                    });
                                  }, 200);
                                } else {
                                  this.options4 = [];
                                }
                              }
        },
        mounted() {
            this.getMainTest();
            this.list = this.states.map(item => {
                                return { value: item, label: item };
                              })
        }
    }

</script>

<style scoped>

</style>