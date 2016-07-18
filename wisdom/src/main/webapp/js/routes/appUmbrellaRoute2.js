/**
 * 路由
 */
define(['appUmbrella2'], function(app){
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
                    .state('umbrellaLead', {
                        url: '/umbrellaLead',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaLeadCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaLeadCtrl',
                                    ['js/controllers/umbrellaApp/umbrellaLeadCtrl.js?ver='+umbrellaVersion,
                                        'styles/umbrellaApp/umbrellaLead.less?ver='+umbrellaVersion],
                                    'js/views/umbrellaApp/umbrellaLead.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('umbrellaIndex', {
                        url: '/umbrellaIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaIndexCtrl',
                                    ['js/controllers/umbrellaApp/umbrellaIndexCtrl.js?ver='+umbrellaVersion,
                                     'styles/umbrellaApp/umbrellaIndex.less?ver='+umbrellaVersion,
                                    'styles/umbrellaApp/umbrellaCommon.less?ver='+umbrellaVersion],
                                    'js/views/umbrellaApp/umbrellaIndex.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('umbrellaFillInfo', {
                        url: '/umbrellaFillInfo',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaFillInfoCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaFillInfoCtrl',
                                    ['js/controllers/umbrellaApp/umbrellaFillInfoCtrl.js?ver='+umbrellaVersion,
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'js/libs/moment.min.js',
                                        'styles/umbrellaApp/umbrellaCommon.less?ver=',
                                        'styles/umbrellaApp/umbrellaFillInfo.less?ver='+umbrellaVersion],
                                    'js/views/umbrellaApp/umbrellaFillInfo.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('umbrellaPay', {
                        url: '/umbrellaPay/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaPayCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaPayCtrl',
                                    ['js/controllers/umbrellaApp/umbrellaPayCtrl.js?ver='+umbrellaVersion,
                                        'styles/umbrellaApp/umbrellaPay.less?ver='+umbrellaVersion],
                                    'js/views/umbrellaApp/umbrellaPay.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('umbrellaAppPaySuccess', {
                        url: '/umbrellaAppPaySuccess',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaAppPaySuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaAppPaySuccessCtrl',
                                    ['js/controllers/umbrellaApp/umbrellaAppPaySuccessCtrl.js?ver='+umbrellaVersion,
                                        'styles/umbrellaApp/umbrellaAppPaySuccess.less?ver='+umbrellaVersion
                                    ],
                                    'js/views/umbrellaApp/umbrellaAppPaySuccess.html?ver='+umbrellaVersion);
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
