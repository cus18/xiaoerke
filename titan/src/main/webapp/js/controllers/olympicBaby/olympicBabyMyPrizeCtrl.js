angular.module('controllers', []).controller('olympicBabyMyPrizeCtrl', [
        '$scope','$state','GetUserPrizes','GetUserOpenId',
        function ($scope,$state,GetUserPrizes,GetUserOpenId) {
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

            //获取用户的openid
            GetUserOpenId.get(function (data) {
                if(data.openid!="none"){
                    //获取用户获奖列表
                    GetUserPrizes.save({"openid":data.openid},function (data) {
                        console.log("data",data);
                        if(data.prizeList!=undefined){
                            $scope.showPrize = true;
                            $scope.showNoPrize = false;
                            $scope.prizeList = data.prizeList;
                        }else{
                            $scope.showPrize = false;
                            $scope.showNoPrize = true;
                        }
                    });
                }else {
                    $scope.showPrize = false;
                    $scope.showNoPrize = true;
                }
            });


            //是否跳转第三方链接
            $scope.goLink = function (index) {
                if($scope.prizeList[index].prizeLink!=""){
                    window.location.href = $scope.prizeList[index].prizeLink;
                }
            }
    }]);
