angular.module('controllers', []).controller('NonTimeUserConversationCtrl', [
        '$scope','$state','$timeout','$http',
        function ($scope,$state,$timeout,$http) {

            $scope.prizeArray = {};

            //ҳ���ʼ��
            $scope.NonTimeUserConversationInit = function(){
                document.title="ҽ���Ự"; //�޸�ҳ��title

            };

    }]);
