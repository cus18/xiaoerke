/**
 * 路由
 */
define(['appMarket'], function(app){
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
                /* 妈妈营养测试*/
                .state('momNutritionTest', {
                        url: '/momNutritionTest',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'momNutritionTestCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.momNutritionTestCtrl',
                                    ['js/controllers/momNutrition/momNutritionTestCtrl.js',
                                        'styles/momNutrition/momNutritionTest.less?ver='+marketVersion,
                                      ],
                                    'js/views/momNutrition/momNutritionTest.html?ver='+marketVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                .state('momNutritionResult', {
                        url: '/momNutritionResult/:result,:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'momNutritionResultCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.momNutritionResultCtrl',
                                    ['js/controllers/momNutrition/momNutritionResultCtrl.js',
                                        'styles/momNutrition/momNutritionResult.less?ver='+marketVersion,
                                    ],
                                    'js/views/momNutrition/momNutritionResult.html?ver='+marketVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
               /* 宝妈爱心接力*/
                .state('lovePlanPoster', {
                        url: '/lovePlanPoster',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'lovePlanPosterCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.lovePlanPosterCtrl',
                                    ['js/controllers/lovePlan/lovePlanPosterCtrl.js',
                                        'styles/lovePlan/lovePlanPoster.less?ver='+marketVersion,
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
                                        'styles/lovPlan/lovePlanList.less?ver='+marketVersion,
                                    ],
                                    'js/views/lovePlan/lovePlanList.html?ver='+marketVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })



                $urlRouterProvider.otherwise('momNutritionTest');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;;
        })
})