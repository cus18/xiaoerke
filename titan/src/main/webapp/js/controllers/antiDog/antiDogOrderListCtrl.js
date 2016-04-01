angular.module('controllers', ['ionic']).controller('antiDogOrderListCtrl', [
    '$scope','$state','$stateParams','getInsuranceRegisterServiceListByUserid','$location','GetUserLoginStatus',
    function ($scope,$state,$stateParams,getInsuranceRegisterServiceListByUserid,$location,GetUserLoginStatus) {

        $scope.num = "";
        $scope.service = "current";
        $scope.insuranceViedList=[];
        $scope.insuranceInvalidList=[];

        $scope.selectService = function(select){
            if(select=="current"){
                $scope.service = "current";
            }
            else{
                $scope.service = "history";
            }
        };
        $scope.lookDetail = function(select){
             $state.go('antiDogOrderDetail',{index:select});
        };

        $scope.$on('$ionicView.enter', function(){
        	var routePath = "/ap/insuranceBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                } else if(data.status=="8"){
                    window.location.href = ata.redirectURL+"?targeturl="+routePath;
                }else {
                	getInsuranceRegisterServiceListByUserid.get(function (data){
                        console.log("dd",data);
                		$scope.insuranceViedList=data.insuranceViedList;
                		$scope.insuranceInvalidList=data.insuranceInvalidList;
                      });
                }
            });
        	

        })
    }]);

