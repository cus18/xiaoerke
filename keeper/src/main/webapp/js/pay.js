// 订单单价,账户余额,订单id,微信需支付
var chargePrice,patient_register_service_id,needPayMoney;

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
                    // config信息验证后会执行ready方法，
                    // 所有接口调用都必须在config接口获得结果之后，
                    // config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，
                    // 则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，
                    // 则可以直接调用，不需要放在ready函数中。
                })
            }else{
            }
        },
        error : function() {
        }
    });

    patient_register_service_id = getCookie("patient_register_service_id");
    if(patient_register_service_id == "noData") {
        $("#doctorInfo").hide();
        $("#appointmentInfo").hide();

    }
    else{
        //获取页面信息
        var mydata = '{"patient_register_service_id":"'
            + patient_register_service_id + '"}';
        $.ajaxSetup({
            contentType : 'application/json'
        });
        $.post('ap/order/user/getOrderPayInfo', mydata,
            function(data) {
                if(data!=null ){
                    document.getElementById("date").innerHTML = data.date;
                    document.getElementById("doctorName").innerHTML = data.doctorName;
                    document.getElementById("hospitalName").innerHTML = data.hospitalName;
                }
            }, 'json');
    }

    //显示支付金额
    var payMoney = document.getElementById("payMoney");
    var needPay = document.getElementById("needPay");
    chargePrice = parseInt(getCookie("chargePrice"))/100;
    payMoney.innerHTML = chargePrice + "元/月";
    needPay.innerHTML = chargePrice+"元";
    needPayMoney = chargePrice;
}

var pay = function(){
    $('#payButton').attr('disabled',"true");//添加disabled属性
    $.ajax({
        url:"ap/account/user/userPay",// 跳转到 action
        async:true,
        type:'get',
        data:{patientRegisterId:patient_register_service_id,payPrice:needPayMoney*100},
        cache:false,
        success:function(data) {
            $('#payButton').removeAttr("disabled");
            var obj = eval('(' + data + ')');
            if(parseInt(obj.agent)<5){
                alert("您的微信版本低于5.0无法使用微信支付");
                return;
            }
            if(obj.false == 'false'){
                if(obj.agent == "6"){
                    alert("支付失败,请重新支付");
                }else if(obj.agent == "7"){
                    alert("该订单已支付,请到我的预约中进行查看");
                }
                return;
            }
            wx.chooseWXPay({
                appId:obj.appId,
                timestamp:obj.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                nonceStr:obj.nonceStr,  // 支付签名随机串，不长于 32 位
                package:obj.package,// 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                signType:obj.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                paySign:obj.paySign,  // 支付签名
                success: function (res) {
                    if(res.errMsg == "chooseWXPay:ok" ) {
                        window.location.href = "appoint#/memberServiceSuccess/"+patient_register_service_id;
                    }else{
                        alert("支付失败,请重新支付")
                    }
                },
                fail: function (res) {
                    alert(res.errMsg)
                }
            });
        },
        error : function() {
        }
    });
}

var getCookie = function(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

var GetQueryString = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

