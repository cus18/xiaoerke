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

var heightForecastFirstInit = function () {
    recordLogs('YYHD_SG_YDY');
};
var goBirthTest = function () {
    recordLogs('YYHD_SG_YDY_YCS');
    window.location.href="heightForecast#/heightForecastBirth"
};
var goNoBirthTest = function () {
    recordLogs('YYHD_SG_YDY_WCS');
    window.location.href="heightForecast#/heightForecastNoBirth"
};

//分享到朋友圈或者微信
var loadShare = function(){
    var share = 'http://120.25.161.33/wisdom/firstPage/heightForecast';
    version="a";
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
                        title: '想知道宝宝能长多高，做个测试就知道', // 分享标题
                        link: share, // 分享链接
                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2FheightForecast.png', // 分享图标
                        success: function (res) {
                            recordLogs("YYHD_SG_FXPYQ");
                        },
                        fail: function (res) {
                        }
                    });
                    wx.onMenuShareAppMessage({
                        title: '想知道宝宝能长多高，做个测试就知道', // 分享标题
                        desc: '哇塞，我家宝宝居然能长这么高？据说99.8%精准哦！', // 分享描述
                        link:share, // 分享链接
                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2FheightForecast.png', // 分享图标
                        success: function (res) {
                            recordLogs("YYHD_SG_FXPP");
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
};
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

