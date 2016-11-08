/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('nonRealTimeConsultApp',['ngResource','ngRoute','ngSanitize','ionic','oc.lazyLoad']);
    return app;
});