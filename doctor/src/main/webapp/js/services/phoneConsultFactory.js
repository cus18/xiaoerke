/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */

define(['phoneConsultApp'], function (app) {
    app
        //获取用户登陆状态
        .factory('GetUserLoginStatus', ['$resource', function ($resource) {
            return $resource('auth/info/loginStatus');
        }])
        //获取某医生的评价
        .factory('GetUserEvaluate',['$resource',function ($resource){
            return $resource('interaction/user/doctorEvaluate');
        }])
        //获取某医生的号源日期
        .factory('GetConsultInfo',['$resource',function ($resource){
            return $resource('consultPhone/consultPhoneDoctor/getConsultInfo');
        }])
        //获取某医生的号源日期的具体信息
        .factory('GetConsultTime',['$resource',function ($resource){
            return $resource('consultPhone/user/doctor/time');
        }])
});
