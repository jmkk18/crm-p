<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
<script type="text/javascript">
	//解决日期中文乱码问题
	(function($){
		$.fn.datetimepicker.dates['zh-CN'] = {
			days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
			daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
			daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
			months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			today: "今天",
			clear: "清空",
			suffix: [],
			meridiem: ["上午", "下午"]
		};
	}(jQuery));
</script>

<script type="text/javascript">
	//处理回车错误
	$(window).keydown(function (e){
		if(e.keyCode==13){
			return false;
		}
	});

	$(function(){
		//对容器调用bs_datetimepicker工具函数
		$(".mydate").datetimepicker({
			format:"yyyy-mm-dd",//日期的格式
			minView:"month",//可以选择的最小视图
			autoclose:true,//设置选择完日期或者时间之后，是否自动关闭日历
			language:"zh-CN",//语言
			initialDate:new Date(),//初始化显示的日期
			todayBtn:true,//设置是否显示"今天"按钮,默认是false
			clearBtn:true//设置是否显示"清空"按钮，默认是false
		});

		//客户名称自动补全
		$("#create-customerName").typeahead({
			source:function (jquery,process) {
				//每次键盘弹起，都自动触发本函数；我们可以向后台送请求，查询客户表中所有的名称，
				//把客户名称以[]字符串形式返回前台，赋值给source
				//process：是个函数，能够将['xxx','xxxxx','xxxxxx',.....]字符串赋值给source，从而完成自动补全
				//jquery：在容器中输入的关键字
				//var customerName=$("#customerName").val();
				//发送查询请求
				$.ajax({
					url:'workbench/transaction/queryCustomerNameByName.do',
					data:{
						name:jquery
					},
					type:'post',
					dataType:'json',
					success:function (data) {//['xxx','xxxxx','xxxxxx',.....]
						process(data);
					}
				});
			}
		});

		//给"阶段"下拉框添加change事件
		$("#create-stage").change(function (){
			//获取参数
			var stageValue=$("#create-stage option:selected").text();
			//表单验证
			if(stageValue==""){
				$("#create-possibility").val("");
				return;
			}
			//发送ajax请求
			$.ajax({
				url:'workbench/transaction/getPossibilityByStage.do',
				data:{
					stageValue:stageValue
				},
				type:'post',
				dataType:'json',
				success:function (data){
					//把可能性显示在输入框
					$("#create-possibility").val(data);
				}
			});
		});

		//给"市场活动源"搜索按钮添加单击事件
		$("#searchActivityBtn").click(function () {
			//初始化工作
			//清空搜索框
			$("#searchActivityTxt").val("");
			//清空搜索列表
			$("#activityTbody").html("");

			//弹出查找市场活动的模态窗口
			$("#findMarketActivity").modal("show");
		});

		//给"市场活动源"搜索框添加键盘弹起事件
		$("#searchActivityTxt").keyup(function () {
			//收集参数
			var activityName=this.value;
			//发送请求
			$.ajax({
				url:'workbench/transaction/queryActivityByFuzzyName.do',
				data:{
					activityName:activityName,
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					//遍历data,显示所有搜索的市场活动
					var htmlStr="";
					$.each(data,function (index,obj) {
						htmlStr+="<tr>";
						htmlStr+="	<td><input type=\"radio\" id=\""+obj.id+"\" activityName=\""+obj.name+"\" name=\"activity\"/></td>";
						htmlStr+="	<td>"+obj.name+"</td>";
						htmlStr+="	<td>"+obj.startDate+"</td>";
						htmlStr+="	<td>"+obj.endDate+"</td>";
						htmlStr+="	<td>"+obj.owner+"</td>";
						htmlStr+="</tr>";
					});
					$("#activityTbody").html(htmlStr);
				}
			});
		});

		//给所有市场活动的单选按钮添加单击事件
		$("#activityTbody").on("click","input[type='radio']",function (){
			//收集参数
			var activityId=this.value;
			var activityName=$(this).attr("activityName");
			//把市场活动的id写到隐藏域，把name写到输入框中
			$("#create-activityId").val(activityId);
			$("#create-activityName").val(activityName);
			//关闭市场活动的模态窗口
			$("#findMarketActivity").modal("hide");
		});

		//给"联系人名称"搜索按钮添加单击事件
		$("#searchContactsBtn").click(function () {
			//初始化工作
			//清空搜索框
			$("#searchContactsTxt").val("");
			//清空搜索列表
			$("#contactsTbody").html("");

			//弹出查找联系人的模态窗口
			$("#findContacts").modal("show");
		});

		//给"联系人名称"搜索框添加键盘弹起事件
		$("#searchContactsTxt").keyup(function () {
			//收集参数
			var contactsName=this.value;
			//发送请求
			$.ajax({
				url:'workbench/transaction/queryContactsByFuzzyName.do',
				data:{
					contactsName:contactsName,
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					//遍历data,显示所有搜索的联系人
					var htmlStr="";
					$.each(data,function (index,obj) {
						htmlStr+="<tr>";
						htmlStr+="	<td><input type=\"radio\" id=\""+obj.id+"\" contactsName=\""+obj.fullname+"\" name=\"activity\"/></td>";
						htmlStr+="	<td>"+obj.fullname+"</td>";
						htmlStr+="	<td>"+obj.email+"</td>";
						htmlStr+="	<td>"+obj.mphone+"</td>";
						htmlStr+="</tr>";
					});
					$("#contactsTbody").html(htmlStr);
				}
			});
		});

		//给所有联系人名称的单选按钮添加单击事件
		$("#contactsTbody").on("click","input[type='radio']",function (){
			//收集参数
			var contactsId=this.value;
			var contactsName=$(this).attr("contactsName");
			//把市场活动的id写到隐藏域，把name写到输入框中
			$("#create-contactsId").val(contactsId);
			$("#create-contactsName").val(contactsName);
			//关闭市场活动的模态窗口
			$("#findContacts").modal("hide");
		});

		//给"保存"按钮添加单击事件
		$("#saveCreateTranBtn").click(function () {
			//收集参数
			var owner=$("#create-owner").val();
			var money=$.trim($("#create-money").val());
			var name=$.trim($("#create-name").val());
			var expectedDate=$("#create-expectedDate").val();
			var customerName=$.trim($("#create-customerName").val());
			var stage=$("#create-stage").val();
			var type=$("#create-type").val();
			var source=$("#create-source").val();
			var activityId=$("#create-activityId").val();
			var contactsId=$("#create-contactsId").val();
			var description=$.trim($("#create-description").val());
			var contactSummary=$.trim($("#create-contactSummary").val());
			var nextContactTime=$("#create-nextContactTime").val();
			//表单验证
			//money只能是非负整数
			var regExp=/^(([1-9]\d*)|0)$/;
			if(money!=""){
				if(!regExp.test(money)){
					alert("money只能是非负整数");
					return;
				}
			}
			if(name==""){
				alert("名字不能为空");
				return;
			}
			if(expectedDate==""){
				alert("预计成交日期不能为空");
				return;
			}
			if(customerName==""){
				alert("客户名称不能为空");
				return;
			}
			if(stage==""){
				alert("状态不能为空");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/transaction/saveCreateTran.do',
				data:{
					owner:owner,
					money:money,
					name:name,
					expectedDate:expectedDate,
					customerName:customerName,
					stage:stage,
					type:type,
					source:source,
					activityId:activityId,
					contactsId:contactsId,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code=="1"){
						//跳转到交易主页面
						window.location.href="workbench/transaction/index.do";
					}else{
						//提示信息
						alert(data.message);
					}
				}
			});
		});

	});
</script>
</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input id="searchActivityTxt" type="text" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="activityTbody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">·</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input id="searchContactsTxt" type="text" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="contactsTbody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="saveCreateTranBtn" type="button" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-owner">
				  <c:forEach items="${requestScope.userList}" var="u">
					  <option value="${u.id}">${u.name}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-money" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-money">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-name">
			</div>
			<label for="create-expectedDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control mydate" id="create-expectedDate" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-customerName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-stage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-stage">
			  	<option></option>
			    <c:forEach items="${requestScope.stageList}" var="s">
			  	  <option value="${s.id}">${s.value}</option>
			    </c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-type" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-type">
				  <option></option>
					<c:forEach items="${requestScope.transactionTypeList}" var="tt">
						<option value="${tt.id}">${tt.value}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-source" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-source">
				  <option></option>
					<c:forEach items="${requestScope.sourceList}" var="so">
						<option value="${so.id}">${so.value}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-activityName" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="searchActivityBtn"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="create-activityId">
				<input type="text" class="form-control" id="create-activityName">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" id="searchContactsBtn"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="create-contactsID">
				<input type="text" class="form-control" id="create-contactsName">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-description" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-description"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control mydate" id="create-nextContactTime" readonly>
			</div>
		</div>
		
	</form>
</body>
</html>