angular.module('controllers', ['ionic']).controller('healthRecordAppointCtrl', [
    '$scope','$state','$stateParams','getAppointmentInfo','GetUserLoginStatus',
    '$location',
    function ($scope,$state,$stateParams,getAppointmentInfo,GetUserLoginStatus,
    		$location) {
        $scope.title = "预约记录";
        $scope.title0 = "预约记录";
        $scope.orderInfo={};
        /* 填写病情诊断*/
        $scope.fillDisease = function(id){
            $state.go("healthRecordFillDisease",{id:id});
        }

        $scope.$on('$ionicView.enter',function() {
        	var routePath = encodeURI(encodeURI("/appointBBBBBB" + $location.path()));
        	GetUserLoginStatus.save({routePath:routePath},function(data){
	            if(data.status=="9") {
	                window.location.href = data.redirectURL;
	            } else if(data.status=="8"){
                    window.location.href = encodeURI(encodeURI(data.redirectURL+"?targeturl="+routePath));
                }else {
	            	var listIndex = $stateParams.index;
	                var babyName = $stateParams.babyName;
		        	//根据宝宝信息获取
		    		getAppointmentInfo.get({'babyId':encodeURI(babyName)},function (data){
		    			if(data!=""){
		    				$scope.orderInfo=data.orderInfoList[listIndex];
		    			}
		            });
	            }
            });	
        });
    }])
