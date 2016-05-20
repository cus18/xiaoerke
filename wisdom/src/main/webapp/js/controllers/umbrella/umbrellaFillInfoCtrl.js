angular.module('controllers', ['ionic']).controller('umbrellaFillInfoCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="填写信息";

            $scope.fn=function(){

            };
            $scope.$on('$ionicView.enter', function(){

            });

    }]);