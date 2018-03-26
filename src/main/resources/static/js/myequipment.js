/**
 * Created by chenmk on 2017/6/30.
 */
//页面导航按钮样式切换
$('.weui-navbar__item').on('click', function () {
    $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
});
//页面切换处理
$("#btn1").click(function () {
        $(".main_x,.main_s").hide();
        $(".main").show();
});

$("#btn2").click(function () {
    $(".main,.main_s").hide();
    $(".main_x").show();
});

$("#btn3").click(function () {
    $(".main,.main_x").hide();
    $(".main_s").show();
});

//点击页面设备型号，页面跳转至设备详情
$(".nameli").click(function(){
	var ht = $(this).find(".namesp2")[0].innerText;
	window.location.href="/deviceDetail?deviceNo="+ht;
});


$(".btn1").click(function () {
    var fix =$(this).parent().siblings().find(".namesp2")[0].innerText;
    window.location.href="/fixOrder?deviceNo="+fix;
});

$(".btn2").click(function () {
	var warranty =$(this).parent().siblings().find(".namesp2")[0].innerText;
    window.location.href="/warrantyList?deviceNo="+warranty;
});


$("#now").click(function () {
	$.ajax({
    	url:"/getTicket",
    	data:{"count":$("#count").text},
		success:function(code){
			if(code == 0){
				window.location.href="/page/myequipment(1)";				
			}
		}
    });
});

$(".bind").click(function () {
    window.location.href="/wxjs/scan?type=1";
});
$(".fix").click(function () {
    window.location.href="/wxjs/scan?type=2";
});
$(".warranty").click(function () {
    window.location.href="/wxjs/scan?type=3";
});