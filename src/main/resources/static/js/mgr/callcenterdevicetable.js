/**
 * Created by chenmk on 2017/8/30.
 */
$(function() {
    var userNo = sessionStorage.getItem("userNo");
    var name = sessionStorage.getItem("name");
    var company_name = sessionStorage.getItem("company_name");
    var phoneNum = sessionStorage.getItem("phoneNum");
    $("#name").text(name);
    $("#company").text(company_name);
    $("#phoneNum").text(phoneNum);
    ajax();
    function ajax() {
        $.ajax({
            url: "/mgr/userDevices",
            type: "POST",
            dataType: "json",
            data: {"userNo": userNo},
            success: function (data) {
            	console.log(data+ ' 2222')
            	setTable(data)
            }
        });

    }
    function setTable(data) {
        var f = 0;
        table =
            $('#table_device').dataTable({
                //使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
                //data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
                pageLength: 12,
                "bSort": false, //是否支持排序功能
                "bFilter": true, //搜索栏
                "bLengthChange": false,//每页显示的记录数
//	      searching: left,
                "aoColumns": [
                    {"data": "num", "sTitle": "序号", "bSortable": false},//序号
                    {"data": "deviceNo.name", "sTitle": "产品", "bSortable": false},//产品
                    {"data": "deviceNo.type", "sTitle": "产品系列", "bSortable": false},//产品系列
                    {"data": "deviceNo.deviceNo", "sTitle": "机器编码", "bSortable": false},//机器编码
                    {"data": "statusStr", "sTitle": "保修状态", "bSortable": false},//保修状态
                    {"data": "ecardNo", "sTitle": "保修卡号", "bSortable": false},//保修卡号
                    {"data": "warrantDate", "sTitle": "保修期", "bSortable": false},//保修期
                    {
                        "data": "", "sTitle": "报修记录", "bSortable": false,
                        "mRender": function (data, type, funll) {
                            return '<a href="javascript:;"  class="repairlink">查看报修记录</a>';
                        }
                    },//报修记录
                    {
                        "data": "", "sTitle": "延保记录", "bSortable": false,
                        "mRender": function (data, type, funll) {
                            return '<a href="javascript:;"  class="warratylink">查看延保记录</a>';
                        }
                    },//延保记录
                    {
                        "data": "", "sTitle": "操作", "bSortable": false,
                        "mRender": function (data, type, funll) {
                            return '<a href="javascript:;"  class="tdlink">详情</a>';
                        }
                    }//操作详情
                ],
                "aaData": data
            });
        $("body").delegate('#table_device tr .tdlink', 'click', function () {
            f = 1;
        });
        $("body").delegate('#table_device tr .repairlink', 'click', function () {
            f = 2;
        });
        $("body").delegate('#table_device tr .warratylink', 'click', function () {
            f = 3;
        });
        $("body").delegate('#table_device tr', 'click', function () {
            var oTable = table.fnGetData(this);
            console.log(oTable);
            var device = oTable.deviceNo.name;
            var type = oTable.deviceNo.type;
            var deviceNo = oTable.deviceNo.deviceNo;
            var mfgDate = oTable.deviceNo.mfgDate;
            var bindTime = oTable.bindTime;
            var statusStr = oTable.statusStr;
            var ecardNo = oTable.ecardNo;
            var warrantDate = oTable.warrantDate;
            var warrantTo = oTable.warrantTo;
            if (f == 1) {
                $('#devicemodal').show();
                $("#device").text(device);
                $("#type").text(type);
                $('#deviceNo').text(deviceNo);
                $('#mfgDate').text(mfgDate);
                $('#bindTime').text(bindTime);
                $('#statusStr').text(statusStr);
                $('#ecardNo').text(ecardNo);
                $('#warrantDate').text(warrantDate);
                $('#warrantTo').text(warrantTo);
            }
            if (f == 2) {
                sessionStorage.setItem('deviceNo', deviceNo);
                window.open("/page/callcenterrepairdata", "_self")
            }
            if (f == 3) {
                sessionStorage.setItem('deviceNo', deviceNo);
                window.open("/page/callcenterwarratydata", "_self")
            }
            f = 0;
        });
    }
});

//点击模态框关闭按钮，模态框隐藏消失
$(".sp_second").click(function () {
    $(".modalbox").hide();
});

//保存按钮处理
$(".keepbtn").click(function () {
    $(".modalbox").hide();
});