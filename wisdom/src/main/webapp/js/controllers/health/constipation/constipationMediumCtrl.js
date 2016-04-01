angular.module('controllers', ['ionic']).controller('constipationMediumCtrl', [
    '$scope','$state','$stateParams','$http',
    function ($scope,$state,$stateParams,$http) {

        $scope.num = [];


        var pData = {logContent:encodeURI("BMGL_43")};
        $http({method:'post',url:'util/recordLogs',params:pData});

    }]);

