<%@ page contentType="text/html;charset=utf-8"%>
<%@page import="java.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/js/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
<script type="text/javascript">

	function checkform(){
	    if(document.getElementById('excelAddr').value==""){
			  alert("路径不能为空，请选择文件！");		  
			  document.getElementById('excelAddr').focus();
			  return false;	  
		}else{
			return true;
		}
	}
	function check(path){
		var filepath=path;
		filepath=filepath.substring(filepath.lastIndexOf('.')+1,filepath.length);
		if(filepath != 'xls')
			var file = $("#excelAddr");
			file.after(file.clone().val(""));
			file.remove();
			alert("只能上传XLS格式的图片");
	}

	function submitIndexForm(){
		var flag=document.getElementById("flag").value;
		var form=document.getElementById("indexForm");
		if(flag=='0' || flag=='1')
			form.action="${pageContext.request.contextPath }/showAllTask?flag="+flag;
		else if(flag=='2' || flag=='3')
			form.action="showBookOrUnbook.jsp?flag="+flag;
		form.submit();
	}
</script>

</head>
<title>信息导入</title>
<body><br><br><br>
<table class="table" align="center">
	<tbody>
		<tr>
			<td>
			<form action="${pageContext.request.contextPath }/dealOLTTrafficUpload" method="post" enctype="multipart/form-data" onsubmit="return checkform()">
				<%-- 类型enctype用multipart/form-data，这样可以把文件中的数据作为流式数据上传，不管是什么文件类型，均可上传。--%>
				请选择要上传<font color="red">OLT流量及带宽</font>的Excel文件<input type="file" name="excelAddr" id="excelAddr" value="" accept=".xls" onchange="check(this.value)">	 
				<br><input type="submit" value="提交" class="btn btn-primary">
			</form>
			</td>
			<td >
			<form action="${pageContext.request.contextPath }/dealOLTUpWidthUpload" method="post" enctype="multipart/form-data" onsubmit="return checkform()">
				<%-- 类型enctype用multipart/form-data，这样可以把文件中的数据作为流式数据上传，不管是什么文件类型，均可上传。--%>
				请选择要上传<font color="red">OLT上行端口带宽</font>的Excel文件<input type="file" name="excelAddr" id="excelAddr" value="" accept=".xls" onchange="check(this.value)">	 
				<br><input type="submit" value="提交" class="btn btn-primary">
			</form>
			</td>
			<td >
			<form action="${pageContext.request.contextPath }/dealOLTDownWidthUpload" method="post" enctype="multipart/form-data" onsubmit="return checkform()">
				<%-- 类型enctype用multipart/form-data，这样可以把文件中的数据作为流式数据上传，不管是什么文件类型，均可上传。--%>
				请选择要上传<font color="red">OLT下行端口带宽</font>的Excel文件<input type="file" name="excelAddr" id="excelAddr" value="" accept=".xls" onchange="check(this.value)">	 
				<br><input type="submit" value="提交" class="btn btn-primary">
			</form>
			</td>
		</tr>
		<tr>
			<td >
			<form action="${pageContext.request.contextPath }/searchChart?step=first" method="post">
				<select name="type">
					<option value="1">OLT上行口流量监控趋势图</option>
					<option value="2">OLT下行口流量监控趋势图</option>
				</select>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交" class="btn btn-primary">
			</form>
			</td>
		</tr>
	</tbody>
</table>
</body>
</html>