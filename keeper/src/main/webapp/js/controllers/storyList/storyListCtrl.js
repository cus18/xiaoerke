angular.module('controllers', [])
    .controller('storyListCtrl',
    ['$scope','GetConfig',
        function ($scope,GetConfig) {
            var imgList = [
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_01.jpg",
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_02.jpg",
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_03.jpg",
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_04.jpg",
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_05.jpg",
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_06.jpg"
            ];
            $scope.storyListInit = function () {
                loadShare();
            };
            $scope.previewImage = function (index) {
                var img = imgList[index];
                wx.previewImage({
                    current: img, // 当前显示图片的http链接
                    urls:imgList  // 需要预览的图片http链接列表
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
                                        title:  $scope.minename+'在这里可以免费咨询三甲医院儿科专家', // 分享标题
                                        link: share, // 分享链接
                                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                        success: function (res) {
                                            recordLogs("ZXYQ_YQK_NEW_SHARE");
                                        },
                                        fail: function (res) {
                                        }
                                    });
                                    wx.onMenuShareAppMessage({
                                        title: $scope.minename  + '向你推荐', // 分享标题
                                        desc: '和三甲医院儿科专家来一对一专业咨询吧！', // 分享描述
                                        link: share, // 分享链接
                                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                        success: function (res) {
                                            recordLogs("ZXYQ_YQK_NEW_SHARE");
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
                });

            };
        }])