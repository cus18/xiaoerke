/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('patientConsultApp',['ngResource','ui.router','ngSanitize','oc.lazyLoad']);
    return app;
});