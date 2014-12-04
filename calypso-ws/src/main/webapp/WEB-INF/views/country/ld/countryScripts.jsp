<%@ include file="/WEB-INF/views/common/global.jsp"%>
<%@ include file="/WEB-INF/views/ld/script.jsp"%>

<s:url var="resources" value="/ui" />

<script type="text/javascript" src="${resources}/ts/LondiniumTheme.js"></script>
<script type="text/javascript" src="${resources}/ts/GEntity.js"></script>
<script type="text/javascript" src="${resources}/ts/GRepository.js"></script>
<script type="text/javascript" src="${resources}/ts/EntityEditor.js"></script>
<script type="text/javascript" src="${resources}/ts/Country.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		LondiniumTheme.init();
		// the rest service is relative to the UI. See web.xml for details.
		var url = "../rs/countries";
		var ajaxCountryRepository = new Country.AjaxCountryRepository(url);
		var countriesUI = new EntityEditor.EntityEditorUI(new Country.CountryMetadata(), ajaxCountryRepository);
	});
</script>
