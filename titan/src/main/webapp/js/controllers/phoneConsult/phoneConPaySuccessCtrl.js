angular.module('controllers', ['ionic']).controller('phoneConPaySuccessCtrl', [
        '$scope','$state','$stateParams','SendWechatMessageToUser','PhoneConsultRegisterInfo','RecordLogs',
        function ($scope,$state,$stateParams,SendWechatMessageToUser,PhoneConsultRegisterInfo,RecordLogs) {
            $scope.title = "预约成功";
            $scope.pageLoading =false;


            $scope.consultDoc = function(){
                RecordLogs.get({logContent:encodeURI("DHZX_YYCG_MFZX")},function(){})
                SendWechatMessageToUser.save({},function(data){
                });
                WeixinJSBridge.call('closeWindow');
            }
            PhoneConsultRegisterInfo.get({phoneConsultaServiceId:$stateParams.consultPhoneRegisterId},function(data){
                console.log(data)
                $scope.appointData = data;
            })
            $scope.$on('$ionicView.enter', function(){


            })
    }])
