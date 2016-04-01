angular.module('controllers', ['ionic']).controller('constipationMShortDealCtrl', [
    '$scope','$state','$stateParams','SendWechatMessageToUser','$http',
    function ($scope,$state,$stateParams,SendWechatMessageToUser,$http) {

        $scope.num = [];
        $scope.useImg = [
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2FmShortDeal1.jpg",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2FmShortDeal2.jpg",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2FmShortDeal3.jpg",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2FmShortDeal4.jpg",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2FmShortDeal5.jpg"
        ];
        var pData = {logContent:encodeURI("BMGL_44")};
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

