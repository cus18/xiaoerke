angular.module('controllers', ['ionic']).controller('doctorIdentifyCtrl', [
    '$scope','$stateParams',
    function ($scope,$stateParams) {
        $scope.tile0="信息录入";
        $scope.lock=false;
    }])
