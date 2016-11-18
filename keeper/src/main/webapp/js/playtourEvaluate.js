
var resultList=["","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fbumanyi_weixuanzhong.png",//不满意
    "","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fmanyiweixuanzhong.png",//满意
    "","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Ffeichangmanyiweixuanzhong.png"]//非常满意

var moneyNum = 0;
var ptm3Flag = 1;//显示输入其它金额
var indexFlag = 0;
var customerId;
var consultStatus="";

$(function(){
   // $('.evalhavemoney').hide();//收到心意钱
   // $('.evalsharebut').hide();//分享按钮
    // $('#ping').hide();
    $("#but").attr("style","background-color:#E8E8E8");
    getCustomerInfo();
    
})

$(document).on('keypress', '#getMoney', function (e) {
    // 在 keypress 事件中拦截错误输入

    var sCharCode = String.fromCharCode(e.charCode);
    var sValue = this.value;

    if (/[^0-9.]/g.test(sCharCode) || __getRegex(sCharCode).test(sValue)) {
        return false;
    }

    /**
     * 根据用户输入的字符获取相关的正则表达式
     * @param  {string} sCharCode 用户输入的字符，如 'a'，'1'，'.' 等等
     * @return {regexp} patt 正则表达式
     */
    function __getRegex (sCharCode) {
        var patt;
        if (/[0]/g.test(sCharCode)) {
            // 判断是否为空
            patt = /^$/g;
        } else if (/[.]/g.test(sCharCode)) {
            // 判断是否已经包含 . 字符或者为空
            patt = /((\.)|(^$))/g;
        } else if (/[1-9]/g.test(sCharCode)) {
            // 判断是否已经到达小数点后两位
            patt = /\.\d{1}$/g;
        }
        return patt;
    }
}).on('keyup paste', '#getMoney', function () {
    // 在 keyup paste 事件中进行完整字符串检测

    var patt = /^((?!0)\d+(\.\d{1,2})?)$/g;

    if (this.value>200) {

        // 错误提示相关代码，边框变红、气泡提示什么的
        $("#getMoney").val(""); //设置
        alert("感谢您的支持，请输入0~200元");
    }
});

/*//判断输入心意钱
var moreMoney = function () {
   // recordLogs("ZXPJSXY_JE");
    
    if($('#getMoney').val()>0){
        $("#but").removeAttr('disable');
        $("#but").attr("style","background-color:#fe717b");
        $('.inputmoney img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_xuanzhong.png");
    }else{
        $("#but").attr('disable','disabled');
        $("#but").attr("style","background-color:#E8E8E8");
        $('.inputmoney img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_bukedian.png");
    }
}

//心意钱数
var setMoney = function (index) {
    moneyNum = $('#getMoney').val();
    if(index==0){

        if(moneyNum<=0){
            $('.ptm img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_bukedian.png");
        }else{
            $('.ptm img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_xuanzhong.png");
            if(moneyNum.indexOf(".")>0){
                $("#but").removeAttr('disable');
                $("#but").attr("style","background-color:#fe717b");
                // moneyNum=parseInt(moneyNum);
            }else {
                $("#but").removeAttr('disable');
                $("#but").attr("style","background-color:#fe717b");
                // moneyNum--;
            }

            var a=parseInt(moneyNum)/5;
            var b=parseInt(moneyNum)%5;
            if(a<1){
                a=0;
            }else {
                if(b==0){
                    a--;
                }else {
                    a = parseInt(a);
                }
            }
            moneyNum=5*(a);
            if(moneyNum==0){
                $("#but").attr('disable','disabled');
                $("#but").attr("style","background-color:#E8E8E8");
                $('.ptm img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_bukedian.png");
            }
            $('#getMoney').val(moneyNum);
        }
    }else if(index==1){
        if(moneyNum.indexOf(".")>0){
            $("#but").removeAttr('disable');
            $("#but").attr("style","background-color:#fe717b");
        }else {
            $("#but").removeAttr('disable');
            $("#but").attr("style","background-color:#fe717b");
        }
        if(moneyNum==""){
            moneyNum=0;
        }
        var a=parseInt(moneyNum)/5;
        a=parseInt(a);
        moneyNum=5*(a+1);
        $('#getMoney').val(moneyNum);
        $('.ptm img').eq(1).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjia_xuanzhong.png");
        $('.ptm img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_xuanzhong.png");
    }
}*/

//点击选择心意金额
var getPtm2 = function (index) {
    //收起输入其它金额
    $(".ptm3_but img").attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour/index2_money.png");
    $(".ptm3_input").hide();
    ptm3Flag = 1;
    $('#getMoney').val("");

    if(moneyNum == 0){
        $(".ptm2 span").eq(index).addClass("action");
        if(index == 0){
            indexFlag = 0;
            recordLogs("ZXPJSXY_one");
            moneyNum = 8;
        }else if(index==1){
            indexFlag = 1;
            recordLogs("ZXPJSXY_two");
            moneyNum = 12;
        }else if(index==2){
            indexFlag = 2;
            recordLogs("ZXPJSXY_three");
            moneyNum = 20;
        }else if(index==3){
            indexFlag = 3;
            recordLogs("ZXPJSXY_four");
            moneyNum = 26;
        }else if(index==4){
            indexFlag = 4;
            recordLogs("ZXPJSXY_five");
            moneyNum = 52;
        }else if(index==5){
            indexFlag = 5;
            recordLogs("ZXPJSXY_six");
            moneyNum = 98;
        }
        $("#but").removeAttr('disable');
        $("#but").attr("style","background-color:#fe717b");
    }else{
        if(indexFlag == index){
            $(".ptm2 span").eq(index).removeClass("action");
            moneyNum = 0;
            $("#but").attr('disable','disabled');
            $("#but").attr("style","background-color:#E8E8E8");
        }else{
            $(".ptm2 span").removeClass("action");
            $(".ptm2 span").eq(index).addClass("action");
            if(index == 0){
                indexFlag = 0;
                recordLogs("ZXPJSXY_one");
                moneyNum = 8;
            }else if(index==1){
                indexFlag = 1;
                recordLogs("ZXPJSXY_two");
                moneyNum = 12;
            }else if(index==2){
                indexFlag = 2;
                recordLogs("ZXPJSXY_three");
                moneyNum = 20;
            }else if(index==3){
                indexFlag = 3;
                recordLogs("ZXPJSXY_four");
                moneyNum = 26;
            }else if(index==4){
                indexFlag = 4;
                recordLogs("ZXPJSXY_five");
                moneyNum = 52;
            }else if(index==5){
                indexFlag = 5;
                recordLogs("ZXPJSXY_six");
                moneyNum = 98;
            }
            $("#but").removeAttr('disable');
            $("#but").attr("style","background-color:#fe717b");
        }

    }
}

//输入其它金额
var getPtm3 = function () {
    recordLogs("ZXPJSXY_other");
    $(".ptm2 span").removeClass("action");
    moneyNum = 0;
    if(ptm3Flag == 1){
        $(".ptm3_but img").attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour/index2_money2.png");
        $(".ptm3_input").show();
        ptm3Flag = 0;
    }else{
        $(".ptm3_but img").attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour/index2_money.png");
        $(".ptm3_input").hide();
        ptm3Flag = 1;
        $('#getMoney').val("");
    }
}

var moreMoney2 = function () {
    if($('#getMoney').val()>0){
        $("#but").removeAttr('disable');
        $("#but").attr("style","background-color:#fe717b");
    }else{
        $("#but").attr('disable','disabled');
        $("#but").attr("style","background-color:#E8E8E8");
    }
}

var GetQueryString = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
function getCustomerInfo(){
    recordLogs("ZXPJXX_PJ");
    customerId=GetQueryString("customerId");
    consultStatus=GetQueryString("consultStatus");
    $.ajax({
        url:"interaction/user/findCustomerEvaluation",// 跳转到 action
        async:false,
        type:'POST',
        data:"{'id':'"+customerId+"'}",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            var evaluation=data.evaluation;
            var starInfo=data.starInfo;
            var doctorInfo=data.doctorHeadImage;
            if(evaluation.serviceAttitude==0){
                window.location.href = "wxPay/patientPay.do?serviceType=customerPay&customerId="+customerId+"&consultStatus="+consultStatus;
            }else if(evaluation.serviceAttitude==1){
                $("#playtourStar").html("不满意");
                $(".evalhavemoney").hide();
                    var dissatisfied = evaluation.dissatisfied;
                    dissatisfied = dissatisfied.split(",");
                    var a = "";
                    for (var i = 0; i < dissatisfied.length; i++) {
                        if (dissatisfied[i] == "0") {
                            a += "服务态度不好       ";
                        }
                        if (dissatisfied[i] == "1") {
                            a += "专业水平不高       ";
                        }
                        if (dissatisfied[i] == "2") {
                            a += "响应速度太慢       ";
                        }
                        // if(dissatisfied[i]=="3"){
                        //     a+="其他       ";
                        // }
                        if(a==""){
                            $("#evaluationSpan").hide();
                        }else {
                            a += "<br/>";
                            $("#evaluation").html(a);
                        }
                }
                $("#suggest").html(evaluation.content);
                $(".evalinputmoney").hide();
                $(".evalhavemoney").hide();
                $('.evalsharebut').show();//分享按钮
                $('#but').hide();//分享按钮
            }else if(evaluation.serviceAttitude==3){
                $("#playtourStar").html("满意");
                $('#ping').hide();
                $("#suggest").html(evaluation.content);
                $(".evalfinish img").attr("src",resultList[1]);
                // getMoney(evaluation);
                if(evaluation.redPacket!='null'&&typeof(evaluation.redPacket) != 'undefined'&&evaluation.redPacket!=""&&evaluation.redPacket!=0 && evaluation.payStatus =="success"){
                    $("#redPacket").html(evaluation.redPacket);
                    $(".evalinputmoney").hide();
                    $(".evalhavemoney").show();
                    $('.evalsharebut').show();//分享按钮
                    $("#but").attr("style","display:none");
                }else{
                    $(".evalhavemoney").hide();
                    $(".evalinputmoney").show();
                    $('.evalsharebut').hide();//分享按钮
                    $('#but').show();//分享按钮
                    $(".ptm3_but img").attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour/index2_money.png");
                    $(".ptm3_input").hide();
                    ptm3Flag = 1;
                    $('#getMoney').val("");
                }
            }else if(evaluation.serviceAttitude==5){
                $("#playtourStar").html("非常满意");
                $('#ping').hide();
                $("#suggest").html(evaluation.content);
                $(".evalfinish img").attr("src",resultList[2]);
                // getMoney(evaluation);
                if(evaluation.redPacket!='null'&&typeof(evaluation.redPacket) != 'undefined'&&evaluation.redPacket!=""){
                    $("#redPacket").html(evaluation.redPacket);
                    $(".evalinputmoney").hide();
                    $(".evalhavemoney").show();
                    $('.evalsharebut').show();//分享按钮
                    $('#but').hide();//分享按钮
                }else{
                    $(".evalhavemoney").hide();
                    $(".evalinputmoney").show();
                    $('.evalsharebut').hide();//分享按钮
                    $('#but').show();//分享按钮
                    $(".ptm3_but img").attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour/index2_money.png");
                    $(".ptm3_input").hide();
                    ptm3Flag = 1;
                    $('#getMoney').val("");
                }
            }
                var star=starInfo.startNum+"";
            $("#playtourImg").attr("src",resultList[evaluation.serviceAttitude]);
            
            if(evaluation.content=='null'||typeof(evaluation.content) == 'undefined'||evaluation.content==''){
                $("#suggestSpan").hide();
            }
                $("#redPacketcount").html(starInfo.redPacket);
            if(star=="1.00"){
                $("#starInfo").html("100%");
            }else {
                $("#starInfo").html(star.split(".")[1] + "%");
            }
                $("#doctorName").html(doctorInfo.doctor_name);
                $("#headImage").attr("src",doctorInfo.doctor_pic_url);
            
        },
        error : function() {
        }
    }, 'json');
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
//订单单价,账户余额,订单id,微信需支付
//var chargePrice,patient_register_service_id,needPayMoney;
//页面初始化执行,用户初始化页面参数信息以及微信的支付接口
var doRefresh = function(){
    var timestamp;//时间戳
    var nonceStr;//随机字符串
    var signature;//得到的签名
    var appid;//得到的签名
    $.ajax({
        url:"/keeper/wechatInfo/getConfig",// 跳转到 action
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
}

function updateCustomerInfo() {
    customerId = GetQueryString("customerId");
    consultStatus=GetQueryString("consultStatus");
    var redPacket;
    if(moneyNum == 0){
        redPacket = $("#getMoney").val();
    }else{
        redPacket = moneyNum;
    }
    redPacket = parseFloat(redPacket);
    if (redPacket != "" && redPacket > 0  ) {
        if(redPacket>200){
            alert("感谢您的支持,目前最大金额为200哦!");
            return;
        }
        /*var num = new Number(redPacket);
        redPacket = num.toFixed(1);*/
        recordLogs("ZXPJSXY_JE");
        $.ajax({
            url:"account/user/customerPay",// 跳转到 action
            async:true,
            type:'get',
            data:{patientRegisterId:customerId,payPrice:redPacket * 100},
            cache:false,
            success:function (data) {
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
                                url: "interaction/user/updateCustomerEvaluation",// 跳转到 action
                                async: false,
                                type: 'POST',
                                data: "{'id':'" + customerId + "','consultStatus':'"+consultStatus+"','redPacket':'" + redPacket + "'}",
                                contentType: "application/json; charset=utf-8",
                                dataType: 'json',
                                success: function (data) {
                                    if (data == "1") {
                                        recordLogs("ZXPJSXY_SXY");
                                        recordLogs("ZXPJSXY_PJ");
                                        window.location.href = "playtour#/playtourShare/" + 2;
                                    }
                                },
                                error: function () {
                                }
                            }, 'json');
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
}

var goShare = function () {

    window.location.href="../keeper/playtour#/playtourShare/6";
}
