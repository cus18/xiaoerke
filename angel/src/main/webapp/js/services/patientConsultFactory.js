define(['appPatientConsult'], function (app) {

    app
        .factory('GetSessionId',['$resource',function ($resource){
            return $resource('consult/user/getSessionId');
        }])
        .factory('UploadMediaFile', ['$resource', function ($resource) {
            return $resource('consult/user/uploadMediaFile');
        }])
        //获取用户登陆状态
        .factory('GetUserLoginStatus', ['$resource', function ($resource) {
            return $resource('auth/info/loginStatus');
        }])
        //获取用户的openId
        .factory('GetUserOpenId', ['$resource', function ($resource) {
            return $resource('util/getOpenid');
        }])
        //只咨询医生
        .factory('ConsultCustomOnly', ['$resource', function ($resource) {
            return $resource('consult/wechat/consultCustomOnly');
        }])
        .factory('CreateOrUpdateWJYPatientInfo', ['$resource', function ($resource) {
            return $resource('consult/user/createOrUpdateWJYPatientInfo');
        }])
        .factory('GetUserCurrentConsultContent', ['$resource', function ($resource) {
            return $resource('consult/user/getUserCurrentConsultContent');
        }])
        //获取wjy用户消息聊天记录
        .factory('GetWJYHistoryRecord', ['$resource', function ($resource) {
            return $resource('consult/cooperate/getHistoryRecord');
        }])
        .factory('CreateInviteCard', ['$resource', function ($resource) {
            return $resource('babyCoin/createInviteCard');
        }])
        .factory('GetAttentionInfo', ['$resource', function ($resource) {
            return $resource('patient/getAttentionInfo');
        }])
        .factory('GetBabyCoinInfo', ['$resource', function ($resource) {
            return $resource('babyCoin/getBabyCoin');
        }])

        //非实时咨询确认
        .factory('ConfirmInstantConsultation', ['$resource', function ($resource) {
            return $resource('consult/wechat/confirmInstantConsultation');
        }])
        //获取用户宝宝币
        .factory('BabyCoinInit',['$resource',function ($resource){
            return $resource('babyCoin/babyCoinInit');
        }])



        //公用数据邀请分享地址
        .factory('inviteUrlData', function() {
            var inviteUrl = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=41,";
            return inviteUrl;
        });

})
