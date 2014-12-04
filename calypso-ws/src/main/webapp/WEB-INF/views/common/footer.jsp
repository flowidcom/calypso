<%@ include file="/WEB-INF/views/common/global.jsp"%>

<%-- common site footer --%>

<jsp:useBean id="date" scope="page" class="java.util.Date" />
<s:url var="logoutURL" value="/logout" />

<footer id="footer">
	<div id="footer_content" class="footer_container">
		<fmt:formatDate value="${date}" pattern="yyyy" />
	</div>
</footer>
