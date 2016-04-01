/**
 * 路由
 */
define(['appNutrition'], function(app){
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
                   /* 营养管理*/
                    .state('nutritionBabyInfo', {
                        url: '/nutritionBabyInfo',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionBabyInfoCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionBabyInfoCtrl',
                                    ['js/controllers/health/nutrition/nutritionBabyInfoCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/health/healthCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionBabyInfo.less?ver='+ntrVersion,
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css'],
                                    'js/views/health/nutrition/nutritionBabyInfo.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionReport', {
                        url: '/nutritionReport/:type',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionReportCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionReportCtrl',
                                    ['js/controllers/health/nutrition/nutritionReportCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/health/healthCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionReport.less?ver='+ntrVersion,
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css'],
                                    'js/views/health/nutrition/nutritionReport.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionPyramid', {
                        url: '/nutritionPyramid',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionPyramidCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionPyramidCtrl',
                                    ['js/controllers/health/nutrition/nutritionPyramidCtrl.js',
                                        'styles/health/nutrition/pyramidCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionPyramid.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionPyramid.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionIndex', {
                        url: '/nutritionIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionIndexCtrl',
                                    ['js/controllers/health/nutrition/nutritionIndexCtrl.js',
                                        'styles/health/healthCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionPyramid.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionIndex.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionIndex.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionFood', {
                        url: '/nutritionFood',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionFoodCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionFoodCtrl',
                                    ['js/controllers/health/nutrition/nutritionFoodCtrl.js',
                                        'styles/health/nutrition/nutritionFood.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionFood.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionAssess', {
                        url: '/nutritionAssess/:flag',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionAssessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionAssessCtrl',
                                    ['js/controllers/health/nutrition/nutritionAssessCtrl.js',
                                        'styles/health/healthCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/pyramidCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionAssess.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionAssess.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionAddAssess', {
                        url: '/nutritionAddAssess/:type,:weight',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionAddAssessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionAddAssessCtrl',
                                    ['js/controllers/health/nutrition/nutritionAddAssessCtrl.js',
                                        'styles/health/healthCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionAddAssess.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionAddAssess.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionAssessWeight', {
                        url: '/nutritionAssessWeight/:type',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionAssessWeightCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionAssessWeightCtrl',
                                    ['js/controllers/health/nutrition/nutritionAssessWeightCtrl.js',
                                        'styles/health/nutrition/nutritionAssessWeight.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionAssessWeight.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionEffect', {
                        url: '/nutritionEffect/:finish,:type,:weight',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionEffectCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionEffectCtrl',
                                    ['js/controllers/health/nutrition/nutritionEffectCtrl.js',
                                        'styles/health/healthCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionEffect.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionEffect.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionAssessResult', {
                        url: '/nutritionAssessResult',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionAssessResultCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionAssessResultCtrl',
                                    ['js/controllers/health/nutrition/nutritionAssessResultCtrl.js',
                                        'styles/health/nutrition/pyramidCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionAssessResult.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionAssessResult.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionNecessary', {
                        url: '/nutritionNecessary',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionNecessaryCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionNecessaryCtrl',
                                    ['js/controllers/health/nutrition/nutritionNecessaryCtrl.js',
                                        'styles/health/healthCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionNecessary.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionNecessary.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionNecessaryList', {
                        url: '/nutritionNecessaryList/:type',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionNecessaryListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionNecessaryListCtrl',
                                    ['js/controllers/health/nutrition/nutritionNecessaryListCtrl.js',
                                        'styles/health/healthCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionNecessaryList.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionNecessaryList.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionNecessaryDetail', {
                        url: '/nutritionNecessaryDetail/:type,:tl,:it,:jd,:jdp,:os,:osp,:tm,:tmp,:img',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionNecessaryDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionNecessaryDetailCtrl',
                                    ['js/controllers/health/nutrition/nutritionNecessaryDetailCtrl.js',
                                        'styles/health/nutrition/nutritionNecessaryDetail.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionNecessaryDetail.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionKuap', {
                        url: '/nutritionKuap',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionKuapCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionKuapCtrl',
                                    ['js/controllers/health/nutrition/nutritionKuapCtrl.js',
                                        'styles/health/nutrition/nutritionKuap.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionKuap.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionAsk', {
                        url: '/nutritionAsk',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionAskCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionAskCtrl',
                                    ['js/controllers/health/nutrition/nutritionAskCtrl.js',
                                        'styles/health/nutrition/nutritionCommon.less?ver='+ntrVersion,
                                        'styles/health/nutrition/nutritionAsk.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionAsk.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('nutritionAskSuccess', {
                        url: '/nutritionAskSuccess',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'nutritionAskSuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.nutritionAskSuccessCtrl',
                                    ['js/controllers/health/nutrition/nutritionAskSuccessCtrl.js',
                                        'styles/health/nutrition/nutritionAskSuccess.less?ver='+ntrVersion],
                                    'js/views/health/nutrition/nutritionAskSuccess.html?ver='+ntrVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })

                $urlRouterProvider.otherwise('nutritionIndex');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;;
        })
})