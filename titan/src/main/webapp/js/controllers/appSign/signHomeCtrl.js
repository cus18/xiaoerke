angular.module('controllers', ['ionic','ngDialog']).controller('signHomeCtrl', [
    '$scope','$state','$stateParams',
    'OrderPayMemberServiceOperation','GetUserMemberService','$ionicScrollDelegate','$location','ngDialog','GetPunchCardPage',
    function ($scope,$state,$stateParams,OrderPayMemberServiceOperation,GetUserMemberService,$ionicScrollDelegate,$location,ngDialog,GetPunchCardPage) {
        //前往个人中心
        $scope.goCenter=function(){
            $state.go('signRecord')
        }
        //启动状态判断
        $scope.start_status=false;
        //支付状态判断
        $scope.pay_status=false;
        //打卡时间提示状态
        $scope.time_status=false;
        //打卡成功提示状态
        $scope.sign_status=false;
        //分享弹窗状态
        $scope.share_status=false;


        //加入的点击事件
        $scope.goJoin=function(){
            $ionicScrollDelegate.scrollTop();
            $scope.start_status=true;
        }
        //支付按钮点击事件
        $scope.goPay=function(){
            $ionicScrollDelegate.scrollTop();

        }
        //喊朋友一起来参加
        $scope.goShare=function(){
            $ionicScrollDelegate.scrollTop();
            $scope.share_status=true;
        }
        //关闭按钮
        $scope.close=function(){
            //启动状态判断
            $scope.start_status=false;
            //支付状态判断
            $scope.pay_status=false;
            //打卡时间提示状态
            $scope.time_status=false;
            //打卡成功提示状态
            $scope.sign_status=false;
        }

        //查看更多记录
        $scope.lookMore=function(){

        }
        GetPunchCardPage.save({},function(res){
            console.log(res)
        })

        //初始化微信
        var doRefresh = function () {
            var timestamp;//时间戳
            var nonceStr;//随机字符串
            var signature;//得到的签名
            var appid;//得到的签名
            $.ajax({
                url: "wechatInfo/getConfig",// 跳转到 action
                async: true,
                type: 'get',
                data: {url: location.href},//得到需要分享页面的url
                cache: false,
                dataType: 'json',
                success: function (data) {
                    if (data != null) {
                        timestamp = data.timestamp;//得到时间戳
                        nonceStr = data.nonceStr;//得到随机字符串
                        signature = data.signature;//得到签名
                        appid = data.appid;//appid
                        //微信配置
                        wx.config({
                            debug: false,
                            appId: appid,
                            timestamp: timestamp,
                            nonceStr: nonceStr,
                            signature: signature,
                            jsApiList: [
                                'chooseWXPay'
                            ] // 功能列表
                        });
                        wx.ready(function () {
                            wx.hideOptionMenu();
                        });
                    } else {
                    }
                },
                error: function () {
                    
                }
            });
        };



        //确认支付
        function wechatPay() {
            recordLogs("consult_charge_twice_paypage_paybutton");
            $.ajax({
                url: "account/user/doctorConsultPay",
                type: 'get',
                data: {
                    payPrice:endCoin ,
                    babyCoinNumber: cashNum*10
                },
                cache: false,
                success: function (data) {
                    var obj = eval('(' + data + ')');
                    if (parseInt(obj.agent) < 5) {
                        alert("您的微信版本低于5.0无法使用微信支付");
                        return;
                    }
                    //打开微信支付控件
                    wx.chooseWXPay({
                        appId: obj.appId,
                        timestamp: obj.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                        nonceStr: obj.nonceStr,  // 支付签名随机串，不长于 32 位
                        package: obj.package,// 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                        signType: obj.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                        paySign: obj.paySign,  // 支付签名
                        success: function (res) {
                            if (res.errMsg == "chooseWXPay:ok") {

                            } else {
                                alert("支付失败,请重新支付")
                            }
                        },
                        fail: function (res) {
                            alert(res.errMsg)
                        }
                    });
                },
                error: function () {
                }
            });
        }
    }])