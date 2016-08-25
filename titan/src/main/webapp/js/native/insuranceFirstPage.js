//var Ip = "s251.baodf.com";
var Ip = "localhost:8080";
var Ip1 = "localhost:8080";
var pageInit = function () {
    setLog("FWLB");
    share();
}
//点击图片 查看宝贝保详情
function goLook(n) {
    switch(n)
    {
        case 1:
            window.location.href = "http://"+Ip+"/titan/insurance#/insuranceAntiDog";
            break;
        case 2:
            window.location.href = "http://"+Ip+"/titan/insurance#/insuranceHandFootMouth";
            break;
        case 3:
            window.location.href = "http://"+Ip+"/titan/insurance#/insurancePneumonia";
            break;
        default:
            window.location.href = "http://"+Ip+"/titan/insurance#/insuranceAntiDog";
    }
}

//购买保险列表
function goPay() {
    setLog("FWLB_PEI_");
    window.location.href = "insurance#/insuranceOrderList";
}

function setLog(item) {
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

function share(){
    var share = "http://"+Ip1+"/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=29";
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
                        title: '小儿肺炎宝', // 分享标题
                        link: share, // 分享链接
                        imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance/orderList/insuranceList3.png', // 分享图标
                        success: function (res) {

                            setLog("FYB_LB_FX");
                        },
                        fail: function (res) {
                        }
                    });

                    wx.onMenuShareAppMessage({
                        title: '小儿肺炎宝', // 分享标题
                        desc: '你绝对想不到，导致儿童死亡的疾病排名第一的竟然是常见的肺炎！', // 分享描述
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