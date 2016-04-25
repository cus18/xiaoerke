var byList = [];
var bodBirthday = "";
//进入页面对日期插件以及微信支付进行初始化
var doRefresh = function(){
    //loadDate();//调用日期插件
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
    var param = '{routePath:"/phoneConsultPay/patientPay.do?phoneConDoctorDetail='
                +GetQueryString("phoneConDoctorDetail")+"AAAAAAdoctorId="+GetQueryString("doctorId")+'"}';
    $.ajax({
        type: "POST",
        url: "auth/info/loginStatus",
        contentType: 'application/json',
        data: param,
        dataType:'json',
        success: function (data) {
            if (data.status == "9") {
                window.location.href = data.redirectURL;
            } else if (data.status == "8") {
                window.location.href = data.redirectURL;
            } else if (data.status == "20") {

                //获取医生个人信息
                $.ajax({
                    url: "consultPhone/consultPhoneDoctor/doctorDetail",// 跳转到 action
                    async: true,
                    type: 'get',
                    data: {doctorId: GetQueryString("doctorId")},
                    cache: false,
                    dataType: 'json',
                    success: function (data) {
                        console.log("data", data);
                        $('#doctorName').html(data.doctorName);
                        $('#position').html(data.position1 + data.position2);
                        $('#hospitalName').html(data.hospitalName);
                        $('#department').html(data.doctor_expert_desc);
                        $('#ServerLength').html(data.ServerLength);
                        $('#price').html(data.price);
                        $('#payPrice').html(data.price);
                        $("#photo").attr("src", "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/" + data.doctorId + "?ver==1.0.2");
                    },
                    error: function () {
                    }
                });

                //预约时间GetQueryString("phoneConDoctorDetail")
                var param = {consultPhoneServiceId: GetQueryString("phoneConDoctorDetail")};
                $.ajax({
                    url: "consultPhone/phoneRegister/getRegisterInfo",// 跳转到 action
                    async: true,
                    type: 'post',
                    data: param,
                    success: function (data) {
                        $('#time').html(moment(data.date).format('YYYY/MM/DD'));
                        $('#begintime').html(moment(data.begintime).format('HH:mm'));
                        $('#endtime').html(moment(data.endtime).format('HH:mm'));
                    },
                    error: function () {
                    }
                });

                //获取宝宝信息
                $.ajax({
                    type: 'POST',
                    url: "healthRecord/getBabyinfoList",
                    data: "{'openid':''}",
                    contentType: "application/json; charset=utf-8",
                    success: function (result) {
                        console.log(result)
                        babyInfo = result.babyInfoList;
                        var userPhone = result.userPhone;
                        $('#connectphone').val(userPhone);
                        var option = "";
                        if (babyInfo == "") {
                            $("#addBaby").hide();
                            loadDate();
                            return;
                        } else {
                            $("#babyName").attr("disabled", "disabled");
                            $(".sex a").removeAttr("onclick");
                        }
                        for (var i = 0; i < babyInfo.length; i++) {
                            option += "<dd class=\"select\" onclick=\"choiceBabyss(" + i + ")\" ><span >" + babyInfo[i].name + "</span></dd>";
                        }
                        $("#selectBabyTitle").after(option);
                        var babyId = GetQueryString("babyId");
                        if (babyId != null && babyId != "") {
                            for (var j = 0; j < babyInfo.length; j++) {
                                var bid = babyInfo[j].id;
                                if (bid == babyId) {
                                    choiceBabyss(j);
                                }
                            }
                        } else {
                            choiceBabyss(0);
                        }
                    },
                    dataType: "json"
                });
            }
        }
    });

}

var goFirstPage=function(){
    window.location.href="/titan/firstPage/phoneConsult"
}
// 点击跳转到个人中心
var goMyselfCenter=function(){
    window.location.href="/titan/phoneConsult#/selfCenter"
}


