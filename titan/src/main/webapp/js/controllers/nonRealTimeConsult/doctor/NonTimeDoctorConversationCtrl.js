angular.module('controllers', []).controller('NonTimeDoctorConversationCtrl', [
        '$scope','$state','$timeout','$http',
        function ($scope,$state,$timeout,$http) {

            $scope.prizeArray = {};

            //ҳ���ʼ��
            $scope.NonTimeDoctorConversationInit = function(){
                document.title="ҽ���Ự"; //�޸�ҳ��title

            };

    }]);
