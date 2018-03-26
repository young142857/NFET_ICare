/**
 * Created by chenmk on 2017/8/30.
 */
$(function() {
    var deviceNo = sessionStorage.getItem("deviceNo");
    var name = sessionStorage.getItem("name");
    $("#name").text(name);
    $("#deviceNo").text(deviceNo);
    ajax();
    function ajax() {
        $.ajax({
            url: "/mgr/deviceWarranties",
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
            $('#table_warranty').dataTable({
                //使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
                //data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
                pageLength: 12,
                "bSort": false, //是否支持排序功能
                "bFilter": true, //搜索栏
                "bLengthChange": false,//每页显示的记录数
//	      searching: left,
                "aoColumns": [
                    {"data": "num", "sTitle": "序号", "bSortable": false},//序号
                    {"data": "warrantyNo", "sTitle": "延保单号", "bSortable": false},//延保单号
                    {"data": "warrantyPkgNo.warrantyPkgContent", "sTitle": "延保套餐", "bSortable": false},//延保套餐
                    {"data": "payAmt", "sTitle": "延保金额", "bSortable": false},//延保金额
                    {"data": "warrantyDate", "sTitle": "延保期", "bSortable": false},//延保期
                    {"data": "warrantyPeriod", "sTitle": "延保期限（月）", "bSortable": false},//延保期限
                    {
                        "data": "", "sTitle": "操作", "bSortable": false,
                        "mRender": function (data, type, funll) {
                            return '<a href="javascript:;"  class="tdlink">详情</a>';
                        }
                    }//操作详情
                ],
                "aaData": data
            });
        $("body").delegate('#table_warranty tr .tdlink', 'click', function () {
            f = 1;
        });
        $("body").delegate('#table_warranty tr', 'click', function () {
            var oTable = table.fnGetData(this);
            console.log(oTable);
            var warrantyNo = oTable.warrantyNo;
            var orderTimeStr = oTable.orderTimeStr;
            var warrantyPkgContent = oTable.warrantyPkgNo.warrantyPkgContent;
            var payAmt = oTable.payAmt;
            var warrantyDate = oTable.warrantyDate;
            var warrantyTo = oTable.warrantyTo;
            var warrantyPeriod = oTable.warrantyPeriod;
            if (f == 1) {
                $('#warrantymodal').show();
                $("#orderTimeStr").text(orderTimeStr);
                $("#warrantyNo").text(warrantyNo);
                $("#warrantyPkgContent").text(warrantyPkgContent);
                $('#payAmt').text(payAmt);
                $('#warrantyDate').text(warrantyDate);
                $('#warrantyTo').text(warrantyTo);
                $('#warrantyPeriod').text(warrantyPeriod);
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