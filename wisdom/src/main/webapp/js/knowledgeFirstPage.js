var webpath = "/xiaoerke-knowledge";
document.write('<scr'+'ipt src="' + webpath + '/js/libs/ionic.bundle.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery-2.1.3.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-resource.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-sanitize.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-route.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.event.drag-1.5.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.touchSlider.js"></scr'+'ipt>');

var babyName = "";
var birthday = "";
var babyClassifyImg = [
    "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon1.png" ,
    "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon2.png",
    "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon3.png",
    "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon4.png",
    "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon5.png" ,
    "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon6.png" ,
    "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon7.png" ,
    "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon8.png"
];
var articleNum = 10;

var knowledgeFirstPageInit = function(){
    wxConfig();

    $("#searchHide").bind("touchstart",function(){
        $(".search1").hide();
        $(".search2").show();
    })

    var param = '{}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.get('ap/knowledge/babyEmr/getBabyEmrList',param,
        function(data) {
            if(data.name!=undefined){
                babyName = data.name;
                birthday = data.birthday;
                if(data.status=="0"){
                    $("#babyPhoto").attr('src',"http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fbaby_photo_default.png");
                }else{
                    $("#babyPhoto").attr('src',"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/"+data.id);
                }
                getBirthDayContent();
            }else{
                window.location.href ="firstPage/knowledgeLogin?value=251333";
            }
        }, 'json');
}

var getBirthDayContent = function(){
    var param = '{}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.get('ap/knowledge/knowledgeDict/standardFigure',param,
        function(data) {
            var paramValue = '<span>'+babyName+'</span> <span>'+data.age+'<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fedit_birthday.png" width="10" height="10.5"> <input id="birthday" name="birthday" type="text" readonly="readonly"> </span>'
            $(".font-c1").html(paramValue);
            getBirthDayTime();
            if(data.age.substring(0,1) < 3){
                var valueStandardFigure = data.standardFigure.split(";");
                paramValue = '<span >'+valueStandardFigure[0]+'<a class="line"></a></span><span>'+valueStandardFigure[1]+'</span>';
                $(".font-c2").html(paramValue);

                $.ajax({
                    url:"ap/knowledge/knowledgeDict/dailyRemind",// 跳转到 action
                    async:true,
                    type:'get',
                    data:{birthDay:birthday},
                    cache:false,
                    dataType:'json',
                    success:function(data) {
                        if(data!=null){
                            var val1 = '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fzheng_remind.png" width="17.5" height="18">' +
                                '郑老师提醒 <a class="more" onclick="getWarn()">更多&nbsp;' +
                                '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fzheng_remind_detail.png" width="9.5" height="10.5"></a>';
                            $("#remind1").html(val1);
                            var dailyList = data.dailyRemind_tip.split("dailyRemind");
                            var dailyRemindList = "";
                            $.each(dailyList,function(index,value){
                                var warnIndex = "'" + index + "'";
                                dailyRemindList = dailyRemindList + '<li onclick="getWarn('+warnIndex+')"><span class="cir"></span>'+value+'<i class="ion-ios-arrow-right"></i> </li>';
                            })
                            $('#remind2').html(dailyRemindList);
                            $(".remind").show();
                        }
                    },
                    error : function() {
                    }
                });

                param = '{}';
                $.ajaxSetup({
                    contentType : 'application/json'
                });
                $.post('ap/knowledge/article/todaySelectAndReadArticleList',param,
                    function(data) {
                        paramValue = "";
                        $.each(data.todayReadArticleList,function(index,value){
                            var knowledgeArticleContentParam = "'" + value.id +"','WX'";
                            var linShiParam = "'" + value.category.name +"','" + value.category.id + "'";
                            paramValue = paramValue + '<li class="item item-borderless"><div class="item-thumbnail-left" ' +
                                'onclick="knowledgeArticleContent('+ knowledgeArticleContentParam + ')"> ' +
                                '<img src="http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/'+ value.id + '"> ' +
                                '<h2>' + value.title + '</h2><p>' + getLimitString(value.description) + '</p> ' +
                                '<div class="sub-info"> ' +
                                '<a class="info info-read"> ' +
                                '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fitem_read.png" ' +
                                'width="12.5" height="8.5">&nbsp;'+ value.hits +'</a>' +
                                ' <a class="info info-comment"> ' +
                                '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fitem_comment.png" ' +
                                'width="12.5" height="11.5">&nbsp;' + value.comment + '</a> <a class="info info-share"> ' +
                                '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fitem_share.png" ' +
                                'width="12.5" height="12.5">&nbsp;'+ value.share +'</a> </div> </div> <div class="belong">' +
                                '<span>' + value.category.name + '</span> ' +
                                '<a onclick="linShi('+ linShiParam +')" >查看更多</a> </div> </li>';
                        })
                        $("#todayRead").html(paramValue);

                        paramValue = "";
                        $.each(data.todayReadArticleList,function(index,value){

                            var knowledgeArticleContentParam = "'" + value.id +"','WX'";
                            var lookMoreParam = "'" + value.category.id +"','" + value.category.name + "'";;
                            paramValue = paramValue + '<li class="item item-borderless">' +
                                '<div class="item-thumbnail-left" onclick="knowledgeArticleContent(' + knowledgeArticleContentParam + ')"> ' +
                                '<img src="http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/'+value.id+'"> ' +
                                '<h2 >'+value.title+'</h2> <p>'+ getLimitString(value.description) +' </p> ' +
                                '<p class="sub-info"> <a  class="info info-read"> ' +
                                '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fitem_read.png" ' +
                                'width="12.5" height="8.5">&nbsp;'+value.hits+' </a> <a class="info info-comment"> ' +
                                '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fitem_comment.png" ' +
                                'width="12.5" height="11.5">&nbsp;'+value.comment+' </a> ' +
                                '<a  class="info info-share"> ' +
                                '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fitem_share.png" ' +
                                'width="12.5" height="12.5">&nbsp;'+value.share+' </a> </p> </div> <div class="belong">' +
                                '<span >'+value.category.name+'</span> ' +
                                '<a  onclick="lookMore('+ lookMoreParam +')">查看更多</a> </div> </li>';
                        })
                        $("#todayJingXuan1").html(paramValue);
                    }, 'json');

                var babyClassify = [];
                var syndromeClassify = [];
                var jingId = "";
                param = '{}';
                $.ajaxSetup({
                    contentType : 'application/json'
                });
                $.post('ap/knowledge/category/categoryList',param,
                    function(data) {
                        babyClassify = data.categoryList[4].secondMenuData;//获取育儿宝典内容
                        syndromeClassify = data.categoryList[7].secondMenuData;//常见疾病
                        jingId = data.categoryList[5].firstMenuId;

                        paramValue = "";
                        $.each(babyClassify,function(index,value){
                            var readMoreParam = "'" + value.secondMenuId +"','" + value.secondMenuName + "'";
                            var imgParam = babyClassifyImg[index];
                            paramValue = paramValue + '<a class="myCol" onclick="todayReadMore(' + readMoreParam + ')"> ' +
                                '<img src='+ imgParam +' width="52" height="52"><br> ' +
                                '<span >'+value.secondMenuName+'</span> </a>';
                        })
                        $("#yuer").html(paramValue);

                        paramValue = "";
                        var todayChoiceNewbornParam = "'" + syndromeClassify[0].secondMenuId +"','" + syndromeClassify[0].secondMenuName + "'";
                        var todayChoiceNurslingParam = "'" + syndromeClassify[1].secondMenuId +"','" + syndromeClassify[1].secondMenuName + "'";
                        paramValue = paramValue + '<dt><span class="line"></span> &nbsp;给宝宝看病</dt> ' +
                            '<dd> <h5>常见疾病症状</h5> <div class="myCol2"> ' +
                            '<a  onclick="lookMore('+ todayChoiceNewbornParam +')" ' +
                            'style="margin-right:35px;">'+syndromeClassify[0].secondMenuName +' </a>' +
                            ' <a onclick="lookMore('+todayChoiceNurslingParam +')">' +
                            syndromeClassify[1].secondMenuName +' </a> </div> </dd>';
                        $("#todayJingXuan3").html(paramValue);

                    }, 'json');
            }else{
                $("#todayReadIndex").hide();
                var jingId = "";
                param = '{}';
                $.ajaxSetup({
                    contentType : 'application/json'
                });
                $.post('ap/knowledge/category/categoryList',param,
                    function(data) {
                        jingId = data.categoryList[5].firstMenuId;
                        loadMoreArticle(jingId);
                    }, 'json');
            }

        }, 'json');
}

var loadMoreArticle = function(jingId){
    var param = '{"id":"' + jingId + '"'+ ',"pageNo":'+ 1 + ',"pageSize":'+ articleNum +'}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.post('ap/knowledge/article/articleList',param,
        function(data) {
            var paramValue = "";
            $.each(data.articleList,function(index,value){
                var knowledgeArticleContentParam =  "'" + value.id +"','WX'";
                paramValue = paramValue + '<li class="item item-borderless"> ' +
                    '<div class="item-thumbnail-left" onclick="knowledgeArticleContent('+knowledgeArticleContentParam+')"> ' +
                    '<img src="http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/'+value.id+'">' +
                    '<h2 >'+value.title+'</h2> ' +
                    '<p>' + getLimitString(value.description) + '</p> ' +
                    '<p class="sub-info"> <a  class="info info-read"> ' +
                    '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fitem_read.png"' +
                    ' width="12.5" height="8.5">&nbsp;'+value.articleReadCount+'</a> ' +
                    '<a class="info info-comment">' +
                    '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fitem_comment.png" ' +
                    'width="12.5" height="11.5">&nbsp;'+value.articleCommentCount+'</a> ' +
                    '<a  class="info info-share"> ' +
                    '<img src="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fitem_share.png" ' +
                    'width="12.5" height="12.5">&nbsp;'+value.articleShareCount+'</a></p></div></li> ';
            });
            var jingIdParam = "'" + jingId + "'";
            paramValue = paramValue + '<a class="jingMore" onclick="loadMoreArticle('+jingIdParam+')">加载更多</a>';
            $("#todayJingXuan2").html(paramValue);
            articleNum = articleNum+10;
        }, 'json');
}

var lookMore = function(categoryId,categoryName){
    if(categoryName=="新生儿"){
        window.location.href = "ap/knowledge#/todayChoiceNewborn/" + categoryId + "," + categoryName;
    }
    else if(categoryName=="婴幼儿"){
        window.location.href = "ap/knowledge#/todayChoiceNursling/" + categoryId + "," + categoryName;
    }
    else {
        window.location.href = "ap/knowledge#/otherDisease/" + categoryId + "," + categoryName;
    }
}

var todayReadMore = function(secondMenuId,secondMenuName){
    window.location.href = "ap/knowledge#/todayReadMore/" + secondMenuId + "," + secondMenuName;
}

var knowledgeArticleContent = function(contentId,generalize){
    window.location.href = "ap/knowledge#/knowledgeArticleContent/" + contentId + "," + generalize;
}

var linShi = function(categoryName,categoryId){
    if(categoryName=="精选"){
        window.location.href = "ap/knowledge#/todayChoiceNurslingList/" + categoryName + ",文章," + categoryId;
    }else{
        window.location.href = "ap/knowledge#/todayReadMore/" + categoryId + "," + categoryName;
    }
}

var getBirthDayTime = function(){
    $("#birthday").mobiscroll().date();
    var currYear = (new Date()).getFullYear();
    var opt=(
    {
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
        endYear:currYear, //结束年份
        onSelect: function (valueText, inst) {
            var param = '{"birthday":"' + valueText + '"}';
            $.ajaxSetup({
                contentType : 'application/json'
            });
            $.post('ap/knowledge/babyEmr/updateBabyEmr',param,
                function(data) {
                    //birthday = valueText;
                    location.reload(); //getBirthDayContent();
                }, 'json');
        }
    }
    );
    $("#birthday").mobiscroll(opt);
}

var showSearch = function(){
    $(".search1").show();
    $(".search2").hide();
}

var chooseImage = function(){
    wx.chooseImage({
        count: 1, // 默认9
        sizeType: ['original','compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
            $("#babyPhoto").attr('src',"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/" + res.localIds);
            var imgId = res.localIds[0];
            //上传图片接口
            wx.uploadImage({
                localId: imgId, // 需要上传的图片的本地ID，由chooseImage接口获得
                isShowProgressTips: 1, // 默认为1，显示进度提示
                success: function (res) {
                    var param = '{"mediaId":"' + res.serverId + '"}';
                    $.ajaxSetup({
                        contentType : 'application/json'
                    });
                    $.post('ap/knowledge/babyEmr/updatePic',param,
                        function(data) {
                            knowledgeFirstPageInit();
                        }, 'json');
                }
            });
        }
    });
}

var calculateAge = function(str){
    var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
    if(r==null)return  false;
    var d = new Date(r[1], r[3]-1, r[4]);
    if  (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]) {
        var Y = new  Date().getFullYear();
        return Y-r[1];
    }
    return("输入的日期格式错误！");
}

var getWarn = function(index){
    if(index==undefined){
        index="";
    }
    window.location.href = "ap/knowledge#/knowledgeWarn/" + birthday + "," + index;
}

var getLimitString = function(val){
    if(val.length>30) {
        return val.substring(0,30)+"...";
    }
    else{
        return val;
    }
}

var searchTitle = function(){
    if($("#searchValue").val()!=undefined){
        window.location.href = "ap/knowledge#/knowledgeSearch/" + $("#searchValue").val() + ",WX" ;
    }else{
        alert("请输入搜索内容！");
    }
}

var wxConfig = function(){
    var shareUrl ='http://baodf.com/xiaoerke-knowledge/wechatInfo/fieldwork/wechat/' +
        'author?url=http://baodf.com/xiaoerke-knowledge/wechatInfo/getZhengIndex';
    var timestamp;//时间戳
    var nonceStr;//随机字符串
    var signature;//得到的签名
    var appid;//得到的签名
    $.ajax({
        url:"wechatInfo/getConfig",// 跳转到 action
        async:true,
        type:'get',
        data:{url:window.location.href},//得到需要分享页面的url
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
                        'onMenuShareWeibo',
                        'chooseImage',
                        'uploadImage',
                        'downloadImage'
                    ] // 功能列表
                });
                wx.ready(function () {
                    //分享到朋友圈
                    wx.onMenuShareTimeline({
                        title: '【郑玉巧育儿经】-宝大夫', // 分享标题
                        link: shareUrl, // 分享链接
                        imgUrl: 'http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg', // 分享图标
                        success: function () {
                            //记录用户分享文章
                            var param = '{"contentId":"郑玉巧育儿经首页"}';
                            $.ajaxSetup({
                                contentType : 'application/json'
                            });
                            $.post('ap/knowledge/ArticleShareRecord',param,
                                function(data) {
                                }, 'json');
                        },
                        cancel: function () {
                            // 用户取消分享后执行的回调函数
                        }
                    });
                    //分享给朋友
                    wx.onMenuShareAppMessage({
                        title: '【郑玉巧育儿经】-宝大夫', // 分享标题
                        desc: '智能匹配月龄，获取针对性一对一育儿指导，建立宝宝专属健康档案，一路呵护，茁壮成长！', // 分享描述
                        link: shareUrl, // 分享链接
                        imgUrl: 'http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg', // 分享图标
                        success: function () {
                            //记录用户分享文章
                            var param = '{"contentId":"郑玉巧育儿经首页"}';
                            $.ajaxSetup({
                                contentType : 'application/json'
                            });
                            $.post('ap/knowledge/ArticleShareRecord',param,
                                function(data) {
                                }, 'json');
                        },
                        cancel: function () {
                            // 用户取消分享后执行的回调函数
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