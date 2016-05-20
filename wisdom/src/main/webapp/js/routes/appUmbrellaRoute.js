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
                        url: '/umbrellaJoin',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaJoinCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaJoinCtrl',
                                    ['js/controllers/umbrella/umbrellaJoinCtrl.js',
                                        'styles/umbrella/umbrellaJoin.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/umbrellaJoin.html?ver='+umbrellaVersion);
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
                                    ['js/controllers/umbrella/umbrellaFillInfoCtrl.js',
                                        'styles/umbrella/umbrellaFillInfo.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/umbrellaFillInfo.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('myGuarantee', {
                        url: '/myGuarantee',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'myGuaranteeCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.myGuaranteeCtrl',
                                    ['js/controllers/umbrella/myGuaranteeCtrl.js',
                                        'styles/umbrella/myGuarantee.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/myGuarantee.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })


                $urlRouterProvider.otherwise('myGuarantee');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;;
        })
})