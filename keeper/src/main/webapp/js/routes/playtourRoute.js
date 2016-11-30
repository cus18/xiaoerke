
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
                        url: '/evaluateUnSatisfy/:customerId/:sessionId/:consultStatus',
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
                /*宝宝币*/
                    .state('babyCoinTicketList', {
                        url: '/babyCoinTicketList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'babyCoinTicketListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.babyCoinTicketList',
                                    ['js/controllers/babyCoin/babyCoinTicketListCtrl.js?ver='+wxVersion,
                                        'styles/babyCoin/babyCoinTicketList.less?ver='+wxVersion],
                                    'js/views/babyCoin/babyCoinTicketList.html?ver='+wxVersion);
                            }
                        }
                    })
                    .state('babyCoinTicketPay', {
                        url: '/babyCoinTicketPay/:id,:name',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'babyCoinTicketPayCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.babyCoinTicketPay',
                                    ['js/controllers/babyCoin/babyCoinTicketPayCtrl.js?ver='+wxVersion,
                                        'styles/babyCoin/babyCoinTicketPay.less?ver='+wxVersion],
                                    'js/views/babyCoin/babyCoinTicketPay.html?ver='+wxVersion);
                            }
                        }
                    })
                    .state('babyCoinInvitePage', {
                        url: '/babyCoinInvitePage',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'babyCoinInvitePageCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.babyCoinInvitePageCtrl',
                                    ['js/controllers/babyCoin/babyCoinInvitePageCtrl.js?ver='+wxVersion,
                                        'styles/babyCoin/babyCoinInvitePage.css?ver='+wxVersion],
                                    'js/views/babyCoin/babyCoinInvitePage.html?ver='+wxVersion);
                            }
                        }
                    })
                    .state('babyCoinInviteOld', {
                        url: '/babyCoinInviteOld/:oldOpenId,:marketer',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'babyCoinInviteOldCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.babyCoinInviteOldCtrl',
                                    ['js/controllers/babyCoin/babyCoinInviteOldCtrl.js?ver='+wxVersion,
                                        'styles/babyCoin/babyCoinInviteOld.css?ver='+wxVersion],
                                    'js/views/babyCoin/babyCoinInviteOld.html?ver='+wxVersion);
                            }
                        }
                    })
                    .state('babyCoinInviteNew', {
                        url: '/babyCoinInviteNew/:oldOpenId,:marketer',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'babyCoinInviteNewCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.babyCoinInviteNewCtrl',
                                    ['js/controllers/babyCoin/babyCoinInviteNewCtrl.js?ver='+wxVersion,
                                        'styles/babyCoin/babyCoinInviteNew.css?ver='+wxVersion],
                                    'js/views/babyCoin/babyCoinInviteNew.html?ver='+wxVersion);
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