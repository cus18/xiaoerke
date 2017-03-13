angular.module('controllers', ['ionic','ngDialog']).controller('addGroupCtrl', [
    '$scope','$state','$stateParams',"GetConfig","SharSeConsult",
    function ($scope,$state,$stateParams,GetConfig,SharSeConsult) {

$scope.loadShare = function () {
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
                        'scanQRCode',
                    ] // 功能列表
                });
                wx.ready(function () {
                    wx.scanQRCode({
                        needResult: 0, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
                        scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
                        success: function (res) {
                            var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                            console.log(result)
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
