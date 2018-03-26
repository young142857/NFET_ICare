/**
 * Created by chenmk on 2017/8/30.
 */
$(function() {
    ajax();
    function ajax() {
        $.ajax({
            url: "/mgr/companyList",
            type: "POST",
            dataType: "json",
            success: function (data) {
                console.log(data);
                setTable(data);
            }
        });
    }
    function setTable(data) {
        table =
            $('#table_example').dataTable({
                //使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
                //data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
                pageLength: 12,
                "bSort": false, //是否支持排序功能
                "bFilter": true, //搜索栏
                "bLengthChange": false,//每页显示的记录数
//	      searching: left,
                "aoColumns": [
                    {"data": "companyId", "sTitle": "序号", "bSortable": false},//序号
                    {"data": "companyName", "sTitle": "公司全称", "bSortable": false},//公司全称
                    {"data": "industry", "sTitle": "所属行业", "bSortable": false},//所属行业
                    {"data": "companyPhone", "sTitle": "公司电话", "bSortable": false},//公司电话
                    {"data": "address", "sTitle": "公司地址", "bSortable": false}//公司地址
                ],
                "aaData": data
            });
    }
});