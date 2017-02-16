var payLock = true;
var moneys = 9.9;
var leaveNotes = "reward";
var useBabyCoin = true;
var payFlag = false;
var payCount = 25;
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

    $('#money').html(moneys);
    recordLogs("consult_charge_twice_information_payclick");
    userBabyCoinPay();
};
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
recordLogs("ZXTS_GMRK")
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
                    payType: "doctorConsultPay",
                    leaveNotes: leaveNotes,
                    payPrice: moneys * 100,
                    useBabyCoinPay: useBabyCoin
                },
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
                                window.location.href = "http://s132.baodf.com/angel/patient/consult#/doctorConsultPaySuccess";
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
        }
    } else {
        payLock = true;
    }
}
function selectPay25Money() {
    document.getElementById("25MoneyPay").style.display = "";//隐藏
    document.getElementById("10MoneyPay").style.display = "none";//显
    payCount = 25;
    userBabyCoinPay();
}
function selectPay10Money() {
    document.getElementById("10MoneyPay").style.display = "";//隐藏
    document.getElementById("25MoneyPay").style.display = "none";//显
    payCount = 10;
    userBabyCoinPay();
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
var userBabyCoinPay = function () {
    var bar = 'Choose';
    $.ajax({
        type: "get",
        url: 'http://' + window.location.host + "/keeper/babyCoin/babyCoinInit",
        dataType: "json",
        success: function (data) {
            //var canUse = 0;//可以抵钱的宝宝币数
            var cash = 140;//现有宝宝币总数
            if (payCount == 10) {//用户选择支付10元的
                document.getElementById("useBabyCoin25Div").style.display = "none";
                document.getElementById("useBabyCoin10Div").style.display = "";
                if (cash >= 50) {
                    payFlag = true;
                    $('#useBabyCoin10Left').html('亲，您有宝宝币可减免5.0元');
                    $('#useBabyCoin10Right').click(function () {
                        if (useBabyCoin) {
                            bar = 'Unchoose';
                            useBabyCoin = false;
                            $('#payConfirm').html('确认支付9.9元');
                        } else {
                            bar = 'Choose';
                            useBabyCoin = true;
                            $('#payConfirm').html('确认支付4.9元');
                        }
                        ;
                        $('#useBabyCoin10RightImg').css('background', 'url("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/useBabyCoinRight' + bar + '.png") 100% 100%/100% 100%');
                        console.log(useBabyCoin)
                    });
                    $('#payConfirm').html('确认支付4.9元');
                } else {
                    $('#useBabyCoin10RightImg').css('background', 'url("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/useBabyCoinRightUnchoose.png") 100% 100%/100% 100%')
                    useBabyCoin = false;
                    console.log(useBabyCoin)
                    $('#payConfirm').html('确认支付9.9元');
                }
            } else {//用户选择支付25元的
                document.getElementById("useBabyCoin10Div").style.display = "none";
                document.getElementById("useBabyCoin25Div").style.display = "";
                if (cash >= 125) {
                    payFlag = true;
                    $('#useBabyCoin25Left').html('亲，您有宝宝币可减免12.5元');
                    $('#useBabyCoin25Right').click(function () {
                        if (useBabyCoin) {
                            bar = 'Unchoose';
                            useBabyCoin = false;
                            $('#payConfirm').html('确认支付25.0元');
                        } else {
                            bar = 'Choose';
                            useBabyCoin = true;
                            $('#payConfirm').html('确认支付12.5元');
                        }
                        ;
                        $('#useBabyCoin25RightImg').css('background', 'url("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/useBabyCoinRight' + bar + '.png") 100% 100%/100% 100%');
                        console.log(useBabyCoin)
                    });
                    $('#payConfirm').html('确认支付12.5元');
                } else {
                    $('#useBabyCoin25RightImg').css('background', 'url("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/useBabyCoinRightUnchoose.png") 100% 100%/100% 100%')
                    useBabyCoin = false;
                    console.log(useBabyCoin)
                    $('#payConfirm').html('确认支付25.0元');
                }
            }

        },
        error: function (jqXHR) {
            console.log("发生错误：" + jqXHR.status);
        },
    });
};
//doctorConsultPaySuccess
function UseBobyCoinPay() {
    $.ajax({
        type: "GET",
        url: "babyCoin/minusOrAddBabyCoin",
        async: false,
        dataType: "json",
        success: function (data) {
            if (data.status == 'success') {
                console.log('我成功扣到钱了');
                window.location.href = "http://s132.baodf.com/angel/patient/consult#/doctorConsultPaySuccess";
            }
        },
        error: function (jqXHR) {
            console.log("发生错误：" + jqXHR.status);
        },
    });
}

