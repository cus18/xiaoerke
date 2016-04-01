angular.module('controllers', ['ionic']).controller('feedbackCtrl', [
        '$scope','$state','$stateParams','$timeout','SendAdvice',
        function ($scope,$state,$stateParams,$timeout,SendAdvice) {
            $scope.title0 = "意见反馈"
            $scope.info = {}
            $scope.lock='false';

            $scope.appointmentFirst = function () {
                window.location.href="firstPage/appoint";
            }

           $scope.submitAdvice = function () {
               $scope.lock='true';
               SendAdvice.save({advice: $scope.info.feedback, contact: $scope.info.contact}, function (data) {
                   $timeout(function() {
                       $scope.lock='false';
                       history.back();
                }, 2000);
            })
        }
    }])