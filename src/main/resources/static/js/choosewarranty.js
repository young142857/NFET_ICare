/**
 * Created by chenmk on 2017/7/25.
 */

$(document).ready(
    // 页面记载时，向页面传值
    // 结算结算金额有初始合计值
    $(function () {
        $(".checklab1 input:first").attr("checked",true);
        var priceFirst= $(".main_first").find(".weui-check:checked").parent().siblings().children(".ft_d2").text();
        var priceSecond= $(".main_second").find(".weui-check:checked").parent().siblings().children(".ft_d2").text();
        var priceNumFirst=Number(priceFirst.substring(1));
        var priceNumSecond=Number(priceSecond.substring(1));
        var total=priceNumFirst+priceNumSecond;
        $("#pay").text("￥"+total);
    }),
    $(function () {
        var originalData=$(".visitdata").text();
        //点击单选按钮，结算金额动态变化
        $(".weui-check").click(function () {
            $(this).parent().parent().siblings(".checklab1").find(".weui-check").attr("checked",null);
            $(this).parent().parent().siblings(".checklab2").find(".weui-check").attr("checked",null);
            var priceFirst= $(".main_first").find(".weui-check:checked").parent().siblings().children(".ft_d2").text();
            var priceSecond= $(".main_second").find(".weui-check:checked").parent().siblings().children(".ft_d2").text();
            var priceNumFirst=Number(priceFirst.substring(1));
            var priceNumSecond=Number(priceSecond.substring(1));
            var total=priceNumFirst+priceNumSecond;
            $("#pay").text("￥"+total);
            //判断保修且上门的点击状态；对延保日期进行处理
            //如果报修且上门被点击，且y值不等于空则延保日期等于a
            //否则延保日期等于原始值
            var data=$(".main_first").find(".weui-check:checked").parent().siblings().find(".data").text();
            if($(".visitcheck").is(":checked")&&data!="") {
                var dataDay=data.substring(4);
                var dataYear=Number(data.slice(0,4))+1;
                var dataTotal=dataYear+dataDay;
                $(".visitdata").text(dataTotal);
            }else {
                $(".visitdata").text(originalData);
            }
        })
    }),
    //点击确定按钮，取各项值并且将值传给后台
    $("#chooseSubBtn").click(function () {
    	//加载loading
    	var $loadingToast = $('#loadingToast');
        if ($loadingToast.css('display') != 'none') return;
        $loadingToast.fadeIn(100);
        setTimeout(function () {
            $loadingToast.fadeOut(100);
        }, 50000);
    	
        var deviceNo =$("#deviceNo").text();
        var pkgType  =$(".main_first").find(".weui-check:checked").parent().siblings().children(".bd_p1").text();
        var visitType=$(".main_second").find(".weui-check:checked").parent().siblings().children(".bd_p1").text();
        var payAmt=$("#pay").text().substring(1);
        if(payAmt != 0) {
        	window.location.href="/clickPay?deviceNo="+deviceNo+"&pkgType="+pkgType+"&visitType="+visitType+"&payAmt="+payAmt;
        }        
    })
);

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