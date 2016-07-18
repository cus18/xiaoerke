/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('umbrellaApp2',['ngResource','ngRoute','ngSanitize','ionic','oc.lazyLoad']);
    return app;
});
