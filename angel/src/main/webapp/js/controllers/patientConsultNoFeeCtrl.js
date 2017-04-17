angular.module('controllers', ['ionic'])
    .controller('patientConsultNoFeeCtrl', ['$scope','$location','$http',
        function ($scope,$location,$http) {

            $scope.sure = function () {
                WeixinJSBridge.call('closeWindow');
            };
            //初始化
            $scope.patientConsultNoFeeInit = function(){


            };

        }]);
