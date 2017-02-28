/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('appWfdb',['ngResource','ngRoute','ngSanitize',
        'ionic','oc.lazyLoad']);
    return app;
});