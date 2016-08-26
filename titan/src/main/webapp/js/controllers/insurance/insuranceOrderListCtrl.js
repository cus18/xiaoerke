angular.module('controllers', ['ionic']).controller('insuranceOrderListCtrl', [
    '$scope','$state','$stateParams','getInsuranceRegisterServiceListByUserid','$location','GetUserLoginStatus','getInsuranceInfo',
    function ($scope,$state,$stateParams,getInsuranceRegisterServiceListByUserid,$location,GetUserLoginStatus,getInsuranceInfo) {

        $scope.num = "";
        $scope.service = "current";
        $scope.insuranceViedList=[];
        $scope.insuranceInvalidList=[];
        $scope.insuranceArr=[];


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
                    getInsuranceInfo.save({},function (data){
                        console.log("data.insuranceInfo ",data.insuranceInfo);
                        $scope.insuranceArr=$scope.insuranceArr.concat(data.insuranceInfo.antiDog.split(";"));
                        $scope.insuranceArr=$scope.insuranceArr.concat(data.insuranceInfo.handFootMouth.split(";"));
                        $scope.insuranceArr=$scope.insuranceArr.concat(data.insuranceInfo.pneumonia.split(";"));
                         console.log(" $scope.insuranceArr", $scope.insuranceArr);


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

