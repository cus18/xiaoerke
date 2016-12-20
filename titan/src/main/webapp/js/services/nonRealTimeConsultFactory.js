/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var path_user = 'nonRealTimeConsultUser/'
var path_doc = 'nonRealTimeConsultDoctor/'
define(['appNonRealTimeConsult'], function (app) {
    app
        .factory('ListHospitalDoctor',['$resource',function ($resource){
            return $resource(path_doc + 'getDoctorListByHospital');
        }])

        .factory('BabyBaseInfo',['$resource',function ($resource){
            return $resource(path_user + 'getBabyBaseInfo');
        }])

        .factory('GetDoctorLoginStatus',['$resource',function ($resource){
            return $resource(path_doc + 'GetDoctorLoginStatus');
        }])
        .factory('CreateSession',['$resource',function ($resource){
            return $resource(path_user + 'createSession');
        }])

        .factory('DepartmentList',['$resource',function ($resource){
            return $resource(path_user + 'getDepartmentList');
        }])

        .factory('StarDoctorList',['$resource',function ($resource){
            return $resource(path_user + 'getStarDoctorlist');
        }])
        .factory('doctorBinding',['$resource',function ($resource){
            return $resource(path_doc + 'doctorBinding');
        }])
        .factory('GetDoctorService',['$resource',function ($resource){
            return $resource(path_doc + 'GetDoctorService');
        }])


        .factory('DoctorListByDepartment',['$resource',function ($resource){
            return $resource(path_user + 'doctorListByDepartment');
        }])

        .factory('UserSessionList',['$resource',function ($resource){
            return $resource(path_user + 'sessionList');
        }])

        .factory('ConversationInfo',['$resource',function ($resource){
            return $resource(path_user + 'conversationInfo');
        }])

        .factory('ConversationDoctorInfo',['$resource',function ($resource){
            return $resource(path_doc + 'conversationDoctorInfo');
        }])

        .factory('UpdateReCode',['$resource',function ($resource){
            return $resource(path_user + 'upadateRecorde');
        }])

        //登陆地址地址
        .factory('LoginUrl', function() {
            return "http://s68.baodf.com/titan/nonRealTimeConsult#/NonTimeDoctorLogin";
        })
        //登陆绑定地址
        .factory('DoctorBindingUrl', function() {
            return "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getDoctorWechatMenId?url=6";
        })
        .factory('GetConfig',['$resource',function ($resource){
            return $resource('util/getConfig');
        }]);

})
