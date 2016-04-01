angular.module('controllers', ['ionic']).controller('AppointmentSuccessCtrl', [
        '$scope','$state','$stateParams','MyselfInfoAppointmentDetail','$location',
        function ($scope,$state, $stateParams,MyselfInfoAppointmentDetail,$location) {
            $scope.title = "预约成功";
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.weekList = ['星期一','星期二','星期三','星期四','星期五','星期六','星期日']
            $scope.cancelAppointment = "确认取消"
            $scope.route = "路线"

            $scope.toBeTreatedDetail = function(){
                $state.go("toBeTreatedDetail",{patient_register_service_id:
                    $stateParams.patient_register_service_id});
            }

            $scope.appointmentCancel = function(){
                $state.go("appointmentCancel",{patient_register_service_id:
                    $stateParams.patient_register_service_id});
            }

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
                        $scope.appointmentDataDetail = data;
                        var i =  moment(data.date).format('d');
                        $scope.week =$scope.weekList[i-1]
                        var title = ((data.status == '1') ? "预约成功" : "预约失败");
                    }
                })
            })
    }])
