﻿angular.module('controllers', ['ionic']).controller('orderDetailCtrl', [
        '$scope','$state','$stateParams',"DoctorDetail",'PhoneConsultRegisterInfo','AppointRegisterInfo',
        function ($scope,$state,$stateParams,DoctorDetail,PhoneConsultRegisterInfo,AppointRegisterInfo) {
            $scope.title = "订单详情";
            $scope.pageLoading =false;

            $scope.doctorId = $stateParams.doctorId;
            $scope.orderId = $stateParams.orderId;
            $scope.type = $stateParams.type;

            $scope.consultDoc = function(){

            };

            $scope.toShare = function(){
                window.location.href="http://s68.baodf.com/titan/appoint#/sharedDetail/"+$scope.orderId+",false,,"+"phone";
            }

            $scope.$on('$ionicView.enter', function(){

                $scope.pageLoading = true;
                //订单详情
                if($scope.type == "phone"){
                    PhoneConsultRegisterInfo.get({phoneConsultaServiceId:parseInt($scope.orderId)},function(data){
                        $scope.pageLoading = false;
                        $scope.orderDetail = data;
                    });
                }
                if($scope.type == "ap"){
                    AppointRegisterInfo.save({patient_register_service_id:
                        $scope.orderId},function(data){
                        $scope.pageLoading = false;
                        $scope.orderDetail = data;
                    });
                }

            })
    }])
