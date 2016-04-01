angular.module('controllers', ['ionic']).controller('guideCtrl', [
        '$scope','$state','SendWechatMessageToUser',
        function ($scope,$state,SendWechatMessageToUser) {
            $scope.title = "路线"
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.consultDoc = function(){
                SendWechatMessageToUser.save({},function(data){
                });
                WeixinJSBridge.call('closeWindow');
            }
    }])
