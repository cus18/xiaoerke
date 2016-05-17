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

var knowledgeLoginPageInit = function(){
    $("#birthday").mobiscroll().date();
    var currYear = (new Date()).getFullYear();
    //初始化日期控件
    var opt = {
        preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
        theme: 'android-ics light', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
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
        startYear:2006, //开始年份
        endYear:currYear //结束年份
        //endYear:2099 //结束年份
    };
    $("#birthday").mobiscroll(opt);

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
                        'chooseImage',
                        'previewImage',
                        'uploadImage',
                        'downloadImage'
                    ] // 功能列表
                });
                wx.ready(function () {
                })
            }else{
            }
        },
        error : function() {
        }
    });
}

var mentionCancel = function(){
    $("#mention").hide();
}

var babySex = "1";
var imgFlag = "false";
var imgId = "";
var chooseSex = function(val){
    if(val=='boy'){
        $("#boycir").show();
        $("#girlcir").hide();
        babySex = "1";

    }else if(val=='girl'){
        $("#boycir").hide();
        $("#girlcir").show();
        babySex = "0";

    }
}

var chooseImage = function(){
    wx.chooseImage({
        count: 1, // 默认9
        sizeType: ['original','compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
            $("#imghead").attr('src',res.localIds);
            imgFlag = "true";
            imgId = res.localIds[0];
        }
    });


}

var babyInfoSave = function(){
    if($("#babyName").val()==undefined||$("#birthday").val()==""||$("#babyName").val()==""){
        alert("请您输入宝宝的姓名和生日，谢谢！");
    }else{
        if(imgFlag == "true"){
            //上传图片接口
            wx.uploadImage({
                localId:imgId, // 需要上传的图片的本地ID，由chooseImage接口获得
                isShowProgressTips: 1, // 默认为1，显示进度提示
                success: function (res) {
                    //宝宝信息保存
                    var param = '{"mediaId":"' + res.serverId + '"'+ ',"babyName":"'+ $("#babyName").val() + '","gender":"'+ babySex +
                        '","babyBirthday":'+ $("#birthday").val() + '"}';
                    $.ajaxSetup({
                        contentType : 'application/json'
                    });
                    $.post('knowledge/babyEmr/save',param,
                        function(data) {
                            window.location.href ="firstPage/knowledge?value=251314";
                        }, 'json');
                }
            });

        }else if(imgFlag == "false"){
            var param = '{"babyName":"'+ $("#babyName").val() + '","gender":"'+ babySex +
                '","babyBirthday":'+ $("#birthday").val() + '"}';
            $.ajaxSetup({
                contentType : 'application/json'
            });
            $.post('knowledge/babyEmr/save',param,
                function(data) {
                    window.location.href ="firstPage/knowledge?value=251314";
                }, 'json');
        }
    }
}