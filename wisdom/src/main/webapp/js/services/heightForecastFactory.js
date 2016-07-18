/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var public = '';
define(['appHeightForecast'], function (app) {
    app
        //保护伞照片墙
        .factory('SaveHeightPredictionInfo',['$resource',function ($resource){
            return $resource(public + 'marketing/saveHeightPredictionInfo');
        }])
})
