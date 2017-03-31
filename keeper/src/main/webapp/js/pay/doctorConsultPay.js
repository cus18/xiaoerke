/*var payLock = true;
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
var payConfirmInfo6 = "购买一次，开始咨询后，30分钟内医生都会回复";
var payConfirmInfo7 = "购买一次，开始咨询后，24小时内医生都会回复";
var angelWebUrl = "";*/

//页面初始化执行,用户初始化页面参数信息以及微信的支付接口

/*var doRefresh = function () {
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

                //payType1SumMoney = data.payType1SumMoney;
                //payType1UseBabycoin = data.payType1UseBabycoin;
                //payType2SumMoney = data.payType2SumMoney;
                //payType2UseBabycoin = data.payType2UseBabycoin;
                //payType1ActualMoney = toDecimal2((payType1SumMoney * 10 - payType1UseBabycoin) / 10);
                //payType2ActualMoney = toDecimal2((payType2SumMoney * 10 - payType2UseBabycoin) / 10);
                //angelWebUrl = data.angelWebUrl;
                //payConfirmInfo0 = "亲，您有宝宝币可减免5.0元";
                //payConfirmInfo1 = "确认支付" + payType1SumMoney + "元";
                //payConfirmInfo2 = "确认支付" + payType1ActualMoney + "元";
                //payConfirmInfo3 = "确认支付" + payType2SumMoney + "元";
                //payConfirmInfo4 = "确认支付" + payType2ActualMoney + "元";
                //payConfirmInfo5 = "亲，您有宝宝币可减免12.5元";
                //$('#payConfirm').html(payConfirmInfo3);
                //$('#useBabyCoin25Left').html(payConfirmInfo5);

                //$('#money').html(moneys);
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

};*/
/*
function selectPayMoney(moneyCount) {
    if (moneyCount == payType2SumMoney) {
        document.getElementById("25MoneyPay").style.display = "";//隐藏
        document.getElementById("10MoneyPay").style.display = "none";//显
        payCount = payType2SumMoney;
        $('#payInfoId').html(payConfirmInfo7);
    } else {
        document.getElementById("10MoneyPay").style.display = "";//隐藏
        document.getElementById("25MoneyPay").style.display = "none";//显
        payCount = payType1SumMoney;
        $('#payInfoId').html(payConfirmInfo6);

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
                                    if(parseInt(moneys) <= parseInt(payType1SumMoney))
                                        consultTime = "30";
                                    else
                                        consultTime = "1440"
                                    window.location.href = angelWebUrl +"angel/patient/consult#/doctorConsultPaySuccess/"+consultTime;
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
*/

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


$(document).ready(function(){
    //我剩余的宝宝币
    var cashTotal;
    //需要支付的金额
    var cash;
    var cash1;
    var cash2;
    var cash3;
    //宝宝币使用数量
    var cashNum;
    var cashNum1;
    var cashNum2;
    var cashNum3;
    //服务方式
    var service=2;
    //宝宝币是否使用
    var oSwitch=true;
    //最终需要支付的金额
    var endCoin;
// 成功支付跳转页面
    var angelWebUrl = "";
    //打日志
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
                    angelWebUrl = data.angelWebUrl;
                    recordLogs("consult_charge_twice_information_payclick");

                    cash1 = data.payType1SumMoney;
                    cashNum1 = data.payType1UseBabycoin/10;

                    cash2 = data.payType2SumMoney;
                    cashNum2 = data.payType2UseBabycoin/10;

                    cash3 = data.payType3SumMoney;
                    cashNum3 = data.payType3UseBabycoin/10;
                    getBabyCoin()

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
    doRefresh();
    //确认支付
    function wechatPay() {
        recordLogs("consult_charge_twice_paypage_paybutton");

        // 先判断是不是使用的宝宝币支付

                $.ajax({
                    url: "account/user/doctorConsultPay",
                    type: 'get',
                    data: {
                        payPrice:endCoin ,
                        babyCoinNumber: cashNum*10
                    },
                    cache: false,
                    success: function (data) {
                        if(data == 'payByBabycoin'){
                        $('#btn').removeAttr("disabled");
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
                                    window.location.href = angelWebUrl +"angel/patient/consult#/doctorConsultPaySuccess/"+service;
                                } else {
                                    alert("支付失败,请重新支付")
                                }
                            },
                            fail: function (res) {
                                alert(res.errMsg)
                            }
                        });
                    }else {
                            window.location.href = angelWebUrl +"angel/patient/consult#/doctorConsultPaySuccess/"+service;
                    }
                    },
                    error: function () {
                    }
                });
    }
    //方式选择调用函数
    function callBack(num){
        console.log(cashNum1,cashNum2,cashNum3)
        if(num==1){
            cash=cash1;
            cashNum=cashNum1;
            $('#coin').html(cashNum)
        }else if(num==2){
            cash=cash2;
            cashNum=cashNum2;
        }else if(num==3){
            cash=cash3;
            cashNum=cashNum3;
        }
        $('#coin').html(cashNum)
        //判断宝宝币选中状态
        if(cashTotal>=cashNum){
            oSwitch=true;
            $('.lack').hide();
            switchMethod(oSwitch)
        }else{
            oSwitch=false;
            $('.lack').show();
            switchMethod(oSwitch)
        }
        $('#btn span').html(endCoin);
    }
    //获取宝宝币
    function getBabyCoin(){
        $.ajax({
            type: "get",
            url: 'http://' + window.location.host + "/keeper/babyCoin/babyCoinInit",
            dataType: "json",
            success: function (data) {
                cashTotal=data.babyCoinVo.cash/10;
                //默认第二种方式
                callBack(service)
            },
            error: function (jqXHR) {
                console.log("发生错误：" + jqXHR.status);
            },
        });
    }

    $(".service1").click(function(){
        service=1;
        $(".service1").addClass('active1')
        $(".service2").removeClass('active2')
        $(".service3").removeClass('active3')
        callBack(service);
    });
    $(".service2").click(function(){
        service=2;
        $(".service1").removeClass('active1')
        $(".service2").addClass('active2')
        $(".service3").removeClass('active3')
        callBack(service);
    });
    $(".service3").click(function(){
        service=3;
        $(".service1").removeClass('active1')
        $(".service2").removeClass('active2')
        $(".service3").addClass('active3')
        callBack(service);
    });

    //是否选择使用babyCoin
    $('.babyCoinSmall').find('i').click(function(){
        console.log(oSwitch,cashTotal,cashNum)
        if(cashTotal<cashNum){
            oSwitch=false;
            switchMethod(oSwitch);
        }else if(cashTotal>=cashNum){
            if( oSwitch==true){
                oSwitch=false;
                switchMethod(oSwitch);

            }else{
                oSwitch=true;
                switchMethod(oSwitch);

            }
        }
        $('#btn span').html(endCoin);
    })
    //宝宝币是否选择
    function switchMethod(oSwitch){
        if(oSwitch){
            $('.babyCoinSmall').find('i').removeClass('choose2').addClass('choose1');
            endCoin=cash-cashNum;
        }else{
            $('.babyCoinSmall').find('i').removeClass('choose1').addClass('choose2');
            endCoin=cash;
        }
    }


    //提交支付
    $('#btn').click(function(){
        $('#btn').attr('disabled')
        if(oSwitch){
            wechatPay()
        }else{
            cashNum=0;
            wechatPay();
        }
    })


    // var userBabyCoinPay = function () {
    //
    //     $.ajax({
    //         type: "get",
    //         url: 'http://' + window.location.host + "/keeper/babyCoin/babyCoinInit",
    //         dataType: "json",
    //         success: function (data) {
    //             cash = data.babyCoinVo.cash;//现有宝宝币总数
    //             if (payCount == payType1SumMoney) {
    //
    //
    //
    //             else {
    //
    //              }
    //
    //             } else {
    //
    //             }
    //
    //         },
    //         error: function (jqXHR) {
    //             console.log("发生错误：" + jqXHR.status);
    //         },
    //     });
    // };

})