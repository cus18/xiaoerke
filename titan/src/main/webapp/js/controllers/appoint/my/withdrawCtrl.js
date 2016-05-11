angular.module('controllers', ['ionic']).controller('withdrawCtrl', [
        '$scope','$state','$stateParams','ReturnPay','resolveUserLoginStatus','$location',
        function ($scope,$state,$stateParams,ReturnPay,resolveUserLoginStatus,$location) {
            $scope.pageLoading = true;

            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.title = "申请提现";
            $scope.accountFund = $stateParams.accountFund
            $scope.info = {};
            $scope.info.isDraw = false;
            $scope.moment = moment().add("1", 'days').format('YYYY/MM/DD HH:mm')

            $scope.withdrawSuccess = function(){
                $scope.info.isDraw = true;
              //打款
                if($scope.info.takeCash>100){
                    alert("提现额度过大,单次提现不能超100元")
                    return;
                }
                ReturnPay.save({takeCashOut:$scope.info.takeCash},function(data){
                 if("success" == data.return_msg){
                     $state.go("withdrawSuccess",{"returnMoney":$scope.info.takeCash})
                 }else{
                     alert(data.return_msg);
                 }
              })
            }

            $scope.$on('$ionicView.enter', function(){
                resolveUserLoginStatus.events($location.path(),"","","","notGo");
            });
    }])
