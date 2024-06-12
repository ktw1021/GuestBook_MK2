<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import= "himedia.GuestBookDao" %>
<%@ page import = "java.util.List" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>방명록</title>
<script type="text/javascript"> 
	function deleteData(no) {
	    if (confirm("정말로 삭제합니까?")) {
	        let password = prompt("비밀번호를 입력하세요:");
	        if (password != null) {
	            // 폼 생성
	            var form = document.createElement("form");
	            form.setAttribute("method", "post");
	            form.setAttribute("action", "guestbook?action=delete&no=" + no);

	            // 히든 필드 (비밀번호)
	            var passwordField = document.createElement("input");
	            passwordField.setAttribute("type", "hidden");
	            passwordField.setAttribute("name", "password");
	            passwordField.setAttribute("value", password);

	            // 폼에 히든 필드 추가 후 전송
	            form.appendChild(passwordField);
	            document.body.appendChild(form);
	            form.submit();
	        }
	    } else {
	        alert("삭제를 취소했습니다.");
	    }
	}

	function handleDeleteResponse(response) {
	    if (response === -1) {
	        alert("비밀번호가 일치하지 않습니다.");
	    } else if (response === 0) {
	        alert("삭제할 항목을 찾을 수 없습니다.");
	    } else {
	        alert("삭제되었습니다.");
	        location.reload(); // 페이지 새로고침
	    }
	}

</script>
</head>
<body>
	<form action="guestbook" method="post">
	<input type="hidden" name="action" value="add">
	<table border=1 width=500>
		<tr>
			<td>이름</td><td><input type="text" name="name"></td>
			<td>비밀번호</td><td><input type="password" name="password"></td>
		</tr>
		<tr>
			<td colspan=4><textarea name="content" cols=60 rows=5></textarea></td>
		</tr>
		<tr>
			<td colspan=4 align=right><input type="submit" VALUE=" 확인 "></td>
		</tr>
	</table>
	</form>
	<br/>
	<table width=510 border=1>
	<%
		GuestBookDao dao = new GuestBookDao();
		List<String[]> list = dao.getList();
		for (String [] entry : list) {
	%>
	
		<tr>
			<td>[<%= entry[0]%>]</td>
			<td><%= entry[1] %></td>
			<td><%= entry[2] %></td>
			<td><a href="javascript:deleteData('<%=entry[0]%>')">삭제</a> | <a href="guestbook?action=edit&no=<%=entry[0] %>">수정</a></td>
		</tr>
		<tr>
			<td colspan=4><%= entry[3] %></td>
		</tr>
	<%
		}
	%>
	</table>
</body>
</html>
