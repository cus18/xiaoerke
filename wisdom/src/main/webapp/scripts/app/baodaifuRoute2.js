angular.module('app2', ['angularFileUpload', 'ui.router', 'ui.bootstrap', 'controllers2', 'directives2', 'services2'])
    .config(function ($httpProvider, $stateProvider, $urlRouterProvider) {
        var root = '/wisdom/scripts/views/bdf2';
        var css = '/wisdom/scripts/styles/bdf2';

        $stateProvider
            .state('index', {
                url: '/index',
                templateUrl: root + '/index.html',
                controller: 'indexCtrl',
                title: '新页面'
            })

        $urlRouterProvider.otherwise('index');
    })