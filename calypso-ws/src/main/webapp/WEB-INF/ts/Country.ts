/// <reference path="../jslib/js/jquery.d.ts" />
/// <reference path="../jslib/js/jquery.dataTables.d.ts" />
/// <reference path="../jslib/js/select2.d.ts" />
/// <reference path="EntityEditor.ts" />

module Country {
    export class Country {
        code : string;
        name: string;
    }

    export class MemCountryRepository extends GRepository.MemoryRepository<string, Country> {
        constructor() {
            super();
            this.items = [
                {code : "CA", name: "Canada"},
                {code : "US", name: "United States"}
            ]
        }

        index(c: Country): any {
            return c.code;
        }
    }

    export class AjaxCountryRepository extends GRepository.AjaxRepository<string, Country> {
        constructor(url: string) {
            super(url);
        }

        index(c: Country): any {
            return c.code;
        }
    }

    export class CountryMetadata implements GEntity.Metadata {
        name:string = "Country";
        pluralName:string = "Countries";

        formInputFields():JQuery {
            return $("#country-form-input").children();
        }

        tableColumns():DataTables.ColumnOptions[] {
            return [
                { mData: 'code', sTitle: 'Code', asSorting: ['asc'], bSortable: true},
                { mData: 'name', sTitle: 'Name', asSorting: ['asc'], bSortable: true}
            ]
        }
    }
}