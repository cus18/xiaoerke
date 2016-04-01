angular.module('controllers', ['ionic']).controller('cooperationHospitalPriceCtrl', [
    '$scope','$state','$stateParams','GetCooperationHospitalInfo',
        function ($scope,$state,$stateParams,GetCooperationHospitalInfo) {
            $scope.haveOrder = false;
            $scope.$on('$ionicView.enter',function() {

            })
    }])