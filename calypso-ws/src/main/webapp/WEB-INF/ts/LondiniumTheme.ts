/// <reference path="../jslib/js/jquery.d.ts" />
/// <reference path="../jslib/js/jquery.dataTables.d.ts" />
/// <reference path="../jslib/js/select2.d.ts" />
/// <reference path="jquery.uniform.d.ts" />

module LondiniumTheme {
    function initDropDown() {
        $('.dropdown, .btn-group').on('show.bs.dropdown', function (e) {
            $(this).find('.dropdown-menu').first().stop(true, true).fadeIn(100);
        });

        //===== Add fadeOut animation to dropdown =====//

        $('.dropdown, .btn-group').on('hide.bs.dropdown', function (e) {
            $(this).find('.dropdown-menu').first().stop(true, true).fadeOut(100);
        });

        $('.popup').click(function (e) {
            e.stopPropagation();
        });
    }

    function initDataTables() {
        $.extend($.fn.dataTable.defaults, {
            autoWidth: false,
            pagingType: 'full_numbers',
            dom: 'T<"datatable-header"fl><"datatable-scroll"t><"datatable-footer"ip>',
            language: {
                search: '<span>Filter:</span> _INPUT_',
                lengthMenu: '<span>Show:</span> _MENU_',
                paginate: { 'first': '<<', 'last': '>>', 'next': '>', 'previous': '<' }
            }
        });
    }

    function initLayout() {
        /* # Default Layout Options
         ================================================== */

        //===== Wrapping content inside .page-content =====//

        $('.page-content').wrapInner('<div class="page-content-inner"></div>');


        //===== Applying offcanvas class =====//

        $(document).on('click', '.offcanvas', function () {
            $('body').toggleClass('offcanvas-active');
        });

    }

    function initNavigation() {

        //===== Default navigation =====//

        $('.navigation').find('li.active').parents('li').addClass('active');
        $('.navigation').find('li').not('.active').has('ul').children('ul').addClass('hidden-ul');
        $('.navigation').find('li').has('ul').children('a').parent('li').addClass('has-ul');


        $(document).on('click', '.sidebar-toggle', function (e) {
            e.preventDefault();

            $('body').toggleClass('sidebar-narrow');

            if ($('body').hasClass('sidebar-narrow')) {
                $('.navigation').children('li').children('ul').css('display', '');

                $('.sidebar-content').hide().delay(100).queue(function () {
                    $(this).show().addClass('animated fadeIn').clearQueue();
                });
            }

            else {
                $('.navigation').children('li').children('ul').css('display', 'none');
                $('.navigation').children('li.active').children('ul').css('display', 'block');

                $('.sidebar-content').hide().delay(100).queue(function () {
                    $(this).show().addClass('animated fadeIn').clearQueue();
                });
            }
        });


        $('.navigation').find('li').has('ul').children('a').on('click', function (e) {
            e.preventDefault();

            if ($('body').hasClass('sidebar-narrow')) {
                $(this).parent('li > ul li').not('.disabled').toggleClass('active').children('ul').slideToggle(250);
                $(this).parent('li > ul li').not('.disabled').siblings().removeClass('active').children('ul').slideUp(250);
            }

            else {
                $(this).parent('li').not('.disabled').toggleClass('active').children('ul').slideToggle(250);
                $(this).parent('li').not('.disabled').siblings().removeClass('active').children('ul').slideUp(250);
            }
        });

        $('.navigation .disabled a, .navbar-nav > .disabled > a').click(function (e) {
            e.preventDefault();
        });
    }

    function initPanels() {

        //===== Panel Options (collapsing, closing) =====//

        /* Collapsing */
        $('[data-panel=collapse]').click(function (e) {
            e.preventDefault();
            var $target = $(this).parent().parent().next('div');
            if ($target.is(':visible')) {
                $(this).children('i').removeClass('icon-arrow-up9');
                $(this).children('i').addClass('icon-arrow-down9');
            }
            else {
                $(this).children('i').removeClass('icon-arrow-down9');
                $(this).children('i').addClass('icon-arrow-up9');
            }
            $target.slideToggle(200);
        });

        /* Closing */
        $('[data-panel=close]').click(function (e) {
            e.preventDefault();
            var $panelContent = $(this).parent().parent().parent();
            $panelContent.slideUp(200).remove(200);
        });
    }

    function initSelect2() {
        //===== Datatable select =====//

        $(".dataTables_length select").select2({
            minimumResultsForSearch: -1
        });


        //===== Default select =====//

        $(".select").select2({
            minimumResultsForSearch: -1,
            width: "200"
        });


        //===== Liquid select =====//

        $(".select-liquid").select2({
            minimumResultsForSearch: -1,
            width: "off"
        });


        //===== Full width select =====//

        $(".select-full").select2({
            width: "100%"
        });


        //===== Select with filter input =====//

        $(".select-search").select2({
            width: "200"
        });


        //===== Multiple select =====//

        $(".select-multiple").select2({
            width: "100%"
        });


        //===== Loading data select =====//

        $("#loading-data").select2({
            placeholder: "Enter at least 1 character",
            allowClear: true,
            minimumInputLength: 1,
            query: function (query) {
                var data = {results: []}, i, j, s;
                for (i = 1; i < 5; i++) {
                    s = "";
                    for (j = 0; j < i; j++) {
                        s = s + query.term;
                    }
                    data.results.push({id: query.term + i, text: s});
                }
                query.callback(data);
            }
        });


        //===== Select with maximum =====//

        $(".maximum-select").select2({
            maximumSelectionSize: 3,
            width: "100%"
        });


        //===== Allow clear results select =====//

        $(".clear-results").select2({
            placeholder: "Select a State",
            allowClear: true,
            width: "200"
        });


        //===== Select with minimum =====//

        $(".minimum-select").select2({
            minimumInputLength: 2,
            width: "200"
        });


        //===== Multiple select with minimum =====//

        $(".minimum-multiple-select").select2({
            minimumInputLength: 2,
            width: "100%"
        });


        //===== Disabled select =====//

        $(".select-disabled").select2(
            "enable", false
        );
    }

    export function init() {
        initNavigation();
        initPanels();
        initDataTables();
        initDropDown();
        initSelect2();
        $(".styled, .multiselect-container input").uniform({ radioClass: 'choice', selectAutoWidth: false });
    }
}