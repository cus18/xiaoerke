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
        .factory('CreateSession',['$resource',function ($resource){
            return $resource(path_user + 'createSession');
        }])

        .factory('DepartmentList',['$resource',function ($resource){
            return $resource(path_user + 'getDepartmentList');
        }])

        .factory('StarDoctorlist',['$resource',function ($resource){
            return $resource(path_user + 'getStarDoctorlist');
        }])

        .factory('DoctorListByDepartment',['$resource',function ($resource){
            return $resource(path_user + 'doctorListByDepartment');
        }])


})
