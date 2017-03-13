/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var activity='advisoryShare/'

define(['appSharePullNew'], function (app) {
    app
        .factory('GetConfig',['$resource',function ($resource){
            return $resource('util/getConfig');
        }])
        .factory('ConversationRecord',['$resource',function ($resource){
            return $resource(activity+'conversationRecord');
        }])
        .factory('SharSeConsult',['$resource',function ($resource){
            return $resource(activity+'sharSeConsult');
        }])

        .factory('BabyEnglish',['$resource',function ($resource){
            return $resource('cheakHusband/babyEnglish');
        }])

})
