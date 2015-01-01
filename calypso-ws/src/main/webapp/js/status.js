$(document).ready(function () {
        AppTheme.ThemeInit.init();
        // the rest service is relative to the UI. See web.xml for details.
        var url = "../rs/countries";
        var ajaxCountryRepository = new Country.AjaxCountryRepository(url);
        EntityEditor.EntityEditorUI.init(new Country.CountryMetadata(), ajaxCountryRepository);
    }
);
