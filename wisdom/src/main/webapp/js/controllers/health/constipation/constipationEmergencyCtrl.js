angular.module('controllers', ['ionic']).controller('constipationEmergencyCtrl', [
    '$scope','$state','$stateParams','$http',
    function ($scope,$state,$stateParams,$http) {

        $scope.num = [11,22,33,44,55];

        var pData = {logContent:encodeURI("BMGL_12")};
        $http({method:'post',url:'util/recordLogs',params:pData});

        $scope.emergencySelect1=function(){
            $state.go('constipationMShortDeal',{type:"next"});
        }
        $scope.emergencySelect2=function(){
            $state.go('constipationWShortDeal',{type:"next"});
        }

    }]);

