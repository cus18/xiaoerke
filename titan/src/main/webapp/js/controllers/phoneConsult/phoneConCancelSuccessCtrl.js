angular.module('controllers', ['ionic']).controller('phoneConCancelSuccessCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title = "取消预约成功";
            $scope.pageLoading =false;
            if($stateParams.price>0){
                $scope.title = "取消预约成功";
            }else{
                $scope.title = "取消预约失败";
            }
            $scope.price = $stateParams.price


            $scope.$on('$ionicView.enter', function(){

            });

            $scope.goFirst = function () {
                window.location.href = "ap/firstPage/phoneConsult";
            }

    }])
