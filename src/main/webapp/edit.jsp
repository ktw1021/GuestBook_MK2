<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import = "java.util.Map" %>
<html>
<head>
<meta charset="UTF-8">
<title>Edit</title>
</head>
<body>
<%
	Map<String, String> entry = (Map<String, String>) request.getAttribute("entry");
%>
<form action="guestbook" method="post">
	<input type="hidden" name="action" value="update">
	<input type="hidden" name="no" value="<%= entry.get("no") %>">
	<table border=1 width=500>
		<tr>
			<td>이름</td><td><input type="text" name="name" value="<%= entry.get("name") %>"></td>
		</tr>
		<tr>
			<td>비밀번호</td><td><input type="password" name="password"></td>
		</tr>
		<tr>
			<td colspan=4><textarea name="content" cols=60 rows=5><%= entry.get("content") %></textarea></td>
		</tr>
		<tr>
			<td colspan=4 align=right><input type="submit" VALUE=" 수정 "></td>
		</tr>
	</table>
</form>
</body>
</html>
