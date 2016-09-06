angular.module('controllers', [])
    .controller('patientConsultInvitePageCtrl',['$scope',
        function ($scope,ConsultCustomOnly) {
            $scope.invitePageInit = function(){
                loadShare();
            }
            //分享到朋友圈或者微信
            var loadShare = function(){
                var share = 'http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=32';
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
                                    title: '我已为蛋蛋进行了公益捐赠，捐款和转发都是献爱心', // 分享标题
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Faxjz.jpg', // 分享图标
                                    success: function (res) {
                                        recordLogs("AXJZ_HDSY_FXPYQ");
                                    },
                                    fail: function (res) {
                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title: '我已为蛋蛋进行了公益捐赠，捐款和转发都是献爱心', // 分享标题
                                    desc: '蛋蛋正在接受化疗，你的一个小小善举，就能挽救一个鲜活生命！', // 分享描述
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Faxjz.jpg', // 分享图标
                                    success: function (res) {
                                        recordLogs("AXJZ_HDSY_FXPY");
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
            };
        }])