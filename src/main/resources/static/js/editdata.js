/**
 * Created by chenmk on 2017/7/12.

 */

//姓名的正则表达式判断
$("#userName").on("blur",function () {
    var pattern=/^([\u4e00-\u9fa5]){2,5}$/;
    var name =$("#userName").val();
    if(!pattern.test(name)){
        $("#toast").show();
        setTimeout(function(){$("#toast").hide();},2000);
    }
});

//三级联动效果处理
//省级区域处理
$(function () {
    $.ajax({
        url:"/provinceList",
        success:function (provinceList) {
            for(var i=0;i<provinceList.length;i++){            	
                var str = "<option value=\"" + provinceList[i]["provinceId"]+ "\">" +provinceList[i]["provinceName"]+ "</option>";
                $("#province").append(str);
            }
        }
    })
});

//省级地址变动时，触发ajax请求市级列表
$("#province").change(function () {
	document.getElementById("city").length = 0;
	$("#city").append("<option value='0'>请选择</option>");
	document.getElementById("area").length = 0;
	$("#area").append("<option value='0'>请选择</option>");
    $.ajax({
        type:"POST",
        url:"/cityList",
        data:{provinceId:$("#province").val()},
        success:function (cityList) {
            for(var c=0;c<cityList.length;c++ ){
                var str = "<option value=\"" + cityList[c]["cityId"]+ "\">" +cityList[c]["cityName"]+ "</option>";
                $("#city").append(str);
            }
        }
    });
});

//市级地址变动时，触发ajax请求县级列表
$("#city").change(function () {
	document.getElementById("area").length = 0;
	$("#area").append("<option value='0'>请选择</option>");
    $.ajax({
        type:"POST",
        url:"/areaList",
        data:{cityId:$("#city").val()}, 
        success:function (areaList) {
            for(var a=0;a<areaList.length;a++ ){
                var str = "<option value=\"" + areaList[a]["areaId"]+ "\">" +areaList[a]["areaName"]+ "</option>";
                $("#area").append(str);
            }
        }
    });
});

//点击保存按钮，ajax传值
$("#savebtn").click(function(){
	var industry = $("#industry").val();
	var address=$("#address").val();
	if(industry.length<2||industry.length>10){
		alert("所属行业最少2个字，最多不能超过10个字");
	}else if(address.length<5||address.length>60){
		alert("详细地址最少5个字，最多不能超过60个字");
	}else{
		//加载loading
		var $loadingToast = $('#loadingToast');
	    if ($loadingToast.css('display') != 'none') return;
	    $loadingToast.fadeIn(100);
	    setTimeout(function () {
	        $loadingToast.fadeOut(100);
	    }, 50000);
		
	    $.ajax({
	        type:"POST",
	        url:"/completeUserInfo",
	        data:{
	        	userName:$("#userName").val(),
	        	companyName:$("#company").val(),
	        	industry:$("#industry").val(),
	            provinceId:$("#province").val(),
	            cityId:$("#city").val(),
	            areaId:$("#area").val(),
	            address:$("#address").val()
	        },
	        success:function (map) {       	
	        	if(map.errorCode==0) {
	        		window.location.href="/page/mydataregistered";        		
	        	}
	        	else {
	        		$('#loadingToast').hide();
	            	setTimeout(function () {
	            		alert(map.desc);
	            	},300);       		
	        	}
	        }
	    })
	}	
});

(function(){
    window.alert = function(name){
        var iframe = document.createElement("IFRAME");
        iframe.style.display="none";
        iframe.setAttribute("src", 'data:text/plain');
        document.documentElement.appendChild(iframe);
        window.frames[0].window.alert(name);
        iframe.parentNode.removeChild(iframe);
    }
})();

$(function () {
	    var isPageHide = false;
	    window.addEventListener('pageshow', function () {
		      if (isPageHide) {
			        window.location.reload();
			      }
		    });
	    window.addEventListener('pagehide', function () {
		      isPageHide = true;
		    });
});
