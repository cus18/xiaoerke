/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('olympicBabyApp',['ngResource','ngRoute','ui.router','ngSanitize','oc.lazyLoad']);
    return app;
});