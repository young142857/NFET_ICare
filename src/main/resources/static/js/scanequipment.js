/**
 * Created by chenmk on 2017/8/10.
 */
$(".import").click(function () {
    window.location.href="/page/inputpage";
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
                url:"/bindSingleDevice",
                data:{"deviceNo":result},
                success:function (map) {
                    if(map.errorCode==0){
                      window.location.href="/deviceDetail?deviceNo="+result+"&type=1";
                    }else{
                      alert(map.desc);
                    }
                }
            });
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