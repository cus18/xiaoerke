/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var activity='activity/'

define(['appHusband'], function (app) {
    app
        .factory('GetCardInfoList',['$resource',function ($resource){
            return $resource(activity + 'getCardInfoList');
        }])
})
