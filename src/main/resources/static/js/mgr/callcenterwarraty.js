/**
 * Created by chenmk on 2017/8/31.
 */
$(function() {
    ajax();
    function ajax() {
        $.ajax({
            url: "/mgr/allWarrantyOrder",
            type: "POST",
            dataType: "json",
            success: function (data) {
                console.log(data);
                setTable(data);
            }
        });
    }
    function setTable(data) {
    	var f = 0;
        table =
            $('#table_warraty').dataTable({
                //使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
                //data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
                pageLength: 12,
                "bSort": false, //是否支持排序功能
                "bFilter": true, //搜索栏
                "bLengthChange": false,//每页显示的记录数
//	      searching: left,
                "aoColumns": [
                    {"data": "num", "sTitle": "序号", "bSortable": false},//序号
                    {"data": "warrantyNo", "sTitle": "延保单号", "bSortable": false},//延保套餐
                    {"data": "deviceNo.deviceNo", "sTitle": "机器编码", "bSortable": false},//机器编码
                    {"data": "userNo.name", "sTitle": "延保人", "bSortable": false},//延保人
                    {"data": "userNo.phone", "sTitle": "延保人电话", "bSortable": false},//延保人电话
                    /*{"data": "industry", "sTitle": "所属行业", "bSortable": false},//所属行业
*/                    {"data": "warrantyDate", "sTitle": "延保期", "bSortable": false},//延保期
                    {"data": "warrantyPkgNo.warrantyPkgContent", "sTitle": "延保套餐", "bSortable": false},//延保套餐
                    {"data": "payAmt", "sTitle": "延保金额（元）", "bSortable": false},//延保金额
                    {
                        "data": "", "sTitle": "操作", "bSortable": false,
                        "mRender": function (data, type, funll) {
                            return '<a href="javascript:;"  class="tdlink">详情</a>';
                        }
                    }//操作详情
                ],
                "aaData": data
            });
        $("body").delegate('#table_warraty tr .tdlink', 'click', function () {
            f = 1;
        });
        $("body").delegate('#table_warraty tr', 'click', function () {
            var oTable = table.fnGetData(this);
            console.log(oTable);
            var warrantyNo = oTable.warrantyNo;
            var orderTimeStr = oTable.orderTimeStr;
            var userNoName = oTable.userNo.name;
            var phone = oTable.userNo.phone;
            var companyName = oTable.companyName;
            var industry = oTable.industry;
            var warrantyDate = oTable.warrantyDate;
            var warrantyTo = oTable.warrantyTo;
            var deviceNo = oTable.deviceNo.deviceNo;
            var warrantyPeriod = oTable.warrantyPeriod;
            var warrantyPkgNo = oTable.warrantyPkgNo.warrantyPkgContent;
            var payAmt = oTable.payAmt;
            if (f == 1) {
                $('#warrantydata').show();
                $("#warrantyNo").text(warrantyNo);
                $("#orderTimeStr").text(orderTimeStr);
                $('#userName').text(userNoName);
                $('#phone').text(phone);
                $('#deviceNo').text(deviceNo);
                $('#companyName').text(companyName);
                $('#industry').text(industry);
                $('#warrantyPeriod').text(warrantyPeriod);
                $('#warrantyDate').text(warrantyDate);
                $('#warrantyTo').text(warrantyTo);
                $('#warrantyPkgNo').text(warrantyPkgNo);
                $('#payAmt').text(payAmt);
            }
            f = 0;
        });
    }
});