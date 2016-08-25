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
                    /*手足口*/
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
                    .state('handfootmouthPaySuccess', {
                        url: '/handfootmouthPaySuccess/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'handfootmouthPaySuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.handfootmouthPaySuccessCtrl',
                                    ['js/controllers/handfootmouth/handfootmouthPaySuccessCtrl.js',
                                        'styles/handfootmouth/handfootmouthPaySuccess.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/handfootmouth/handfootmouthPaySuccess.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('handfootmouthAddBaby', {
                        url: '/handfootmouthAddBaby',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'handfootmouthAddBabyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.handfootmouthAddBabyCtrl',
                                    ['js/controllers/handfootmouth/handfootmouthAddBabyCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'js/libs/moment.min.js',
                                        'styles/handfootmouth/handfootmouthAddBaby.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/handfootmouth/handfootmouthAddBaby.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })


                    /*肺炎宝*/
                    .state('pneumoniaIndex', {
                        url: '/pneumoniaIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'pneumoniaIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.pneumoniaIndexCtrl',
                                    ['js/controllers/insurance/pneumonia/pneumoniaIndexCtrl.js?ver='+insuranceVersion,
                                        'styles/insurance/insuranceCommon.less?ver='+insuranceVersion,
                                        'styles/insurance/pneumonia/pneumoniaIndex.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/insurance/pneumonia/pneumoniaIndex.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })

                    .state('insuranceAntiDog', {
                        url: '/insuranceAntiDog',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'insuranceAntiDogCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.insuranceAntiDogCtrl',
                                    ['js/controllers/insurance/insuranceAntiDogCtrl.js?ver='+insuranceVersion,
                                        'styles/insurance/insuranceAntiDog.less?ver='+insuranceVersion,
                                        'styles/insurance/insuranceFirstCommon.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/insurance/insuranceAntiDog.html?ver='+insuranceVersion);
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
                                    ['js/controllers/insurance/antiDogHospitalCtrl.js',
                                        'styles/insurance/antiDogHospital.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/insurance/antiDogHospital.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('insuranceHandFootMouth', {
                        url: '/insuranceHandFootMouth',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'insuranceHandFootMouthCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.insuranceHandFootMouthCtrl',
                                    ['js/controllers/insurance/insuranceHandFootMouthCtrl.js?ver='+insuranceVersion,
                                        'styles/insurance/insuranceFirstCommon.less?ver='+insuranceVersion,
                                        'styles/insurance/insuranceHandFootMouth.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/insurance/insuranceHandFootMouth.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('insurancePneumonia', {
                        url: '/insurancePneumonia',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'insurancePneumoniaCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.insurancePneumoniaCtrl',
                                    ['js/controllers/insurance/insurancePneumoniaCtrl.js?ver='+insuranceVersion,
                                        'styles/insurance/insuranceFirstCommon.less?ver='+insuranceVersion,
                                        'styles/insurance/insurancePneumonia.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/insurance/insurancePneumonia.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('insurancePaySuccess', {
                        url: '/insurancePaySuccess/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'insurancePaySuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.insurancePaySuccessCtrl',
                                    ['js/controllers/insurance/insurancePaySuccessCtrl.js?ver='+insuranceVersion,
                                        'styles/insurance/insurancePaySuccess.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/insurance/insurancePaySuccess.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('insuranceAddBaby', {
                        url: '/insuranceAddBaby/:type',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'insuranceAddBabyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.insuranceAddBabyCtrl',
                                    ['js/controllers/insurance/insuranceAddBabyCtrl.js?ver='+insuranceVersion,
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'js/libs/moment.min.js',
                                        'styles/insurance/insuranceAddBaby.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/insurance/insuranceAddBaby.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('insuranceOrderList', {
                        url: '/insuranceOrderList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'insuranceOrderListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.insuranceOrderListCtrl',
                                    ['js/controllers/insurance/insuranceOrderListCtrl.js',
                                        'styles/insurance/insuranceOrderList.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/insurance/insuranceOrderList.html?ver='+insuranceVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('insuranceOrderDetail', {
                        url: '/insuranceOrderDetail/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'insuranceOrderDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.insuranceOrderDetailCtrl',
                                    ['js/controllers/insurance/insuranceOrderDetailCtrl.js',
                                        'styles/insurance/insuranceOrderDetail.less?ver='+insuranceVersion,
                                    ],
                                    'js/views/insurance/insuranceOrderDetail.html?ver='+insuranceVersion);
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