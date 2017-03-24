/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var activity='punchCard/';

define(['appSign'], function (app) {
    app
        .factory('MakeNewInviteCard',['$resource',function ($resource){
            return $resource(activity + 'makeNewInviteCard');
        }])
        .factory('GetConfig',['$resource',function ($resource){
            return $resource('util/getConfig');
        }])
        .factory('GetPunchCardPage',['$resource',function ($resource){
            return $resource(activity+'getPunchCardPage');
        }])
        .factory('TakePunchCardActivity',['$resource',function ($resource){
            return $resource(activity+'takePunchCardActivity');
        }])
        .factory('FindPunchCardBySelf',['$resource',function ($resource){
            return $resource(activity+'findPunchCardBySelf');
        }])
        .factory('PayPunchCardCash',['$resource',function ($resource){
            return $resource(activity+'payPunchCardCash');
        }])
        .factory('GetPunchCardRewards',['$resource',function ($resource){
            return $resource(activity+'getPunchCardRewards');
        }])
})
