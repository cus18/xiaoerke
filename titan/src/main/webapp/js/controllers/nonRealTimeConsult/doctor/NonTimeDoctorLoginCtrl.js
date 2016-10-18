angular.module('controllers', []).controller('NonTimeDoctorLoginCtrl', [
        '$scope','$state','$timeout','$http',
        function ($scope,$state,$timeout,$http) {

            $scope.prizeArray = {};

            //页面初始化
            $scope.NonTimeDoctorLoginInit = function(){
                document.title="医生登陆"; //title

            };

    }]);
