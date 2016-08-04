angular.module('controllers', []).controller('olympicBabyDrawPrizeCtrl', [
        '$scope','$state','$timeout','GetGameScorePrize','GetUserOpenId','SaveUserAddress','GetUserGameScore',
        function ($scope,$state,$timeout,GetGameScorePrize,GetUserOpenId,SaveUserAddress,GetUserGameScore) {
            $scope.title = "奥运宝贝-抽奖";
            $scope.layerLock = false;//浮层总开关
            $scope.noScoreLock = false;//无积分浮层开关
            $scope.noPriseLock = false;//无奖品浮层开关
            $scope.getPriseLock = false;//有奖品浮层开关
            $scope.DrawPriseLock = false;//抽奖浮层开关
            $scope.FillInfoLock = false;//领取奖品浮层开关
            $scope.info = {};//存放姓名，电话，家庭地址
            $scope.prizeArray = [false,false,false,false,false,false,false,false,false,false];//奖品列表图片
            var click=false;
            var openid ;
            var prizIndex = 3;

            //页面初始化
            $scope.olympicBabyDrawPrizeInit = function(){
                document.title="积分抽奖"; //修改页面title
                lottery.init('lottery');
                //获取用户openid
                GetUserOpenId.get(function (data) {
                    if(data.openid!="none"){
                        openid = data.openid;
                        GetUserGameScore.save({"openid":data.openid},function (data) {
                            console.log("fenshu",data);
                            var score = parseInt(data.gameScore);
                            if(score>=80){
                                $scope.scoreNumber = parseInt(score/80);
                            }else{
                                $scope.scoreNumber = 0;
                            }
                            $scope.score = score;
                        })
                    }else{
                        openid = "none";
                    }
                });
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
                    if ($("#"+id).find(".lottery-unit").length>0) {
                        var lotteryId = $("#"+id);
                        var lotteryUnits = lotteryId.find(".lottery-unit");
                        this.obj = lotteryId;
                        this.count = lotteryUnits.length;
                    }
                },

                littleRoll:function(){
                    var index = this.index;
                    var count = this.count;
                    $scope.prizeArray[index] = false;
                    index += 1;
                    if (index>count-1) {
                        index = 0;
                    };
                    $scope.prizeArray[index] = true;
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
                        if(openid!="none"&&($scope.score>=80)) {
                            $scope.score = $scope.score - 80;
                            $scope.scoreNumber = parseInt($scope.score / 80);
                            getPrizeIndex();//获取奖品
                            lottery.speed = 100;
                            roll();
                            click = true;
                            return false;
                        }else{
                            $scope.noScoreLock = true;
                    }
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
                    if(prizIndex == 3){
                        $scope.noPriseLock = true;
                    }else{
                        $scope.getPriseLock = true;
                    }
                }else{
                    if (lottery.times<lottery.cycle) {
                        lottery.speed -= 10;
                    }else if(lottery.times==lottery.cycle) {
                         // var index = Math.random()*(lottery.count)|0;
                         // lottery.prize = index;
                        lottery.prize = prizIndex;
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
                    }
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
                $state.go("olympicBabyMyPrize");
            };

            //点击 查看抽奖规则
            var myScroll;
            $scope.lookDrawPrizeRule = function(){
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
                $scope.noScoreLock = false;//无积分浮层开关
                $scope.noPriseLock = false;//无奖品浮层开关
                $scope.getPriseLock = false;//有奖品浮层开关
                $scope.DrawPriseLock = false;//抽奖浮层开关
                $scope.FillInfoLock = false;
            };

            //点击 浮层下 玩游戏
            $scope.playGame = function(){
                $state.go("olympicBabyFirst");
            };

            //点击 浮层下 再玩一次
            $scope.tryAgain = function(){
                $scope.cancelLayer();
            };

            //点击 浮层下 领取奖品
            $scope.getPrize = function(){
                $scope.getPriseLock = false;
                if($scope.prizeLink!=""){
                    window.location.href = $scope.prizeLink;
                }else{
                    $scope.FillInfoLock = true;
                }
            };

            //保存用户邮寄地址
            $scope.saveMess = function () {
                if($scope.info.name==""||$scope.info.name==undefined||$scope.info.phone==""||
                    $scope.info.phone==undefined||$scope.info.address==""||$scope.info.address==undefined){
                    alert("信息不能为空！");
                }else{
                    SaveUserAddress.save({"openid":openid,"address":$scope.info.name+","+$scope.info.phone+","+$scope.info.address},
                    function (data) {
                        console.log("save",data);
                        if(data.$resolved == true){
                            $scope.FillInfoLock = false;
                        }else{
                            alert("信息保存失败！");
                        }
                    });
                }
            }
            
            //根据openid获取奖品
            var getPrizeIndex = function () {
                GetGameScorePrize.save({"openid":openid},function (data) {
                    console.log("data",data);
                    var index = parseInt(data.prizeOrder);
                    $scope.prizeOrder = index;
                    $scope.postage = data.postage==""?"":"(此奖品需要您支付"+data.postage+"元的快递费)";
                    if(data.prizeLink!=""){
                        $scope.prizeLink = data.prizeLink;
                        $scope.prizeName = data.prizeName;
                    }else{
                        $scope.prizeLink = "";
                        $scope.prizeName = data.prizeName+"此奖项需要您填写家庭详细信息。";
                    }
                    if(index==3){
                        prizIndex = 3;
                    }else if(index>9){
                        prizIndex = 5;
                    }else{
                        prizIndex = index;
                    }
                    console.log("prizIndex",prizIndex);
                });
            }

    }]);
