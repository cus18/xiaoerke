angular.module('controllers', ['ionic']).controller('accountBalanceCtrl', [
        '$scope','$state','AccountInfo','$location',
        function ($scope,$state,AccountInfo,$location) {
            $scope.title="账户余额";
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.accountFund = ''
            $scope.itemizedAccountInfo = null;

            $scope.$on('$ionicView.enter',function() {
                $scope.pageLoading = true;
                var routePath = "/appointBBBBBB" + $location.path();
                AccountInfo.save({routePath:routePath},function(data) {
                    $scope.pageLoading = false;
                    if (data.status == "9") {
                        window.location.href = data.redirectURL;
                    }
                    else {
                        $scope.accountFund = data.accountFund / 100;
                        $scope.itemizedAccountInfo = data.itemizedAccountList;
                    }
                })
            })
        }])
