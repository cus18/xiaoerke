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

var attentionLock=true;
var version="b"; /*方案版本*/

var shareUmbrellaId="0";

$(document).ready(function() {
    version = GetQueryString("status");
    shareUmbrellaId = GetQueryString("id")==null?120000000:GetQueryString("id");
    $.ajax({
        url:"umbrella/getOpenid",// 跳转到 action
        async:true,
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.openid=="none"){
                // window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?" +
                //     "url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrella"+version+"_"+ shareUmbrellaId;
                 window.location.href = "http://s2.xiaork.cn/keeper/wechatInfo/fieldwork/wechat/author?url=http://s2.xiaork.cn/keeper/wechatInfo/getUserWechatMenId?url=umbrella"+version+"_"+ shareUmbrellaId;
            }
        },
        error : function() {
        }
    });

    ifExistOrder("1");
});

var umbrellaFirstPageInit = function() {


    $("#NoShareDiv").hide();
    $(".shadow-content").hide();//每次页面加载时先隐藏提示浮层
    ifExistOrder("2");
    recordLogs("BHS_HDSY");
    cancelRemind();
    /*获取当前年月日*/
    var date = new Date();
    date = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
    $("#date").html(date);

    //获取首页数据
    $.ajax({
        type: 'POST',
        url: "umbrella/firstPageDataCount",
        contentType: "application/json; charset=utf-8",
        success: function(result){
            var count=result.count;
            $("#totalUmbrellaMoney").html(result.count*5);
            $("#count").html(count);
        },
        dataType: "json"
    });

    $.ajax({
        type: 'POST',
        url: "umbrella/firstPageDataTodayCount",
        contentType: "application/json; charset=utf-8",
        success: function(result){
            var todayCount=result.todayCount;
            $("#todayCount").html(todayCount);
        },
        dataType: "json"
    });

    // $.ajax({
    //     type: 'POST',
    //     url: "umbrella/firstPageDataTotalUmbrellaMoney",
    //     contentType: "application/json; charset=utf-8",
    //     success: function(result){
    //         var totalUmbrellaMoney=result.totalUmbrellaMoney;
    //         $("#totalUmbrellaMoney").html(totalUmbrellaMoney);
    //     },
    //     dataType: "json"
    // });

    //通过openid 获取当前用户是否关注
    // $.ajax({
    //     type: 'POST',
    //     url: "umbrella/getOpenidStatus",
    //     contentType: "application/json; charset=utf-8",
    //     success: function(result){
    //         var status=result.status;
    //         if(status=="1"){
    //             attentionLock=false;
    //         }
    //     },
    //     dataType: "json"
    // });
    // scanQRCode();
    // $("#readBuy").attr("disabled",false);
    // $("#readLock").show();
}

function scanQRCode(){

    var shareid = GetQueryString("id")==null?120000000:GetQueryString("id");
    $.ajax({
        type: 'POST',
        url: "umbrella/getUserQRCode",
        contentType: "application/json; charset=utf-8",
        async:false,
        data:"{'id':'"+shareid+"'}",
        success: function (data) {
            console.log("s",data.qrcode);
            $("#QRCode").attr("src",data.qrcode);
        },
        dataType: "json"
    });
}

// function  joinUs(){
//     $.ajax({
//         type: 'POST',
//         url: "umbrella/joinUs",
//         data:"{'version':'"+version+"'}",
//         contentType: "application/json; charset=utf-8",
//         async:false,
//         success: function(data){
//             if(data.result==1){
//                 shareUmbrellaId=data.id;
//                 loadShare();
//             }else if(data.result==2){
//                 shareUmbrellaId=data.umbrella.id;
//                 loadShare();
//             }else if(data.result==3){
//                 shareUmbrellaId=data.umbrella.id;
//                 loadShare();
//             }
//         },
//         dataType: "json"
//     });
// }

function loadShare(){
    // if(version=="a"){
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
                        title: '5元＝40万？原来做公益，只要一根雪糕钱！', // 分享标题
                        link: "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+shareUmbrellaId+"/"+version, // 分享链接
                        imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                        success: function (res) {
                            recordLogs("BHS_HDSY_FXPYQ");
                            //记录用户分享文章
                            $.ajax({
                                type: 'POST',
                                url: "umbrella/updateBabyUmbrellaInfoIfShare",
                                data:"{'id':'"+shareUmbrellaId+"'}",
                                contentType: "application/json; charset=utf-8",
                                success: function(result){
                                    var todayCount=result.todayCount;
                                    $("#todayCount").html(todayCount);
                                },
                                dataType: "json"
                            });

                        },
                        fail: function (res) {
                        }
                    });
                    wx.onMenuShareAppMessage({
                        title: '5元＝40万？原来做公益，只要一根雪糕钱！', // 分享标题
                        desc: "我已成为宝护伞互助公益爱心大使，领到了40万的健康保障，你也快来加入吧！", // 分享描述
                        link: "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+shareUmbrellaId+"/"+version, // 分享链接
                        imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                        success: function (res) {
                            $.ajax({
                                type: 'POST',
                                url: "umbrella/updateBabyUmbrellaInfoIfShare",
                                data:"{'id':'"+shareUmbrellaId+"'}",
                                contentType: "application/json; charset=utf-8",
                                success: function(result){
                                    recordLogs("BHS_HDSY_FXPY");
                                    var todayCount=result.todayCount;
                                    $("#todayCount").html(todayCount);
                                },
                                dataType: "json"
                            });
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
    // }else{
    //     var timestamp;//时间戳
    //     var nonceStr;//随机字符串
    //     var signature;//得到的签名
    //     var appid;//得到的签名
    //     $.ajax({
    //         url:"wechatInfo/getConfig",// 跳转到 action
    //         async:true,
    //         type:'get',
    //         data:{url:location.href.split('#')[0]},//得到需要分享页面的url
    //         cache:false,
    //         dataType:'json',
    //         success:function(data) {
    //             if(data!=null ){
    //                 timestamp=data.timestamp;//得到时间戳
    //                 nonceStr=data.nonceStr;//得到随机字符串
    //                 signature=data.signature;//得到签名
    //                 appid=data.appid;//appid
    //                 //微信配置
    //                 wx.config({
    //                     debug: false,
    //                     appId: appid,
    //                     timestamp:timestamp,
    //                     nonceStr: nonceStr,
    //                     signature: signature,
    //                     jsApiList: [
    //                         'onMenuShareTimeline',
    //                         'onMenuShareAppMessage'
    //                     ] // 功能列表
    //                 });
    //                 wx.ready(function () {
    //                     // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
    //                     wx.onMenuShareTimeline({
    //                         title: '我已为宝宝免费领取了一份40万的大病保障，你也赶紧加入吧!', // 分享标题
    //                         link: "../wisdom/umbrella#/umbrellaLead/"+version+"/"+shareUmbrellaId, // 分享链接
    //                         imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
    //                         success: function (res) {
    //                             recordLogs("BHS_HDSY_FXPYQ");
    //                             //记录用户分享文章
    //                             $.ajax({
    //                                 type: 'POST',
    //                                 url: "umbrella/updateBabyUmbrellaInfoIfShare",
    //                                 data:"{'id':'"+shareUmbrellaId+"'}",
    //                                 contentType: "application/json; charset=utf-8",
    //                                 success: function(result){
    //                                     var todayCount=result.todayCount;
    //                                     $("#todayCount").html(todayCount);
    //                                 },
    //                                 dataType: "json"
    //                             });
    //
    //                         },
    //                         fail: function (res) {
    //                         }
    //                     });
    //                     wx.onMenuShareAppMessage({
    //                         title: '我已为宝宝免费领取了一份40万的大病保障，你也赶紧加入吧!', // 分享标题
    //                         desc: "现在加入即可免费获取最高40万60种儿童重疾保障，还等什么，妈妈们 let's go！", // 分享描述
    //                         link: "../wisdom/umbrella#/umbrellaLead/"+version+"/"+shareUmbrellaId, // 分享链接
    //                         imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
    //                         success: function (res) {
    //                             recordLogs("BHS_HDSY_FXPY");
    //                             $.ajax({
    //                                 type: 'POST',
    //                                 url: "umbrella/updateBabyUmbrellaInfoIfShare",
    //                                 data:"{'id':'"+shareUmbrellaId+"'}",
    //                                 contentType: "application/json; charset=utf-8",
    //                                 success: function(result){
    //                                     var todayCount=result.todayCount;
    //                                     $("#todayCount").html(todayCount);
    //                                 },
    //                                 dataType: "json"
    //                             });
    //                         },
    //                         fail: function (res) {
    //                         }
    //                     });
    //                 })
    //             }else{
    //             }
    //         },
    //         error : function() {
    //         }
    //     });
    // }

}

function  ifExistOrder(load){
    $.ajax({
        type: 'POST',
        url: "umbrella/ifExistOrder",
        contentType: "application/json; charset=utf-8",
        success: function(data){
            if(data.result==2||data.result==3){
                if(data.umbrella.version=="a"){
                    version="a";
                }else{
                    version="b";
                }
                if(data.umbrella.pay_result=="fail"){
                    $("#NoShareDiv").show();
                    $("#shareDiv").hide();
                }else if(load=="2"){
                    $("#NoShareDiv").hide();
                    $("#shareDiv").show();
                }
                shareUmbrellaId = data.umbrella.id;
                if(load=="1"){
                    loadShare();
                }

            }else{
                if(data.type=="pay"){
                    version="a";
                }
                if(load=="2"){
                    $("#NoShareDiv").show();
                    $("#shareDiv").hide();
                }
                shareUmbrellaId=120000000;
                if(load=="1"){
                    loadShare();
                }
            }

            /*a版本和b版本的内容变化*/
            /* var content="";
             var textIntro="";
             if(version=="a"){
             content='<img width="60" height="auto" src="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/introPic1a.png" >'
             + '<div class="f4 c3"><span class="c12">5元</span>即加入</div>'
             +'<div class="f4 c3">免单随时享</div>'
             textIntro=' 您只需支付<span class="c11">最多5元即可加入</span>' ;
             $('.introPic li').eq(0).html(content);
             $('#textIntro').html(textIntro);
             $(".helpPlan .pic img").attr("src","http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/help_pic1a.png");
             }
             else{
             content='<img width="60" height="auto" src="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/introPic1b.png" >'
             + '<div class="f4 c3">现在参与</div>'
             +'<div class="f4 c3"><span class="c12">免费</span>加入 </div>';
             textIntro=' <span class="c11">现阶段免费加入</span>' ;
             $('.introPic li').eq(0).html(content);
             $('#textIntro').html(textIntro);
             $(".helpPlan .pic img").attr("src","http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/help_pic1b.png");
             }*/
        },
        dataType: "json"
    });
}

/*重大疾病名称及定义 展开隐藏*/
var lookHelpPlan = function() {
    recordLogs("BHS_HDSY_CJWT");
    $(".helpPlan .fold").toggleClass("show");
    $(".helpPlan .foldText").toggleClass("change");

}

/*运营保障 关怀 展开隐藏*/
var care = function() {
    recordLogs("BHS_HDSY_CJWT");
    $(".operation .text .care1").toggleClass("show");
    $(".operation .text .care2").toggleClass("change");

}

/*常见问题 展开隐藏*/
var lookQuestion = function(index) {
    recordLogs("BHS_HDSY_CJWT");
    $(".questions dt").eq(index).siblings("dt").children("a").removeClass("show");
    $(".questions dd").eq(index).siblings("dd").removeClass("change");
    $(".questions dt a").eq(index).toggleClass("show");
    $(".questions dd").eq(index).toggleClass("change");

}

/*宝大夫儿童重疾互助计划公约  60种重大疾病名称及定义 15种轻症名称及定义 名词释义*/
var lookProtocol = function(index) {

    $(".c-shadow").show();
    if(index==4){
        recordLogs("BHS_HDSY_EJH");
    }else{
        recordLogs("BHS_HDSY_CJWT");
    }
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

    var shareid = GetQueryString("id")==null?120000000:GetQueryString("id");
    window.location.href = "umbrella#/umbrellaJoin/"+new Date().getTime()+"/"+shareid;

}

/*跳转到领取成功页面*/
// var goJoin = function() {
//     recordLogs("BHS_HDSY_LJLQ");
//     var shareid = GetQueryString("id")==null?120000000:GetQueryString("id");
//     if(!attentionLock && version=="a"){
//         window.location.href = "http://s251.baodf.com/keeper/wxPay/patientPay.do?serviceType=umbrellaPay&shareId="+shareid;
//     }else if(!attentionLock){
//         $(".c-shadow").show();
//         $(".shadow-content.attention").show();
//     }else if(version=="b"){
//         window.location.href = "umbrella#/umbrellaJoin/"+new Date().getTime()+"/"+shareid;
//     }else if(version=="a"){
//         window.location.href = "http://s251.baodf.com/keeper/wxPay/patientPay.do?serviceType=umbrellaPay&shareId="+shareid;
//     }
// }

var goJoin = function() {
    recordLogs("BHS_HDSY_LJLQ");
    var shareId = GetQueryString("id")==null?120000000:GetQueryString("id");
    // window.location.href = "http://s165.baodf.com/wisdom/umbrella#/umbrellaFillInfo/"+shareId+"/a";
    window.location.href = "../wisdom/umbrella#/umbrellaFillInfo/"+shareId+"/a";
}

var GetQueryString = function(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
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


var umbrellaConsult = function (){
    window.location.href='http://s165.baodf.com/wisdom/umbrella#/umbrellaTest';
}
