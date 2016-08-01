angular.module('controllers', ['ionic']).controller('olympicBabyInvitationCardCtrl', [
    '$scope','$state','$timeout',
    function ($scope,$state,$timeout) {
        $scope.title = "奥运宝贝-首页";


        $scope.$on('$ionicView.enter', function(){

        });
    }]);
