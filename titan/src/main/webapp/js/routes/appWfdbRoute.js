/**
 * 璺敱
 */
define(['appWfdb','swiper','appWfdbFactory'], function(app){
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
                /*棣栭〉*/
                $stateProvider
                    .state('home', {
                        url: '/home',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'homeCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.homeCtrl',
                                    [ 'js/controllers/appWfdb/homeCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appWfdb/home.less?ver='+marketVersion],
                                    'js/views/appWfdb/home.html?ver='+marketVersion);
                            }
                        }
                    })
                   /* 濂栧搧瑙勫垯*/
                    .state('myCard', {
                        url: '/myCard',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'myCardCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.myCardCtrl',
                                    ['js/controllers/appWfdb/myCardCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appWfdb/myCard.less?ver='+marketVersion],
                                    'js/views/appWfdb/myCard.html?ver='+marketVersion);
                            }
                        }
                    })
                    .state('prizeList', {
                        url: '/prizeList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'prizeListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.prizeListCtrl',
                                    ['js/controllers/appWfdb/prizeListCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appWfdb/prizeList.less?ver='+marketVersion],
                                    'js/views/appWfdb/prizeList.html?ver='+marketVersion);
                            }
                        }
                    })
                    //老用户邀请好友页面
                    .state('oldUser', {
                        url: '/oldUser',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'oldUserCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.oldUserCtrl',
                                    ['js/controllers/appWfdb/oldUserCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appWfdb/oldUser.less?ver='+marketVersion],
                                    'js/views/appWfdb/oldUser.html?ver='+marketVersion);
                            }
                        }
                    })
                    //新用户邀请好友页面
                    .state('newUser', {
                        url: '/newUser',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'newUserCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.newUserCtrl',
                                    ['js/controllers/appWfdb/newUserCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appWfdb/newUser.less?ver='+marketVersion],
                                    'js/views/appWfdb/newUser.html?ver='+marketVersion);
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