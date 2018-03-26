/**
 * Created by chenmk on 2017/8/22.
 */
$(".loginbtn").click(function () {
    var staffNo =$(".jobcode").val();
    var password=$(".password").val();
    if(staffNo==""){
        alert("请输入工号！");
    }else if(password==""){
        alert("请输入密码！");
    }else{
        $.ajax({
            type:"GET",
            url:"/mgr/login",
            data:{
                staffNo :$(".jobcode").val(),
                password:$(".password").val()
            },
            success:function (data) {
                if(data.errorCode==1){
                    alert(data.desc);
                }else if (data.roleId==1){
                    window.location.href="/page/marketdepartment";
                }else{
                    window.location.href="/page/trafficstatistics";
                }
            }
        })
    }
});
