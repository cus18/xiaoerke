angular.module('controllers', ['ionic']).controller('umbrellaMemberListCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="宝护伞";


            $scope.goActive=function(){
                $state.go("umbrellaFillInfo",{id:$scope.umbrellaId});
            };
            $scope.goShare=function(){
                $scope.shareLock=true;
            };

            $scope.$on('$ionicView.enter', function(){

            });

            
    }]);