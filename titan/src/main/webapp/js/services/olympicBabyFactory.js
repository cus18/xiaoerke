
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
             * gameAction 1表示需要关注，2表示需要邀请好友
             * needInviteFriendNum 表示还需要邀请加入的好友数
             ***/
            return $resource('olympicBaby/firstPage/GetGameMemberStatus');
        }])
        .factory('GetUserPrizeList',['$resource',function ($resource){
            /**
             * 获取奖品领取的轮播图消息
             * result: {messageList:[{userName:孙晓,Content:"获得了微波炉大奖"},{userName:孙大奖"},晓,Content:"获得了微波炉大奖"},
             * {userName:孙晓,Content:"获得了微波炉大奖"},{userName:孙晓,Content:"获得了微波炉
             * {userName:孙晓,Content:"获得了微波炉大奖"}]}
             ***/
            return $resource('olympicBaby/gameScore/GetUserPrizeList');
        }])
        .factory('GetUserGameScore',['$resource',function ($resource){
            /**
             * 获取用户的游戏积分
             * input: {openid:"fwefwefewfw"}
             * result: {gameScore:8888}
             ***/
            return $resource('olympicBaby/firstPage/GetUserGameScore');
        }])
        .factory('GetUserOpenId',['$resource',function ($resource){
            /**
             * 获取用户openid
             * result: 没有openid的返回值{ openid=none}
             ***/
            return $resource('util/getOpenid');
        }])
        .factory('SaveGameScore',['$resource',function ($resource){
            /**
             * 将某关的游戏积分存入后台
             * input:{openid:"fwefewfewf",gameLevel:3,gameScore:80}
             * result: {result:"success"}
             ***/
            return $resource('olympicBaby/gameScore/SaveGameScore');
        }])
        .factory('GetGamePlayingTimes',['$resource',function ($resource){
            /**
             * 获取某个游戏玩的次数
             * input:{openid:"fwefewfewf",gameLevel:3}
             * result: {gamePlayingTimes:2}
             ***/
            return $resource('olympicBaby/gameScore/GetGamePlayingTimes');
        }])
        .factory('GetGameScorePrize',['$resource',function ($resource){
            /**
             * 根据积分，进行奖品抽奖
             * input:{openid:"fwefewfewf"}
             * result: {leftTimes:2，prizeInfo:[{name:"电饭煲"},{describe:"电饭煲"},{XXX:"电饭煲"}]}
             * leftTimes为剩余的抽奖次数，如果为-1，表示积分不够抽奖，抽奖失败
             ***/
            return $resource('olympicBaby/gameScore/GetGameScorePrize');
        }])
        .factory('GetUserPrizes',['$resource',function ($resource){
            /**
             * 获取用户抽到的奖品列表
             * input:{openid:"fwefewfewf"}
             * result: {prizeList:[{prizeName:"电饭煲",XXX:"XXXXX"},{prizeName:"电饭煲"},{prizeName:"电饭煲"}]}
             ***/
            return $resource('olympicBaby/gameScore/GetUserPrizes');
        }])
        .factory('SaveUserAddress',['$resource',function ($resource){
            /**
             * 保存用户领取奖品的地址
             * input:{openid:"fwefewfewf"}
             * result: {addressName:"海淀区",code:"100053","phone":"13601025662","userName":"赵得良"}
             ***/
            return $resource('olympicBaby/gameScore/SaveUserAddress');
        }])
        .factory('GetInviteCard',['$resource',function ($resource){
            /**
             * 生成邀请卡
             * * input:{openid:"fwefewfewf"}
             * result: {path:图片地址}
             ***/
            return $resource('olympicBaby/GetInviteCard');
        }])



})
