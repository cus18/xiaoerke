angular.module('controllers', [])
    .controller('babyCoinInviteOldCtrl',
    ['$scope','$state','CreateInviteCard','$stateParams','GetConfig','$ionicScrollDelegate','redPacketShare',
        function ($scope,$state,CreateInviteCard,$stateParams,GetConfig,$ionicScrollDelegate,redPacketShare) {

            $scope.marketer = $stateParams.marketer;
            $scope.oldOpenId = $stateParams.oldOpenId;
            $scope.minename = '您的朋友';
            $scope.openid = $stateParams.oldOpenId;
            $scope.redPacketId = $stateParams.redPacketId;
            $scope.inviteUrlData = "";
            $scope.info = {
                share:false,
                goConsult:false
            };
            $scope.receiveLock = false;//未领取
            // 判断用户是否领了该红包
            //该红包是否可领
            redPacketShare.save({redPacketId:$scope.redPacketId},function (data) {
                console.log(data);
                // 已经领取
                if(data.packetstatus == "receive"){
                    $scope.receiveLock = true;//未领取
                    $scope.balance = data.balance;//领取金额
                    $scope.recordVoList = data.recordVoList;//红包分配情况
                }else if(data.packetstatus == "share"){
                    $scope.receiveLock = true;//未领取
                    $scope.balance = data.balance;//领取金额
                    $scope.recordVoList = data.recordVoList;//红包分配情况
                }else if(data.packetstatus == "isend"){
                    $scope.receiveLock = false;//未领取
                    $scope.recordVoList = data.recordVoList;//红包分配情况
                }

            })

            /*好友领取宝宝币信息*/
            $scope.friendsData=[
                {
                    name:"王某某",
                    coin:"50",
                    date:"2016/03/02",
                    headPic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg"
                },
                {
                    name:"陈某某",
                    coin:"50",
                    date:"2016/04/02",
                    headPic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg"
                },
                {
                    name:"赵某某",
                    coin:"55",
                    date:"2016/06/02",
                    headPic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg"
                },
                {
                    name:"王某某",
                    coin:"350",
                    date:"2017/01/02",
                    headPic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg"
                },
                {
                    name:"李某某",
                    coin:"150",
                    date:"2017/01/31",
                    headPic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg"
                },
                {
                    name:"张某某",
                    coin:"10",
                    date:"2016/03/30",
                    headPic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg"
                },
            ];
            /*点击最下面 立即使用 按钮*/
            $scope.goUse = function () {
                $state.go("babyCoinTicketList");
            };
            /*点击最下面 我来发红包 按钮*/
            $scope.sendRedPackets = function () {
                $state.go("babyCoinInvitePage");
            };
            $scope.invitePageInit = function () {
                CreateInviteCard.save({"marketer":$scope.marketer,"oldOpenId":$scope.oldOpenId}, function (data) {
                    $scope.headImgUrl = {
                        'background': 'url(' + data.headImgUrl + ')',
                        'background-size': '100% 100%'
                    };
                    $scope.headImgNickName = data.babyCoinVo.nickName;
                    /*$scope.userQRCode = {
                     'background': 'url(' + data.userQRCode + ')',
                     'background-size': '100% 100%'
                     };*/
                    $scope.minename = $scope.headImgNickName;
                    if($scope.minename == undefined || $scope.minename==''){
                        $scope.minename = '您的朋友';
                    }
                    $scope.userQRCode = data.userQRCode;
                    loadShare();
                });
            };
            $scope.showGoConsult = function(){
                recordLogs("ZXYQ_YQK_ZXYS");
                $scope.info.goConsult = true;
            };
            $scope.hideGoConsult = function () {
                $scope.info.goConsult = false;
            };
            $scope.showShare = function(){
                recordLogs("ZXYQ_YQK_ZXYS");
                $scope.info.share = true;
            };
            $scope.hideShare = function () {
                $scope.info.share = false;
            };
            $scope.linkInvitePage = function () {
                recordLogs("ZXYQ_YQK_YQGZ");
                $state.go("babyCoinInvitePage");
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
                GetConfig.save({}, function (data) {
                    $scope.inviteUrlData = data.publicSystemInfo.inviteUrl;
                    var share = $scope.inviteUrlData + $scope.openid+","+$scope.marketer+",";//最后url=41，openid,marketer
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
                                        title:  '在这里可以免费咨询三甲医院儿科专家', // 分享标题
                                        link: share, // 分享链接
                                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                        success: function (res) {
                                            recordLogs("ZXYQ_YQK_OLD_SHARE");
                                        },
                                        fail: function (res) {
                                        }
                                    });
                                    wx.onMenuShareAppMessage({
                                        title: $scope.minename  + '向你推荐', // 分享标题
                                        desc: '在这里可以免费咨询三甲医院儿科专家', // 分享描述
                                        link: share, // 分享链接
                                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                        success: function (res) {
                                            recordLogs("ZXYQ_YQK_OLD_SHARE");
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

            };

        }])