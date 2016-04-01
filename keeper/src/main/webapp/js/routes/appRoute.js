/**
 * 路由
 */
define(['app'], function(app){
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
                    /* 咨询申请*/
                    .state('consultFillInfo', {
                        url: '/consultFillInfo/:index,:conid',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'consultFillInfoCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.consultFillInfo',
                                    ['js/controllers/consultApply/consultFillInfoCtrl.js','styles/consultApply/consultFillInfo.less?ver='+wxVersion],
                                    'js/views/consultApply/consultFillInfo.html?ver='+wxVersion);
                            }
                        }
                    })
                    .state('consultHistory', {
                        url: '/consultHistory',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'consultHistoryCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.consultHistory',
                                    ['js/controllers/consultApply/consultHistoryCtrl.js','styles/consultApply/consultHistory.less?ver='+wxVersion],
                                    'js/views/consultApply/consultHistory.html?ver='+wxVersion);
                            }
                        }
                    })
                    .state('consultBabyList', {
                        url: '/consultBabyList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'consultBabyListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.consultBabyList',
                                    ['js/controllers/consultApply/consultBabyListCtrl.js','styles/consultApply/consultBabyList.less?ver='+wxVersion],
                                    'js/views/consultApply/consultBabyList.html?ver='+wxVersion);
                            }
                        }
                    })
                    .state('consultAddBaby', {
                        url: '/consultAddBaby',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'consultAddBabyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.consultAddBaby',
                                    ['js/controllers/consultApply/consultAddBabyCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/consultApply/consultAddBaby.less?ver='+wxVersion,
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css'],
                                    'js/views/consultApply/consultAddBaby.html?ver='+wxVersion);
                            }
                        }
                    })

                $urlRouterProvider.otherwise('consultBabyList');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = '1.0.1';
        })
})