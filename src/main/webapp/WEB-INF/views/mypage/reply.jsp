<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <style type="text/css">
        .row {
            margin: 0px auto;
            width:700px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row">
        <h3 class="text-center">답변하기</h3>
        <form method="post" action="reply_ok.do">
            <table class="table table-hover">
                <tr>
                    <th class="danger text-right" width=15%>이름</th>
                    <td width=85%>
                        <input type=text name=name size=15 class="input-sm">
                        <input type=hidden name=pno value="${no }">
                    </td>
                </tr>
                <tr>
                    <th class="danger text-right" width=15%>제목</th>
                    <td width=85%>
                        <input type=text name=subject size=45 class="input-sm">
                    </td>
                </tr>
                <tr>
                    <th class="danger text-right" width=15%>내용</th>
                    <td width=85%>
                        <textarea rows="10" cols="50" name=content></textarea>
                    </td>
                </tr>
                <tr>
                    <th class="danger text-right" width=15%>비밀번호</th>
                    <td width=85%>
                        <input type=password name=pwd size=10 class="input-sm">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="text-center">
                        <input type=submit value=답변 class="btn btn-sm btn-primary">
                        <input type=button value=취소 class="btn btn-sm btn-primary"
                               onclick="javascript:history.back()"
                        >
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>
