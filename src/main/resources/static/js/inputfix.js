/**
 * Created by chenmk on 2017/7/31.
 */
//点击输入后，页面进行跳转处理
$("#fix").click(function () {
    window.location.href="/page/inputfix";
});


//点击提交按钮，将输入的值传给后台
$(".zhucebt").click(function () {
    var deviceNo = $("#equipmentNo1").val();
	if(deviceNo==""){
      alert("请输入设备编码！")
	}
	else {
		//加载loading
    	var $loadingToast = $('#loadingToast');
        if ($loadingToast.css('display') != 'none') return;
        $loadingToast.fadeIn(100);
        setTimeout(function () {
            $loadingToast.fadeOut(100);
        }, 50000);
        
		$.ajax({
            type:"POST",
            url:"/checkFix",
            data:{"deviceNo":deviceNo},
            success:function(map){
                if(map.errorCode == 0){
                    window.location.href="/fixOrder?deviceNo="+deviceNo;
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

$("#scan_wx").click(function () {
	scanQr();
});

function scanQr(){
    wx.scanQRCode({
        needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
        scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
        success: function (res) {
            var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
            //alert(result);
            $.ajax({
                type:"POST",
                url:"/checkFix",
                data:{"deviceNo":result},
                success:function(map){
                    if(map.errorCode == 0){
                        window.location.href="/fixOrder?deviceNo="+result;
                    }
                    else {
                        alert(map.desc);
                    }
                }
            }) 
        }
    }); 
}

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


