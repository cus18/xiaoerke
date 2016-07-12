angular.module('controllers', ['ionic']).controller('lovePlanPaySuccessCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {
        $scope.title ="宝妈爱心接力";
        $scope.goUmbrella =function(){
            window.location.href="http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000005/a"
        };
        $scope.doRefresh = function(){
            loadShare();
            recordLogs("AXJZ_ZFCGY");
        };
        //分享到朋友圈或者微信
        var loadShare = function(){
            // if(version=="a"){
            var share = 'http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=32';
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
                                'onMenuShareAppMessage'
                            ] // 功能列表
                        });
                        wx.ready(function () {
                            // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                            wx.onMenuShareTimeline({
                                title: '我和任泉已为蛋蛋进行了公益捐款，捐款和转发都是献爱心', // 分享标题
                                link: share, // 分享链接
                                imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Faxjz.jpg', // 分享图标
                                success: function (res) {
                                    recordLogs("AXJZ_FXPYQ");
                                },
                                fail: function (res) {
                                }
                            });
                            wx.onMenuShareAppMessage({
                                title: '我和任泉已为蛋蛋进行了公益捐款，捐款和转发都是献爱心', // 分享标题
                                desc: '蛋蛋正在接受化疗，你的一个小小善举，就能挽救一个鲜活生命！', // 分享描述
                                link:share, // 分享链接
                                imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Faxjz.jpg', // 分享图标
                                success: function (res) {
                                    recordLogs("AXJZ_FXPY");
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
        $scope.$on('$ionicView.enter', function(){

        })
    }]);
