var user_h5 = '/xiaoerke-wxapp/ap/'
var wxChat = '/xiaoerke-wxapp/ap/wechatInfo/'

define(['appPlayTour'], function (app) {
    app
        //获取用户登陆状态
        .factory('modifyBabyIndfo',['$resource',function ($resource){
            return $resource(user_h5 + 'healthRecord/modifyBabyIndfo');
        }])
         .factory('findCustomerEvaluation',['$resource',function ($resource){
            return $resource(user_h5 + 'interaction/user/findCustomerEvaluation');
        }])


})
