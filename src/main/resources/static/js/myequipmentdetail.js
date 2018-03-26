/**
 * Created by chenmk on 2017/6/27.
 */
$(document).ready(function () {
    //保修卡详情
    $(".imgsp").click(function(){
        var ht = $(".mainlist").find(".listsp2")[0].innerText;
        window.location.href="ecardDetail?deviceNo="+ht;
    });
    
    //弹出框效果处理
    $(function(){
        $('#redpacket').on('click', function(){
            $(this).fadeOut(200);
            $('#iosDialog3').fadeIn(200);
        });
        $("#now").click(
            function () {
            	var ht = $("#device").text();
                $.ajax({
                	url:"/getTicket",
                	data:{"count":"1"},
            		success:function(code){
            			if(code == 0){
            				window.location.href="deviceDetail?deviceNo="+ht;
            			}
            		}
                });
            });

        $("#user").click(function () {
        	$("#iosDialog3").fadeOut(200);
        	var ht = $("#device").text();
        	$.ajax({
        		url:"/useTicket",
        		data:{
        			"deviceNo":ht
        		},
        		success:function(code){
        			if(code == 0){
        				window.location.href="deviceDetail?deviceNo="+ht;
        			}
        		}
        	})
        });
    });
    
    $(".btn1").click(function () {
        var fix =$("#device").text();
        window.location.href="/fixOrder?deviceNo="+fix;
    });

    $(".btn2").click(function () {
    	var warranty =$("#device").text();
        window.location.href="/warrantyList?deviceNo="+warranty;
    });
});