var user_h5 = 'ap/'

define(['appPatientConsult'], function (app) {
    app
        .factory('GetSessionId',['$resource',function ($resource){
            return $resource(user_h5 + 'consult/getSessionId');
        }])
        //获取用户登陆状态
        .factory('GetUserLoginStatus', ['$resource', function ($resource) {
            return $resource(user_h5 + 'info/loginStatus');
        }])
        .factory('UploadMediaFile', ['$resource', function ($resource) {
            return $resource(user_h5 + 'consult/uploadMediaFile');
        }])
})
