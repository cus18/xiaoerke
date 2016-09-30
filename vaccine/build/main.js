var app = angular.module('app', ['ui.router', 'ngCookies', 'ngGrid']);
var SERVER_BASE_URL = 'http://123.57.45.33:8088/';

//初始化配置
app.run(['$rootScope', function($rootScope) {
    $rootScope.appName = '图班网';
    $rootScope.desc = '后台管理';
    $rootScope.author = '鬼谣';
    $rootScope.email = 'wlhmyit@126.com';
}]);

//服务列表
app.constant('ServiceConfig', {
    user_login: SERVER_BASE_URL + 'user/login',
    user_getAll: SERVER_BASE_URL + 'user/getAll',
    user_updateTag: SERVER_BASE_URL + 'user/updateTag',
    user_getUserByCondition: SERVER_BASE_URL + 'user/getByCondition',
    user_delete: SERVER_BASE_URL + 'user/delete',
    article_create: SERVER_BASE_URL + 'article/create',
    weibo_getByCondition: SERVER_BASE_URL + 'wei/getByCondition',
    weibo_set2null: SERVER_BASE_URL + 'wei/set2null',
    email_findPassword: SERVER_BASE_URL + 'email/findPassword',
    vaccine_index: SERVER_BASE_URL + 'vaccine/getVaccineStation'
});


//JSON parse
app.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    $httpProvider.defaults.transformRequest = [function(data) {
        var obj = [];
        for (var key in data) {
            obj.push(key + '=' + data[key]);
        }
        return obj.join('&');
    }];
}]);

//路由配置
app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    /*URL路由*/
    $urlRouterProvider.otherwise("/");
    /*状态配置*/
    $stateProvider
    //登录
        .state('login', {
        url: '/login',
        views: {
            '': {
                templateUrl: 'views/login.html',
                controller: 'LoginController'
            }
        }
    })

    .state('index', {
            url: '/index',
            views: {
                '': {
                    templateUrl: 'views/index.html',
                    controller: 'MenuController'
                }
            }
        })
        .state('vaccineIndex', {
            url: '/',
            views: {
                '': {
                    templateUrl: 'views/vaccineIndex.html',
                    controller: 'VaccineIndexController'
                }
            }
        })

    .state('index.article', {
        url: '/article',
        views: {
            '': {
                templateUrl: 'views/index.html',
                controller: 'MenuController'
            },
            'list@index': {
                templateUrl: 'views/article.html',
                controller: 'ArticleController'
            }
        }
    })

    .state('index.weibo', {
        url: '/weibo',
        views: {
            '': {
                templateUrl: 'views/index.html',
                controller: 'MenuController'
            },
            'list@index': {
                templateUrl: 'views/weibo.html',
                controller: 'WeiboController'
            }
        }
    })

    .state('index.user', {
        url: '/user',
        views: {
            '': {
                templateUrl: 'views/index.html',
                controller: 'MenuController'
            },
            'list@index': {
                templateUrl: 'views/user.html',
                controller: 'UserController'
            }
        }
    })

    .state('index.email', {
        url: '/email',
        views: {
            '': {
                templateUrl: 'views/index.html',
                controller: 'MenuController'
            },
            'list@index': {
                templateUrl: 'views/email.html',
                controller: 'EmailController'
            }
        }
    });
}]);
;app.service('MenuSelect', ['$rootScope',
function($rootScope){
	return {
		setSelected: function(name){
			$rootScope.select_user = 'menu_unselect';
			$rootScope.select_article = 'menu_unselect';
			$rootScope.select_weibo = 'menu_unselect';
			$rootScope.select_email = 'menu_unselect';
			$rootScope.select_login = 'menu_unselect';
			
			if(name){
				$rootScope[name] = 'menu_select';
			}
		}
	}
}]);
;
app.controller('LoginController',
['$scope', '$http', '$rootScope', '$cookieStore', '$timeout', '$location', 'ServiceConfig', 
function($scope, $http, $rootScope, $cookieStore, $timeout, $location, ServiceConfig) {
	$scope.login = function() {
		var email = $scope.email,
			password = $scope.password,
			data = {
				'email': email,
				'password': password
			};
		$http.post(ServiceConfig.user_login, data).success(function(data) {
			if (data.status) {
				$cookieStore.put('user', data);
				$location.path('/index/user');
			} else {
				var width = window.innerWidth;
				Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '难倒你忘记了密码...', 1);
				$timeout(Tip.hideTip, 3000);
			}
		}).error(function() {
			var width = window.innerWidth;
			Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '服务君该回家养老了...', 1);
			$timeout(Tip.hideTip, 3000);
		});
	};

	$scope.goRegister = function() {
		$location.path('/register');
	};
}]);;
app.controller('ArticleController', 
['$scope', '$rootScope', '$timeout', '$http', '$cookieStore', 'ServiceConfig', 'MenuSelect',
function($scope, $rootScope, $timeout, $http, $cookieStore, ServiceConfig, MenuSelect) {
	var user = $cookieStore.get('user');
	var width = window.innerWidth;
	
	MenuSelect.setSelected('select_article');
	
	$scope.author = '';
	
	$scope.submit = function(title, author, link, ref, content){
		var article = {
			token: user.token,
			title: title || '',
			author: author || '',
			link: link || '',
			ref: ref || '',
			content: content || []
		};
		
		$http.post(ServiceConfig.article_create, article).success(function(data){
			if(data.status){
				$scope.author = '';
				$scope.title = '';
				$scope.author = '';
				$scope.ref = '';
				$scope.link = '';
				$scope.content = '';
				
				Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '文章发表成功....', 1);
				$timeout(Tip.hideTip, 3000);
			}else{
				Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '文章发表失败....', 1);
				$timeout(Tip.hideTip, 3000);
			}
		});
	};
}]);;
app.controller('MenuController', [
'$rootScope', '$location',
function($rootScope, $location){
	//menu_select
	//menu_unselect
	//控制按钮选中状态使用rootscope更好！这样，不必监听多处简单变量
	var path = $location.path();
	switch(path){
		case '/index/user':
			$rootScope.select_user = 'menu_select';
			break;
		case '/index/article':
			$rootScope.select_article = 'menu_select';
		    break;
		case '/index/weibo':
			$rootScope.select_weibo = 'menu_select';
		    break;
		case '/index/qita':
			$rootScope.select_qita= 'menu_select';
		    break;
		case '/index/login':
			$rootScope.select_login = 'menu_select';
		    break;
		default:
			break;
	}
}]);
;
app.controller('UserController', 
['$scope', '$rootScope', '$timeout', '$http', '$cookieStore', 'ServiceConfig', 'MenuSelect',
function($scope, $rootScope, $timeout, $http, $cookieStore, ServiceConfig, MenuSelect) {
	var user = $cookieStore.get('user');
	var width = window.innerWidth;
	
	MenuSelect.setSelected('select_user');
	
	//分页
	$scope.mySelections = [];
	$scope.filterOptions = {
        filterText: "",
        useExternalFilter: true
    }; 
    $scope.totalServerItems = 0;
    $scope.pagingOptions = {
        pageSizes: [10, 15, 30],
        pageSize: 10,
        currentPage: 1
    };	
	$scope.setPagingData = function(data, page, pageSize){	
        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
        $scope.myData = pagedData;
        $scope.totalServerItems = data.length;
        if (!$scope.$$phase) {
            $scope.$apply();
        }
    };
    $scope.getPagedDataAsync = function (pageSize, page) {
        setTimeout(function () {
            $http.get(ServiceConfig.user_getAll + '?token=' + user.token).success(function (data) {
            	if(data.status){
            		$scope.setPagingData(data.items, page, pageSize);
            	}
            }); 
        }, 100);
    };
	
    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
	
    $scope.$watch('pagingOptions', function (newVal, oldVal) {
        if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
          	$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
        }
    }, true);
    
	$scope.$on('ngGridEventEndCellEdit', function(evt){
    	var obj = evt.targetScope.row.entity;
    	var data = {
    		userid: obj.userid,
    		token: user.token,
    		tag: obj.tag
    	};
    	$http.post(ServiceConfig.user_updateTag, data).success(function(data){
    		if(!data.status){
				Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '数据更新失败', 1);
				$timeout(Tip.hideTip, 3000);
    		}
    	});
	});
	
	$scope.removeRow = function(entity){
		//确认提示框
		//alert('确定删除');
		var data = {
			token: user.token,
			userid: entity.userid
		};
		console.log(ServiceConfig.user_delete);
		$http.post(ServiceConfig.user_delete, data).success(function(data){
			if(data.status){
				Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '删除成功......', 1);
				$timeout(Tip.hideTip, 3000);
			}else{
				Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '删除失败......', 1);
				$timeout(Tip.hideTip, 3000);
			}
		});
	};
	
    $scope.gridOptions = {
        data: 'myData',
        enablePaging: true,
		showFooter: true,
		enableCellEdit: true,
        totalServerItems: 'totalServerItems',
        pagingOptions: $scope.pagingOptions,
        multiSelect: false,
        selectedItems: $scope.mySelections,
        columnDefs: [
    		{field:'userid', displayName:'用户ID'},
    		{field:'email', displayName:'邮箱'}, 
    		{field:'tag', displayName:'角色'},
    		{field:'nickname', displayName:'昵称'},
    		{field:'realname', displayName:'真实姓名'},
    		{field:'tel', displayName:'电话'},
    		{ field: '慎重操作', cellTemplate: '<button class="user_btn" ng-click="removeRow(row.entity)">删除</button>' }
    	]
    };
    
    //搜索
    $scope.Search = function($event, choose, keywords){
    	if($event.keyCode === 13 || $event.which === 32){
    		var condition = '?token=' + user.token + '&';
    		var choose = choose || 'default';
    		switch(choose){
    			case 'email':
    				condition = condition + 'email=' + keywords;
    				break;
				case 'nickname':
					condition = condition + 'nickname=' + keywords;
    				break;
				case 'realname':
					condition = condition + 'realname=' + keywords;
    				break;
    			default:
    				console.log(keywords);
    				condition = condition + 'email=' + keywords;
    				break;
    		}
    		$http.get(ServiceConfig.user_getUserByCondition + condition).success(function(data){
				if(data.status){
					$scope.mySelections = data.items;
					//后期做一个弹出框，让用户自己选择查询到的用户，然后进行显示
					//或者展示出所有的用户信息
				}else{
					
				}
			});
    	}
    };
	
}]);






;
app.controller('WeiboController',
['$scope', '$rootScope', '$http', '$cookieStore', '$timeout','ServiceConfig', 'MenuSelect',
function($scope, $rootScope, $http, $cookieStore, $timeout, ServiceConfig, MenuSelect) {
	var user = $cookieStore.get('user');
	var width = window.innerWidth;
	
	MenuSelect.setSelected('select_weibo');
	
	$scope.email = '';
	
	$scope.search = function(email, nickname, realname, keyweords){
		var condition = '';
		if(email){
			condition = '?email=' + email;
		}
		if(nickname){
			condition = '?nickname=' + nickname;
		}
		if(realname){
			condition = '?realname=' + realname;
		}
		if(keyweords){
			condition = '?content=' + keyweords;
		}
		$http.get(ServiceConfig.weibo_getByCondition + condition).success(function(data){
			$scope.weiboObj = JSON.stringify(data);
		});
	};
	
	$scope.modify = function(){
		var data = {
			token: user.token,
			_id: JSON.parse($scope.weiboObj)._id
		};

		$http.post(ServiceConfig.weibo_set2null, data).success(function(data){
			if(data.status){
				Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '微博置空成功....', 1);
				$timeout(Tip.hideTip, 3000);
			}else{
				Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '微博置空失败....', 1);
				$timeout(Tip.hideTip, 3000);
			}
		});
	};
}]);;
app.controller('EmailController',
['$scope', '$rootScope', '$http', '$timeout', '$cookieStore', 'ServiceConfig', 'MenuSelect',
function($scope, $rootScope, $http, $timeout, $cookieStore, ServiceConfig, MenuSelect) {
	var user = $cookieStore.get('user');
	var width = window.innerWidth;
	
	MenuSelect.setSelected('select_email');
	
	$scope.postEmail = function(email){
		var data = {
			token: user.token,
			email: email
		};
		
		$http.post(ServiceConfig.email_findPassword, data).success(function(data){
			if(data.status){
				Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '邮件发送成功....', 1);
				$timeout(Tip.hideTip, 3000);
			}else{
				Tip.setTip(250, (parseInt(width) - 240) / 2, null, null, 260, 80, '邮件发送失败....', 1);
				$timeout(Tip.hideTip, 3000);
			}
		});
	};
}]);;app.controller('VaccineIndexController', [
    '$scope', '$http', 'ServiceConfig',
    function($scope, $http, ServiceConfig) {
        $scope.showInput = function() {
            var date = new Date(+new Date() + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '');
            $("#babyBirthday").mobiscroll().date();
            //初始化日期控件
            var opt = {
                preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
                theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
                display: 'modal', //显示方式 ，可选：modal\inline\bubble\top\bottom
                mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
                lang: 'zh',
                dateFormat: 'yyyy-mm-dd', // 日期格式
                setText: '确定', //确认按钮名称
                cancelText: '取消', //取消按钮名籍我
                dateOrder: 'yyyymmdd', //面板中日期排列格式
                dayText: '日',
                monthText: '月',
                yearText: '年', //面板中年月日文字
                showNow: false,
                nowText: "今",
                startYear: 1960, //开始年份
                //endYear:currYear //结束年份
                /*minDate: new Date(2002,date.substring(5,7)-1,date.substring(8,10)),*/
                maxDate: new Date(date.substring(0, 4), date.substring(5, 7) - 1, date.substring(8, 10))
                    //endYear:2099 //结束年份
            };
            $("#babyBirthday").mobiscroll(opt);
            $("#babyBirthday").mobiscroll("show");
        };
        var data = "";
        $http.post(ServiceConfig.vaccine_index, data).success(function(data) {
            console.log(data)
        }).error(function() {});

        $scope.isSelectedG = true;
        $scope.isSelectedB = false;
        //选择孩子性别
        $scope.selectSex = function(sex) {
            $scope.sexItem = sex;
            if ($scope.sexItem == 0) {
                $scope.isSelectedB = true;
                $scope.isSelectedG = false;
            }
            if ($scope.sexItem == 1) {
                $scope.isSelectedG = true;
                $scope.isSelectedB = false;
            }
        };
        $(".select-area .select-value").each(function() {
            if ($(this).next("select").find("option:selected").length != 0) {
                $(this).text($(this).next("select").find("option:selected").text());
            }
        });
        $(".select-area select").change(function() {
            var value = $(this).find("option:selected").text();
            $(this).parent(".select-area").find(".select-value").text(value);
        });
    }
]);
