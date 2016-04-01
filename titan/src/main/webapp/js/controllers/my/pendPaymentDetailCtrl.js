angular.module('controllers', ['ionic']).controller('PendPaymentDetailCtrl', [
        '$scope','$stateParams','MyselfInfoAppointmentDetail','GetUserLoginStatus','$location',
        function ($scope,$stateParams,MyselfInfoAppointmentDetail,GetUserLoginStatus,$location) {
            $scope.title = "预约详情"
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.workTime = "出诊时间："
            $scope.workLocation = "出诊地点："
            $scope.appointmentNumber = "订单号："
            $scope.department = "科室："
            $scope.appointmentSuccess = "预约成功："
            $scope.appointmentStatus = "待支付"
            $scope.tips = "温馨提示："
            $scope.tips1= "1.支付后请按时就诊。"
            $scope.tips2 = "2. 如您在下单后30分钟内没有支付此订单，则此订单将自动取消。"
            $scope.status1 = "取消预约"
            $scope.status2 = "立即评价"
            $scope.status3 = "立即分享"
            $scope.status4 = "立即建档"
            $scope.status0 = "立即支付"

            $scope.$on('$ionicView.enter', function() {
                $scope.pageLoading = true;
                var routePath = "/appointBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath: routePath}, function (data) {
                    $scope.pageLoading = false;
                    if (data.status == "9") {
                        window.location.href = data.redirectURL;
                    }
                    else {
                        $scope.pageLoading = true;
                        MyselfInfoAppointmentDetail.save({patient_register_service_id: $stateParams.patient_register_service_id}, function (data) {
                            $scope.pageLoading = false;
                            $scope.appointmentDataDetail = data;
                        })
                    }
                })
            })
    }])
