/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var user_h5 = '/xiaoerke-marketing-webapp/'
var wxChat = '/xiaoerke-marketing-webapp/wechatInfo/'
//var user_h5 = '';

define(['appHeightForecast'], function (app) {
    app
        //保护伞照片墙
        .factory('PhotoWall',['$resource',function ($resource){
            return $resource(user_h5 + 'mutualHelp/donation/photoWall');
        }])
})
