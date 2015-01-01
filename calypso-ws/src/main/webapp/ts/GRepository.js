/// <reference path="../jslib/jquery/jquery.d.ts" />
/// <reference path="GEntity.ts" />
var GRepository;
(function (GRepository) {
    var AppConfig = (function () {
        function AppConfig() {
        }
        AppConfig.getAppCd = function () {
            return navigator.userAgent;
        };
        AppConfig.getVersion = function () {
            return "1.0";
        };
        return AppConfig;
    })();
    GRepository.AppConfig = AppConfig;
    var MemoryRepository = (function () {
        function MemoryRepository() {
        }
        MemoryRepository.prototype.getItems = function () {
            var d = $.Deferred();
            d.resolve({
                version: AppConfig.getVersion(),
                items: this.items
            });
            return d.promise();
        };
        MemoryRepository.prototype.addItem = function (t) {
            console.debug("Adding item: " + this.index(t));
            var d = $.Deferred();
            d.resolve(t);
            return d.promise();
        };
        MemoryRepository.prototype.saveItem = function (t) {
            var d = $.Deferred();
            var k = this.index(t);
            for (var i = 0; i < this.items.length; i++) {
                if (this.index(this.items[i]) == k) {
                    this.items[i] = t;
                }
            }
            d.resolve({
                version: AppConfig.getVersion(),
                data: t
            });
            return d.promise();
        };
        MemoryRepository.prototype.getItem = function (k) {
            var d = $.Deferred();
            var t = null;
            for (var i = 0; i < this.items.length; i++) {
                if (this.index(this.items[i]) == k) {
                    t = this.items[i];
                }
            }
            d.resolve({
                version: AppConfig.getVersion(),
                data: t
            });
            return d.promise();
        };
        MemoryRepository.prototype.deleteItem = function (k) {
            var d = $.Deferred();
            var t = null;
            for (var i = 0; i < this.items.length; i++) {
                if (this.index(this.items[i]) == k) {
                    t = this.items.splice(i, 1)[0];
                }
            }
            d.resolve({
                version: AppConfig.getVersion(),
                data: t
            });
            return d.promise();
        };
        MemoryRepository.prototype.uploadItems = function (files) {
            alert("TODO - implement the Memory version.");
            var d = $.Deferred();
            return d.promise();
            d.resolve("ok");
            return d.promise();
        };
        MemoryRepository.prototype.index = function (t) {
            return null;
        };
        return MemoryRepository;
    })();
    GRepository.MemoryRepository = MemoryRepository;
    var AjaxRepository = (function () {
        function AjaxRepository(url) {
            this.url = url;
        }
        AjaxRepository.prototype.getItems = function () {
            console.debug("getItems");
            var promise = $.ajax({
                dataType: 'json',
                headers: { "X-Client-Api-Version": "1.0" },
                url: this.url,
                cache: false,
                type: 'GET'
            });
            return promise;
        };
        AjaxRepository.prototype.addItem = function (t) {
            var promise = $.ajax({
                dataType: 'json',
                data: JSON.stringify(t),
                url: this.url,
                type: 'POST',
                contentType: "application/json"
            });
            return promise;
        };
        AjaxRepository.prototype.saveItem = function (t) {
            var saveItemUrl = this.url + "/" + this.index(t);
            var jqXHR = $.ajax({
                dataType: 'json',
                data: JSON.stringify(t),
                url: saveItemUrl,
                type: 'POST',
                contentType: "application/json"
            });
            return jqXHR;
        };
        AjaxRepository.prototype.deleteItem = function (k) {
            var deleteUrl = this.url + "/" + k;
            var jqXHR = $.ajax({
                dataType: 'json',
                url: deleteUrl,
                type: 'DELETE'
            });
            return jqXHR;
        };
        AjaxRepository.prototype.getItem = function (k) {
            console.debug("getItem(" + k + ")");
            var getItemUrl = this.url + "/" + k;
            var jqXHR = $.ajax({
                dataType: 'json',
                url: getItemUrl,
                cache: false,
                type: 'GET'
            });
            return jqXHR;
        };
        AjaxRepository.prototype.uploadItems = function (files) {
            console.debug("Upload files");
            var data = new FormData();
            $.each(files, function (key, value) {
                data.append(key, value);
            });
            var jqXHR = $.ajax({
                url: this.url + "/action/upload",
                type: 'POST',
                data: data,
                cache: false,
                dataType: 'json',
                processData: false,
                contentType: false // Set content type to false as jQuery will tell the server its a query string request
            });
            return jqXHR;
        };
        AjaxRepository.prototype.index = function (t) {
            return null;
        };
        return AjaxRepository;
    })();
    GRepository.AjaxRepository = AjaxRepository;
})(GRepository || (GRepository = {}));
//# sourceMappingURL=GRepository.js.map