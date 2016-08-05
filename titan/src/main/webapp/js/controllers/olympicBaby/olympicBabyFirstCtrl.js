angular.module('controllers', []).controller('olympicBabyFirstCtrl', [
    '$scope','$state','$timeout','GetUserPrizeList','GetGameMemberNum','GetGamePlayingTimes','GetUserOpenId',
    'GetUserGameScore','GetGameMemberStatus',
    function ($scope,$state,$timeout,GetUserPrizeList,GetGameMemberNum,GetGamePlayingTimes,GetUserOpenId,
              GetUserGameScore,GetGameMemberStatus) {
        $scope.title = "奥运宝贝-首页";
        $scope.headcount = 0;//参与游戏的总人数
        $scope.shareFloat = false;
        $scope.invitePeopleNum = 0;//邀请成功的人数
        $scope.withoutCount = false;//游戏次数用完了
        $scope.playCount = 0;//游戏次数
        $scope.score = 0;//游戏积分
        $scope.scroll = [];
        $scope.olympicBabyFirstInit = function(){
            recordLogs("action_olympic_baby_index_visit");
            loadShare();
            //获取openId
            GetUserOpenId.save({},function (data) {
                if(data.openid == "none"){
                    //window.location.href = "http://s123.xiaork.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s123.xiaork.com/keeper/wechatInfo/getUserWechatMenId?url=37";
                }else{
                    $scope.openid = data.openid;
                    //获得奖品列表
                    GetUserPrizeList.save({},function (data) {
                        $scope.scroll = data.prizeList;
                        //prizeName
                    });

                    //获得参加游戏的人数
                    GetGameMemberNum.save({},function (data) {
                        $scope.headcount = data.gameMemberNum;
                    });
                    //获取游戏的状态
                    GetGameMemberStatus.save({openid:$scope.openid},function (data) {
                        $scope.attentionOrNot = data.gameAction;
                        $scope.gameLevel = data.gameLevel;
                        //获得积分
                        GetUserGameScore.save({openid:''+$scope.openid},function (data) {
                            $scope.score = data.gameScore;
                        });
                    });
                }
            });
        };
        var time = setInterval(function(){
            if($('#scroll_begin span').eq(0).text() != ''){
                ScrollImgLeft();
                clearTimeout(time);
            }
        },100);
        function ScrollImgLeft() {
            var speed = 50;
            var scroll_begin = document.getElementById("scroll_begin");
            var scroll_end = document.getElementById("scroll_end");
            var scroll_div = document.getElementById("scroll_div");
            scroll_end.innerHTML = scroll_begin.innerHTML;

            function Marquee() {
                if (scroll_end.offsetWidth - scroll_div.scrollLeft <= 0) {
                    scroll_div.scrollLeft -= scroll_begin.offsetWidth;
                } else {
                    scroll_div.scrollLeft++;
                }
            }
            var MyMar = setInterval(Marquee, speed);
        };
        $scope.bulletScreen= function(){
            $('#ruleBtn').bind('click', function() {
                $('#ruleBox').show();
            });
            $('#ruleBox').bind('click', function(e) {
                if (e.target.className != 'ruleBoxContent') {
                    $('#ruleBox').hide();
                }
            });
        };
        $scope.goFirstPass= function(){
            GetGamePlayingTimes.save({openid:$scope.openid,gameLevel:"1"},function (data) {
                $scope.playCount = data.gamePlayingTimes;
                if($scope.playCount < 3){
                    recordLogs("action_olympic_baby_once_visit");
                    $state.go("olympicGameLevel1",{playCount:$scope.playCount});
                }else{
                    $scope.withoutCount = true;
                }
            });
        };
        $scope.goSecondPass= function(){
            //获取玩游戏的次数
            GetGamePlayingTimes.save({openid:$scope.openid,gameLevel:"2"},function (data) {
                $scope.playCount = data.gamePlayingTimes;
                GetGameMemberStatus.save({openid:$scope.openid},function (data) {
                    $scope.attentionOrNot = data.gameAction;
                    if($scope.attentionOrNot == 1){
                        $scope.shareFloat = true;
                    }else if($scope.attentionOrNot == 2){
                        $state.go("olympicBabyInvitationCard",{});
                    }else if($scope.playCount < 3 && $scope.attentionOrNot == 0){
                        recordLogs("action_olympic_baby_tiwce_visit");
                        $state.go("olympicGameLevel2",{});
                    }else if($scope.playCount == 3){
                        $scope.withoutCount = true;
                    }
                });
            });
        };
        $scope.goThirdPass= function(){
            //获取玩游戏的次数
            GetGamePlayingTimes.save({openid:$scope.openid,gameLevel:"3"},function (data) {
                $scope.playCount = data.gamePlayingTimes;
                GetGameMemberStatus.save({openid:$scope.openid},function (data) {
                    $scope.attentionOrNot = data.gameAction;
                    if($scope.attentionOrNot == 1){
                        $scope.shareFloat = true;
                    }else if($scope.attentionOrNot == 2){
                        $state.go("olympicBabyInvitationCard",{});
                    }else if($scope.playCount < 3 && $scope.attentionOrNot == 0){
                        recordLogs("action_olympic_baby_thirth_visit");
                        $state.go("olympicGameLevel3",{});
                    }else if($scope.playCount == 3){
                        $scope.withoutCount = true;
                    }
                });
            });

        };
        $scope.cancelFloat = function(){
            $scope.shareFloat = false;
            $scope.withoutCount = false;
        };
        $scope.lottery = function(){
            recordLogs("action_olympic_baby_golottery");
            $state.go("olympicBabyDrawPrize",{});

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
                                    recordLogs("action_olympic_baby_index_share");
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
                                    recordLogs("action_olympic_baby_index_share");
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
