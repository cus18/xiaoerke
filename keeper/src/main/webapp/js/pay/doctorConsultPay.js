var GetQueryString = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

$(document).ready(function(){
    var thirdName=GetQueryString("thirdName");
    console.log("thirdName data",thirdName);
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
                    console.log("后台数据显示：",data);

                    cash1 = data.payType1SumMoney;
                  /*  cashNum1 = data.payType1UseBabycoin/10;*/
                    cashNum1 = data.payType1UseBabycoin;

                    cash2 = data.payType2SumMoney;
                   /* cashNum2 = data.payType2UseBabycoin/10;*/
                    cashNum2 = data.payType2UseBabycoin;

                    cash3 = data.payType3SumMoney;
                    /*cashNum3 = data.payType3UseBabycoin/10;*/
                    cashNum3 = data.payType3UseBabycoin;
                    getBabyCoin();

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

    //获取宝宝币
    function getBabyCoin(){
        $.ajax({
            type: "get",
            url: 'http://' + window.location.host + "/keeper/babyCoin/babyCoinInit",
            dataType: "json",
            success: function (data) {
                /*cashTotal=data.babyCoinVo.cash/10;*/
                cashTotal=data.babyCoinVo.cash;
                $('#coin').html(cashTotal);

                console.log("宝宝币总数：",cashTotal);

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


    //三种服务方式 选择调用函数
    function callBack(num){
        console.log(cashNum1,cashNum2,cashNum3)
        if(num==1){
            cash=cash1;
            cashNum=cashNum1;
        }else if(num==2){
            cash=cash2;
            cashNum=cashNum2;
        }else if(num==3){
            cash=cash3;
            cashNum=cashNum3;
        }
        //判断宝宝币选中状态
        if(cashTotal>=cashNum){
            oSwitch=true;
            switchMethod(oSwitch);
            $('#use').removeClass("red");
            $('.babyCoinSmall').find('i').show();
            $('#use').html("本次可用"+cashNum+"枚");
        }else{
            oSwitch=false;
            switchMethod(oSwitch);
            $('#use').html("满"+cashNum+"枚可抵用");
            $('.babyCoinSmall').find('i').hide();
            $('#use').addClass("red");
        }
        $('#btn span').html(endCoin);
    }

    //是否选择使用babyCoin
    $('.babyCoinSmall').find('i').click(function(){
        console.log(oSwitch,cashTotal,cashNum);
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
            /*endCoin=cash-cashNum;*/
            endCoin=0;
        }else{
            $('.babyCoinSmall').find('i').removeClass('choose1').addClass('choose2');
            endCoin=cash;
        }
    }

    //提交支付
    $('#btn').click(function(){
        $('#btn').attr('disabled');
        if(oSwitch){
            wechatPay();
        }else{
            cashNum=0;
            wechatPay();
        }
    })

    //确认支付
    function wechatPay() {
        recordLogs("consult_charge_twice_paypage_paybutton");

        // 先判断是不是使用的宝宝币支付

        $.ajax({
            url: "account/user/doctorConsultPay",
            type: 'get',
            data: {
                payPrice:endCoin ,
                /*babyCoinNumber: cashNum*10*/
                babyCoinNumber: cashNum
            },
            cache: false,
            success: function (data) {
                if(data != 'payByBabycoin'){
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
                                if(thirdName=="GuoWei"){
                                    window.location.href = angelWebUrl +"angel/patient/consult#/doctorConsultPaySuccess/"+service+"/"+thirdName;
                                }
                                else{
                                    window.location.href = angelWebUrl +"angel/patient/consult#/doctorConsultPaySuccess/"+service+"/"+"";
                                }
                            } else {
                                alert("支付失败,请重新支付")
                            }
                        },
                        fail: function (res) {
                            alert(res.errMsg)
                        }
                    });
                }
                else {
                    //window.location.href = angelWebUrl +"angel/patient/consult#/doctorConsultPaySuccess/"+service;
                    if(thirdName=="GuoWei"){
                        window.location.href = angelWebUrl +"angel/patient/consult#/doctorConsultPaySuccess/"+service+"/"+thirdName;
                    }
                    else{
                        window.location.href = angelWebUrl +"angel/patient/consult#/doctorConsultPaySuccess/"+service+"/"+"";
                    }
                }
            },
            error: function () {
            }
        });
    }

})