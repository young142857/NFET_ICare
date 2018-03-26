/**
 * Created by chenmk on 2017/7/6.
 */

$(document).ready(function () {
    //页面导航样式处理
    $('.weui-navbar__item').on('click', function () {
        $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
    });
    //页面切换处理
    $("#btn2").click(function () {
        $(".main_fix").hide();
        $(".main_order").show();
    });

    $("#btn1").click(function () {
        $(".main_order").hide();
        $(".main_fix").show();
    });
    //我的延保订单未支付，页面跳转传值页面跳转传值处理
    $(".order").click(function () {
        var deviceNo =$(this).next().find(".device")[0].innerText;
        window.location.href="/warrantyDetail?sort=1&warrantyNo="+deviceNo;
    });
    //我的延保订单已完成,页面跳转传值页面跳转传值处理
    $(".done").click(function () {
        var devices =$(this).next().find(".device_s")[0].innerText;
        window.location.href="/warrantyDetail?sort=2&warrantyNo="+devices;
    });
    //我的报修订单处理中，页面跳转传值页面跳转传值处理
    $(".fix").click(function () {
        var devicez =$(this).next().find(".fixcode")[0].innerText;
        window.location.href="/fixDetail?sort=2&fixId="+devicez;
    });
    //我的报修订单处理中已评价，页面跳转传值页面跳转传值处理
    $(".fixEvaluate").click(function () {
        var devicez =$(this).next().find(".fixevacode")[0].innerText;
        window.location.href="/fixDetail?sort=1&fixId="+devicez;
    });
    //我的报修订单未评价，页面跳转传值页面跳转传值处理
    $(".fixdone").click(function () {
        var deviced =$(this).next().find(".donecode")[0].innerText;
        window.location.href="/fixDetail?sort=2&fixId="+deviced;
    });
    // 我的报修订单已评价，页面跳转传值页面跳转传值处理
    $(".fixover").click(function () {
        var deviceo =$(this).next().find(".overcode")[0].innerText;
        window.location.href="/fixDetail?sort=1&fixId="+deviceo;
    });
    
    $(".evaluate").click(function () {
        var fixNo=$(this).closest(".handle").siblings().find(".orderNo")[0].innerText;
        var deviceNo=$(this).closest(".handle").siblings().find(".deviceNo")[0].innerText;
        window.location.href="/skipToEvaluate?fixNo="+fixNo+"&deviceNo="+deviceNo;
    });
    
    function onBridgeReady() {
        WeixinJSBridge.call('hideOptionMenu');
    }

    if (typeof WeixinJSBridge == "undefined") {
        if (document.addEventListener) {
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        } else if (document.attachEvent) {
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    } else {
        onBridgeReady();
    }
    
    $(".pay").click(function () {
    	var warrantyNo =$(this).closest(".handle").siblings().find(".device")[0].innerText;
    	var deviceNo =$(this).closest(".handle").siblings().find(".deviceNo")[0].innerText;
    	var payAmt =$(this).closest(".handle").siblings().find(".amount")[0].innerText;
    	var req = $.ajax({
            url : 'wxjs/pay',
            type : 'get',
            data: {
            	"deviceNo":deviceNo, 
            	"payAmt":payAmt 
            },
            dataType : 'json',
            success : function (data) {
               WeixinJSBridge.invoke('getBrandWCPayRequest', {
            	   //"appId" : "wxb3b870cc433e0099",
            	    "appId" : data.appid,
                    "timeStamp" : data.timeStamp,
                    "nonceStr" : data.nonceStr,
                    "package" : data.pkg,
                    "signType" :"MD5",
                    "paySign" : data.paySign
                }, function(res) {
                    if (res.err_msg == "get_brand_wcpay_request:ok") {
                    	//加载loading
                  	  	var $loadingToast = $('#loadingToast');
                        $loadingToast.fadeIn(100);
                        setTimeout(function () {
                            $loadingToast.fadeOut(100);
                        }, 50000);
                    	
                    	$.ajax({
                    		url:"paySuccess",
                    		data:{
                    			"warrantyNo":warrantyNo
                    		},
                    		success:function(data){
                    			if(data==0){
                    				window.location.href="/page/warrantysuccess";
                    			}
                    		}                    		
                    	})
                    }
                });
            },
            error : function () {
                alert("error");
            }
        });
    });
});

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
