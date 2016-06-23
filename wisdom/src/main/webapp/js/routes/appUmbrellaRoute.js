/**
 * 路由
 */
define(['appUmbrella'], function(app){
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
                /* 宝护伞 */
                    .state('umbrellaJoin', {
                        url: '/umbrellaJoin/:id/:shareid',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaJoinCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaJoinCtrl',
                                    ['js/controllers/umbrella/umbrellaJoinCtrl.js?ver='+umbrellaVersion,
                                        'js/libs/moment.min.js',
                                        'styles/umbrella/umbrellaCommon.less?ver='+umbrellaVersion,
                                        'styles/umbrella/umbrellaJoin.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/umbrellaJoin.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('umbrellaFillInfo', {
                        url: '/umbrellaFillInfo/:id/:status',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaFillInfoCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaFillInfoCtrl',
                                    ['js/controllers/umbrella/umbrellaFillInfoCtrl.js?ver='+umbrellaVersion,
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'styles/umbrella/umbrellaCommon.less?ver=',
                                        'styles/umbrella/umbrellaFillInfo.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/umbrellaFillInfo.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('umbrellaMemberAdd', {
                        url: '/umbrellaMemberAdd/:id/:status',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaMemberAddCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaMemberAddCtrl',
                                    ['js/controllers/umbrella/umbrellaMemberAddCtrl.js?ver='+umbrellaVersion,
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'styles/umbrella/umbrellaMemberAdd.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/umbrellaMemberAdd.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('umbrellaMemberList', {
                        url: '/umbrellaMemberList/:id/:status',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaMemberListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaMemberListCtrl',
                                    ['js/controllers/umbrella/umbrellaMemberListCtrl.js?ver='+umbrellaVersion,
                                        'js/libs/moment.min.js',
                                        'styles/umbrella/umbrellaCommon.less?ver='+umbrellaVersion,
                                        'styles/umbrella/umbrellaMemberList.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/umbrellaMemberList.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('umbrellaLead', {
                        url: '/umbrellaLead/:id/:status',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaLeadCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaLeadCtrl',
                                    ['js/controllers/umbrella/umbrellaLeadCtrl.js?ver='+umbrellaVersion,
                                        'styles/umbrella/umbrellaLead.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/umbrellaLead.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('umbrellaTest', {
                        url: '/umbrellaTest',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaTestCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaTestCtrl',
                                    ['js/controllers/umbrella/umbrellaTestCtrl.js?ver='+umbrellaVersion,
                                        'styles/umbrella/umbrellaLead.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/umbrellaTest.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('umbrellaPaySuccess', {
                        url: '/umbrellaPaySuccess/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaPaySuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaPaySuccessCtrl',
                                    ['js/controllers/umbrella/umbrellaPaySuccessCtrl.js?ver='+umbrellaVersion,
                                        'styles/umbrella/umbrellaPaySuccess.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/umbrellaPaySuccess.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })

                $urlRouterProvider.otherwise('umbrellaLead');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
        })
})