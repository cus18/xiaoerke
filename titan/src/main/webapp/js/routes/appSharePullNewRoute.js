/**
 * 路由
 */
define(['appSharePullNew','swiper','appSharePullNewFactory'], function(app){
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
                    .state('home', {
                        url: '/home/:sessionId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'homeCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.homeCtrl',
                                    [ 'js/controllers/appSharePullNew/homeCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appSharePullNew/home.less?ver='+marketVersion],
                                    'js/views/appSharePullNew/home.html?ver='+marketVersion);
                            }
                        }
                    })
                    .state('haveRecord', {
                        url: '/haveRecord/:sessionId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'haveRecordCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.haveRecordCtrl',
                                    [ 'js/controllers/appSharePullNew/haveRecordCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appSharePullNew/haveRecord.less?ver='+marketVersion],
                                    'js/views/appSharePullNew/haveRecord.html?ver='+marketVersion);
                            }
                        }
                    })

                $urlRouterProvider.otherwise('home');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
            $rootScope.memberFunction = "false";
        })
})