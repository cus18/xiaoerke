var babyList = [];
var babySex = 1;//男孩
var parentSex = 0;//妈妈
var needPayMoney =10;//保险金额
var babyID;
var insuranceType="1";
//var Ip = "s68.baodf.com";
var Ip = "s120.xiaork.com";
//var Ip = "localhost:8080";

$(function(){
    insuranceType= GetQueryString("insuranceType");
    if(insuranceType==null){
        window.location.href = "http://"+Ip+"/titan/firstPage/insurance";
    }
    var param = '{routePath:"/wxPay/patientPay.do?serviceType=insurance&insuranceType='+insuranceType+ '"}';
    $.ajax({
        type: "POST",
        url: "auth/info/loginStatus",
        contentType: 'application/json',
        data: param,
        dataType: 'json',
        success: function (data) {
            if (data.status == "9") {
                window.location.href = data.redirectURL;
            } else if (data.status == "8") {
                window.location.href = data.redirectURL;
            } else if (data.status == "20") {
                if(data.openId=="noOpenId"){
                    window.location.href = "http://s251.baodf.com/keeper/wechatInfo/" +
                        "fieldwork/wechat/author?url=http://s251.baodf.com/" +
                        "keeper/wechatInfo/getUserWechatMenId?url=29";
                }else{
                    initWx();//初始化微信
                    getBabyInfo();//获取宝宝信息

                }
            }
        }
    });
   /* initWx();//初始化微信
    getBabyInfo();//获取宝宝信息*/
    initDate();
    recordLogs("FYB_XX");

});
//获取页面参数值
function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null){
        return  unescape(r[2]);
        getPrice();// 获取价格
    }
    return null;
}
//获取保险价格
function getPrice(){
    $.ajax({
        url:"insurance/getInsuranceInfo",//
        async:true,
        type:'post',
        data: "",
        cache:false,
        dataType:'json',
        contentType: "application/json; charset=utf-8",
        success:function(data) {
            if(insuranceType=="1"){
                needPayMoney=parseFloat((data.insuranceInfo.antiDog.split(";"))[0]);
                console.log(needPayMoney);
                if(needPayMoney=="" || needPayMoney==undefined){
                    needPayMoney=19.8;
                }
            }
            if(insuranceType=="2"){
                needPayMoney=parseFloat((data.insuranceInfo.handFootMouth.split(";"))[0]);
                console.log(needPayMoney);
                if(needPayMoney=="" || needPayMoney==undefined){
                    needPayMoney=26.8;
                }
            }
            if(insuranceType=="3"){
                needPayMoney=parseFloat((data.insuranceInfo.pneumonia.split(";"))[0]);
                console.log(needPayMoney);
                if(needPayMoney=="" || needPayMoney==undefined){
                    needPayMoney=68;
                }
            }
            $("#payButton").text(" 微信支付  ￥"+needPayMoney);
        },
        error : function() {
        }
    });
}
//初始化微信

function isHaveInsurance (object) {
    $.ajax({
        type: 'POST',
        url: "insurance/getInsuranceRegisterServiceIfValid",
        data: "{'babyId':'"+object.id+"','insuranceType':'"+insuranceType+"'}",
        //data: "{'babyId':'"+object.id+"','insuranceType':'3'}",
        contentType: "application/json; charset=utf-8",
        success: function(data){
            getBaby(object);
            if(data.valid!=0){
                $('#getRemind').show();
                $('#getShadow').show();
            }else{
                $('#getRemind').hide();
                $('#getShadow').hide();
            }
        },
        dataType: "json"
    });
}

//选择宝宝

function toggleBaby(){
    $('#getBaby').toggle();
}
//选择已有的宝宝
function selectedBaby  (index) {
    isHaveInsurance(babyList[index]);
    toggleBaby();
}

//添加宝宝
function addBaby (){
    window.location.href = "http://"+Ip+"/titan/insurance#/insuranceAddBaby/insurance";
}
//取消选择宝宝

//孩子性别
function selectSex(item) {
    babySex=item;
    $('.sex a').removeClass('select').eq(1-item).addClass('select');
}
//父母性别
function selectParent (item) {
    parentSex=item;
    $('.parent a').removeClass('select').eq(1-item).addClass('select');
}

//显示宝宝信息
function getBaby(object) {
    $('#babyName').val(object.name);
    $('#birthday').val(moment(object.birthday).format("YYYY-MM-DD"));
    babyID = object.id;
    $('.sex a').removeClass('select').eq(1-parseInt(object.sex)).addClass('select');

}
//查看订单
function lookOrderInfo() {
    window.location.href = "http://"+Ip+"/titan/insurance#/insuranceOrderList";
}

//取消重新填写宝宝信息
function cancelRemind() {
    $('#getRemind').hide();
    $('#getShadow').hide();
    $('#babyName').val("");
    $('#birthday').val("");
    babySex = 1;
    $('.sex a').eq(0).addClass('select');
}

//支付
function payInsurance() {
    var flag = 0;
    if($('#babyName').val()!=undefined&&$('#babyName').val()!=""&&$('#birthday').val()!=undefined&&$('#birthday').val()!=""
        &&$("#parentName").val()!=undefined&&$("#parentName").val()!=""&&$("#IdCard").val()!=undefined&&$("#IdCard").val()!=""){
        if(checkIdCard()==false){
            return;
        }
        $.each(babyList, function (index,value) {
            if(value.name!=$('#babyName').val()){
                flag++;
            }else{
                return;
            }
        });
        if(flag==babyList.length){
            saveBaby($('#babyName').val(),babySex.toString(),$('#birthday').val());
        }else{
            payLast(babyID,babySex.toString(),$('#birthday').val(),$("#IdCard").val(),$('#phone').val(),$("#parentName").val(),parentSex.toString());
        }
    }else{
        alert("信息不能为空！");
    }
}
//保存宝宝信息
function saveBaby (name,sex,birthday) {
    $.ajax({
        type: 'GET',
        url: "healthRecord/saveBabyInfo",
        data: {name:encodeURI(name),sex:sex,birthDay:birthday},
        contentType: "application/json; charset=utf-8",
        success: function(data){
            if(data.resultCode=='1'){
                payLast(data.autoId,babySex.toString(),$('#birthday').val(),$("#IdCard").val(),$('#phone').val(),$("#parentName").val(),parentSex.toString());
            }else{
                alert("保存信息失败！");
            }
        },
        dataType: "json"
    });
}

//保存订单并进行支付
function payLast(id,babySex,babyBirthday,card,phone,parentname,parentSex) {
    recordLogs("SZKB_DDTX_WXZF");
    $.ajax({
        type: 'POST',
        async:false,
        url: "insurance/saveInsuranceRegisterService",
        data: "{'babyId':'"+id+"','sex':'"+babySex+"','birthday':'"+babyBirthday+"','idCard':'"+card+"','parentPhone':'"+phone+"','insuranceType':'"+insuranceType+"','parentName':'"+parentname+"','parentType':'"+parentSex+"'}",
        contentType: "application/json; charset=utf-8",
        success: function(result){
            if(result.id!=""){
                var insuranceId=result.id;
                $('#payButton').attr('disabled',"true");//添加disabled属性
                $.ajax({
                    url:"account/user/antiDogPay",// 跳转到 action
                    async:true,
                    type:'get',
                    data:{patientRegisterId:insuranceId,payPrice:needPayMoney*100},
                    cache:false,
                    success:function(data) {
                        $('#payButton').removeAttr("disabled");
                        var obj = eval('(' + data + ')');
                        if(parseInt(obj.agent)<5){
                            alert("您的微信版本低于5.0无法使用微信支付");
                            return;
                        }
                        //打开微信支付控件
                        wx.chooseWXPay({
                            appId:obj.appId,
                            timestamp:obj.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                            nonceStr:obj.nonceStr,  // 支付签名随机串，不长于 32 位
                            package:obj.package,// 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                            signType:obj.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                            paySign:obj.paySign,  // 支付签名
                            success: function (res) {
                                if(res.errMsg == "chooseWXPay:ok" ) {
                                    window.location.href="http://"+Ip+"/titan/insurance#/insurancePaySuccess/"+insuranceId;
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
        },
        dataType: "json"
    });
}

function initWx() {
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
}
//获取宝宝基本信息
function getBabyInfo(){
    $.ajax({
        type: 'POST',
        url: "healthRecord/getBabyinfoList",
        data: "{'openid':''}",
        contentType: "application/json; charset=utf-8",
        success: function(data){
            $('#phone').val(data.userPhone);
            if(data.babyInfoList.length!=0){
                babyList = data.babyInfoList;
                getBaby(data.babyInfoList[0]);
                var op ="";
                $.each(data.babyInfoList, function (index,value) {
                    op += "<dd class=\"select\" onclick=\"selectedBaby("+index+")\" ><span >"+value.name+"</span></dd>";
                });
                $("#selectBabyTitle").after(op);
            }else{
                $('.sex a').eq(0).addClass('select');
            }
        },
        dataType: "json"
    });
}
//初始化时间控件
function initDate() {
    var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
    var myYear=date.substring(0,4);
    var myMonth=date.substring(5,7);
    var myDay=date.substring(8,10);
    $("#birthday").mobiscroll().date();
    //初始化日期控件
    var opt = {
        preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
        theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
        display: 'modal', //显示方式 ，可选：modal\inline\bubble\top\bottom
        mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
        lang:'zh',
        dateFormat: 'yyyy-mm-dd', // 日期格式
        setText: '确定', //确认按钮名称
        cancelText: '取消',//取消按钮名籍我
        dateOrder: 'yyyymmdd', //面板中日期排列格式
        dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
        showNow: false,
        nowText: "今",
        // startYear:1980, //开始年份
        // endYear:currYear //结束年份
        minDate: new Date(myYear-14,myMonth-1,myDay),
        maxDate: new Date(myYear, myMonth-1, myDay),
        onSelect: function (valueText) {

        },
        onCancel: function () {
        }
    };
    $("#birthday").mobiscroll(opt);
}

//记录日志
function recordLogs(val){
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
}
//判断宝宝是否购买过保险
//校验身份证号
function checkIdCard(){
    /* $scope.info.IdCardNum=num;*/
    var sId =$("#IdCard").val();

    var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",
        23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",
        41:"河南",42:"湖北", 43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",
        52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",
        71:"台湾",81:"香港",82:"澳门",91:"国外"}
    var iSum=0
    if(!/^\d{17}(\d|x)$/i.test(sId)){
        $(".tips").show();
        $(".tips span").html("身份证号不正确");
        return false;
    }
    sId=sId.replace(/x$/i,"a");
    if(aCity[parseInt(sId.substr(0,2))]==null){
        $(".tips").show();
        $(".tips span").html("身份证号不正确");
        return false;
    }
    sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2));
    var d=new Date(sBirthday.replace(/-/g,"/"))
    if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate())){
        $(".tips").show();
        $(".tips span").html("身份证号不正确");
        return false;
    }
    for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11)
    if(iSum%11!=1){
        $(".tips").show();
        $(".tips span").html("身份证号不正确");
        return false;
    }
    if(true){
        $(".tips").hide();
        return true;
    }

}