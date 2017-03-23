/**
 * 路由
 */
define(['appSign','appSignFactory'], function(app){
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
                /*首页*/
                $stateProvider
                    .state('signHome', {
                        url: '/signHome/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'signHomeCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.signHomeCtrl',
                                    [ 'js/controllers/appSign/signHomeCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appSign/signHome.less?ver='+marketVersion],
                                    'js/views/appSign/signHome.html?ver='+marketVersion);
                            }
                        }
                    })
                    .state('signRecord', {
                        url: '/signRecord/:openId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'signRecordCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.signRecordCtrl',
                                    ['js/controllers/appSign/signRecordCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appSign/signRecord.less?ver='+marketVersion],
                                    'js/views/appSign/signRecord.html?ver='+marketVersion);
                            }
                        }
                    })
                    .state('signNewUser', {
                        url: '/signNewUser/:oldOpenId,:marketer',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'signNewUserCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.signNewUserCtrl',
                                    ['js/controllers/appSign/signNewUserCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appSign/signNewUser.less?ver='+marketVersion],
                                    'js/views/appSign/signNewUser.html?ver='+marketVersion);
                            }
                        }
                    })

                $urlRouterProvider.otherwise('signHome');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
            $rootScope.memberFunction = "false";
        })
})