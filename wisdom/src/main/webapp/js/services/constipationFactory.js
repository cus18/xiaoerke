/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var user_h5 = ''
var wxChat = '/wechatInfo/'

define(['appConstipation'], function (app) {
    app
        //健康管理查看评论
        .factory('AppraisalList',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/appraisal/appraisalList');
        }])
        //健康管理添加评论
        .factory('SaveAppraisal',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/appraisal/saveAppraisal');
        }])
        //健康管理查看打卡详情
        .factory('ConfirmDetail',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/confirmDetail');
        }])
        //健康管理查看打卡情况
        .factory('GetConfirmSituation',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/getConfirmSituation');
        }])
        //健康管理中短期处理
        .factory('Handle',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/handle');
        }])
        //健康管理取消计划
        .factory('CancelPlan',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/cancelPlan');
        }])
        //健康管理获取任务列表
        .factory('GetTasksList',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/getTasksList');
        }])
        //健康管理获取任务详情
        .factory('GetTasksInfo',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/getTasksInfo');
        }])
        //健康管理提醒设置
        .factory('UpdatePlanTask',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/updatePlanTask');
        }])
        //健康管理任务打卡
        .factory('PushTicket',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/pushTicket');
        }])
        //健康管理获取任务模板
        .factory('GetTemplateTasks',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/getTemplateTasks');
        }])
        //健康管理获取食材
        .factory('GetFoodList',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/getFoodList');
        }])
        //健康管理保存食材
        .factory('SaveFoodList',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/saveFoodList');
        }])
        //健康管理获取饮食打卡
        .factory('GetDietList',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/getDietList');
        }])
        //健康管理计划打卡
        .factory('TaskListConfirm',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/taskListConfirm');
        }])
        //健康管理购买链接
        .factory('SaveShopping',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/saveShopping');
        }])
        //健康管理修改计划更新时间，用于周期提示打卡情况
        .factory('UpdatePlanInfoForCycleTip',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/updatePlanInfoForCycleTip');
        }])
        //健康管理制定计划等级
        .factory('GetQuestion',['$resource',function ($resource){
            return $resource(user_h5 + 'constipation/getquestion');
        }])
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
        //营养管理保存用户管理信息,点击首页加号时添加
        .factory('SaveManagementInfo',['$resource',function ($resource){
            return $resource(user_h5 + 'nutrition/saveManagementInfo');
        }])
        //send WechatMessage To User
        .factory('SendWechatMessageToUser',['$resource',function ($resource){
            return $resource(wxChat + 'postInfoToWechat');
        }])
        .factory('SendWechatMessageToUserOnline',['$resource',function ($resource){
            return $resource(wxChat + 'postInfoToWechatOnline');
        }])

})
