/**
 * ·��
 */
define(['appOlympicBaby'], function(app){
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
                    .state('olympicBabyFirst', {
                        url: '/olympicBabyFirst',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'olympicBabyFirstCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.olympicBabyFirstCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/olympicBaby/olympicBabyFirstCtrl.js?ver='+olympicBabyVersion,
                                        'styles/olympicBaby/olympicBabyFirst.less?ver='+olympicBabyVersion],
                                    'js/views/olympicBaby/olympicBabyFirst.html?ver='+olympicBabyVersion);
                            }
                        }
                    })
                    .state('olympicGameLevel1', {
                        url: '/olympicGameLevel1',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'olympicGameLevel1Ctrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.olympicGameLevel1Ctrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/olympicBaby/olympicGameLevel1Ctrl.js?ver='+olympicBabyVersion,
                                        'styles/olympicBaby/olympicGameLevel1.less?ver='+olympicBabyVersion],
                                    'js/views/olympicBaby/olympicGameLevel1.html?ver='+olympicBabyVersion);
                            }
                        }
                    })
                    .state('olympicGameLevel2', {
                        url: '/olympicGameLevel2',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'olympicGameLevel2Ctrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.olympicGameLevel2Ctrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/olympicBaby/olympicGameLevel2Ctrl.js?ver='+olympicBabyVersion,
                                        'styles/olympicBaby/olympicGameLevel2.less?ver='+olympicBabyVersion],
                                    'js/views/olympicBaby/olympicGameLevel2.html?ver='+olympicBabyVersion);
                            }
                        }
                    })
                    .state('olympicGameLevel3', {
                        url: '/olympicGameLevel3',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'olympicGameLevel3Ctrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.olympicGameLevel3Ctrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/olympicBaby/olympicGameLevel3Ctrl.js?ver='+olympicBabyVersion,
                                        'styles/olympicBaby/olympicGameLevel3.less?ver='+olympicBabyVersion],
                                    'js/views/olympicBaby/olympicGameLevel3.html?ver='+olympicBabyVersion);
                            }
                        }
                    })
                    .state('olympicBabyDrawPrize', {
                        url: '/olympicBabyDrawPrize',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'olympicBabyDrawPrizeCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.olympicBabyDrawPrizeCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/olympicBaby/olympicBabyDrawPrizeCtrl.js?ver='+olympicBabyVersion,
                                        'styles/olympicBaby/olympicBabyDrawPrize.less?ver='+olympicBabyVersion],
                                    'js/views/olympicBaby/olympicBabyDrawPrize.html?ver='+olympicBabyVersion);
                            }
                        }
                    })
                    .state('olympicBabyMyPrize', {
                        url: '/olympicBabyMyPrize',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'olympicBabyMyPrizeCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.olympicBabyMyPrizeCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/olympicBaby/olympicBabyMyPrizeCtrl.js?ver='+olympicBabyVersion,
                                        'styles/olympicBaby/olympicBabyMyPrize.less?ver='+olympicBabyVersion],
                                    'js/views/olympicBaby/olympicBabyMyPrize.html?ver='+olympicBabyVersion);
                            }
                        }
                    })
                    .state('olympicBabyInvitationCard', {
                        url: '/olympicBabyInvitationCard',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'olympicBabyInvitationCardCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.olympicBabyInvitationCardCtrl',
                                    ['js/controllers/olympicBaby/olympicBabyInvitationCardCtrl.js?ver='+olympicBabyVersion,
                                      'styles/olympicBaby/olympicBabyInvitationCard.less?ver='+olympicBabyVersion],
                                    'js/views/olympicBaby/olympicBabyInvitationCard.html?ver='+olympicBabyVersion);
                            }
                        }
                    })







                $urlRouterProvider.otherwise('olympicBabyFirst');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
            $rootScope.memberFunction = "false";
        })
})