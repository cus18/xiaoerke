/**
 * 路由
 */
define(['appPatientConsult'], function(app){
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
                    .state('patientConsultFirst', {
                        url: '/patientConsultFirst',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'patientConsultFirstCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'patientConsultFirstCtrl',
                                    ['js/controllers/patientConsultFirstCtrl.js',
                                        'js/libs/scrollglue.js','js/libs/moment.min.js',
                                        'js/styles/patientConsultFirst.css'],
                                    'js/views/patientConsultFirst.html?ver='+patientConsultVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('patientConsultUmbrella', {
                        url: '/patientConsultUmbrella',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'patientConsultUmbrellaCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'patientConsultUmbrellaCtrl',
                                    ['js/controllers/patientConsultUmbrellaCtrl.js',
                                        'js/libs/scrollglue.js','js/libs/moment.min.js',
                                        'js/styles/patientConsultUmbrella.css'],
                                    'js/views/patientConsultUmbrella.html?ver='+patientConsultVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('customerService', {
                        url: '/customerService',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'customerServiceCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'customerServiceCtrl',
                                    ['js/controllers/customerServiceCtrl.js',
                                        'js/libs/scrollglue.js','js/libs/moment.min.js',
                                        'js/styles/main.css','js/styles/customerService.css'],
                                    'js/views/customerService.html');
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('doctorConsultPaySuccess', {
                        url: '/doctorConsultPaySuccess',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'doctorConsultPaySuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'doctorConsultPaySuccessCtrl',
                                    ['js/controllers/doctorConsultPaySuccessCtrl.js',
                                        'js/libs/scrollglue.js','js/libs/moment.min.js',
                                        'js/styles/main.css','js/styles/doctorConsultPaySuccess.css'],
                                    'js/views/doctorConsultPaySuccess.html');
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('patientConsultWJY', {
                        url: '/patientConsultWJY/:token',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'patientConsultWJYCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'patientConsultWJYCtrl',
                                    ['js/controllers/patientConsultWJYCtrl.js',
                                        'js/libs/scrollglue.js','js/libs/moment.min.js',
                                        'js/styles/patientConsultWJY.css'],
                                    'js/views/patientConsultWJY.html?ver='+patientConsultVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    });

                //$urlRouterProvider.otherwise('patientConsultFirst');
                //$urlRouterProvider.otherwise('patientConsultUmbrella');
                //$urlRouterProvider.otherwise('doctorConsultJumpFirst');
            }])
        .run(function ($rootScope){
        })
})