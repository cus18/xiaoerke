angular.module('app', ['angularFileUpload', 'ui.router', 'ui.bootstrap', 'controllers', 'directives', 'services'])
    .config(function ($httpProvider, $stateProvider, $urlRouterProvider) {
        var root = '/xiaoerke-wxapp/scripts/views';
        $stateProvider
            .state('statistic', {
                url: '/statistic',
                templateUrl: root + '/statisticdata.html',
                controller: 'statistic',
                title: '运营数据统计'
            });
        $urlRouterProvider.otherwise('statistic');
    })