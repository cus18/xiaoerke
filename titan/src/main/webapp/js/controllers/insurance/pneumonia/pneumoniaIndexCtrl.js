angular.module('controllers', ['ionic']).controller('pneumoniaIndexCtrl', [
    '$scope','$state','$stateParams','$location','$http',
    function ($scope,$state,$stateParams,$location,$http) {
        $scope.readLock = true;
        var Ip = "s251.baodf.com";

        $scope.$on('$ionicView.enter', function(){
            setLog("SZKB_FWXQ");
        });
        //阅读
        $scope.read = function () {
            $scope.readLock=!$scope.readLock;
        }

        //支付
        $scope.goPay = function(){
            setLog("SZKB_FWXQ_LJGM_");
            window.location.href = "http://"+Ip+"/keeper/wxPay/patientPay.do?serviceType=pneumonia";
            //window.location.href = "/keeper/wxPay/patientPay.do?serviceType=pneumonia";
        }

        //日志
        var setLog = function (item) {
            var pData = {logContent:encodeURI(item)};
            $http({method:'post',url:'util/recordLogs',params:pData});
        }
        
        $scope.doRefresh = function(){
            var share = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=30";
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
                                title: '妈妈要当心，儿童最高发的传染病——手足口病又来了，预防和保障一个不能少！', // 分享标题
                                link: share, // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2Fhandfootmouth.jpg', // 分享图标
                                success: function (res) {

                                    setLog("SZKB_FXPPQ");
                                },
                                fail: function (res) {
                                }
                            });

                            wx.onMenuShareAppMessage({
                                title: '小儿手足口宝', // 分享标题
                                desc: '妈妈要当心，儿童最高发的传染病——手足口病又来了，预防和保障一个不能少！', // 分享描述
                                link:share, // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2Fhandfootmouth.jpg', // 分享图标
                                success: function (res) {

                                    setLog("SZKB_FXPP");
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


