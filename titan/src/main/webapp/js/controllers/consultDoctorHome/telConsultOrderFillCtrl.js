angular.module('controllers', ['ionic']).controller('telConsultOrderFillCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.info = {};

        // 点击 提交订单
        $scope.submitOrder=function(){
            window.location.href="http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=59,"+$scope.phonePayPrice
        };



        $scope.$on('$ionicView.beforeEnter',function() {
            console.log("医生电话咨询价格 ",$stateParams.price);
            $scope.phonePayPrice=$stateParams.price;
        })

    }]);

