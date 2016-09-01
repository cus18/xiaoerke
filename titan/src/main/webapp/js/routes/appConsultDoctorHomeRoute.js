/**
 * 路由
 */
define(['appConsultDoctorHome'], function(app){
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
                    .state('consultDoctorHome', {
                        url: '/consultDoctorHome/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'consultDoctorHomeCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.consultDoctorHomeCtrl',
                                    ['js/controllers/consultDoctorHome/consultDoctorHomeCtrl.js',
                                        'styles/consultDoctorHome/consultDoctorHome.less?ver='+picVersion,
                                    ],
                                    'js/views/consultDoctorHome/consultDoctorHome.html?ver='+picVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('consultDoctorCommentDetails', {
                        url: '/consultDoctorCommentDetails/:id,:name,:gender',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'consultDoctorCommentDetailsCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.consultDoctorCommentDetailsCtrl',
                                    ['js/controllers/consultDoctorHome/consultDoctorCommentDetailsCtrl.js',
                                        'styles/consultDoctorHome/consultDoctorCommentDetails.less?ver='+picVersion,
                                    ],
                                    'js/views/consultDoctorHome/consultDoctorCommentDetails.html?ver='+picVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    });



                $urlRouterProvider.otherwise('consultDoctorHome');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
        })
})
