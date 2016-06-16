angular.module('controllers', ['ionic']).controller('umbrellaTestCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="宝大夫儿童家庭重疾互助计划";

            $scope.$on('$ionicView.enter', function(){

            });

            
    }]);