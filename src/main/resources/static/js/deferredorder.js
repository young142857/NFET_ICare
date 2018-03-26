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

$(function () {    
	$("#pay_confirm").click(function () {
		var warrantyNo = $("#warrantyNo").text();
		var deviceNo = $("#deviceNo").text();
		var payAmt = $("#amount").text().substring(1);
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
					"appId" : data.appid, 
					//"appId" : "wxb3b870cc433e0099",
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