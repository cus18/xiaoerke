angular.module('controllers', ['ionic']).controller('constipationWeightCtrl', [
    '$scope','$state','$stateParams','$http',
    function ($scope,$state,$stateParams,$http) {

        $scope.num = [];

        var pData = {logContent:encodeURI("BMGL_46")};
        $http({method:'post',url:'util/recordLogs',params:pData});

    }]);

