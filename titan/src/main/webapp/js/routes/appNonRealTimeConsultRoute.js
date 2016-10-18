/**
 * 路由
 */
define(['appNonRealTimeConsult'], function(app){
    return app
        .config(['$stateProvider','$urlRouterProvider',
            function($stateProvider,$urlRouterProvider) {
                var loadFunction = function($templateCache, $ocLazyLoad, $q, $http,name,files,htmlURL){
                    lazyDeferred = $q.defer();
                    return $ocLazyLoad.load ({
                        name: name,
                        files: files
                    }).then(function() {
                        return $http.get(htmlURL)
                            .success(function(data, status, headers, config) {
                                return lazyDeferred.resolve(data);
                            }).
                            error(function(data, status, headers, config) {
                                return lazyDeferred.resolve(data);
                            });
                    });
                };

                $stateProvider
                    /* doctor 医生端*/
                    .state('NonTimeDoctorLogin', {
                        url: '/NonTimeDoctorLogin',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeDoctorLoginCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeDoctorLoginCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/nonRealTimeConsult/doctor/NonTimeDoctorLoginCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/doctor/NonTimeDoctorLogin.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/doctor/NonTimeDoctorLogin.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    .state('NonTimeDoctorMessageList', {
                        url: '/NonTimeDoctorMessageList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeDoctorMessageListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeDoctorMessageListCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/nonRealTimeConsult/doctor/NonTimeDoctorMessageListCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/doctor/NonTimeHistoryRecord.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/doctor/NonTimeDoctorMessageList.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    .state('NonTimeDoctorConversation', {
                        url: '/NonTimeDoctorConversation',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeDoctorConversationCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeDoctorConversationCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/libs/scrollglue.js',
                                        'js/controllers/nonRealTimeConsult/doctor/NonTimeDoctorConversationCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/doctor/NonTimeDoctorConversation.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/doctor/NonTimeDoctorConversation.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                   /* 用户端*/
                    /*咨询详情*/
                    .state('NonTimeUserConversation', {
                        url: '/NonTimeUserConversation',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserConversationCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserConversationCtrl',
                                    [
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserConversationCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/user/NonTimeUserConversation.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserConversation.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    /*咨询列表*/
                    .state('NonTimeUserConsultList', {
                        url: '/NonTimeUserConsultList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserConsultListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserConsultListCtrl',
                                    [
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserConsultListCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/doctor/NonTimeHistoryRecord.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserConsultList.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    /*首次咨询*/
                    .state('NonTimeUserFirstConsult', {
                        url: '/NonTimeUserFirstConsult',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserFirstConsultCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserFirstConsultCtrl',
                                    [
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserFirstConsultCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/user/NonTimeUserFirstConsult.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserFirstConsult.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    /*找医生*/
                    .state('NonTimeUserFindDoctor', {
                        url: '/NonTimeUserFindDoctor',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserFindDoctorCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserFindDoctorCtrl',
                                    [
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserFindDoctorCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/user/NonTimeUserFindDoctor.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserFindDoctor.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    /*医生列表*/
                    .state('NonTimeUserDoctorList', {
                        url: '/NonTimeUserDoctorList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserDoctorListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserDoctorListCtrl',
                                    [
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserDoctorListCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/user/NonTimeUserDoctorList.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserDoctorList.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })

                $urlRouterProvider.otherwise('');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
            $rootScope.memberFunction = "false";
        })
})