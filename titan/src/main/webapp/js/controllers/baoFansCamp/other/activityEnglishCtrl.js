angular.module('controllers', ['ionic']).controller('activityEnglishCtrl', [
        '$scope','$state','$stateParams','Englisactivity',
        function ($scope,$state,$stateParams,Englisactivity) {


            /*调用微信分享*/
            $scope.loadShare=function() {
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
                                    title: '', // 分享标题
                                    link:  "",
                                    imgUrl: '', // 分享图标
                                    success: function (res) {

                                    },

                                    fail: function (res) {

                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title: '', // 分享标题
                                    desc: "", // 分享描述
                                    link:  "", // 分享链接
                                    imgUrl: '', // 分享图标
                                    success: function (res) {

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


            $scope.$on('$ionicView.enter', function(){
                $scope.loadShare();
                Englisactivity.save({},function (data) {
                    $scope.market = data.market;
                    $scope.userQRCode = data.userQRCode;
                })
            })
    }])