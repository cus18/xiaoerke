var user_h5 = ''

define(['appPatientConsult'], function (app) {
    app
        .factory('GetSessionId',['$resource',function ($resource){
            return $resource('consult/getSessionId');
        }])
        //获取用户登陆状态
        .factory('GetUserLoginStatus', ['$resource', function ($resource) {
            return $resource('info/loginStatus');
        }])
        .factory('UploadMediaFile', ['$resource', function ($resource) {
            return $resource('consult/uploadMediaFile');
        }])
})
