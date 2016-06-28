angular.module('controllers', ['ionic']).controller('lovePlanPaySuccessCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.title ="宝妈爱心接力";
        $scope.goUmbrella =function(){

        };

        $scope.$on('$ionicView.enter', function(){

        })
    }]);

