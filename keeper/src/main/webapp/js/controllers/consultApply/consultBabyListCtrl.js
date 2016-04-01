angular.module('controllers', ['ionic']).controller('consultBabyListCtrl', [
    '$scope','$state','$stateParams','$ionicModal','GetUserLoginStatus','$location',
    'getBabyinfoList','$http',
    function ($scope,$state,$stateParams,$ionicModal,GetUserLoginStatus,$location,
    		getBabyinfoList,$http) {
        $scope.title0 = "宝宝信息";
        $scope.title = "宝宝信息";
        $scope.babyInfoList=[];//宝宝信息列表
        
        $scope.startConsult=function (){
        	WeixinJSBridge.call('closeWindow');
        }

        $scope.$on('$ionicView.enter',function() {
        	var routePath = "/ap/healthBBBBBB" + $location.path();
        	GetUserLoginStatus.save({routePath:routePath},function(data){
	            if(data.status=="9") {
	                window.location.href = data.redirectURL;
	            }else if(data.status=="8"){
					window.location.href = data.redirectURL+"?targeturl="+routePath;
				}else{
					//获取用户下宝宝信息列表
					getBabyinfoList.save({"openId":""},function (data){
						if(data.babyInfoList.length==0){
							window.location.href ="ap/health?value=251334#/consultAddBaby";
						}
						$scope.babyInfoList=data.babyInfoList;
					});
				}
        	});
        });
		var pData = {logContent:encodeURI("wsbbxx")};
		$http({method:'post',url:'util/recordLogs',params:pData});
    }])
