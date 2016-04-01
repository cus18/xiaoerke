angular.module('controllers', ['ionic']).controller('userDealCtrl', [
        '$scope','$ionicPopup','$state','$stateParams','CheckBind','GetDoctorInfo','OutOfBind',
        function ($scope,$ionicPopup,$state,$stateParams,CheckBind,GetDoctorInfo,OutOfBind) {

            $scope.$on('$ionicView.enter', function(){
            });
    }])
