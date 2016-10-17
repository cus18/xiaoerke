angular.module('controllers', []).controller('NonTimeDoctorConversationCtrl', [
        '$scope','$state','$timeout','$http',
        function ($scope,$state,$timeout,$http) {

            $scope.prizeArray = {};

            //页面初始化
            $scope.NonTimeDoctorConversationInit = function(){
                document.title="医生会话"; //修改页面title

            };

    }]);
