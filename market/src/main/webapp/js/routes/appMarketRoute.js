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


                $urlRouterProvider.otherwise('momNutritionTest');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;;
        })
})