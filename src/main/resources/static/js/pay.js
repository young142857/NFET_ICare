$(function () {

    function onBridgeReady() {
        //WeixinJSBridge.call('hideOptionMenu');
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
    
    $("#pay_confirm").click(function () {
        var req = $.ajax({
            url : '/wxjs/pay',
            type : 'get',
            dataType : 'json',
            success : function (data) {
               WeixinJSBridge.invoke('getBrandWCPayRequest', {
                    "appId" : "wxb3b870cc433e0099",
                    "timeStamp" : data.timeStamp,
                    "nonceStr" : data.nonceStr,
                    "package" : data.pkg,
                    "signType" :"MD5",
                    "paySign" : data.paySign
                }, function(res) {
                    if (res.err_msg == "get_brand_wcpay_request:ok") {
                        window.location.href = "/paySuccess";
                    }
                });
            },
            error : function () {
                alert("error");
            }
        });
    });
});
