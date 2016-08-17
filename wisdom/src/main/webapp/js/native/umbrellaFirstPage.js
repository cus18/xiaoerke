var webpath = "/wisdom";
document.write('<scr'+'ipt src="' + webpath + '/js/libs/ionic.bundle.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery-2.1.3.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-resource.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-sanitize.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-route.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.event.drag-1.5.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.touchSlider.js"></scr'+'ipt>');

var version="b"; /*�����汾*/
var shareUmbrellaId = "0";
var nickName="������";
var openid;

$(document).ready(function() {
    version = GetQueryString("status");
    shareUmbrellaId = GetQueryString("id") == null?120000000:GetQueryString("id");
    recordLogs("UmbrellaShareFirstPage_"+ shareUmbrellaId);
    $.ajax({
        url:"umbrella/getOpenid",// ��ת�� action
        async:true,
        type:'post',
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data.openid=="none"){
                window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?" +
                    "url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrella"+version+"_"+ shareUmbrellaId;
            }
            else{
                openid=data.openid;
                console.log("my test  openid",openid);
                $.ajax({
                    url:"umbrella/getNickNameAndRanking",//
                    async:true,
                    type:'post',
                    data: "{'openid':'"+openid+"'}",
                    //data: "{'openid':'"+data.openid+"'}",
                   // data: "{'openid':'o3_NPwlfeHYBUk3oFOuvhyrfKwDQ'}",
                    cache:false,
                    dataType:'json',
                    contentType: "application/json; charset=utf-8",
                    success:function(data) {
                        console.log("my data",data)
                        if(data.nickName!=""){
                            nickName=data.nickName;
                        }
                        console.log("my nickName",nickName)
                    },
                    error : function() {
                    }
                });
            }
        },
        error : function() {
        }
    });
    //ifExistOrder("1");
});

var umbrellaFirstPageInit = function() {
    $("#NoShareDiv").hide();
    $(".shadow-content").hide();//ÿ��ҳ�����ʱ��������ʾ����
    ifExistOrder("2");
    recordLogs("BHS_HDSY");
    cancelRemind();

    //��ȡ��ҳ����
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

function loadShare(){
    version="a";
    var timestamp;//ʱ���
    var nonceStr;//����ַ���
    var signature;//�õ���ǩ��
    var appid;//�õ���ǩ��
    $.ajax({
        url:"wechatInfo/getConfig",// ��ת�� action
        async:true,
        type:'get',
        data:{url:location.href.split('#')[0]},//�õ���Ҫ����ҳ���url
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data!=null ){
                timestamp=data.timestamp;//�õ�ʱ���
                nonceStr=data.nonceStr;//�õ�����ַ���
                signature=data.signature;//�õ�ǩ��
                appid=data.appid;//appid
                //΢������
                wx.config({
                    debug: false,
                    appId: appid,
                    timestamp:timestamp,
                    nonceStr: nonceStr,
                    signature: signature,
                    jsApiList: [
                        'onMenuShareTimeline',
                        'onMenuShareAppMessage'
                    ] // �����б�
                });
                wx.ready(function () {
                    // 2.2 ��������������Ȧ����ť������Զ���������ݼ��������ӿ�
                    wx.onMenuShareTimeline({
                        title: 'Ϊ����ĺ��ӣ�'+nickName+'��������밮�Ĺ��棬������40����ֽ��ϣ�', // �������
                        link: "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+shareUmbrellaId+"/"+version, // ��������
                        imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // ����ͼ��
                        success: function (res) {
                            recordLogs("BHS_HDSY_FXPYQ_"+shareUmbrellaId);
                            //��¼�û���������
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
                        title: 'Ϊ����ĺ��ӣ�'+nickName+'��������밮�Ĺ��棬������40����ֽ��ϣ�', // �������
                        desc: "�ɱ������й���ͯ�����������Ϸ��𣬾���ֵ��������", // ��������
                        link: "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+shareUmbrellaId+"/"+version, // ��������
                        imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // ����ͼ��
                        success: function (res) {
                            $.ajax({
                                type: 'POST',
                                url: "umbrella/updateBabyUmbrellaInfoIfShare",
                                data:"{'id':'"+shareUmbrellaId+"'}",
                                contentType: "application/json; charset=utf-8",
                                success: function(result){
                                    recordLogs("BHS_HDSY_FXPY_"+shareUmbrellaId);
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
}

function ifExistOrder(load){
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
            }else{
                if(data.type=="pay"){
                    version="a";
                }
                if(load=="2"){
                    $("#NoShareDiv").show();
                    $("#shareDiv").hide();
                }
                shareUmbrellaId=120000000;
            }
            loadShare();
        },
        dataType: "json"
    });
}

/*�������� չ������*/
var lookQuestion = function(index) {
    recordLogs("BHS_HDSY_CJWT");
    $(".questions dt").eq(index).siblings("dt").children("a").removeClass("show");
    $(".questions dd").eq(index).siblings("dd").removeClass("change");
    $(".questions dt a").eq(index).toggleClass("show");
    $(".questions dd").eq(index).toggleClass("change");

}

/*ê������ת*/
var skip=function(item){
    myScroll.scrollToElement('#'+item, 100)
}

/*����������  ����ר�� ΢����ѯ */
var lookOther = function(index) {
    $(".c-shadow").show();
    $(".shadow-content2").eq(index).show();
}

/*������ͯ�ؼ������ƻ���Լ  75�ּ������Ƽ����� ��������*/
var lookProtocol = function(index) {
    recordLogs("BHS_HDSY_CJWT");
    $(".c-shadow").show();
    $(".protocol").eq(index).show();
}

/*�������*/
var goShare = function() {
    $(".c-shadow").show();
    $(".shadow-content.share").show();
    /* ��������İ�*/
    var shareTextArray=[
        "��������൱�ڶ��˸��ؼ����գ���Ѽ�����ܻ���40��һȷ����ܸ�Ǯ���ȱ��տ���ˣ�",
        "ǽ���Ƽ������ǹ�棬�������Ǻ���Ҫ���ǶԺ��Ӻͼ�ͥ�ĸ������Ѿ����������㻹��������",
        "��Ϊ���ӽ������������ȡ��40��Ĵ����Ʒѣ���Ҳ����ɣ�",
        "��Ϊ�����������𣬾�Ȼ��ѻ����40��Ĵ����Ʒѣ�����Ҫ��",
        "��ȡ40��Ĵ����Ʒѣ���һ�������ó��ʱ��ѣ��ȵ��ȵ�����",
        "ÿ�춼�к�����ûǮ�β�������������40�����Ʒѣ��͸��㣡",
        "ûʲô���͵ģ�40��Ĵ����Ʒѣ��͸��㣡",
        "��������������ߣ�����40���ú��ӽ���ȥ�ɳ���",
        "���轭���ȼ�������40��Ĵ����Ʒѣ��������ã�"
    ];
    var randomNum=parseInt(9*Math.random());//�����İ������
    $(".share p").html( shareTextArray[randomNum]);

}

/*�رշ�����ʾ*/
var cancelRemind = function() {
    $(".c-shadow").hide();
    $(".shadow-content").hide();
}

/*��ת������ɹ�ҳ��*/
var u = navigator.userAgent, app = navigator.appVersion;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('linux') > -1; //g
var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios�ն�
var myGuaranteeLock = "true";
var myGuarantee = function() {
    var shareId = GetQueryString("id")==null?120000000:GetQueryString("id");
    if(isAndroid){
        //ͨ��openid ��ȡ��ǰ�û��Ƿ��ע
        $.ajax({
            type: 'POST',
            url: "umbrella/getOpenidStatus",
            contentType: "application/json; charset=utf-8",
            success: function(result){
                var status=result.status;
                if(status=="1"){
                    window.location.href="../wisdom/umbrella#/umbrellaPaySuccess/"+shareId;
                }else{
                    window.location.href = "../wisdom/umbrella#/umbrellaJoin/"+new Date().getTime()+"/"+shareId;
                }
            },
            dataType: "json"
        });
    }else if(isIOS){
        if(myGuaranteeLock=="true"){
            myGuaranteeLock = "false";
        }else{
            //ͨ��openid ��ȡ��ǰ�û��Ƿ��ע
            $.ajax({
                type: 'POST',
                url: "umbrella/getOpenidStatus",
                contentType: "application/json; charset=utf-8",
                success: function(result){
                    var status=result.status;
                    if(status=="1"){
                        window.location.href="../wisdom/umbrella#/umbrellaPaySuccess/"+shareId;
                    }else{
                        window.location.href = "../wisdom/umbrella#/umbrellaJoin/"+new Date().getTime()+"/"+shareId;
                    }
                },
                dataType: "json"
            });
        }
    }
}

var goJoin = function() {
    recordLogs("BHS_HDSY_LJLQ");
    var shareId = GetQueryString("id")==null?120000000:GetQueryString("id");
    window.location.href = "http://s165.baodf.com/wisdom/umbrella#/umbrellaFillInfo/"+shareId+"/a";
}

var GetQueryString = function(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

var recordLogs = function(val){
    $.ajax({
        url:"util/recordLogs",// ��ת�� action
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
