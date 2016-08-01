/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var public = '';
define(['appHeightForecast'], function (app) {
    app
        //保存数据
        .factory('SaveHeightPredictionInfo',['$resource',function ($resource){
            return $resource(public + 'marketing/saveHeightPredictionInfo');
        }])
        //获得openId
        .factory('GetOpenidStatus',['$resource',function ($resource){
            return $resource(public + 'umbrella/getOpenidStatus');
        }])
})
