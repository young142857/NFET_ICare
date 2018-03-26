$(function(){
	ajax();
	function ajax(){
		$.ajax({
		    url:"/mgr/warrantyStatistics",
		    type:"POST",
		    dataType:"json",
		    success:function (data) {
		        setTable(data.totalCount);
		    }
		});
	}

	function setTable(data) {
	    $('#warratytable').dataTable({
	            //使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
	            //data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
	            //pageLength: 8,
				"bPaginate":false,
	            "bSort": false, //是否支持排序功能
	            "bFilter": false, //搜索栏
	            "bLengthChange": false,//每页显示的记录数
	            "oLanguage":false,
//	      searching: left,
	        "aoColumns":[
	        	{"data": "name" ,"sTitle":"延保套餐","bSortable":false},//延保套餐
	        	{"data": "value","sTitle":"份数","bSortable":false},//份数
	        	{"data": "payAmts","sTitle":"金额（元）","bSortable":false},//金额 
	        ],
	        "aaData":data,
	    });
	}    
})