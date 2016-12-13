/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */

var common ='vaccine/';

var appName = 'vaccineUser/';

define(['appVaccine'], function (app) {
    app
        .factory('GetVaccineStation',['$resource',function ($resource){
            return $resource(common + 'getVaccineStation');
        }])
        .factory('SaveBabyVaccine',['$resource',function ($resource){
            return $resource(common + 'saveBabyVaccine');
        }])
        .factory('GetVaccineCodeList',['$resource',function ($resource){
            return $resource(appName + 'getVaccineNameList');
        }])
        .factory('GetOneVaccineInfoList',['$resource',function ($resource){
            return $resource(appName + 'getVaccineInfoList');
        }])
});






