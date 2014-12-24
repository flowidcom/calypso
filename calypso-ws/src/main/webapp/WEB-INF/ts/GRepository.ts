/// <reference path="../jslib/js/jquery.d.ts" />
/// <reference path="GEntity.ts" />

module GRepository {
    export interface GRepository<K, T> {
        getItems() : JQueryPromise<GEntity.List<T>>;
        getItem(k:K): JQueryPromise<T>;
        addItem(rq:T) : JQueryPromise<T>;
        saveItem(rq:T) : JQueryPromise<T>;
        deleteItem(k:K): JQueryPromise<T>;
        uploadItems(fileName:FileList) : JQueryPromise<any>;
        index(t:T): K;
    }

    export class AppConfig {
        static getAppCd():string {
            return navigator.userAgent;
        }

        static getVersion():string {
            return "1.0";
        }
    }

    export class MemoryRepository<K, T> implements GRepository<K, T> {
        items:T[];

        getItems() {
            var d = $.Deferred();
            d.resolve({
                version: AppConfig.getVersion(),
                items: this.items
            });
            return d.promise();
        }

        addItem(t:T):JQueryPromise<T> {
            console.debug("Adding item: " + this.index(t));
            var d = $.Deferred();
            d.resolve(t);
            return d.promise();
        }

        saveItem(t:T):JQueryPromise<T> {
            var d = $.Deferred();
            var k:K = this.index(t);
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
        }

        getItem(k:K):JQueryPromise<T> {
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
        }

        deleteItem(k:K):JQueryPromise<T> {
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
        }

        uploadItems(files:FileList):JQueryPromise<any> {
            alert("TODO - implement the Memory version.");
            var d = $.Deferred();
            return d.promise();
            d.resolve("ok");
            return d.promise();
        }

        index(t:T):K {
            return null;
        }

    }

    export class AjaxRepository<K, T> implements GRepository<K, T> {
        constructor(private url:string) {
        }

        getItems() {
            console.debug("getItems");
            var promise:JQueryPromise<GEntity.List<T>> = $.ajax({
                dataType: 'json',
                headers: {"X-Client-Api-Version": "1.0"},
                url: this.url,
                cache: false,
                type: 'GET'
            });

            return promise;
        }

        addItem(t:T) {
            var promise:JQueryPromise<T> = $.ajax({
                dataType: 'json',
                data: JSON.stringify(t),
                url: this.url,
                type: 'POST',
                contentType: "application/json"
            });
            return promise;
        }

        saveItem(t:T) {
            var saveItemUrl = this.url + "/" + this.index(t);
            var jqXHR = $.ajax({
                dataType: 'json',
                data: JSON.stringify(t),
                url: saveItemUrl,
                type: 'POST',
                contentType: "application/json"
            });
            return jqXHR;
        }

        deleteItem(k:K):JQueryPromise<any> {
            var deleteUrl:string = this.url + "/" + k;
            var jqXHR = $.ajax({
                dataType: 'json',
                url: deleteUrl,
                type: 'DELETE'
            });
            return jqXHR;
        }

        getItem(k:K):JQueryPromise<any> {
            console.debug("getItem(" + k + ")");
            var getItemUrl:string = this.url + "/" + k;
            var jqXHR = $.ajax({
                dataType: 'json',
                url: getItemUrl,
                cache: false,
                type: 'GET'
            });
            return jqXHR;
        }

        uploadItems(files:FileList):JQueryPromise<any> {
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
                processData: false, // Don't process the files
                contentType: false // Set content type to false as jQuery will tell the server its a query string request
            });
            return jqXHR;
        }

        index(t:T):K {
            return null;
        }
    }
}
