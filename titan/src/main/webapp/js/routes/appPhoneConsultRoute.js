/**
 * 路由
 */
define(['appPhoneConsult'], function(app){
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
                    .state('selfCenter', {
                        url: '/selfCenter',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'selfCenterCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.selfCenterCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/selfCenterCtrl.js',
                                        'styles/phoneConsult/selfCenter.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/selfCenter.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConDoctorList', {
                        url: '/phoneConDoctorList/:searchName,:action,:titleName',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConDoctorListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConDoctorListCtrl',
                                    [
                                        'js/libs/moment.min.js',
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/phoneConDoctorListCtrl.js',
                                        'styles/commonDoctor/doctorInfo.less?ver='+phoneConsultVersion,
                                        'styles/phoneConsult/phoneConDoctorList.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConDoctorList.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConDoctorDetail', {
                        url: '/phoneConDoctorDetail/:doctorId,:choiceItem',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConDoctorDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConDoctorDetailCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/libs/moment.min.js',
                                        'js/controllers/phoneConsult/phoneConDoctorDetailCtrl.js',
                                        'js/libs/moment.min.js',
                                        'styles/commonDoctor/doctorInfo.less?ver='+phoneConsultVersion,
                                        'styles/phoneConsult/phoneConDoctorDetail.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConDoctorDetail.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConAddBaby', {
                        url: '/phoneConAddBaby/:phoneConDoctorDetail,:doctorId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConAddBabyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConAddBabyCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/phoneConAddBabyCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        'styles/phoneConsult/phoneConAddBaby.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConAddBaby.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConPaySuccess', {
                        url: '/phoneConPaySuccess/:consultPhoneRegisterId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConPaySuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConPaySuccessCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/phoneConPaySuccessCtrl.js',
                                        'styles/commonDoctor/orderInfo.less?ver='+phoneConsultVersion,
                                        'styles/commonDoctor/remindInfo.less?ver='+phoneConsultVersion,
                                        'styles/phoneConsult/phoneConPaySuccess.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConPaySuccess.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConDoctorGroup', {
                        url: '/phoneConDoctorGroup',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConDoctorGroupCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConDoctorGroupCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/phoneConDoctorGroupCtrl.js',
                                        'styles/phoneConsult/phoneConDoctorGroup.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConDoctorGroup.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('currentOrderList', {
                        url: '/currentOrderList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'currentOrderListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.currentOrderListCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/currentOrderListCtrl.js',
                                        'styles/commonDoctor/doctorInfo.less?ver='+phoneConsultVersion,
                                        'styles/commonDoctor/classifyItem.less?ver='+phoneConsultVersion,
                                        'styles/phoneConsult/currentOrderList.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/currentOrderList.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('appointOrder', {
                        url: '/appointOrder',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'appointOrderCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.appointOrderCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/appointOrderCtrl.js',
                                        'styles/commonDoctor/doctorInfo.less?ver='+phoneConsultVersion,
                                        'styles/commonDoctor/classifyItem.less?ver='+phoneConsultVersion,
                                        'styles/phoneConsult/appointOrder.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/appointOrder.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConsultOrder', {
                        url: '/phoneConsultOrder',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConsultOrderCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConsultOrderCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/phoneConsultOrderCtrl.js',
                                        'styles/commonDoctor/classifyItem.less?ver='+phoneConsultVersion,
                                        'styles/commonDoctor/doctorInfo.less?ver='+phoneConsultVersion,
                                        'styles/phoneConsult/appointOrder.less?ver='+phoneConsultVersion,
                                        'styles/phoneConsult/phoneConsultOrder.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConsultOrder.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('orderDetail', {
                        url: '/orderDetail:doctorId,:orderId,:type',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'orderDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.orderDetaillCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/orderDetailCtrl.js',
                                        'styles/commonDoctor/doctorInfo.less?ver='+phoneConsultVersion,
                                        'styles/commonDoctor/orderInfo.less?ver='+phoneConsultVersion,
                                        'styles/phoneConsult/orderDetail.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/orderDetail.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConCancelOrder', {
                        url: '/phoneConCancelOrder:consultPhone_register_service_id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConCancelOrderCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConCancelOrderCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/phoneConCancelOrderCtrl.js',
                                        'styles/phoneConsult/phoneConCancelOrder.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConCancelOrder.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConCancelSuccess', {
                        url: '/phoneConCancelSuccess/:price',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConCancelSuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConCancelSuccessCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/phoneConCancelSuccessCtrl.js',
                                        'styles/phoneConsult/phoneConCancelSuccess.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConCancelSuccess.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConEvaluate', {
                        url: '/phoneConEvaluate:consultphone_register_service_id,:doctorId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConEvaluateCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConEvaluateCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/phoneConEvaluateCtrl.js',
                                        'js/libs/mobiscroll.custom-2.5.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.5.0.min.css',
                                        'styles/commonDoctor/evaluateDoctor.less?ver='+phoneConsultVersion,
                                        'styles/phoneConsult/phoneConEvaluate.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConEvaluate.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('evaluateList', {
                        url: '/evaluateList:doctorId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'evaluateListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.evaluateListCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/evaluateListCtrl.js',
                                        'styles/phoneConsult/evaluateList.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/evaluateList.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConReconnection', {
                        url: '/phoneConReconnection/:consultPhoneServiceId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConReconnectionCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConReconnectionCtrl',
                                    [
                                        'js/libs/moment.min.js',
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/phoneConReconnectionCtrl.js',
                                        'styles/commonDoctor/doctorInfo.less?ver='+phoneConsultVersion,
                                        'styles/commonDoctor/orderInfo.less?ver='+phoneConsultVersion,
                                        'styles/phoneConsult/phoneConReconnection.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConReconnection.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('phoneConConnectCall', {
                        url: '/phoneConConnectCall/:phoneConsultaServiceId,:doctorName,:phone,:doctorId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'phoneConConnectCallCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.phoneConConnectCallCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/phoneConConnectCallCtrl.js',
                                        'styles/phoneConsult/phoneConConnectCall.less?ver='+phoneConsultVersion],
                                    'js/views/phoneConsult/phoneConConnectCall.html?ver='+phoneConsultVersion);
                            }
                        }
                    })
                    .state('myBabyMoney', {
                        url: '/myBabyMoney',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'myBabyMoneyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.myBabyMoneyCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/phoneConsult/myBabyMoneyCtrl.js',
                                        'styles/phoneConsult/myBabyMoney.less?ver'+appointVersion],
                                    'js/views/phoneConsult/myBabyMoney.html?ver='+appointVersion);
                            }
                        }
                    })




                $urlRouterProvider.otherwise('selfCenter');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
            $rootScope.memberFunction = "false";
        })
})