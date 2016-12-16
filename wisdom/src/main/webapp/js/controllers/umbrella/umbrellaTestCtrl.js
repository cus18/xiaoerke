angular.module('controllers', ['ionic']).controller('umbrellaTestCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="宝大夫儿童家庭重疾互助计划";

            $scope.$on('$ionicView.enter', function(){

                $.ajax({
                    url:"umbrella/getOpenid",// 跳转到 action
                    async:true,
                    type:'post',
                    cache:false,
                    dataType:'json',
                    success:function(data) {
                        if(data.openid=="none"){
                            window.location.href = "http://s201.xiaork.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s201.xiaork.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
                        }
                    },
                    error : function() {
                    }
                });

                /*
                 以前支付代码
                 */
                // 订单单价,账户余额,订单id,微信需支付
                //var chargePrice,patient_register_service_id,needPayMoney;
                //页面初始化执行,用户初始化页面参数信息以及微信的支付接口
                var doRefresh = function(){
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
                                timestamp = data.timestamp;//得到时间戳
                                nonceStr = data.nonceStr;//得到随机字符串
                                signature = data.signature;//得到签名
                                appid = data.appid;//appid
                                //微信配置
                                wx.config({
                                    debug: false,
                                    appId: appid,
                                    timestamp:timestamp,
                                    nonceStr: nonceStr,
                                    signature: signature,
                                    jsApiList: [
                                        'chooseWXPay'
                                    ] // 功能列表
                                });
                                wx.ready(function () {
                                    wx.hideOptionMenu();
                                    // config信息验证后会执行ready方法，
                                    // 所有接口调用都必须在config接口获得结果之后，
                                    // config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，
                                    // 则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，
                                    // 则可以直接调用，不需要放在ready函数中。
                                });

                            }else{
                            }
                        },
                        error : function() {
                        }
                    });

                }
            });

            
    }]);