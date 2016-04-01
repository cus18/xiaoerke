/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var user_h5 = '/xiaoerke-knowledge/ap/'
var wxChat = '/xiaoerke-knowledge/ap/wechatInfo/'

define(['app'], function (app) {
    app
        //根据文章栏目或标题查询文章接口
        .factory('GetArticleList',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/article/articleList');
        }])
        //查询栏目列表接口
        .factory('GetCategoryList',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/category/categoryList');
        }])
        //查询文章内容接口
        .factory('GetArticleDetail',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/article/articleDetail');
        }])
        // 郑玉巧说文章记录
        .factory('ArticleShareRecord',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/ArticleShareRecord');
        }])
        .factory('ArticleZanRecord',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/ArticleZanRecord');
        }])
        //郑玉巧育儿经评论保存
        .factory('saveComment',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/comment/save');
        }])
        //郑玉巧育儿经获取评论
        .factory('listComment',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/comment/list');
        }])
        //郑玉巧育儿经每日提醒
        .factory('dailyRemind',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/knowledgeDict/dailyRemind');
        }])
        //郑玉巧育儿经登录页面保存宝宝信息
        .factory('saveBabyEmr',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/babyEmr/save');
        }])
        //郑玉巧育儿经身高体重
        .factory('standardFigure',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/knowledgeDict/standardFigure');
        }])
        //修改宝宝生日
        .factory('updateBabyBirthday',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/babyEmr/updateBabyEmr');
        }])
        //修改宝宝头像
        .factory('updateBabyPic',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/babyEmr/updatePic');
        }])
        //获得宝宝是否登录信息
        .factory('getBabyEmrList',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/babyEmr/getBabyEmrList');
        }])
        //获取今日精选和今日必读文章
        .factory('gettodaySelectAndReadArticleList',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/article/todaySelectAndReadArticleList');
        }])
        //根据一个目录的id，获取该目录下的目录列表
        .factory('findByParentId',['$resource',function ($resource){
            return $resource(user_h5 + 'knowledge/category/findByParentId');
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
        //获取医院的某个科室的医生
        .factory('ListHospitalDepartmentDoctor', ['$resource', function ($resource) {
            return $resource(user_h5 + 'sys/hospital/department/doctor');
        }])
})
