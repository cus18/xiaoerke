var payLock = false;
var moneys = "0";
var leaveNotes = "";

var recordLogs = function(val){
    $.ajax({
        url:"util/recordLogs",
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
var skip=function(item){
    $("html,body").stop().animate({"scrollTop":$("#"+item).offset().top},0);
}
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
                    data:{url:location.href},//得到需要分享页面的url
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
                                debug: true,
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
};

function wechatPay() {
    recordLogs("LOVE_PLAN_001");
    var u = navigator.userAgent, app = navigator.appVersion;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('linux') > -1; //g
    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    if (isAndroid) {
        payLock=true;
    }
    if(payLock) {
        moneys = $('#money').val();
        leaveNotes = $("#leaveNotes").val();
        if (moneys != "0" && moneys!="") {
            $.ajax({
                url: "account/user/lovePlanPay",
                async: true,
                type: 'get',
                data: {leaveNote: encodeURI(leaveNotes), payPrice: moneys * 100},
                cache: false,
                success: function (data) {
                    $('#payButton').removeAttr("disabled");
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
                                window.location.href="http://s175.baodf.com/market/market#/lovePlanPaySuccess";
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
        } else {
            alert("请输入大于0的数字！");
        }
    }else{
        payLock=true;
    }
}
