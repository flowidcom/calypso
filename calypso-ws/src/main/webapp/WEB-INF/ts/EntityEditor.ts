/// <reference path="../jslib/ts/jquery.d.ts" />
/// <reference path="../jslib/ts/jquery.dataTables.d.ts" />
/// <reference path="../jslib/ts/jquery.dataTables-1.10.d.ts" />
/// <reference path="../jslib/ts/select2.d.ts" />
/// <reference path="../jslib/ts/jquery.uniform.d.ts" />
/// <reference path="GEntity.ts" />
/// <reference path="GRepository.ts" />

module EntityEditor {
    export class EntityEditorUI <K, T> {
        private table:DataTables.DataTable;
        private formPanel:JQuery;
        private uploadPanel:JQuery;
        private tablePanel:JQuery;
        private repository:GRepository.GRepository<any, any>;
        private metadata: GEntity.Metadata;

        constructor (entityMetadata:GEntity.Metadata, repository:GRepository.GRepository<K, T>) {
            var ui = this;
            ui.metadata = entityMetadata;
            ui.repository = repository;
            ui.formPanel = $('#entity-form-panel');
            $("#entity-form-input", ui.formPanel).append(entityMetadata.formInputFields());
            $(".entity-name").text(entityMetadata.name);
            ui.formPanel.find('.action-add');
            ui.formPanel.find('.action-cancel').click(() => EntityEditorUI.showListPanel(ui));

            ui.tablePanel = $('#entity-list-panel');
            ui.clearErrors(ui.tablePanel);
            $(".entity-names").text(entityMetadata.pluralName);

            ui.uploadPanel = $('#entity-upload-form-panel');
            ui.uploadPanel.find('.action-cancel').click(() => EntityEditorUI.showListPanel(ui));
            ui.clearErrors(ui.uploadPanel);

            ui.table = $('.entity-list-container table').dataTable({
                ajax: function (data, callback, settings) {
                    var promise:JQueryPromise<GEntity.List<T>> = ui.repository.getItems();
                    promise.done(function (result:GEntity.List<T>) {
                        callback({
                            data: result.items
                        })
                    })
                        .fail(function (jqXHR, textStatus, errorThrown) {
                            ui.displayError(ui.tablePanel, textStatus, errorThrown, jqXHR.responseJSON);
                        })
                },
                paginate: true,
                sort: true,
                destroy: true,
                info: false,
                processing: true,
                columns: entityMetadata.tableColumns(),
                oTableTools: {
                    aButtons: [
                        {
                            sExtends: "text",
                            sButtonText: 'Add',
                            sButtonClass: "btn btn-success",
                            fnClick: () => { ui.displayAddItemForm(this)}
                        },
                        {
                            sExtends: "text",
                            sButtonText: 'Upload',
                            sButtonClass: "btn btn-success",
                            fnClick: () => { ui.displayUploadItemsForm(this)}
                        }
                    ]
                },
                createdRow: function (row:Node, data:any, index:number) {
                    $(row).click(function () {
                        ui.displayEditForm(ui, row, data, index)
                    });
                }
            });

            // the following is required to render nicely the dropdown of items-per-page
            if ($(".dataTables_length select").select2 != undefined ) {
                $(".dataTables_length select").select2({
                    minimumResultsForSearch: -1
                });
            }
        }

        refreshTable(ui: EntityEditor.EntityEditorUI<K, T>) {
            console.log("Refreshing the table.");
            ui.table.api().ajax.reload();
        }

        displayAddItemForm = (ui: EntityEditor.EntityEditorUI<K, T>) => {
            ui.clearErrors(ui.formPanel);
            ui.showEntityPanel(ui);
            ui.formPanel.find(".id").prop('disabled', null);
            ui.formPanel.find('.action-delete').hide();
            ui.formPanel.find('.action-add').show();
            ui.formPanel.find('.action-save').hide();
            ui.formPanel.find(':input:text').val('');
            ui.formPanel.find('.action-add').off().click(() => {
                var t = {};
                // copy items from the form into the object
                ui.updateAttributes(t, ui.formPanel);
                ui.repository.addItem(t)
                    .done((result:any) => {
                        // ui.table.fnAddData(result);
                        ui.refreshTable(ui);
                        EntityEditorUI.showListPanel(ui);
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        ui.displayError(ui.formPanel, textStatus, errorThrown, jqXHR.responseJSON);
                    })
            });

            // escape -> cancel
            ui.setupCancelEvents(ui);
        }

        displayUploadItemsForm(ui: EntityEditor.EntityEditorUI<K, T>) {
            ui.showUploadItemsPanel(ui);
            // clear the files selected
            ui.clearInputFile($(':input:file', ui.uploadPanel));

            ui.uploadPanel.find('.action-upload').off().click(function (e:JQueryEventObject) {
                var $input = $(':input:file', ui.uploadPanel);
                var files = $input.get(0).files;
                if (!files || files.length == 0) {
                    alert("No files selected.");
                }
                else {
                    ui.repository.uploadItems(files)
                        .done((data:any, textStatus:String, jqXHR) => {
                            if (typeof data.error === 'undefined') {
                                ui.refreshTable(ui);
                                EntityEditorUI.showListPanel(ui);
                            }
                            else {
                                // Handle errors here
                                ui.displayError(ui.uploadPanel, data.error, null, null);
                            }
                        })
                        .fail((jqXHR, textStatus, errorThrown) => {
                            // Handle errors here
                            ui.displayError(ui.uploadPanel, textStatus, errorThrown, jqXHR.responseJSON);
                            // STOP LOADING SPINNER
                        })
                }
            });

            // escape -> cancel
            ui.setupCancelEvents(ui);
        }

        clearInputFile($input: JQuery) {
            $input = $input.val('').clone(true);
            $.uniform.update($input);
        }

        displayError(panel:JQuery, textStatus:string, errorThrown:any, responseJSON:GEntity.Error) {
            var message = "";
            // $('.entity-list-container', panel).hide();
            if (responseJSON) {
                message = "Server Error: " + responseJSON.code + "-" + responseJSON.message;
            }
            else {
                message = "Server Error: " + textStatus + "/" + errorThrown;
            }
            $('.alert', panel).show().find('.message').text(message);
        }

        clearErrors(panel:JQuery) {
            $('.alert', panel).hide().find('.message').text("...");
        }

        private updateAttributes(t:any, formPanel: JQuery) {
            $("#entity-form-input", formPanel).find(":input").each(function () {
                var $this = $(this); // this is the JQuery of the input
                var id = $this.attr('id');
                var val: any = $this.val();
                t[id] = val;
            })
        }

        private setupCancelEvents(ui: EntityEditor.EntityEditorUI<K, T>) {
            $(document).keyup(function (e) {
                if (e.keyCode == 27) {
                    e.preventDefault();
                    EntityEditorUI.showListPanel(ui)
                }   // esc
            });
        }

        displayEditForm(ui: EntityEditor.EntityEditorUI<K, T>, row:Node, data:any, index:number) : void {
            var id = ui.repository.index(data);
            ui.repository.getItem(id).done((result:any) => {
                $.each(result, function (key, value) {
                    ui.formPanel.find('#' + key).val(value);
                });
                ui.clearErrors(ui.formPanel);
                ui.formPanel.find(".id").prop('disabled', true);
                ui.showEntityPanel(ui);
                ui.formPanel.find('.action-add').hide();
                ui.formPanel.find('.action-save').show();
                ui.formPanel.find('.action-delete').show();
                ui.formPanel.find('.action-delete').off().click(() => {
                    if (confirm("Are you sure you want to delete the record?")) {
                        ui.repository.deleteItem(id)
                            .done((result:any) => {
                                console.debug("Deleting at index: " + index + " - " + id);
                                ui.table.fnDeleteRow(row);
                                ui.refreshTable(ui);
                                EntityEditorUI.showListPanel(ui);
                            })
                            .fail(function (jqXHR, textStatus, errorThrown) {
                                ui.displayError(ui.formPanel, textStatus, errorThrown, jqXHR.responseJSON);
                            })
                    }

                });

                ui.formPanel.find('.action-save').off().click(() => {
                    console.debug("Updating at index: " + index + " - " + id);
                    var t = result;
                    ui.updateAttributes(t, ui.formPanel);
                    ui.repository.saveItem(t)
                        .done((result:any) => {
                            ui.table.fnUpdate(result, row);
                            ui.refreshTable(ui);
                            EntityEditorUI.showListPanel(ui);
                        })
                        .fail((jqXHR, textStatus, errorThrown) => {
                            ui.displayError(ui.formPanel, textStatus, errorThrown, jqXHR.responseJSON);
                        })
                });

                // escape -> cancel
                ui.setupCancelEvents(ui);
            });
        }

        static showListPanel(ui: EntityEditor.EntityEditorUI<any, any>) : void {
            ui.formPanel.hide();
            ui.uploadPanel.hide();
            ui.tablePanel.fadeIn(100);
        }

        showEntityPanel(ui: EntityEditor.EntityEditorUI<K, T>) : void {
            ui.tablePanel.hide();
            ui.formPanel.fadeIn(100);
        }

        showUploadItemsPanel(ui: EntityEditor.EntityEditorUI<K, T>) : void {
            ui.tablePanel.hide();
            ui.uploadPanel.fadeIn(100);
        }
    }
}
