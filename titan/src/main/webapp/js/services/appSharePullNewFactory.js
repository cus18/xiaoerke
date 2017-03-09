/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var activity='activity/'

define(['appSharePullNew'], function (app) {
    app
        .factory('GetConfig',['$resource',function ($resource){
            return $resource('util/getConfig');
        }])

})
