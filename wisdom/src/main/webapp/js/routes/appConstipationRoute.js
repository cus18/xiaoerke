/**
 * 路由
 */
define(['appConstipation'], function(app){
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
                    /*  便秘管理*/
                    .state('constipationLead', {
                        url: '/constipationLead',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationLeadCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationLeadCtrl',
                                    ['js/controllers/health/constipation/constipationLeadCtrl.js',
                                        'styles/health/constipation/constipationLead.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationLead.html?ver='+ctpVersion);
                            }
                        }
                    })
                    .state('constipationQuestions', {
                        url: '/constipationQuestions',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationQuestionsCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationQuestionsCtrl',
                                    ['js/controllers/health/constipation/constipationQuestionsCtrl.js',
                                        'styles/health/constipation/constipationQuestions.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationQuestions.html?ver='+ctpVersion);
                            }
                        }
                    })
                    .state('constipationIndex', {
                        url: '/constipationIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationIndexCtrl',
                                    ['js/controllers/health/constipation/constipationIndexCtrl.js','js/libs/moment.min.js',
                                        'styles/health/constipation/constipationIndex.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationIndex.html?ver='+ctpVersion);
                            }
                        }
                    })
                    .state('constipationLight', {
                        url: '/constipationLight',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationLightCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationLightCtrl',
                                    ['js/controllers/health/constipation/constipationLightCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationCommon3.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationLight.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationLight.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationMedium', {
                        url: '/constipationMedium',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationMediumCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationMediumCtrl',
                                    ['js/controllers/health/constipation/constipationMediumCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationCommon3.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationMedium.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationMedium.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationMShortDeal', {
                        url: '/constipationMShortDeal/:type',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationMShortDealCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationMShortDealCtrl',
                                    ['js/controllers/health/constipation/constipationMShortDealCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationMShortDeal.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationMShortDeal.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationWeight', {
                        url: '/constipationWeight',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationWeightCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationWeightCtrl',
                                    ['js/controllers/health/constipation/constipationWeightCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationCommon3.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationWeight.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationWeight.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationWShortDeal', {
                        url: '/constipationWShortDeal/:type',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationWShortDealCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationWShortDealCtrl',
                                    ['js/controllers/health/constipation/constipationWShortDealCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationWShortDeal.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationWShortDeal.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationRemind', {
                        url: '/constipationRemind/:flag',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationRemindCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationRemindCtrl',
                                    ['js/controllers/health/constipation/constipationRemindCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationRemind.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationRemind.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationComment', {
                        url: '/constipationComment',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationCommentCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationCommentCtrl',
                                    ['js/controllers/health/constipation/constipationCommentCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationComment.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationComment.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationKnead', {
                        url: '/constipationKnead/:id,:planInfoId,:planTaskType',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationKneadCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationKneadCtrl',
                                    ['js/controllers/health/constipation/constipationKneadCtrl.js',
                                        'js/libs/moment.min.js',
                                        'styles/health/constipation/constipationCommon2.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationKnead.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationKnead.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationFood', {
                        url: '/constipationFood/:id,:planInfoId,:planTaskType',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationFoodCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationFoodCtrl',
                                    ['js/controllers/health/constipation/constipationFoodCtrl.js',
                                        'js/libs/moment.min.js',
                                        'styles/health/constipation/constipationCommon2.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationFood.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationFood.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationFoodAvoid', {
                        url: '/constipationFoodAvoid',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationFoodAvoidCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationFoodAvoidCtrl',
                                    ['js/controllers/health/constipation/constipationFoodAvoidCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationFoodAvoid.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationFoodAvoid.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationSports', {
                        url: '/constipationSports/:id,:planInfoId,:planTaskType',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationSportsCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationSportsCtrl',
                                    ['js/controllers/health/constipation/constipationSportsCtrl.js',
                                        'js/libs/moment.min.js',
                                        'styles/health/constipation/constipationCommon2.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationSports.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationSports.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationDefecate', {
                        url: '/constipationDefecate/:id,:planInfoId,:planTaskType',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationDefecateCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationDefecateCtrl',
                                    ['js/controllers/health/constipation/constipationDefecateCtrl.js',
                                        'js/libs/moment.min.js',
                                        'styles/health/constipation/constipationCommon2.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationDefecate.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationDefecate.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('cMotherMistakes', {
                        url: '/cMotherMistakes',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'cMotherMistakesCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.cMotherMistakesCtrl',
                                    ['js/controllers/health/constipation/cMotherMistakesCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/cMotherMistakes.less?ver='+ctpVersion],
                                    'js/views/health/constipation/cMotherMistakes.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('cMotherNecessary', {
                        url: '/cMotherNecessary',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'cMotherNecessaryCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.cMotherNecessaryCtrl',
                                    ['js/controllers/health/constipation/cMotherNecessaryCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/cMotherNecessary.less?ver='+ctpVersion],
                                    'js/views/health/constipation/cMotherNecessary.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('cMotherNecessaryDetail', {
                        url: '/cMotherNecessaryDetail/:necessaryNum',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'cMotherNecessaryDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.cMotherNecessaryDetailCtrl',
                                    ['js/controllers/health/constipation/cMotherNecessaryDetailCtrl.js',
                                        'styles/health/constipation/constipationCommon.less?ver='+ctpVersion,
                                        'styles/health/constipation/cMotherNecessaryDetail.less?ver='+ctpVersion],
                                    'js/views/health/constipation/cMotherNecessaryDetail.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationFeedback', {
                        url: '/constipationFeedback',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationFeedbackCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationFeedbackCtrl',
                                    ['js/controllers/health/constipation/constipationFeedbackCtrl.js',
                                        'styles/health/constipation/constipationCommon4.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationFeedback.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationFeedback.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationFollow', {
                        url: '/constipationFollow/:type,:planInfoId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationFollowCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationFollowCtrl',
                                    ['js/controllers/health/constipation/constipationFollowCtrl.js',
                                        'styles/health/constipation/constipationCommon4.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationFollow.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationFollow.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationFollowForget', {
                        url: '/constipationFollowForget/:show,:planInfoId',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationFollowForgetCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationFollowForgetCtrl',
                                    ['js/controllers/health/constipation/constipationFollowForgetCtrl.js',
                                        'styles/health/constipation/constipationCommon4.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationFollowForget.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationFollowForget.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('constipationEmergency', {
                        url: '/constipationEmergency',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'constipationEmergencyCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                loadFunction($templateCache, $ocLazyLoad, $q, $http,'app.constipationEmergencyCtrl',
                                    ['js/controllers/health/constipation/constipationEmergencyCtrl.js',
                                        'styles/health/constipation/constipationCommon4.less?ver='+ctpVersion,
                                        'styles/health/constipation/constipationEmergency.less?ver='+ctpVersion],
                                    'js/views/health/constipation/constipationEmergency.html?ver='+ctpVersion);
                            }
                        },
                        data: {
                            public: true
                        }
                    })

                $urlRouterProvider.otherwise('constipationIndex');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = picVersion;
        })
})