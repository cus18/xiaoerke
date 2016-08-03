angular.module('controllers', []).controller('olympicGameLevel1Ctrl', [
        '$scope','$state','$timeout',
        function ($scope,$state,$timeout) {
            $scope.title = "奥运宝贝游戏第一关";
            $scope.btnLock =false;
        $scope.startCutdownLock =false;//3秒倒计时开关
        $scope.playCutdownLock =false;//15秒游戏倒计时开关
        $scope.playTime =15;//15秒游戏倒计时
        $scope.num =0;//
        $scope.score =0;//得分
        $scope.totalNum=0;
        $scope.getScoreLock=false;
        $scope.playTimes=1;
        var pageHeight;//页面高度
        var speed=2000;//回调速度
        var times=100;//点击次数基数
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
                        console.log("$scope.playTime "+$scope.playTime);
                        clearInterval(counterTimer);
                        $scope.playCutdownLock =false;
                        $scope.getScoreLock=true;
                    }
                    $scope.$digest(); // 通知视图模型的变化
                }, 1000);
            }

        };
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

        $scope.olympicGameLevel1Init = function(num){
            /*  $scope.startCutdownLock =true;
             $timeout(function() {
             $scope.startCutdownLock =false;
             $scope.playCutdownLock =true;
             }, 4000);*/
           pageHeight=document.body.clientHeight-200;//获取页面高度
        };



    }])
