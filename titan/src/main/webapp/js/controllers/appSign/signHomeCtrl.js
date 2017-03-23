angular.module('controllers', ['ionic','ngDialog']).controller('signHomeCtrl', [
    '$scope','$state','$stateParams',
    'OrderPayMemberServiceOperation','GetUserMemberService','$ionicScrollDelegate','$location','ngDialog','GetPunchCardPage','TakePunchCardActivity','PayPunchCardCash','GetConfig',
    function ($scope,$state,$stateParams,OrderPayMemberServiceOperation,GetUserMemberService,$ionicScrollDelegate,$location,ngDialog,GetPunchCardPage,TakePunchCardActivity,PayPunchCardCash,GetConfig) {
        //前往个人中心
        $scope.goCenter=function(){
            $state.go('signRecord')
        }
        //参加打卡按钮的状态
        $scope.goJoinStatus=false;
        //我要打卡按钮的状态
        $scope.goSignStatus=false;




        //启动状态判断
        $scope.start_status=false;
        //支付状态判断
        $scope.pay_status=false;
        //打卡时间提示状态
        $scope.time_status=false;
        //打卡成功提示状态
        $scope.sign_status=false;
        //分享弹窗状态
        $scope.share_status=false;

        if($stateParams.id=='false'){
            $scope.sign_status=false;
        }else if($stateParams.id=='true'){
            $scope.sign_status=true;
        }
        //加入的点击事件
        $scope.goJoin=function(){
            $ionicScrollDelegate.scrollTop();
            $scope.start_status=true;
        }
        //支付按钮点击事件
        $scope.goPay=function(){
            $ionicScrollDelegate.scrollTop();
            window.location.href='http://s201.xiaork.com/keeper/wxPay/patientPay.do?serviceType=payPunchCardCash';
        }
        //喊朋友一起来参加
        $scope.goShare=function(){
            $ionicScrollDelegate.scrollTop();
            $scope.share_status=true;
            //启动状态判断
            $scope.start_status=false;
            //支付状态判断
            $scope.pay_status=false;
            //打卡时间提示状态
            $scope.time_status=false;
            //打卡成功提示状态
            $scope.sign_status=false;
        }
        //关闭分享弹窗
        $scope.close_share=function(){
            $scope.share_status=false;
        }

        //我要打卡
        $scope.goSign=function(){
            TakePunchCardActivity.save({openId:$scope.openId},function(res){
                if(res.status=="failure"){
                    alert('打卡失败')
                }else{
                    $scope.sign_status=true;
                }
            })
        }
        //关闭按钮
        $scope.close=function(){
            //启动状态判断
            $scope.start_status=false;
            //支付状态判断
            $scope.pay_status=false;
            //打卡时间提示状态
            $scope.time_status=false;
            //打卡成功提示状态
            $scope.sign_status=false;
        }

        //查看更多记录
        $scope.lookMore=function(){

        }
        GetPunchCardPage.save({},function(res){
            if(res.resultCode==9999){
                alert('服务器错误')
            }else{
                $scope.oData=res;
                $scope.openId=res.openId;
                $scope.market=res.market;
                if(res.isOrNotPay=='no'){
                    $scope.goJoinStatus=true;
                    $scope.goSignStatus=false;
                }else if(res.isOrNotPay=='yes'){
                    $scope.goJoinStatus=false;
                    $scope.goSignStatus=true;
                }
                $scope.doRefresh();
            }
        })
        //初始化微信
         $scope.doRefresh = function () {
            GetConfig.save({}, function (data) {
                $scope.inviteUrlData = data.publicSystemInfo.redPackageShareUrl;
                var share = $scope.inviteUrlData + $scope.openid + "," + $scope.market + ",";//最后url=41，openid,marketer

                // var share = $scope.inviteUrlData + $scope.openid+","+$scope.marketer+","+ $scope.uuid+",";//最后url=41，openid,marketer
                // if(version=="a"){
                version = "a";
                var timestamp;//时间戳
                var nonceStr;//随机字符串
                var signature;//得到的签名
                var appid;//得到的签名
                $.ajax({
                    url: "wechatInfo/getConfig",// 跳转到 action
                    async: true,
                    type: 'get',
                    data: {url: location.href.split('#')[0]},//得到需要分享页面的url
                    cache: false,
                    dataType: 'json',
                    success: function (data) {
                        if (data != null) {
                            timestamp = data.timestamp;//得到时间戳
                            nonceStr = data.nonceStr;//得到随机字符串
                            signature = data.signature;//得到签名
                            appid = data.appid;//appid
                            //微信配置
                            wx.config({
                                debug: false,
                                appId: appid,
                                timestamp: timestamp,
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
                                    title: '感恩妈妈节，在这里可以免费咨询三甲医院儿科专家,还有机会赢现金大礼', // 分享标题
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                    success: function (res) {
                                        //recordLogs("ZXYQ_YQY_SHARE");
                                        // redPacketCreate.save({"uuid":$scope.uuid},function (data) {
                                        // });
                                    },
                                    fail: function (res) {

                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title: $scope.minename + '向你推荐', // 分享标题
                                    desc: '感恩妈妈节，在这里可以免费咨询三甲医院儿科专家,还有机会赢现金大礼 ',// 分享描述
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                    success: function (res) {
                                    },
                                    fail: function (res) {
                                    }
                                });
                            })
                        } else {
                        }
                    },
                    error: function () {
                    }
                });

            });
        }
    }])