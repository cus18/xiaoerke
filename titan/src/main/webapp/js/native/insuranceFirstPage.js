
var pageInit = function () {
    setLog("FWLB");
    share();
}
//肺炎宝
var goPneumonia = function () {
    window.location.href = "insurance#/pneumoniaIndex";
}

//手足口
var goHandfootmouth = function () {
    window.location.href = "insurance#/handfootmouthIndex";
}
//防犬宝
var goantiDog = function () {
    window.location.href = "firstPage/antiDogFirst";
}
//购买保险列表
var goPay = function () {
    setLog("FWLB_PEI_");
    window.location.href = "insurance#/insuranceOrderList";
}

var setLog = function (item) {
    $.ajax({
        url:"util/recordLogs",// 跳转到 action
        async:true,
        type:'get',
        data:{logContent:encodeURI(item)},
        cache:false,
        dataType:'json',
        success:function(data) {
        },
        error : function() {
        }
    });
}

var share = function(){
    var share = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=29";
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
                        title: '妈妈要当心，儿童最高发的传染病——手足口病又来了，预防和保障一个不能少！', // 分享标题
                        link: share, // 分享链接
                        imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance/orderList/insuranceList3.png', // 分享图标
                        success: function (res) {

                            setLog("FYB_LB_FX");
                        },
                        fail: function (res) {
                        }
                    });

                    wx.onMenuShareAppMessage({
                        title: '小儿手足口宝', // 分享标题
                        desc: '妈妈要当心，儿童最高发的传染病——手足口病又来了，预防和保障一个不能少！', // 分享描述
                        link:share, // 分享链接
                        imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance/orderList/insuranceList3.png', // 分享图标
                        success: function (res) {

                            setLog("FYB_LB_FX");
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