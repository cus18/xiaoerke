angular.module('controllers', ['ionic']).controller('umbrellaJoinCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="领取成功";

            $scope.fn=function(){

            };
            $scope.$on('$ionicView.enter', function(){

            });

    }]);