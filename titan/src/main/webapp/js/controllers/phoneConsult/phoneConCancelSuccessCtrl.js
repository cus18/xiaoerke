angular.module('controllers', ['ionic']).controller('phoneConCancelSuccessCtrl', [
        '$scope','$state',
        function ($scope,$state) {
            $scope.title = "取消预约成功";
            $scope.pageLoading =false;

            $scope.$on('$ionicView.enter', function(){

            });

            $scope.goFirst = function () {
                window.location.href = "ap/firstPage/phoneConsult";
            }

    }])
