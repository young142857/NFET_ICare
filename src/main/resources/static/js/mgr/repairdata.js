/**
 * Created by xuebei on 2017/8/30.
 */
$(function() {
    var deviceNo = sessionStorage.getItem("deviceNo");
    var name = sessionStorage.getItem("name");
    $("#name").text(name);
    $("#deviceNo").text(deviceNo);
    ajax();
    function ajax() {
        $.ajax({
            url: "/mgr/deviceFixes",
            type: "POST",
            dataType: "json",
            data: {"deviceNo": deviceNo},
            success: function (data) {
                console.log(data);
                setTable(data);
            }
        });

    }
    function setTable(data) {
        var f = 0;
        table =
            $('#table_repair').dataTable({
                //使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
                //data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
                pageLength: 8,
                "bSort": false, //是否支持排序功能
                "bFilter": true, //搜索栏
                "bLengthChange": false,//每页显示的记录数
//	      searching: left,
                "aoColumns": [
                    {"data": "num","width": "5%", "sTitle": "序号", "bSortable": false},//序号
                    {"data": "fixId", "sTitle": "报修单号", "bSortable": false},//报修单号
                    {"data": "faultName", "sTitle": "故障类型", "bSortable": false},//故障类型
                    {"data": "fixDesc","width": "25%", "sTitle": "故障描述", "bSortable": false},//故障描述
                    {"data": "fixPatternStr", "sTitle": "维修方式", "bSortable": false},//维修方式
                    {"data": "address","width": "20%", "sTitle": "维修地址", "bSortable": false},//维修地址
                    {"data": "fixScheduleStr", "sTitle": "报修进度", "bSortable": false},//报修进度
                    {"data": "evaluate","width": "10%", "sTitle": "评价（星）", "bSortable": false},//评价
                    {
                        "data": "", "sTitle": "操作", "bSortable": false,
                        "mRender": function (data, type, funll) {
                            return '<a href="javascript:;"  class="tdlink">详情</a>';
                        }
                    }//操作详情
                ],
                "aaData": data
            });
        $("body").delegate('#table_repair tr .tdlink', 'click', function () {
            f = 1;
        });
        $("body").delegate('#table_repair tr', 'click', function () {
            var oTable = table.fnGetData(this);
            console.log(oTable);
            var fixId = oTable.fixId;
            var faultName = oTable.faultName;
            var fixDesc = oTable.fixDesc;
            var fixPatternStr = oTable.fixPatternStr;
            var address = oTable.address;
            var fixScheduleStr = oTable.fixScheduleStr;
            var evaluate = oTable.evaluate;
            var orderTimeStr = oTable.orderTimeStr;
            if (f == 1) {
                $('#repairmodal').show();
                $("#fixId").text(fixId);
                $("#faultName").text(faultName);
                $('#fixDesc').text(fixDesc);
                $('#fixPatternStr').text(fixPatternStr);
                $('#address').text(address);
                $('#fixScheduleStr').text(fixScheduleStr);
                $('#evaluate').text(evaluate);
                $('#orderTimeStr').text(orderTimeStr);
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