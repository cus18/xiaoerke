/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('umbrellaApp',['ngResource','ngRoute','ngSanitize','ionic','oc.lazyLoad']);
    return app;
});