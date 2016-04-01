angular.module('controllers', ['ionic']).controller('memberDealCtrl', [
        '$scope',
        function ($scope) {

            $scope.title = "会员服务";
            $scope.title0 = "宝大夫（400-623-7120）";

            $scope.$on('$ionicView.enter', function(){


            });
    }])
