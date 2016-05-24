angular.module('controllers', ['ionic']).controller('umbrellaJoinCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="宝护伞";
            $scope.shareLock=false;

            $scope.goDetail=function(){
                window.location.href = "/wisdom/firstPage/umbrella";
            };
            $scope.goActive=function(){
                $state.go("umbrellaFillInfo");
            };
            $scope.goShare=function(){
                $scope.shareLock=true;
            };
            $scope.cancelShare=function(){
                $scope.shareLock=false;
            };
            $scope.$on('$ionicView.enter', function(){

            });

    }]);