angular.module('controllers', ['ionic']).controller('phoneConReconnectionCtrl', [
        '$scope','$state','$stateParams','PhoneConsultRegisterInfo','RecordLogs',
        function ($scope,$state,$stateParams,PhoneConsultRegisterInfo,RecordLogs) {
            $scope.title = "重新连接";
            $scope.pageLoading =false;

            RecordLogs.get({logContent:encodeURI("DHZX_CXLJ")},function(){})
            PhoneConsultRegisterInfo.get({phoneConsultaServiceId:$stateParams.consultPhoneServiceId},function(data){
                $scope.orderInfo = data
                $scope.position = (data.position1 == ""?"":data.position1+",") + data.position2
                $scope.surplusTime =moment(data.surplusTime).format("mm:ss")
            });
            //
            //$scope.$on('$ionicView.enter', function(){
            //
            //
            //});

            $scope.reconnection = function(){
                RecordLogs.get({logContent:encodeURI("DHZX_CXLJ_CXLJTH")},function(){})
                $state.go("phoneConConnectCall",{phoneConsultaServiceId:$stateParams.consultPhoneServiceId,doctorName: $scope.orderInfo.doctorName,phone: $scope.orderInfo.phone,doctorId:$scope.orderInfo.doctorId})
            }
    }]);
