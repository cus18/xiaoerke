angular.module('controllers', ['ionic']).controller('umbrellaLeadCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="宝大夫儿童家庭重疾互助计划";

            /*立即加入*/
            $scope.goJoin=function(){

            };
            $scope.$on('$ionicView.enter', function(){

            });

            
    }]);