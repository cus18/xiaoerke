/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var user_h5 = ''
var wxChat = 'wechatInfo/'
var antiDog='insurance/'
var healthRecord='healthRecord/'

define(['appInsurance'], function (app) {
    app

        //获取用户登陆状态
        .factory('GetUserLoginStatus', ['$resource', function ($resource) {
            return $resource(user_h5 + 'auth/info/loginStatus');
        }])
        //发送验证码
        .factory('IdentifyUser', ['$resource', function ($resource) {
            return $resource(user_h5 + 'util/user/getCode');
        }])
        .factory('RecordLogs',['$resource',function ($resource){
            return $resource(user_h5 + 'util/recordLogs');
        }])

        //添加宝宝信息
        .factory('saveBabyInfo',['$resource',function ($resource){
            return $resource(healthRecord + 'saveBabyInfo');
        }])
        //send WechatMessage To User
        .factory('SendWechatMessageToUser',['$resource',function ($resource){
            return $resource(wxChat + 'postInfoToWechat');
        }])
        .factory('SendWechatMessageToUserOnline',['$resource',function ($resource){
            return $resource(wxChat + 'postInfoToWechatOnline');
        }])
         .factory('getBabyinfoList',['$resource',function ($resource){
            return $resource(healthRecord + 'getBabyinfoList');
        }])
         .factory('getInsuranceRegisterServiceVisitLeadPageLogByOpenid',['$resource',function ($resource){
            return $resource(antiDog + 'getInsuranceRegisterServiceVisitLeadPageLogByOpenid');
        }])
         .factory('getInsuranceRegisterServiceByOpenid',['$resource',function ($resource){
            return $resource(antiDog + 'getInsuranceRegisterServiceByOpenid');
        }])
         .factory('getInsuranceRegisterServiceById',['$resource',function ($resource){
            return $resource(antiDog + 'getInsuranceRegisterServiceById');
        }])
         .factory('getInsuranceRegisterServiceListByUserid',['$resource',function ($resource){
            return $resource(antiDog + 'getInsuranceRegisterServiceListByUserid');
        }])
        .factory('getInsuranceHospitalListByInfo',['$resource',function ($resource){
            return $resource(antiDog + 'getInsuranceHospitalListByInfo');
        }])
        //获取类保险价格和保险名称
        .factory('getInsuranceInfo',['$resource',function ($resource){
            return $resource(antiDog + 'getInsuranceInfo');
        }])
})
