/// <reference path="../jslib/jquery/jquery.d.ts" />
/// <reference path="../jslib/datatables/jquery.dataTables.d.ts" />
/// <reference path="../jslib/select2/select2.d.ts" />
/// <reference path="jquery.uniform.d.ts" />

module SbAdminTheme {
    export function init() {
        $.extend($.fn.dataTable.defaults, {
            autoWidth: false,
            pagingType: 'full_numbers',
            dom : '<"datatable-header"fTl><"datatable-scroll"t><"datatable-footer"ip>',
            language: {
                search: '<span>Filter:</span> _INPUT_',
                lengthMenu: '<span>Show:</span> _MENU_',
                paginate: { 'first': '<<', 'last': '>>', 'next': '>', 'previous': '<' }
            }
        });
    }
}