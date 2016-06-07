var moneys="";
var umbrelladId="";
var umbrellaPayInit=function(){
    cancelRemind();
    $("#QRCodeDIV").hide();
    $("#FreeOrder").hide();
    $("#payButton").attr("disabled","disabled");
    $.ajax({
        url:"umbrella/randomMoney",// 跳转到 action
        async:true,
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.type=="free"){
                window.location.href = "http://localhost:8080/wisdom/firstPage/umbrella?status=b";
            }else {
                moneys = data.result;
                if (moneys == "0") {
                    $("#payMoneyDis").html("0");
                    $("#FreeOrder").show();
                    $("#payOrder").hide();
                    $("#payButton").removeAttr("disabled");
                } else {
                    $("#payMoney").html(5-moneys);
                    $("#payMoneyDis").html(moneys);
                    $("#FreeOrder").hide();
                    $("#payButton").removeAttr("disabled");
                    umbrelladId=data.id;
                }
                $.ajax({
                    url:"umbrella/getOpenid",// 跳转到 action
                    async:true,
                    type:'post',
                    cache:false,
                    dataType:'json',
                    success:function(data) {
                        if(data.openid=="none"){
                            window.location.href = "http://s2.xiaork.cn/keeper/wechatInfo/fieldwork/wechat/author?url=http://s2.xiaork.cn/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
                        }
                    },
                    error : function() {
                    }
                });
            }
        },
        error : function() {
        }
    });
    doRefresh();
};
var payUmbrella=function(){
    window.location.href="/wisdom/umbrella#/umbrellaJoin"
};
var GetQueryString = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
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

function wechatPay() {
    if (moneys != "0") {
        moneys = 0.01;
        $.ajax({
            url: "account/user/umbrellaPay",// 跳转到 action
            async: true,
            type: 'get',
            data: {patientRegisterId: umbrelladId+"_"+GetQueryString("shareId"), payPrice: moneys * 100},
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
                            $.ajax({
                                type: 'POST',
                                url: "umbrella/getOpenidStatus",
                                contentType: "application/json; charset=utf-8",
                                success: function(result){
                                    var status=result.status;
                                    if(status=="1"){
                                        var shareid = GetQueryString("shareId")==null||GetQueryString("shareId")=="120000000"?130000000:GetQueryString("shareId");
                                        $.ajax({
                                            type: 'POST',
                                            url: "umbrella/getUserQRCode",
                                            contentType: "application/json; charset=utf-8",
                                            async:false,
                                            data:"{'id':'"+shareid+"'}",
                                            success: function (data) {
                                                $("#QRCode").attr("src",data.qrcode);
                                                $("#QRCodeDIV").show();
                                                $(".c-shadow").show();
                                                $(".shadow-content").show();
                                            },
                                            dataType: "json"
                                        });
                                    }else{
                                        window.location.href = "http://s2.xiaork.cn/wisdom/firstPage/umbrella?status=a";
                                    }
                                },
                                dataType: "json"
                            });

                            // window.location.href = "http://s2.xiaork.cn/wisdom/firstPage/umbrella?status=a";
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
    }else{
        window.location.href = "http://s2.xiaork.cn/wisdom/firstPage/umbrella?status=a";
    }
}
/*关闭关注二维码提示*/
var cancelRemind = function() {
    $(".c-shadow").hide();
    $(".shadow-content").hide();
}