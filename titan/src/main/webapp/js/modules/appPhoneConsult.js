/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('phoneConsultApp',['ngResource','ngRoute','ngSanitize','ionic','oc.lazyLoad']);
    return app;
});