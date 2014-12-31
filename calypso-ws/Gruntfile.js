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
                    'jquery/jquery.js': 'jquery/dist/jquery.js',
                    'jquery/jquery.min.js': 'jquery/dist/jquery.min.js',
                    'jquery/jquery.d.ts' : "dt/jquery/jquery.d.ts"
                }
            },
            bootstrap: {
                files: {
                    'bootstrap/bootstrap.js': 'bootstrap/dist/js/bootstrap.js',
                    'bootstrap/bootstrap.min.js': 'bootstrap/dist/js/bootstrap.min.js',
                    'bootstrap/bootstrap.css': 'bootstrap/dist/css/bootstrap.css'
                }
            },
            'jquery-ui': {
                files: {
                    'jquery-ui/jquery-ui.js': 'jquery-ui/jquery-ui.js',
                    'jquery-ui/jquery-ui.min.js': 'jquery-ui/jquery-ui.min.js'
                }

            },
            datatables: {
                files: {
                    'datatables/datatables.js': 'datatables/media/js/jquery.dataTables.js',
                    'datatables/datatables.min.js': 'datatables/media/js/jquery.dataTables.min.js',
                    'datatables/jquery.dataTables.d.ts' : "dt/jquery.dataTables/jquery.dataTables.d.ts",
                    'datatables/datatables.css': 'datatables/media/css/jquery.dataTables.css'
                }
            },
            "datatables-plugins": {
                files: {
                    'datatables/datatables.bootstrap.css': 'datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css',
                    'datatables/datatables.bootstrap.js': 'datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.js'
                }
            },
            select2: {
                files: {
                    "select2/select2.js": "select2/select2.js",
                    "select2/select2.min.js": "select2/select2.min.js",
                    'select2/select2.d.ts' : "dt/select2/select2.d.ts",
                    "select2/select2.css": "select2/select2.css"
                }
            },
            tabletools: {
                files: {
                    "tabletools/tabletools.js": "/tabletools/js/dataTables.tableTools.js",
                    "tabletools/tabletools.css": "tabletools/css/dataTables.tableTools.css"
                }
            },
            uniform: {
                files: {
                    'uniform/jquery.uniform.js': "uniform/jquery.uniform.js",
                    'uniform/jquery.uniform.min.js': "uniform/jquery.uniform.min.js"
                }
            },
            "sb-admin-2": {
                files: {
                    'sbadmin2/sb-admin-2.js': "sb-admin-2/js/sb-admin-2.js",
                    'sbadmin2/sb-admin-2.css': "sb-admin-2/css/sb-admin-2.css",
                }
            },
            "metisMenu": {
                files: {
                    'metisMenu/metisMenu.js': "metisMenu/dist/metisMenu.js",
                    'metisMenu/metisMenu.css': "metisMenu/dist/metisMenu.css",
                }
            },
            "font-awesome": {
                files: {
                    'fonts/font-awesome.css': "font-awesome/css/font-awesome.css",
                    'fonts': "font-awesome/fonts/**"
                }
            }
        },
		
		typescript: {
            base: {
                src: ['src/main/webapp/WEB-INF/ts/*.ts'],
                dest: '.',
				options: {
				   sourceMap : true
				}
            }
        }
	});

    grunt.loadNpmTasks('grunt-bowercopy');
    grunt.loadNpmTasks('grunt-typescript');

// Default task(s).
    grunt.registerTask('default', ['bowercopy', 'typescript']);
};
