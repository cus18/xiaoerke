/**
 * 路由
 */
define(['appHeightForecast'], function(app){
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
               /* 出生身高测评*/
                .state('heightForecastBirth', {
                        url: '/heightForecastBirth',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'heightForecastBirthCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.heightForecastBirthCtrl',
                                    ['js/controllers/heightForecast/heightForecastBirthCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'styles/heightForecast/heightForecastBirthTest.less?ver='+marketVersion
                                    ],
                                    'js/views/heightForecast/heightForecastBirthTest.html?ver='+marketVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
               /* 未出生身高测评*/
                .state('heightForecastNoBirth', {
                        url: '/heightForecastNoBirth',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'heightForecastNoBirthCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.heightForecastNoBirthCtrl',
                                    ['js/controllers/heightForecast/heightForecastNoBirthCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'styles/heightForecast/heightForecastNoBirthTest.less?ver='+marketVersion
                                    ],
                                    'js/views/heightForecast/heightForecastNoBirthTest.html?ver='+marketVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
               /* 身高测评结论*/
                .state('heightForecastResult', {
                        url: '/heightForecastResult/:resultBoy,/:resultGirl',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'heightForecastResultCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.heightForecastResultCtrl',
                                    ['js/controllers/heightForecast/heightForecastResultCtrl.js',
                                        'styles/heightForecast/heightForecastResult.less?ver='+marketVersion
                                    ],
                                    'js/views/heightForecast/heightForecastResult.html?ver='+marketVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    });
                //$urlRouterProvider.otherwise('heightForecastBirth');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
        })
})