﻿angular.module('controllers', ['ionic']).controller('umbrellaMemberListCtrl', [
        '$scope','$state','$stateParams','getFamilyList',
        function ($scope,$state,$stateParams,getFamilyList) {
            $scope.title="宝护伞";
            //$scope.id = $stateParams.id;

            getFamilyList.save({"id":$stateParams.id},function(data){
                console.log(data);
            })

            $scope.addMember=function(){
                $state.go("umbrellaMemberAdd",{id:$stateParams.id});
            }

            $scope.goActive=function(){
                $state.go("umbrellaFillInfo",{id:$scope.umbrellaId});
            };
            $scope.goShare=function(){
                $scope.shareLock=true;
            };

            $scope.$on('$ionicView.enter', function(){

            });

            
    }]);