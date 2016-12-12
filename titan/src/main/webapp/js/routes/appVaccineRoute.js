/**
 * 路由
 */
define(['appVaccine'], function(app){
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
                    .state('vaccineIndex', {
                        url: '/vaccineIndex/:openId,:QRCode',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'vaccineIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.vaccineIndexCtrl',
                                    ['js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'js/controllers/vaccine/vaccineIndexCtrl.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'styles/vaccine/vaccineIndex.css?ver='+picVersion
                                    ],
                                    'js/views/vaccine/vaccineIndex.html?ver='+picVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('vaccineList', {
                        url: '/vaccineList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'vaccineListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.vaccineListCtrl',
                                    ['js/controllers/vaccine/vaccineListCtrl.js',
                                        'styles/vaccine/vaccineList.css?ver='+picVersion
                                    ],
                                    'js/views/vaccine/vaccineList.html?ver='+picVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('vaccineInformation', {
                        url: '/vaccineInformation',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'vaccineInformationCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.vaccineInformationCtrl',
                                    ['js/controllers/vaccine/vaccineInformationCtrl.js',
                                        'styles/vaccine/vaccineInformation.css?ver='+picVersion
                                    ],
                                    'js/views/vaccine/vaccineInformation.html?ver='+picVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('vaccineNotify', {
                        url: '/vaccineNotify',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'vaccineInformationCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.vaccineNotifyCtrl',
                                    ['js/controllers/vaccine/vaccineNotifyCtrl.js',
                                        'styles/vaccine/vaccineNotify.css?ver='+picVersion
                                    ],
                                    'js/views/vaccine/vaccineNotify.html?ver='+picVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                $urlRouterProvider.otherwise('vaccineIndex');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
        })
})
