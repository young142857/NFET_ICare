/**
 * Created by chenmk on 2017/6/13.
 */

$(document).ready(function () {
    //点击输入多个设备，页面处理
    $("#s12").click(function(){
        $(".equipment_one").hide();
        $(".equipment_more").show();
    });
    //点击输入单个设备，页面处理
    $("#s11").click(function () {
        $(".equipment_more").hide();
        $(".equipment_one").show();
    });
    //单个设备，点击提交按钮
    $("#submit1").click(function(){
        $.get()
    });
    //多个设备，点击提交按钮
    $("#submit2").click(function () {
        $.get()
    });
});