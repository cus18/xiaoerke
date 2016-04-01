angular.module('controllers', ['ionic']).controller('nutritionAssessWeightCtrl', [
    '$scope','$state','$stateParams','$ionicScrollDelegate',
    function ($scope,$state,$stateParams,$ionicScrollDelegate) {

        $scope.scrollNum=0;
        $scope.leftIndex="0";

        $scope.classifyListText=["谷薯类","蔬菜类","油盐类","奶类","鱼禽肉蛋类","水果类","水类"];
        $scope.classifyListPic=[
            [
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglHangingNoodles50g",
                    text:"挂面50g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglRice50g",
                    text:"大米50g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglRice110g",
                    text:"米饭110g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglSteamedBuns160g",
                    text:"馒头160g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglSweetPotato280g",
                    text:"红薯280g"
                }
            ],
            [
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglBellPepper120g",
                    text:"柿子椒120g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglCabbage900g",
                    text:"圆白菜900g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglCauliflower120g",
                    text:"菜花120g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglChineseCabbage120g",
                    text:"小白菜120g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglCucumber140g",
                    text:"黄瓜140g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglEggplant390g",
                    text:"茄子390g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglgarlicBolt120g",
                    text:"蒜苗120g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglLettuce630g",
                    text:"莴笋630g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglLotus380g",
                    text:"藕380g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglWaxGourd125g",
                    text:"冬瓜125g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglWhiteRadish550g",
                    text:"白萝卜550g"
                }
            ],
            [
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglOil25g",
                    text:"油25g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglSalt6g",
                    text:"盐6g"
                }
            ],
            [
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglMilk200ml",
                    text:"鲜奶200ml"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglPowderedMilk15g",
                    text:"奶粉15g"
                }
            ],
            [
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglChickenBreast50g",
                    text:"鸡胸肉50g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglHairtail65g",
                    text:"带鱼65g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglPerch520g",
                    text:"鲈鱼520g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglPork50g",
                    text:"猪肉50g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglShrimp80g",
                    text:"虾80g"
                }
            ],
            [
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglApple260g",
                    text:"苹果260g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglBanana150g",
                    text:"香蕉150g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglCherryTomatoes100g",
                    text:"樱桃西红柿100g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglFreshDates115g",
                    text:"鲜枣115g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglGrape115g",
                    text:"葡萄115g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglkiwifruit120g",
                    text:"猕猴桃120g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglMango550g",
                    text:"芒果550g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglOrange130g",
                    text:"橘子130g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglPeach260g",
                    text:"桃260g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglPear180g",
                    text:"梨180g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglpineapple930g",
                    text:"菠萝930g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglpitaya480g",
                    text:"火龙果480g"
                },
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglWatermelon180g",
                    text:"西瓜180g"
                }
            ],
            [
                {
                    img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglWater200ml",
                    text:"水200ml"
                }
            ]
        ];
        $scope.selectClassify =function(index){
            $scope.leftIndex=index;
            $ionicScrollDelegate.scrollTop();
        };



        $scope.$on('$ionicView.enter', function(){
            if ($stateParams.type == "oilSalt") {
                $scope.leftIndex = 2;
            }
            if ($stateParams.type == "milk") {
                $scope.leftIndex = 3;
            }
            if ($stateParams.type== "fishEggs") {
                $scope.leftIndex = 4;
            }
            if ($stateParams.type == "meat") {
                $scope.leftIndex = 5;
            }
            if ($stateParams.type== "milletandpotato") {
                $scope.leftIndex = 0;
            }
            if ($stateParams.type == "water") {
                $scope.leftIndex = 6;
            }
            if ($stateParams.type == "vegetables") {
                $scope.leftIndex = 1;
            }

        });


    }]);

