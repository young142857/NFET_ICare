/**
 * Created by chenmk on 2017/8/8.
 */
/*$(".navli-first,.navli-second").click(function () {
    $(this).children().addClass("backstyle");
    $(this).siblings().children().removeClass("backstyle");
});*/

$(".user").click(function () {
        $('.downlist').toggle();
});

$("#downli-first").click(function () {
    $(this).siblings().children().css("color","#999999");
    $(this).children().css("color","#00a0e9");
    $(".user-main").show();
    $(".repair-main").hide();
    $(".warraty-main").hide();
});

$("#downli-second").click(function () {
    $(this).siblings().children().css("color","#999999");
    $(this).children().css("color","#00a0e9");
    $(".user-main").hide();
    $(".repair-main").show();
    $(".warraty-main").hide();
});

$("#downli-third").click(function () {
    $(this).siblings().children().css("color","#999999");
    $(this).children().css("color","#00a0e9");
    $(".user-main").hide();
    $(".repair-main").hide();
    $(".warraty-main").show();
});
var ad=$(".tdlink");
//点击模态框编辑按钮，公司全称和公司电话，变为可编辑
$(".sp_third").click(function () {
    $(".modal-tdx").children().removeAttr("readonly");
});
//点击模态框关闭按钮，模态框隐藏消失
$(".sp_second").click(function () {
    $(".modalbox").hide();
});
//保存按钮处理
$(".keepbtn").click(function () {
    $(".modalbox").hide();
});
