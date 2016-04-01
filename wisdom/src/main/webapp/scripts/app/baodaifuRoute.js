angular.module('app', ['angularFileUpload', 'ui.router', 'ui.bootstrap', 'controllers', 'directives', 'services'])
    .config(function ($httpProvider, $stateProvider, $urlRouterProvider) {
        var root = '/xiaoerke-knowledge/scripts/views';
        $stateProvider
            .state('baodaifuIndex', {
                url: '/baodaifuIndex',
                templateUrl: root + '/baodaifuIndex.html',
                controller: 'baodaifuIndexCtrl',
                title: '运营数据统计'
            })
            .state('zixundaifu',{
            url:'/zixundaifu',
             templateUrl: root + '/zixundaifu.html',
             controller: 'zixundaifuCtrl',
                title: '咨询大夫'
            })
            .state('yuyuedaifu',{
                url:'/yuyuedaifu',
                templateUrl: root + '/yuyuedaifu.html',
                controller: 'yuyuedaifuCtrl',
                title: '预约大夫'
            })
            .state('shuo',{
                url:'/shuo',
                templateUrl: root + '/shuo.html',
                controller: 'shuoCtrl',
                title: '预约大夫'
            })
            .state('zixun',{
                url:'/zixun',
                templateUrl: root + '/zixun.html',
                controller: 'zixunCtrl',
                title: '媒体咨讯'
            })
            .state('about',{
                url:'/about',
                templateUrl: root + '/about.html',
                controller: 'aboutCtrl',
                title: '关于我们'
            })
            .state('remen',{
                url:'/remen',
                templateUrl: root + '/remen.html',
                controller: 'remenCtrl',
                title: '预约大夫',
                publicAccess: true
            })
            .state('remenArticle',{
                url:'/remenArticle/:id',
                templateUrl: root + '/remenArticle.html',
                controller: 'remenArticleCtrl',
                title: '预约大夫',
                publicAccess: true
            })
            .state('wenda',{
                url:'/wenda',
                templateUrl: root + '/wenda.html',
                controller: 'wendaCtrl',
                title: '问答',
                publicAccess: true
            })
            .state('wendaArticle',{
                url:'/wendaArticle/:id',
                templateUrl: root + '/wendaArticle.html',
                controller: 'wendaArticleCtrl',
                title: '问答文章',
                publicAccess: true
            })
            .state('content',{
                url:'/content/:id',
                templateUrl: root + '/content.html',
                controller: 'contentCtrl',
                title: '育儿经详细内容',
                publicAccess: true
            })
            .state('hotthing',{
                url:'/hotthing/:id',
                templateUrl: root + '/hotthing.html',
                controller: 'hotthingCtrl',
                title: '育儿经详细内容',
                publicAccess: true
            })
            .state('contactUs',{
                url:'/contactUs',
                templateUrl: root + '/contactUs.html',
                controller: 'contactUsCtrl',
                title: '联系我们',
                publicAccess: true
            })
            .state('productIntroduction',{
                url:'/productIntroduction',
                templateUrl: root + '/productIntroduction.html',
                controller: 'productIntroductionCtrl',
                title: '产品简介',
                publicAccess: true
            })
            .state('yinsibaohu',{
                url:'/yinsibaohu',
                templateUrl: root + '/yinsibaohu.html',
                controller: 'yinsibaohuCtrl',
                title: '隐私保护',
                publicAccess: true
            })
            .state('fuwutiaokuan',{
                url:'/fuwutiaokuan',
                templateUrl: root + '/fuwutiaokuan.html',
                controller: 'fuwutiaokuanCtrl',
                title: '服务条款',
                publicAccess: true
            })
        $urlRouterProvider.otherwise('baodaifuIndex');
    })