/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */

var common ='vaccine/';

define(['appVaccine'], function (app) {
    app
        .factory('GetVaccineStation',['$resource',function ($resource){
            return $resource(common + 'getVaccineStation');
        }])
        .factory('SaveBabyVaccine',['$resource',function ($resource){
            return $resource(common + 'saveBabyVaccine');
        }])

});






