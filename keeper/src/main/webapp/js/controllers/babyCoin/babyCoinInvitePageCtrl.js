angular.module('controllers', [])
    .controller('babyCoinInvitePageCtrl',['$scope','$state','GetAttentionInfo','GetUserOpenId','GetBabyCoinInfo','BabyCoinInit','GetConfig','$ionicScrollDelegate','$stateParams','redPacketCreate',
        function ($scope,$state,GetAttentionInfo,GetUserOpenId,GetBabyCoinInfo,BabyCoinInit,GetConfig,$ionicScrollDelegate,$stateParams,redPacketCreate) {
            $scope.minename = '您的朋友';
            $scope.openid = '';
            $scope.marketer = '';
            $scope.inviteUrlData = "";
            $scope.uuid = "";
            $scope.info = {
                flag:false
            };
            loadShare();
            if($stateParams.isShow != ''){
                $ionicScrollDelegate.scrollTop();
                $scope.info.flag = true;
            }
            //获取页面参数值
            // function GetQueryString(name)
            // {
            //     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            //     var r = window.location.search.substr(1).match(reg);
            //     if(r!=null)return  unescape(r[2]); return null;
            // }
            //
            // var a = GetQueryString("aa");

            $scope.invitePageInit = function(){
                BabyCoinInit.save({},function(data){
                    $scope.openid = data.babyCoinVo.openId;
                    $scope.marketer = data.babyCoinVo.marketer ;
                    $scope.minename = data.babyCoinVo.nickName;
                    if($scope.minename == undefined || $scope.minename==''){
                        $scope.minename = '您的朋友';
                    }
                    loadShare();
                });
                recordLogs("ZXYQ_YQY");
            };
            $scope.showFlag = function () {
                $ionicScrollDelegate.scrollTop();
                $scope.info.flag = true;
            };
            $scope.hideFlag = function () {
                $scope.info.flag = false;
            };
            $scope.goCoupon = function () {
                recordLogs("ZUYQ_YQY_YHQDH");
                $state.go("babyCoinTicketList");
            };
            $scope.goStore = function () {
                recordLogs("ZXYQ_YQY_QWSC");
                window.location.href = "https://h5.koudaitong.com/v2/showcase/homepage?kdt_id=17783033&redirect_count=1";
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
                $scope.uuid = uuid();
                // redPacketCreate.save({"uuid":$scope.uuid},function (data) {
                //     $scope.uuid = data.uuid;
                // });
                GetConfig.save({}, function (data) {
                    $scope.inviteUrlData = data.publicSystemInfo.inviteUrl;

                    redPacketCreate.save({"uuid":$scope.uuid},function (data) {
                        $scope.uuid = data.uuid;
                        var share = $scope.inviteUrlData + $scope.openid+","+$scope.marketer+","+ $scope.uuid+",";//最后url=41，openid,marketer

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

            function uuid() {
                var s = [];
                var hexDigits = "0123456789abcdef";
                for (var i = 0; i < 36; i++) {
                    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
                }
                s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
                s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
                s[8] = s[13] = s[18] = s[23] = "-";

                var uuid = s.join("");
                return uuid;
            }
        }])