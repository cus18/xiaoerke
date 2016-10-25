/**
 * 路由
 */
define(['appNonRealTimeConsult'], function(app){
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
                    /* doctor 医生端*/
                    .state('NonTimeDoctorLogin', {
                        url: '/NonTimeDoctorLogin',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeDoctorLoginCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeDoctorLoginCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/nonRealTimeConsult/doctor/NonTimeDoctorLoginCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/doctor/NonTimeDoctorLogin.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/doctor/NonTimeDoctorLogin.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    .state('NonTimeDoctorMessageList', {
                        url: '/NonTimeDoctorMessageList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeDoctorMessageListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeDoctorMessageListCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/controllers/nonRealTimeConsult/doctor/NonTimeDoctorMessageListCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/doctor/NonTimeHistoryRecord.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/doctor/NonTimeDoctorMessageList.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    .state('NonTimeDoctorConversation', {
                        url: '/NonTimeDoctorConversation/:sessionId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeDoctorConversationCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeDoctorConversationCtrl',
                                    [
                                        'js/libs/lodash.min.js',
                                        'js/libs/scrollglue.js',
                                        'js/libs/angular-file-upload-shim.min.js',
                                        'js/libs/angular-file-upload.min.js',
                                        'js/libs/jquery.qqFace.js',
                                        'js/controllers/nonRealTimeConsult/doctor/NonTimeDoctorConversationCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/doctor/NonTimeDoctorConversation.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/doctor/NonTimeDoctorConversation.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                   /* 用户端*/
                    /*咨询详情*/
                    .state('NonTimeUserConversation', {
                        url: '/NonTimeUserConversation/:sessionId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserConversationCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserConversationCtrl',
                                    [
                                        'js/libs/jquery.qqFace.js',"js/libs/jquery.browser.min.js",
                                        'js/libs/scrollglue.js',
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserConversationCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/user/NonTimeUserConversation.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserConversation.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    /*咨询列表*/
                    .state('NonTimeUserConsultList', {
                        url: '/NonTimeUserConsultList',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserConsultListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserConsultListCtrl',
                                    [
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserConsultListCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/doctor/NonTimeHistoryRecord.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserConsultList.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    /*首次咨询*/
                    .state('NonTimeUserFirstConsult', {
                        url: '/NonTimeUserFirstConsult/:doctorId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserFirstConsultCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserFirstConsultCtrl',
                                    ['js/libs/mobiscroll.custom-2.17.0.min.js',
                                        'styles/lib/mobiscroll.custom-2.17.0.min.css',
                                        "js/libs/angular-file-upload.min.js",
                                        "js/libs/angular-file-upload-shim.min.js",
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserFirstConsultCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/user/NonTimeUserFirstConsult.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserFirstConsult.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    /*找医生*/
                    .state('NonTimeUserFindDoctor', {
                        url: '/NonTimeUserFindDoctor',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserFindDoctorCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserFindDoctorCtrl',
                                    [
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserFindDoctorCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/user/NonTimeUserFindDoctor.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserFindDoctor.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })
                    /*医生列表*/
                    .state('NonTimeUserDoctorList', {
                        url: '/NonTimeUserDoctorList/:department',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'NonTimeUserDoctorListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.NonTimeUserDoctorListCtrl',
                                    [
                                        'js/controllers/nonRealTimeConsult/user/NonTimeUserDoctorListCtrl.js?ver='+nonRealTimeConsultVersion,
                                        'styles/nonRealTimeConsult/user/NonTimeUserDoctorList.less?ver='+nonRealTimeConsultVersion],
                                    'js/views/nonRealTimeConsult/user/NonTimeUserDoctorList.html?ver='+nonRealTimeConsultVersion);
                            }
                        }
                    })

                $urlRouterProvider.otherwise('');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
            $rootScope.memberFunction = "false";
        })

        .run(['$anchorScroll', function($anchorScroll) {
            $anchorScroll.yOffset = 50;
            // 默认向下便宜50px
            // 在此处配置偏移量
        }])

    .filter('emotionReceive',function(){
            return function(val,messageType){
                if(messageType = "text"){
                    val = val.replace(/\/::\)/g, '[em_1]');val = val.replace(/\/::~/g, '[em_2]');val = val.replace(/\/::B/g, '[em_3]');val = val.replace(/\/::\|/g, '[em_4]');
                    val = val.replace(/\/:8-\)/g, '[em_5]');val = val.replace(/\/::</g, '[em_6]');val = val.replace(/\/::X/g, '[em_7]');val = val.replace(/\/::Z/g, '[em_8]');
                    val = val.replace(/\/::</g, '[em_9]');val = val.replace(/\/::-\|/g, '[em_10]');val = val.replace(/\/::@/g, '[em_11]');val = val.replace(/\/::P/g, '[em_12]');
                    val = val.replace(/\/::D/g, '[em_13]');val = val.replace(/\/::O/g, '[em_14]');val = val.replace(/\/::\(/g, '[em_15]');val = val.replace(/\/:--b/g, '[em_16]');
                    val = val.replace(/\/::Q/g, '[em_17]');val = val.replace(/\/::T/g, '[em_18]');val = val.replace(/\/:,@P/g, '[em_19]');val = val.replace(/\/:,@-D/g, '[em_20]');
                    val = val.replace(/\/::d/g, '[em_21]');val = val.replace(/\/:,@-o/g, '[em_22]');val = val.replace(/\/::g/g, '[em_23]');val = val.replace(/\/:\|-\)/g, '[em_24]');
                    val = val.replace(/\/::!/g, '[em_25]');val = val.replace(/\/::L/g, '[em_26]');val = val.replace(/\/::>/g, '[em_27]');val = val.replace(/\/::,@/g, '[em_28]');
                    val = val.replace(/\/:,@f/g, '[em_29]');val = val.replace(/\/::-S/g, '[em_30]');val = val.replace(/\/:\?/g, '[em_31]');val = val.replace(/\/:,@x/g, '[em_32]');
                    val = val.replace(/\/:,@@/g, '[em_33]');val = val.replace(/\/::8/g, '[em_34]');val = val.replace(/\/:,@!/g, '[em_35]');val = val.replace(/\/:xx/g, '[em_36]');
                    val = val.replace(/\/:bye/g, '[em_37]');val = val.replace(/\/:wipe/g, '[em_38]');val = val.replace(/\/:dig/g, '[em_39]');val = val.replace(/\/:&-\(/g, '[em_40]');
                    val = val.replace(/\/:B-\)/g, '[em_41]');val = val.replace(/\/:<@/g, '[em_42]');val = val.replace(/\/:@>/g, '[em_43]');val = val.replace(/\/::-O/g, '[em_44]');
                    val = val.replace(/\/:>-\|/g, '[em_45]');val = val.replace(/\/:P-\(/g, '[em_46]');val = val.replace(/\/::'\|/g, '[em_47]');val = val.replace(/\/:X-\)/g, '[em_48]');
                    val = val.replace(/\/::\*/g, '[em_49]');val = val.replace(/\/:@x/g, '[em_50]');val = val.replace(/\/:8\*/g, '[em_51]');val = val.replace(/\/:hug/g, '[em_52]');
                    val = val.replace(/\/:moon/g, '[em_53]');val = val.replace(/\/:sun/g, '[em_54]');val = val.replace(/\/:bome/g, '[em_55]');val = val.replace(/\/:!!!/g, '[em_56]');
                    val = val.replace(/\/:pd/g, '[em_57]');val = val.replace(/\/:pig/g, '[em_58]');val = val.replace(/\/:<W>/g, '[em_59]');val = val.replace(/\/:coffee/g, '[em_60]');
                    val = val.replace(/\/:eat/g, '[em_61]');val = val.replace(/\/:heart/g, '[em_62]');val = val.replace(/\/:strong/g, '[em_63]');val = val.replace(/\/:weak/g, '[em_64]');
                    val = val.replace(/\/:share/g, '[em_65]');val = val.replace(/\/:v/g, '[em_66]');val = val.replace(/\/:@\)/g, '[em_67]');val = val.replace(/\/:jj/g, '[em_68]');
                    val = val.replace(/\/:ok/g, '[em_69]');val = val.replace(/\/:no/g, '[em_70]');val = val.replace(/\/:rose/g, '[em_71]');val = val.replace(/\/:fade/g, '[em_72]');
                    val = val.replace(/\/:showlove/g, '[em_73]');val = val.replace(/\/:love/g, '[em_74]');val = val.replace(/\/:<L>/g, '[em_75]');


                    val = val.replace(/\</g,'&lt;');
                    val = val.replace(/\>/g,'&gt;');
                    val = val.replace(/\n/g,'<br/>');
                    val = val.replace(/\[em_([0-9]*)\]/g,'<img src="http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F$1.gif" border="0" />');
                    return val;
                }

            }
     });
})