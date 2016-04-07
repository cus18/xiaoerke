angular.module('controllers', ['ionic']).controller('appointOrderCtrl', [
        '$scope','$state','MyselfInfoAppointment',
        function ($scope,$state,MyselfInfoAppointment) {
            $scope.title = "预约挂号订单";
            $scope.pageLoading =false;
            $scope.classifyItem =0;
            $scope.classifyItemList =["全部","待支付","待就诊","待评价","待分享","已完成","已取消"];

            $scope.orderInfo=[];

            $scope.MyselfInfoAppointment = function(pageNo,pageSize,status){
                $scope.pageLoading = true;
                MyselfInfoAppointment.save({"pageNo": pageNo, "pageSize": pageSize, "status": status}, function (data) {
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
                    $scope.MyselfInfoAppointment("1","10");
                }else{
                    $scope.MyselfInfoAppointment("1","10",$scope.status+"");
                }

            };

            $scope.orderDerail = function(item){
                $state.go("orderDetail",{doctorId:item.doctorId,orderId:item.orderId,type:"ap"})
            };


            $scope.$on('$ionicView.enter', function(){
                $scope.MyselfInfoAppointment("1","10");

            })
    }])
