angular.module('controllers', ['ionic']).controller('antiDogOrderDetailCtrl', [
    '$scope','$state','$stateParams','$http','getInsuranceRegisterServiceById','$location','GetUserLoginStatus',
    function ($scope,$state,$stateParams,$http,getInsuranceRegisterServiceById,$location,GetUserLoginStatus) {

        $scope.num = "";
        $scope.service = "current";
        $scope.insurance={};
        $scope.parRemindLock =false;


        $scope.selectService = function(select){
            if(select=="current"){
                $scope.service = "current";
            }
            else{
                $scope.service = "history";
            }

          /*  $state.go('constipationRemind',{flag:$stateParams.type});*/
        };
        // 我要领补贴提醒

        $scope.receiveMoney=function(){
            $scope.parRemindLock =true;
        	var pData = {logContent:encodeURI("FQB_DDXQ_LBT")};
            $http({method:'post',url:'ap/util/recordLogs',params:pData});
        }
        //取消 补贴 提醒
        $scope.cancelPayRemind = function(){
            $scope.parRemindLock =false;
        };
        $scope.$on('$ionicView.enter', function(){
        	var routePath = "/ap/insuranceBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                } else if(data.status=="8"){
                    window.location.href = ata.redirectURL+"?targeturl="+routePath;
                }else {
                	var id=$stateParams.index;
                	getInsuranceRegisterServiceById.save({"id":id}, function (data){
                		$scope.insurance=data.InsuranceRegisterService;
                		
                     });
                }
            });
        })
    }]);

