/**
 * Created by chenmk on 2017/8/29.
 */

$(".user").click(function () {
    $('.downlist').toggle();
});

$("#downli-first").click(function () {
    $(this).siblings().children().css("color","#999999");
    $(this).children().css("color","#00a0e9");
    $("#companydata").hide();
    $("#devicedata").hide();
    $("#repairdata").hide();
    $("#warratydata").hide();
    $("#userdata").show();
});

$("#downli-second").click(function () {
    $(this).siblings().children().css("color","#999999");
    $(this).children().css("color","#00a0e9");
    $("#companydata").show();
    $("#userdata").hide();
    $("#devicedata").hide();
    $("#repairdata").hide();
    $("#warratydata").hide();
});

$(".devicelink").click(function () {
    $("#userdata").hide();
    $("#devicedata").show();
});

$(".repairlink").click(function () {
    $("#devicedata").hide();
    $("#warratydata").hide();
    $("#repairdata").show();
});
$(".warratylink").click(function () {
    $("#devicedata").hide();
    $("#repairdata").hide();
    $("#warratydata").show();
});

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

$(".user-repair").click(function () {
    $('.downlist-repair').toggle();
});
$(".user-warraty").click(function () {
    $('.downlist-warraty').toggle();
});
//点击上传客户设备按钮
$("#customerdevice").click(function () {
	/*debugger;*/
	$("#errorInfo").text("");
	$("#choose").val("");
	$(".progress-bar").css("width",0);
	var fileType = "customer_device";
	sessionStorage.setItem('fileType', fileType);
	$("#updatamodal").show();
});
//点击上传客户资料按钮
$("#customerInfo").click(function () {
	/*debugger;*/
	var fileType = "customerInfo";
	sessionStorage.setItem('fileType', fileType);
	$("#updatamodal").show();
});
//点击上传客户公司资料
$("#customercompany ").click(function () {
	/*debugger;*/
	$("#errorInfo").text("");
	$("#choose").val("");
	$(".progress-bar").css("width",0);
	var fileType = "customer_company";
	sessionStorage.setItem('fileType', fileType);
	$("#updatamodal").show();
});
$("#updatabtn").click(function () {
    $("#updatamodal").show();
});

/*$("#updata").click(function () {
	var choosevalue =$("#choose").val();
   if (choosevalue!="") {
       $("#updata").unbind();
           $(".progress-bar").animate({width:"526px"},function () {
               $(this).text("100%");
           });
   }
});*/

$(".keepbtnx").click(function () {
    //$("#updatamodal").hide();
    window.location.reload();
});

$(".sp_secondx").click(function () {
    /*$("#updatamodal").hide();*/
	window.location.reload();
});

$(".imglix").click(function () {
    /*$("#updatamodal").hide();*/
	window.location.href="/page/trafficstatistics";
});

$(".imgliy").click(function () {
    /*$("#updatamodal").hide();*/
	window.location.href="/page/marketdepartment";
});