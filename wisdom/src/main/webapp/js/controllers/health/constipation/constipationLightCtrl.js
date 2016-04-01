angular.module('controllers', ['ionic']).controller('constipationLightCtrl', [
    '$scope','$state','$stateParams','$http',
    function ($scope,$state,$stateParams,$http) {

        $scope.num = [11,22,33,44,55];

        var pData = {logContent:encodeURI("BMGL_42")};
        $http({method:'post',url:'util/recordLogs',params:pData});

    }]);

