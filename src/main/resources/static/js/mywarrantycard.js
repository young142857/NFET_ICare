/**
 * Created by chenmk on 2017/6/27.
 */
$(document).ready(function () {
    //导航条样式切换处理
    $('.weui-navbar__item').on('click', function () {
        $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
    });
    //页面切换处理
    $("#btn1").click(function () {
        $(".main_a,.main_b").hide();
        $(".main").show();
    });

    $("#btn2").click(function () {
        $(".main,.main_b").hide();
        $(".main_a").show();
    });

    $("#btn3").click(function () {
        $(".main,.main_a").hide();
        $(".main_b").show();
    });

    //点保修卡，页面跳转保修卡详情
    $(".warrantycard1").click(function(){
    	//var days=$(this).find(".days")[0].innerText;
        var deviceNo = $(this).find(".spCode")[0].innerText;
   //判断天数是否为0，如果为0，则移除click事件
        //if(days==0){
        	 //$(this).unbind();
       // }else{
        	window.location.href="ecardDetail?deviceNo="+deviceNo;
        //}
    });
//点我延保处理；   	 
    $(".warrantyitem_5s").click(function () {
        var deviceNos=$(this).siblings().find(".spCode")[0].innerText;
        window.location.href="/warrantyList?deviceNo="+deviceNos;
    });
});

