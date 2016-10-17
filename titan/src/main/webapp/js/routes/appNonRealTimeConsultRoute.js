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
                    /* phoneConsult 电话咨询*/
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
                                        'styles/nonRealTimeConsult/doctor/NonTimeDoctorMessageList.less?ver='+nonRealTimeConsultVersion],
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
                                        'js/controllers/nonRealTimeConsult/doctor/NonTimeDoctorConversationCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/doctor/NonTimeDoctorConversation.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/doctor/NonTimeDoctorConversation.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                   /* 用户端*/
                    .state('NonTimeUserConversation', {
                        url: '/NonTimeUserConversation',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserConversationCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserConversationCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserConversationCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/user/NonTimeUserConversation.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserConversation.html?ver='+nonRealTimeConsultVersion);
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