angular.module('controllers', ['ionic']).controller('picSpreadResultCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {
        $scope.title ="图片传播";

        $scope.tryAgain = function(){
            $state.go("picSpreadIndex");
        };

        $scope.$on('$ionicView.enter', function() {


        });
    }]);

