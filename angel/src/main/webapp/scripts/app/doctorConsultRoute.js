angular.module('doctorConsultApp', ['angularFileUpload', 'ui.router','controllers', 'directives', 'services'])
    .config(function ($httpProvider, $stateProvider, $urlRouterProvider) {
        var root = '/angel/scripts/views';
        $stateProvider
            .state('doctorConsultFirst', {
                url: '/doctorConsultFirst/:action,:userId',
                templateUrl: root + '/doctorConsultFirst.html',
                controller: 'doctorConsultFirstCtrl',
                title: '医生咨询客户端首页'
            })
            .state('messageList',{
                url:'/messageList',
                templateUrl: root + '/messageList.html',
                controller: 'messageListCtrl',
                title: '消息列表'
            })
        $urlRouterProvider.otherwise('doctorConsultFirst');
    })