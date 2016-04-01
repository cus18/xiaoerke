/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('constipationApp',['ngResource','ngRoute','ngSanitize','ionic','oc.lazyLoad']);
    return app;
});