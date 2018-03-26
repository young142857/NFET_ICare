/**
 * Created by chenmk on 2017/7/20.
 */
/**
 * 页面五角星评级处理
 * 点击五角星，五角星转换为实心，评价文字随之相应变化
 */
$(function () {
    //设备评价处理
    $(".starbox>.star").click(function () {
        $(this).removeClass("bg1").addClass("bg2");
        $(this).prevAll().removeClass("bg1").addClass("bg2");
        $(this).nextAll().removeClass("bg2").addClass("bg1");
        var device=$(".bg2");
        device.length==1?$(".word").html("非常差"):
            device.length==2?$(".word").html("差"):
                device.length==3?$(".word").html("一般"):
                    device.length==4?$(".word").html("满意"):$(".word").html("很满意");
    });
    //客服评价处理
    $(".starbox2>.star").click(function () {
        $(this).removeClass("bg1").addClass("bg2x");
        $(this).prevAll().removeClass("bg1").addClass("bg2x");
        $(this).nextAll().removeClass("bg2x").addClass("bg1");
        var service=$(".bg2x");
        service.length==1?$(".word2").html("非常差"):
            service.length==2?$(".word2").html("差"):
                service.length==3?$(".word2").html("一般"):
                    service.length==4?$(".word2").html("满意"): $(".word2").html("很满意");
    });
    //维修师傅评价处理
    $(".starbox3>.star").click(function () {
        $(this).removeClass("bg1").addClass("bg2s");
        $(this).prevAll().removeClass("bg1").addClass("bg2s");
        $(this).nextAll().removeClass("bg2s").addClass("bg1");
        var repair=$(".bg2s");
        repair.length==1?$(".word3").html("非常差"):
            repair.length==2?$(".word3").html("差"):
                repair.length==3?$(".word3").html("一般"):
                    repair.length==4?$(".word3").html("满意"):$(".word3").html("很满意");
    });
});
/**
 * 点击发布按钮处理
 * 判断：
 *设备评价、客服态度、维修态度、评价信息
 * 页面取值、跳转
 */
$(function () {
    //点击发布按钮
    $(".footer").click(function () {
        var fixNo=$(".device").text();
        var evaluateDevice=$(".bg2").length;
        var evaluateService=$(".bg2x").length;
        var evaluateRepair=$(".bg2s").length;
        var evaluateAnonymous=$(".weui-switch:checked").length;
        var evaluateDesc=$(".textarea1").val().length;
        var desc=$(".textarea1").val();
        console.log(evaluateAnonymous);
        if(evaluateDevice==0){
            alert("请评价设备");
        }else if(evaluateDesc>40){
            alert("字数超出限制");
        }else if(evaluateService==0){
            alert("请评价客服人员态度");
        }else if(evaluateRepair==0){
            alert("请评价维修师傅态度");
        }else{
        	//加载loading
        	var $loadingToast = $('#loadingToast');
            if ($loadingToast.css('display') != 'none') return;
            $loadingToast.fadeIn(100);
            setTimeout(function () {
                $loadingToast.fadeOut(100);
            }, 50000);
        	
        	$.ajax({
        		url:"insertEvaluate",
        		data:{
        			"fixNo":fixNo,
        			"evaluateDevice":evaluateDevice,
        			"evaluateService":evaluateService,
        			"evaluateRepair":evaluateRepair,
        			"evaluateAnonymous":evaluateAnonymous,
        			"evaluateDesc":desc
        		},
        		success:function(data){
        			if(data==0){
        				window.location.href="/page/estimatesuccess";
        			}
        			else{
        				$('#loadingToast').hide();
                    	setTimeout(function () {
                    		alert("该设备已评价！");
                    	},300);        				
        			}
        		}
        	})
        }
    });
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