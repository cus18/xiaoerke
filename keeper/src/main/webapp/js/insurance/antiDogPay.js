//全局变量
var babyInfo;
var sex="1";
var parentType=1;
var needPayMoney=19.8;
var ifAddBaby=false;
// 选择宝宝
var selectBaby=function(){
    $(".baby-list").show();
}
// 添加宝宝
var addBaby=function(){
    window.location.href = "http://s68.baodf.com/titan/insurance#/antiDogAddBaby";
}
// 取消选择宝宝
var cancelSelectBaby=function(){
    $(".baby-list").hide();
}
// 选择性别
var selectSex=function(sexx){
    $(".sex a").removeClass('select');
    if(sexx=="boy"){
        $(".sex a").eq(0).addClass('select');
        sex="1";
    }
    else{
        $(".sex a").eq(1).addClass('select');
        sex="0";
    }
}
// 选择父母
var selectParent=function(parent){
    $(".parent a").removeClass('select');
    if(parent=="father"){
        $(".parent a").eq(0).addClass('select');
        parentType=1;
    }
    else{
        $(".parent a").eq(1).addClass('select');
        parentType=0;
    }
}
// 查看订单信息
var lookOrderInfo=function(){
    window.location.href = "http://s68.baodf.com/titan/insurance#/antiDogOrderList";
}
var cancelRemind=function(){
    $(".c-shadow").hide();
    $(".c-remind").hide();
    selectBaby();
}

var openRemind=function(){
    $(".c-shadow").show();
    $(".c-remind").show();
}
// 校验 手机号
var checkPhone= function() {
    var phoneNumber = $("#phone").val();
    if ( phoneNumber.match(/^1[3578]\d{9}$/)){
        $(".tips").hide();
        return;
    } else{
        //$("#phone").focus()
        $(".tips").show();
        $(".tips span").html("手机号不正确");
    }
}

//校验身份证号
var checkIdCard= function(){
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

/*
 以前支付代码
 */
// 订单单价,账户余额,订单id,微信需支付
//var chargePrice,patient_register_service_id,needPayMoney;
//页面初始化执行,用户初始化页面参数信息以及微信的支付接口
var doRefresh = function(){
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

var GetQueryString = function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

$(function(){
    loginCheck();
});

function getBabyInfo(){
    $.ajax({
        type: 'POST',
        url: "healthRecord/getBabyinfoList",
        data: "{'openid':''}",
        contentType: "application/json; charset=utf-8",
        success: function(result){
            babyInfo=result.babyInfoList;
            var userPhone=result.userPhone;
            $("#phone").val(userPhone);
            var option="";
            if(babyInfo==""){
                loadDate();
                return;
            }else{
                $("#babyName").attr("disabled","disabled");
                $(".sex a").removeAttr("onclick");
            }
            for(var i=0;i<babyInfo.length;i++){
                option+="<dd class=\"select\" onclick=\"selectedBaby("+i+")\" ><span >"+babyInfo[i].name+"</span></dd>";
            }
            $("#selectBabyTitle").after(option);
            var babyId=GetQueryString("babyId");
            if(babyId!=null&&babyId!=""){
                    for(var j=0;j<babyInfo.length;j++){
                        var bid=babyInfo[j].id;
                        if(bid==babyId){
                            selectedBaby(j);
                        }
                    }
            }else{
                selectedBaby(0);
            }
        },
        dataType: "json"
    });
}

function selectedBaby(index){
    var baby=babyInfo[index];
    $("#babyName").val(baby.name);
    $("#birthday").val(new Date(baby.birthday).Format("yyyy-MM-dd"));
    var sex=baby.sex;
    if(sex=="1"){
        selectSex('boy');
    }else {
        selectSex('girl');
    }
    $(".sex a").removeAttr("onclick");
    $("#babyId").val(baby.id);
    $(".baby-list").hide();
}



function payInsurance(){
	var last=new Date();
	var now=$("#birthday").val();
	now=new Date(Date.parse(now.replace(/-/g, "/")));
	var days=compareDate(new Date(now).Format("yyyy-MM-dd"),new Date(last).Format("yyyy-MM-dd"));
	var fourthDay=14*365;
	if(days>fourthDay){
		alert("目前还只服务于0-14岁的宝宝哦~ ");
		return;
	}
	if(checkPhone()==false){
		return;
	}
	if(checkIdCard()==false){
		return;
	}
     recordLogs("FQB_FWXQ_WXZF");
     var wechatInfo = navigator.userAgent.match(/MicroMessenger\/([\d\.]+)/i) ;
     if ( wechatInfo[1] < "5.0" ) {
         alert("请使用微信5.0以上版本进行支付") ;
         return ;
     }
    var name=$("#babyName").val();
    var birthDay=$("#birthday").val();
    var babyid=$("#babyId").val();
  //不为空则无需添加宝宝信息
    if(typeof(babyid)!='undefined'&&babyid!=""&&ifAddBaby==false){
    	//验证此宝宝是否以存在保险订单
        $.ajax({
            type: 'POST',
            async:false,
            url: "insurance/getInsuranceRegisterServiceIfValid",
            data: "{'babyId':'"+babyid+"','insuranceType':'1'}",
            contentType: "application/json; charset=utf-8",
            success: function(result){
                if(result.valid!=""){
                    openRemind();
                    return;
                }else{
                	//如不存在则添加保险信息
                    var idCard=$("#IdCard").val();
                    var parentName=$("#parentName").val();
                    var parentPhone=$("#phone").val();
                    if(typeof(idCard)=='undefined'||idCard==""){
                        alert("请输入您的身份证号");
                        return;
                    }
                    if(typeof(parentPhone)=='undefined'||parentPhone==""){
                        alert("请输入手机号");
                        return;
                    }
                    if(typeof(parentName)=='undefined'||parentName==""){
                        alert("请输入监护人姓名");
                        return;
                    }
                    //添加保险信息
                        $.ajax({
                            type: 'POST',
                            async:false,
                            url: "insurance/saveInsuranceRegisterService",
                            data: "{'babyId':'"+babyid+"','idCard':'"+idCard+"','parentPhone':'"+parentPhone+"','insuranceType':'1','parentName':'"+parentName+"','parentType':'"+parentType+"'}",
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
                                                                window.location.href="http://s68.baodf.com/titan/insurance#/antiDogPaySuccess/"+insuranceId;
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
            },
            dataType: "json"
        });
    }else{
    	if(typeof(name)=='undefined'||name==''){
            alert("请选择或添加一个宝宝");
            return;
        }
        var idCard=$("#IdCard").val();
        var parentName=$("#parentName").val();
        var parentPhone=$("#phone").val();
        if(typeof(idCard)=='undefined'||idCard==""){
            alert("请输入您的身份证号");
            return;
        }
        if(typeof(parentPhone)=='undefined'||parentPhone==""){
            alert("请输入手机号");
            return;
        }
        if(typeof(parentName)=='undefined'||parentName==""){
            alert("请输入监护人姓名");
            return;
        }
        //babyid为空则说明没有宝宝信息，需要添加一个
        if(typeof(babyid) == "undefined"||babyid==""){
            $.ajax({
                type: 'get',
                async:false,
                url: "healthRecord/saveBabyInfo",
                data: {'name':encodeURI(name),'sex':sex,'birthDay':birthDay},
                contentType: "application/json; charset=utf-8",
                success: function(result){
                    if(result.autoId!=""){
                    	ifAddBaby=true;
                        babyid=result.autoId;
                        $.ajax({
                            type: 'POST',
                            async:false,
                            url: "insurance/saveInsuranceRegisterService",
                            data: "{'babyId':'"+babyid+"','idCard':'"+idCard+"','parentPhone':'"+parentPhone+"','insuranceType':'1','parentName':'"+parentName+"','parentType':'"+parentType+"'}",
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
                                                wx.chooseWXPay({
                                                    appId:obj.appId,
                                                    timestamp:obj.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                                                    nonceStr:obj.nonceStr,  // 支付签名随机串，不长于 32 位
                                                    package:obj.package,// 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                                                    signType:obj.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                                                    paySign:obj.paySign,  // 支付签名
                                                    success: function (res) {
                                                        if(res.errMsg == "chooseWXPay:ok" ) {
                                                        	window.location.href="http://s68.baodf.com/titan/insurance#/antiDogPaySuccess/"+insuranceId;
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
                },
                dataType: "json"
            });
        }
    }
}
//转换时间格式
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

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
        dateFormat: 'yyyy-mm-dd', // 日期格式
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
            console.log("value",valueText);
        },
        onCancel: function () {
        }
    };
    $("#birthday").mobiscroll(opt);
}
//记录日志
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
}

function loginCheck(){
    $.ajax({
        url: 'auth/info/loginStatus',
        type: 'post',
        data: {},
        complete: function(jqXHR){
            if(jqXHR.status=="404"){
                window.location.href = "http://s251.baodf.com/keeper/wxPay/patientPay.do?serviceType=antiDogPay";
            }
        },
        success:function(data){
            var param = '{routePath:"/wxPay/patientPay.do?serviceType=antiDogPay"}';
            $.ajaxSetup({
                contentType : 'application/json'
            });
            $.post('auth/info/loginStatus',param,
                function(data) {
                    if(data.status=="9"){
                        window.location.href = (data.redirectURL)
                    }else if(data.status=="20"){
                        if(data.openId=="noOpenId"){
                            window.location.href = "http://s251.baodf.com/keeper/wechatInfo/" +
                                "fieldwork/wechat/author?url=http://s251.baodf.com/" +
                                "keeper/wechatInfo/getUserWechatMenId?url=26";
                        }else{
                            getBabyInfo();
                        }
                    }
                }, 'json');
        }
    });

}

//计算两个日期的时间间隔 
function compareDate(start,end){ 
    if(start==null||start.length==0||end==null||end.length==0){ 
        return 0; 
    } 
     
    var arr=start.split("-");  
    var starttime=new Date(arr[0],parseInt(arr[1]-1),arr[2]);  
    var starttimes=starttime.getTime(); 
     
    var arrs=end.split("-");  
    var endtime=new Date(arrs[0],parseInt(arrs[1]-1),arrs[2]);  
    var endtimes=endtime.getTime(); 
     
    var intervalTime = endtimes-starttimes;//两个日期相差的毫秒数 一天86400000毫秒 
    var Inter_Days = ((intervalTime).toFixed(2)/86400000)+1;//加1，是让同一天的两个日期返回一天 
     
    return Inter_Days; 
} 