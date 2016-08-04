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
            $scope.playCount = 3;//游戏次数
            $scope.score = 300;//游戏积分
            $scope.scroll = [];
            $scope.olympicBabyFirstInit = function(){
                recordLogs("action_olympic_baby_index_visit");
                    //获取openId
                GetUserOpenId.save({},function (data) {
                    if(data.openid == "none"){
                        window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=37";
                    }else{
                        $scope.openid = data.openid;
                        //获得奖品列表
                        GetUserPrizeList.save({},function (data) {
                            $scope.scroll = data.prizeList;
                            //prizeName
                        });
                        //获得积分
                        GetUserGameScore.save({openid:''+$scope.openid},function (data) {
                            $scope.score = data.gameScore;
                        });
                        //获得参加游戏的人数
                        GetGameMemberNum.save({},function (data) {
                            $scope.headcount = data.gameMemberNum;
                        });
                        //获取游戏的状态
                        GetGameMemberStatus.get({openid:$scope.openid},function (data) {
                            $scope.attentionOrNot = data.gameAction;
                            $scope.gameLevel = data.gameLevel;
                        });
                    }
                });
            };
            var time = setInterval(function(){
                if($('.bulletText').eq(3).text() != ''){
                    ScrollImgLeft();
                    clearTimeout(time);
                }
            },100)
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
            var getGamePlayingTimes= function(num){
                //获取玩游戏的次数
                GetGamePlayingTimes.save({openid:$scope.openid,gameLevel:num},function (data) {
                    $scope.playCount = data;
                });
                GetGameMemberStatus.save({openid:$scope.openid,gameLevel:num},function (data) {
                    $scope.attentionOrNot = data;
                });
            };

            $scope.goFirstPass= function(){
                getGamePlayingTimes(1);
                if($scope.playCount < 3){
                    recordLogs("action_olympic_baby_once_visit");
                    $state.go("olympicGameLevel1",{playCount:$scope.playCount});
                }else{
                    $scope.withoutCount = true;
                }
            };
            $scope.goSecondPass= function(){
                getGamePlayingTimes(2);
                if($scope.attentionOrNot == 1){
                    $scope.shareFloat = true;
                }else if($scope.attentionOrNot == 2){
                    $state.go("olympicBabyInvitationCard",{});
                }else if($scope.playCount > 0){
                    recordLogs("action_olympic_baby_tiwce_visit");
                    $state.go("olympicGameLevel2",{});
                }else{
                    $scope.withoutCount = true;
                }
            };
            $scope.goThirdPass= function(){
                getGamePlayingTimes(3);
                if($scope.attentionOrNot == 1){
                    $scope.shareFloat = true;
                }else if($scope.attentionOrNot == 2){
                    $state.go("olympicBabyInvitationCard",{});
                }else if($scope.playCount > 3){
                    recordLogs("action_olympic_baby_thirth_visit");
                    $state.go("olympicGameLevel3",{});
                }else{
                    $scope.withoutCount = true;
                }
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
        }])
