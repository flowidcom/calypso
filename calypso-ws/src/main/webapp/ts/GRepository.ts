/// <reference path="../jslib/jquery/jquery.d.ts" />
/// <reference path="GEntity.ts" />

module GRepository {
    export interface GRepository<K, T> {
        getItems() : JQueryPromise<GEntity.List<T>>;
        getItem(k:K): JQueryPromise<T>;
        addItem(rq:T) : JQueryPromise<T>;
        saveItem(rq:T) : JQueryPromise<T>;
        deleteItem(k:K): JQueryPromise<T>;
        uploadItems(fileNames:FileList) : JQueryPromise<any>;
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
            throw new Error('This method is abstract');
        }

    }

    export class AjaxRepository<K, T> implements GRepository<K, T> {
        constructor(private url:string) {
        }

        getItems() :JQueryPromise<GEntity.List<T>> {
            console.debug("getItems");
            return $.ajax({
                dataType: 'json',
                headers: {"X-Client-Api-Version": "1.0"},
                url: this.url,
                cache: false,
                type: 'GET'
            });
        }

        addItem(t:T) :JQueryPromise<T> {
            return $.ajax({
                dataType: 'json',
                data: JSON.stringify(t),
                url: this.url,
                type: 'POST',
                contentType: "application/json"
            });
        }

        saveItem(t:T) {
            var saveItemUrl = this.url + "/" + this.index(t);
            return $.ajax({
                dataType: 'json',
                data: JSON.stringify(t),
                url: saveItemUrl,
                type: 'POST',
                contentType: "application/json"
            });
        }

        deleteItem(k:K):JQueryPromise<any> {
            var deleteUrl:string = this.url + "/" + k;
            return $.ajax({
                dataType: 'json',
                url: deleteUrl,
                type: 'DELETE'
            });
        }

        getItem(k:K):JQueryPromise<any> {
            console.debug("getItem(" + k + ")");
            var getItemUrl:string = this.url + "/" + k;
            return $.ajax({
                dataType: 'json',
                url: getItemUrl,
                cache: false,
                type: 'GET'
            });
        }

        uploadItems(files:FileList):JQueryPromise<any> {
            console.debug("Upload files");

            var data = new FormData();
            $.each(files, function (key, value) {
                data.append(key, value);
            });

            return $.ajax({
                url: this.url + "/action/upload",
                type: 'POST',
                data: data,
                cache: false,
                dataType: 'json',
                processData: false, // Don't process the files
                contentType: false // Set content type to false as jQuery will tell the server its a query string request
            });
        }

        // This operation is implemented in derived classes
        index(t:T):K {
            throw new Error('This method is abstract');
        }
    }
}
