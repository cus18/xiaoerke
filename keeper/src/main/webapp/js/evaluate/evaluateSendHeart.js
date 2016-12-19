//全局变量
var  customerId;//此次咨询会话的ID
var sessionId ; //咨询ID
var starNum1=3;//对医生的评价，0无评价 1不满意 3满意 5非常满意

var redPacket =0; // 最终支付钱数

var consultStatus = "" ;

var sendHeartInit= function (){
    customerId=GetQueryString("customerId");
    sessionId=GetQueryString("sessionId");
    consultStatus=GetQueryString("consultStatus");
    getCustomerEvaluation();//获取当前会话中医生的信息
    weChatInit();// 初始化微信配置信息

    console.log("11 customerId ",customerId);
    $('#content').bind('input propertychange',function(){
        $(".commit").attr('disabled',false);
        /*if($('#content').val()==""){
            if(redPacket>0){
                $(".commit").attr('disabled',false);
                console.log("评价为空时，钱数大于0");

            }
            else{
                $(".commit").attr('disabled',true);
                console.log("评价为空时，钱数等于0");
            }
        }
        else{
            $(".commit").attr('disabled',false);
            console.log("评价输入中。。。");

        }*/
    });
}


//获取页面参数值
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
// 根据页面参数获取医生信息
function getCustomerEvaluation(){
    recordLogs("ZXPJXX_PJ");
    consultStatus=GetQueryString("consultStatus");

    var data = {'id':customerId};
    $.ajax({
        url:"interaction/user/findCustomerEvaluation",// 跳转到 action
        type:'POST', //GET
        data:JSON.stringify(data),
        dataType:'json',
        contentType: "application/json;charset=UTF-8",
        success:function(data) {
            console.log("医生评价信息",data);
            $(".doctorName").html(data.doctorHeadImage.doctor_name);
            $("#headImage").attr("src",data.doctorHeadImage.doctor_pic_url);
            // $("#redPacket").html(data.starInfo.serverId);
            $("#evaluateSendHeart").html(data.serverNum);
            $("#starInfo").html(parseFloat(data.starInfo.startNum)*100+"%");
            if(data.evaluation.serviceAttitude!=0){
                window.location.href = "wxPay/patientPay.do?serviceType=playtourPay&customerId="+customerId+"&consultStatus="+consultStatus;
            }
            else{
                $(".main-box").show();
                $(".commit").show();

            }

        },
        error : function() {
        }
    }, 'json');
}
/* 点击服务不好 */
function unSatisfy(){
    window.location.href="../keeper/playtour#/evaluateUnSatisfy/"+customerId+"/"+sessionId+"/"+consultStatus;
}
/* 选择心意钱数 */
function selectMoney(index,moneyItem){
    $(".commit").attr('disabled',false);
    $(".picMoney-list li").children(".select").hide();
    if( redPacket==moneyItem){
        $(".picMoney-list li").eq(index).children(".select").hide();
        redPacket=0;
        /*$(".commit").attr('disabled',true);*/
       /* if($('#content').val()==""){
            $(".commit").attr('disabled',true);
            console.log("选择钱数时。钱数等于0时，评价内容为空");
        }*/

    }
    else{
        $(".picMoney-list li").eq(index).children(".select").show();
        redPacket=moneyItem;
      /*  $(".commit").attr('disabled',false);*/
        console.log("选择钱数时，钱数大于0");
    }
    /*console.log("选择钱数",redPacket);*/

}
//提交评价
function commitEvaluate(){
    $(".commit").attr('disabled',true);
    var content=$("#content").val();
    consultStatus=GetQueryString("consultStatus");
    if (redPacket != "" && redPacket > 0  ) {
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
                            var data = {'id':customerId,'consultStatus':consultStatus,'starNum1':starNum1,'content':content,'redPacket':redPacket,'sessionId':sessionId};
                            $.ajax({
                                url:"interaction/user/updateCustomerEvaluation",// 跳转到 action
                                type:'POST',
                                data:JSON.stringify(data), 
                                contentType: "application/json; charset=utf-8",
                                dataType:'json',
                                success:function(data) {
                                    if(data=="1"){
                                        recordLogs("ZXPJSXY_SXY");
                                        recordLogs("ZXPJSXY_PJ");
                                        /* window.location.href = "playtour#/playtourShare/"+3;*/
                                        window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_YQY_WXCD";
                                    }
                                    if(data=="2"){
                                        window.location.href = "playtour#/evaluateSuccess";
                                    }
                                },
                                error : function(res) {
                                    recordLogs("PAY_ERROR1:"+res.errMsg);
                                }
                            }, 'json');
                        }else{
                            recordLogs("PAY_ERROR2:"+res.errMsg);
                            alert("支付失败,请重新支付")
                        }
                    },
                    fail: function (res) {
                        alert(res.errMsg)
                        recordLogs("PAY_ERROR3:"+res.errMsg);
                    }
                });
            },
            error : function(res) {
                recordLogs("PAY_ERROR4:"+res.errMsg);
            }
        });
    }
    else{
        var data = {'id':customerId,'consultStatus':consultStatus,'starNum1':starNum1+"",'content':content,'redPacket':redPacket,'sessionId':sessionId};
        $.ajax({
            url:"interaction/user/updateCustomerEvaluation",// 跳转到 action
            type:'POST',
            data:JSON.stringify(data), 
            dataType:'json', 
            contentType: "application/json;charset=UTF-8",
            success:function(data) {
                if(data=="1"){
                    recordLogs("ZXPJSXY_PJ");
                    /* window.location.href = "playtour#/playtourShare/"+3;*/
                    window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_YQY_WXCD";
                }
                if(data=="2"){
                    window.location.href = "playtour#/evaluateSuccess";
                }
            },
            error : function() {
            }
        }, 'json');
    }
}

//初始化微信的支付接口
var weChatInit = function(){
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