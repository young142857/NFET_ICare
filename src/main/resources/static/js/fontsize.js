/**
 * Created by chenmk on 2017/6/21.
 */
//获取窗口的宽度，动态设置rem根字体大小的宽度
(function(){
    var dw =750;//保存视觉稿宽度
    document.documentElement.style.fontSize=document.documentElement.clientWidth/(dw/100)+'px';
})();

