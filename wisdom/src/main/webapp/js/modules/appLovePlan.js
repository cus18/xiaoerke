/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('lovePlanApp',['ngResource','ngRoute','ngSanitize','ionic','oc.lazyLoad']);
    return app;
});
