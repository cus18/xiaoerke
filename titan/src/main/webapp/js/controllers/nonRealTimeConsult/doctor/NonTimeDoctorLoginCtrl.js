angular.module('controllers', []).controller('NonTimeDoctorLoginCtrl', [
        '$scope','$state','$timeout','$http',
        function ($scope,$state,$timeout,$http) {

            $scope.prizeArray = {};

            //ҳ���ʼ��
            $scope.NonTimeDoctorLoginInit = function(){
                document.title="ҽ����¼"; //�޸�ҳ��title

            };

    }]);
