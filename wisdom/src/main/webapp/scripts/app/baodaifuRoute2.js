angular.module('app2', ['angularFileUpload', 'ui.router', 'ui.bootstrap', 'controllers2', 'directives2', 'services2'])
    .config(function ($httpProvider, $stateProvider, $urlRouterProvider) {
        var root = '/wisdom/scripts/views/bdf2';
        var css = '/wisdom/scripts/styles/bdf2';

        $stateProvider
            .state('index', {
                url: '/index',
                templateUrl: root + '/index.html',
                controller: 'indexCtrl',
                title: '首页'
            })
            .state('callMine', {
                url: '/callMine',
                templateUrl: root + '/callMine.html',
                controller: 'callMineCtrl',
                title: '联系我们'
            })

        $urlRouterProvider.otherwise('index');
    })