angular.module('controllers', ['ionic']).controller('lovePlanPosterCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.title ="宝妈爱心接力";

        $scope.$on('$ionicView.enter', function(){

        })
    }]);

