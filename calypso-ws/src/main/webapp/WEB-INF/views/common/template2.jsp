<!DOCTYPE html>
<%@ include file="/WEB-INF/views/common/global.jsp"%>

<%
    // No caching to avoid keeping the javascript code in the  
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Expires", "0");
%>

<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1">
<title>Calypso</title>

<tiles:insertAttribute name="style" />
</head>

<body>
    <tiles:insertAttribute name="header" />

    <tiles:insertAttribute name="sidebar" />

    <tiles:insertAttribute name="right-col" />

    <tiles:insertAttribute name="script" ignore="true" />
</body>

</html>
