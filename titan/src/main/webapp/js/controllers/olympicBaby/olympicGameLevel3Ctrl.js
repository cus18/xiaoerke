angular.module('controllers', []).controller('olympicGameLevel3Ctrl', [
        '$scope','$state','$timeout','GetGamePlayingTimes','GetUserOpenId',
        function ($scope,$state,$timeout,GetGamePlayingTimes,GetUserOpenId) {
            $scope.title = "奥运宝贝-游戏第三关";
            $scope.score = 0;
            $scope.time = 15;
            $scope.lookResultFloat = false;
            $scope.challengeAgainImg = true;
            $scope.challengeMoreImg = false;
            $scope.shareFloat = false;
            $scope.startFloat = true;
            $timeout(function() {
                $scope.startFloat =false;
            }, 4000);
            $scope.olympicGameLevel3 = function () {
                //获取openId
                GetUserOpenId.get({},function (data) {
                    console.log(data.openid);
                    $scope.openid = data.openid;
                });
                getGamePlayingTimes();
                if($scope.playCount > 0){

                }

            };

            var getGamePlayingTimes = function () {
                //获取玩游戏的次数
                GetGamePlayingTimes.get({openid:$scope.openid,gameLevel:3},function (data) {
                    console.log(data);
                    $scope.playCount = data;
                });
            };
            $scope.challengeAgain = function () {
                $scope.lookResultFloat = false;
                $scope.score = 0;
                $scope.time = 15;
                $scope.playCount--;
            };
            $scope.challengeMore = function () {
                $state.go("olympicBabyFirst",{});
            };
            var timer1 = null;
            var timer2 = null;
            var timer3 = null;
            var flag = true;
            $scope.startRun = function(){
                if(flag){
                    startTime();
                    startMoveBack();
                }
                startMoveBoy();
                $scope.score++;
            };
            //人物动画
            var i = 0;
            var startMoveBoy = function () {
                if(!timer1){
                    timer1 = setInterval(fun,100);
                }else{
                    return
                }
            };
            function fun(){
                if(i === 7){
                    clearInterval(timer1);
                    timer1 = null;
                    $('.runningBoy').css('background-position',0 + 'px 0px');
                    i = 0;
                }else{
                    i++;
                    var num = -174 * i;
                    $('.runningBoy').css('background-position',num + 'px 0px');
                }
            }
            //背景动画
            var startMoveBack = function () {
                var j = 80;
                flag = false;
                timer3 = setInterval(function () {
                    j--;
                    var num = -10 * j;
                    $('.olympicGameLevel3').css('background-position',num+'px 0px');
                    if(j === 0){
                        $('.olympicGameLevel3').css('background-position',-800+'px 0px');
                        j = 80;
                    }
                },100);
            };
            //倒计时
            var startTime = function () {
               timer2 = setInterval(function () {
                   $scope.time--;
                   if($scope.time == 0){
                       $scope.lookResultFloat = true;
                       flag = true;
                       clearInterval(timer3);
                       clearInterval(timer2);
                   }
                   $scope.$digest();
               },1000);
            };
            //取消浮层
            $scope.cancelFloat = function () {
                $scope.lookResultFloat = false;
            };
            /*var startMoveBoy = function () {
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

             };*/
    }])
