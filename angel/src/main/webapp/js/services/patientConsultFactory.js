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
})
