﻿angular.module('controllers', ['ionic']).controller('currentOrderListCtrl', [
        '$scope','$state','$stateParams','getOrderList',
        function ($scope,$state,$stateParams,getOrderList) {
            $scope.title = "当前订单";
            $scope.pageLoading =false;
            $scope.classifyItem ="all";

            $scope.userId=$stateParams.userId;
            $scope.orderInfo=[];

            //默认获取全部订单列表
            $scope.pageLoading = true;
            getOrderList.save({"pageNo": "1", "pageSize": "10", type: $scope.classifyItem},function(data){
                $scope.pageLoading = false;
                $scope.orderInfo=data.orderList;
            })

           /* 选择订单分类*/
            $scope.selectClassify = function(item){
                $scope.classifyItem =item;
                getOrderList.save({"pageNo": "1", "pageSize": "10", type:$scope.classifyItem},function(data){
                    $scope.pageLoading = false;
                    $scope.orderInfo=data.orderList;
                })
            };

            $scope.orderDerail = function(item){
                $state.go("orderDetail",{doctorId:item.doctorId,orderId:item.orderId,type:item.classify})
            };

            $scope.$on('$ionicView.enter', function(){


            })
    }])
