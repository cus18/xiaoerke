angular.module('controllers', []).controller('olympicBabyInvitationCardCtrl', [
    '$scope','$state','$stateParams','GetInviteCard','GetUserOpenId','GetGameMemberStatus','$http',
    function ($scope,$state,$stateParams,GetInviteCard,GetUserOpenId,GetGameMemberStatus,$http) {
        document.title="我的邀请卡"; //修改页面title
        var imgList = [     "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao1.png",
                            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao2.png",
                            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao3.png",
                            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao4.png",
                            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao5.png"
                            // "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao6.png"
            ];


        //$scope.imgEwm = "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_erweima.png";

        //获取用户openid
        GetUserOpenId.get(function (data) {
            $scope.Refresh();
            if(data.openid!="none"){
                //获取用户邀请卡
                GetInviteCard.save({"openid":data.openid},function (data) {
                     $scope.imgEwm = data.path;
                 });
                /*GetGameMemberStatus.save({"openid":data.openid},function (data) {
                    console.log("data",data);
                   if(data.gameLevel<6){
                       $scope.imgIndex = imgList[parseInt(data.gameLevel)-1];
                   }else{
                       $scope.imgIndex = imgList[4];
                   }
                });*/

                if(parseInt($stateParams.gameLevel)==2){
                    $scope.imgIndex = imgList[0];
                }else if(parseInt($stateParams.gameLevel)==3){
                    $scope.imgIndex = imgList[1];
                }else if(parseInt($stateParams.gameLevel)==4){
                    $scope.imgIndex = imgList[2];
                }else if(parseInt($stateParams.gameLevel)==5){
                    $scope.imgIndex = imgList[3];
                }else if(parseInt($stateParams.gameLevel)==6){
                    $scope.imgIndex = imgList[4];
                }

            }else{
                alert("请从宝大夫公众号进入游戏，谢谢！");
            }
        });

        //记录日志
        var setLog = function (content) {
            var pData = {logContent:encodeURI(content)};
            $http({method:'post',url:'util/recordLogs',params:pData});
        }

        $scope.Refresh = function(){
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
                                link: share, // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/common/sharePic.png', // 分享图标
                                success: function (res) {
                                    setLog("action_olympic_baby_share");
                                },
                                fail: function (res) {
                                }
                            });

                            wx.onMenuShareAppMessage({
                                title: '赢个大奖居然这么简单……', // 分享标题
                                desc: '宝宝奥运大闯关”开始啦！玩游戏闯关卡，赢取超值豪礼！我已加入，你也赶紧一起来参与吧！', // 分享描述
                                link: share, // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/common/sharePic.png', // 分享图标
                                success: function (res) {
                                    setLog("action_olympic_baby_share");
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
        

        
        
    }]);
