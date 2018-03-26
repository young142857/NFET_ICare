/**
 * Created by chenmk on 2017/8/2.
 */
$(".btn2").click(function () {
	//加载loading
	var $loadingToast = $('#loadingToast');
    if ($loadingToast.css('display') != 'none') return;
    $loadingToast.fadeIn(100);
    setTimeout(function () {
        $loadingToast.fadeOut(100);
    }, 50000);
	
    var fixNo=$("#orderNo").text();
    var deviceNo=$("#deviceNo").text();
    window.location.href="/skipToEvaluate?fixNo="+fixNo+"&deviceNo="+deviceNo;
});

$(".status").click(function () {
	//加载loading
	var $loadingToast = $('#loadingToast');
    if ($loadingToast.css('display') != 'none') return;
    $loadingToast.fadeIn(100);
    setTimeout(function () {
        $loadingToast.fadeOut(100);
    }, 50000);
	
    var fixNo=$("#orderNo").text();
    var deviceNo=$("#deviceNo").text();
    window.location.href="/fixStatusPage?fixId="+fixNo+"&deviceNo="+deviceNo;
});

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