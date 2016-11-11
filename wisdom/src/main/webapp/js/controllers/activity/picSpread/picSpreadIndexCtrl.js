angular.module('controllers', ['ionic']).controller('picSpreadIndexCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.title ="图片传播";

        $scope.makePic = function(){
            $state.go("picSpreadResult");
        };

        $scope.$on('$ionicView.enter', function() {
            $(".main-wrap").css("height",screen.height+"px")

        });
    }]);

