/// <reference path="../jslib/ts/jquery.d.ts" />
/// <reference path="../jslib/ts/jquery.dataTables.d.ts" />

module GEntity {
    export interface Metadata {
        formInputFields(): JQuery;
        tableColumns() : DataTables.ColumnOptions[];
        name: string
        pluralName: string
    }

    export interface List<T> {
        items: T[];
        start: number;
        totalCount: number;
    }

    export interface Error {
        code: string;
        message: string;
    }
}
