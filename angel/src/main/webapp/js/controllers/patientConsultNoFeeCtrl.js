angular.module('controllers', ['ionic'])
    .controller('patientConsultNoFeeCtrl', ['$scope','$location','$http','ConfirmInstantConsultation',
        function ($scope,$location,$http,ConfirmInstantConsultation) {

            $scope.sure = function () {
                ConfirmInstantConsultation.save({},function (data) {
                    
                    
                    WeixinJSBridge.call('closeWindow');
                    
                    
                });
            };
            //初始化
            $scope.patientConsultNoFeeInit = function(){


            };

        }]);
