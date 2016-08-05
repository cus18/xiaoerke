angular.module('controllers', []).controller('olympicGameLevel2Ctrl', [
    '$scope','$state','$timeout','GetUserOpenId','SaveGameScore',
    function ($scope,$state,$timeout,GetUserOpenId,SaveGameScore) {
        $scope.title = "奥运宝贝-游戏第二关";
        $scope.olympicGameLevel2Init = function(){
            (function(){
                loadShare();
                var i = 1,
                    y = 1,
                    time1,
                    time2,
                    time3,
                    time4,
                    randomNub = 0,
                    bingo = false,
                    score = 0,
                    total = 20,
                    countDown = -1;
                $('#score').html(score*5);
                $('#surplus').html('还剩'+total+'个栏');
                time4 = setInterval(function(){
                    countDown++;
                    if(countDown == '4'){
                        clearInterval(time4);
                        movingBackgroundRun();
                        $('#startAnimat').hide();
                    }
                    $('#startAnimat>img').hide();
                    $('#startAnimat>img').eq(countDown).show();
                },1000)
                function barrierRun() {
                    if(total == '0'){
                        clearTime();
                        $('#startAnimat').show()
                        $('#resultImg').show()
                        $('#spielergebnis').html(score*5);
                        submitScore();
                    }
                    var barrierLeft = (parseInt($('#barrier').css('left')) / parseInt($('#hurdlesBox').css('width')) * 100);
                    if (barrierLeft > 5 && barrierLeft < 25) {
                        if (bingo == false) {
                            clearTime();
                            $('#startAnimat').show()
                            $('#resultImg').show()
                            $('#spielergebnis').html(score*5);
                            submitScore();
                        }
                    };
                    if (barrierLeft < -25 || isNaN(barrierLeft)) {
                        $('#barrier').remove();
                        if (randomNub != '0') {
                            if (randomNub == 1) {
                                $("#athlete").before('<div class="barrier" id="barrier"></div>');
                                total--;
                                score++;
                                $('#score').html(score*5);
                                $('#surplus').html('还剩'+total+'个栏')
                                randomNub = 0;
                            } else {
                                randomNub--;
                            }
                        } else {
                            randomNub = random() + 1;
                        };
                    } else {
                        $('#barrier').css('left', barrierLeft - 3 + '%');
                    }
                }

                function movingBackgroundRun() {
                    time1 = setInterval(athleteRun, 150);
                    time2 = setInterval(function() {
                        $('#movingBackground').css('background-position', (y -= 10) + 'px ' + (0) + 'px')
                    }, 42);
                    time3 = setInterval(barrierRun, 42)
                }

                function random() {
                    return Math.floor(Math.random() * 50);
                }

                function athleteRun() {
                    $('#athlete').css('background-position', (169.5 * i) + 'px ' + (0) + 'px');
                    $('#scottpilgrim').css('background-position', (-108 * i) + 'px ' + (0) + 'px');
                    i++;
                    i == 9 ? i = 1 : i = i;
                };

                function clearTime() {
                    clearInterval(time1);
                    clearInterval(time2);
                    clearInterval(time3);
                    $("#jumpBtn").unbind();
                };

                function jump() {
                    bingo = true;
                    clearInterval(time1);
                    document.getElementById('jumpBtn').removeEventListener("touchstart", jump);
                    $('#athlete').css('background-position', (169.5 * 7) + 'px ' + (0) + 'px');
                    $("#athlete").animate({
                        top: '-=200px',
                    }, 400);
                    $("#athlete").animate({
                        top: '+=200px',
                    }, 600);
                    setTimeout(function() {
                        time1 = setInterval(athleteRun, 150);
                        bingo = false;
                        document.getElementById('jumpBtn').addEventListener("touchstart", jump);
                    }, 850)
                };

                function submitScore() {
                    SaveGameScore.save({'openid':''+$scope.openid,'gameLevel':'2','gameScore':''+score*5},function(data){
                        if(data.gamePlayingTimes>=3){
                            $('#challengeAgain').remove();
                        }
                    });
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
                recordLogs('action_olympic_baby_tiwce_visit');
                document.getElementById('jumpBtn').addEventListener("touchstart", jump);
                $('#challengeAgain').bind('click',function(){
                    location.reload();
                });
                $('#challengeMore').bind('click',function(){
                    window.location.href="olympicBaby#/olympicBabyFirst";
                });
                GetUserOpenId.get({},function (data) {
                    if(data.openid=="none"){
                        window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=37";
                    }else{
                        $scope.openid = data.openid;
                    }
                });
            }());
        };


        //分享到朋友圈或者微信
        var loadShare = function(){
            var share = 'http://s68.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s68.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=37';
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
                                    recordLogs('action_olympic_baby_tiwce_share');
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
                                    recordLogs('action_olympic_baby_tiwce_share');
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
