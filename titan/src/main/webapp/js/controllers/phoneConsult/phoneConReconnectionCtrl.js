angular.module('controllers', ['ionic']).controller('phoneConReconnectionCtrl', [
        '$scope','$state',
        function ($scope,$state) {
            $scope.title = "重新连接";
            $scope.pageLoading =false;

            $scope.$on('$ionicView.enter', function(){


            });

            $scope.reconnection = function(){
                $state.go("phoneConConnectCall");
            }
    }]);
