﻿angular.module('controllers', ['ionic']).controller('withDrawlsConfirmCtrl', [
        '$scope','$state','$stateParams','CheckBind','GetDoctorInfo','$location','GetUserLoginStatus',
        function ($scope,$state,$stateParams,CheckBind,GetDoctorInfo,$location,GetUserLoginStatus) {

            $scope.withDrawlsComplete = function(){
                $state.go("myselfFirst");
            }

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/ap/doctorBBBBBB" + $location.path();
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
                                $scope.arriveDate = $stateParams.arriveDate;
                                $scope.money = $stateParams.money;
                            }
                        });
                    }
                })
            });
    }])
