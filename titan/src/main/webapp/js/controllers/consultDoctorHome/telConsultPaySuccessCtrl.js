angular.module('controllers', ['ionic']).controller('telConsultPaySuccessCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.evaluationList = [];

        $scope.goNonTimeConsult=function(){
            window.location.href="nonRealTimeConsult#/NonTimeUserFirstConsult/"+$stateParams.id+","+$scope.nonRealPayPrice;
        };



        $scope.$on('$ionicView.beforeEnter',function() {

        })

    }]);

