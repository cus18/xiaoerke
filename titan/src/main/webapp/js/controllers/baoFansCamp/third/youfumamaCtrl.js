angular.module('controllers', ['ionic']).controller('youFuMamaCtrl', [
        '$scope','$state','$ionicScrollDelegate','RecordLogs','$http',
        function ($scope,$state,$ionicScrollDelegate,RecordLogs,$http) {
            $scope.title0 = "宝大夫健康咨询";
            $scope.shareLock=false;

            $scope.share=function(){
                $scope.shareLock=true;
            };
            $scope.cancel=function(){
                $scope.shareLock=false;
            };



            $scope.doRefresh = function(){
                var  host2=document.domain;
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
                                    title: '还在为宝宝各种育儿健康问题发愁吗？赶紧点击链接免费咨询一下专家吧！', // 分享标题
                                    link: window.location.href.replace("true","false"), // 分享链接
                                    imgUrl: 'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/third%2Fwechat.png', // 分享图标
                                    success: function (res) {
                                        //记录用户分享文章
                                        var pData = {logContent:encodeURI("YFMM_Moments")};
                                        $http({method:'post',url:'util/recordLogs',params:pData});
                                    },
                                    fail: function (res) {
                                    }
                                });

                                wx.onMenuShareAppMessage({
                                    title: '还在为宝宝各种育儿健康问题发愁吗？赶紧点击链接免费咨询一下专家吧！', // 分享标题
                                    desc: '还在为宝宝各种育儿健康问题发愁吗？赶紧点击链接免费咨询一下专家吧！', // 分享描述
                                    link:window.location.href.replace("true","false"), // 分享链接
                                    imgUrl: 'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/third%2Fwechat.png', // 分享图标
                                    success: function (res) {
                                        var pData = {logContent:encodeURI("YFMM_Friends")};
                                        $http({method:'post',url:'util/recordLogs',params:pData});
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

    }])
