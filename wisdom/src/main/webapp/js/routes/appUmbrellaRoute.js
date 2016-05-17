/**
 * 路由
 */
define(['appUmbrella'], function(app){
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
                   /* 营养管理*/
                    .state('umbrellaIndex', {
                        url: '/umbrellaIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'umbrellaIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.umbrellaIndexCtrl',
                                    ['js/controllers/umbrella/umbrellaIndexCtrl.js',
                                        'styles/umbrella/umbrellaIndex.less?ver='+umbrellaVersion],
                                    'js/views/umbrella/umbrellaIndex.html?ver='+umbrellaVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })


                $urlRouterProvider.otherwise('umbrellaIndex');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;;
        })
})