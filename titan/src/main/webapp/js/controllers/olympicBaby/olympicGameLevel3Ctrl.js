angular.module('controllers', []).controller('olympicGameLevel3Ctrl', [
        '$scope','$state','$timeout','GetGamePlayingTimes','GetUserOpenId','SaveGameScore',
        function ($scope,$state,$timeout,GetGamePlayingTimes,GetUserOpenId,SaveGameScore) {
            $scope.title = "奥运宝贝-游戏第三关";
            $scope.score = 0;
            $scope.time = 5;
            $scope.lookResultFloat = false;
            $scope.challengeAgainImg = false;
            $scope.challengeMoreImg = false;
            $scope.shareFloat = false;
            $scope.startFloat = true;
            $timeout(function() {
                $scope.startFloat =false;
            }, 4000);
            $scope.olympicGameLevel3 = function () {
                recordLogs("action_olympic_baby_thirth_visit");
                //第三关访问量
                //recordLogs("action_olympic_baby_thirth_share");
                //获取openId
                GetUserOpenId.get({},function (data) {
                    console.log(data.openid);
                    $scope.openid = data.openid;
                });
                //getGamePlayingTimes();
            };

            /*var getGamePlayingTimes = function () {
                //获取玩游戏的次数
                GetGamePlayingTimes.save({openid:"222222",gameLevel:"3"},function (data) {
                    console.log('playCount',data);
                    $scope.playCount = data.gamePlayingTimes;
                });
            };*/
            $scope.challengeAgain = function () {
                $scope.lookResultFloat = false;
                $scope.score = 0;
                $scope.time = 5;
            };
            $scope.challengeMore = function () {
                $state.go("olympicBabyFirst",{});
            };
            var timer1 = null;
            var timer2 = null;
            var timer3 = null;
            var flag = true;
            var startRun = function(){
                if(flag){
                    startTime();
                    startMoveBack();
                    console.log('playCount',$scope.playCount);
                    if($scope.playCount == 2){
                        $scope.challengeMoreImg = true;
                        $scope.challengeAgainImg = false;
                    }else{
                        $scope.challengeAgainImg = true;
                    }
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
            var timeM = setInterval(function(){
                if(document.getElementById('buttonImg') != null) {
                    document.getElementById('buttonImg').addEventListener("touchstart", startRun);
                    clearTimeout(timeM);
                }
            },100);
            var startTime = function () {
               timer2 = setInterval(function () {
                   $scope.time--;
                   if($scope.time == 0){
                       flag = true;
                       $scope.lookResultFloat = true;
                       clearInterval(timer3);
                       clearInterval(timer2);
                       SaveGameScore.save({openid:"222222",gameLevel:"3",gameScore:$scope.score.toString()},function (data) {
                           $scope.playCount = data.gamePlayingTimes;
                           /*if($scope.playCount == 15){
                               $scope.challengeMoreImg = true;
                               $scope.challengeAgainImg = false;
                           }*/
                           console.log('playCount',$scope.playCount)
                       });
                   }
                   $scope.$digest();
               },1000);
            };
            //取消浮层
            $scope.cancelFloat = function () {
                $scope.lookResultFloat = false;
            };
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
