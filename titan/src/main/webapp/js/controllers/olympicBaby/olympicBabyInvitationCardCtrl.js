angular.module('controllers', []).controller('olympicBabyInvitationCardCtrl', [
    '$scope','$state','GetInviteCard','GetUserOpenId','GetGameMemberStatus',
    function ($scope,$state,GetInviteCard,GetUserOpenId,GetGameMemberStatus) {
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
            if(data.openid!="none"){
                //获取用户邀请卡
                GetInviteCard.save({"openid":data.openid},function (data) {
                     $scope.imgEwm = data.path;
                 });
                GetGameMemberStatus.get({"openid":data.openid},function (data) {
                    console.log("data",data);
                   if(data.gameLevel<6){
                       $scope.imgIndex = imgList[parseInt(data.gameLevel)-1];
                   }else{
                       $scope.imgIndex = imgList[4];
                   }
                });
            }else{
                alert("请从宝大夫公众号进入游戏，谢谢！");
            }
        })
        

        
        
    }]);
