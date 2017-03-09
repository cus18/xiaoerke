/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var activity='cheakHusband/'

define(['appHusband'], function (app) {
    app
        .factory('GetCardInfoList',['$resource',function ($resource){
            return $resource(activity + 'getCardInfoList');
        }])
        .factory('GetConfig',['$resource',function ($resource){
            return $resource('util/getConfig');
        }])
        .factory('CheackAttention',['$resource',function ($resource){
            return $resource('isAttention');
        }])
})
