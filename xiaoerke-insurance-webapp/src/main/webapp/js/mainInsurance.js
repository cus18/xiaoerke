/**
 * 入口文件
 * 2014-11-30 mon
 */
require.config({
    baseUrl: "/xiaoerke-insurance-webapp/js/",
    paths: {
        "angular" : "libs/angular.min",
        "angular-resource" : "libs/angular-resource.min",
        "angular-sanitize" : "libs/angular-sanitize.min",
        "angular-route" : "libs/angular-route.min",
        "ocLazyLoad":"libs/ocLazyLoad.require.min",
        "jquery":"libs/jquery-2.1.3.min",
        "insuranceFactory" : "services/insuranceFactory",
        "insuranceDirective" : "directives/insuranceDirective",
        "appInsuranceRoute" : "routes/appInsuranceRoute",
        "appInsurance" : "modules/appInsurance"
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
        'appNutrition':['ocLazyLoad'],
    }
});

require(['angular','angular-resource','angular-sanitize','angular-route',
    'ocLazyLoad','jquery','insuranceFactory','insuranceDirective','appInsuranceRoute','appInsurance'],
    function (angular){
        angular.bootstrap(document,["insuranceApp"]);
    });

