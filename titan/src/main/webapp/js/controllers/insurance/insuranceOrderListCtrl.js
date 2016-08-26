angular.module('controllers', ['ionic']).controller('insuranceOrderListCtrl', [
    '$scope','$state','$stateParams','getInsuranceRegisterServiceListByUserid','$location','GetUserLoginStatus',
    function ($scope,$state,$stateParams,getInsuranceRegisterServiceListByUserid,$location,GetUserLoginStatus) {

        $scope.num = "";
        $scope.service = "current";
        $scope.insuranceViedList=[];
        $scope.insuranceInvalidList=[];
        $scope.$on('$ionicView.enter', function(){
            var routePath = "/insuranceBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                } else if(data.status=="8"){
                    window.location.href = ata.redirectURL+"?targeturl="+routePath;
                }else {
                    getInsuranceRegisterServiceListByUserid.get({},function (data){
                        $scope.insuranceViedList=data.insuranceViedList;
                        $scope.insuranceInvalidList=data.insuranceInvalidList;
                    });
                }
            });
        });

        $scope.selectService = function(select){
            if(select=="current"){
                $scope.service = "current";
            }
            else{
                $scope.service = "history";
            }
        };
        $scope.lookDetail = function(id){
            $state.go('insuranceOrderDetail',{id:id});
        };

    }]);


