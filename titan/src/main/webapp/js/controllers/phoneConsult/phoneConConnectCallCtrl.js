angular.module('controllers', ['ionic']).controller('phoneConConnectCallCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title = "连接通话";
            $scope.pageLoading =false;
            $scope.phone = $stateParams.phone
            $scope.doctorName = $stateParams.doctorName
            $scope.doctorId = $stateParams.doctorId
            $scope.$on('$ionicView.enter', function(){


            });


    }])
