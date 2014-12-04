<%@ include file="/WEB-INF/views/common/global.jsp" %>

<s:url var="resources" value="/ui" />

<script type="text/javascript" src="${resources}/jslib/js/jquery.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/js/jquery.uniform.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/js/select2.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/js/datatables.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/js/tabletools.js"></script>
<script type="text/javascript" src="${resources}/jslib/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${resources}/js/AppConfig.js"></script>

<script type="text/javascript">
   AppConfig.contextPath='<c:out value="${pageContext.servletContext.contextPath}" />';
</script>
