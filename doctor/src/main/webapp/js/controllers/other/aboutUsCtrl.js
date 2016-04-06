angular.module('controllers', ['ionic']).controller('aboutUsCtrl', [
        '$scope','$ionicPopup','$state','$stateParams','CheckBind','GetDoctorInfo','OutOfBind','$location','GetUserLoginStatus',
        function ($scope,$ionicPopup,$state,$stateParams,CheckBind,GetDoctorInfo,OutOfBind,$location,GetUserLoginStatus) {
            $scope.userDeal = function(){
                $state.go("userDeal");
            }

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/doctorBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    }else{
                        $scope.scrollLoading = "加载更多";
                        $scope.pageLoading = true;
                        GetDoctorInfo.save({},function(data){
                            $scope.pageLoading = false;
                            if(data.doctorId!=""){
                                $scope.doctorId = data.doctorId;
                                $scope.choice = "everyDayList";
                            }
                        });
                    }
                })
            });
    }])
