$("#sure").click(
		function () {
			var userno = $("#userno").text();
			$.ajax({
				type:"GET",
		        url:"/giveScore",
				data:{
					"userNo":userno
				},
				success:function(code){
					if(code == 0){
						window.location.href="/page/mydataregistered";						
					}
				}
			})
});