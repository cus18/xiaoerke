/**
 * 入口文件
 * 2014-11-30 mon
 */
require.config({
    baseUrl: "/wisdom/js/",
    paths: {
        "angular" : "libs/angular.min",
        "angular-resource" : "libs/angular-resource.min",
        "angular-sanitize" : "libs/angular-sanitize.min",
        "angular-route" : "libs/angular-route.min",
        "ocLazyLoad":"libs/ocLazyLoad.require.min",
        "jquery":"libs/jquery-2.1.3.min",
        "activityFactory" : "services/activityFactory",
        "activityDirective" : "directives/activityDirective",
        "appActivityRoute" : "routes/appActivityRoute",
        "appActivity": "modules/appActivity"
    },
    waitSeconds: 0,
    shim: {
        'angular': {
            exports: 'angular'
        },
        'angular-resource':{
            deps: ["angular"],
            exports: 'angular-resource'
        },
        'angular-route':{
            deps: ['angular'],   //依赖什么模块
            exports: 'angular-route'
        },
        'angular-sanitize':{
            deps: ['angular'],   //依赖什么模块
            exports: 'angular-sanitize'
        },
        'ocLazyLoad': ['angular'],
        'appUmbrella':['ocLazyLoad'],
    }
});

require(['angular','angular-resource','angular-sanitize','angular-route',
    'ocLazyLoad','jquery','activityFactory','activityDirective','appActivityRoute','appActivity'],
    function (angular){
        angular.bootstrap(document,["activityApp"]);
    });

