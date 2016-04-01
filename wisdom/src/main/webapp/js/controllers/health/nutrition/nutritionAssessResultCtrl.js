angular.module('controllers', ['ionic']).controller('nutritionAssessResultCtrl', [
    '$scope','$state','$stateParams','$timeout','GetEvaluate','ReEvaluate',
    function ($scope,$state,$stateParams,$timeout,GetEvaluate,ReEvaluate) {

        $scope.scrollNum=0;
        //膳食宝塔
        $scope.pyramidList=[
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood1.png",
                classify:"油盐类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood2.png",
                classify:"奶类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood3.png",
                classify:"鱼禽肉蛋类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood4.png",
                classify:"蔬菜类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood5.png",
                classify:"水果类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood6.png",
                classify:"谷薯类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood7.png",
                classify:"水"
            }
        ];

        //重新评估
        $scope.nutritionAssessAgain =function(){
            //$state.go("nutritionAssess",{flag:"again"});
            ReEvaluate.save({}, function (data) {
                if(data.resultMsg=="OK"){
                    window.location.href = "ap/ntr?value=251341#/nutritionAssess/again";
                }

            });
        };
        //查看营养师建议
        $scope.lookAdvice=function(){
            $state.go("nutritionEffect",{finish:"yes",type:"",weight:""});
        };
        //查看营养师食谱
        $scope.lookFood = function () {
            $state.go("nutritionFood");
        }


        $scope.$on('$ionicView.enter', function(){

            GetEvaluate.get({"flag":"evaluate"}, function (data) {
                if(data.todayEvaluateMap.fishEggs!=undefined){
                    if(data.todayEvaluateMap.fishEggs!=""){
                        if(data.todayEvaluateMap.fishEggs.split(";")[1]=="just"){//正好

                        }else if(data.todayEvaluateMap.fishEggs.split(";")[1]=="more"){//多了
                            getMore(2);
                        }else{
                            getLess(2);
                        }
                    }else{
                        getLess(2);
                    }

                    if(data.todayEvaluateMap.meat!=""){
                        if(data.todayEvaluateMap.meat.split(";")[1]=="just"){//正好

                        }else if(data.todayEvaluateMap.meat.split(";")[1]=="more"){//多了
                            getMore(4);
                        }else{
                            getLess(4);
                        }
                    }else{
                        getLess(4);
                    }

                    if(data.todayEvaluateMap.milk!=""){
                        if(data.todayEvaluateMap.milk.split(";")[1]=="just"){//正好

                        }else if(data.todayEvaluateMap.milk.split(";")[1]=="more"){//多了
                            getMore(1);
                        }else{
                            getLess(1);
                        }
                    }else{
                        getLess(1);
                    }



                    if(data.todayEvaluateMap.millet!=""){
                        if(data.todayEvaluateMap.millet.split(";")[1]=="just"){//正好

                        }else if(data.todayEvaluateMap.millet.split(";")[1]=="more"){//多了
                            getMore(5);
                        }else{
                            getLess(5);
                        }
                    }else{
                        getLess(5);
                    }


                    if(data.todayEvaluateMap.oilSalt!=""){
                        if(data.todayEvaluateMap.oilSalt.split(";")[1]=="just"){//正好

                        }else if(data.todayEvaluateMap.oilSalt.split(";")[1]=="more"){//多了
                            getMore(0);
                        }else{
                            getLess(0);
                        }
                    }else{
                        getLess(0);
                    }

                    if(data.todayEvaluateMap.potato!="") {
                        if (data.todayEvaluateMap.potato.split(";")[1] == "just") {//正好

                        } else if (data.todayEvaluateMap.potato.split(";")[1] == "more") {//多了
                            getMore(5);
                        } else {
                            getLess(5);
                        }
                    }else{
                        getLess(5);
                    }

                    if(data.todayEvaluateMap.vegetables!="") {
                        if (data.todayEvaluateMap.vegetables.split(";")[1] == "just") {//正好

                        } else if (data.todayEvaluateMap.vegetables.split(";")[1] == "more") {//多了
                            getMore(3);
                        } else {
                            getLess(3);
                        }
                    }else{
                        getLess(3);
                    }

                    if(data.todayEvaluateMap.water!=""){
                        if(data.todayEvaluateMap.water.split(";")[1]=="just"){//正好

                        }else if(data.todayEvaluateMap.water.split(";")[1]=="more"){//多了
                            getMore(6);
                        }else{
                            getLess(6);
                        }
                    }else{
                        getLess(6);
                    }

                }else{
                    getLess(0);getLess(1);getLess(2);getLess(3);getLess(4);getLess(5);getLess(6);
                }
            })

        });

        function getLess(index){
            $(".classify li").eq(index).removeClass("more");
            $(".classify li").eq(index).addClass("less");
            $(".classify li").eq(index).removeClass("more1");
            $timeout(function() {
                $(".classify li").eq(index).addClass("less1");
            }, 5000);
        }

        function getMore(index){
            $(".classify li").eq(index).removeClass("less");
            $(".classify li").eq(index).addClass("more");
            $(".classify li").eq(index).removeClass("less1");
            $timeout(function() {
                $(".classify li").eq(index).addClass("more1");
            }, 5000);
        }




    }]);

