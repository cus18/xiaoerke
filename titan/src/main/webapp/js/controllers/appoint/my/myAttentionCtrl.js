angular.module('controllers', ['ionic']).controller('MyAttentionCtrl', [
        '$scope','$state','MyAttention','resolveUserLoginStatus','$location',
        function ($scope,$state,MyAttention,resolveUserLoginStatus,$location) {

            $scope.title = "我的关注";
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.week = ['今天','明天','后天','3天后','4天后','5天后','6天后'];
            $scope.available_time = ['今日可约','明日可约','后天可约','3天后可约','4天后可约','5天后可约','6天后可约'];
            $scope.isBlank = false

            $scope.pageLoading = true;
            MyAttention.save({pageNo:'1',pageSize:'100',orderBy:'0'},function(data){
                $scope.pageLoading = false;
                $scope.departmentData = data.doctorDataVo;
                if(data.doctorDataVo.length==0){
                    $scope.care1 = "还没有关注医生哦，赶快为宝宝选择专家吧！"
                    $scope.isBlank = true
                }
            })

            $scope.doctorAppointment = function(doctorId,available_time,hospitalName,location,position,location_id){
                if(available_time>=0&&available_time<7)
                {
                    $state.go("doctorAppointment", {
                        doctorId: doctorId, available_time: available_time,
                        hospitalName: hospitalName, location: location, position: position,
                        mark:"dateAvailable",location_id:location_id,attentionDoctorId:""
                    });
                }
                else
                {
                    $state.go("doctorAppointment", {
                        doctorId: doctorId, available_time: available_time,
                        hospitalName: hospitalName, location: location, position: position,
                        mark:"dateNoAvailable",location_id:location_id,attentionDoctorId:""
                    });
                }
            }

            $scope.$on('$ionicView.enter', function(){
                resolveUserLoginStatus.events($location.path(),"","","","notGo");
            });
   }])
