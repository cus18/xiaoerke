/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */

var doctor ='consult/doctor/';

define(['appConsultDoctorHome'], function (app) {
    app
        .factory('GetConsultDoctorHomepageInfo',['$resource',function ($resource){
            return $resource(doctor + 'getConsultDoctorHomepageInfo');
        }])
        .factory('FindDoctorAllEvaluation',['$resource',function ($resource){
            return $resource(doctor + 'findDoctorAllEvaluation');
        }])

        .factory('CreateConnect',['$resource',function ($resource){
            return $resource('consultPhone/consultOrder/getConnect');
        }]);
    
});

