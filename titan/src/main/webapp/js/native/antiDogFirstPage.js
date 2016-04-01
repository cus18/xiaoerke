var webpath = "/xiaoerke-insurance-webapp";
document.write('<scr'+'ipt src="' + webpath + '/js/libs/ionic.bundle.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery-2.1.3.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-resource.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-sanitize.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-route.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.event.drag-1.5.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.touchSlider.js"></scr'+'ipt>');

var readLock = true;
var doRefresh = function(){
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
                        title: '小区里的猫狗打过疫苗吗？遛狗不牵着？野猫到处跑？', // 分享标题
                        link: window.location.href.replace("true","false"), // 分享链接
                        imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2Findex_banner.png', // 分享图标
                        success: function (res) {
                            //记录用户分享文章
                                        recordLogs("FXJG_FXPYQDog");
                        },
                        fail: function (res) {
                        }
                    });

                    wx.onMenuShareAppMessage({
                        title: '小区里的猫狗打过疫苗吗？遛狗不牵着？野猫到处跑？', // 分享标题
                        desc: '小区里的猫狗打过疫苗吗？遛狗不牵着？野猫到处跑？', // 分享描述
                        link:window.location.href.replace("true","false"), // 分享链接
                        imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2Findex_banner.png', // 分享图标
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

var goBuy = function(){
    recordLogs("FQB_FWXQ_LJGM");
    window.location.href = "antiDogPay/patientPay.do";
};

// 点击已阅读
var read = function(){
    if(readLock){
        $("#readBuy").attr("disabled",false);
        $("#readLock").show();
        readLock =false;
    } else{
        $("#readBuy").attr("disabled",true);
        $("#readLock").hide();
        readLock = true;
    }
};
//点击页面右下角 赔
var goPay = function(){
    recordLogs("FQB_FWXQ_PEI");
    window.location.href = "insurance#/antiDogOrderList";
};
