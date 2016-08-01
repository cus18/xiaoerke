
define(['appOlympicBaby'], function (app) {
    app
        .factory('GetGameMemberNum',['$resource',function ($resource){
            /**
             * 获取参加奥运宝宝游戏的总人数
             * inputParam: {}
             * resultParam: {gameMemberNum:8888}
             ***/
            return $resource('olympicBaby/firstPage/GetGameMemberNum');
        }])
        .factory('GetGameMemberStatus',['$resource',function ($resource){
            /**
             * 获取参加用户目前所处的游戏关数,已经要开启下一关，所要进行的操作
             * input: {openid:"fwefwefewfw"}
             * result: {gameLevel:2,gameAction:1，needInviteFriendNum:3,}
             * gameLevel 表示当前所处的管卡,0表示用户处于通关的状态
             * gameAction 1表示需要邀请用户关注，2表示需要邀请好友
             * needInviteFriendNum 表示还需要邀请加入的好友数
             ***/
            return $resource('olympicBaby/firstPage/GetGameMemberNum');
        }])
        .factory('GetGameMemberStatus',['$resource',function ($resource){
            /**
             * 获取参加用户目前所处的游戏关数,已经要开启下一关，所要进行的操作
             * input: {openid:"fwefwefewfw"}
             * result: {gameLevel:2,gameAction:1，needInviteFriendNum:3,}
             * gameLevel表示当前所处的管卡
             * gameAction 1表示需要邀请用户关注，2表示需要邀请好友
             * needInviteFriendNum 表示还需要邀请加入的好友数
             ***/
            return $resource('olympicBaby/firstPage/GetGameMemberNum');
        }])
        .factory('GetFirstPageSlideMessage',['$resource',function ($resource){
            /**
             * 获取奖品领取的轮播图消息
             * result: {messageList:[{userName:孙晓,Content:"获得了微波炉大奖"},{userName:孙晓,Content:"获得了微波炉大奖"},
             * {userName:孙晓,Content:"获得了微波炉大奖"},{userName:孙晓,Content:"获得了微波炉大奖"},
             * {userName:孙晓,Content:"获得了微波炉大奖"}]}
             ***/
            return $resource('olympicBaby/firstPage/GetFirstPageSlideMessage');
        }])
        .factory('GetUserGameScore',['$resource',function ($resource){
            /**
             * 获取用户的游戏积分
             * input: {openid:"fwefwefewfw"}
             * result: {gameScore:8888}
             ***/
            return $resource('olympicBaby/firstPage/GetUserGameScore');
        }])




})
