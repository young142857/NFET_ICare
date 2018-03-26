$(function(){
	ajax();
	function ajax(){
		$.ajax({
		    url:"/mgr/userList",
		    type:"POST",
		    dataType:"json",
		    success:function (data) {
		        setTable(data.rows);
		    }
		});
	}
	function setTable(data) {
		var f = 0;
		table = 
	    $('#table_id_example').dataTable({
	            //使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
	            //data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
	            pageLength: 8,
	            "bSort": false, //是否支持排序功能
	            "bFilter": true, //搜索栏
	            "bLengthChange": false,//每页显示的记录数
//	      searching: left,
	        "aoColumns":[
	        	{"data": "name","sTitle":"会员姓名","bSortable":false},//会员姓名
	        	{"data": "phone","sTitle":"会员电话","bSortable":false},//会员电话
	        	{"data": "company.companyName","sTitle":"会员公司","bSortable":false},//会员公司
	        	{"data": "userNo" ,"sTitle":"会员号","bSortable":false},//会员号
	        	{"data": "regTime","sTitle":"注册日期","bSortable":false},//注册日期
	        	{"data": "score","sTitle":"会员积分","bSortable":false},//会员积分
	        	{"data": "","sTitle":"操作","bSortable":false,
	        	    "mRender":function (data, type, funll) {
	        	        return '<a href="javascript:;"  class="tdlink">详情</a>';
	        	    }
	        	}//会员公司	        	
	        ],
	        "aaData":data,
	    });
	    $("body").delegate('#table_id_example tr .tdlink','click',function(){
	    	f = 1;
        });
	    $("body").delegate('#table_id_example tr','click',function(){
	    	var oTable = table.fnGetData(this);
	    	console.log(oTable);
	    	var name = oTable.name;
	    	var userNo = oTable.userNo;
	    	var regTime = oTable.regTime;
	    	var score = oTable.score;
	    	var phone = oTable.phone;
	    	var company_name = oTable.company.companyName;
	    	var company_phone = oTable.company.companyPhone;
	    	var address = oTable.company.address;
	    	var industry = oTable.company.industry;
	    	if(company_phone==null){
	    		company_phone = "";
	    	}
	    	if(address==null){
	    		address = "";
	    	}
	    	if(industry==null){
	    		industry = "";
	    	}
	    	if(f == 1){
	    		$('.modalbox').show();
	    		$("#name").text(name);
	    		$("#userNo").text(userNo);
	    		$('#company_phone').val(company_phone);
	    		$('#regTime').text(regTime);
	    		$('#score').text(score);
	    		$('#phone').text(phone);
	    		$('#address').text(address);
	    		$('#industry').text(industry);
	    		$('#company_name').val(company_name);
	    	}
	    	f = 0;
        });        
	}
})