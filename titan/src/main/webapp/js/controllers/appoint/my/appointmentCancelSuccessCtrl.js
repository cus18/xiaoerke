angular.module('controllers', ['ionic']).controller('AppointmentCancelSuccessCtrl', [
        '$scope','$state','$stateParams','GetUserLoginStatus','MyselfInfo','$rootScope','$location',
        function ($scope,$state,$stateParams,GetUserLoginStatus,MyselfInfo,$rootScope,$location) {

            $scope.title = "取消预约成功";
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.amount = $stateParams.amount =='' ?0:$stateParams.amount
            $scope.memberId = $stateParams.payType;
            $scope.relationType =  $stateParams.relationType;

            $scope.appointmentFirst = function(){
                window.location.href = "ap/firstPage/appoint";
            }

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/ap/appointBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    } else {
                        MyselfInfo.save({unBindUserPhoneNum:$rootScope.unBindUserPhoneNum+""},
                            function(data) {
                            $scope.bondSwitch = data.bondSwitch
                        });
                    }
                });
            });
   }])