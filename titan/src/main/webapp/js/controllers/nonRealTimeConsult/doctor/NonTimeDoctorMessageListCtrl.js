angular.module('controllers', []).controller('NonTimeDoctorMessageListCtrl', [
        '$scope','$state','$timeout','$http',
        function ($scope,$state,$timeout,$http) {

            $scope.prizeArray = {};

            //ҳ���ʼ��
            $scope.NonTimeDoctorMessageListInit = function(){
                document.title="��Ϣ�б�"; //�޸�ҳ��title

            };

    }]);
