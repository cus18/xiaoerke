angular.module('controllers', ['ionic']).controller('consultShareCtrl', [
        '$scope','$state','$ionicScrollDelegate','RecordLogs',
        function ($scope,$state,$ionicScrollDelegate,RecordLogs) {
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.shareLock=false;

            $scope.share=function(){
                $scope.shareLock=true;
                $ionicScrollDelegate.scrollTop();
            }
            $scope.cancel=function(){
                $scope.shareLock=false;
            }

            $scope.doRefresh = function(){
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
                                    title: '不敢相信，微信竟然能免费咨询北京三甲医院的儿科专家！回答的太详细了！',
                                    link: window.location.href,
                                    imgUrl: 'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg',
                                    success: function (res) {
                                        RecordLogs.get({logContent:encodeURI("评价分享成功")},function(){})
                                    },
                                    fail: function (res) {
                                        alert(JSON.stringify(res));
                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title:'你知道吗?微信竟然能免费咨询北京三甲医院的儿科专家',
                                    desc: '还在为宝宝各种育儿健康问题发愁吗?赶紧点击链接免费咨询一下专家吧!',
                                    link: window.location.href,
                                    imgUrl: 'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg',
                                    success: function (res) {
                                        RecordLogs.save({logContent:"评价分享成功"},function(){})
                                    },
                                    fail: function (res) {
                                        alert(JSON.stringify(res));
                                    }
                                });
                            })
                        }
                        else{
                        }
                    },
                    error : function() {
                    }
                });
                RecordLogs.save({logContent:encodeURI("评价分享")},function(){})
            }

    }])
