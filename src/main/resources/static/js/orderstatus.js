/**
 * Created by chenmk on 2017/8/4.
 */

/**
 *进度条处理
 * 报修步骤进度处理
 */
 $(function () {
     $.ajax({
         url:"/fixSchedule",
         data:{
        	"fixId":$("#fixId").text() 
         },
         dataType:"JSON",
         success:function (statusObj) {
             var status=statusObj.status;
/**
* status=001时，取值
*/
             //原始年月日时分秒
             var datatime =statusObj.data[0].time;
             //原始年月日
             var date= datatime.split(" ")[0];
             //时分秒
             var time= datatime.split(" ")[1];
             //截取的原始月日值
             var lineDate=date.split("-");
             //拼接的月日值
             var newDate=lineDate[1]+"月"+lineDate[2]+"日";
     
/**
* 对status的值进行判定处理
* status==001判断处理
* status==002判断处理
* status==003判断处理
* status==004判断处理
*/             
             if (status==001){
                 //status==001时，进度条推进；相应处理变化
                 $(".red").attr("class","redwt1");
                 $("#time_1").attr("class","datesp1");
                 $("#date_1").attr("class","datesp2");
                 $(".status_one").attr("class","spanbox");
                 $("#date_1").text(newDate);
                 $("#time_1").text(time);
             }else if(status==002){
/**
* status=002时，取值
*/
                 //原始年月日时分秒
                 var datatime =statusObj.data[1].time;
                 //原始年月日
                 var date_two= datatime.split(" ")[0];
                 //时分秒
                 var time_two= datatime.split(" ")[1];
                 //截取的原始月日值
                 var lineDate_two=date.split("-");
                 //拼接的月日值
                 var newDate_two=lineDate_two[1]+"月"+lineDate_two[2]+"日";
                 //status==001时，进度条推进；相应处理变化
                 $("#time_1").attr("class","datesp1");
                 $("#date_1").attr("class","datesp2");
                 $(".status_one").attr("class","spanbox");
                 $("#date_1").text(newDate);
                 $("#time_1").text(time);
                 //status==002时，进度条推进；相应处理变化
                 $(".red").attr("class","redwt2");
                 $("#time_2").attr("class","datesp1");
                 $("#date_2").attr("class","datesp2");
                 $(".status_two").attr("class","spanbox");
                 $(".note2").replaceWith("<img class='notex' src='/image/orderstatus/1.png'/>");
                 $("#date_2").text(newDate_two);
                 $("#time_2").text(time_two);
             }else if(status==003){
/**
 * status=002时，取值
*/
                 //原始年月日时分秒
                 var datatime =statusObj.data[1].time;
                 //原始年月日
                 var date_two= datatime.split(" ")[0];
                 //时分秒
                 var time_two= datatime.split(" ")[1];
                 //截取的原始月日值
                 var lineDate_two=date.split("-");
                 //拼接的月日值
                 var newDate_two=lineDate_two[1]+"月"+lineDate_two[2]+"日";
/**
 * status=003时，取值
*/
                 //原始年月日时分秒
                 var datatime =statusObj.data[2].time;
                 //原始年月日
                 var date_three= datatime.split(" ")[0];
                 //时分秒
                 var time_three= datatime.split(" ")[1];
                 //截取的原始月日值
                 var lineDate_three=date.split("-");
                 //拼接的月日值
                 var newDate_three=lineDate_three[1]+"月"+lineDate_three[2]+"日";
                 //status==001时，进度条推进；相应处理变化
                 $("#time_1").attr("class","datesp1");
                 $("#date_1").attr("class","datesp2");
                 $(".status_one").attr("class","spanbox");
                 $("#date_1").text(newDate);
                 $("#time_1").text(time);
                 //status==002时，进度条推进；相应处理变化
                 $("#time_2").attr("class","datesp1");
                 $("#date_2").attr("class","datesp2");
                 $(".status_two").attr("class","spanbox");
                 $(".note2").replaceWith("<img class='notex' src='/image/orderstatus/1.png'/>");
                 $("#date_2").text(newDate_two);
                 $("#time_2").text(time_two);
                 //status==003时，进度条推进；相应处理变化
                 $(".red").attr("class","redwt3");
                 $("#time_3").attr("class","datesp1");
                 $("#date_3").attr("class","datesp2");
                 $(".status_three").attr("class","spanbox");
                 $(".note3").replaceWith("<img class='notex' src='/image/orderstatus/1.png'/>");
                 $("#date_3").text(newDate_three);
                 $("#time_3").text(time_three);
             }else{
/**
* status=002时，取值
*/
                 //原始年月日时分秒
                 var datatime =statusObj.data[1].time;
                 //原始年月日
                 var date_two= datatime.split(" ")[0];
                 //时分秒
                 var time_two= datatime.split(" ")[1];
                 //截取的原始月日值
                 var lineDate_two=date.split("-");
                 //拼接的月日值
                 var newDate_two=lineDate_two[1]+"月"+lineDate_two[2]+"日";
/**
 * status=003时，取值
*/
                 //原始年月日时分秒
                 var datatime =statusObj.data[2].time;
                 //原始年月日
                 var date_three= datatime.split(" ")[0];
                 //时分秒
                 var time_three= datatime.split(" ")[1];
                 //截取的原始月日值
                 var lineDate_three=date.split("-");
                 //拼接的月日值
                 var newDate_three=lineDate_three[1]+"月"+lineDate_three[2]+"日";
/**
* status=004时，取值
*/
                 //原始年月日时分秒
                 var datatime =statusObj.data[3].time;
                 //原始年月日
                 var date_four= datatime.split(" ")[0];
                 //时分秒
                 var time_four= datatime.split(" ")[1];
                 //截取的原始月日值
                 var lineDate_four=date.split("-");
                 //拼接的月日值
                 var newDate_four=lineDate_four[1]+"月"+lineDate_four[2]+"日";
                 //status==001时，进度条推进；相应处理变化
                 $("#time_1").attr("class","datesp1");
                 $("#date_1").attr("class","datesp2");
                 $(".status_one").attr("class","spanbox");
                 $("#date_1").text(newDate);
                 $("#time_1").text(time);
                 //status==002时，进度条推进；相应处理变化
                 $("#time_2").attr("class","datesp1");
                 $("#date_2").attr("class","datesp2");
                 $(".status_two").attr("class","spanbox");
                 $(".note2").replaceWith("<img class='notex' src='/image/orderstatus/1.png'/>");
                 $("#date_2").text(newDate_two);
                 $("#time_2").text(time_two);
                 //status==003时，进度条推进；相应处理变化
                 $("#time_3").attr("class","datesp1");
                 $("#date_3").attr("class","datesp2");
                 $(".status_three").attr("class","spanbox");
                 $(".note3").replaceWith("<img class='notex' src='/image/orderstatus/1.png'/>");
                 $("#date_3").text(newDate_three);
                 $("#time_3").text(time_three);
                 //status==004时，进度条推进；相应处理变化
                 $(".red").attr("class","redwt4");
                 $("#time_4").attr("class","datesp1");
                 $("#date_4").attr("class","datesp2");
                 $(".status_four").attr("class","spanbox");
                 $(".note4").replaceWith("<img class='notex' src='/image/orderstatus/1.png'/>");
                 $("#date_4").text(newDate_four);
                 $("#time_4").text(time_four);
             }
         }
     })
 });