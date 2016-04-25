/**
 * 路由
 */
define(['app'], function(app){
   return app
       .config(['$stateProvider','$urlRouterProvider',
           function($stateProvider,$urlRouterProvider) {
               var version = appointVersion;
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
                   .state('doctorIdentify', {
                       url: '/doctorIdentify',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'doctorIdentifyCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.doctorIdentifyCtrl',
                                   ['js/controllers/account/doctorIdentifyCtrl.js','styles/lib/common.css',
                                       'styles/account/doctorIdentify.less?ver='+version],
                                   'js/views/account/doctorIdentify.html?ver='+version);
                           }
                       }
                   })
                   .state('waitIdentify', {
                       url: '/waitIdentify',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'waitIdentifyCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.waitIdentifyCtrl','styles/lib/common.css',
                                   ['js/controllers/account/waitIdentifyCtrl.js','styles/account/waitIdentify.less?ver='+version],
                                   'js/views/account/waitIdentify.html?ver='+version);
                           }
                       }
                   })
                   .state('doctorFirst', {
                       url: '/doctorFirst/:addDate,:fixDate',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'doctorFirstCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.doctorFirstCtrl',
                                   ['js/controllers/ap/doctorFirstCtrl.js','js/libs/moment.min.js',
                                       'styles/ap/doctorFirst.less?ver='+version,'styles/lib/common.css','styles/showBo.css'],
                                   'js/views/ap/doctorFirst.html?ver='+version);
                           }
                       }
                   })
                   .state('addTime', {
                       url: '/addTime/:doctorId,:date,:fixDate',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'addTimeCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.addTimeCtrl',
                                   ['js/controllers/ap/addTimeCtrl.js','js/libs/mobiscroll.custom-2.5.0.min.js','js/libs/lodash.min.js',
                                       'styles/ap/addTime.less?ver='+version,'styles/lib/mobiscroll.custom-2.5.0.min.css',
                                       'styles/fonts/ionicons.ttf','styles/fonts/ionicons.woff'],
                                   'js/views/ap/addTime.html?ver='+version);
                           }
                       }
                   })
                   .state('acceptRemind', {
                       url: '/acceptRemind',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'acceptRemindCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.acceptRemindCtrl',
                                   ['js/controllers/ap/acceptRemindCtrl.js',
                                       'styles/ap/acceptRemind.less?ver='+version,'styles/lib/common.css'],
                                   'js/views/ap/acceptRemind.html?ver='+version);
                           }
                       }
                   })
                   .state('settlement', {
                       url: '/settlement/:date',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'settlementCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.settlementCtrl',
                                   ['js/controllers/ap/settlementCtrl.js', 'js/libs/moment.min.js',
                                       'styles/ap/settlement.less?ver='+version,'styles/lib/common.css'],
                                   'js/views/ap/settlement.html?ver='+version);
                           }
                       }
                   })
                   .state('myselfFirst', {
                       url: '/myselfFirst',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'myselfFirstCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.myselfFirstCtrl',
                                   ['js/controllers/account/myselfFirstCtrl.js',
                                       'styles/account/myselfFirst.less?ver='+version],
                                   'js/views/account/myselfFirst.html?ver='+version);
                           }
                       }
                   })
                   .state('myAccount', {
                       url: '/myAccount',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'myAccountCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.myselfFirstCtrl',
                                   ['js/controllers/account/myAccountCtrl.js','js/libs/mobiscroll.custom-2.17.0.min.js',
                                       'js/libs/lodash.min.js', 'js/libs/moment.min.js',
                                       'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                       'styles/account/myAccount.less?ver='+version],
                                   'js/views/account/myAccount.html?ver='+version);
                           }
                       }
                   })
                   .state('myCard', {
                       url: '/myCard',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'myCardCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.myCardCtrl',
                                   ['js/controllers/account/myCardCtrl.js','js/libs/lodash.min.js','js/libs/mobiscroll.custom-2.5.0.min.js',
                                       'styles/lib/mobiscroll.custom-2.5.0.min.css',
                                       'js/libs/moment.min.js', 'styles/account/myCard.less?ver='+version],
                                   'js/views/account/myCard.html?ver='+version);
                           }
                       }
                   })
                   .state('withDrawls', {
                       url: '/withDrawls',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'withDrawlsCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.withDrawlsCtrl',
                                   ['js/controllers/account/withDrawlsCtrl.js',
                                       'styles/account/withDrawls.less?ver='+version],
                                   'js/views/account/withDrawls.html?ver='+version);
                           }
                       }
                   })
                   .state('withDrawlsConfirm', {
                       url: '/withDrawlsConfirm/:arriveDate,:money',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'withDrawlsConfirmCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.withDrawlsConfirmCtrl',
                                   ['js/controllers/account/withDrawlsConfirmCtrl.js','js/libs/moment.min.js',
                                       'styles/account/withDrawlsConfirm.less?ver='+version],
                                   'js/views/account/withDrawlsConfirm.html?ver='+version);
                           }
                       }
                   })
                   .state('setting', {
                       url: '/setting',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'settingCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.settingCtrl',
                                   ['js/controllers/account/settingCtrl.js',
                                       'styles/account/setting.less?ver='+version],
                                   'js/views/account/setting.html?ver='+version);
                           }
                       }
                   })
                   .state('aboutUs', {
                       url: '/aboutUs',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'aboutUsCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.aboutUsCtrl',
                                   ['js/controllers/other/aboutUsCtrl.js',
                                       'styles/other/aboutUs.less?ver='+version],
                                   'js/views/other/aboutUs.html?ver='+version);
                           }
                       }
                   })
                   .state('userDeal', {
                       url: '/userDeal',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'userDealCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.userDealCtrl',
                                   ['js/controllers/other/userDealCtrl.js',
                                       'styles/other/userDeal.less?ver='+version],
                                   'js/views/other/userDeal.html?ver='+version);
                           }
                       }
                   })
                   .state('doctorFans', {
                       url: '/doctorFans/:id',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'doctorFansCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.doctorFansCtrl',
                                   ['js/controllers/account/doctorFansCtrl.js',
                                       'styles/account/doctorFans.less?ver='+version],
                                   'js/views/account/doctorFans.html?ver='+version);
                           }
                       }
                   })
                   .state('doctorHome', {
                       url: '/doctorHome/:id',
                       templateProvider: function() { return lazyDeferred.promise; },
                       controller: 'doctorHomeCtrl',
                       resolve: {
                           load: function($templateCache, $ocLazyLoad, $q, $http) {
                               loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.doctorHomeCtrl',
                                   ['js/controllers/account/doctorHomeCtrl.js',
                                       'styles/account/doctorHome.less?ver='+version],
                                   'js/views/account/doctorHome.html?ver='+version);
                           }
                       }
                   });


               $urlRouterProvider.otherwise('myselfFirst');
   }])
       .run(function (){
       })
})