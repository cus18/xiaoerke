/**
 * 路由
 */
define(['appInsurance'], function(app){
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
                   /* 防犬宝*/
                    .state('antiDogLead', {
                        url: '/antiDogLead',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'antiDogLeadCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.antiDogLeadCtrl',
                                    ['js/controllers/antiDog/antiDogLeadCtrl.js',
                                        'styles/antiDog/antiDogLead.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/antiDog/antiDogLead.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('antiDogIndex', {
                        url: '/antiDogIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'antiDogIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.antiDogIndexCtrl',
                                    ['js/controllers/antiDog/antiDogIndexCtrl.js',
                                        'styles/antiDog/antiDogIndex.less?ver='+insuranceVersion,
                                      ],
                                    'js/views/antiDog/antiDogIndex.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('antiDogUpload', {
                        url: '/antiDogUpload',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'antiDogUploadCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.antiDogUploadCtrl',
                                    ['js/controllers/antiDog/antiDogUploadCtrl.js',
                                        'styles/antiDog/antiDogUpload.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/antiDog/antiDogUpload.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('antiDogPaySuccess', {
                        url: '/antiDogPaySuccess/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'antiDogPaySuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.antiDogPaySuccessCtrl',
                                    ['js/controllers/antiDog/antiDogPaySuccessCtrl.js',
                                        'styles/antiDog/antiDogPaySuccess.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/antiDog/antiDogPaySuccess.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('antiDogOrderList', {
                        url: '/antiDogOrderList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'antiDogOrderListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.antiDogOrderListCtrl',
                                    ['js/controllers/antiDog/antiDogOrderListCtrl.js',
                                        'styles/antiDog/antiDogOrderList.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/antiDog/antiDogOrderList.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('antiDogOrderDetail', {
                        url: '/antiDogOrderDetail/:index',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'antiDogOrderDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.antiDogOrderDetailCtrl',
                                    ['js/controllers/antiDog/antiDogOrderDetailCtrl.js',
                                        'styles/antiDog/antiDogOrderDetail.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/antiDog/antiDogOrderDetail.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('antiDogAddBaby', {
                        url: '/antiDogAddBaby',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'antiDogAddBabyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.antiDogAddBabyCtrl',
                                    ['js/controllers/antiDog/antiDogAddBabyCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'styles/antiDog/antiDogAddBaby.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/antiDog/antiDogAddBaby.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('antiDogHospital', {
                        url: '/antiDogHospital',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'antiDogHospitalCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.antiDogHospitalCtrl',
                                    ['js/controllers/antiDog/antiDogHospitalCtrl.js',
                                        'styles/antiDog/antiDogHospital.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/antiDog/antiDogHospital.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('handfootmouthIndex', {
                        url: '/handfootmouthIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'handfootmouthIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.handfootmouthIndexCtrl',
                                    ['js/controllers/handfootmouth/handfootmouthIndexCtrl.js',
                                        'styles/handfootmouth/handfootmouthIndex.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/handfootmouth/handfootmouthIndex.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })



                $urlRouterProvider.otherwise('antiDogLead');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;;
        })
})