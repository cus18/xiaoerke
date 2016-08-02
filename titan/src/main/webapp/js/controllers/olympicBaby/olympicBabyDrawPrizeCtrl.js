angular.module('controllers', []).controller('olympicBabyDrawPrizeCtrl', [
        '$scope','$state','$timeout',
        function ($scope,$state,$timeout) {
            $scope.title = "奥运宝贝-抽奖";
            $scope.layerLock = false;//浮层总开关
            $scope.noScoreLock = false;//无积分浮层开关
            $scope.noPriseLock = false;//无奖品浮层开关
            $scope.getPriseLock = false;//有奖品浮层开关
            $scope.DrawPriseLock = false;//抽奖浮层开关
            $scope.prizeArray = [false,false,false,false,false,false,false,false,false,false];//奖品列表图片
            var click=false;

            //页面初始化
            $scope.olympicBabyDrawPrizeInit = function(){
                lottery.init('lottery');
            };

            var lottery={
                index:-1,	//当前转动到哪个位置，起点位置
                count:10,	//总共有多少个位置
                timer:0,	//setTimeout的ID，用clearTimeout清除
                speed:20,	//初始转动速度
                times:0,	//转动次数
                cycle:50,	//转动基本次数：即至少需要转动多少次再进入抽奖环节
                prize:-1,	//中奖位置
                init:function(id){
                    /*if (this.count>0) {
                     $lottery = $("#"+id);
                     //$units = $lottery.find(".lottery-unit");
                     this.obj = $lottery;
                     // console.log("$lottery " + $lottery);
                     //this.count = $units.length;
                     //$lottery.find(".lottery-unit-"+this.index).addClass("active");
                     $scope.prizeArray[this.index]=true;
                     console.log("init");
                     };*/
                    if ($("#"+id).find(".lottery-unit").length>0) {
                        $lottery = $("#"+id);
                        $units = $lottery.find(".lottery-unit");
                        this.obj = $lottery;
                        this.count = $units.length;
                    }

                },

                littleRoll:function(){
                    var index = this.index;
                    var count = this.count;
                    // var lottery = this.obj;
                    //$(lottery).find(".lottery-unit-"+index).removeClass("active");
                    $scope.prizeArray[index] = false;
                    console.log(index+" "+$scope.prizeArray[index]);
                    index += 1;
                    if (index>count-1) {
                        index = 0;
                    };
                    //$(lottery).find(".lottery-unit-"+index).addClass("active");
                    $scope.prizeArray[index] = true;
                    console.log(index+" "+$scope.prizeArray[index]);
                    this.index=index;
                    return false;
                },
                stop:function(index){
                    this.prize=index;
                    return false;
                }
            };

            //点击 我要抽奖
            $scope.startDrawPrize = function(){
                if (click) {
                    return false;
                }else{
                    lottery.speed=100;
                    roll();
                    click=true;
                    return false;
                }

            };

            var roll=function (){
                lottery.times += 1;
                lottery.littleRoll();
                if (lottery.times > lottery.cycle+10 && lottery.prize==lottery.index) {
                    //clearTimeout(lottery.timer);
                    $timeout.cancel(lottery.timer);
                    lottery.prize=-1;
                    lottery.times=0;
                    click=false;
                }else{
                    if (lottery.times<lottery.cycle) {
                        lottery.speed -= 10;
                    }else if(lottery.times==lottery.cycle) {
                        var index = Math.random()*(lottery.count)|0;
                        lottery.prize = index;
                        console.log("我的奖品"+ lottery.prize );
                    }else{
                        if (lottery.times > lottery.cycle+10 && ((lottery.prize==0 && lottery.index==7) || lottery.prize==lottery.index+1)) {
                            lottery.speed += 110;
                        }else{
                            lottery.speed += 20;
                        }
                    }
                    if (lottery.speed<40) {
                        lottery.speed=40;
                    };
                    console.log(lottery.times+'^^^^^^'+lottery.speed+'^^^^^^^'+lottery.prize);
                    //lottery.timer = setTimeout(roll,lottery.speed);
                    lottery.timer = $timeout(function () {
                        roll();
                    },lottery.speed);
                }
                return false;
            }

            //点击 返回游戏
            $scope.backHomePage = function(){
                $state.go("olympicBabyFirst");
            };

            //点击 查看我的奖品
            $scope.lookMyPrize = function(){
                // $scope.layerLock = true;
                // $scope.getPriseLock = true;
                $state.go("olympicBabyMyPrize");
            };

            //点击 查看抽奖规则
            var myScroll;
            $scope.lookDrawPrizeRule = function(){
                $scope.layerLock = true;
                $scope.DrawPriseLock = true;
                loaded();
                window.addEventListener("load",loaded,false);

            };
            function loaded(){
                setTimeout(function(){
                        myScroll = new iScroll('wrapper', {
                            hideScrollbar: true,
                            checkDOMChanges:true
                        }) },
                    100 );
            }

            //点击 取消浮层
            $scope.cancelLayer = function(){
                $scope.layerLock = false;
                $scope.noScoreLock = false;//无积分浮层开关
                $scope.noPriseLock = false;//无奖品浮层开关
                $scope.getPriseLock = false;//有奖品浮层开关
                $scope.DrawPriseLock = false;//抽奖浮层开关
            };

            //点击 浮层下 玩游戏
            $scope.playGame = function(){
                $state.go("olympicBabyFirst");
            };

            //点击 浮层下 再玩一次
            $scope.tryAgain = function(){
                //$state.go("olympicBabyFirst");
                $scope.cancelLayer();
            };

            //点击 浮层下 领取奖品
            $scope.getPrize = function(){
                $state.go("olympicBabyFirst");
            };
            
    }]);
