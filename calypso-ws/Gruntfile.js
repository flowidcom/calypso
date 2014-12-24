module.exports = function (grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        bowercopy: {
            options: {
                runBower: false,
                srcPrefix: 'bower_components',
                destPrefix: 'src/main/webapp/WEB-INF/jslib'
            },
            jquery: {
                files: {
                    'js/jquery.js': 'jquery/dist/jquery.js',
                    'js/jquery.min.js': 'jquery/dist/jquery.min.js',
                    'js/jquery.d.ts' : "dt/jquery/jquery.d.ts"
                }
            },
            bootstrap: {
                files: {
                    'js/bootstrap.js': 'bootstrap/dist/js/bootstrap.js',
                    'js/bootstrap.min.js': 'bootstrap/dist/js/bootstrap.min.js',
                    'css/bootstrap.css': 'bootstrap/dist/css/bootstrap.css'
                }
            },
            'jquery-ui': {
                files: {
                    'js/jquery-ui.js': 'jquery-ui/jquery-ui.js',
                    'js/jquery-ui.min.js': 'jquery-ui/jquery-ui.min.js'
                }

            },
            datatables: {
                files: {
                    'js/datatables.js': 'datatables/media/js/jquery.dataTables.js',
                    'js/datatables.min.js': 'datatables/media/js/jquery.dataTables.min.js',
                    'js/jquery.dataTables.d.ts' : "dt/jquery.dataTables/jquery.dataTables.d.ts",
                    'css/datatables.css': 'datatables/media/css/jquery.dataTables.css'
                }
            },
            "datatables-plugins": {
                files: {
                    'css/datatables.bootstrap.css': 'datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css',
                    'js/dataTables.bootstrap.js': 'datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.js'
                }
            },
            select2: {
                files: {
                    "js/select2.js": "select2/select2.js",
                    "js/select2.min.js": "select2/select2.min.js",
                    'js/select2.d.ts' : "dt/select2/select2.d.ts",
                    "css/select2.css": "select2/select2.css"
                }
            },
            tabletools: {
                files: {
                    "js/tabletools.js": "/tabletools/js/dataTables.tableTools.js",
                    "css/tabletools.css": "tabletools/css/dataTables.tableTools.css"
                }
            },
            uniform: {
                files: {
                    'js/jquery.uniform.js': "uniform/jquery.uniform.js",
                    'js/jquery.uniform.min.js': "uniform/jquery.uniform.min.js"
                }
            },
            "sb-admin-2": {
                files: {
                    'js/sb-admin-2.js': "sb-admin-2/js/sb-admin-2.js",
                    'css/sb-admin-2.css': "sb-admin-2/css/sb-admin-2.css",
                }
            },
            "metisMenu": {
                files: {
                    'js/metisMenu.js': "metisMenu/dist/metisMenu.js",
                    'css/metisMenu.css': "metisMenu/dist/metisMenu.css",
                }
            },
            "font-awesome": {
                files: {
                    'css/font-awesome.css': "font-awesome/css/font-awesome.css",
                    'fonts': "font-awesome/fonts/**"
                }
            }

        }
    })
    ;

    grunt.loadNpmTasks('grunt-bowercopy');

// Default task(s).
    grunt.registerTask('default', ['bowercopy']);
}
;
