angular.module('controllers', ['ionic']).controller('umbrellaLeadCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {
        $scope.title="宝大夫儿童家庭重疾互助计划";



        $scope.goJoin=function(index){
            $state.go("umbrellaIndex");
        };
        $scope.slideHasChanged=function(index){

        };


        $scope.$on('$ionicView.enter', function(){

        });


    }]);
