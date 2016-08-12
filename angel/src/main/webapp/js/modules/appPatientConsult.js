/**
 * 建立angular.module
 */

define(['angular'], function (angular) {
    var app = angular.module('patientConsultApp',['ngResource','ngRoute','ui.router','ngSanitize','ionic','oc.lazyLoad']);
    return app;
});