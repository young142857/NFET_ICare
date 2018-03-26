/**
 * Created by chenmk· on 2017/7/12.
 */
$(document).ready(function () {
    /**
     * 故障类型处理
     * ajax请求故障问题列表
     */
    $(function () {
        $.ajax({
            type:"POST",
            url:"/faultList",
            success:function (faultList) {
                for(var i=0;i<faultList.length;i++){
                    var faults = "<option value=\"" + faultList[i]["type_no"]+ "\">" +faultList[i]["type_name"]+ "</option>";
                    $("#fault").append(faults);
                }
            }
        });
    });
    
    /**
     * 维修方式绑定点击事件
     * 取得维修方式的选项值
     * 判断维修方式的选项值，如果为0；则显示县级列表
     * 否则，县级列表隐藏,详细地址属性变为只读
     */
    $("#repair").click(function () {
    	$("#province").find("option:first").removeAttr("selected");
    	$("#city").find("option:first").removeAttr("selected");
    	$("#county").find("option:first").removeAttr("selected");
    	$("#province").find("option[value='0']").attr("selected","selected");
    	$("#city").find("option[value='0']").attr('selected','selected');
    	$("#county").find("option[value='0']").attr('selected','selected');
    	$("#address").val("");
        var repair =$("#repair").find("option:selected").val();
        /*if(repair==0){
            //$("#county").show();
            $("#address").removeAttr("readonly");

        }else{
            //$("#county").hide();
            $("#address").attr("readonly","readonly");

        }*/
    });
    /**
     * 三级联动效果处理
     * 省级区域处理，ajxa请求省级列表
     * 省级地址变动时，触发ajax请求市级列表
     */
//省级区域处理
    $(function () {
        $.ajax({
            url:"/provinceList",
            success:function (provinceList) {
                for(var i=0;i<provinceList.length;i++){
                    var province = "<option value=\"" + provinceList[i]["provinceId"]+ "\">" +provinceList[i]["provinceName"]+ "</option>";
                    $("#province").append(province);
                }
            }
        })
    });
//省级地址变动时，触发ajax请求市级列表
    $("#province").change(function () {
        document.getElementById("city").length = 1;
        document.getElementById("county").length = 1;
        $.ajax({
            type:"POST",
            url:"/cityList",
            data:{provinceId:$("#province").val()},
            success:function (cityList) {
                for(var c=0;c<cityList.length;c++ ){
                    var city = "<option value=\"" + cityList[c]["cityId"]+ "\">" +cityList[c]["cityName"]+ "</option>";
                    $("#city").append(city);
                }
            }
        });
    });
    /**
     * 市级地址变动时，触发ajax处理
     * 取得维修方式的选项值
     * 判断维修方式的选项值，如果为0；则ajax请求县级区域列表
     * 否则，ajax请求分公司地址
     */
    $("#city").change(function () {
        var fixWay =$("#repair").find("option:selected").val();
        document.getElementById("county").length = 1;
        if(fixWay==0){
            $.ajax({
                type:"POST",
                url:"/areaList",
                data:{cityId:$("#city").val()},
                success:function (areaList) {
                    for(var a=0;a<areaList.length;a++ ){
                        var county = "<option value=\"" + areaList[a]["areaId"]+ "\">" +areaList[a]["areaName"]+ "</option>";
                        $("#county").append(county);
                    }
                }
            });
        }else{
            $.ajax({
                type:"POST",
                url:"/branchAddress",
                data:{cityId:$("#city").val()},
                success:function (branch) {
                    $("#address").val(branch.address);
                }
            });
        }
    });
/**
  * 点击提交按钮处理
  * 判断故障类型，所属行业，所在区域，字数
  * 页面跳转传值
*/
     //点击提交按钮页面处理
    $(".submitbt").click(function () {
        var deviceNo =$("#deviceNo").text();
        var fixNo = $("#fault").find("option:selected").val();
        var faultName = $("#fault").find("option:selected").text();
        var fixPattern=$("#repair").find("option:selected").val();
        var industry =$("#industry").val();
        var provinceId=$("#province").val();
        var cityId=$("#city").val();
        var areaId=$("#county").val();
        var address=$("#address").val();
        var desc=$("#desc").val().length;
        var fixDesc=$("#desc").val();
        if(fixNo==0){
            alert("请选择故障类型");
        }else if(industry.length<2||industry.length>10){
            alert("所属行业最少2个字，最多不能超过10个字");
        }else if(desc>40){
            alert("字数超出限制");
        }else if(provinceId==0||cityId==0){
            alert("请选择所在区域");
        }
        else if(areaId==0&&fixPattern==0){
            alert("请选择所在区域");
        }
        else if(address.length<5||address.length>60){
            alert("详细地址最少5个字，最多不能超过60个字");
        }
        else{
        	//加载loading
        	var $loadingToast = $('#loadingToast');
            if ($loadingToast.css('display') != 'none') return;
            $loadingToast.fadeIn(100);
            setTimeout(function () {
                $loadingToast.fadeOut(100);
            }, 50000);
        	
        	$.ajax({
        		url:"clickFix",
        		data:{
        			"deviceNo":deviceNo,
        			"fixNo":fixNo,
        			"fixPattern":fixPattern,
        			"industry":industry,
        			"provinceId":provinceId,
        			"cityId":cityId,
        			"areaId":areaId,
        			"address":address,
        			"fixDesc":fixDesc,
        			"faultName":faultName
        		},
        		success:function(data){
        			if(data.code == 0){
        				window.location.href="/fixsuccess?fixId="+data.fixId+"&deviceNo="+data.deviceNo;
        			}
        			else{
        				$('#loadingToast').hide();
                    	setTimeout(function () {
                    		alert("该设备正在维修中！");
                    	},300);
        			}
        		}
        		
        	})
        }
    });
    
    $(".problem").click(function(){
    	window.location.href="/page/problemlist";
    })
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


