angular.module('controllers', []).controller('NonTimeDoctorMessageListCtrl', [
        '$scope','$state','$timeout','$http',
        function ($scope,$state,$timeout,$http) {

            $scope.prizeArray = {};

            //页面初始化
            $scope.NonTimeDoctorMessageListInit = function(){
                document.title="消息列表"; //修改页面title

            };

    }]);
