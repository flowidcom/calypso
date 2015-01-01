// TODO - this is a workaround because the installed jquery.dataTables.d.ts is for an older version

declare module DataTables {
    export interface DataTable {
        api() : any;
    }
}
