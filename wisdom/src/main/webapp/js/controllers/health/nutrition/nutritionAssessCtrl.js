angular.module('controllers', ['ionic']).controller('nutritionAssessCtrl', [
    '$scope','$state','$stateParams','SaveUpdateEvaluate','$timeout','GetEvaluate','$http','GetUserLoginStatus','$location',
    function ($scope,$state,$stateParams,SaveUpdateEvaluate, $timeout,GetEvaluate,$http,GetUserLoginStatus,$location) {

        $scope.footerNum=1;
        $scope.homeImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer1.png";
        $scope.assessImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer2_select.png";
        $scope.necessaryImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer3.png";
        $scope.reportImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer4.png";
        $scope.info = {};
        $scope.assessLock=false;//是否评估
        $scope.ricePotatoLock=false;//选择谷薯类
        $scope.classifyNum=0;//判断当前在评估七类食物中的哪一个
        var type = "";//食物种类
        var typeWeight = "";//食物多少


        //膳食宝塔数据
        $scope.pyramidList=[
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood1.png",
                classify:"油盐类",
                classifyText:"油盐",
                assessLock:false,
                amount:"just"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood2.png",
                classify:"奶类",
                classifyText:"奶",
                assessLock:false,
                amount:"just"

            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood3.png",
                classify:"鱼禽肉蛋类",
                classifyText:"鱼禽肉蛋",
                assessLock:false,
                amount:"just"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood4.png",
                classify:"蔬菜类",
                classifyText:"蔬菜",
                assessLock:false,
                amount:"just"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood5.png",
                classify:"水果类",
                classifyText:"水果",
                assessLock:false,
                amount:"just"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood6.png",
                classify:"谷薯类",
                classifyText:"谷薯",
                assessLock:false,
                amount:"just"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood7.png",
                classify:"水",
                classifyText:"水",
                assessLock:false,
                amount:"just"
            }
        ];
        /* 底部菜单选择*/
        $scope.menuSelect =function(index){
            $scope.index=index;
            if(index==0){
                $state.go("nutritionIndex");
            }
            else if(index==1){
                $state.go("nutritionAssess");
            }
            else if(index==2){
                window.location.href = "ap/ntr?value=251339#/nutritionReport/second";
            }
            else if(index==3){
                $state.go("nutritionNecessary");
            }else if(index==4){
                $scope.commentImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fdianjihou_png_03.png";
                $state.go("nutritionAsk");
            }
        };
        //取消评估
        $scope.cancelAssess =function(){
            $scope.assessLock=false;
        };
        //评估完成
        $scope.assessFinish =function(){
            if($scope.info.weights==undefined||$scope.info.weights==""){
                alert("请输入重量！");
            }else{
                setLog("YYGL_PG_SRWC");
                $scope.pyramidList[$scope.classifyNum].assessLock=true;
                $scope.assessLock=false;
                setWeight();
                $scope.info.weights ="";

            }

        };
        $scope.classifyAssess =function(index){
            $scope.classifyNum=index;
            $scope.assessLock=true;

           /* if(index==5){
                $scope.assessLock=false;
                $scope.ricePotatoLock=true;
                $scope.pyramidList[index].assessLock=true;
            }
            else{
                $scope.assessLock=true;
                $scope.pyramidList[index].assessLock=true;
            }*/
            setType();

        };
        //选择谷薯类
        $scope.selectRicePotato =function(index){
            $scope.assessLock=true;
            $scope.ricePotatoLock=false;
            if(index==0){
                $scope.pyramidList[5].classifyText="谷";
            }
            else{
                $scope.pyramidList[5].classifyText="薯";
            }
        };

        $scope.lookFood = function () {
            $state.go("nutritionFood");
        }

        $scope.goPyramid = function () {
            $state.go("nutritionPyramid");
        }

        $scope.addFinish = function () {
            setLog("YYGL_PG_TJWB");
            window.location.href = "ap/ntr?value=251350#/nutritionAssessResult";
        }

        $scope.goWeight = function () {
            $state.go("nutritionAssessWeight",{type:type});
        }
        //食物特点
        $scope.goTrait = function () {
            $state.go("nutritionEffect",{finish:"no",type:type,weight:""});
        }

        $scope.$on('$ionicView.enter', function(){
            var routePath = "/ap/ntrBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    $scope.commentImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fdianjiqian_png_03.png";
                    var pData = {logContent:encodeURI("YYGL_PU")};
                    $http({method:'post',url:'ap/util/recordLogs',params:pData});
                    if($stateParams.flag!="again"){
                        GetEvaluate.get({"flag":"evaluate"}, function (data) {
                            if(data.todayEvaluateMap.fishEggs!=undefined){
                                if(data.todayEvaluateMap.fishEggs!=""){
                                    if(data.todayEvaluateMap.fishEggs.split(";")[1]=="just"){//正好
                                        getJust(2);
                                    }else if(data.todayEvaluateMap.fishEggs.split(";")[1]=="more"){//多了
                                        getMore(2);
                                    }else{
                                        getLess(2);
                                    }
                                }

                                if(data.todayEvaluateMap.meat!=""){
                                    if(data.todayEvaluateMap.meat.split(";")[1]=="just"){//正好
                                        getJust(4);
                                    }else if(data.todayEvaluateMap.meat.split(";")[1]=="more"){//多了
                                        getMore(4);
                                    }else{
                                        getLess(4);
                                    }
                                }

                                if(data.todayEvaluateMap.milk!=""){
                                    if(data.todayEvaluateMap.milk.split(";")[1]=="just"){//正好
                                        getJust(1);
                                    }else if(data.todayEvaluateMap.milk.split(";")[1]=="more"){//多了
                                        getMore(1);
                                    }else{
                                        getLess(1);
                                    }
                                }

                                if(data.todayEvaluateMap.millet!=""){
                                    if(data.todayEvaluateMap.millet.split(";")[1]=="just"){//正好
                                        getJust(5);
                                    }else if(data.todayEvaluateMap.millet.split(";")[1]=="more"){//多了
                                        getMore(5);
                                    }else{
                                        getLess(5);
                                    }
                                }

                                if(data.todayEvaluateMap.oilSalt!=""){
                                    if(data.todayEvaluateMap.oilSalt.split(";")[1]=="just"){//正好
                                        getJust(0);
                                    }else if(data.todayEvaluateMap.oilSalt.split(";")[1]=="more"){//多了
                                        getMore(0);
                                    }else{
                                        getLess(0);
                                    }
                                }

                                if(data.todayEvaluateMap.potato!="") {
                                    if (data.todayEvaluateMap.potato.split(";")[1] == "just") {//正好
                                        getJust(5);
                                    } else if (data.todayEvaluateMap.potato.split(";")[1] == "more") {//多了
                                        getMore(5);
                                    } else {
                                        getLess(5);
                                    }
                                }

                                if(data.todayEvaluateMap.vegetables!="") {
                                    if (data.todayEvaluateMap.vegetables.split(";")[1] == "just") {//正好
                                        getJust(3);
                                    } else if (data.todayEvaluateMap.vegetables.split(";")[1] == "more") {//多了
                                        getMore(3);
                                    } else {
                                        getLess(3);
                                    }
                                }

                                if(data.todayEvaluateMap.water!=""){
                                    if(data.todayEvaluateMap.water.split(";")[1]=="just"){//正好
                                        getJust(6);
                                    }else if(data.todayEvaluateMap.water.split(";")[1]=="more"){//多了
                                        getMore(6);
                                    }else{
                                        getLess(6);
                                    }
                                }
                            }
                        });

                    }
                }
            });


        });

        function setLog(item){
            var pData = {logContent:encodeURI(item)};
            $http({method:'post',url:'ap/util/recordLogs',params:pData});
        }

        function getLess(index){
                $scope.pyramidList[index].assessLock =true;
                $(".classify li").eq(index).removeClass("more");
                $(".classify li").eq(index).addClass("less");
                $(".classify li").eq(index).removeClass("more1");
                $(".classify .add-assess  img").eq(index).hide();

        /*    $(".classify li").eq(index).removeClass("more");
            $(".classify li").eq(index).addClass("less");
            $(".classify li").eq(index).removeClass("more1");
           /!* $timeout(function() {
                $(".classify li").eq(index).addClass("less1");
            }, 5000);*!/
            $(".classify .add-assess  img").eq(index).hide();*/
        }

        function getMore(index){
                $scope.pyramidList[index].assessLock =true;
                $(".classify li").eq(index).removeClass("less");
                $(".classify li").eq(index).addClass("more");
                $(".classify li").eq(index).removeClass("less1");
                $(".classify .add-assess  img").eq(index).hide();

           /* $timeout(function() {
                $(".classify li").eq(index).addClass("more1");
            }, 5000);*/

        }

        function getJust(index){
            $scope.pyramidList[index].assessLock =true;
            $(".classify .add-assess  img").eq(index).hide();
        }

        //获取饮食类型
        function setType() {
            if ($scope.pyramidList[$scope.classifyNum].classifyText == "油盐") {
                type = "oilSalt";
            }
            if ($scope.pyramidList[$scope.classifyNum].classifyText == "奶") {
                type = "milk";
            }
            if ($scope.pyramidList[$scope.classifyNum].classifyText == "鱼禽肉蛋") {
                type = "fishEggs";
            }
            if ($scope.pyramidList[$scope.classifyNum].classifyText == "水果") {
                type = "meat";
            }
            if ($scope.pyramidList[$scope.classifyNum].classifyText == "谷薯") {
                type = "milletandpotato";
            }
            if ($scope.pyramidList[$scope.classifyNum].classifyText == "水") {
                type = "water";
            }
            if ($scope.pyramidList[$scope.classifyNum].classifyText == "蔬菜") {
                type = "vegetables";
            }

        }

            //判断饮食类型和吃的多少
            function setWeight() {
                if ($scope.pyramidList[$scope.classifyNum].classifyText == "油盐") {
                    type = "oilSalt";
                    if ($scope.info.weights >= 0 && $scope.info.weights < 10) {
                        typeWeight = "less";//少了
                        $scope.pyramidList[$scope.classifyNum].amount="less";
                    } else if ($scope.info.weights >= 10 && $scope.info.weights <= 20) {
                        typeWeight = "just";//正好
                        $scope.pyramidList[$scope.classifyNum].amount="just";
                    } else {
                        typeWeight = "more";//多了
                        $scope.pyramidList[$scope.classifyNum].amount="more";
                    }
                    SaveUpdateEvaluate.save({"oilSalt":$scope.info.weights+";"+typeWeight}, function (data) {
                        if(data.resultMsg=="OK"){
                            $state.go("nutritionAddAssess",{type:type,weight:typeWeight});
                        }else{
                            alert("保存失败！");
                        }

                    });

                }
                if ($scope.pyramidList[$scope.classifyNum].classifyText == "奶") {
                    type = "milk";
                    if ($scope.info.weights >= 0 && $scope.info.weights < 400) {
                        typeWeight = "less";//少了
                        $scope.pyramidList[$scope.classifyNum].amount="less";
                    } else if ($scope.info.weights >= 400 && $scope.info.weights <= 600) {
                        typeWeight = "just";//正好
                        $scope.pyramidList[$scope.classifyNum].amount="just";
                    } else {
                        typeWeight = "more";//多了
                        $scope.pyramidList[$scope.classifyNum].amount="more";
                    }
                    SaveUpdateEvaluate.save({"milk":$scope.info.weights+";"+typeWeight}, function (data) {
                        if(data.resultMsg=="OK"){
                            $state.go("nutritionAddAssess",{type:type,weight:typeWeight});
                        }else{
                            alert("保存失败！");
                        }
                    });
                }
                if ($scope.pyramidList[$scope.classifyNum].classifyText == "鱼禽肉蛋") {
                    type = "fishEggs";
                    if ($scope.info.weights >= 0 && $scope.info.weights < 50) {
                        typeWeight = "less";//少了
                        $scope.pyramidList[$scope.classifyNum].amount="less";
                    } else if ($scope.info.weights >= 50 && $scope.info.weights <= 100) {
                        typeWeight = "just";//正好
                        $scope.pyramidList[$scope.classifyNum].amount="just";
                    } else {
                        typeWeight = "more";//多了
                        $scope.pyramidList[$scope.classifyNum].amount="more";
                    }
                    SaveUpdateEvaluate.save({"fishEggs":$scope.info.weights+";"+typeWeight}, function (data) {
                        if(data.resultMsg=="OK"){
                            $state.go("nutritionAddAssess",{type:type,weight:typeWeight});
                        }else{
                            alert("保存失败！");
                        }
                    });
                }
                if ($scope.pyramidList[$scope.classifyNum].classifyText == "水果") {
                    type = "meat";
                    if ($scope.info.weights >= 0 && $scope.info.weights < 100) {
                        typeWeight = "less";//少了
                        $scope.pyramidList[$scope.classifyNum].amount="less";
                    } else if ($scope.info.weights >= 100 && $scope.info.weights <= 200) {
                        typeWeight = "just";//正好
                        $scope.pyramidList[$scope.classifyNum].amount="just";
                    } else {
                        typeWeight = "more";//多了
                        $scope.pyramidList[$scope.classifyNum].amount="more";
                    }
                    SaveUpdateEvaluate.save({"meat":$scope.info.weights+";"+typeWeight}, function (data) {
                        if(data.resultMsg=="OK"){
                            $state.go("nutritionAddAssess",{type:type,weight:typeWeight});
                        }else{
                            alert("保存失败！");
                        }
                    });
                }
                if ($scope.pyramidList[$scope.classifyNum].classifyText == "谷薯") {
                    type = "milletandpotato";
                    if ($scope.info.weights >= 0 && $scope.info.weights < 70) {
                        typeWeight = "less";//少了
                        $scope.pyramidList[$scope.classifyNum].amount="less";
                    } else if ($scope.info.weights >= 70 && $scope.info.weights <= 100) {
                        typeWeight = "just";//正好
                        $scope.pyramidList[$scope.classifyNum].amount="just";
                    } else {
                        typeWeight = "more";//多了
                        $scope.pyramidList[$scope.classifyNum].amount="more";
                    }
                    SaveUpdateEvaluate.save({"millet":$scope.info.weights+";"+typeWeight,"potato":$scope.info.weights+";"+typeWeight}, function (data) {
                        if(data.resultMsg=="OK"){
                            $state.go("nutritionAddAssess",{type:type,weight:typeWeight});
                        }else{
                            alert("保存失败！");
                        }
                    });
                }
                if ($scope.pyramidList[$scope.classifyNum].classifyText == "水") {
                    type = "water";
                    if ($scope.info.weights >= 0 && $scope.info.weights < 600) {
                        typeWeight = "less";//少了
                        $scope.pyramidList[$scope.classifyNum].amount="less";
                    } else if ($scope.info.weights >= 600 && $scope.info.weights <= 1000) {
                        typeWeight = "just";//正好
                        $scope.pyramidList[$scope.classifyNum].amount="just";
                    } else {
                        typeWeight = "more";//多了
                        $scope.pyramidList[$scope.classifyNum].amount="more";
                    }
                    SaveUpdateEvaluate.save({"water":$scope.info.weights+";"+typeWeight}, function (data) {
                        if(data.resultMsg=="OK"){
                            $state.go("nutritionAddAssess",{type:type,weight:typeWeight});
                        }else{
                            alert("保存失败！");
                        }
                    });
                }
                if ($scope.pyramidList[$scope.classifyNum].classifyText == "蔬菜") {
                    type = "vegetables";
                    if ($scope.info.weights >= 0 && $scope.info.weights < 100) {
                        typeWeight = "less";//少了
                        $scope.pyramidList[$scope.classifyNum].amount="less";
                    } else if ($scope.info.weights >= 100 && $scope.info.weights <= 200) {
                        typeWeight = "just";//正好
                        $scope.pyramidList[$scope.classifyNum].amount="just";
                    } else {
                        typeWeight = "more";//多了
                        $scope.pyramidList[$scope.classifyNum].amount="more";
                    }
                    SaveUpdateEvaluate.save({"vegetables":$scope.info.weights+";"+typeWeight}, function (data) {
                        if(data.resultMsg=="OK"){
                            $state.go("nutritionAddAssess",{type:type,weight:typeWeight});
                        }else{
                            alert("保存失败！");
                        }
                    });
                }

            }


    }]);

