angular.module('controllers', ['ionic']).controller('umbrellaIndexCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="宝护伞";

            $scope.fn=function(){

            };
            $scope.$on('$ionicView.enter', function(){

            });

    }]);