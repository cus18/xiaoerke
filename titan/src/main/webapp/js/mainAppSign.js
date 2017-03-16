/**
 * 入口文件
 * 2014-11-30 mon
 */
require.config({
    baseUrl: "js/",
    paths: {
        "angular" : "libs/angular.min",
        "angular-resource" : "libs/angular-resource.min",
        "angular-sanitize" : "libs/angular-sanitize.min",
        "angular-route" : "libs/angular-route.min",
        "ocLazyLoad":"libs/ocLazyLoad.require.min",
        "ngDialog":"libs/ngDialog",
        "jquery":"libs/jquery-2.1.3.min",
        "swiper":"libs/swiper",
        "appSignFactory" : "services/appSignFactory",
        "titanCommonFactory": "services/titanCommonFactory",
        "appSignRoute" : "routes/appSignRoute",
        "appSign" : "modules/appSign"
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
            deps: ['angular'],   //以来什么模块
            exports: 'angular-route'
        },
        'angular-sanitize':{
            deps: ['angular'],   //以来什么模块
            exports: 'angular-sanitize'
        },
        'ngDialog':{
            deps: ['angular'],   //依赖�?么模�?
            exports: 'ngDialog'
        },
        'ocLazyLoad': ['angular'],
        'app':['ocLazyLoad'],
    }
});

require(['angular','angular-resource','angular-sanitize','angular-route','ngDialog',
        'ocLazyLoad','jquery','swiper','appSignFactory','titanCommonFactory',
        'appSignRoute','appSign'],
    function (angular){
        angular.bootstrap(document,["appSign"]);
    });

