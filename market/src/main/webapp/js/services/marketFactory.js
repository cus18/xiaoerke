/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
/*var user_h5 = '/xiaoerke-marketing-webapp/'
var wxChat = '/xiaoerke-marketing-webapp/wechatInfo/'*/
var user_h5 = ''
var wxChat = 'wechatInfo/'
var healthRecord='healthRecord/'

define(['appMarket'], function (app) {
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
        //获取妈妈击败%数和查询答题结果
        .factory('GetMarketingActivitiesByOpenid',['$resource',function ($resource){
            return $resource(user_h5 + 'marketing/getMarketingActivitiesByOpenid');
        }])
        //保存妈妈答题结果
        .factory('SaveMarketingActivities',['$resource',function ($resource){
            return $resource(user_h5 + 'marketing/saveMarketingActivities');
        }])
        //保存妈妈分享记录
        .factory('UpdateMarketingActivities',['$resource',function ($resource){
            return $resource(user_h5 + 'marketing/updateMarketingActivities');
        }])
        //保护伞照片墙
        .factory('PhotoWall',['$resource',function ($resource){
            return $resource(user_h5 + 'mutualHelp/donation/photoWall');
        }])
        //保护伞捐款总人数
        .factory('Count',['$resource',function ($resource){
            return $resource(user_h5 + 'mutualHelp/donation/count');
        }])
        //保护伞捐款总钱数
        .factory('SumMoney',['$resource',function ($resource){
            return $resource(user_h5 + 'mutualHelp/donation/sumMoney');
        }])
        //保护伞最新留言
        .factory('LastNote',['$resource',function ($resource){
            return $resource(user_h5 + 'mutualHelp/donation/lastNote');
        }])
        //保护伞最新留言并捐款
        .factory('AddNoteAndDonation',['$resource',function ($resource){
            return $resource(user_h5 + 'mutualHelp/donation/addNoteAndDonation');
        }])
})
