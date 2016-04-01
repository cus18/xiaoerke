angular.module('controllers', ['ionic']).controller('constipationFoodAvoidCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.num = [];
        $scope.deleteFoodLock = false;
        $scope.deleteFood=function(index){
            $scope.index = index;
            $(".avoid-list a").eq( $scope.index).toggleClass("del").children("img").toggleClass("show");

        }


    }]);

