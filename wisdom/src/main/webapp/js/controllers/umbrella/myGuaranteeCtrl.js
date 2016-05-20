angular.module('controllers', ['ionic']).controller('myGuaranteeCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="我的保障";

            $scope.fn=function(){

            };
            $scope.$on('$ionicView.enter', function(){

            });

    }]);