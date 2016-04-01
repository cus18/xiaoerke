angular.module('controllers', ['ionic']).controller('ToBeTreatedDetailCtrl', [
        '$scope','$state','$stateParams','MyselfInfoAppointmentDetail','GetUserLoginStatus','$location',
        function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,GetUserLoginStatus,$location) {

            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.title = "预约详情"
            $scope.registerId = $stateParams.patient_register_service_id
            $scope.status= $stateParams.status;
            $scope.appointmentFirst = function(){
                window.location.href = "/xiaoerke-appoint/ap";
            }
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
                        //根据用户的业务订购ID，获取医生信息，和预约详情信息
                        $scope.pageLoading = true;
                        MyselfInfoAppointmentDetail.save({patient_register_service_id:
                            $stateParams.patient_register_service_id},function(data){
                            $scope.pageLoading = false;
                            $scope.appointmentDataDetail = data;
                        })
                    }
                })
            })
        }])
