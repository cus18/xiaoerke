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
document.write('<scr'+'ipt src="' + webpath + '/js/libs/moment.min.js"></scr'+'ipt>');

var imgList = ["http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FlovePlan%2Fprove1.png",
    "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FlovePlan%2Fprove2.png",
    "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FlovePlan%2Fprove3.png",
    "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FlovePlan%2Fprove4.png",
    "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FlovePlan%2Fprove5.png",
    "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FlovePlan%2Fprove6.png",
    "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FlovePlan%2Fprove7.png"];
var moreLock=false;

$(function(){
    getUserListImage();
    count();
    sumMoney();
    addNoteAndDonation();
    lastNote();
    loadShare();
    getUserInfo();
});

var lovePlanFirsInit = function(){
};

var lookMore = function(){
   if(moreLock){
        moreLock=false;
       $(".pic-list dd:gt(2)").hide();
       $(".lookMore a").html("点击查看全部"+'&nbsp;&nbsp;'+'<i class="ion-ios-arrow-down"> </i>');
   }
    else{
       moreLock=true;
       $(".pic-list dd:gt(3)").show();
       $(".lookMore a").html("收起"+'&nbsp;&nbsp;'+'<i class="ion-ios-arrow-up"> </i>');
       $("html,body").stop().animate({"scrollTop":$("#money").offset.top},0);

   }

};

var goComment = function(){
    $(".c-shadow").show();
    $(".real-edit").show();
};

var cancelComment = function(){
    $(".c-shadow").hide();
    $(".real-edit").hide();
};

var goLovePlanList = function(){
    window.location.href="lovePlan#/lovePlanList"
};

/*var createPoster = function(){
    window.location.href="market#/lovePlanPoster"
};*/

// 点击 我要捐款
var goContribute = function(){
    window.location.href="http://s251.baodf.com/keeper/wxPay/patientPay.do?serviceType=lovePlanPay"
};

// 宝护伞 查看详情
var goUmbrella = function(){
    window.location.href="http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000005/a"
};

//用户内容的初始化
var getUserInfo=function () {
    $.ajax({
        url:"loveMarketing/visitPage",// 跳转到 action
        async:false,
        type:'POST',
        data:"",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            if(data.fault=="null"){
                $(".info").hide();
                $(".info").hide();
            }else{
                $("#friendNums").html(data.friendNum);
                $("#transcend").html(data.transcend);
                $("#loveMoney").html(data.lovemoney);
                $("#headImage").attr("src",data.headImage);
            }
        },
        error : function() {
        }
    }, 'json');
};

//所有的捐款人数
var count=function () {
    $.ajax({
        url:"mutualHelp/donation/count",
        async:false,
        type:'POST',
        data:"{}",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            $("#counts").html(data.count);
        },
        error : function() {
        }
    }, 'json');
};

//所有的捐款总和
var sumMoney=function () {
    $.ajax({
        url:"mutualHelp/donation/sumMoney",
        async:false,
        type:'POST',
        data:"{'page':'first'}",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            var sumMoney = data.count + 20000;
            var length = sumMoney / 2000;
            $(".lovePlanFirst .ruler .line").css('width',length+'%');
            $("#lovemoneyCount").html(sumMoney);
        },
        error : function() {
        }
    }, 'json');
};

//最后的留言内容
var lastNote=function () {
    $.ajax({
        url:"mutualHelp/donation/lastNote",
        async:false,
        type:'POST',
        data:"{}",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            var date = moment(data.createTime).format('HH:mm');
            $("#newNoteContent").html(data.leaveNote);
            $("#createTime").html(date);
            if(data.headImgUrl != ''){
                $("#headImgUrl").attr("src",data.headImgUrl);
            }
        },
        error : function() {
        }
    }, 'json');
};

//发布留言
var addNoteAndDonation = function(){
    var leaveNote = $('#leaveNoteContent').val();
    if(leaveNote != ''){
        $.ajax({
            url:"mutualHelp/donation/addNoteAndDonation",
            type:'POST',
            data: "{'leaveNote':'"+leaveNote+"'}",
            contentType: "application/json; charset=utf-8",
            dataType:'json',
            success:function() {
                lastNote();
                $('#leaveNoteContent').val('');
                cancelComment();
            },
            error : function() {
            }
        }, 'json');
    }
};

//所有捐款的用户的头像
var getUserListImage=function () {
    $.ajax({
        url:"mutualHelp/donation/photoWall",// 跳转到 action
        async:false,
        type:'POST',
        data:"{'pageNo':'6'}",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            var userImage=data.donationList;
            if(userImage.length != 0){
                for(var i=0;i<userImage.length;i++){
                    if(userImage[i].headImgUrl != ''){
                        $("#loverHearts"+i).attr("src",userImage[i].headImgUrl);
                    }
                }
            }
        },
        error : function() {
        }
    }, 'json');
};

//分享到朋友圈或者微信
var loadShare = function(){
    var share = 'http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=32';
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
                        'onMenuShareAppMessage',
                        'previewImage'
                    ] // 功能列表
                });
                wx.ready(function () {
                    // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                    wx.onMenuShareTimeline({
                        title: '我和任泉已为蛋蛋进行了公益捐款，捐款和转发都是献爱心', // 分享标题
                        link: share, // 分享链接
                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Faxjz.jpg', // 分享图标
                        success: function (res) {
                            recordLogs("AXJZ_HDSY_FXPYQ");
                        },
                        fail: function (res) {
                        }
                    });
                    wx.onMenuShareAppMessage({
                        title: '我和任泉已为蛋蛋进行了公益捐款，捐款和转发都是献爱心', // 分享标题
                        desc: '蛋蛋正在接受化疗，你的一个小小善举，就能挽救一个鲜活生命！', // 分享描述
                        link: share, // 分享链接
                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Faxjz.jpg', // 分享图标
                        success: function (res) {
                            recordLogs("AXJZ_HDSY_FXPY");
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

var previewImage = function (index) {
    var img = imgList[index];
    wx.previewImage({
        current: img, // 当前显示图片的http链接
        urls:imgList  // 需要预览的图片http链接列表
    });
};
