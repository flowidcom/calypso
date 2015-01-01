<%@ include file="/WEB-INF/views/common/global.jsp" %>

<s:url var="resources" value="/ui" />

<script type="text/javascript" src="${resources}/jslib/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/uniform/jquery.uniform.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/datatables/datatables.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/datatables/datatables.bootstrap.js"></script>
<script type="text/javascript" src="${resources}/jslib/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="${resources}/jslib/metisMenu/metisMenu.js"></script>
<script type="text/javascript" src="${resources}/jslib/tabletools/tabletools.js"></script>
<script type="text/javascript" src="${resources}/jslib/sbadmin2/sb-admin-2.js"></script>

<script type="text/javascript" src="${resources}/ts/AppConfig.js"></script>
<script type="text/javascript">
   AppConfig.contextPath='<c:out value="${pageContext.servletContext.contextPath}" />';
</script>
