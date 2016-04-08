/**
 * 路由
 */
define(['app'], function(app){
    return app
        .config(['$stateProvider','$urlRouterProvider',
            function($stateProvider,$urlRouterProvider) {
                var version = "1.0.1";
                $stateProvider
                    
                    /*郑玉巧在线*/
                    .state('sheOnlineIndex', {
                        url: '/sheOnlineIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'sheOnlineIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.sheOnlineIndexCtrl',
                                    files: ['/wisdom/js/controllers/sheOnline/sheOnlineIndexCtrl.js',
                                        '/wisdom/js/libs/moment.min.js']
                                }).then(function() {
                                    return $http.get('js/views/sheOnline/sheOnlineIndex.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('sheOnQuestionsDetail', {
                        url: '/sheOnQuestionsDetail/:contentId,:contentTitle',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'sheOnQuestionsDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.sheOnQuestionsDetailCtrl',
                                    files: ['/wisdom/js/controllers/sheOnline/sheOnQuestionsDetailCtrl.js']
                                }).then(function() {
                                    return $http.get('js/views/sheOnline/sheOnQuestionsDetail.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('sheOnlineDetail', {
                        url: '/sheOnlineDetail/:contentId,:contentTitle,:ReadCount,:ShareCount',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'sheOnlineDetailCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.sheOnlineDetailCtrl',
                                    files: ['/wisdom/js/controllers/sheOnline/sheOnlineDetailCtrl.js']
                                }).then(function() {
                                    return $http.get('js/views/sheOnline/sheOnlineDetail.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })

                    /* 知识库模块*/
                    .state('knowledgeIndex', {
                        url: '/knowledgeIndex',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'knowledgeIndexCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.knowledgeIndexCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/knowledgeIndexCtrl.js',
                                        '/wisdom/js/libs/mobiscroll.custom-2.5.0.min.js']
                                }).then(function() {
                                    return $http.get('js/views/knowledge/knowledgeIndex.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('knowledgeLogin', {
                        url: '/knowledgeLogin',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'knowledgeLoginCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.knowledgeLoginCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/knowledgeLoginCtrl.js',
                                        '/wisdom/js/libs/moment.min.js',
                                        '/wisdom/js/libs/mobiscroll.custom-2.5.0.min.js'
                                    ]
                                }).then(function() {
                                    return $http.get('js/views/knowledge/knowledgeLogin.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('knowledgeSearch', {
                        url: '/knowledgeSearch/:content,:generalize',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'knowledgeSearchCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.knowledgeSearchCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/knowledgeSearchCtrl.js'
                                    ]
                                }).then(function() {
                                    return $http.get('js/views/knowledge/knowledgeSearch.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('knowledgeWarn', {
                        url: '/knowledgeWarn/:birthday,:index',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'knowledgeWarnCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.knowledgeWarnCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/knowledgeWarnCtrl.js']
                                }).then(function() {
                                    return $http.get('js/views/knowledge/knowledgeWarn.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('todayReadMore', {
                        url: '/todayReadMore/:secondMenuId,:title',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'todayReadMoreCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.todayReadMoreCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/todayReadMoreCtrl.js',
                                        '/wisdom/js/libs/moment.min.js',
                                        '/wisdom/js/libs/lodash.min.js']
                                }).then(function() {
                                    return $http.get('js/views/knowledge/todayReadMore.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('todayChoiceNewborn', {
                        url: '/todayChoiceNewborn/:id,:title',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'todayChoiceNewbornCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.todayChoiceNewbornCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/todayChoiceNewbornCtrl.js']
                                }).then(function() {
                                    return $http.get('js/views/knowledge/todayChoiceNewborn.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('todayChoiceNursling', {
                        url: '/todayChoiceNursling/:id,:title',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'todayChoiceNurslingCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.todayChoiceNurslingCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/todayChoiceNurslingCtrl.js']
                                }).then(function() {
                                    return $http.get('js/views/knowledge/todayChoiceNursling.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('todayChoiceNurslingList', {
                        url: '/todayChoiceNurslingList/:id,:zhutitle,:houtitle',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'todayChoiceNurslingListCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.todayChoiceNurslingListCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/todayChoiceNurslingListCtrl.js']
                                }).then(function() {
                                    return $http.get('js/views/knowledge/todayChoiceNurslingList.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('knowledgeArticleContent', {
                        url: '/knowledgeArticleContent/:contentId,:generalize',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'knowledgeArticleContentCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.knowledgeArticleContentCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/knowledgeArticleContentCtrl.js']
                                }).then(function() {
                                    return $http.get('js/views/knowledge/knowledgeArticleContent.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('otherDisease', {
                        url: '/otherDisease/:id,:title',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'otherDiseaseCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.otherDiseaseCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/otherDiseaseCtrl.js']
                                }).then(function() {
                                    return $http.get('js/views/knowledge/otherDisease.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('knowledgeComment', {
                        url: '/knowledgeComment/:id',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'knowledgeCommentCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.knowledgeCommentCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/knowledgeCommentCtrl.js']
                                }).then(function() {
                                    return $http.get('js/views/knowledge/knowledgeComment.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })
                    .state('aboutConsult', {
                        url: '/aboutConsult',
                        templateProvider: function() { return lazyDeferred.promise; },
                        controller: 'aboutConsultCtrl',
                        resolve: {
                            load: function($templateCache, $ocLazyLoad, $q, $http) {
                                lazyDeferred = $q.defer();
                                return $ocLazyLoad.load ({
                                    name: 'app.aboutConsultCtrl',
                                    files: ['/wisdom/js/controllers/knowledge/aboutConsultCtrl.js']
                                }).then(function() {
                                    return $http.get('js/views/knowledge/aboutConsult.html?ver='+version)
                                        .success(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        }).
                                        error(function(data, status, headers, config) {
                                            return lazyDeferred.resolve(data);
                                        });
                                });
                            }
                        },
                        data: {
                            public: true
                        }
                    })

                $urlRouterProvider.otherwise('knowledgeIndex');
            }])
        .run(function ($rootScope){
            $rootScope.unBindUserPhoneNum = '';
            $rootScope.picVer = '1.0.1';
        })
})