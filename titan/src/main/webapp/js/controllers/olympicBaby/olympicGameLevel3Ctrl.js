angular.module('controllers', []).controller('olympicGameLevel3Ctrl', [
        '$scope','$state','$timeout',
        function ($scope,$state,$timeout) {
            $scope.title = "奥运宝贝-游戏第三关";
            $scope.num = 0;
            $scope.totalNum = 150;
            $scope.time = 15;
            $scope.lookResultFloat = false;
            $scope.shareFloat = false;
            $scope.startFloat = true;
            $timeout(function() {
                $scope.startFloat =false;
            }, 4000);
            $scope.olympicGameLevel3 = function () {
            };
            var timer1 = null;
            var timer2 = null;
            var timer3 = null;
            var flag = true;
            var flag1 = true;
            $scope.startRun = function(){
                $scope.num++;
                if(flag){
                    $scope.startFloat =true;
                    $timeout(function() {
                        $scope.startFloat =false;
                        startTime();
                    }, 4000);
                    startMoveBack();
                }
                startMoveBoy();
                $scope.totalNum = $scope.num;
            };
            var startMoveBoy = function () {
                var i = 0;
                flag = false;
                timer1 = setInterval(function () {
                    i++;
                    var num = -174 * i;
                    $('.runningBoy').css('background-position',num+'px 0px');
                    if(i == 7){
                        clearInterval(timer1);
                        i = 0;
                        $('.runningBoy').css('background-position',0 + 'px 0px');
                    }
                },100);

            };
            var startMoveBack = function () {
                var i = 110;
                flag = false;
                timer3 = setInterval(function () {
                    i--;
                    var num = -10 * i;
                    $('.olympicGameLevel3').css('background-position',num+'px 0px');
                    if(i == 0){
                        $('.olympicGameLevel3').css('background-position',-1100+'px 0px');
                        i = 110;
                        if($scope.time == 0){
                            clearInterval(timer3);
                        }
                    }
                },100);
            };
            /*$scope.startRun = function(){
             $scope.num++;
             if(flag){
             startMoveBack();
             }
             startMoveBoy();
             $scope.totalNum = $scope.num;
             };
             function fun() {
             if(i === 7){
             console.log('funi',i);
             clearInterval(timer1);
             timer1 = null;
             }else {
             console.log('fune',i);
             i++;
             var num = -174 * i;
             $('.runningBoy').css('background-position',num+'px 0px');
             }
             };*/
            //定时器
            var startTime = function () {
               timer2 = setInterval(function () {
                   $scope.time--;
                   $scope.$digest();
                   if($scope.time == 0){
                       clearInterval(timer2);
                   }
               },1000);
            };
            //取消浮层
            $scope.cancelFloat = function () {
                $scope.lookResultFloat = false;
            };
    }])
