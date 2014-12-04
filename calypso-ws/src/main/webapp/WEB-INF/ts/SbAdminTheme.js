/// <reference path="../jslib/ts/jquery.d.ts" />
/// <reference path="../jslib/ts/jquery.dataTables.d.ts" />
/// <reference path="../jslib/ts/select2.d.ts" />
/// <reference path="../jslib/ts/jquery.uniform.d.ts" />
var SbAdminTheme;
(function (SbAdminTheme) {
    function init() {
        $.extend($.fn.dataTable.defaults, {
            autoWidth: false,
            pagingType: 'full_numbers',
            dom: '<"datatable-header"fTl><"datatable-scroll"t><"datatable-footer"ip>',
            language: {
                search: '<span>Filter:</span> _INPUT_',
                lengthMenu: '<span>Show:</span> _MENU_',
                paginate: { 'first': '<<', 'last': '>>', 'next': '>', 'previous': '<' }
            }
        });
    }
    SbAdminTheme.init = init;
})(SbAdminTheme || (SbAdminTheme = {}));
//# sourceMappingURL=SbAdminTheme.js.map