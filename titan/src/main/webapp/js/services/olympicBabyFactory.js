/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var user_h5 = ''
var user_appoint = ''
var wxChat = 'wechatInfo/'

define(['appOlympicBaby'], function (app) {
    app
        .factory('ListHospitalDoctor',['$resource',function ($resource){
            return $resource(user_h5 + 'consultPhoneDoctor/getDoctorListByHospital');
        }])




})
