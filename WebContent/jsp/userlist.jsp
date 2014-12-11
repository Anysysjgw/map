<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%-- Validate if user login successfully. --%>
<%@include file="validate.jsp"%>

<%-- check authority --%>
<%
	if (role != 1) {
%>
<%@include file="noAuthorityError.jsp"%>
<%
		return;
	}
	
	Boolean firstLoad = Boolean.parseBoolean(request.getParameter("firstload"));
	String pageNum = request.getParameter("pageNum");
	String totalCount = request.getParameter("totalCount");
	String pageNumShown = request.getParameter("pageNumShown");
	String searchUserName = request.getParameter("searchUserName");
	String searchRole = request.getParameter("searchRole");
	String searchCreateDate = request.getParameter("searchCreateDate");
	
	if (firstLoad == null) {
		firstLoad = false;
	}
	if (pageNum == null) {
		pageNum = "1";
	}
	if (totalCount == null) {
		totalCount = "";
	}
	if (pageNumShown == null) {
		pageNumShown = "";
	}
	if (searchUserName == null) {
		searchUserName = "";
	}
	if (searchRole == null) {
		searchRole = "0";
	}
	if (searchCreateDate == null) {
		searchCreateDate = "";
	}
%>

<script type="text/javascript">
	
	getUserList("<%=searchUserName%>", "<%=searchRole%>", "<%=searchCreateDate%>");
	
	function searchUser() {
		$("#userPageNum").val("1");
		$("#searchUserName").val($("#username").val());
		$("#searchRole").val($("#role").val());
		$("#searchCreateDate").val($("#createdate").val());
		var params = $("#gridPagerForm").serializeArray();
		navTab.reload("userlist.jsp?firstload=true", {data: params, callback: null});
		return;
	}

	function getUserList(userName, role, createDate) {
		var formData = new FormData();
		formData.append("username", userName);
		formData.append("role", role);
		formData.append("createdate", createDate);
		
		$.ajax({
			url: "searchUser",
			type: "POST",
			data: formData,
			processData: false,  // 告诉jQuery不要去处理发送的数据
			contentType: false,  // 告诉jQuery不要去设置Content-Type请求头
			success: function(responseText) {
				generateUserList(responseText);
			}
		});
	}
	
 	function generateUserList(userListArray) {
 		resetRows();
		var userList = eval(userListArray);
		
		/* Set for hidden fields */
		if (userList.length > 200) {
			$("#userTotalCount").val(200);
			$("#userPageNumShown").val(10);
		} else {
			$("#userTotalCount").val(userList.length);
			$("#userPageNumShown").val(Math.ceil(userList.length/20));
		}
		<%
			if (firstLoad) {
		%>
				var params = $("#gridPagerForm").serializeArray();
				navTab.reload("userlist.jsp", {data: params, callback: null});
				return;
		<%
			}
		%>
		
		/* Set for pager info */
		$("#userTotalCountText").text("每页最多显示20条，共计" + userList.length + "条用户数据");
		$("#userListPager").attr("totalCount", userList.length);
		$("#userListPager").attr("pageNumShown", Math.ceil(userList.length/20));
		
		var start = ($("#userPageNum").val() - 1) * 20;
		for (var i = start; i < userList.length; i++) {
			if (i - start >= 20 ) {
				continue;
			}
			/* Modify rows */
			var $tr = $("#user_" + (i - start));
			if ($tr) {
				$tr.attr("rel", userList[i].username);
				$tr.show();
				/* Get role description */
				var roleDesc;
				var role = userList[i].role;
				switch(role) {
				case 1:
					roleDesc = "系统管理员";
					break;
				case 2:
					roleDesc = "网格数据管理员";
					break;
				case 3:
					roleDesc = "营销数据管理员";
					break;
				case 4:
					roleDesc = "普通用户";
					break;
				default:
					roleDesc = "未知角色";
				}
				/* Format created date */
				var createDate = new Date(userList[i].createdate);
				var year = createDate.getFullYear().toString();
				var month = (createDate.getMonth() + 1).toString();
				var day = createDate.getDate().toString();
				if (day < 10) {
					day = "0" + day;
				}
				var createDateStr = year + "-" + month + "-" + day;
				var index = 0;
				var $tr_children = $tr.children();
				$tr_children.each(function() {
					switch (index) {
					case 0:
						$(this).text(userList[i].username);
						break;
					case 1:
						$(this).text(roleDesc);
						break;
					case 2:
						$(this).text(userList[i].realname);
						break;
					case 3:
						$(this).text(userList[i].department);
						break;
					case 4:
						$(this).text(createDateStr);
						break;
					}
					index = index + 1;
				});
			}
		}
	}
 	
 	function resetRows() {
 		for (var i = 0; i < 20; i++) {
			var $tr = $("#user_" + i);
			if ($tr) {
				$tr.attr("rel", i);
				var $tr_children = $tr.children();
				$tr_children.each(function(){
					$(this).text("");
				});
				$tr.hide();
			}
 		}
 	}
</script>

<form id="pagerForm" name="gridPagerForm" method="post" action="userlist.jsp">
	<input type="hidden" id="userPageNum" name="pageNum" value="<%=pageNum%>" />
	<input type="hidden" id="userTotalCount" name="totalCount" value="<%=totalCount%>" />
	<input type="hidden" id="userPageNumShown" name="pageNumShown" value="<%=pageNumShown%>" />
	<input type="hidden" id="searchUserName" name="searchUserName" value="<%=searchUserName%>" />
	<input type="hidden" id="searchRole" name="searchRole" value="<%=searchRole%>" />
	<input type="hidden" id="searchCreateDate" name="searchCreateDate" value="<%=searchCreateDate%>" />
</form>

<div class="pageHeader">
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<td>用户名：<input id="username" type="text" name="username" value="<%=searchUserName%>" /></td>
				<td>
					<select class="combox" id="role" name="role">
						<option value="0" <% if (searchRole.equals("0")) { out.println("selected"); } %>>所有角色</option>
						<option value="1" <% if (searchRole.equals("1")) { out.println("selected"); } %>>系统管理员</option>
						<option value="2" <% if (searchRole.equals("2")) { out.println("selected"); } %>>网格数据管理员</option>
						<option value="3" <% if (searchRole.equals("3")) { out.println("selected"); } %>>营销数据管理员</option>
						<option value="4" <% if (searchRole.equals("4")) { out.println("selected"); } %>>普通用户</option>
					</select>
				</td>
				<td>账户开通日期：<input type="text" class="date" id="createdate" name="createdate" value="<%=searchCreateDate%>" readonly="true" /></td>
			</tr>
		</table>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit" onclick="searchUser()">检索</button>
						</div>
					</div></li>
				<li><a class="button" href="useradvsearch.jsp" target="dialog"
					height="360" mask="true" title="查询框"><span>高级检索</span></a></li>
			</ul>
		</div>
	</div>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="useradd.jsp"
				target="dialog" height="360"><span>添加</span></a></li>
			<li class="line">line</li>
			<li><a id="delete" class="delete" href="deleteUser?uid={sid_user}"
				target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
			<li class="line">line</li>
			<li><a id="edit" class="edit" href="useredit.jsp?uid={sid_user}"
				target="dialog" height="360"><span>修改</span></a></li>
		</ul>
	</div>
	<table class="table" width="720" layoutH="138">
		<thead>
			<tr>
				<th align="center" width="150">用户名</th>
				<th align="center" width="150">角色</th>
				<th align="center" width="150">姓名</th>
				<th align="center" width="200">所属部门</th>
				<th align="center" width="120">账户开通日期</th>
			</tr>
		</thead>
		<tbody id="userList">
			<tr id="user_0" target="sid_user" rel="0">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_1" target="sid_user" rel="1">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_2" target="sid_user" rel="2">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_3" target="sid_user" rel="3">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_4" target="sid_user" rel="4">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_5" target="sid_user" rel="5">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_6" target="sid_user" rel="6">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_7" target="sid_user" rel="7">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_8" target="sid_user" rel="8">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_9" target="sid_user" rel="9">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_10" target="sid_user" rel="10">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_11" target="sid_user" rel="11">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_12" target="sid_user" rel="12">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_13" target="sid_user" rel="13">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_14" target="sid_user" rel="14">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_15" target="sid_user" rel="15">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_16" target="sid_user" rel="16">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_17" target="sid_user" rel="17">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_18" target="sid_user" rel="18">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="user_19" target="sid_user" rel="19">
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span id="userTotalCountText"></span>
		</div>

		<div id="userListPager" class="pagination" targetType="navTab" totalCount="<%=totalCount%>"
			numPerPage="20" pageNumShown="<%=pageNumShown%>" currentPage="<%=pageNum%>"></div>

	</div>
	
</div>
