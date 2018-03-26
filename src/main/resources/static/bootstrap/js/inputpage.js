/**
 * Created by chenmk on 2017/6/29.
 */
$(document).ready(function(){
  $('.weui-navbar__item').on('click', function () {
    $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
  });
 var deviceSelType = 1 ;
  $("#btn2").click(function (deviceSelType) {
    $(".more").show();
      deviceSelType =2;
  });

  $("#btn1").click(function (deviceSelType) {
    $(".more").hide();
      deviceSelType=1;
  });
  //点击提交按钮
  $("#submit").click(function (deviceSelType) {
      // 判断“输入多个设备”按钮的点击状态
     //如果“多个设备”按钮被选中，则传两个值，否则传一个值
      if(deviceSelType==2){
        $.ajax({
            type:"POST",
            url:"/bindSingleDevice",
            data:{
                deviceNoStart:$("#equipmentNo1").val(),
                deviceNoEnd:$("#equipmentNo2").val()
            },
            success:function (map) {
                if (map.errorCode==0){
                    window.location.href="/page/mydataregistered";
                }else{
                    alert(map.desc);
                }
            }
        });
      }else{
          $.ajax({
              type:"POST",
              url:"/bindSingleDevice",
              data:{deviceNo:$("#equipmentNo1").val()},
              success:function (map) {
                  if(map.errorCode==0){
                    window.location.href="/page/mydataregistered";
                  }else{
                    alert(map.desc);
                  }
              }
          });
      }
  });
});
