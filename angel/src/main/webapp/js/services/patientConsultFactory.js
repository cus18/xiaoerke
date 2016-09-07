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

})
