//全局变量
var  customerId;//此次咨询会话的ID
var sessionId ; //咨询ID
var starNum1=3;//对医生的评价，0无评价 1不满意 3满意 5非常满意
var noManYi = [];
//医生提示语数组
var showDocList = ["他们说我收到心意后开心得像个小孩子","宝宝在长大，医生会变老","谢谢妈妈们的好评和心意","让宝宝更健康是宝大夫团队的信仰"];
//随机钱数和图片数组
var randomMoneyList = [
    {
        pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/heart1_1.1.png",
        money:1.1
    },
    {
        pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/heart2_2.8.png",
        money:2.8
    },
    {
        pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/heart3_5.2.png",
        money:5.2
    },
    {
        pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/heart4_6.6.png",
        money:6.6
    },
    {
        pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/heart5_8.8.png",
        money:8.8
    },
    {
        pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/heart6_26.6.png",
        money:26.6
    },
];
var randomMoney ;//随机钱数和图片数组 下标值
var randomMoneyItem;// 随机生成钱数和图片选项
var redPacket ; // 最终支付钱数

var sendHeartInit= function (){
    getCustomerInfo();//获取当前会话中医生的信息
    weChatInit();// 初始化微信配置信息
    customerId=GetQueryString("customerId");
    sessionId=GetQueryString("sessionId");

}
//点击选择是否满意
function setEvaluate(index) {
    console.log("setEvaluate 被调用");
    $(".evaluation-remind").hide();// 隐藏 图片 ‘放心评价，宝大夫信任您’
    $(".main-under").show();
    $(".select-content").hide().eq(index).show();
    if(index==0){
        $(".evaluate-item img").eq(1).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/satisfy2_no.png");
        $(".evaluate-item img").eq(2).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/satisfy3_no.png");
        $(".evaluate-item img").eq(index).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/satisfy1_yes.png");
        starNum1=1;
        redPacket=0;
        recordLogs("ZXPJSXY_BMY");
    }
    else if(index==1){
        changeDocTitle();
        $(".evaluate-item img").eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/satisfy1_no.png");
        $(".evaluate-item img").eq(2).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/satisfy3_no.png");
        $(".evaluate-item img").eq(index).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/satisfy2_yes.png");
        starNum1=3;
        recordLogs("ZXPJSXY_MY");
    }
    else if(index==2){
        changeDocTitle();
        $(".evaluate-item img").eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/satisfy1_no.png");
        $(".evaluate-item img").eq(1).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/satisfy2_no.png");
        $(".evaluate-item img").eq(index).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/satisfy3_yes.png");
        starNum1=5;
        recordLogs("ZXPJSXY_FCMY");
    }
}

//医生提示语 标题改变
var changeDocTitle = function () {
    var randomNum = parseInt(4*Math.random());
    $(".doctorWord").html(showDocList[randomNum]);
}

//不满意的选项
var setNo = function(index){
    var flag = 0;
    if(noManYi.length==0){
        noManYi.push(index);
        $('.noSatisfy-item span').eq(index).addClass("select");
    }
    else{
        $.each(noManYi, function (num,value) {
            if(value==index){
                $('.noSatisfy-item  span').eq(index).removeClass("select");
                noManYi.splice(num,1);
                flag = 1;
                return;
            }
        });
        if(flag!=1){
            noManYi.push(index);
            $('.noSatisfy-item  span').eq(index).addClass("select");
        }
    }
}

//点击 列出的钱数选项
function selectMoneyItem(index,selectMoney){
    redPacket=selectMoney;
    console.log("选择的钱数",redPacket);
    $(".money-item li").children('a').removeClass("select");
    $(".money-item li").eq(index).children('a').addClass("select");
    $(".randomSelectMoney").html("随机").removeClass("select");
    if(redPacket != "" && redPacket >0 ){
        $(".commit").attr('disabled',false);
    }
}
//点击页面 随机 按钮
function getRandomMoney(){
    $(".c-shadow").show();
    $(".shadow-content").show();
    /* $(".c-shadow").height();*/
    console.log( " height",$(".c-shadow").height())
    /* pageHeight=document.body.clientHeight-200;//获取页面高度*/
    if(  $(".randomSelectMoney").html()=='随机'){
        randomMoney = parseInt(6*Math.random());
        console.log("随机产生的")
    }
    changeRandomInfo();
}
//点击浮层 换一波 按钮
function changeMoney(){
    randomMoney = parseInt(6*Math.random());
    changeRandomInfo();
}
//点击浮层 关闭 按钮
function closeRandomMoney(){
    $(".c-shadow").hide();
    $(".shadow-content").hide();
}
//点击浮层 完成按钮
function finishRandomMoney(){
    $(".c-shadow").hide();
    $(".shadow-content").hide();
    $(".randomSelectMoney").html(randomMoneyList[randomMoney].money+"元").addClass("select");
    $(".money-item li").children('a').removeClass("select");
    redPacket=randomMoneyList[randomMoney].money;
    console.log("选择的钱数",redPacket);
}
//切换浮层里面随机图片和钱数
function changeRandomInfo(){
    randomMoneyItem= ' <img width="200" height="auto" src='+randomMoneyList[randomMoney].pic+'>' +
        '<div class="bold f8 money"> '+ randomMoneyList[randomMoney].money+ '元'+'</div>';
    $(".random-info").html(randomMoneyItem);
}
//校验输入框输入的钱数
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


//获取页面参数值
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
// 根据页面参数获取医生信息
function getCustomerInfo(){
    recordLogs("ZXPJXX_PJ");000
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
            console.log("evaluation.serviceAttitude 的值 ",evaluation.serviceAttitude);
            var star=starInfo.startNum+"";
            $("#redPacket").html(starInfo.redPacket);
            $(".doctorName").html(doctorInfo.doctor_name);
            $("#headImage").attr("src",doctorInfo.doctor_pic_url);
            if(star=="1.00"){
                $("#starInfo").html("100%");
            }else {
                $("#starInfo").html(star.split(".")[1] + "%");
            }

            //根据用户评价情况 判断 显示页面
            $(".evaluate-select").show();
            if(evaluation.serviceAttitude==0){
                $(".evaluation-remind").show();
             }
            else{
                $(".finish-evaluate").show();
                $(".main-under").show();
                $(".headline").hide();
                $(".leave-word").hide();
               if(evaluation.serviceAttitude==1){
                   $(".evaluate-item li").hide().eq(0).show();
                   $(".evaluate-item img").eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/sendHeart/satisfy1_yes.png");
                   $(".select-content").hide()
                   $(".suggest").show();
                   $("#suggestContent").html(evaluation.content);
                   $(".go-share").show();
                   $(".commit").hide();

                }
              if(evaluation.serviceAttitude==3){
                  $(".evaluate-item li").hide().eq(1).show();
                  setEvaluate(1);
                  if(redPacket==undefined){
                      $(".commit").attr('disabled',true);
                  };
                  $(".commit").html("我要送心意");
                }
                if(evaluation.serviceAttitude==5){
                    $(".evaluate-item li").hide().eq(2).show();
                    setEvaluate(2);
                    console.log("redPacket",redPacket);
                    if(redPacket==undefined){
                        $(".commit").attr('disabled',true);
                    };
                    $(".commit").html("我要送心意");
                }
            }

        },
        error : function() {
        }
    }, 'json');
}
/*分享给更多的宝妈*/
function goShare(){
    window.location.href="../keeper/playtour#/playtourShare/6";
}
//提交评价
function updateCustomerInfo(){
    var content=$("#content").val();
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
                            $.ajax({
                                url:"interaction/user/updateCustomerEvaluation",// 跳转到 action
                                async:false,
                                type:'POST',
                                data:"{'id':'"+customerId+"','starNum1':'"+starNum1+"','content':'"+content+"','dissatisfied':'"+noManYi+"','redPacket':'"+redPacket+"','sessionId':'"+sessionId+"'}",
                                contentType: "application/json; charset=utf-8",
                                dataType:'json',
                                success:function(data) {
                                    if(data=="1"){
                                        recordLogs("ZXPJSXY_SXY");
                                        recordLogs("ZXPJSXY_PJ");
                                        window.location.href = "playtour#/playtourShare/"+2;
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
        $.ajax({
            url:"interaction/user/updateCustomerEvaluation",// 跳转到 action
            async:false,
            type:'POST',
            data:"{'id':'"+customerId+"','starNum1':'"+starNum1+"','content':'"+content+"','dissatisfied':'"+noManYi+"','redPacket':'"+redPacket+"','sessionId':'"+sessionId+"'}",
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