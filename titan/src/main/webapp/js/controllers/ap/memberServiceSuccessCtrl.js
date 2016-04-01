angular.module('controllers', ['ionic']).controller('memberServiceSuccessCtrl', [
        '$scope','$state','$stateParams','AppointmentStatus','MyselfInfoAppointmentDetail','$location',
        function ($scope,$state,$stateParams,AppointmentStatus,MyselfInfoAppointmentDetail,$location) {

            $scope.title = "会员服务成功"
            $scope.title0 = "宝大夫"
            $scope.haveOrder = false
            $scope.routeInfo = {}
            $scope.goBack = function(){
                history.back();
            }
            $scope.patient_register_service_id = $stateParams.patient_register_service_id;

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/appointBBBBBB" + $location.path();
                MyselfInfoAppointmentDetail.save({patient_register_service_id :
                    $stateParams.patient_register_service_id,
                    routePath:routePath
                }, function(data) {
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }
                    else{
                        $scope.pageLoading = false;
                        $scope.appointmentDataDetail = data;
                        var title = ((data.status == '1') ? "预约成功" : "预约失败");
                    }
                })
            })
    }])
