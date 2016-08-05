﻿angular.module('controllers', []).controller('olympicGameLevel3Ctrl', [
        '$scope','$state','$timeout','GetGamePlayingTimes','GetUserOpenId','SaveGameScore',
        function ($scope,$state,$timeout,GetGamePlayingTimes,GetUserOpenId,SaveGameScore) {
            $scope.title = "奥运宝贝-游戏第三关";
            $scope.score = 0;
            $scope.time = 15;
            $scope.lookResultFloat = false;
            $scope.challengeAgainImg = false;
            $scope.challengeMoreImg = false;
            $scope.shareFloat = false;
            $scope.startFloat = true;
            $timeout(function() {
                $scope.startFloat =false;
            }, 4000);
            $scope.olympicGameLevel3 = function () {
                loadShare();
                recordLogs("action_olympic_baby_thirth_visit");

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
                $scope.time = 15;
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
            //分享到朋友圈或者微信
            var loadShare = function(){
                var share = 'http://s123.xiaork.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s123.xiaork.com/keeper/wechatInfo/getUserWechatMenId?url=37';
                var shareDes='“宝宝奥运大闯关”开始啦！玩游戏闯关卡，赢取超值豪礼！我已加入，你也赶紧一起来参与吧！';
                var shareTitle='赢个大奖居然这么简单……';
                var imgUrl='http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/common/sharePic.png';
                version="a";
                var timestamp;//时间戳
                var nonceStr;//随机字符串
                var signature;//得到的签名
                var appid;//得到的签名
                $.ajax({
                    url:"wechatInfo/getConfig",// 跳转到 action
                    async:true,
                    type:'get',
                    data:{url:location.href.split('#')[0]},//得到需要分享页面的url
                    cache:false,
                    dataType:'json',
                    success:function(data) {
                        if(data!=null ){
                            timestamp=data.timestamp;//得到时间戳
                            nonceStr=data.nonceStr;//得到随机字符串
                            signature=data.signature;//得到签名
                            appid=data.appid;//appid
                            //微信配置
                            wx.config({
                                debug: false,
                                appId: appid,
                                timestamp:timestamp,
                                nonceStr: nonceStr,
                                signature: signature,
                                jsApiList: [
                                    'onMenuShareTimeline',
                                    'onMenuShareAppMessage'
                                ] // 功能列表
                            });
                            wx.ready(function () {
                                // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                                wx.onMenuShareTimeline({
                                    title: shareTitle, // 分享标题
                                    link: share, // 分享链接
                                    imgUrl: imgUrl, // 分享图标
                                    success: function (res) {
                                        //第三关访问量
                                        recordLogs("action_olympic_baby_thirth_share");
                                    },
                                    fail: function (res) {
                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title: shareTitle, // 分享标题
                                    desc: shareDes, // 分享描述
                                    link:share, // 分享链接
                                    imgUrl: imgUrl, // 分享图标
                                    success: function (res) {
                                        recordLogs("action_olympic_baby_thirth_share");
                                    },
                                    fail: function (res) {
                                    }
                                });
                            })
                        }else{
                        }
                    },
                    error : function() {
                    }
                });
            };
    }])
