/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var user_h5 = '/xiaoerke-healthPlan/ap/'
var wxChat = '/xiaoerke-healthPlan/ap/wechatInfo/'

define(['appNutrition'], function (app) {
    app
        //获取用户登陆状态
        .factory('GetUserLoginStatus', ['$resource', function ($resource) {
            return $resource(user_h5 + 'info/loginStatus');
        }])
        //发送验证码
        .factory('IdentifyUser', ['$resource', function ($resource) {
            return $resource(user_h5 + 'util/user/getCode');
        }])
        .factory('RecordLogs',['$resource',function ($resource){
            return $resource(user_h5 + 'util/recordLogs');
        }])
        //send WechatMessage To User
        .factory('SendWechatMessageToUser',['$resource',function ($resource){
            return $resource(wxChat + 'postInfoToWechat');
        }])
        .factory('SendWechatMessageToUserOnline',['$resource',function ($resource){
            return $resource(wxChat + 'postInfoToWechatOnline');
        }])
        //营养管理保存宝宝信息
        .factory('SaveBabyInfonutrition',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/saveBabyInfo');
        }])
        //营养管理判断用户创建了哪个管理
        .factory('JudgeUserManage',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/judgeUserManage');
        }])
        //营养管理保存用户管理信息,点击首页加号时添加
        .factory('SaveManagementInfo',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/saveManagementInfo');
        }])
        //营养管理返回宝宝信息
        .factory('GetBabyInfo',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/getBabyInfo');
        }])
        //营养管理修改宝宝信息
        .factory('UpdateBabyInfo',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/updateBabyInfo');
        }])
        //营养管理获取饮食食谱
        .factory('GetRecipes',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/getRecipes');
        }])
        //营养管理获取文章
        .factory('GetTodayRead',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/getTodayRead');
        }])
        //营养管理保存和修改评估记录
        .factory('SaveUpdateEvaluate',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/saveUpdateEvaluate');
        }])
        //营养管理获取评估记录
        .factory('GetEvaluate',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/getEvaluate');
        }])
        //营养管理推送消息提醒
        .factory('UpdateSendWechatMessage',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/updateSendWechatMessage');
        }])
        //营养管理重新评估
        .factory('ReEvaluate',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/reEvaluate');
        }])
        //营养管理添加评论
        .factory('SaveAppraisal',['$resource',function ($resource){
            return $resource(user_h5 + 'plan/appraisal/saveAppraisal');
        }])
})
