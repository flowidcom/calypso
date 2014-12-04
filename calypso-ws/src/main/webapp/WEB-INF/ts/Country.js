/// <reference path="../jslib/ts/jquery.d.ts" />
/// <reference path="../jslib/ts/jquery.dataTables.d.ts" />
/// <reference path="../jslib/ts/select2.d.ts" />
/// <reference path="EntityEditor.ts" />
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
var Country;
(function (_Country) {
    var Country = (function () {
        function Country() {
        }
        return Country;
    })();
    _Country.Country = Country;
    var MemCountryRepository = (function (_super) {
        __extends(MemCountryRepository, _super);
        function MemCountryRepository() {
            _super.call(this);
            this.items = [
                { code: "CA", name: "Canada" },
                { code: "US", name: "United States" }
            ];
        }
        MemCountryRepository.prototype.index = function (c) {
            return c.code;
        };
        return MemCountryRepository;
    })(GRepository.MemoryRepository);
    _Country.MemCountryRepository = MemCountryRepository;
    var AjaxCountryRepository = (function (_super) {
        __extends(AjaxCountryRepository, _super);
        function AjaxCountryRepository(url) {
            _super.call(this, url);
        }
        AjaxCountryRepository.prototype.index = function (c) {
            return c.code;
        };
        return AjaxCountryRepository;
    })(GRepository.AjaxRepository);
    _Country.AjaxCountryRepository = AjaxCountryRepository;
    var CountryMetadata = (function () {
        function CountryMetadata() {
            this.name = "Country";
            this.pluralName = "Countries";
        }
        CountryMetadata.prototype.formInputFields = function () {
            return $("#country-form-input").children();
        };
        CountryMetadata.prototype.tableColumns = function () {
            return [
                { mData: 'code', sTitle: 'Code', asSorting: ['asc'], bSortable: true },
                { mData: 'name', sTitle: 'Name', asSorting: ['asc'], bSortable: true }
            ];
        };
        return CountryMetadata;
    })();
    _Country.CountryMetadata = CountryMetadata;
})(Country || (Country = {}));
//# sourceMappingURL=Country.js.map