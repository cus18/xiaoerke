/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var activity='activity/'

define(['appWfdb'], function (app) {
    app
        .factory('GetCardInfoList',['$resource',function ($resource){
            return $resource(activity + 'getCardInfoList');
        }])
        .factory('ChooseCard',['$resource',function ($resource){
            return $resource(activity + 'chooseCard');
        }])
        .factory('UpdateRedPackageInfo',['$resource',function ($resource){
            return $resource(activity + 'updateRedPackageInfo');
        }])
        .factory('CreateInviteCardInfo',['$resource',function ($resource){
            return $resource(activity + 'createInviteCardInfo');
        }])
        .factory('GetConfig',['$resource',function ($resource){
            return $resource('util/getConfig');
        }])
        .factory('RedPacketCreate',['$resource',function ($resource){
            return $resource(user_h5 + 'babyCoin/redPacketCreate');
        }])

})
