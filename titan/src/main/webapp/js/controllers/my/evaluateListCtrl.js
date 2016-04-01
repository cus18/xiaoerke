angular.module('controllers', ['ionic']).controller('EvaluateListCtrl', [
        '$scope','$stateParams','MyselfInfoAppointmentDetail','GetUserLoginStatus','$state','$location',
        function ($scope,$stateParams,MyselfInfoAppointmentDetail,GetUserLoginStatus,$state,$location) {
            $scope.title = "订单列表";
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.patientRegisterId = $stateParams.patient_register_service_id

            var weekList = ["星期一","星期二","星期三","星期四","星期五","星期六","星期日",];
            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/ap/appointBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    } else {
                        $scope.pageLoading = true;
                        MyselfInfoAppointmentDetail.save({patient_register_service_id:
                            $stateParams.patient_register_service_id},function(data){
                            $scope.pageLoading = false;
                            $scope.appointmentDataDetail = data;
                        })
                    }
                })
            });
    }])
