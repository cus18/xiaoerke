angular.module('controllers', []).controller('microElementCtrl', [
        '$scope','$stateParams',
        function ($scope,$stateParams) {

            $scope.imgPaht = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/"+$stateParams.imgPath;

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
            }
            recordLogs("MICRO_SHOW");

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
                                    'previewImage'
                                ] // 功能列表
                            });
                            wx.ready(function () {
                            })
                        }
                    },
                    error : function() {
                    }
                });
            }

            $scope.showImg = function () {
                wx.previewImage({
                    current: $scope.imgPaht, // 当前显示图片的http链接
                    urls: [$scope.imgPaht] // 需要预览的图片http链接列表
                });
            }
    }]);
