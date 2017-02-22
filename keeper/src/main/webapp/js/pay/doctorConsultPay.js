var payLock = true;
var moneys = 9.9;
var babyCoinNumber = 0;
var leaveNotes = "reward";
var useBabyCoin = true;
var payCount = 25;
var payType1SumMoney = 0;
var payType1UseBabycoin = 0;
var payType1ActualMoney = 0;
var payType2SumMoney = 0;
var payType2UseBabycoin = 0;
var payType2ActualMoney = 0;
var bar = 'Choose';
var cash = 0;

var payConfirmInfo0 = "";
var payConfirmInfo1 = "";
var payConfirmInfo2 = "";
var payConfirmInfo3 = "";
var payConfirmInfo4 = "";
var payConfirmInfo5 = "";

//页面初始化执行,用户初始化页面参数信息以及微信的支付接口
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

                payType1SumMoney = data.payType1SumMoney;
                payType1UseBabycoin = data.payType1UseBabycoin;
                payType2SumMoney = data.payType2SumMoney;
                payType2UseBabycoin = data.payType2UseBabycoin;
                payType1ActualMoney = toDecimal2((payType1SumMoney * 10 - payType1UseBabycoin) / 10);
                payType2ActualMoney = toDecimal2((payType2SumMoney * 10 - payType2UseBabycoin) / 10);
                payConfirmInfo0 = "亲，您有宝宝币可减免5.0元";
                payConfirmInfo1 = "确认支付" + payType1SumMoney + "元";
                payConfirmInfo2 = "确认支付" + payType1ActualMoney + "元";
                payConfirmInfo3 = "确认支付" + payType2SumMoney + "元";
                payConfirmInfo4 = "确认支付" + payType2ActualMoney + "元";
                payConfirmInfo5 = "亲，您有宝宝币可减免12.5元";
                $('#payConfirm').html(payConfirmInfo3);
                $('#useBabyCoin25Left').html(payConfirmInfo5);

                $('#money').html(moneys);
                recordLogs("consult_charge_twice_information_payclick");
                userBabyCoinPay();
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

function selectPayMoney(moneyCount) {
    if (moneyCount == payType2SumMoney) {
        document.getElementById("25MoneyPay").style.display = "";//隐藏
        document.getElementById("10MoneyPay").style.display = "none";//显
        payCount = payType2SumMoney;
    } else {
        document.getElementById("10MoneyPay").style.display = "";//隐藏
        document.getElementById("25MoneyPay").style.display = "none";//显
        payCount = payType1SumMoney;
    }
    userBabyCoinPay();
}

var userBabyCoinPay = function () {

    $.ajax({
        type: "get",
        url: 'http://' + window.location.host + "/keeper/babyCoin/babyCoinInit",
        dataType: "json",
        success: function (data) {
            //var canUse = 0;//可以抵钱的宝宝币数
            cash = data.babyCoinVo.cash;//现有宝宝币总数
            if (payCount == payType1SumMoney) {//用户选择支付10元的
                document.getElementById("useBabyCoin25Div").style.display = "none";
                document.getElementById("useBabyCoin10Div").style.display = "";
                if (cash >= payType1SumMoney * 10) {
                    $('#useBabyCoin10Left').html(payConfirmInfo0);
                    if(bar == "Choose"){
                        moneys = payType1ActualMoney;
                        babyCoinNumber = payType1UseBabycoin;
                        $('#payConfirm').html(payConfirmInfo2);
                    }else{
                        moneys = payType1SumMoney;
                        babyCoinNumber = 0;
                        $('#payConfirm').html(payConfirmInfo1);
                    }

                } else {
                    useBabyCoin = false;
                    console.log(useBabyCoin)
                    $('#payConfirm').html(payConfirmInfo1);
                    moneys = payType1SumMoney;
                    babyCoinNumber = 0;
                    bar = "Unchoose";
                }
                $('#useBabyCoin10Left').html(payConfirmInfo0);
                $('#useBabyCoin10RightImg').css('background', 'url("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/useBabyCoinRight' + bar + '.png") 100% 100%/100% 100%')

            } else {//用户选择支付25元的
                document.getElementById("useBabyCoin10Div").style.display = "none";
                document.getElementById("useBabyCoin25Div").style.display = "";
                if (cash >= payType2UseBabycoin) {
                    $('#useBabyCoin25Left').html(payConfirmInfo5);
                    if(bar == "Choose"){
                        moneys = payType2ActualMoney;
                        babyCoinNumber = payType2UseBabycoin;
                        $('#payConfirm').html(payConfirmInfo4);
                    }else{
                        moneys = payType2SumMoney;
                        babyCoinNumber = 0;
                        $('#payConfirm').html(payConfirmInfo3);
                    }
                } else {
                    useBabyCoin = false;
                    console.log(useBabyCoin)
                    $('#payConfirm').html(payConfirmInfo3);
                    moneys = payType2SumMoney;
                    babyCoinNumber = 0;
                    bar = "Unchoose";
                }
                $('#useBabyCoin25RightImg').css('background', 'url("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/useBabyCoinRight' + bar + '.png") 100% 100%/100% 100%')

            }

        },
        error: function (jqXHR) {
            console.log("发生错误：" + jqXHR.status);
        },
    });
};

function useBabyCoinClick(useBabyCoinType){

    if("useBabyCoin10Right" == useBabyCoinType){
        if (useBabyCoin) {
            bar = 'Unchoose';
            useBabyCoin = false;
            $('#payConfirm').html(payConfirmInfo1);
            moneys = payType1SumMoney;
            babyCoinNumber = 0;
        } else if(cash >= payType1SumMoney * 10){
            bar = 'Choose';
            useBabyCoin = true;
            $('#payConfirm').html(payConfirmInfo2);
            moneys = payType1ActualMoney;
            babyCoinNumber = payType1UseBabycoin;
        }else{
            alert("对不起，宝宝币不足"+payType1SumMoney * 10+"个，无法使用");
            $('#payConfirm').html(payConfirmInfo1);
        }

        $('#useBabyCoin10RightImg').css('background', 'url("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/useBabyCoinRight' + bar + '.png") 100% 100%/100% 100%');
        console.log(useBabyCoin)
    }else {
        if (useBabyCoin) {
            bar = 'Unchoose';
            useBabyCoin = false;
            $('#payConfirm').html(payConfirmInfo3);
            moneys = payType2SumMoney;
            babyCoinNumber = 0;
        } else if(cash >= payType2UseBabycoin){
            bar = 'Choose';
            useBabyCoin = true;
            $('#payConfirm').html(payConfirmInfo4);
        }else{
            alert("对不起，宝宝币不足"+payType2UseBabycoin+"个，无法使用");
            $('#payConfirm').html(payConfirmInfo3);
        }
        $('#useBabyCoin25RightImg').css('background', 'url("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/useBabyCoinRight' + bar + '.png") 100% 100%/100% 100%');
        console.log(useBabyCoin)
    }
}

function wechatPay() {
    recordLogs("consult_charge_twice_paypage_paybutton");
    var u = navigator.userAgent, app = navigator.appVersion;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('linux') > -1; //g
    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    if (isAndroid) {
        payLock = true;
    }
    if (payLock) {
        if (moneys != "0" && moneys != "") {
            $.ajax({
                url: "account/user/doctorConsultPay",
                type: 'get',
                data: {
                    payPrice: moneys,
                    babyCoinNumber: babyCoinNumber
                },
                cache: false,
                success: function (data) {
                    if(data == "false"){
                        alert("对不起，宝宝币不足，请选择其他支付方式！");
                    }else{
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
                                    var consultTime ="";
                                    if(moneys <= payType1SumMoney)
                                        consultTime = "30";
                                    else
                                        consultTime = "1440"
                                    window.location.href = "http://s132.baodf.com/angel/patient/consult#/doctorConsultPaySuccess/"+consultTime;
                                } else {
                                    alert("支付失败,请重新支付")
                                }
                            },
                            fail: function (res) {
                                alert(res.errMsg)
                            }
                        });
                    }
                },
                error: function () {
                }
            });
        } else {
        }
    } else {
        payLock = true;
    }
}

//制保留2位小数，如：2，会在2后面补上00.即2.00
function toDecimal2(x) {
    var f = parseFloat(x);
    if (isNaN(f)) {
        return false;
    }
    var f = Math.round(x * 100) / 100;
    var s = f.toString();
    var rs = s.indexOf('.');
    if (rs < 0) {
        rs = s.length;
        s += '.';
    }
    while (s.length <= rs + 2) {
        s += '0';
    }
    return s;
}
var recordLogs = function (val) {
    $.ajax({
        url: "util/recordLogs",// 跳转到 action
        async: true,
        type: 'get',
        data: {logContent: encodeURI(val)},
        cache: false,
        dataType: 'json',
        success: function (data) {
        },
        error: function () {
        }
    });
};

//doctorConsultPaySuccess
//function UseBobyCoinPay() {
//    $.ajax({
//        type: "GET",
//        url: "babyCoin/minusOrAddBabyCoin",
//        async: false,
//        dataType: "json",
//        success: function (data) {
//            if (data.status == 'success') {
//                console.log('我成功扣到钱了');
//                window.location.href = "http://s132.baodf.com/angel/patient/consult#/doctorConsultPaySuccess";
//            }
//        },
//        error: function (jqXHR) {
//            console.log("发生错误：" + jqXHR.status);
//        },
//    });
//}