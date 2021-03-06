/**
 * 路由
 */
define(['appLovePlan'], function(app){
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
               /* 宝妈爱心接力*/
                .state('lovePlanPoster', {
                        url: '/lovePlanPoster',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'lovePlanPosterCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.lovePlanPosterCtrl',
                                    ['js/controllers/lovePlan/lovePlanPosterCtrl.js',
                                        'styles/lovePlan/lovePlanPoster.less?ver='+marketVersion
                                    ],
                                    'js/views/lovePlan/lovePlanPoster.html?ver='+marketVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                .state('lovePlanList', {
                        url: '/lovePlanList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'lovePlanListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.lovePlanListCtrl',
                                    ['js/controllers/lovePlan/lovePlanListCtrl.js',
                                        'styles/lovePlan/lovePlanList.less?ver='+marketVersion
                                    ],
                                    'js/views/lovePlan/lovePlanList.html?ver='+marketVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                .state('lovePlanPaySuccess', {
                        url: '/lovePlanPaySuccess',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'lovePlanPaySuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.lovePlanPaySuccessCtrl',
                                    ['js/controllers/lovePlan/lovePlanPaySuccessCtrl.js',
                                        'styles/lovePlan/lovePlanPaySuccess.less?ver='+marketVersion
                                    ],
                                    'js/views/lovePlan/lovePlanPaySuccess.html?ver='+marketVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    });
                $urlRouterProvider.otherwise('lovePlanList');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
        })
})