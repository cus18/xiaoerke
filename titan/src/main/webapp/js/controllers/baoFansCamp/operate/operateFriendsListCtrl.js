angular.module('controllers', ['ionic']).controller('operateFriendsListCtrl', [
        '$scope','$state','$stateParams','GetAttentionFromMarketer',
        function ($scope,$state,$stateParams,GetAttentionFromMarketer) {
            $scope.info = {}

            $scope.$on('$ionicView.enter', function() {
                $scope.lock = 'false';
                $scope.shareNum = $stateParams.ShareNum;
                $scope.url = $stateParams.url;
                $scope.doRefresh();

                if($stateParams.ShareNum==0){
                    $scope.NoNum = "false";
                }else{
                    $scope.NoNum = "true";
                    $scope.pageLoading = true;
                    GetAttentionFromMarketer.save({}, function (data) {
                        $scope.pageLoading = false;
                        if(data.userList=="0"){
                            $scope.NoNum = "false";
                            $scope.shareNum = 0;
                        }else{
                            $scope.userList = data.userList;
                        }
                    });
                }
            })

            $scope.know=function(){
                $scope.lock='false';
            }
            $scope.share=function(){
                $scope.lock='true';
            }

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
                                    title: '12.15-12.23 邀请好友关注宝大夫，赢取生鲜商城50元无门槛现金券！', // 分享标题
                                    link:webname+'/titan/ap#/operateShare/'+$scope.url, // 分享链接
                                    imgUrl:webname+'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg', // 分享图标
                                    success: function (res) {

                                        //记录用户分享文章
                                        ArticleShareRecord.save({"contentId":"运营活动"},function(data){

                                        })
                                    },
                                    fail: function (res) {
                                    }
                                });

                                wx.onMenuShareAppMessage({
                                    title: '12.15-12.23 邀请好友关注宝大夫，赢取生鲜商城50元无门槛现金券！', // 分享标题
                                    desc: '宝大夫为您提供儿科专家线上咨询，名院、名医就诊预约，郑玉巧育儿经服务，为宝宝健康保驾护航。', // 分享描述
                                    link: webname+'/titan/ap#/operateShare/'+$scope.url, // 分享链接
                                    imgUrl:webname+'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg', // 分享图标
                                    success: function (res) {
                                        //记录用户分享文章
                                        ArticleShareRecord.save({"contentId":"运营活动"},function(data){

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
    }])