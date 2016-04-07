﻿angular.module('controllers', ['ionic']).controller('phoneConsultOrderCtrl', [
        '$scope','$state','MyselfInfoPhoneConsult',
        function ($scope,$state,MyselfInfoPhoneConsult) {
            $scope.title = "电话咨询订单";
            $scope.pageLoading =false;
            $scope.classifyItem =0;
            $scope.classifyItemList =["全部","待支付","待接听","待评价","待分享","已完成","已取消"];
            $scope.orderInfo=[];

            $scope.MyselfInfoPhoneConsult = function(pageNo,pageSize,status){
                $scope.pageLoading = true;
                MyselfInfoPhoneConsult.save({"pageNo": pageNo, "pageSize": pageSize, "status": status}, function (data) {
                    $scope.pageLoading = false;
                    $scope.orderInfo = data.orderList;

                });
            }

           /* 选择订单分类*/
            $scope.selectClassify = function(item){
                $scope.classifyItem =item;

                $scope.status = item -1;
                $scope.pageLoading = true;
                if($scope.status < 0){
                    $scope.MyselfInfoPhoneConsult("1","1000");
                }else{
                    $scope.MyselfInfoPhoneConsult("1","1000",$scope.status+"");
                }

            };
            $scope.orderDerail = function(item){
                $state.go("orderDetail",{doctorId:item.doctorId,orderId:item.orderId,type:"phone"})
            };


            $scope.$on('$ionicView.enter', function(){
                $scope.MyselfInfoPhoneConsult("1","1000");

            })
    }])
