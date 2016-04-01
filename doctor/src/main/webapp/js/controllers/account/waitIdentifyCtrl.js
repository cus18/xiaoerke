angular.module('controllers', ['ionic']).controller('waitIdentifyCtrl', [
    '$scope',
    function ($scope) {
        $scope.lock=false;
        $scope.tile0="等待认证";
    }])
