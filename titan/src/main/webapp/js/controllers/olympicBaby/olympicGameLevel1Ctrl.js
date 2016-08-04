angular.module('controllers', []).controller('olympicGameLevel1Ctrl', [
        '$scope','$state','$timeout','GetUserOpenId','GetGamePlayingTimes','SaveGameScore',
        function ($scope,$state,$timeout,GetUserOpenId,GetGamePlayingTimes,SaveGameScore) {
            $scope.btnLock =false;
            $scope.startCutdownLock =true;//3秒倒计时开关
            $scope.playCutdownLock =false;//15秒游戏倒计时开关 控制游泳的状态
            $scope.challengeAgainLock =true;//重新挑战游戏开关
            $scope.noPlayTimesLock =false;//没玩游戏机会开关
            $scope.playTimes =0;//玩游戏次数
            $scope.playTime =15;//15秒游戏倒计时
           /* $scope.num =0;*/
            $scope.score =0;//得分
            $scope.getScoreLock=false;//得分浮层开关
            $scope.playTimes=1;
            var pageHeight;//页面高度
            var speed=2000;//回调速度
            var times=200;//点击次数基数
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
                times=200;
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
           var startSwim = function(){
                $scope.playCutdownLock =true;
                /*$scope.score =num;*/
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
                            SaveGameScore.save({"openid":$scope.openid,"gameLevel":"1","gameScore": $scope.score.toString()},function (data) {
                                GetGamePlayingTimes.save({"openid":$scope.openid,"gameLevel":"1"},function (data) {
                                    console.log("GetGamePlayingTimes ",data.gamePlayingTimes);
                                    $scope.playTimes=data.gamePlayingTimes;
                                    if($scope.playTimes>=3){
                                        $scope.challengeAgainLock =false;
                                    }
                                });
                            });

                            $scope.getScoreLock=true;
                        }
                        $scope.$digest(); // 通知视图模型的变化
                    }, 1000);
                }

            };
            //倒计时
            var timeM = setInterval(function(){
                if(document.getElementById('btn-start') != null) {
                    document.getElementById('btn-start').addEventListener("touchstart",startSwim);
                    clearTimeout(timeM);
                }
            },100);

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

            /*日志打点*/
            //记录日志
            var setLog = function (content) {
                var pData = {logContent:encodeURI(content)};
                $http({method:'post',url:'util/recordLogs',params:pData});
            };
            /*页面分享*/
            $scope.loadShare=function() {
                var share = "http://s123.xiaork.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s123.xiaork.com/keeper/wechatInfo/getUserWechatMenId?url=37";
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
                                    title: '赢个大奖居然这么简单……', // 分享标题
                                    link:  share,
                                    imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                    success: function (res) {
                                        setLog("action_olympic_baby_once_share");
                                    },
                                    fail: function (res) {
                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title: '赢个大奖居然这么简单……', // 分享标题
                                    desc: "宝宝奥运大闯关”开始啦！玩游戏闯关卡，赢取超值豪礼！我已加入，你也赶紧一起来参与吧！", // 分享描述
                                    link:  share, // 分享链接
                                    imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                    success: function (res) {
                                        setLog("action_olympic_baby_once_share");
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
            }
            /*页面初始化*/
            $scope.olympicGameLevel1Init = function(){
                document.title="第一关 游泳"; //修改页面title
                pageHeight=document.body.clientHeight-200;//获取页面高度
                GetUserOpenId.save({"openid":$scope.openid},function (data) {
                    console.log("openid ",data.openid);
                    $scope.openid = data.openid;
                    if( $scope.openid!="none"){
                        GetGamePlayingTimes.save({"openid":$scope.openid,"gameLevel":"1"},function (data) {
                            console.log("GetGamePlayingTimes ",data.gamePlayingTimes);
                            if(data.gamePlayingTimes<3){
                                /* 游戏开始3秒倒计时*/
                                $scope.startCutdownLock =true;
                                $timeout(function() {
                                    $scope.startCutdownLock =false;
                                }, 4000);
                            }
                            else{
                                $scope.startCutdownLock =false;
                                $scope.noPlayTimesLock =true;
                            }
                        });
                    }
                    else{
                        alert("去微信关注宝大夫（BaodfWXf）吧");
                    }


                });
                setLog("action_olympic_baby_once_visit");
                $scope.loadShare();
        };
    }])
