var webpath = "/wisdom";
document.write('<scr'+'ipt src="' + webpath + '/js/libs/ionic.bundle.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery-2.1.3.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-resource.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-sanitize.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-route.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.event.drag-1.5.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.touchSlider.js"></scr'+'ipt>');

var attentionLock=false;

var umbrellaFirstPageInit = function() {
    /*获取当前年月日*/
    var date = new Date();
     date = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
     $("#date").html(date);

    //通过openid 获取当前用户是否关注
    $.ajax({
        type: 'POST',
        url: "umbrella/firstPageData",
        contentType: "application/json; charset=utf-8",
        success: function(result){
            var count=result.count;
            var todayCount=result.todayCount;
            $("#count").html(count);
            $("#todayCount").html(todayCount);
            $("#totalUmbrellaMoney").html(totalUmbrellaMoney);
        },
        dataType: "json"
    });
    
    //通过openid 获取当前用户是否关注
    $.ajax({
        type: 'POST',
        url: "umbrella/getOpenidStatus",
        contentType: "application/json; charset=utf-8",
        success: function(result){
            var status=result.status;
            if(status=="0"){
                attentionLock=true;
            }
        },
        dataType: "json"
    });

    recordLogs("FQB_FWXQ");
    $("#readBuy").attr("disabled",false);
    $("#readLock").show();
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
                timestamp=data.timestamp;//得到时间戳
                nonceStr=data.nonceStr;//得到随机字符串
                signature=data.signature;//得到签名
                appid=data.appid;//appid
                //微信配置
                wx.config({
                    debug: false,
                    appId: appid,
                    timestamp:timestamp,
                    nonceStr: nonceStr,
                    signature: signature,
                    jsApiList: [
                        'onMenuShareTimeline',
                        'onMenuShareAppMessage'
                    ] // 功能列表
                });
                wx.ready(function () {
                    // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                    wx.onMenuShareTimeline({
                        title: '我为您的宝宝领取了最高40万保障金', // 分享标题
                        link: window.location.href.replace("true","false"), // 分享链接
                        imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                        success: function (res) {
                            //记录用户分享文章
                            recordLogs("FXJG_FXPYQDog");
                        },
                        fail: function (res) {
                        }
                    });

                    wx.onMenuShareAppMessage({
                        title: '我为您的宝宝领取了最高40万保障金', // 分享标题
                        desc: '前20万用户免费加入即可获取最高40万60种儿童重疾保障', // 分享描述
                        link:window.location.href.replace("true","false"), // 分享链接
                        imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                        success: function (res) {
                            recordLogs("FXJG_FXPYDog");
                        },
                        fail: function (res) {
                        }
                    });

                })
            }else{
            }
        },
        error : function() {
        }
    });
}

/*重大疾病名称及定义 展开隐藏*/
var lookHelpPlan = function() {
    $(".helpPlan .fold").toggleClass("show");
    $(".helpPlan .foldText").toggleClass("change");

}
/*运营保障 关怀 展开隐藏*/
var care = function() {
    $(".operation .text .care1").toggleClass("show");
    $(".operation .text .care2").toggleClass("change");

}
/*常见问题 展开隐藏*/
var lookQuestion = function(index) {
    $(".questions dt").eq(index).siblings("dt").children("a").removeClass("show");
    $(".questions dd").eq(index).siblings("dd").removeClass("change");
    $(".questions dt a").eq(index).toggleClass("show");
    $(".questions dd").eq(index).toggleClass("change");

}
/*宝大夫儿童重疾互助计划公约  60种重大疾病名称及定义 15种轻症名称及定义 名词释义*/
var lookProtocol = function(index) {
    $(".c-shadow").show();
    $(".protocol").eq(index).show();

}

/*分享好友*/
var goShare = function() {
    $(".c-shadow").show();
    $(".shadow-content.share").show();
}
/*关闭分享提示*/
var cancelRemind = function() {
    $(".c-shadow").hide();
    $(".shadow-content").hide();
}

/*跳转到参与成功页面*/
var myGuarantee = function() {
    window.location.href = "umbrella#/umbrellaJoin";

}
/*跳转到领取成功页面*/
var goJoin = function() {
   /* window.location.href = "umbrella#/umbrellaJoin";*/
    if(!attentionLock){
        $(".c-shadow").show();
        $(".shadow-content.attention").show();
    }
    else{
        window.location.href = "umbrella#/umbrellaJoin";
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
