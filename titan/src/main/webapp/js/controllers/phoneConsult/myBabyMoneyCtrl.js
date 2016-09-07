angular.module('controllers', ['ionic']).controller('myBabyMoneyCtrl', [
    '$scope','$state','$stateParams','$http','$ionicPopup','$location','BabyCoinInit',
    function ($scope,$state,$stateParams,$http,$ionicPopup,$location,BabyCoinInit) {
        $scope.bobyMoneyInit = function(){
            BabyCoinInit.save({},function(data){
                $scope.babyMoney = data.babyCoinVo.cash;
                $scope.babyCoinRecordVos = data.babyCoinRecordVos;
            })
        }
    }])
