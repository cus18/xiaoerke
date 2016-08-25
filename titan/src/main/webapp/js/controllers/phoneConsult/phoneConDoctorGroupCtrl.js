angular.module('controllers', ['ionic']).controller('phoneConDoctorGroupCtrl', [
        '$scope','$state',
        function ($scope,$state) {
            $scope.title = "路线";
            $scope.pageLoading =false;
            $scope.consultDoc = function(){

            };
            $scope.$on('$ionicView.enter', function(){

            })
    }])
