angular.module('controllers', ['ionic']).controller('healthRecordConsultCtrl', [
    '$scope','$state','$stateParams','GetUserLoginStatus'
	,'$location',
    function ($scope,$state,$stateParams,GetUserLoginStatus
    		,$location) {
        $scope.title = "订单填写";

        $scope.$on('$ionicView.enter',function() {
            var routePath = "/ap/appointBBBBBB" + $location.path();
        	GetUserLoginStatus.save({routePath:routePath},function(data){
	            if(data.status=="9") {
	                window.location.href = data.redirectURL;
	            }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }
        	});
        });
    }])
