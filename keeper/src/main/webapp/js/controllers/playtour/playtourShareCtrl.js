angular.module('controllers', ['ionic']).controller('playtourShareCtrl', [
    '$scope','$state','$stateParams','$http',
    function ($scope,$state,$stateParams,$http) {

        var shareimgList= ["http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fsharewubiaoti.png",//无心意评价分享
            "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fsharefuceng.png",//收到心意分享
            "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fsharefuceng_1.png"];//没有心意只有评价分享
        var imgType = 6;
        $scope.title0="分享";
        $scope.$on('$ionicView.enter', function(){

            var pData = {logContent:encodeURI("ZXFX_OPEN")};
            $http({method:'post',url:'util/recordLogs',params:pData});
            $scope.sharetou = false;
            $scope.shareimg = false;//分享图片
            imgType=$stateParams.id;
            $scope.goShare();
            $scope.doRefresh();

        });

        //显示分享图层
        $scope.goShare = function () {
            if(imgType!=6) {
                $scope.sharetou = true;
                $scope.shareimg = true;
                if (imgType == 1) {
                    $scope.shareImg = shareimgList[0];
                } else if (imgType == 2) {
                    $scope.shareImg = shareimgList[1];
                } else {
                    $scope.shareImg = shareimgList[2];
                }
            }else {
                var pData = {logContent:encodeURI("ZXFX_OPEN_SHARE")};
                $http({method:'post',url:'util/recordLogs',params:pData});
                imgType=1;
            }
        }

        //取消分享图层
        $scope.cancleShare = function(){
            $scope.sharetou = false;
            $scope.shareimg = false;//收到心意分享图片

        }

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
                                title: '你知道吗？微信竟然能免费咨询三甲医院的儿科专家！回答的超详细！', // 分享标题
                                link: "http://s251.baodf.com/keeper/playtour#/playtourShare/6", // 分享链接
                                imgUrl: 'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/third%2Fwechat.png', // 分享图标
                                success: function (res) {
                                    //记录用户分享文章
                                    var pData = {logContent:encodeURI("ZXFX")};
                                    $http({method:'post',url:'util/recordLogs',params:pData});

                                    // var shareData = {logContent:encodeURI("consultfirstchargefree")};
                                    // $http({method:'post',url:'util/recordLogs',params:shareData});

                                    //增加一次机会
                                    // addMePermTimes.save({"shareType":"consultfirstchargefree"},function (data) {
                                    //
                                    // });
                                },
                                fail: function (res) {
                                }
                            });

                            wx.onMenuShareAppMessage({
                                title: '哇！宝妈们都为之震惊啦。。。', // 分享标题
                                desc: '你知道吗？微信竟然能免费咨询三甲医院的儿科专家！回答的超详细！', // 分享描述
                                link:'http://s251.baodf.com/keeper/playtour#/playtourShare/6', // 分享链接
                                imgUrl: 'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/third%2Fwechat.png', // 分享图标
                                success: function (res) {
                                    var pData = {logContent:encodeURI("ZXFX")};
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


    }]);