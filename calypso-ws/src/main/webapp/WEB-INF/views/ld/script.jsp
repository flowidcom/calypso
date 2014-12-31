<%@ include file="/WEB-INF/views/common/global.jsp" %>

<s:url var="resources" value="/ui" />

<script type="text/javascript" src="${resources}/jslib/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/uniform/jquery.uniform.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/select2/select2.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/datatables/datatables.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/tabletools/tabletools.js"></script>
<script type="text/javascript" src="${resources}/jslib/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="${resources}/js/AppConfig.js"></script>

<script type="text/javascript">
   AppConfig.contextPath='<c:out value="${pageContext.servletContext.contextPath}" />';
</script>
