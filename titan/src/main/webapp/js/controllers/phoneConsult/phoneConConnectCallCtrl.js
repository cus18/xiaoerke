angular.module('controllers', ['ionic']).controller('phoneConConnectCallCtrl', [
        '$scope','$state',
        function ($scope,$state) {
            $scope.title = "连接通话";
            $scope.pageLoading =false;

            $scope.$on('$ionicView.enter', function(){


            });


    }])
