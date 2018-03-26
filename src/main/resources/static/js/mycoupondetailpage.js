$(".item4").click(function () {
    var deviceNo= $(this).siblings(".item6").find(".sp2").text();
    //加载loading
	var $loadingToast = $('#loadingToast');
    if ($loadingToast.css('display') != 'none') return;
    $loadingToast.fadeIn(100);
    setTimeout(function () {
        $loadingToast.fadeOut(100);
    }, 50000);
    
    $.ajax({
		url:"/useTicket",
		data:{
			"deviceNo":deviceNo
		},
		success:function(code){
			if(code == 0){
				window.location.href="/page/usesuccess";
			}
			else {
				$('#loadingToast').hide();
            	setTimeout(function () {
            		alert("该延保券已使用！");
            	},300);
			}
		}
	})
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