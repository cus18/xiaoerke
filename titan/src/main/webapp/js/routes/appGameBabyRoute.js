/**
 * 路由
 */
define(['appBabyGame'], function(app){
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
                    /* phoneConsult 电话咨询*/
                    .state('babyGameFirst', {
                        url: '/babyGameFirst',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'babyGameFirstCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.babyGameFirstCtrl',
                                    ['js/controllers/phoneConsult/babyGameFirstCtrl.js',
                                        'styles/phoneConsult/babyGameFirst.less?ver=' + babyGameVersion],
                                    'js/views/phoneConsult/babyGameFirst.html?ver=' + babyGameVersion);
                            }
                        }
                    })
                $urlRouterProvider.otherwise('babyGameFirst');
            }])
        .run(function ($rootScope){
            $rootScope.picVer = picVersion;
        })
})