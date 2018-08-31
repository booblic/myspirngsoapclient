<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="path" value="${pageContext.request.contextPath}"></c:set>

<html>

<head>
</head>

<body>

    <form action="${path}/getfile" method="POST">
        <p><label>Get File?</label></p>
        <p><input type="text" name="name"/></p>
        <p><button type="submit">Get</button></p>
    </form>

</body>

</html>