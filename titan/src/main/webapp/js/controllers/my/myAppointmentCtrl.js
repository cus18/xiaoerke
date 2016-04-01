angular.module('controllers', ['ionic']).controller('MyAppointmentCtrl', [
        '$scope','$state','MyselfInfoAppointment','GetUserLoginStatus','$location',
        function ($scope,$state,MyselfInfoAppointment,GetUserLoginStatus,$location) {

            $scope.title = "我的预约";
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.statusPendPay = "待支付";
            $scope.statusToBeTreat = "待就诊";
            $scope.statusToBeEvaluate = "待评价";
            $scope.statusToBeShare = "待分享";
            $scope.statusIsFinished = "已完成";
            $scope.statusIsCancel = "已取消";

            $scope.$on('$ionicView.enter', function () {
                $scope.pageLoading = true;
                var routePath = "/appointBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath: routePath}, function (data) {
                    $scope.pageLoading = false;
                    if (data.status == "9") {
                        window.location.href = data.redirectURL;
                    }
                    else {
                        $scope.pageLoading = true;
                        MyselfInfoAppointment.save({"pageNo": "1", "pageSize": "1000"}, function (data) {
                            $scope.pageLoading = false;
                            $scope.appointmentData = data.appointmentData;
                            if (data.appointmentData.length == 0) {
                                $scope.care1 = "还没有预约订单哦，医生在等待与宝宝见面哦！"
                                $scope.isBlank = true
                            }
                        })
                    }
                })
            })
        }])