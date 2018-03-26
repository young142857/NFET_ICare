/**
 * Created by chenmk on 2017/9/7.
 */
$(document).ready(function() {

	$("#updata").click(function(event) {
		$("#errorInfo").text("");

		// stop submit the form, we will post it manually.
		event.preventDefault();

		fire_ajax_submit();

	});

});

function fire_ajax_submit() {

	// Get form
	var form = $('#fileUploadForm')[0];
	// 校验文件的后缀名
	var fileName = $('#choose').val();
	var index=fileName.lastIndexOf("."); 
	var nameLength = fileName.length;
	var ext = fileName.substring(index,nameLength);
	if(ext != ".xlsx" && ext != ".xls"){
		//alert("文件格式不是Excel，请重新上传");
		//$("#errorInfo").text("错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：文件格式不是Excel，请重新上传！");
		$("#errorInfo").text("错误信息：文件格式不是Excel，请重新上传！");
		return;
	}	
	var data = new FormData(form);

	var phone = sessionStorage.getItem("phone");
	var fileType = sessionStorage.getItem("fileType");

	data.append("fileType", fileType);

	data.append("phone", phone);

	$("#updata").prop("disabled", true);

	$.ajax({
		type : "POST",
		enctype : 'multipart/form-data',
		url : "/mgr/upload",
		data : data,
		// http://api.jquery.com/jQuery.ajax/
		// http://developer.mozilla.org/en-US/docs/Web/API/FormData/Using_FormData_Objects
		processData : false, // prevent jQuery from automatically
		// transforming the data into a query string
		contentType : false,
		cache : false,
		timeout : 600000,
		success : function(data) {
			console.log(data);
			if (data.errorType == "" && data.errorLine == "") {
				$("#errorInfo").text("数据上传完毕！");				
				var choosevalue =$("#choose").val();
				if (choosevalue!="") {
					/*$("#updata").unbind();*/
					$(".progress-bar").animate({width:"526px"},function () {
						$(this).text("100%");
					}); 
				}

			} else {
				// 错误提示语
				var tip = data.errorType;
				if (data.errorLine != "" && data.errorLine != null) {
					// 遍历错误的行数
					tip = tip + "第";
					var errorList = data.errorLine;
					for (var i = 0; i < errorList.length; i++) {
						// tip追加具体的错误行数
						if(i != errorList.length-1) {							
							tip = tip + errorList[i]+"、";
						}
						else {
							tip = tip + errorList[i]+"行有错。";
						}
					}
				}
				//alert(tip);
				//$("#errorInfo").text("错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息：错误信息："+tip);
				$("#errorInfo").append("数据上传完毕！"+'<br/>'+"错误信息："+tip);
			}
			$("#result").text(data);
			console.log("SUCCESS : ", data);
			$("#updata").prop("disabled", false);

		},
		error : function(e) {

			$("#result").text(e.responseText);
			console.log("ERROR : ", e);
			$("#updata").prop("disabled", false);

		}
	});
}
