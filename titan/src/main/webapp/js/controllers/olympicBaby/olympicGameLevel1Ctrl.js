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

        // 取消浮层 得分情况
        $scope.cancelLayer=function(){
            $scope.getScoreLock=false;
        };
        var flag = true;

        /* $scope.startSwim = function(num){
         $scope.playCutdownLock =true;
         if( $scope.playCutdownLock){
         $scope.score=num;
         $scope.score++;
         $scope.totalNum+= $scope.score;
         $("#swimmer").animate({bottom:$scope.totalNum/10+"px"},0);
         }
         var timer1 = setInterval(function() {
         $scope.playTime--;
         $scope.$digest(); // 通知视图模型的变化
         console.log("playTime"+$scope.playTime);
         }, 1000);

         if(flag){
         flag = false;
         var timer = setInterval(function() {
         $scope.playTime--;
         if($scope.playTime==0){
         $scope.playTime=0;
         clearInterval(timer);
         $scope.playCutdownLock =false;
         console.log("playTime 0");
         }
         $scope.$digest(); // 通知视图模型的变化
         console.log("playTime"+$scope.playTime);
         }, 1000);
         }

         };*/
        var speed=2000;
        var times=100;
        var top=0;
        var myTimer;
        var lock = false;
        var counterTimer;
        $scope.startSwim = function(num){
            $scope.playCutdownLock =true;
            $scope.score =num;
            $scope.score++;
            if($scope.score<100){
                speed=speed*(1-$scope.score/times);
            }
            clearTimeout(myTimer);
            mySpeed();
            if(!lock){
                lock = !lock;
                    counterTimer = setInterval(function() {
                    $scope.playTime--;
                    if($scope.playTime==0){
                        $scope.playTime=0;
                        clearInterval(counterTimer);
                        $scope.playCutdownLock =false;
                    }
                    $scope.$digest(); // 通知视图模型的变化
                }, 1000);
            }

        };
        var mySpeed=function(){
            top++;
            $("#swimmer").animate({bottom: top+"px"},0);
            console.log("top"+top);
            myTimer = setTimeout(mySpeed,speed);
            if($scope.playTime==0){
                clearTimeout(myTimer);
            }
        }
        $scope.olympicGameLevel1Init = function(num){
            /*  $scope.startCutdownLock =true;
             $timeout(function() {
             $scope.startCutdownLock =false;
             $scope.playCutdownLock =true;
             }, 4000);*/

        };



    }])
