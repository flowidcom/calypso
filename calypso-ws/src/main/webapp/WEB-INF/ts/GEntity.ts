/// <reference path="../jslib/jquery/jquery.d.ts" />
/// <reference path="../jslib/datatables/jquery.dataTables.d.ts" />

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
