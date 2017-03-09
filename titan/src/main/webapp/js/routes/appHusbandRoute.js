/**
 * 路由
 */
define(['appHusband','swiper','appHusbandFactory'], function(app){
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
                /*首页*/
                $stateProvider
                    .state('husGuide', {
                        url: '/husGuide',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'husGuideCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.husGuideCtrl',
                                    [ 'js/controllers/appHusband/husGuideCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appHusband/husGuide.less?ver='+marketVersion],
                                    'js/views/appHusband/husGuide.html?ver='+marketVersion);
                            }
                        }
                    })
                   /* 奖品规则*/
                    .state('husTest', {
                        url: '/husTest',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'husTestCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.husTestCtrl',
                                    ['js/controllers/appHusband/husTestCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appHusband/husTest.less?ver='+marketVersion],
                                    'js/views/appHusband/husTest.html?ver='+marketVersion);
                            }
                        }
                    })
                    .state('husFollow', {
                        url: '/husFollow',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'husFollowCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.husFollowCtrl',
                                    ['js/controllers/appHusband/husFollowCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appHusband/husFollow.less?ver='+marketVersion],
                                    'js/views/appHusband/husFollow.html?ver='+marketVersion);
                            }
                        }
                    })
                    .state('husResult', {
                        url: '/husResult/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'husResultCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.husResultCtrl',
                                    ['js/controllers/appHusband/husResultCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appHusband/husResult.less?ver='+marketVersion],
                                    'js/views/appHusband/husResult.html?ver='+marketVersion);
                            }
                        }
                    })

                $urlRouterProvider.otherwise('husGuide');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
            $rootScope.memberFunction = "false";
        })
})