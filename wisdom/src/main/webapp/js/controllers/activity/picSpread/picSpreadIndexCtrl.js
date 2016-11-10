angular.module('controllers', ['ionic']).controller('picSpreadIndexCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.title ="图片传播";

        $scope.transformDate = function(){

        };

        $scope.$on('$ionicView.enter', function() {


        });
    }]);

