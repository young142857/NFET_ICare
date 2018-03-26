/**
 * Created by chenmk on 2017/6/29.
 */
$(document).ready(function(){
  $('.weui-navbar__item').on('click', function () {
    $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
  });
 var deviceSelType = 1 ;
  $("#btn2").click(function () {
      $(".one").hide();
      $(".more").show();
      deviceSelType =2;
  });

  $("#btn1").click(function () {
    $(".more").hide();
      $(".one").show();
      deviceSelType=1;
  });
  //点击提交按钮
  $("#submit").click(function () {
      // 判断“输入多个设备”按钮的点击状态
     //如果“多个设备”按钮被选中，则传两个值，否则传一个值
	  var deviceNo=$("#equipmentNo1").val();
	  //输入页面非空判断
	  if(deviceNo==""){
	      alert("请输入设备编码！")
	  }
	  else if(deviceSelType==2){
		if($("#equipmentNo2").val().length>5&&$("#equipmentNo3").val().length>5){
			$.ajax({
				type:"POST",
				url:"/bindMoreDevice",
				data:{
					deviceNoStart:$("#equipmentNo2").val(),
					deviceNoEnd:$("#equipmentNo3").val()
				},
				success:function (map) {
					if (map.errorCode==0){
						window.location.href="/page/myequipment(1)";
					}else{
						alert(map.desc);
					}
				}
			});
		}
      }else{
    	  //加载loading
    	  var $loadingToast = $('#loadingToast');
          $loadingToast.fadeIn(100);
          setTimeout(function () {
              $loadingToast.fadeOut(100);
          }, 50000);
          
          $.ajax({
              type:"POST",
              url:"/bindSingleDevice",
              data:{deviceNo:$("#equipmentNo1").val()},
              success:function (map) {
                  if(map.errorCode==0){
                	  window.location.href="/deviceDetail?deviceNo="+$("#equipmentNo1").val()+"&type=1";
                  }else{
                	  $('#loadingToast').hide();
		              setTimeout(function () {
		            	 alert(map.desc);
		              },300);
                  }
              }
          });
      }
  });
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
