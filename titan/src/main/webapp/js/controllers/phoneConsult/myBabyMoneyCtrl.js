angular.module('controllers', ['ionic']).controller('myBabyMoney', [
    '$scope','$state','$stateParams','$http','$ionicPopup',
    '$location',
    function ($scope,$state,$stateParams,$http,$ionicPopup,
              $location) {
        $scope.title = "我的资料";
        $scope.title0 = "宝大夫（400-623-7120）";
    }])
