angular.module('controllers', ['ionic']).controller('phoneConPaySuccessCtrl', [
        '$scope','$state','$stateParams','SendWechatMessageToUser','PhoneConsultRegisterInfo',
        function ($scope,$state,$stateParams,SendWechatMessageToUser,PhoneConsultRegisterInfo) {
            $scope.title = "预约成功";
            $scope.pageLoading =false;


            $scope.consultDoc = function(){
                SendWechatMessageToUser.save({},function(data){
                });
                WeixinJSBridge.call('closeWindow');
            }
            PhoneConsultRegisterInfo.get({phoneConsultServiceId:$stateParams.consultPhoneRegisterId},function(data){
                console.log(data)
                $scope.appointData = data;
            })
            $scope.$on('$ionicView.enter', function(){


            })
    }])
