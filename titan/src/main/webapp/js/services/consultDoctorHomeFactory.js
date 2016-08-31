/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */

define(['appConsultDoctorHome'], function (app) {
    app
        .factory('AccountInfo',['$resource',function ($resource){
            return $resource(user_h5 + 'account/user/accountInfo');
        }])
    
});

