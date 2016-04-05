//全局变量
var  customerId;//此次咨询会话的ID
var starNum1=3;
var noManYi = [];
var moneyNum = 0;


//点击选择是否满意
var setEvaluate = function (index) {
    if(index==0){
        $("#getMoney").val("");
        starNum1=1;
        $('.playtourpj_1 img').attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fbumanyi_xuanzhong_01.png");
        $('.playtourmoney').hide();
        $('.playtourno').show();
        $('.center1 img').attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fmanyiweixuanzhong.png");
        $('.playtourpj_3 img').attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Ffeichangmanyiweixuanzhong.png");
        recordLogs("ZXPJSXY_BMY");
    }else if(index==1){
        starNum1=3;
        $('.playtourmoney').show();
        $('.playtourno').hide();
        $('.playtourpj_1 img').attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fbumanyi_weixuanzhong.png");
        $('.center1 img').attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fmanyixuanzhong_01.png");
        $('.playtourpj_3 img').attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Ffeichangmanyiweixuanzhong.png");
        recordLogs("ZXPJSXY_MY");
    }else if(index==2){
        starNum1=5;
        $('.playtourmoney').show();
        $('.playtourno').hide();
        $('.playtourpj_1 img').attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fbumanyi_weixuanzhong.png");
        $('.center1 img').attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fmanyiweixuanzhong.png");
        $('.playtourpj_3 img').attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Ffeichangmanyiyixuanzhogn01.png");
        recordLogs("ZXPJSXY_FCMY");
    }
}

//不满意的选项
var setNo = function(index){
    var flag = 0;
    if(noManYi.length==0){
        noManYi.push(index);
        $('.com').eq(index).addClass("dianji");
    }else{
        $.each(noManYi, function (inde,value) {
            if(value==index){
                $('.com').eq(index).removeClass("dianji");
                noManYi.splice(inde,1);
                flag = 1;
                return;
            }
        });
        if(flag!=1){
            noManYi.push(index);
            $('.com').eq(index).addClass("dianji");
        }
    }
}

//判断输入心意钱
var moreMoney = function () {
    recordLogs("ZXPJSXY_JE");
    if($('#getMoney').val()>0){
        $('.ptm img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_xuanzhong.png");
    }else{
        $('.ptm img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_bukedian.png");
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
            moneyNum--;
            if(moneyNum==0){
                $('.ptm img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_bukedian.png");
            }
            $('#getMoney').val(moneyNum);
        }
    }else if(index==1){
        moneyNum++;
        $('#getMoney').val(moneyNum);
        $('.ptm img').eq(1).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjia_xuanzhong.png");
        $('.ptm img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_xuanzhong.png");
    }
}

//提交评价
var sumEval = function () {
    window.location.href = "playtour#/playtourShare";
}
$(function(){
    getCustomerInfo();
    setEvaluate(1);
    $("#moneyDiff").hide();
});

var GetQueryString = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
function getCustomerInfo(){
    recordLogs("ZXPJXX_PJ");
    customerId=GetQueryString("customerId");
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
            if(evaluation.serviceAttitude!=0){
                window.location.href = "playtour#/playtourEvaluate/"+customerId;
            }else{
                var star=starInfo.startNum+"";
                $("#redPacket").html(starInfo.redPacket);
                $("#starInfo").html(star.split(".")[0]+"%");
                $("#doctorName").html(doctorInfo.doctor_name);
                $("#headImage").attr("src",doctorInfo.doctor_pic_url);
            }
        },
        error : function() {
        }
    }, 'json');
}
function updateCustomerInfo(){
    customerId=GetQueryString("customerId");
    var content=$("#content").val();
    var redPacket=$("#getMoney").val();
    if(redPacket!=""){
        if(redPacket<0){
            $("#moneyDiff").show();
            return;
        }
        recordLogs("ZXPJSXY_JE");
        $.ajax({
            url:"account/user/customerPay",// 跳转到 action
            async:true,
            type:'get',
            data:{patientRegisterId:customerId,payPrice:redPacket*100},
            cache:false,
            success:function(data) {
                var obj = eval('(' + data + ')');
                if(parseInt(obj.agent)<5){
                    alert("您的微信版本低于5.0无法使用微信支付");
                    return;
                }
                //打开微信支付控件
                wx.chooseWXPay({
                    appId:obj.appId,
                    timestamp:obj.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                    nonceStr:obj.nonceStr,  // 支付签名随机串，不长于 32 位
                    package:obj.package,// 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                    signType:obj.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                    paySign:obj.paySign,  // 支付签名
                    success: function (res) {
                        if(res.errMsg == "chooseWXPay:ok" ) {
                            $.ajax({
                                url:"interaction/user/updateCustomerEvaluation",// 跳转到 action
                                async:false,
                                type:'POST',
                                data:"{'id':'"+customerId+"','starNum1':'"+starNum1+"','content':'"+content+"','dissatisfied':'"+noManYi+"','redPacket':'"+redPacket+"'}",
                                contentType: "application/json; charset=utf-8",
                                dataType:'json',
                                success:function(data) {
                                    if(data=="1"){
                                        recordLogs("ZXPJSXY_SXY");
                                        recordLogs("ZXPJSXY_PJ");
                                        window.location.href = "playtour#/playtourShare/"+2;
                                    }
                                },
                                error : function() {
                                }
                            }, 'json');
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
    }else{
        $.ajax({
            url:"interaction/user/updateCustomerEvaluation",// 跳转到 action
            async:false,
            type:'POST',
            data:"{'id':'"+customerId+"','starNum1':'"+starNum1+"','content':'"+content+"','dissatisfied':'"+noManYi+"','redPacket':'"+redPacket+"'}",
            contentType: "application/json; charset=utf-8",
            dataType:'json',
            success:function(data) {
                if(data=="1"){
                    recordLogs("ZXPJSXY_PJ");
                    window.location.href = "playtour#/playtourShare/"+3;
                }
            },
            error : function() {
            }
        }, 'json');
    }
}
/*
 以前支付代码
 */
//订单单价,账户余额,订单id,微信需支付
//var chargePrice,patient_register_service_id,needPayMoney;
//页面初始化执行,用户初始化页面参数信息以及微信的支付接口
var doRefresh = function(){
    $('.playtourmoney').show();
    $('.playtourno').hide();
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