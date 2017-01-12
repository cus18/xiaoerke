angular.module('controllers', [])
    .controller('babyCoinInvitePageCtrl',['$scope','$state','GetAttentionInfo','GetUserOpenId','GetBabyCoinInfo','BabyCoinInit','GetConfig','$ionicScrollDelegate','$stateParams',
        function ($scope,$state,GetAttentionInfo,GetUserOpenId,GetBabyCoinInfo,BabyCoinInit,GetConfig,$ionicScrollDelegate,$stateParams) {
            $scope.minename = '您的朋友';
            $scope.openid = '';
            $scope.marketer = '';
            $scope.inviteUrlData = "";
            $scope.info = {
                flag:false
            };

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
                                        title: '在这里可以免费咨询三甲医院儿科专家', // 分享标题
                                        link: share, // 分享链接
                                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                        success: function (res) {
                                            recordLogs("ZXYQ_YQY_SHARE");
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

            };
        }])