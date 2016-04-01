angular.module('controllers', ['ionic']).controller('constipationWShortDealCtrl', [
    '$scope','$state','$stateParams','SendWechatMessageToUser','$http',
    function ($scope,$state,$stateParams,SendWechatMessageToUser,$http) {

        $scope.num = [];
        var pData = {logContent:encodeURI("BMGL_47")};
        $http({method:'post',url:'util/recordLogs',params:pData});
        //咨询医生
        $scope.consultDoc = function(){
            var pData = {logContent:encodeURI("BMGL_9")};
            $http({method:'post',url:'util/recordLogs',params:pData});
            SendWechatMessageToUser.save({},function(data){

            });
            WeixinJSBridge.call('closeWindow');
        }

        $scope.remind = function(){
            $state.go('constipationRemind',{flag:$stateParams.type});
        }
    }]);

