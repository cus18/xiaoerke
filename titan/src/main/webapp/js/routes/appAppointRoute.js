/**
 * 路由
 */
define(['appAppoint'], function(app){
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
                    /*ap模块*/
                    .state('cooperationHospital', {
                        url: '/cooperationHospital/:hospitalId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'CooperationHospitalCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.CooperationHospitalCtrl',
                                    ['js/controllers/appoint/ap/cooperationHospitalCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appoint/ap/cooperationHospital.less?ver='+appointVersion],
                                    'js/views/appoint/ap/cooperationHospital.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('cooperationHospitalPrice', {
                        url: '/cooperationHospitalPrice',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'cooperationHospitalPriceCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.cooperationHospitalPriceCtrl',
                                    ['js/controllers/appoint/ap/cooperationHospitalPriceCtrl.js',
                                        'js/libs/lodash.min.js',
                                        'styles/appoint/ap/cooperationHospitalPrice.less?ver='+appointVersion],
                                    'js/views/appoint/ap/cooperationHospitalPrice.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('searchList', {
                        url: '/searchList/:searchName,:action,:titleName',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'SearchListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.SearchListCtrl',
                                    ['js/controllers/appoint/ap/searchListCtrl.js',
                                        'js/libs/moment.min.js','js/libs/jquery.textSearch-1.0.js',
                                        'styles/appoint/ap/searchList.less?ver='+appointVersion],
                                    'js/views/appoint/ap/searchList.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('doctorAppointment', {
                        url: '/doctorAppointment/:doctorId,:available_time,:hospitalName,:location,:position,' +
                        ':mark,:location_id,:attentionDoctorId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'DoctorAppointmentCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.DoctorAppointmentCtrl',
                                    ['js/controllers/appoint/ap/doctorAppointmentCtrl.js',
                                        'js/libs/moment.min.js','js/libs/lodash.min.js',
                                        'styles/appoint/ap/doctorAppointment.less?ver'+appointVersion],
                                    'js/views/appoint/ap/doctorAppointment.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('doctorGroupList', {
                        url: '/doctorGroupList/:groupId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'DoctorGroupListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.DoctorGroupListCtrl',
                                    ['js/controllers/appoint/ap/doctorGroupListCtrl.js',
                                        'js/libs/moment.min.js','js/libs/lodash.min.js',
                                        'styles/appoint/ap/doctorGroupList.less?ver'+appointVersion],
                                    'js/views/appoint/ap/doctorGroupList.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('appointmentConfirm', {
                        url: '/appointmentConfirm/:register_service_id,:doctorId,:date,' +
                        ':begin_time,:end_time,:needPay,:chargePrice',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'AppointmentConfirmCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.AppointmentConfirmCtrl',
                                    ['js/controllers/appoint/ap/appointmentConfirmCtrl.js',
                                        'js/libs/moment.min.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'js/libs/showBo.js',
                                        'styles/lib/showBo.css','styles/appoint/ap/appointmentConfirm.less?ver'+appointVersion,
                                        'styles/lib/common.css','styles/lib/mobiscroll.custom-2.17.0.min.css'],
                                    'js/views/appoint/ap/appointmentConfirm.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('payProblem', {
                        url: '/payProblem/:patient_register_service_id,:inviteCodeNum',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'payProblemCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.payProblemCtrl',
                                    ['js/controllers/appoint/ap/payProblemCtrl.js',
                                        'styles/appoint/ap/payProblem.less?ver'+appointVersion],
                                    'js/views/appoint/ap/payProblem.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('appointmentSuccess', {
                        url: '/appointmentSuccess/:patient_register_service_id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'AppointmentSuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.AppointmentSuccessCtrl',
                                    ['js/controllers/appoint/appointmentSuccessCtrl.js',
                                        'js/libs/moment.min.js',
                                        'styles/appoint/appointmentSuccess.less?ver'+appointVersion],
                                    'js/views/appoint/appointmentSuccess.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('route', {
                        url: '/route/:sys_regist_id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'RouteCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.RouteCtrl',
                                    ['js/controllers/appoint/ap/routeCtrl.js',
                                        'styles/appoint/ap/route.less?ver'+appointVersion],
                                    'js/views/appoint/ap/route.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('memberService', {
                        url: '/memberService/:memberType,:action,:register_service_id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'memberServiceCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.memberServiceCtrl',
                                    ['js/controllers/appoint/ap/memberServiceCtrl.js',
                                        'js/libs/showBo.js','js/libs/moment.min.js','styles/lib/showBo.css',
                                        'styles/appoint/ap/memberService.less?ver'+appointVersion],
                                    'js/views/appoint/ap/memberService.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('memberServiceDetail', {
                        url: '/memberServiceDetail/:memberServiceId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'memberServiceDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.memberServiceDetailCtrl',
                                    ['js/controllers/appoint/ap/memberServiceDetailCtrl.js',
                                        'js/libs/showBo.js','js/libs/moment.min.js',
                                        'styles/lib/showBo.css',
                                        'styles/appoint/ap/memberServiceDetail.less?ver'+appointVersion],
                                    'js/views/appoint/ap/memberServiceDetail.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('memberServiceSuccess', {
                        url: '/memberServiceSuccess/:patient_register_service_id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'memberServiceSuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.memberServiceSuccessCtrl',
                                    ['js/controllers/appoint/ap/memberServiceSuccessCtrl.js',
                                        'styles/appoint/ap/memberServiceSuccess.less?ver'+appointVersion],
                                    'js/views/appoint/ap/memberServiceSuccess.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('appointmentUseMember', {
                        url: '/appointmentUseMember/:patient_register_service_id,:date,:begin_time,:end_time',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'appointmentUseMemberCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.appointmentUseMemberCtrl',
                                    ['js/controllers/appoint/appointmentUseMemberCtrl.js',
                                        'styles/appoint/appointmentUseMember.less?ver'+appointVersion],
                                    'js/views/appoint/appointmentUseMember.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('seeDoctorProcess', {
                        url: '/seeDoctorProcess/:hospitalId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'seeDoctorProcessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.seeDoctorProcessCtrl',
                                    ['js/controllers/appoint/ap/seeDoctorProcessCtrl.js',
                                        'styles/appoint/ap/seeDoctorProcess.less?ver'+appointVersion],
                                    'js/views/appoint/ap/seeDoctorProcess.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('memberDeal', {
                        url: '/memberDeal',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'memberDealCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.memberDealCtrl',
                                    ['js/controllers/appoint/ap/memberDealCtrl.js',
                                        'styles/appoint/ap/memberDeal.less?ver'+appointVersion],
                                    'js/views/appoint/ap/memberDeal.html?ver='+appointVersion);
                            }
                        }
                    })

                    
                    /* my模块*/
                    .state('accountBalance', {
                        url: '/accountBalance',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'accountBalanceCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.accountBalanceCtrl',
                                    ['js/controllers/appoint/my/accountBalanceCtrl.js',
                                        'styles/appoint/my/accountBalance.less?ver'+appointVersion],
                                    'js/views/appoint/my/accountBalance.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('appointmentCancel', {
                        url: '/appointmentCancel/:patient_register_service_id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'AppointmentCancelCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.appointmentCancelCtrl',
                                    ['js/controllers/appoint/my/appointmentCancelCtrl.js',
                                        'styles/appoint/my/appointmentCancel.less?ver'+appointVersion],
                                    'js/views/appoint/my/appointmentCancel.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('appointmentCancelSuccess', {
                        url: '/appointmentCancelSuccess/:amount,:payType,:relationType',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'AppointmentCancelSuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.AppointmentCancelSuccessCtrl',
                                    ['js/controllers/appoint/my/appointmentCancelSuccessCtrl.js',
                                        'styles/appoint/my/appointmentCancelSuccess.less?ver'+appointVersion],
                                    'js/views/appoint/my/appointmentCancelSuccess.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('evaluateDoctor', {
                        url: '/evaluateDoctor/:patient_register_service_id,:date,:beginTime,:endTime',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'EvaluateDoctorCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.EvaluateDoctorCtrl',
                                    ['js/controllers/appoint/my/evaluateDoctorCtrl.js',
                                        'js/libs/mobiscroll.custom-2.5.0.min.js',
                                        'styles/appoint/my/evaluateDoctor.less?ver'+appointVersion,
                                        'styles/lib/mobiscroll.custom-2.5.0.min.css'],
                                    'js/views/appoint/my/evaluateDoctor.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('evaluateDoctorSuccess', {
                        url: '/evaluateDoctorSuccess/:InviteCode,:patientId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'EvaluateDoctorSuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.EvaluateDoctorSuccessCtrl',
                                    ['js/controllers/appoint/my/evaluateDoctorSuccessCtrl.js',
                                        'styles/appoint/my/evaluateDoctorSuccess.less?ver'+appointVersion],
                                    'js/views/appoint/my/evaluateDoctorSuccess.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('evaluateList', {
                        url: '/evaluateList/:patient_register_service_id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'EvaluateListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.EvaluateListCtrl',
                                    ['js/controllers/appoint/my/evaluateListCtrl.js',
                                        'js/libs/moment.min.js',
                                        'styles/appoint/my/evaluateList.less?ver'+appointVersion],
                                    'js/views/appoint/my/evaluateList.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('myAppointment', {
                        url: '/myAppointment',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'MyAppointmentCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.MyAppointmentCtrl',
                                    ['js/controllers/appoint/my/myAppointmentCtrl.js',
                                        'styles/appoint/my/myAppointment.less?ver'+appointVersion],
                                    'js/views/appoint/my/myAppointment.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('myAttention', {
                        url: '/myAttention',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'MyAttentionCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.MyAttentionCtrl',
                                    ['js/controllers/appoint/my/myAttentionCtrl.js',
                                        'styles/appoint/my/myAttention.less?ver'+appointVersion],
                                    'js/views/appoint/my/myAttention.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('myInfo', {
                        url: '/myInfo/:userPhoneNum',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'MyInfoCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.MyInfoCtrl',
                                    ['js/controllers/appoint/my/myInfoCtrl.js',
                                        'styles/appoint/my/myInfo.less?ver'+appointVersion],
                                    'js/views/appoint/my/myInfo.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('myService', {
                        url: '/myService',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'myServiceCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.myServiceCtrl',
                                    ['js/controllers/appoint/my/myServiceCtrl.js',
                                        'styles/appoint/my/myService.less?ver'+appointVersion],
                                    'js/views/appoint/my/myService.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('myselfFirst', {
                        url: '/myselfFirst/:userPhoneNum',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'MyselfFirstCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.MyselfFirstCtrl',
                                    ['js/controllers/appoint/my/myselfFirstCtrl.js',
                                        'styles/appoint/my/myselfFirst.less?ver'+appointVersion],
                                    'js/views/appoint/my/myselfFirst.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('pendPayment', {
                        url: '/pendPayment',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'PendPaymentCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.PendPaymentCtrl',
                                    ['js/controllers/appoint/my/pendPaymentCtrl.js'],
                                    'js/views/appoint/my/pendPayment.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('pendPaymentDetail', {
                        url: '/pendPaymentDetail/:patient_register_service_id,:time',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'PendPaymentDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.PendPaymentDetailCtrl',
                                    ['js/controllers/appoint/my/pendPaymentDetailCtrl.js',
                                        'styles/appoint/my/pendPaymentDetail.less?ver'+appointVersion],
                                    'js/views/appoint/my/pendPaymentDetail.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('sharedDetail', {
                        url: '/sharedDetail/:patient_register_service_id,:status,:returnpay',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'SharedDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.SharedDetailCtrl',
                                    ['js/controllers/appoint/my/sharedDetailCtrl.js',
                                        'styles/appoint/my/sharedDetail.less?ver'+appointVersion],
                                    'js/views/appoint/my/sharedDetail.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('shareSuccess', {
                        url: '/shareSuccess/:patient_register_service_id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'shareSuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.shareSuccessCtrl',
                                    ['js/controllers/appoint/my/shareSuccessCtrl.js',
                                        'styles/appoint/my/shareSuccess.less?ver'+appointVersion],
                                    'js/views/appoint/my/shareSuccess.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('toBeEvaluated', {
                        url: '/toBeEvaluated',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'ToBeEvaluatedCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.ToBeEvaluatedCtrl',
                                    ['js/controllers/appoint/my/toBeEvaluatedCtrl.js',
                                        'styles/appoint/my/toBeEvaluated.less?ver'+appointVersion],
                                    'js/views/appoint/my/toBeEvaluated.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('toBeShared', {
                        url: '/toBeShared',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'ToBeSharedCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.ToBeSharedCtrl',
                                    ['js/controllers/appoint/my/toBeSharedCtrl.js',
                                        'styles/appoint/my/toBeShared.less?ver'+appointVersion],
                                    'js/views/appoint/my/toBeShared.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('toBeSharedDetail', {
                        url: '/toBeSharedDetail/:patient_register_service_id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'ToBeSharedDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.ToBeSharedDetailCtrl',
                                    ['js/controllers/appoint/my/toBeSharedDetailCtrl.js',
                                        'styles/appoint/my/toBeSharedDetail.less?ver'+appointVersion],
                                    'js/views/appoint/my/toBeSharedDetail.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('toBeTreated', {
                        url: '/toBeTreated',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'ToBeTreatedCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.ToBeTreatedCtrl',
                                    ['js/controllers/appoint/my/toBeTreatedCtrl.js'],
                                    'js/views/appoint/my/toBeTreated.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('toBeTreatedDetail', {
                        url: '/toBeTreatedDetail/:patient_register_service_id,:status',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'ToBeTreatedDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.ToBeTreatedDetailCtrl',
                                    ['js/controllers/appoint/my/toBeTreatedDetailCtrl.js',
                                        'styles/appoint/my/toBeTreatedDetail.less?ver'+appointVersion],
                                    'js/views/appoint/my/toBeTreatedDetail.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('withdraw', {
                        url: '/withdraw/:accountFund',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'withdrawCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.withdrawCtrl',
                                    ['js/controllers/appoint/my/withdrawCtrl.js',
                                        'styles/appoint/my/withdraw.less?ver'+appointVersion],
                                    'js/views/appoint/my/withdraw.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('withdrawSuccess', {
                        url: '/withdrawSuccess/:returnMoney',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'withdrawSuccessCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.withdrawSuccessCtrl',
                                    ['js/controllers/appoint/my/withdrawSuccessCtrl.js',
                                        'styles/appoint/my/withdrawSuccess.less?ver'+appointVersion],
                                    'js/views/appoint/my/withdrawSuccess.html?ver='+appointVersion);
                            }
                        }
                    })



                    /* 健康档案*/
                    .state('healthRecordIndex', {
                        url: '/healthRecordIndex/:index',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'healthRecordIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.healthRecordIndexCtrl',
                                    ['js/controllers/appoint/healthRecord/healthRecordIndexCtrl.js',
                                        'styles/appoint/healthRecord/healthRecordIndex.less?ver'+appointVersion,
                                        'styles/appoint/healthRecord/healthRecordCommon.less?ver'+appointVersion],
                                    'js/views/appoint/healthRecord/healthRecordIndex.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('healthRecordAppoint', {
                        url: '/healthRecordAppoint/:index,:babyName',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'healthRecordAppointCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.healthRecordAppointCtrl',
                                    ['js/controllers/appoint/healthRecord/healthRecordAppointCtrl.js',
                                        'styles/appoint/healthRecord/healthRecordAppoint.less?ver'+appointVersion],
                                    'js/views/appoint/healthRecord/healthRecordAppoint.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('healthRecordConsult', {
                        url: '/healthRecordConsult',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'healthRecordConsultCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.healthRecordConsultCtrl',
                                    ['js/controllers/appoint/healthRecord/healthRecordConsultCtrl.js',
                                        'styles/appoint/healthRecord/healthRecordConsult.less?ver'+appointVersion],
                                    'js/views/appoint/healthRecord/healthRecordConsult.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('healthRecordSelectBaby', {
                        url: '/healthRecordSelectBaby/:index,:page,:conid',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'healthRecordSelectBabyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.healthRecordSelectBabyCtrl',
                                    ['js/controllers/appoint/healthRecord/healthRecordSelectBabyCtrl.js',
                                        'styles/appoint/healthRecord/healthRecordSelectBaby.less?ver'+appointVersion],
                                    'js/views/appoint/healthRecord/healthRecordSelectBaby.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('healthRecordAddBaby', {
                        url: '/healthRecordAddBaby/:pageIndex,:conid',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'healthRecordAddBabyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.healthRecordAddBabyCtrl',
                                    ['js/controllers/appoint/healthRecord/healthRecordAddBabyCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/appoint/healthRecord/healthRecordAddBaby.less?ver'+appointVersion,
                                    'styles/lib/mobiscroll.custom-2.17.0.min.css'],
                                    'js/views/appoint/healthRecord/healthRecordAddBaby.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('healthRecordUpdateBaby', {
                        url: '/healthRecordUpdateBaby/:index,:pageIndex,:conid',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'healthRecordUpdateBabyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.healthRecordUpdateBabyCtrl',
                                    ['js/controllers/appoint/healthRecord/healthRecordUpdateBabyCtrl.js',
                                        'js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/appoint/healthRecord/healthRecordUpdateBaby.less?ver'+appointVersion,
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css'],
                                    'js/views/appoint/healthRecord/healthRecordUpdateBaby.html?ver='+appointVersion);
                            }
                        }
                    })
                    .state('healthRecordFillDisease', {
                        url: '/healthRecordFillDisease/:id,:index',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'healthRecordFillDiseaseCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.healthRecordFillDiseaseCtrl',
                                    ['js/controllers/appoint/healthRecord/healthRecordFillDiseaseCtrl.js',
                                        'styles/appoint/healthRecord/healthRecordFillDisease.less?ver'+appointVersion],
                                    'js/views/appoint/healthRecord/healthRecordFillDisease.html?ver='+appointVersion);
                            }
                        }
                    })

                $urlRouterProvider.otherwise('myselfFirst/,');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
            $rootScope.memberFunction = "false";
        })
})