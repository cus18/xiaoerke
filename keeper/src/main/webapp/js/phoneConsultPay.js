var GetQueryString = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

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
    var param = '{routePath:"/wxPay/patientPay.do?serviceType=phoneConsultAAAAAAphoneConDoctorDetail='
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
                        $('#department').html(data.departmentName);
                        $('#ServerLength').html(data.ServerLength);
                        $('#price').html(data.price);
                        $('#payPrice').html(data.price);
                        $("#photo").attr("src", "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/" + data.doctorId + "?ver==1.0.2");
                        $('#phoneConDoctorDetail').html(GetQueryString("phoneConDoctorDetail"));
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
                $('#phoneConDoctorDetail').html(GetQueryString("phoneConDoctorDetail"));
            }
        }
    });
    $('#phoneConDoctorDetail').html(GetQueryString("phoneConDoctorDetail"));

}

//  日期插件
function loadDate(){
    var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
    $("#birthday").mobiscroll().date();
    //初始化日期控件
    var opt = {
        preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
        theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
        display: 'modal', //显示方式 ，可选：modal\inline\bubble\top\bottom
        mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
        lang:'zh',
        dateFormat: 'yyyy/mm/dd', // 日期格式
        setText: '确定', //确认按钮名称
        cancelText: '取消',//取消按钮名籍我
        dateOrder: 'yyyymmdd', //面板中日期排列格式
        dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
        showNow: false,
        nowText: "今",
        // startYear:1980, //开始年份
        // endYear:currYear //结束年份
        minDate: new Date(1980,0,1),
        maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10)),
        onSelect: function (valueText) {
            //console.log("value",valueText);
            bodBirthday = valueText;
        },
        onCancel: function () {
        }
    };
    $("#birthday").mobiscroll(opt);
}

// 点击跳转到首页
var goFirstPage=function(){
    window.location.href="http://s68.baodf.com/titan/firstPage/phoneConsult"
}

// 点击跳转到个人中心
var goMyselfCenter=function(){
    window.location.href="http://s68.baodf.com/titan/phoneConsult#/selfCenter"
}

// 点击选择宝宝按钮
var selectBaby=function(){
    $(".baby-list").show();
}

// 从宝宝列表中选择宝宝
var choiceBaby=function(index){
    $(".baby-list").hide();
    $('#connectname').val(byList[index].name);
    $("#birthday").val(byList[index].birthday);
}

var choiceBabyss=function(index){
    var baby=babyInfo[index];
    $("#babyName").val(baby.name);
    var time = moment(baby.birthday).format('YYYY/MM/DD');
    $("#birthday").val(time);
    bodBirthday = time;
    var sex=baby.sex;
    $(".sex a").removeAttr("onclick");
    $("#babyId").val(baby.id);
    $(".baby-list").hide();
}

// 添加宝宝
var addBaby=function(){
    window.location.href = "http://s68.baodf.com/titan/phoneConsult#/phoneConAddBaby/"+GetQueryString("phoneConDoctorDetail")+","+GetQueryString("doctorId");
}

// 取消选择宝宝
var cancelSelectBaby=function(){
    $(".baby-list").hide();
}
// 点击已阅读
var readLock=false;
var read = function(){
    if(readLock){
        $("#readLock").show();
        readLock =false;
    } else{
        $("#readLock").hide();
        readLock = true;
    }
};
//监听 病情描述的长度
var skip=function(item){
       $("html,body").animate({ scrollTop: $("#"+item).offset().top }, 0);
    if(item=="case"){
        caseLength();
    }
}
//监听 病情描述的长度
var caseLength=function(){
    $('#case').bind('input propertychange',function(){
        $(".case a").html($('#case').val().length+"/200");
         console.log($('#case').val().length);
        if($('#case').val().length==200){
            $("#case").attr("readonly","true")
        }
    })
}

//页面初始化执行,用户初始化页面参数信息以及微信的支付接口
var pay = function(){
    if($('#connectphone').val()==""||$('#connectphone').val()==undefined||
        $('#babyName').val()==""||$('#babyName').val()==undefined||
        bodBirthday==""||bodBirthday==undefined
    ){
        alert("信息不能为空");
    }else if($('#case').val().length<10){
        alert("病情不能少于10个字！");
    }else if(readLock){
        alert("请勾选预约须知");
    }else{
        $('#payButton').attr('disabled',"true");//添加disabled属性
        $.ajax({
            url:"consultPhone/consultOrder/createOrder",// 跳转到 action
            async:true,
            type:'post',
            data: "{'babyId':'"+$("#babyId").val()+"','babyName':'"+$("#babyName").val()+"','phoneNum':'"+$("#connectphone").val()+"','illnessDesc':'"+$("#case").val()+"','sysConsultPhoneId':'"+GetQueryString('phoneConDoctorDetail')+"','birthDay':'"+bodBirthday+"'}",
            contentType: "application/json; charset=utf-8",
            cache:false,
            dataType:'json',
            success:function(data) {
                console.log("生产订单",data);
                if("false" != data.state){
                    wxPay(data.resultState);
                }else{
                    alert("预约失败,请重新预约或联系客服");
                }
                // $('#payButton').attr('disabled',"false");//添加disabled属性
            },
            error : function() {
            }
        });
    }

}

var wxPay = function (consultPhoneServiceId) {
    $.ajax({
        url:"account/user/consultPhonePay",// 跳转到 action
        async:true,
        type:'get',
        data:{patientRegisterId:consultPhoneServiceId},
        cache:false,
        success:function(data) {
            var obj = eval('(' + data + ')');
            if(parseInt(obj.agent)<5){
                alert("您的微信版本低于5.0无法使用微信支付");
                return;
            }
            if(obj.false == 'false'){
                if(obj.agent == "6"){
                    alert("支付失败,请重新支付");
                }else if(obj.agent == "7"){
                    alert("该订单已支付,请到我的预约中进行查看");
                }
                return;
            }
            wx.chooseWXPay({
                appId:obj.appId,
                timestamp:obj.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                nonceStr:obj.nonceStr,  // 支付签名随机串，不长于 32 位
                package:obj.package,// 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                signType:obj.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                paySign:obj.paySign,  // 支付签名
                success: function (res) {
                    if(res.errMsg == "chooseWXPay:ok" ) {
                        window.location.href = "http://s68.baodf.com/titan/phoneConsult#/phoneConPaySuccess/"+consultPhoneServiceId;
                    }else{
                        alert("支付失败,请重新支付")
                    }
                },
                fail: function (res) {
                    alert(res.errMsg)
                }
            });
        },
        error : function() {
        }
    });
}

var getCookie = function(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}



