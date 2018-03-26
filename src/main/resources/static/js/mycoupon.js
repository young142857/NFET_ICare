/**
 * Created by chenmk on 2017/6/26.
 */
$(document).ready(function () {
    //页面导航条按下后的样式切换
    $('.weui-navbar__item').on('click', function () {
        $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
    });
    //页面切换处理
    $(".btn2").click(function () {
        $(".main_deferred").hide();
        $(".main_coupon").show();
    });

    $(".btn1").click(function () {
        $(".main_coupon").hide();
        $(".main_deferred").show();
    });
    

    $(".deferredbox2").click(function () {
        var deviceCode =$(this).find(".expirydate").children()[0].innerHTML;
        var deviceCodeSub=deviceCode.substring(5);
        window.location.href="ticketDetail?deviceNo="+deviceCodeSub;
    });
});


