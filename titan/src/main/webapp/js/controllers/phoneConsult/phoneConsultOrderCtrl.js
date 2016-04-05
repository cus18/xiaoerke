angular.module('controllers', ['ionic']).controller('phoneConsultOrderCtrl', [
        '$scope','$state','PhoneConsultOrderList',
        function ($scope,$state,PhoneConsultOrderList) {
            $scope.title = "电话咨询订单";
            $scope.pageLoading =false;
            $scope.classifyItem =0;
            $scope.classifyItemList =["全部","待支付","待接听","待评价","待分享","已完成","已取消"];
            $scope.orderInfo=[];

           /* 选择订单分类*/
            $scope.selectClassify = function(item){
                $scope.classifyItem =item;

                $scope.status = item -1;
                $scope.pageLoading = true;
                if($scope.status < 0){
                    PhoneConsultOrderList.save({"pageNo": "1", "pageSize": "10"}, function (data) {
                        $scope.pageLoading = false;
                        $scope.orderInfo = data.appointmentData;

                    });
                }else{
                    PhoneConsultOrderList.save({"pageNo": "1", "pageSize": "10", "status": $scope.status+""}, function (data) {
                        $scope.pageLoading = false;
                        $scope.orderInfo = data.appointmentData;
                    });
                }


            };
            $scope.orderDerail = function(item){
                $state.go("orderDetail",{doctorId:item.doctorId,orderId:item.orderId,type:"phone"})
            };


            $scope.$on('$ionicView.enter', function(){
                PhoneConsultOrderList.save({"pageNo": "1", "pageSize": "10"}, function (data) {
                    $scope.pageLoading = false;
                    $scope.orderInfo = data.appointmentData;

                });

            })
    }])
