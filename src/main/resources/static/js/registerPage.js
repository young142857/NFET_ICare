/**
 * Created by chenmk on 2017/6/20.
 */
/**
* 注册页面项目流程处理
 * 一、页面输入框各项判断
 * 1,姓名判断
 * 2,公司名判断
 * 3,手机号码判断
 * 二、判断通过
 * 1,发送ajax请求，获取验证码
 * 2,按钮倒计时
*/
$("#btn1").click(function() {
    //手机正则表达式
    var reg = /^1[3|4|5|7|8][0-9]{9}$/;
    var phone = $("#phonenum").val();
    //姓名正则表达式
    var pattern=/^([\u4e00-\u9fa5]){2,5}$/;
    var name =$("#username").val();
    var company=$("#company").val().length;
    console.log(company);
    if(!pattern.test(name)){
        alert("请输入正确的姓名");
    }else if (company<2){
        alert("请输入正确的公司名");
    }else if (!reg.test(phone)) {
        alert("请输入正确的手机号");
    }else{
    stopwatch(this);
    var rq_url = "/sms/" + phone;
    console.log(rq_url);
    $.ajax({
        url : rq_url,
        type : 'GET', //GET
        timeout : 5000, //超时时间
        dataType : 'json', //返回的数据格式：json/xml/html/script/jsonp/text
        success : function(data, textStatus, jqXHR) {
            console.log(data);
            console.log(textStatus);
            console.log(jqXHR);
        },
        error : function(xhr, textStatus) {
            console.log('错误');
            console.log(xhr);
            console.log(textStatus);
        },
        complete : function() {
            console.log('结束');
        }
    })
    }
});
//60秒倒计时效果
var sec=60;
function stopwatch(o){
    if (sec==0) {
        o.removeAttribute("disabled");
        o.innerHTML="获取验证码";
        sec = 60;
    } else {
        o.setAttribute("disabled", true);
        o.innerHTML="重新发送(" + sec + "s)";
        sec--;
        setTimeout(function() {
                stopwatch(o)
            },
            1000)
    }
}
/**
 * 注册按钮处理
 *1,点击注册按钮
 *2,触发ajax事件，传值;
*/
$("#registerbtn").click(function(){
	
	    //手机正则表达式
	    var reg = /^1[3|4|5|7|8][0-9]{9}$/;
	    var phone = $("#phonenum").val();
	    //姓名正则表达式
	    var pattern=/^([\u4e00-\u9fa5]){2,5}$/;
	    var name =$("#username").val();
	    //公司名称长度
	    var company=$("#company").val().length;
	    //验证码
	    var vCode=$("#vcode").val();
	    var codePatter=/^\d{6}$/;
	    console.log(company);
	    if(!pattern.test(name)){
	        alert("请输入正确的姓名");
	    }else if (company<2){
	        alert("请输入正确的公司名");
	    }else if (!reg.test(phone)) {
	        alert("请输入正确的手机号");
	    }else if (!codePatter.test(vCode)) {
	        alert("请输入正确的验证码");
	    }else{
	    	//加载loading
	    	var $loadingToast = $('#loadingToast');
            if ($loadingToast.css('display') != 'none') return;
            $loadingToast.fadeIn(100);
            setTimeout(function () {
                $loadingToast.fadeOut(100);
            }, 50000);
	    	
		    $.ajax({
		        type:"GET",
		        url:"/reg",
		        data:{
		            userName:$("#username").val(),
		            phone:$("#phonenum").val(),
		            company:$("#company").val(),
		            vCode:$("#vcode").val()
		        },
		        success:function (map) {
		            if(map.errorCode==0) {   	
		            	window.location.href="/page/mydataregistered";
		            }
		            else{
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





