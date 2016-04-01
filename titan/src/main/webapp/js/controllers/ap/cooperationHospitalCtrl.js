angular.module('controllers', ['ionic']).controller('CooperationHospitalCtrl', [
    '$scope','$state','$stateParams','GetCooperationHospitalInfo','$http',
        function ($scope,$state,$stateParams,GetCooperationHospitalInfo,$http) {

            $scope.haveOrder = false;
            $scope.pageLoading = false;
            $scope.hospitalMoreLock = false;
            $scope.hospitalMoreText = "查看详情";
            $scope.hospitalMoreImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_down.png";
            $scope.week = ['今天','明天','后天','3天后','4天后','5天后','6天后'];

            $scope.hospitalMore = function(){
                if(  $scope.hospitalMoreLock == false){
                    $scope.hospitalMoreLock = true;
                    $scope.hospitalMoreText = "收起";
                    $scope.hospitalMoreImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_up.png";
                }
                else{
                    $scope.hospitalMoreLock = false;
                    $scope.hospitalMoreText = "查看详情";
                    $scope.hospitalMoreImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_down.png";
                }
            }

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

            $scope.$on('$ionicView.enter',function() {
                $scope.hospitalId = $stateParams.hospitalId;
                GetCooperationHospitalInfo.get({hospitalId:$stateParams.hospitalId},function(data){
                    $scope.hospitalInfo = data.hospitalInfo;
                    $scope.doctorList = data.doctorDataVo;
                })

                var pData = {logContent:encodeURI("我的test")};
                $http({method:'post',url:'util/recordLogs',params:pData});
            })
    }])