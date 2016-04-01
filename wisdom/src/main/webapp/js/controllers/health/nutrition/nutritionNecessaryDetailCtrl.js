angular.module('controllers', ['ionic']).controller('nutritionNecessaryDetailCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.scrollNum=0;
        $scope.index="0";

        $scope.selectShop =function(index){
            $scope.index=index;
            window.location.href =$scope.shopList[index].href;
        };


        $scope.$on('$ionicView.enter', function(){
            $scope.shopList = [];
            var one = {};var tow = {};var three = {};
            $scope.title = $stateParams.tl;
            $scope.introduce = $stateParams.it;
            $scope.img = $stateParams.img;

            if($stateParams.type=="工具"){
                one.href = $stateParams.jd;
                one.price = $stateParams.jdp;
                one.shop = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fshop_jd.png";
                $scope.shopList[0] = one;
                tow.href = $stateParams.os;
                tow.price = $stateParams.osp;
                tow.shop = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fshop_yhd.png";
                $scope.shopList[1] = tow;
                three.href = $stateParams.tm;
                three.price = $stateParams.tmp;
                three.shop = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fshop_tamll.png";
                $scope.shopList[2] = three;

            }else if($stateParams.type=="图书"){
                one.href = $stateParams.jd;
                one.price = $stateParams.jdp;
                one.shop = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fshop_jd.png";
                $scope.shopList[0] = one;
                tow.href = $stateParams.os;//当当
                tow.price = $stateParams.osp;
                tow.shop = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fshop_dangdang.png";
                $scope.shopList[1] = tow;
                three.href = $stateParams.tm;//亚马逊
                three.price = $stateParams.tmp;
                three.shop = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fshop_amazon.png";
                $scope.shopList[2] = three;

            }else if($stateParams.type=="水果"){
                one.href = $stateParams.jd;//顺丰
                one.price = $stateParams.jdp;
                one.shop = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fshop_sf.png";
                $scope.shopList[0] = one;
                tow.href = $stateParams.os;//本来生活
                tow.price = $stateParams.osp;
                tow.shop = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fshop_benlai.png";
                $scope.shopList[1] = tow;

            }else{
                one.href = $stateParams.jd;//顺丰
                one.price = $stateParams.jdp;
                one.shop = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fshop_sf.png";
                $scope.shopList[0] = one;
                tow.href = $stateParams.os;//本来生活
                tow.price = $stateParams.osp;
                tow.shop = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fshop_benlai.png";
                $scope.shopList[1] = tow;
            }

        });


    }]);

