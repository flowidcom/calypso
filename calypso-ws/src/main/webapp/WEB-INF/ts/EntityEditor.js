/// <reference path="../jslib/ts/jquery.d.ts" />
/// <reference path="../jslib/ts/jquery.dataTables.d.ts" />
/// <reference path="../jslib/ts/jquery.dataTables-1.10.d.ts" />
/// <reference path="../jslib/ts/select2.d.ts" />
/// <reference path="../jslib/ts/jquery.uniform.d.ts" />
/// <reference path="GEntity.ts" />
/// <reference path="GRepository.ts" />
var EntityEditor;
(function (EntityEditor) {
    var EntityEditorUI = (function () {
        function EntityEditorUI(entityMetadata, repository) {
            var _this = this;
            this.displayAddItemForm = function (ui) {
                ui.clearErrors(ui.formPanel);
                ui.showEntityPanel(ui);
                ui.formPanel.find(".id").prop('disabled', null);
                ui.formPanel.find('.action-delete').hide();
                ui.formPanel.find('.action-add').show();
                ui.formPanel.find('.action-save').hide();
                ui.formPanel.find(':input:text').val('');
                ui.formPanel.find('.action-add').off().click(function () {
                    var t = {};
                    // copy items from the form into the object
                    ui.updateAttributes(t, ui.formPanel);
                    ui.repository.addItem(t).done(function (result) {
                        // ui.table.fnAddData(result);
                        ui.refreshTable(ui);
                        EntityEditorUI.showListPanel(ui);
                    }).fail(function (jqXHR, textStatus, errorThrown) {
                        ui.displayError(ui.formPanel, textStatus, errorThrown, jqXHR.responseJSON);
                    });
                });
                // escape -> cancel
                ui.setupCancelEvents(ui);
            };
            var ui = this;
            ui.metadata = entityMetadata;
            ui.repository = repository;
            ui.formPanel = $('#entity-form-panel');
            $("#entity-form-input", ui.formPanel).append(entityMetadata.formInputFields());
            $(".entity-name").text(entityMetadata.name);
            ui.formPanel.find('.action-add');
            ui.formPanel.find('.action-cancel').click(function () { return EntityEditorUI.showListPanel(ui); });
            ui.tablePanel = $('#entity-list-panel');
            ui.clearErrors(ui.tablePanel);
            $(".entity-names").text(entityMetadata.pluralName);
            ui.uploadPanel = $('#entity-upload-form-panel');
            ui.uploadPanel.find('.action-cancel').click(function () { return EntityEditorUI.showListPanel(ui); });
            ui.clearErrors(ui.uploadPanel);
            ui.table = $('.entity-list-container table').dataTable({
                ajax: function (data, callback, settings) {
                    var promise = ui.repository.getItems();
                    promise.done(function (result) {
                        callback({
                            data: result.items
                        });
                    }).fail(function (jqXHR, textStatus, errorThrown) {
                        ui.displayError(ui.tablePanel, textStatus, errorThrown, jqXHR.responseJSON);
                    });
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
                            fnClick: function () {
                                ui.displayAddItemForm(_this);
                            }
                        },
                        {
                            sExtends: "text",
                            sButtonText: 'Upload',
                            sButtonClass: "btn btn-success",
                            fnClick: function () {
                                ui.displayUploadItemsForm(_this);
                            }
                        }
                    ]
                },
                createdRow: function (row, data, index) {
                    $(row).click(function () {
                        ui.displayEditForm(ui, row, data, index);
                    });
                }
            });
            // the following is required to render nicely the dropdown of items-per-page
            if ($(".dataTables_length select").select2 != undefined) {
                $(".dataTables_length select").select2({
                    minimumResultsForSearch: -1
                });
            }
        }
        EntityEditorUI.prototype.refreshTable = function (ui) {
            console.log("Refreshing the table.");
            ui.table.api().ajax.reload();
        };
        EntityEditorUI.prototype.displayUploadItemsForm = function (ui) {
            ui.showUploadItemsPanel(ui);
            // clear the files selected
            ui.clearInputFile($(':input:file', ui.uploadPanel));
            ui.uploadPanel.find('.action-upload').off().click(function (e) {
                var $input = $(':input:file', ui.uploadPanel);
                var files = $input.get(0).files;
                if (!files || files.length == 0) {
                    alert("No files selected.");
                }
                else {
                    ui.repository.uploadItems(files).done(function (data, textStatus, jqXHR) {
                        if (typeof data.error === 'undefined') {
                            ui.refreshTable(ui);
                            EntityEditorUI.showListPanel(ui);
                        }
                        else {
                            // Handle errors here
                            ui.displayError(ui.uploadPanel, data.error, null, null);
                        }
                    }).fail(function (jqXHR, textStatus, errorThrown) {
                        // Handle errors here
                        ui.displayError(ui.uploadPanel, textStatus, errorThrown, jqXHR.responseJSON);
                        // STOP LOADING SPINNER
                    });
                }
            });
            // escape -> cancel
            ui.setupCancelEvents(ui);
        };
        EntityEditorUI.prototype.clearInputFile = function ($input) {
            $input = $input.val('').clone(true);
            $.uniform.update($input);
        };
        EntityEditorUI.prototype.displayError = function (panel, textStatus, errorThrown, responseJSON) {
            var message = "";
            // $('.entity-list-container', panel).hide();
            if (responseJSON) {
                message = "Server Error: " + responseJSON.code + "-" + responseJSON.message;
            }
            else {
                message = "Server Error: " + textStatus + "/" + errorThrown;
            }
            $('.alert', panel).show().find('.message').text(message);
        };
        EntityEditorUI.prototype.clearErrors = function (panel) {
            $('.alert', panel).hide().find('.message').text("...");
        };
        EntityEditorUI.prototype.updateAttributes = function (t, formPanel) {
            $("#entity-form-input", formPanel).find(":input").each(function () {
                var $this = $(this); // this is the JQuery of the input
                var id = $this.attr('id');
                var val = $this.val();
                t[id] = val;
            });
        };
        EntityEditorUI.prototype.setupCancelEvents = function (ui) {
            $(document).keyup(function (e) {
                if (e.keyCode == 27) {
                    e.preventDefault();
                    EntityEditorUI.showListPanel(ui);
                } // esc
            });
        };
        EntityEditorUI.prototype.displayEditForm = function (ui, row, data, index) {
            var id = ui.repository.index(data);
            ui.repository.getItem(id).done(function (result) {
                $.each(result, function (key, value) {
                    ui.formPanel.find('#' + key).val(value);
                });
                ui.clearErrors(ui.formPanel);
                ui.formPanel.find(".id").prop('disabled', true);
                ui.showEntityPanel(ui);
                ui.formPanel.find('.action-add').hide();
                ui.formPanel.find('.action-save').show();
                ui.formPanel.find('.action-delete').show();
                ui.formPanel.find('.action-delete').off().click(function () {
                    if (confirm("Are you sure you want to delete the record?")) {
                        ui.repository.deleteItem(id).done(function (result) {
                            console.debug("Deleting at index: " + index + " - " + id);
                            ui.table.fnDeleteRow(row);
                            ui.refreshTable(ui);
                            EntityEditorUI.showListPanel(ui);
                        }).fail(function (jqXHR, textStatus, errorThrown) {
                            ui.displayError(ui.formPanel, textStatus, errorThrown, jqXHR.responseJSON);
                        });
                    }
                });
                ui.formPanel.find('.action-save').off().click(function () {
                    console.debug("Updating at index: " + index + " - " + id);
                    var t = result;
                    ui.updateAttributes(t, ui.formPanel);
                    ui.repository.saveItem(t).done(function (result) {
                        ui.table.fnUpdate(result, row);
                        ui.refreshTable(ui);
                        EntityEditorUI.showListPanel(ui);
                    }).fail(function (jqXHR, textStatus, errorThrown) {
                        ui.displayError(ui.formPanel, textStatus, errorThrown, jqXHR.responseJSON);
                    });
                });
                // escape -> cancel
                ui.setupCancelEvents(ui);
            });
        };
        EntityEditorUI.showListPanel = function (ui) {
            ui.formPanel.hide();
            ui.uploadPanel.hide();
            ui.tablePanel.fadeIn(100);
        };
        EntityEditorUI.prototype.showEntityPanel = function (ui) {
            ui.tablePanel.hide();
            ui.formPanel.fadeIn(100);
        };
        EntityEditorUI.prototype.showUploadItemsPanel = function (ui) {
            ui.tablePanel.hide();
            ui.uploadPanel.fadeIn(100);
        };
        return EntityEditorUI;
    })();
    EntityEditor.EntityEditorUI = EntityEditorUI;
})(EntityEditor || (EntityEditor = {}));
//# sourceMappingURL=EntityEditor.js.map