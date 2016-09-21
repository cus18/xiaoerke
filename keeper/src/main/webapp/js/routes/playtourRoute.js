
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
                    .state('playtourEvaluate', {
                        url: '/playtourEvaluate/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'playtourEvaluateCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.playtourEvaluate',
                                    ['js/controllers/playtour/playtourEvaluateCtrl.js',
                                     'styles/playtour/playtourEvaluate.less?ver='+wxVersion],
                                    'js/views/playtour/playtourEvaluate.html?ver='+wxVersion);
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