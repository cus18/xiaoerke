/**
 * 入口文件
 * 2014-11-30 mon
 */
require.config({
    baseUrl: "/xiaoerke-consult-webapp/js/",
    paths: {
        "angular" : "libs/angular.min",
        "angular-resource" : "libs/angular-resource.min",
        "angular-sanitize" : "libs/angular-sanitize.min",
        "angular-ui-router": "libs/angular-ui-router.min",
        "angular-file-upload": "libs/angular-file-upload.min",
        "angular-file-upload-shim":"libs/angular-file-upload-shim.min",
        "ocLazyLoad":"libs/ocLazyLoad.require.min",
        "jquery":"libs/jquery-2.1.3.min",
        "patientConsultFactory" : "services/patientConsultFactory",
        "patientConsultDirective" : "directives/patientConsultDirective",
        "patientConsultRoute" : "routes/patientConsultRoute",
        "appPatientConsult" : "modules/appPatientConsult"
    },
    shim: {
        'angular': {
            exports: 'angular'
        },
        'angular-resource':{
            deps: ["angular"],
            exports: 'angular-resource'
        },
        'angular-ui-router':{
            deps: ['angular'],   //依赖什么模块
            exports: 'angular-ui-router'
        },
        'angular-file-upload':{
            deps: ['angular'],   //依赖什么模块
            exports: 'angular-file-upload'
        },
        'angular-file-upload-shim':{
            deps: ['angular'],   //依赖什么模块
            exports: 'angular-file-upload-shim'
        },
        'angular-sanitize':{
            deps: ['angular'],   //依赖什么模块
            exports: 'angular-sanitize'
        },
        'ocLazyLoad': ['angular'],
        'appPatientConsult':['ocLazyLoad'],
    }
});

require(['angular','angular-resource','angular-sanitize','angular-ui-router',
        'angular-file-upload','angular-file-upload-shim',
        'ocLazyLoad', 'jquery','patientConsultFactory','patientConsultDirective',
        'patientConsultRoute','appPatientConsult'],
    function (angular){
        angular.bootstrap(document,["patientConsultApp"]);
    });

