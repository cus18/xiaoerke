angular.module('controllers', ['ionic']).controller('umbrellaInviteCtrl', [
        '$scope','$state','$stateParams','$timeout','$location','$anchorScroll','Chats',
        function ($scope,$state,$stateParams, $timeout,$location,$anchorScroll,Chats) {
            $scope.title="邀请提额";
            $scope.shareLock=false;
            $scope.invite=function(){
                $scope.shareLock=true;
            };
            $scope.cancelShare=function(){
                $scope.shareLock=false;
            };

            $scope.$on('$ionicView.enter', function(){

            });
    }]);