/**
 * 路由
 */
define(['appActivity'], function(app){
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
                    .state('picSpreadIndex', {
                        url: '/picSpreadIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'picSpreadIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.picSpreadIndexCtrl',
                                    ['js/controllers/activity/picSpread/picSpreadIndexCtrl.js?ver='+activityVersion,
                                        'styles/activity/picSpread/picSpreadIndex.less?ver='+activityVersion
                                      ],
                                    'js/views/activity/picSpread/picSpreadIndex.html?ver='+activityVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })


                $urlRouterProvider.otherwise('nutritionIndex');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;;
        })
})