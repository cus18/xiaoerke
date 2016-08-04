angular.module('controllers', []).controller('olympicGameLevel1Ctrl', [
        '$scope','$state','$timeout','GetUserOpenId','GetGamePlayingTimes','SaveGameScore',
        function ($scope,$state,$timeout,GetUserOpenId,GetGamePlayingTimes,SaveGameScore) {
            $scope.btnLock =false;
            $scope.startCutdownLock =true;//3秒倒计时开关
            $scope.playCutdownLock =false;//15秒游戏倒计时开关 控制游泳的状态
            $scope.challengeAgainLock =true;//15秒游戏倒计时开关
            $scope.playTimes =0;//玩游戏次数
            $scope.playTime =15;//15秒游戏倒计时
           /* $scope.num =0;//*/
            $scope.score =0;//得分
            $scope.totalNum=0;
            $scope.getScoreLock=false;
            $scope.playTimes=1;
            var pageHeight;//页面高度
            var speed=2000;//回调速度
            var times=120;//点击次数基数
            var bottom=0;// 距离页面底部距离
            var myTimer;//定时器
            var timerLock = false;// 定时器锁
            var counterTimer;
            // 重新挑战
            $scope.challengeAgain=function(){
                $scope.getScoreLock=false;
                $scope.score =0;
                $scope.playTime =15;
                speed=2000;
                times=100;
                bottom=0;
                $("#swimmer").animate({bottom: bottom+"px"},0);
                timerLock = false;
                clearTimeout(myTimer);
            };
            // 挑战更多
            $scope.challengeMore=function(){
                $state.go("olympicBabyFirst");
            };
            //点击发力
            $scope.startSwim = function(num){
                $scope.playCutdownLock =true;
                $scope.score =num;
                $scope.score++;
                if($scope.score<100){
                    speed=speed*(1-$scope.score/times);
                }
                clearTimeout(myTimer);
                mySpeed();
                if(!timerLock){
                    timerLock = !timerLock;
                        counterTimer = setInterval(function() {
                        $scope.playTime--;
                        if($scope.playTime==0){
                            $scope.playTime=0;
                            clearInterval(counterTimer);
                            $scope.playCutdownLock =false;
                            SaveGameScore.save({"openid":$scope.openid,"gameLevel":1,"gameScore": $scope.score},function (data) {
                                console.log("SaveGameScore ",data.gameScore);
                            });
                            GetGamePlayingTimes.save({"openid":$scope.openid,"gameLevel":1},function (data) {
                                console.log("GetGamePlayingTimes ",data.gamePlayingTimes);
                                $scope.playTimes=data.gamePlayingTimes;
                                if($scope.playTimes>2){
                                    $scope.challengeAgainLock =false;
                                }
                            });
                            $scope.getScoreLock=true;
                        }
                        $scope.$digest(); // 通知视图模型的变化
                    }, 1000);
                }

            };

           //控制游泳的速度
            var mySpeed=function(){
                if(bottom<pageHeight/7){
                    bottom=bottom+0.2;

                }
                else if(bottom<2*pageHeight/7){
                    bottom=bottom+0.3;
                }
                else if(bottom<3*pageHeight/7){
                    bottom=bottom+0.4;

                }
                else if(bottom<4*pageHeight/7){
                    bottom=bottom+0.3;

                }
                else if(bottom<5*pageHeight/7){
                    bottom=bottom+0.2;

                }
                else if(bottom<6*pageHeight/7){
                    bottom=bottom+0.1;
                }
                else if(bottom<pageHeight){
                    bottom=bottom+0.1;
                }
                else{
                    bottom=pageHeight;
                }
                $("#swimmer").animate({bottom: bottom+"px"},0);
                myTimer = setTimeout(mySpeed,speed);
                if($scope.playTime==0){
                    clearTimeout(myTimer);
                }
            };

            //日志打点
            var recordLogs = function(val){
                    $.ajax({
                        url:"util/recordLogs",// 跳转到 action
                        async:true,
                        type:'get',
                        data:{logContent:encodeURI(val)},
                        cache:false,
                        dataType:'json',
                        success:function(data) {
                        },
                        error : function() {
                        }
                    });
                };
            /*页面初始化*/
            $scope.olympicGameLevel1Init = function(){
                document.title="第一关 游泳"; //修改页面title
                pageHeight=document.body.clientHeight-200;//获取页面高度
               /* 游戏开始3秒倒计时*/
                 $timeout(function() {
                 $scope.startCutdownLock =false;
                 }, 4000);
                GetUserOpenId.save({"openid":$scope.openid},function (data) {
                    console.log("openid ",data.openid);
                    $scope.openid = data.openid;

                });
        };



    }])
