angular.module('controllers', ['ionic']).controller('feedbackCtrl', [
        '$scope','$state','$stateParams','$timeout','saveFeedBack',
        function ($scope,$state,$stateParams,$timeout,saveFeedBack) {
            $scope.title = "意见反馈";
            $scope.info = {
                produce:true,
                error:false
            };
            $scope.lock='false';

            $scope.checked = function (flag) {
                if(flag == 'produce'){
                    $scope.info.produce = true;
                    $scope.info.error = false;
                }else{
                    $scope.info.produce = false;
                    $scope.info.error = true;
                }
            };
            $scope.appointmentFirst = function () {
                window.location.href="firstPage/appoint";
            };

           $scope.submitAdvice = function () {
               $scope.lock='true';
               saveFeedBack.save({advice: $scope.info.feedback, contact: $scope.info.contact}, function (data) {
                       $scope.lock='false';
                       wx.closeWindow();

            })
        };
    }]);