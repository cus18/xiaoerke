angular.module('controllers', ['ionic']).controller('olympicBabyMyPrizeCtrl', [
        '$scope','$state','$timeout',
        function ($scope,$state,$timeout) {
            $scope.title = "奥运宝贝-我的奖品";


            $scope.$on('$ionicView.enter', function(){

            })
    }])
