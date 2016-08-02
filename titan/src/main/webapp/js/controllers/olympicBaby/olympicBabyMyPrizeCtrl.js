angular.module('controllers', []).controller('olympicBabyMyPrizeCtrl', [
        '$scope','$state','$timeout',
        function ($scope,$state,$timeout) {
            $scope.title = "奥运宝贝-我的奖品";
            $scope.showPrize = true;
            $scope.showNoPrize = false;
            $scope.prizeList = [
                {
                    src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                    prizeName:'京东优惠券',
                    prize:'京东优惠券'
                },{
                    src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                    prizeName:'京东优惠券',
                    prize:'京东优惠券'
                },{
                    src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                    prizeName:'京东优惠券',
                    prize:'京东优惠券'
                }
            ]
    }])
