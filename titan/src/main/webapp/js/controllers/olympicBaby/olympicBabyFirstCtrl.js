angular.module('controllers', []).controller('olympicBabyFirstCtrl', [
        '$scope','$state','$timeout','GetUserPrizeList','GetGameMemberNum','GetGamePlayingTimes','GetUserOpenId','GetUserGameScore',
        function ($scope,$state,$timeout,GetUserPrizeList,GetGameMemberNum,GetGamePlayingTimes,GetUserOpenId,GetUserGameScore) {
            $scope.title = "奥运宝贝-首页";
            $scope.headcount = 0;//参与游戏的总人数
            $scope.shareFloat = false;
            $scope.openid = '';
            $scope.invitePeopleNum = 0;//邀请成功的人数
            $scope.attentionOrNot = false;//是否关注宝大夫
            $scope.withoutCount = false;//游戏次数用完了
            $scope.playCount = 3;//游戏次数
            $scope.score = 300;//游戏积分
            $scope.scroll = [];
            $scope.scroll = [
                {
                    url:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/branchAnswer%2Frentou.png',
                    nickName:'西瓜啊西瓜中了保护伞'
                },{
                    url:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/branchAnswer%2Frentou.png',
                    nickName:'里哈米星人中了电烤箱'
                },{
                    url:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/branchAnswer%2Frentou.png',
                    nickName:'超级大梅梅中了爱奇艺会员'
                },{
                    url:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/branchAnswer%2Frentou.png',
                    nickName:'大熊猫中了微波炉'
                },{
                    url:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/branchAnswer%2Frentou.png',
                    nickName:'陆州桥中了九阳豆浆机'
                }
            ];
            $scope.olympicBabyFirstInit = function(){
                //获取openId
                /** $.ajax({
                 url:"umbrella/getOpenid",// 跳转到 action
                 async:true,
                 type:'post',
                 cache:false,
                 dataType:'json',
                 success:function(data) {
                 if(data.openid=="none"){
                 window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=37";
                 }
                 },
                 error : function() {
                 }
                 });*/
                //获得奖品列表
                GetUserPrizeList.save({},function (data) {
                    console.log(data);
                    $scope.scroll = data;
                });
                //获得积分
                GetUserGameScore.save({},function (data) {
                    console.log(data);
                    $scope.score = data.gameScore;
                });
                //获得参加游戏的人数
                GetGameMemberNum.save({},function (data) {
                    console.log(data.gameMemberNum);
                    $scope.headcount = data.gameMemberNum;
                });
                //获取openId
                GetUserOpenId.save({},function (data) {
                    console.log(data.openid);
                    $scope.openid = data.openid;
                });

            };
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
                ScrollImgLeft();
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
                GetGamePlayingTimes.get({openid:$scope.openid,gameLevel:num},function (data) {
                    console.log(data);
                    $scope.playCount = data;
                });
            };
            $scope.goFirstPass= function(){
                getGamePlayingTimes(1);
                if($scope.playCount > 0){
                    $state.go("olympicGameLevel1",{playCount:$scope.playCount});
                }else{
                    $scope.withoutCount = true;
                }
            };
            $scope.goSecondPass= function(){
                getGamePlayingTimes(2);
                if($scope.attentionOrNot){
                    if($scope.invitePeopleNum >= 1){
                        if($scope.playCount > 0){
                            $state.go("olympicGameLevel2",{});
                        }else{
                            $scope.withoutCount = true;
                        }
                    }else{
                        $state.go("olympicBabyInvitationCard",{});

                    }
                }else {
                    $scope.shareFloat = true;
                }
            };
            $scope.goThirdPass= function(){
                getGamePlayingTimes(3);
                if($scope.attentionOrNot){
                    if($scope.invitePeopleNum >= 3){
                        if($scope.playCount > 0){
                            $state.go("olympicGameLevel3",{});
                        }else{
                            $scope.withoutCount = true;
                        }
                    }else{
                        $state.go("olympicBabyInvitationCard",{});

                    }
                }else {
                    $scope.shareFloat = true;
                }
            };
            $scope.cancelFloat = function(){
                $scope.shareFloat = false;
                $scope.withoutCount = false;
            };
            $scope.lottery = function(){
                $state.go("olympicBabyDrawPrize",{});

            };
        }])
