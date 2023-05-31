<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2023/5/19
  Time: 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件上传</title>
</head>
<body>
    <form action="/web/upload" method="post" enctype="multipart/form-data">
        <p>
            选择文件：<input type="file" name="file"><br/>
            上传路径：<input type="text" name="path">
        </p>
        <input type="hidden" name="id" value="">
        <input type="submit" value="上传">
    </form>

</body>
</html>
