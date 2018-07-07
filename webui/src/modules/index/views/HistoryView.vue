<template>
    <section>

        <el-col :span="24" class="toolbar" style="padding-bottom: 12px;">
            <div style="display: inline-block">
                <span class="demonstration">请选择日期：</span>
                <el-date-picker v-model="alertDateRange" type="daterange" align="right" unlink-panels
                                size="mini"
                                range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
                                :default-time="['00:00:00', '23:59:59']"
                                :picker-options="pickerOptions">
                </el-date-picker>
            </div>

            <el-button @click="onOkPressed" size="mini" type="primary">确定</el-button>

            <el-button @click="onClickBatchDelete" v-if="expertMode" icon="el-icon-delete" size="mini">批量删除
            </el-button>

            <div style="float:right;display: inline-block">
                <el-dropdown size="mini" trigger="click" @command="onSmsDropdownClick">
                    <el-button size="mini">
                        {{DROPDOWN_COMMANDS_MAP[selectedSmsStatus].txt}}<i
                            class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item size="mini" command="cmdSmsAll">{{DROPDOWN_COMMANDS_MAP['cmdSmsAll'].txt}}
                        </el-dropdown-item>
                        <el-dropdown-item size="mini" command="cmdSmsSent" divided>
                            {{DROPDOWN_COMMANDS_MAP['cmdSmsSent'].txt}}
                        </el-dropdown-item>
                        <el-dropdown-item size="mini" command="cmdSmsNotSent" divided>
                            {{DROPDOWN_COMMANDS_MAP['cmdSmsNotSent'].txt}}
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>

                <el-dropdown size="mini" trigger="click" @command="onAlertDropdownClick">
                    <el-button size="mini">
                        {{DROPDOWN_COMMANDS_MAP[selectedAlertType].txt}}<i
                            class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item size="mini" command="cmdAlertAll">{{DROPDOWN_COMMANDS_MAP['cmdAlertAll'].txt}}
                        </el-dropdown-item>
                        <el-dropdown-item size="mini" command="cmdAlertAuto" divided>
                            {{DROPDOWN_COMMANDS_MAP['cmdAlertAuto'].txt}}
                        </el-dropdown-item>
                        <el-dropdown-item size="mini" command="cmdAlertReal" divided>
                            {{DROPDOWN_COMMANDS_MAP['cmdAlertReal'].txt}}
                        </el-dropdown-item>
                        <el-dropdown-item size="mini" command="cmdAlertFake" divided>
                            {{DROPDOWN_COMMANDS_MAP['cmdAlertFake'].txt}}
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>

        </el-col>

        <div id="history-view">

            <div>
                <el-dialog title="报警" :visible.sync="alertFormVisible">
                    <el-form :model="alertForm" :rules="alertFormRules">
                        <el-form-item label="姓名" :label-width="formLabelWidth">
                            <el-input v-model="alertForm.name" auto-complete="off"></el-input>
                        </el-form-item>

                        <el-form-item label="手机号码" :label-width="formLabelWidth" prop="phone" required>
                            <el-input v-model="alertForm.phone" auto-complete="off"></el-input>
                        </el-form-item>

                        <el-form-item label="报警内容" :label-width="formLabelWidth" required>
                            <el-input v-model="alertForm.content" type="textarea" :autosize="{ minRows: 2, maxRows: 4}"
                                      :disabled="true"></el-input>
                        </el-form-item>

                        <el-form-item label="图片" :label-width="formLabelWidth">
                            <img class="bjjl-image" :src="alertForm.imgUrl" style="width:100%; height:auto">
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="alertFormVisible = false">取 消</el-button>
                        <el-button type="primary" @click="onManualAlertClick">确 定</el-button>
                    </div>
                </el-dialog>
            </div>

            <el-table :data="bjjlList" highlight-current-row v-loading="isDataLoading" :show-header="false"
                      @selection-change="onSelsChange" style="width: 100%;">
                <el-table-column type="selection" width="30" v-if="expertMode">
                </el-table-column>
                <!--<el-table-column type="index" width="60">-->
                <!--</el-table-column>-->
                <el-table-column label="图片" width="100" >
                    <template scope="scope">
                        <div style="float:left;">
                            <a :href="scope.row.url" target="_blank">
                                <img class="bjjl-image" :src="scope.row.url">
                            </a>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="内容" width="350">
                    <template scope="scope">
                        <div style="float:left;">
                            <ul style="list-style: none;padding-left: 0;">
                                <li>
                                    <span class="bjjl-status">
                                        【{{ALERT_STATUS_MAP[scope.row.alertStatus]}}】【{{SMS_STATUS_MAP[scope.row.smsStatus]}}】
                                    </span>
                                    {{scope.row.alertContent}}
                                </li>
                                <li></li>

                                <li v-if="scope.row.smsStatus === 1 && selectedSmsStatus === 'cmdSmsSent'">
                                    <div>执法信息：</div>
                                    <div style="font-weight: bold;">&nbsp;&nbsp;&nbsp;&nbsp;短信已通知，正在执法过程中</div>
                                    <div style="font-weight: bold;">&nbsp;&nbsp;&nbsp;&nbsp;{{scope.row.zfPhone}}</div>
                                    <span>【自动告警时间】</span>{{scope.row.alertTime}}&nbsp;【短信通知时间】{{scope.row.notifyTime}}&nbsp;【执法完成时间】{{scope.row.finishTime}}
                                </li>
                                <li v-else>【自动告警时间】{{scope.row.alertTime}}</li>
                            </ul>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="操作" >
                    <template scope="scope">
                        <div style="float:right;text-align:right;">
                            <div>
                                <div >
                                    <el-button size="small"
                                            @click="onClickFake(scope.row)">置为误报
                                    </el-button> &nbsp;
                                    <el-button size="small"
                                            @click="onClickActual(scope.row)">置为真实告警
                                    </el-button> &nbsp;
                                    <el-button size="small"
                                            @click="onClickAlert(scope.row)">
                                        发送报警短信
                                    </el-button>
                                    <button type="danger" size="small"
                                            v-if="expertMode"
                                            @click="onClickDelete(scope.row)">
                                        删除报警
                                    </button>
                                </div>
                            </div>
                        </div>
                    </template>
                </el-table-column>
            </el-table>

            <el-col :span="24" class="toolbar">
                <el-pagination name="pg"
                               style="float:right"
                               layout="prev, pager, next"
                               @current-change="onPageChange"
                               :page-size="pageSize"
                               :total="total">
                </el-pagination>
            </el-col>
        </div>


    </section>
</template>

<script>
    import {wJsonPost} from 'api/api';

    // Date function
    Date.prototype.format = function (format) {
        let o = {
            "M+": this.getMonth() + 1, // month
            "d+": this.getDate(), // day
            "h+": this.getHours(), // hour
            "m+": this.getMinutes(), // minute
            "s+": this.getSeconds(), // second
            "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
            "S": this.getMilliseconds()
            // millisecond
        };

        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
        }

        for (let k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;
    };

    function getGracefulDatetime(timestamp) {
        if (typeof(timestamp) === 'undefined') {
            return ''
        }

        if (null === timestamp || 'null' === timestamp || 'NULL' === timestamp || '' === timestamp) {
            return ''
        }

        try {
            return new Date(parseInt(timestamp)).format("yyyy-MM-dd hh:mm:ss");
        } catch (err) {
            return '';
        }
    }
    
    function getImgUrl(name) {
        return 'http://192.168.1.252:19080' + name; // test only
    }

    // 防止过于频繁地发送短信告警
    let send_sms_handler = {
        // public
        can_send_sms: function (cameraId) {
            if (typeof(cameraId) === 'undefined') {
                return true;
            }
            let obj = send_sms_handler.request[cameraId];
            return (typeof(obj) === 'undefined' || obj === null);
        },

        push_request: function (cameraId) {
            setTimeout(function () {
                send_sms_handler.request[cameraId] = null;
                delete(send_sms_handler.request[cameraId]);
            }, send_sms_handler.timeout);
            send_sms_handler.request[cameraId] = true;

        },

        // private
        request: {},  // cameraId:flag
        timeout: 5 * 1000,  // 5s内不能重复发送
    };

    // dropdown list map
    const DROPDOWN_COMMANDS_MAP = {
        'cmdSmsAll': {txt: '全部', sqlTag: ''},
        'cmdSmsSent': {txt: '已发送短信通知', sqlTag: '(b.SMS_SENT_FLAG = 1)'},
        'cmdSmsNotSent': {txt: '未发送短信通知', sqlTag: '(b.SMS_SENT_FLAG = 0)'},

        'cmdAlertAll': {txt: '全部', sqlTag: ''},
        'cmdAlertAuto': {txt: '自动告警', sqlTag: "(b.STATUS = 'AUTO')"},
        'cmdAlertReal': {
            txt: '真实告警',
            sqlTag: "(b.STATUS='RGBJ' or b.STATUS='SGBJ' or b.STATUS='ACTUAL' or b.STATUS='CONFIRM')"
        },
        'cmdAlertFake': {txt: '误报', sqlTag: "(b.STATUS='IGNORE' or b.STATUS='RGQX')"},
    };

    // alert status map
    const ALERT_STATUS_MAP = {
        CONFIRM: '真实告警', //'已确认',
        IGNORE: '误报',
        AUTO: '自动告警',
        RGBJ: '真实告警', //'人工报警',
        SGBJ: '真实告警', //'人工报警',
        ACTUAL: '真实告警',
        RGQX: '误报' //'人工排除错误报警'
    };

    // sms status map
    const SMS_STATUS_MAP = {
        1: '已发送短信',
        0: '未发送短信'
    };

    export default {
        data() {
            return {
                // main data
                bjjlList: [],
                pageList: [],
                currentPage: 1,
                pageSize: 25,
                total: 0,

                // expertmode flag
                expertMode: false,

                // request param
                reqParams: {
                    action: 'read/HB_BJJL',
                    pattern: 3,
                    cameraCondition: '',
                    cond: "",
                    enforceCondition: '',
                    all: "NO",
                },

                // main ok button
                isDataLoading: false,

                // datepicker
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                alertDateRange: [],

                // dropdown list
                DROPDOWN_COMMANDS_MAP,
                selectedSmsStatus: 'cmdSmsAll',
                selectedAlertType: 'cmdAlertAll',

                // status map
                ALERT_STATUS_MAP,
                SMS_STATUS_MAP,

                // alert dialog
                alertFormVisible: false,
                alertForm: {
                    name: '',
                    phone: '',
                    content: '',
                    imgUrl: '',
                    cameraId: '',
                },
                formLabelWidth: '80px',
                alertFormRules: {
                    phone: [{required: true, pattern: /^[0-9, ]{1,}$/, message: '请输入正确的手机号', trigger: 'blur'},]
                }

            };
        },

        created: function () {
            // this.basicInfo = this.fetchBasicInfo()
        },

        mounted: function () {
            // init
            this.init();

            // // fetch remote data
            this.fetchData();

            console.log('historyview mounted')
        },

        updated: function () {
            // this happenes after html
            console.log('historyview updated')
            this.addZoomOnImage();
        },

        methods: {
            // ui init
            init: function () {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                this.alertDateRange = [start, end];
            },

            // fetch data from server
            fetchData: function (paramsChanged = true) {

                if (paramsChanged) this.combineReqParams();

                this.isDataLoading = true;
                let url = '/ndc/do.shtml';
                // let url = 'http://localhost:8080/ndc/do.shtml';  // test url
                wJsonPost(url, {param: JSON.stringify(this.reqParams), page: this.currentPage, pageSize: this.pageSize})
                    .then((data) => {
                        // handle the received data here
                        /*
                        data format:
                        {Total, action, body, msg, page, status}
                         */
                        this.bjjlList = [];
                        this.total = 0;
                        if (data.status === 200) {
                            this.total = data.Total;
                            for (let i = 0; i < data.body.length; i++) {
                                let item = data.body[i];
                                let newItem = {};

                                newItem.url = getImgUrl(item.BZZP);
                                newItem.zoom = (item.ZOOM_ZP !== "null");
                                if (newItem.zoom) {
                                    newItem.zoomUrl = getImgUrl(item.ZOOM_ZP);
                                }
                                newItem.id = item.ID;
                                newItem.cameraId = item.CAMERA_ID;
                                newItem.alertStatus = item.STATUS;
                                newItem.smsStatus = item.SMS_SENT_FLAG;
                                newItem.alertContent = item.BJNR;
                                newItem.zfName = item.NAME;
                                newItem.zfPhone = item.SJ;
                                newItem.notifyTime = getGracefulDatetime(item.NOTIFY_TIME);
                                newItem.alertTime = getGracefulDatetime(item.CDATE);
                                newItem.finishTime = getGracefulDatetime(item.FINISH_TIME);
                                newItem.checked = false;
                                // newItem.alertTime = new Date(parseInt(item.CDATE)).toLocaleDateString();
                                this.bjjlList.push(newItem);
                            }
                        }

                        this.isDataLoading = false;
                        return 'success';

                    })
                    .catch((jqXHR, textStatus, errorThrown) => {
                        // handle exception here
                        this.onResponseException(jqXHR, textStatus, errorThrown);

                        this.isDataLoading = false;
                        return 'failed';
                    });

            },

            updateAlertStatus: function (item, statusValue) {
                let params = {
                    action: 'update/HB_BJJL',
                    ID: item.id,
                    STATUS: statusValue,
                };
                wJsonPost('./ndc/do.shtml', {param: JSON.stringify(params)})
                    .then((data) => {
                        if (data.status === 200) {
                            item.alertStatus = statusValue;
                            this.$message({
                                message: '更新成功',
                                type: 'success'
                            });
                        }
                    })
                    .catch((jqXHR, textStatus, errorThrown) => {
                        // handle exception here
                        this.onResponseException(jqXHR, textStatus, errorThrown);
                        this.$message.error('更新失败：' + jqXHR.statusText);
                    });
            },

            deleteItem: function (item) {
                let params = {
                    action: 'batch-delete/HB_BJJL',
                    rows: [{ID: item.id}],
                };

                this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    wJsonPost('./dc/do.shtml', {param: JSON.stringify(params)})
                        .then((data) => {
                            if (data.status === 200) {
                                let index = this.bjjlList.indexOf(item);
                                if (index > -1) {
                                    this.bjjlList.splice(index, 1);
                                }

                                this.$message({
                                    message: '删除成功',
                                    type: 'success'
                                });
                            }
                        })
                        .catch((jqXHR, textStatus, errorThrown) => {
                            // handle exception here
                            this.onResponseException(jqXHR, textStatus, errorThrown);
                            this.$message.error('删除失败：' + jqXHR.statusText);
                        });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },

            deleteBatchItems: function () {
                let rows = [];
                for (let i = 0; i < this.bjjlList.length; ++i) {
                    let v = this.bjjlList[i];
                    if (v.checked) {
                        rows.push({ID: v.id});
                    }
                }
                if (rows.length <= 0) return;

                let params = {
                    action: 'batch-delete/HB_BJJL',
                    rows: rows,
                };

                this.$confirm('此操作将永久删除' + rows.length + '条记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    wJsonPost('./dc/do.shtml', {param: JSON.stringify(params)})
                        .then((data) => {
                            if (data.status === 200) {
                                for (let i = this.bjjlList.length - 1; i >= 0; i--) {
                                    if (this.bjjlList[i].checked) {
                                        this.bjjlList.splice(i, 1)
                                    }
                                }

                                this.$message({
                                    message: '删除成功',
                                    type: 'success'
                                });
                            }


                        })
                        .catch((jqXHR, textStatus, errorThrown) => {
                            // handle exception here
                            this.onResponseException(jqXHR, textStatus, errorThrown);
                            this.$message.error('删除失败：' + jqXHR.statusText);
                        });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },

            combineReqParams: function () {
                // SMS condition
                let sql = ' ' + DROPDOWN_COMMANDS_MAP[this.selectedSmsStatus].sqlTag + ' ';
                sql += (DROPDOWN_COMMANDS_MAP[this.selectedSmsStatus].sqlTag === '' ? '' : ' and ');

                // alert condition
                sql += ' ' + DROPDOWN_COMMANDS_MAP[this.selectedAlertType].sqlTag + ' ';
                sql += (DROPDOWN_COMMANDS_MAP[this.selectedAlertType].sqlTag === '' ? '' : ' and ');

                // time condition
                this.reqParams['timeCondition'] =
                    ' (b.CDATE between '
                    + this.alertDateRange[0].getTime()
                    + ' and '
                    + this.alertDateRange[1].getTime() + ') ';

                // pattern type
                this.reqParams['pattern'] = (this.selectedSmsStatus === 'cmdSmsSent' ? 1 : 3);

                // expert mode
                this.reqParams['all'] = (this.expertMode ? 'YES' : 'NO');

                // final condition
                this.reqParams['cond'] = ' where ' + sql + this.reqParams['timeCondition']
            },

            addZoomOnImage: function () {
                // let _bjjl_images = $('#bjjl-table .bjjl-image');
                // if (typeof(_bjjl_images) !== 'undefined' && _bjjl_images !== null && _bjjl_images.length > 0) {
                //     for (let _i = 0; _i < _bjjl_images.length; _i++) {
                //         let _img_src = $(_bjjl_images[_i]).attr('src');
                //         utilMouseOverZoomImage.setTarget($(_bjjl_images[_i]), _img_src);
                //     }
                // }
            },

            onResponseException: function (jqXHR, textStatus, errorThrown) {
                // check if re-login required
                if (jqXHR.readyState === 4
                    && jqXHR.status === 200
                    && jqXHR.responseText.indexOf('window.location.href') !== -1) {
                    window.location.href = "./logout.jsp";
                    return;
                }

                // log
                console.log(jqXHR.responseText);
                console.log(jqXHR.status);
                console.log(jqXHR.readyState);
                console.log(jqXHR.statusText);

                console.log(textStatus);
                console.log(errorThrown);
            },

            onPageChange: function (currentPage) {
                this.currentPage = currentPage;
                console.log('you change the page to ' + currentPage);
                this.fetchData(false);
            },

            // 响应dropdown list 点击事件
            onSmsDropdownClick(command) {
                // this.$message('click on item ' + command);
                this.selectedSmsStatus = command;
                this.fetchData();
            },

            onAlertDropdownClick(command) {
                // this.$message('click on item ' + command);
                this.selectedAlertType = command;
                this.fetchData();
            },

            // 响应确定按钮
            onOkPressed() {
                this.fetchData();
            },

            // 响应条目上按钮点击
            onClickFake(item) {
                // this.$message('clicked ' + item);
                this.updateAlertStatus(item, 'IGNORE');
            },

            onClickActual(item) {
                // this.$message('clicked ' + item);
                this.updateAlertStatus(item, 'ACTUAL');
            },

            onClickDelete(item) {
                this.deleteItem(item);
            },

            onClickBatchDelete() {
                this.deleteBatchItems();
            },

            // 在条目中点击发送短信报警
            onClickAlert(item) {
                // this.$message('clicked ' + item);
                this.alertForm.imgUrl = item.url;
                this.alertForm.phone = item.zfPhone;
                this.alertForm.name = item.zfName;
                this.alertForm.content = item.alertContent;
                this.alertForm.cameraId = item.cameraId;
                this.alertForm.id = item.id;

                this.alertFormVisible = true;
            },

            // 在弹出框点击报警
            onManualAlertClick() {
                // check the phoneno
                let strRegex = /^[0-9, ]{1,}$/;
                let re = new RegExp(strRegex);
                if (!re.test(this.alertForm.phone)) {
                    this.$message.error("手机号码只能包含数字和英文输入法下的逗号");
                    return;
                }

                if (!send_sms_handler.can_send_sms(this.alertForm.cameraId)) {
                    this.$message.error("该摄像头发送短信告警过于频繁，请稍后再试");
                    return;
                }

                // record sms sent count
                send_sms_handler.push_request(this.alertForm.cameraId);

                // send out th request now
                let params = {
                    action: 'send-sms',
                    number: this.alertForm.phone,
                    name: this.alertForm.name,
                    bjnr: this.alertForm.content,
                    CAMERA_ID: this.alertForm.cameraId,
                    BJ_ID: this.alertForm.id
                };
                wJsonPost('./dc/do.shtml', {param: JSON.stringify(params)})
                    .then((data) => {
                        if (data.status === 200) {
                            for (let i = 0; i < this.bjjlList.length; ++i) {
                                if (this.bjjlList[i].id === this.alertForm.id) {
                                    this.bjjlList[i].smsStatus = 1;
                                }
                            }
                            this.alertFormVisible = false;
                            this.$message({
                                message: '短信发送成功',
                                type: 'success'
                            });
                        }
                    })
                    .catch((jqXHR, textStatus, errorThrown) => {
                        // handle exception here
                        this.onResponseException(jqXHR, textStatus, errorThrown);
                        this.$message.error('短信发送失败：' + jqXHR.statusText);
                    });
            },

            onSelsChange() {
                this.sels = sels;
            },

        }
    }

</script>

<style scoped lang="scss">
    @import '~scss_vars';

    .bjjl-image {
        width: 100px;
        width: 80px;
        margin-right: 10px;
    }

    /*.table {*/
        /*font-size: 13px;*/
    /*}*/

    #history .topbar-left {
        font-size: 12px;
    }

    #history-view > .content > .wrapper::-webkit-scrollbar {
        width: 6px;
        background: #ffeccd;
    }

    #history-view > .content > .wrapper::-webkit-scrollbar-thumb {
        width: 6px;
        background: #ffa67b;
    }
</style>