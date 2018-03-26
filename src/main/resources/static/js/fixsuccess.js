$("#fixStatus").click(function(){
	//加载loading
	var $loadingToast = $('#loadingToast');
    $loadingToast.fadeIn(100);
    setTimeout(function () {
        $loadingToast.fadeOut(100);
    }, 50000);
	
	var fixId = $("#fixId").text(); 
	var deviceNo = $("#deviceNo").text(); 
	window.location.href="/fixStatusPage?fixId="+fixId+"&deviceNo="+deviceNo;
})

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