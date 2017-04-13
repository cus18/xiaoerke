/**
 * 路由
 */
define(['appBaoFansCamp'], function(app){
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
                    /*other部分*/
                    .state('feedback', {
                        url: '/feedback',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'feedbackCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.feedbackCtrl',
                                    ['js/controllers/baoFansCamp/other/feedbackCtrl.js',
                                        'styles/baoFansCamp/other/feedback.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/other/feedback.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('guide', {
                        url: '/guide',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'guideCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.guideCtrl',
                                    ['js/controllers/baoFansCamp/other/guideCtrl.js',
                                        'styles/baoFansCamp/other/guide.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/other/guide.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('questions', {
                        url: '/questions',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'QuestionsCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.QuestionsCtrl',
                                    ['js/controllers/baoFansCamp/other/questionsCtrl.js'
                                        ,'styles/baoFansCamp/other/questions.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/other/questions.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('userEvaluate', {
                        url: '/userEvaluate/:doctorId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'userEvaluateCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.userEvaluateCtrl',
                                    ['js/controllers/baoFansCamp/other/userEvaluateCtrl.js',
                                        'styles/baoFansCamp/other/userEvaluate.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/other/userEvaluate.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('consultShare', {
                        url: '/consultShare',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'consultShareCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.consultShareCtrl',
                                    ['js/controllers/baoFansCamp/other/consultShareCtrl.js',
                                        'styles/baoFansCamp/other/consultShare.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/other/consultShare.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('consultShareBeHoo', {
                        url: '/consultShareBeHoo',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'consultShareBeHooCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.consultShareBeHooCtrl',
                                    ['js/controllers/baoFansCamp/other/consultShareBeHooCtrl.js',
                                        'styles/baoFansCamp/other/consultShare.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/other/consultShareBeHoo.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('consultVisit', {
                        url: '/consultVisit/:openId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'consultVisitCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.consultVisitCtrl',
                                    ['js/controllers/baoFansCamp/other/consultVisitCtrl.js',
                                        'js/libs/angular-animate.min.js',
                                        'styles/baoFansCamp/other/consultVisit.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/other/consultVisit.html?ver='+baoFansCampVersion);
                            }
                        }
                    })

                    /*实用指南*/
                    .state('guideFirst', {
                        url: '/guideFirst',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'guideFirstCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.guideFirstCtrl',
                                    ['js/controllers/baoFansCamp/guideBook/guideFirstCtrl.js',
                                        'styles/baoFansCamp/guideBook/guideFirst.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/guideBook/guideFirst.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('guideConsult', {
                        url: '/guideConsult',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'guideConsultCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.guideConsultCtrl',
                                    ['js/controllers/baoFansCamp/guideBook/guideConsultCtrl.js',
                                        'styles/baoFansCamp/guideBook/guideConsult.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/guideBook/guideConsult.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('guideAppointment', {
                        url: '/guideAppointment',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'guideAppointmentCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.guideAppointmentCtrl',
                                    ['js/controllers/baoFansCamp/guideBook/guideAppointmentCtrl.js',
                                        'styles/baoFansCamp/guideBook/guideAppointment.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/guideBook/guideAppointment.html?ver='+baoFansCampVersion);

                            }
                        }
                    })
                    .state('guideSheSaid', {
                        url: '/guideSheSaid',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'guideSheSaidCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.guideSheSaidCtrl',
                                    ['js/controllers/baoFansCamp/guideBook/guideSheSaidCtrl.js',
                                        'styles/baoFansCamp/guideBook/guideSheSaid.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/guideBook/guideSheSaid.html?ver='+baoFansCampVersion);
                            }
                        }
                    })

                    /* 运营活动*/
                    .state('operateIndex', {
                        url: '/operateIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'operateIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.operateIndexCtrl',
                                    ['js/controllers/baoFansCamp/operate/operateIndexCtrl.js',
                                        'styles/baoFansCamp/operate/operateIndex.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/operate/operateIndex.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('operateActiveRule', {
                        url: '/operateActiveRule',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'operateActiveRuleCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.operateActiveRuleCtrl',
                                    ['js/controllers/baoFansCamp/operate/operateActiveRuleCtrl.js',
                                        'styles/baoFansCamp/operate/operateActiveRule.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/operate/operateActiveRule.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('operateFriendsList', {
                        url: '/operateFriendsList/:ShareNum,:url',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'operateFriendsListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.operateFriendsListCtrl',
                                    ['js/controllers/baoFansCamp/operate/operateFriendsListCtrl.js',
                                        'styles/baoFansCamp/operate/operateFriendsList.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/operate/operateFriendsList.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    .state('operateShare', {
                        url: '/operateShare/:url',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'operateShareCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.operateShareCtrl',
                                    ['js/controllers/baoFansCamp/operate/operateShareCtrl.js',
                                        'styles/baoFansCamp/operate/operateShare.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/operate/operateShare.html?ver='+baoFansCampVersion);
                            }
                        }
                    })

                    /* 测试题*/
                    .state('testV2', {
                        url: '/testV2',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'testV2Ctrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.testV2Ctrl',
                                    ['js/controllers/baoFansCamp/testQuestion/testV2Ctrl.js',
                                        'styles/baoFansCamp/testQuestion/testV2.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/testQuestion/testV2.html?ver='+baoFansCampVersion);
                            }
                        }
                    })

                    /* 第三方合作*/
                    .state('youfumama', {
                        url: '/youfumama',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'youFuMamaCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.youFuMamaCtrl',
                                    ['js/controllers/baoFansCamp/third/youfumamaCtrl.js',
                                        'styles/baoFansCamp/third/youfumama.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/third/youfumama.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    /* 2016您与宝大夫的故事*/
                    .state('story2016Index', {
                        url: '/story2016Index',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'story2016IndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.story2016IndexCtrl',
                                    ['js/controllers/baoFansCamp/story2016/story2016IndexCtrl.js',
                                    'js/libs/swiper.min.js',
                                    'js/libs/swiper.animate.min.js',
                                        'styles/baoFansCamp/story2016/swiper.min.css?ver'+baoFansCampVersion,
                                        'styles/baoFansCamp/story2016/animate.min.css?ver'+baoFansCampVersion,
                                        'styles/baoFansCamp/story2016/animateSelf.css?ver'+baoFansCampVersion,
                                        'styles/baoFansCamp/story2016/story2016Index.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/story2016/story2016Index.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    /* 店长招聘*/
                    .state('recruitShopkeeper', {
                        url: '/recruitShopkeeper',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'recruitShopkeeperCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.recruitShopkeeperCtrl',
                                    ['js/controllers/baoFansCamp/recruit/recruitShopkeeperCtrl.js',
                                        'js/libs/swiper.min.js',
                                        'js/libs/swiper.animate.min.js',
                                        'styles/baoFansCamp/story2016/swiper.min.css?ver'+baoFansCampVersion,
                                        'styles/baoFansCamp/story2016/animate.min.css?ver'+baoFansCampVersion,
                                        'styles/baoFansCamp/recruit/recruitShopkeeper.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/recruit/recruitShopkeeper.html?ver='+baoFansCampVersion);
                            }
                        }
                    })
                    /* 英语活动*/
                    .state('activityEnglish', {
                        url: '/activityEnglish',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'activityEnglishCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.activityEnglishCtrl',
                                    ['js/controllers/baoFansCamp/other/activityEnglishCtrl.js',
                                        'styles/baoFansCamp/other/activityEnglish.less?ver'+baoFansCampVersion],
                                    'js/views/baoFansCamp/other/activityEnglish.html?ver='+baoFansCampVersion);
                            }
                        }
                    })






                $urlRouterProvider.otherwise('guideFirst');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
            $rootScope.memberFunction = "false";
        })
})