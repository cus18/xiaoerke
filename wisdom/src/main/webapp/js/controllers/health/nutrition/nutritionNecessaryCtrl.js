angular.module('controllers', ['ionic']).controller('nutritionNecessaryCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.footerNum=3;
        $scope.homeImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer1.png";
        $scope.assessImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer2.png";
        $scope.necessaryImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer3_select.png";
        $scope.reportImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer4.png";
        $scope.necessaryList=[
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fnecessary1.png",
                text:"工具"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fnecessary2.png",
                text:"蔬菜"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fnecessary3.png",
                text:"水果"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fnecessary4.png",
                text:"图书"
            },
        ];

        $scope.necessaryList1 = [
            { title:"食物称",
                introduce:"可用于称量各种食物，帮助宝妈控制宝宝的食量，让宝宝均衡饮食。",
                jingdong:"http://item.jd.com/1276101.html",
                jingdongprice:"59",
                oneshop:"http://item.yhd.com/item/50383878",
                oneshopprice:"55",
                tianmao:"https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.66.oIUhE9&id=43044077865&skuId=85649334409&areaId=110100&cat_id=2&rn=57318741c2f2603b220058e0b1e51f70&user_id=372448381&is_b=1",
                tianmaoprice:"59",
                price:"55",
                img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglfoodscale"
            },
            { title:"辅食机",
                introduce:"可以榨果汁、打肉馅鱼泥、磨干粉、打豆浆，辅助妈妈更好的制作宝宝辅食。",
                jingdong:"http://item.jd.com/942300.html#",
                jingdongprice:"199",
                oneshop:"http://item.yhd.com/item/59477416?tc=3.0.5.59477416.1&tp=51.JYL-C022E.124.1.1.LCIduq3-10-8Z9`A&abtest=1.0_179&ti=U2WZ",
                oneshopprice:"218",
                tianmao:"https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.1.1Ea3Yg&id=5720261533&skuId=30165984691&areaId=110100&cat_id=2&rn=d6dced50860eabb219fb07da9a12bee8&user_id=415670368&is_b=1",
                tianmaoprice:"199",
                price:"199",
                img:"http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/yyglFeedingMachine"
            }
        ];


        $scope.$on('$ionicView.enter', function(){
            $scope.commentImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fdianjiqian_png_03.png";
        });

        $scope.chooseType = function (text) {
            $state.go("nutritionNecessaryList",{type:text});
        }

        /* 底部菜单选择*/
        $scope.menuSelect =function(index){
            $scope.index=index;
            if(index==0){
                $state.go("nutritionIndex");
            }
            else if(index==1){
                // $state.go("nutritionAssess",{flag:"noagain"});
                window.location.href = "ntr?value=251349#/nutritionAssess/noagain";
            }
            else if(index==2){
                //$state.go("nutritionReport",{type:"second"});
                window.location.href = "ntr?value=251337#/nutritionReport/second";
            }
            else if(index==3){
                $state.go("nutritionNecessary");
            }else if(index==4){
                $scope.commentImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fdianjihou_png_03.png";
                $state.go("nutritionAsk");
            }
        };


        $scope.goNecessaryList =function(tl,it,jd,jdp,os,osp,tm,tmp,img){
            $state.go("nutritionNecessaryDetail",{type:"工具",tl:tl,it:it,jd:jd,jdp:jdp,os:os,osp:osp,tm:tm,tmp:tmp,img:img})
        };





    }]);

