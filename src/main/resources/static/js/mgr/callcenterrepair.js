/**
 * Created by chemmk on 2017/8/31.
 */
$(function() {
    ajax();
    function ajax() {
        $.ajax({
            url: "/mgr/allFixOrder",
            type: "POST",
            dataType: "json",
            success: function (data) {
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
                pageLength: 6,
                "bSort": false, //是否支持排序功能
                "bFilter": true, //搜索栏
                "bLengthChange": false,//每页显示的记录数
//	      searching: left,
                "aoColumns": [
                    {"data": "num","width": "5%", "sTitle": "序号", "bSortable": false},//序号
                    {"data": "fixId", "sTitle": "报修单号", "bSortable": false},//报修单号
                    {"data": "deviceNo.deviceNo", "sTitle": "机器编码", "bSortable": false},//机器编码
                    {"data": "userName","width": "6%", "sTitle": "报修人", "bSortable": false},//报修人
                    {"data": "userPhone","width": "7%", "sTitle": "报修人电话", "bSortable": false},//报修人电话
                   /* {"data": "companyIndustry","width": "8%", "sTitle": "所属行业", "bSortable": false},//所属行业
*/                  {"data": "faultName","width": "8%", "sTitle": "故障类型", "bSortable": false},//故障类型
                    {"data": "fixDesc","width": "15%", "sTitle": "故障描述", "bSortable": false},//故障描述
                    {"data": "fixPatternStr","width": "8%", "sTitle": "维修方式", "bSortable": false},//维修方式
                    {"data": "address","width": "12%", "sTitle": "维修地址", "bSortable": false},//维修地址
                    {"data": "fixScheduleStr","width": "8%", "sTitle": "报修进度", "bSortable": false},//报修进度
                    /*{"data": "", "sTitle": "受理部门", "bSortable": false}//受理部门*/
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
            var orderTimeStr = oTable.orderTimeStr;
			var deviceNo = oTable.deviceNo.deviceNo;
            var userName = oTable.userName;
            var userPhone = oTable.userPhone;
            var companyName = oTable.companyName;
            var companyIndustry = oTable.companyIndustry;
            var faultName = oTable.faultName;
            var fixDesc = oTable.fixDesc;
            var fixPatternStr = oTable.fixPatternStr;
            var fixScheduleStr = oTable.fixScheduleStr;
            var address = oTable.address;
            if (f == 1) {
                $('.modalbox').show();
                $("#fixId").text(fixId);
                $("#orderTimeStr").text(orderTimeStr);
				$("#deviceNo").text(deviceNo);
                $('#userName').text(userName);
                $('#userPhone').text(userPhone);
                $('#companyName').text(companyName);
                $('#companyIndustry').text(companyIndustry);
                $('#faultName').text(faultName);
                $('#fixDesc').text(fixDesc);
                $('#fixPatternStr').text(fixPatternStr);
                $('#address').text(address);
                $('#fixScheduleStr').text(fixScheduleStr);
            }
            f = 0;
        });
        	
    }
});