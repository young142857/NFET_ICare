$(".midbtn").click(function(){
	var warranty=$("#device").text();
	window.location.href="/warrantyList?deviceNo="+warranty;
});

$(".declaration").click(function () {
    window.location.href="/page/Warrantystatement";
});