angular.module('controllers', ['ionic']).controller('constipationFollowForgetCtrl', [
    '$scope','$state','$stateParams','SendWechatMessageToUser','UpdatePlanInfoForCycleTip','$http',
    function ($scope,$state,$stateParams,SendWechatMessageToUser,UpdatePlanInfoForCycleTips,$http) {

        $scope.forgetLock=false;
        $scope.forgetImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fforget11.png";

        var pData = {logContent:encodeURI("BMGL_40")};
        $http({method:'post',url:'util/recordLogs',params:pData});

        $scope.consultDoc = function(){
            $scope.change();
            SendWechatMessageToUser.save({},function(data){

            });
            WeixinJSBridge.call('closeWindow');
        }
        $scope.doTask=function(){
            $scope.change();
            $state.go("constipationIndex");
        }
        $scope.$on('$ionicView.enter', function() {
            $scope.show = $stateParams.show;
            if( $scope.show=="noDo"){
                $scope.forgetImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fforget11.png";

            }
          else{
                $scope.forgetImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fforget21.png";

            }

        });

        $scope.change = function(){
            UpdatePlanInfoForCycleTips.get({"planInfoId":parseInt($stateParams.planInfoId)},function(data){

            });
        }

    }]);

