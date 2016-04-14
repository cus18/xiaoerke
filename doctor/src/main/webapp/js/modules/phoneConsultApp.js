/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('AppPhoneConsult',['ngResource','ngRoute','ngSanitize','ionic','oc.lazyLoad']);
    return app;
});