/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var user_h5 = ''
var wxChat = '/wechatInfo/'
var healthRecord='healthRecord/'

define(['appUmbrella'], function (app) {
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
        .factory('JoinUs',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/joinUs');
        }])

        //获取用户下宝宝信息
        .factory('getBabyinfoList',['$resource',function ($resource){
            return $resource(healthRecord + 'getBabyinfoList');
        }])

        .factory('getOpenidStatus',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/getOpenidStatus');
        }])

        .factory('ifExistOrder',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/ifExistOrder');
        }])

        //添加宝宝信息
        .factory('saveBabyInfo',['$resource',function ($resource){
            return $resource(healthRecord + 'saveBabyInfo');
        }])
        //修改宝宝信息
        .factory('updateBabyInfo',['$resource',function ($resource){
            return $resource(healthRecord + 'updateBabyInfo');
        }])

        //更新保障信息
        .factory('updateInfo',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/updateInfo');
        }])

        //更新激活时间
        .factory('updateActivationTime',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/updateActivationTime');
        }])

        //保护伞成员列表
        .factory('getFamilyList',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/familyList');
        }])

        //保护伞成员添加
        .factory('addFamily',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/addFamily');
        }])

        //保护伞成员检测
        .factory('checkFamilyMembers',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/checkFamilyMembers');
        }])

        //新版添加保障信息
        .factory('newJoinUs',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/newJoinUs');
        }])

        //新版添加保障信息
        .factory('getUserQRCode',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/getUserQRCode');
        }])
        //获取用户昵称和排名
        .factory('getNickNameAndRanking',['$resource',function ($resource){
            return $resource(user_h5 + 'umbrella/getNickNameAndRanking');
        }])
})
