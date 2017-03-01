angular.module('controllers', ['ionic','ngDialog']).controller('homeCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog','GetCardInfoList','GetConfig','RedPacketCreate',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog,GetCardInfoList,GetConfig,RedPacketCreate) {

        //分享到朋友圈或者微信
        var loadShare = function(){
            // redPacketCreate.save({"uuid":$scope.uuid},function (data) {
            //     $scope.uuid = data.uuid;
            // });
            GetConfig.save({}, function (data) {
                $scope.inviteUrlData = data.publicSystemInfo.redPackageShareUrl;

                RedPacketCreate.save({"uuid":$scope.uuid},function (data) {
                    $scope.uuid = data.uuid;
                    var share = $scope.inviteUrlData + $scope.openid+","+$scope.market+","+ $scope.uuid+",";//最后url=41，openid,marketer

                    // var share = $scope.inviteUrlData + $scope.openid+","+$scope.marketer+","+ $scope.uuid+",";//最后url=41，openid,marketer
                    // if(version=="a"){
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
                                        'onMenuShareAppMessage',
                                        'previewImage'
                                    ] // 功能列表
                                });
                                wx.ready(function () {
                                    // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                                    wx.onMenuShareTimeline({
                                        title: '比比运气，大波红包等你抢~在这里可以免费咨询三甲医院儿科专家', // 分享标题
                                        link: share, // 分享链接
                                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                            success: function (res) {
                                            recordLogs("ZXYQ_YQY_SHARE");
                                            // redPacketCreate.save({"uuid":$scope.uuid},function (data) {
                                            // });
                                        },
                                        fail: function (res) {
                                        }
                                    });
                                    wx.onMenuShareAppMessage({
                                        title: $scope.minename  + '向你推荐', // 分享标题
                                        desc: '比比运气，大波红包等你抢~在这里可以免费咨询三甲医院儿科专家', // 分享描述
                                        link: share, // 分享链接
                                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                        success: function (res) {
                                            recordLogs("ZXYQ_YQY_SHARE");
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
                })
            });
        };


        //获取基本信息接口
        GetCardInfoList.save({},function(res){
            $scope.card=res.cardInfoVo;
            $scope.openid=res.cardInfoVo.openid;
            $scope.market=res.cardInfoVo.market;
            loadShare();
        })









        /*查看我的奖品*/
        $scope.lookPrize=function(){
            $state.go('prizeList')
        }
        //跳转我的卡片
        $scope.lookMyCard=function(){
            $state.go('myCard')
        }
        //查看规则
        $scope.lookRules=function(){
            ngDialog.open({
                template: 'rules',
                scope:$scope,//这样就可以景星传递参数
                controller: ['$scope', '$interval', function ($scope, $interval) {}],
                className: 'ngdialog-theme-default ngdialog-theme-custom',
                width:'11.797333rem',
                height:'15.317333rem'
            });
        }
        //分享
        $scope.share_status=false;
        $scope.share=function(){
            $scope.share_status=true;
        }
        $scope.share_close=function(){
            $scope.share_status=false;
        }





    }])
