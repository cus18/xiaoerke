angular.module('controllers', ['ionic']).controller('picSpreadResultCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {
        $scope.title ="图片传播";
        $scope.pageLoading =true;// 页面加载是否完成

        $scope.tryAgain = function(){
            $state.go("picSpreadIndex");
        };

        $scope.$on('$ionicView.enter', function() {


        });
    }]);

