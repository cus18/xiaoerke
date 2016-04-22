angular.module('controllers', ['ionic']).controller('myselfFirstCtrl', [
        '$scope','$state','$ionicPopup','CheckBind','GetDoctorInfo','$location','GetUserLoginStatus',
        function ($scope,$state,$ionicPopup,CheckBind,GetDoctorInfo,$location,GetUserLoginStatus) {
            $scope.myAccount = function(){
                $state.go("myAccount");
            }
            $scope.myCard = function(){
                $state.go("myCard");
            }
            $scope.setting = function(){
                $state.go("setting");
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
                        $scope.pageLoading = true;
                        GetDoctorInfo.save({},function(data){
                            $scope.pageLoading = false;
                            if(data.doctorId!=""){
                                $scope.doctorId = data.doctorId;
                                $scope.doctorName = data.doctorName;
                                $scope.phone = data.phone;
                            }
                        });
                    }
                })
            });

            $scope.gohome = function () {
                $state.go("doctorHome",{id:$scope.doctorId});
            }
    }])
