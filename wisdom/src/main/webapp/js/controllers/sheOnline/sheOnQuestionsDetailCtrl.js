﻿angular.module('controllers', ['ionic']).controller('sheOnQuestionsDetailCtrl', [
        '$scope','$state','$stateParams','GetArticleDetail','ArticleShareRecord',
        function ($scope,$state,$stateParams,GetArticleDetail,ArticleShareRecord) {

            $scope.doRefresh = function(){
                var webname="http://www.baodf.com";
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
                                    title: $stateParams.contentTitle+'-'+'【郑玉巧在线】', // 分享标题
                                    link: window.location.href.replace("true","false"), // 分享链接
                                    imgUrl: webname+'http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg', // 分享图标
                                    success: function (res) {
                                        //记录用户分享文章
                                        ArticleShareRecord.save({"title":"郑玉巧在线"},function(data){
                                        })
                                    },
                                    fail: function (res) {
                                    }
                                });

                                wx.onMenuShareAppMessage({
                                    title: $stateParams.contentTitle+'-'+'【郑玉巧在线】', // 分享标题
                                    desc: $scope.description, // 分享描述
                                    link: window.location.href.replace("true","false"), // 分享链接
                                    imgUrl: webname+'http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg', // 分享图标
                                    success: function (res) {
                                        //记录用户分享文章
                                        ArticleShareRecord.save({"title":"郑玉巧在线"},function(data){
                                        })
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

            $scope.$on('$ionicView.enter', function() {
                $scope.titleId = $stateParams.contentId;
                $scope.title = $stateParams.contentTitle;
                $scope.pageLoading=true;
                GetArticleDetail.save({"id":$scope.titleId},function(data){
                    $scope.pageLoading=false;
                });
            });
    }])