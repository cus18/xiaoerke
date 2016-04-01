angular.module('controllers', ['ionic']).controller('withdrawSuccessCtrl', [
        '$scope','$stateParams','resolveUserLoginStatus','$location',
    function ($scope,$stateParams,resolveUserLoginStatus,$location) {
        $scope.pageLoading = true;
        $scope.title0 = "宝大夫（400-623-7120）";
        $scope.title = "提现成功";
        $scope.returnMoney = $stateParams.returnMoney;

        $scope.$on('$ionicView.enter', function(){
            resolveUserLoginStatus.events($location.path(),"","","","notGo");
        });
    }])
