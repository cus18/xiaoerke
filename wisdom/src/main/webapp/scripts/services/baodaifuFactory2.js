var public = '/wisdom/'

angular.module('services2', ['ngResource'])
    //获得文章内容列表
    .factory('getArticleList', ['$resource', function ($resource) {
        return $resource(public + 'knowledge/article/articleList')
    }])
    //查询文章内容接口
    .factory('GetArticleDetail',['$resource',function ($resource){
        return $resource(public + 'knowledge/article/articleDetail');
    }]);
