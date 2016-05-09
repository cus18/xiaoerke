angular.module('controllers', ['ionic']).controller('youFuMamaCtrl', [
        '$scope','$state','$ionicScrollDelegate','RecordLogs','$http',
        function ($scope,$state,$ionicScrollDelegate,RecordLogs,$http) {
            $scope.title0 = "宝大夫健康咨询";
            $scope.shareLock=false;

            $scope.share=function(){
                $scope.shareLock=true;
            };
            $scope.cancel=function(){
                $scope.shareLock=false;
            };


    }])
