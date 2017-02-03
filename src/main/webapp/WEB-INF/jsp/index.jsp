<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Deeplearing4J VGG-16</title>
<script
  src="https://code.jquery.com/jquery-2.2.4.min.js"
  integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
  crossorigin="anonymous"></script>
</head>
<body>

<div>
	<h1>Web App with VGG-16 using Deeplearing4J</h1>
</div>

<div class="col-md-7">
	<div>
		<h3>Choose File</h3>
		<form id="upload-file-form">
			<input id="upload-file-input" type="file" name="uploadfile" accept="*" />
		</form>
	</div>
	
	<hr/>
	
	<div>
		<img style="border-style: hidden;" id="uploaded-img" src="" />
	</div>
	
	<hr/>
	
	<div>
		<h3>Result</h3>
	</div>
</div>
</body>

<script>
$(document).ready(function() {
	$("#upload-file-input").on("change", uploadFile);
});
</script>

<script>
var uploadFile = function() {
	var fileName = $('#upload-file-input').get(0).files[0].name;
	
	 $('#uploaded-img').attr('src', window.URL.createObjectURL($('#upload-file-input').get(0).files[0]));
	
	$.ajax({
		url : "/uploadFile",
		type : "POST",
		data : new FormData($("#upload-file-form")[0]),
		enctype : 'multipart/form-data',
		processData : false,
		contentType : false,
		cache : false,
		success : function() {
			$.ajax({
				url : "/infer?fileName=" + fileName,
				type : "GET",
				dataType: 'json', 
				success: function(data) {
					console.debug(data);
				},
				error: function() {
				}
			});
		},
		error : function() {
		}
	});
};

</script>

</html>