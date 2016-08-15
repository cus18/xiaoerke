/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var user_h5 = ''
var wxChat = '/xiaoerke-wxapp/wechatInfo/'

define(['app'], function (app) {
    app
        //获取用户登陆状态
        .factory('GetUserLoginStatus', ['$resource', function ($resource) {
            return $resource(user_h5 + 'info/loginStatus');
        }])

        //发送验证码
        .factory('IdentifyUser', ['$resource', function ($resource) {
            return $resource(user_h5 + 'util/user/getCode');
        }])
        
        .factory('modifyBabyIndfo',['$resource',function ($resource){
            return $resource(user_h5 + 'healthRecord/modifyBabyIndfo');
        }])
        .factory('updateConversationInfo',['$resource',function ($resource){
            return $resource(user_h5 + 'healthRecord/updateConversationInfo');
        }])
        .factory('getCustomerLogByOpenID',['$resource',function ($resource){
            return $resource(user_h5 + 'customer/getCustomerLogByOpenID');
        }])
        //获取用户下宝宝信息
        .factory('getBabyinfoList',['$resource',function ($resource){
            return $resource(user_h5 + 'healthRecord/getBabyinfoList');
        }])
	    .factory('saveBabyInfo',['$resource',function ($resource){
	        return $resource(user_h5 + 'healthRecord/saveBabyInfo');
	    }])

        .factory('addMePermTimes',['$resource',function ($resource){
            return $resource(user_h5 + 'consult/user/addMePermTimes');
        }]);
})
