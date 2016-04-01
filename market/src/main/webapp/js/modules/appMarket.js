/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('marketApp',['ngResource','ngRoute','ngSanitize','ionic','oc.lazyLoad']);
    return app;
});