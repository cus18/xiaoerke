angular.module('controllers', ['ionic']).controller('lovePlanPaySuccessCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {
        $scope.title ="宝妈爱心接力";
        $scope.goUmbrella =function(){
            window.location.href="http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000000/a"
        };

        $scope.$on('$ionicView.enter', function(){

        })
    }]);

