<%@ include file="/WEB-INF/views/common/global.jsp"%>

<!-- Page content -->
<div class="page-content">

    <!-- Page header -->
    <div class="page-header">
        <div class="page-title">
            <h3 class="entity-names">Entities</h3>
        </div>

    </div>
    <!-- /page header -->
    <div id="entity-list-panel" class="panel col-lg-9">
        <div class="panel-heading"></div>
        <div class="panel-body">
            <div class="alert alert-danger fade in block-inner" hidden="true">
                <div class="message">...</div>
            </div>
            <div class="entity-list-container">
                <table class="table">
                    <thead></thead>
                    <tfoot></tfoot>
                </table>
            </div>
        </div>
    </div>

    <!-- Basic inputs -->
    <div id="entity-form-panel" class="panel col-lg-9" hidden="true">
        <div class="panel-heading">
            <h6 class="panel-title"><i class="icon-pencil"></i><span class="entity-name">Entity</span></h6>
        </div>
        <div class="panel-body">
            <div class="alert alert-danger fade in block-inner">
                <div class="message">...</div>
            </div>
            <form class="form-horizontal" role="form" action="#">
                <div id="entity-form-input"></div>
                <div class="form-actions text-left">
                    <button type="button" class="btn btn-default action-add">Add</button>
                    <button type="button" class="btn btn-default action-save">Save</button>
                    <button type="button" class="btn btn-default action-delete">Delete</button>
                    <button type="button" class="btn btn-default action-cancel">Cancel</button>
                </div>
            </form>
        </div>
    </div>

    <div id="country-form-input" hidden="true">
        <div class="form-group">
            <label class="col-sm-2 control-label">Code: </label>

            <div class="col-sm-10">
                <input type="text" class="form-control id" id="code">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Name: </label>

            <div class="col-sm-10">
                <input type="text" class="form-control" id="name">
            </div>
        </div>
    </div>

    <!-- Upload Form -->
    <div id="entity-upload-form-panel" class="panel col-lg-9" hidden="true">
        <div class="panel-heading"></div>
        <div class="panel-body">
            <div class="alert alert-danger fade in block-inner">
                <div class="message">...</div>
            </div>
            <form class="form-horizontal" role="form" action="#">
                <div class="form-group">
                    <label class="col-sm-1 control-label">File: </label>

                    <div class="col-sm-11">
                        <input type="file" class="styled">
                    </div>
                </div>
                <div class="form-actions text-left">
                    <button type="button" class="btn btn-default action-upload"><i class="icon-upload"></i>Upload</button>
                    <button type="button" class="btn btn-default action-cancel">Cancel</button>
                </div>
            </form>
        </div>
    </div>

</div>
<!-- /page content -->
