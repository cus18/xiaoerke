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
                                    ['js/controllers/umbrella/umbrellaJoinCtrl.js',
                                        'js/libs/moment.min.js',
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
                                    ['js/controllers/umbrella/umbrellaFillInfoCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'styles/umbrella/fillInfoCommon.less?ver='+umbrellaVersion,
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
                                    ['js/controllers/umbrella/umbrellaMemberAddCtrl.js',
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
                                    ['js/controllers/umbrella/umbrellaMemberListCtrl.js',
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
                $urlRouterProvider.otherwise('umbrellaJoin');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
        })
})