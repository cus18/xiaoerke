var user_h5 = ''
var wxChat = '/wechatInfo/'

define(['appPlayTour'], function (app) {
    app
        //获取用户登陆状态
        .factory('modifyBabyIndfo',['$resource',function ($resource){
            return $resource(user_h5 + 'healthRecord/modifyBabyIndfo');
        }])
         .factory('findCustomerEvaluation',['$resource',function ($resource){
            return $resource(user_h5 + 'interaction/user/findCustomerEvaluation');
        }])
        .factory('updateCustomerEvaluation',['$resource',function ($resource){
            return $resource(user_h5 + 'interaction/user/updateCustomerEvaluation');
        }])
        //非实时咨询确认
        .factory('ConfirmInstantConsultation', ['$resource', function ($resource) {
            return $resource('consult/wechat/confirmInstantConsultation');
        }])
        //邀请页面
        .factory('GetAttentionInfo', ['$resource', function ($resource) {
            return $resource('patient/getAttentionInfo');
        }])
        .factory('GetUserOpenId', ['$resource', function ($resource) {
            return $resource('util/getOpenid');
        }])
        .factory('GetBabyCoinInfo', ['$resource', function ($resource) {
            return $resource('babyCoin/getBabyCoinInfo');
        }])
        .factory('BabyCoinInit',['$resource',function ($resource){
            return $resource('babyCoin/babyCoinInit');
        }])
        .factory('GetConfig',['$resource',function ($resource){
            return $resource('util/getConfig');
        }])
        .factory('CreateInviteCard', ['$resource', function ($resource) {
            return $resource('babyCoin/createInviteCard');
        }])
        .factory('sendMindCouponList',['$resource',function ($resource){
            return $resource(user_h5 + 'babyCoin/sendMindCouponList');
        }])

        .factory('exchangeCoupon',['$resource',function ($resource){
            return $resource(user_h5 + 'babyCoin/exchangeCoupon');
        }]);


})
