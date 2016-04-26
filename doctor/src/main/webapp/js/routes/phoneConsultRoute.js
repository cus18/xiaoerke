/**
 * 路由
 */
define(['phoneConsultApp'], function(app){
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
                    .state('phoneConsultFirst', {
                        url: '/phoneConsultFirst/:date',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConsultFirstCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConsultFirstCtrl',
                                    ['js/controllers/phoneConsult/phoneConsultFirstCtrl.js',
                                        'js/libs/moment.min.js',
                                        'styles/phoneConsult/phoneConsultFirst.less?ver='+appointVersion],
                                    'js/views/phoneConsult/phoneConsultFirst.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('phoneConsultDetails', {
                        url: '/phoneConsultDetails/:id,:doctorId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConsultDetailsCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConsultDetailsCtrl',
                                    ['js/controllers/phoneConsult/phoneConsultDetailsCtrl.js',
                                        'styles/phoneConsult/phoneConsultDetails.less?ver='+appointVersion],
                                    'js/views/phoneConsult/phoneConsultDetails.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('phoneConsultFans', {
                        url: '/phoneConsultFans',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConsultFansCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConsultFansCtrl',
                                    ['js/controllers/phoneConsult/phoneConsultFansCtrl.js',
                                        'styles/phoneConsult/phoneConsultFans.less?ver='+appointVersion],
                                    'js/views/phoneConsult/phoneConsultFans.html?ver='+appointVersion);
                            }
                        }
                    })

                $urlRouterProvider.otherwise('phoneConsultFirst');
            }])
        .run(function (){
        });
});