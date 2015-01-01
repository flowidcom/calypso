var Global = (function () {
    return {
        init: function () {

        },

        clearErrors: function (container) {
            container.empty().hide();
        },

        addErrors: function (container, errors, func) {
            container.show();
            $(errors).each(function (i, val) {
                container.append($('<li>', { text: func ? func(val) : val }));
            });
        }
    }
})();


Global.init();