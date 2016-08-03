angular.module('controllers', []).controller('olympicBabyMyPrizeCtrl', [
        '$scope','$state',
        function ($scope,$state) {
            $scope.showPrize = false;
            $scope.showNoPrize = true;
            $scope.prizeList = [
                {
                    src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                    prizeName:'价值168元的乐普多种维生素加矿物质片',
                    
                },{
                    src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                    prizeName:'京东优惠券',
                    
                },{
                    src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                    prizeName:'京东优惠券',
                   
                }
            ]


    }]);
