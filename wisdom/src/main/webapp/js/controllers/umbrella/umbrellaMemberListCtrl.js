angular.module('controllers', ['ionic']).controller('umbrellaMemberListCtrl', [
        '$scope','$state','$stateParams','getFamilyList',
        function ($scope,$state,$stateParams,getFamilyList) {
            $scope.title="宝护伞";

            getFamilyList.save({"id":"23"},function(data){
                console.log(data);
            })
            $scope.goActive=function(){
                $state.go("umbrellaFillInfo",{id:$scope.umbrellaId});
            };
            $scope.goShare=function(){
                $scope.shareLock=true;
            };

            $scope.$on('$ionicView.enter', function(){

            });

            
    }]);