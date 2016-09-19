var payLock = true;
var moneys = 9.9;
var leaveNotes = "reward";
var useBabyCoin = true;

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
                });
            }else{
            }
        },
        error : function() {
        }
    });

     $('#money').html(moneys);
    recordLogs("consult_charge_twice_information_payclick");
};
function wechatPay() {
    if(useBabyCoin){
        UseBobyCoinPay();
    }
    recordLogs("consult_charge_twice_paypage_paybutton");
    var u = navigator.userAgent, app = navigator.appVersion;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('linux') > -1; //g
    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    if (isAndroid) {
        payLock=true;
    }
    if(payLock) {
        if (moneys != "0" && moneys!="") {
            $.ajax({
                url: "account/user/doctorConsultPay",
                async: true,
                type: 'get',
                data: {payType:"doctorConsultPay",leaveNotes: leaveNotes, payPrice: moneys * 100,useBabyCoinPay:useBabyCoin},
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
                              window.location.href="http://s132.baodf.com/angel/patient/consult#/doctorConsultPaySuccess";
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
    }else{
        payLock=true;
    }
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
(function(){
    var time1,
        bar = 'Choose';
    time1 = setInterval(flag,100);
    function flag(){
        if($('#useBabyCoinRight')){
            $.ajax({
                type: "get",
                url: 'http://'+window.location.host + "/keeper/babyCoin/babyCoinInit",
                dataType: "json",
                success: function(data) {
                    var canUse = 90;
                    data.babyCoinVo.cash>99?canUse = 99:canUse = data.babyCoinVo.cash;
                    $('#useBabyCoinLeft').html('宝宝币：'+data.babyCoinVo.cash+'枚，本次可以用'+canUse+'枚');
                    if(data.babyCoinVo.cash > 0){
                        $('#useBabyCoinRight').click(function(){
                            if(useBabyCoin){
                                bar = 'Unchoose';
                                useBabyCoin = false;
                            }else{
                                bar = 'Choose';
                                useBabyCoin = true;
                            };
                            $('#useBabyCoinRightImg').css('background','url("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/useBabyCoinRight'+bar+'.png") 100% 100%/100% 100%');
                            console.log(useBabyCoin)
                        });
                    }else{
                        $('#useBabyCoinRightImg').css('background','url("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/useBabyCoinRightUnchoose.png") 100% 100%/100% 100%')
                        useBabyCoin = false;
                        console.log(useBabyCoin)
                    }
                },
                error: function(jqXHR){
                    console.log("发生错误：" + jqXHR.status);
                },
            });
        }
        clearTimeout(time1);
    }
}());
//doctorConsultPaySuccess
function UseBobyCoinPay() {
    $.ajax({
        type: "GET",
        url: "babyCoin/minusOrAddBabyCoin",
        async:false,
        dataType: "json",
        success: function(data) {
            if(data.status == 'success'){
                console.log('我成功扣到钱了');
                window.location.href="http://s132.baodf.com/angel/patient/consult#/doctorConsultPaySuccess";
            }
        },
        error: function(jqXHR){
            console.log("发生错误：" + jqXHR.status);
        },
    });
}

