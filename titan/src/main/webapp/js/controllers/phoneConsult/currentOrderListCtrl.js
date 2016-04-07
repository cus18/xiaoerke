angular.module('controllers', ['ionic']).controller('currentOrderListCtrl', [
        '$scope','$state','$stateParams','getOrderListAll','MyselfInfoAppointment','MyselfInfoPhoneConsult',
        function ($scope,$state,$stateParams,getOrderListAll,MyselfInfoAppointment,MyselfInfoPhoneConsult) {
            $scope.title = "当前订单";
            $scope.pageLoading =false;
            $scope.classifyItem ="all";

            $scope.userId=$stateParams.userId;
            $scope.orderInfo=[];

            //默认获取全部订单列表
            $scope.getOrderListAll = function(pageNo,pageSize){
                $scope.pageLoading = true;
                getOrderListAll.save({"pageNo": pageNo, "pageSize": pageSize},function(data){
                    $scope.pageLoading = false;
                    $scope.orderInfo=data.orderList;
                })
            }

           /* 选择订单分类*/
            $scope.selectClassify = function(item){
                $scope.classifyItem =item;
                if($scope.classifyItem == "ap"){
                    MyselfInfoAppointment.save({"pageNo": "1", "pageSize": "10"}, function (data) {
                        $scope.pageLoading = false;
                        $scope.orderInfo = data.orderList;

                    });
                }else if($scope.classifyItem == "phone"){
                    MyselfInfoPhoneConsult.save({"pageNo": "1", "pageSize": "10"}, function (data) {
                        $scope.pageLoading = false;
                        $scope.orderInfo = data.orderList;
                    });
                }else{
                    $scope.getOrderListAll("1","10");
                }

            };

            $scope.orderDerail = function(item){
                $state.go("orderDetail",{doctorId:item.doctorId,orderId:item.orderId,type:item.classify})
            };

            $scope.$on('$ionicView.enter', function(){
                $scope.getOrderListAll("1","10");

            })
    }])
