$(function(){
	ajax();
	function ajax(){
		$.ajax({
		    url:"/mgr/userData",
		    type:"POST",
		    dataType:"json",
		    success:function (data) {
		        setTable(data);
		    }
		});
	}
	function setTable(data) {
		var f = 0;
		table = 
	    $('#table_id_example').dataTable({
	            //使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
	            //data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
	            pageLength: 12,
	            "bSort": false, //是否支持排序功能
	            "bFilter": true, //搜索栏
	            "bLengthChange": false,//每页显示的记录数
//	      searching: left,
	        "aoColumns":[
                {"data": "num" ,"sTitle":"序号","bSortable":false},//序号
                {"data": "name","sTitle":"会员姓名","bSortable":false},//会员姓名
                {"data": "phone","sTitle":"会员电话","bSortable":false},//会员电话
                {"data": "company.companyName","sTitle":"会员公司","bSortable":false},//会员公司
	        	{"data": "userNo" ,"sTitle":"会员卡号","bSortable":false},//会员卡号
                {"data": "score","sTitle":"会员积分","bSortable":false},//会员积分
                {"data": "","sTitle":"注册设备","bSortable":false,
                    "mRender":function (data, type, funll) {
                        return '<a href="javascript:;"  class="devicelink">查看注册的设备</a>';
                    }
                },//注册的设备
	        	{"data": "","sTitle":"操作","bSortable":false,
	        	    "mRender":function (data, type, funll) {
	        	        return '<a href="javascript:;"  class="tdlink">详情</a>';
	        	    }
	        	}//操作详情
	        ],
	        "aaData":data,
	    });
	    $("body").delegate('#table_id_example tr .tdlink','click',function(){
	    	f = 1;
        });
	    $("body").delegate('#table_id_example tr .devicelink','click',function(){
	    	f = 2;
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
	    		$('#marketdata').show();
	    		$("#name").text(name);
	    		$("#userNo").text(userNo);
	    		$('#company_phone').text(company_phone);
	    		$('#regTime').text(regTime);
	    		$('#score').text(score);
	    		$('#phone').text(phone);
	    		$('#address').text(address);
	    		$('#industry').text(industry);
	    		$('#company_name').text(company_name);
	    	}
	    	if(f == 2){
	    		console.log('111');
	    		sessionStorage.setItem('userNo',userNo);	    		
	    		sessionStorage.setItem('name',name);	    		
	    		sessionStorage.setItem('company_name',company_name);	    		
	    		sessionStorage.setItem('phoneNum',phone);	    		
	    		window.open("/page/devicetable","_self")
	    	}
	    	f = 0;
        });        
	}
})