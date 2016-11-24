
/**
 * 路由
 */
define(['appPlayTour'], function(app){
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
                    /* 咨询打赏*/
                    .state('playtourShare', {
                        url: '/playtourShare/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'playtourShareCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.playtourShare',
                                    ['js/controllers/playtour/playtourShareCtrl.js',
                                     'styles/playtour/playtourShare.less?ver='+wxVersion],
                                    'js/views/playtour/playtourShare.html?ver='+wxVersion);
                            }
                        }
                    })
                    .state('evaluateUnSatisfy', {
                        url: '/evaluateUnSatisfy/:customerId/:sessionId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'evaluateUnSatisfyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.evaluateUnSatisfy',
                                    ['js/controllers/playtour/evaluateUnSatisfyCtrl.js',
                                     'styles/playtour/evaluateCommon.less?ver='+wxVersion,
                                     'styles/playtour/evaluateUnSatisfy.less?ver='+wxVersion],
                                    'js/views/playtour/evaluateUnSatisfy.html?ver='+wxVersion);
                            }
                        }
                    })
                    .state('evaluateSuccess', {
                        url: '/evaluateSuccess',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'evaluateSuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.evaluateSuccess',
                                    ['js/controllers/playtour/evaluateSuccessCtrl.js',
                                        'styles/playtour/evaluateCommon.less?ver='+wxVersion,
                                        'styles/playtour/evaluateSuccess.less?ver='+wxVersion],
                                    'js/views/playtour/evaluateSuccess.html?ver='+wxVersion);
                            }
                        }
                    })
                    .state('patientConsultNoFee', {
                        url: '/patientConsultNoFee',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'patientConsultNoFeeCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.patientConsultNoFee',
                                    ['js/controllers/playtour/patientConsultNoFeeCtrl.js?ver='+wxVersion,
                                        'styles/playtour/patientConsultNoFee.css?ver='+wxVersion],
                                    'js/views/playtour/patientConsultNoFee.html?ver='+wxVersion);
                            }
                        }
                    })


                $urlRouterProvider.otherwise('playtourShare');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = '1.0.1';
        })
})