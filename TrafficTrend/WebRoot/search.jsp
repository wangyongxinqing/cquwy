<%@ page contentType="text/html;charset=utf-8"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/js/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.6.4/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#ip").click(function(){
		$("#selip").toggle();
	});
	
	//找到下拉框
	var deviceNameSelect=$(".seldevice").children("select");
	var portNameSelect=$(".selport").children("select");
	var ipNameSelect=$(".seloltip").children("select");
	//给下拉框注册事件
	deviceNameSelect.change(function(){
		//获取一级下拉框的值
		var deviceName=$(this).val();
		if(deviceName!=''){
			var type=$("input[name='type']").val();
			$.ajax({
				type:"post",
				traditional:true,
				url:'${pageContext.request.contextPath}/searchChart',  
	            data:{"device":deviceName,"step":"second","type":type},
	            success:function(data){ 
	            	var p=eval(data);
	            	portNameSelect.html("");
	            	ipNameSelect.html("");
	            	$("<option >----------选择端口----------</option>").appendTo(portNameSelect);
	            	$("<option >----------选择管理IP----------</option>").appendTo(ipNameSelect);
					$(p).each(function(key,value){
						$("<option value='"+value['port']+"'>"+value['port']+"</option>").appendTo(portNameSelect);
						$("<option value='"+value['ip']+"'>"+value['ip']+"</option>").appendTo(ipNameSelect);
					});
	            }
			});
		}
	});
	
	$("#statistics").click(function(){
		var device=$(".device").val();
		var port=$(".port").val();
		var ip=$(".ip").val();
		var showtype=$(".showtype").val();
		var type=$("input[name='type']").val();   //1表示上行口，2表示下行口
		var fromtime=$("input[name='fromtime']").val();
		var totime=$("input[name='totime']").val();
		$.ajax({
			type:"post",
			traditional:true,
			url:'${pageContext.request.contextPath}/searchChart',  
            data:{"device":device,"port":port,"ip":ip,"fromtime":fromtime,"totime":totime,"showtype":showtype,"type":type,"step":"third"},
            success:function(data){ 
            	$("#showimage").attr("src",data);
            }
		});
	});
});
function selectAll(obj,str){
	var allElements=document.getElementsByName(str);
	var len=allElements.length;
	for(var i=0;i<len;i++)
		allElements[i].checked=obj.checked;
}
</script>
</head>
<title>趋势图</title>
<body align="center">
<form action="${pageContext.request.contextPath}/searchChart?step=third" method="post">
	<table class="table">
		<tr>
			<td colspan="6">选择查询条件</td>
		</tr>
		<tr>
			<td>OLT名称</td>
			<td>
				<span class="seldevice">
					<select name="device" class="device">
						<option >----------选择设备名称----------</option>
						<c:forEach items="${deviceNames }" var="device">
							<option value="${device }">${device }</option>
						</c:forEach>
					</select>
				</span>
			</td>
			<td>OLT端口</td>
			<td>
				<span class="selport">
					<select name="port" class="port">
						<option >----------选择端口----------</option>
					</select>
				</span>
			</td>
			<td>OLT管理IP</td>
			<td>
				<span class="seloltip">
					<select name="ip" class="ip">
						<option >----------选择管理IP----------</option>
					</select>
				</span>
			</td>
		</tr>
		<tr>
			<td>统计时间</td>
			<td>
				从<input type="text" name="fromtime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="readonly"/>
				到<input type="text" name="totime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="readonly"/>
			</td>
			<td>查看方式</td>
			<td>
				<select name="showtype" class="showtype">
					<option value="1">按日</option>
					<option value="2">按周</option>
					<option value="3">按月</option> 
				</select>
			</td>
			<td></td>
			<td></td>
		</tr>
		<tr align="center">
			<td colspan="6">
			<input type="button" value="统计查询" class="btn btn-primary" id="statistics">
			<input type="hidden" value="${type }" name="type"/>
			</td>
		</tr>
	</table>
</form>
<img id="showimage" width=420 height=300 border=0>
</body>
</html>