<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input v-model="filters.sampleId" placeholder="样件编号"></el-input>
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
            <el-table-column prop="sampleId" label="样件编号" width="120" sortable>
            </el-table-column>
            <el-table-column prop="sampleTime" label="采样时间" min-width="180">
            </el-table-column>
            <el-table-column prop="sampleWay" label="采样方式" width="120">
            </el-table-column>
            <el-table-column prop="sampleCompany" label="公司名称" width="100">
            </el-table-column>
            <el-table-column prop="sampleMode" label="型号" width="100">
            </el-table-column>
            <el-table-column prop="threadLen" label="螺纹长度" width="100">
            </el-table-column>
            <el-table-column prop="wholeLen" label="总长度" width="100">
            </el-table-column>
            <el-table-column prop="d0" label="公称直径" width="100">
            </el-table-column>
            <el-table-column prop="ph0" label="导程" width="100">
            </el-table-column>
             <el-table-column prop="d1" label="最小底径" width="100">
             </el-table-column>
            <el-table-column prop="dw" label="钢球直径" width="100">
            </el-table-column>
            <el-table-column prop="d2" label="外径" width="100">
            </el-table-column>
             <el-table-column prop="ballCircle" label="反向结构数量" width="100">
             </el-table-column>
            <el-table-column prop="ballList" label="滚动体有效圈数" width="100">
            </el-table-column>
             <el-table-column prop="beta" label="螺纹升角" width="100">
             </el-table-column>
            <el-table-column prop="r" label="丝杆滚道设计半径" width="100">
            </el-table-column>
            <el-table-column prop="alfa" label="接触角" width="100">
            </el-table-column>
             <el-table-column prop="nutDg6" label="螺母体外径" width="100">
             </el-table-column>
            <el-table-column prop="nutD1" label="螺母法兰外径" width="100">
            </el-table-column>
             <el-table-column prop="nutB" label="螺母法兰厚度" width="100">
             </el-table-column>
            <el-table-column prop="nutL1" label="螺母总长度" width="100">
            </el-table-column>
            <el-table-column prop="nutM" label="螺钉孔尺寸" width="100">
            </el-table-column>
             <el-table-column prop="ca" label="额定动载荷" width="100">
             </el-table-column>
            <el-table-column prop="coa" label="额定静载荷" width="100">
            </el-table-column>
            <el-table-column prop="k" label="标称刚性" width="100">
            </el-table-column>
             <el-table-column prop="dn0" label="DN值" width="100">
             </el-table-column>
            <el-table-column prop="cycleModel" label="循环类型" width="100">
            </el-table-column>
            <el-table-column prop="netType" label="螺母类型" width="100">
            </el-table-column>
             <el-table-column prop="preLoad" label="预紧等级" width="100">
             </el-table-column>
            <el-table-column prop="selfLub" label="是否自润滑" width="100">
            </el-table-column>
            <el-table-column prop="spclDesc" label="特殊说明" min-width="180">
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
                <el-form-item label="样件编码" prop="sampleId">
                    <el-input v-model="editForm.platId" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="采样时间">
                    <el-date-picker type="date" placeholder="选择日期" v-model="editForm.sampleTime"></el-date-picker>
                </el-form-item>
                <el-form-item label="采样方式">
                    <el-input v-model="editForm.sampleWay" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="公司名称">
                    <el-input v-model="editForm.sampleCompany" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="型号">
                     <el-input v-model="editForm.sampleMode" auto-complete="off"></el-input>
                 </el-form-item>
                 <el-form-item label="螺纹长度">
                    <el-input v-model="editForm.threadLen" auto-complete="off"></el-input>
                 </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
            </div>
        </el-dialog>

        <!--新增界面-->
        <el-dialog title="添加设备" :visible.sync="addFormVisible" :close-on-click-modal="false">
            <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
                <el-form-item label="样件编码" prop="sampleId">
                    <el-input v-model="editForm.platId" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="采样时间">
                    <el-date-picker type="date" placeholder="选择日期" v-model="editForm.sampleTime"></el-date-picker>
                </el-form-item>
                <el-form-item label="采样方式">
                    <el-input v-model="editForm.sampleWay" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="公司名称">
                    <el-input v-model="editForm.sampleCompany" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="型号">
                     <el-input v-model="editForm.sampleMode" auto-complete="off"></el-input>
                 </el-form-item>
                 <el-form-item label="螺纹长度">
                    <el-input v-model="editForm.threadLen" auto-complete="off"></el-input>
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
                filters: {
                    platId: ''
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
                    sampleId:'',
                    sampleTime:'',
                    sampleWay:'',
                    sampleCompany:'',
                    sampleMode:'',
                    threadLen:''
                },

                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    name: [
                        {required: true, message: '请输入设备名称', trigger: 'blur'}
                    ]
                },
                //新增界面数据
                addForm: {
                    sampleId:'',
                    sampleTime:'',
                    sampleWay:'',
                    sampleCompany:'',
                    sampleMode:'',
                    threadLen:''
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
            //获取列表
            getMainTest() {
                let para = {
                    page: this.page,
                    testId: this.filters.testId
                };
                this.listLoading = true;
                //NProgress.start();
                api.getMainTest(para)
                    .then((res) => {
                        this.total = res.data.length;
                        this.mainTest = res.data;
                        this.listLoading = false;
                        //NProgress.done();
                    })
                    .catch((error) => {

                    });
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                    let para = {id: row.sampleId};
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
                    sampleId:'',
                    sampleTime:'',
                    sampleWay:'',
                    sampleCompany:'',
                    sampleMode:'',
                    threadLen:''
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
                            para.platStartDate = (!para.platStartDate || para.platStartDate == '') ? '' : util.formatDate.format(new Date(para.platStartDate), 'yyyy-MM-dd');
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
                            para.platStartDate = (!para.platStartDate || para.platStartDate == '') ? '' : util.formatDate.format(new Date(para.platStartDate), 'yyyy-MM-dd');

                            addUser(para).then((res) => {
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
                var ids = this.sels.map(item => item.sampleId).toString();
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
            }
        },
        mounted() {
            this.getMainTest();
        }
    }

</script>

<style scoped>

</style>