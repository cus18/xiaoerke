angular.module('controllers', []).controller('olympicBabyMyPrizeCtrl', [
        '$scope','$state','GetUserPrizes','GetUserOpenId','$http',
        function ($scope,$state,GetUserPrizes,GetUserOpenId,$http) {
            $scope.showPrize = false;
            $scope.showNoPrize = false;
            document.title="我的奖品"; //修改页面title
            
            //获取用户的openid
            GetUserOpenId.get(function (data) {
                if(data.openid!="none"){
                    //获取用户获奖列表
                    GetUserPrizes.save({"openid":data.openid},function (data) {
                        console.log("data",data);
                        if(data.prizeList!=undefined){
                            $scope.showPrize = true;
                            $scope.showNoPrize = false;
                            $scope.prizeList = data.prizeList;
                        }else{
                            $scope.showPrize = false;
                            $scope.showNoPrize = true;
                        }
                    });
                }else {
                    $scope.showPrize = false;
                    $scope.showNoPrize = true;
                }
            });


            //是否跳转第三方链接
            $scope.goLink = function (index) {
                if($scope.prizeList[index].prizeLink!=""){
                    window.location.href = $scope.prizeList[index].prizeLink;
                }
            }

            /*//记录日志
            var setLog = function (content) {
                var pData = {logContent:encodeURI(content)};
                $http({method:'post',url:'util/recordLogs',params:pData});
            }

            $scope.Refresh = function(){
                var share = "";
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
                                    title: '赢个大奖居然这么简单……', // 分享标题
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/common/sharePic.png', // 分享图标
                                    success: function (res) {
                                        //记录用户分享文章
                                        
                                    },
                                    fail: function (res) {
                                    }
                                });

                                wx.onMenuShareAppMessage({
                                    title: '赢个大奖居然这么简单……', // 分享标题
                                    desc: '宝宝奥运大闯关”开始啦！玩游戏闯关卡，赢取超值豪礼！我已加入，你也赶紧一起来参与吧！', // 分享描述
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/common/sharePic.png', // 分享图标
                                    success: function (res) {
                                        setLog("action_olympic_baby_share");
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

            }*/
    }]);
