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

                //$urlRouterProvider.otherwise('patientConsultFirst');
                $urlRouterProvider.otherwise('patientConsultUmbrella');
            }])
        .run(function ($rootScope){
        })
})