var webpath = "/xiaoerke-marketing-webapp";
document.write('<scr'+'ipt src="' + webpath + '/js/libs/ionic.bundle.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery-2.1.3.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-resource.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-sanitize.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-route.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.event.drag-1.5.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.touchSlider.js"></scr'+'ipt>');


var testList=[
    {
        num:1,
        img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest1.jpg",
        title:"1. 母乳是贫铁的食物吗？",
        questionList:[
            {   select: "A. 是",
                checked:false},
            {   select: "B. 不是",
                checked:false}
        ],
        result:"no",
        answer:0,
        checked:false
    },
    {
        num:2,
        img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest2.jpg",
        title:"2. 宝宝满6月龄后应优先添加什么辅食呢？",
        questionList:[
            {
                select: "A. 富铁食物如强化铁的米粉和肉泥",
                checked:false
            },
            {
                select: "B. 富含维生素、矿物质的蔬菜、水果泥",
                checked:false
            }
        ],
        result:"no",
        answer:0,
        checked:false

    },
    {
        num:3,
        img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest3.jpg",
        title:"3. 宝宝“枕秃”是缺钙引起的吗？",
        questionList:[
            {
                select: "A. 是",
                checked:false
            },
            {select:
                "B. 不一定",
                checked:false
            }
        ],
        result:"no",
        answer:1,
        checked:false
    },
    {
        num:4,
        img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest4.jpg",
        title:"4. 怎么给宝宝转奶呢？",
        questionList:[
            {
                select: "A. 在旧奶粉中少量添加新奶粉，逐渐加量，直至全部添加新奶粉",
                checked:false
            },
            {
                select: "B. 旧奶粉需要停一天，然后在新奶粉中添加一些米粉，再逐步去掉",
                checked:false
            }
        ],
        result:"no",
        answer:0,
        checked:false
    },
    {
        num:5,
        img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest5.jpg",
        title:"5. 宝宝最初的米粉应该怎么吃呢？",
        questionList:[
            {
                select: "A. 做成稀米糊后放进奶瓶里给宝宝吃，让宝宝慢慢适应",
                checked:false
            },
            {
                select: "B. 调成泥状（由稀到稠），用勺子喂给宝宝吃",
                checked:false
            }
        ],
        result:"no",
        answer:1,
        checked:false
    },
    {
        num:6,
        img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest6.jpg",
        title:"6. 宝宝1岁该断奶了吗？",
        questionList:[
            {
                select: "A. 是的，母乳6个月后没营养，而且宝宝依恋母乳，没法儿好好吃饭",
                checked:false
            },
            {
                select: "B. 不是的，母乳喂养应尽量坚持到宝宝满2岁",
                checked:false
            }
        ],
        result:"no",
        answer:1,
        checked:false
    },
    {
        num:7,
        img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest7.jpg",
        title:"7. 宝宝到了一定年龄还需要定量补充奶制品吗？",
        questionList:[
            {
                select: "A. 需要",
                checked:false
            },
            {
                select: "B. 不需要",
                checked:false
            }
        ],
        result:"no",
        answer:0,
        checked:false
    },
    {
        num:8,
        img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest8.jpg",
        title:"8. 牛奶和富含维C的水果或果汁能一起吃吗？",
        questionList:[
            {
                select: "A. 当然可以吃啦，这样会更有利于消化吸收",
                checked:false
            },
            {
                select: "B. 最好不要哦，因为两者会产生化学反应影响吸收",
                checked:false
            }
        ],
        result:"no",
        answer:0,
        checked:false
    },
    {
        num:9,
        img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest9.jpg",
        title:"9.含钙丰富的食物主要包括哪些？",
        questionList:[
            {
                select: "A. 奶制品、豆制品、鱼虾类",
                checked:false
            },
            {
                select: "B. 牛羊肉类，谷薯类",
                checked:false
            }
        ],
        result:"no",
        answer:0,
        checked:false
    },
    {
        num:10,
        img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest10.jpg",
        title:"10. 以下哪种食物补铁效果好呢？",
        questionList:[
            {
                select: "A.猪肝",
                checked:false
            },
            {
                select: "B. 鸡胸肉",
                checked:false
            },
            {
                select: "C. 红枣",
                checked:false
            }
        ],
        result:"no",
        answer:0,
        checked:false
    }
];
var momNutritionTestInit = function(){
    refreshTestList();
    Refresh();
    recordLogs("HDSY");
}

var selectAnswer = function(index,parentNum){
    var parentNum = parseInt(parentNum);
    testList[parentNum].result = parseInt(index);
    for(var i=0;i< testList[parentNum].questionList.length;i++){
        testList[parentNum].questionList[i].checked=false;
    }
    testList[parentNum].questionList[parseInt(index)].checked=true;
    refreshTestList();
}

var refreshTestList = function(){
    var testListValue = "";
    $.each(testList,function(index1,value1){
        testListValue = testListValue + '<br/><img class="my-img-block" width="100%" height="auto" src="'+ value1.img+'"><dl>' +
            '<dt>'+ value1.title + '</dt> ';
        var testListSecondValue = "";
        $.each(value1.questionList,function(index2,value2){
            var paramNu = "'" + index2 + "'" + ","  + "'" + index1 + "'";
            if(testList[index1].questionList[index2].checked){
                testListSecondValue = testListSecondValue + '<dd class="cur" onclick="selectAnswer('+paramNu+')">'+ value2.select +' </dd>';
            }else{
                testListSecondValue = testListSecondValue + '<dd onclick="selectAnswer('+paramNu+')">'+ value2.select +' </dd>';
            }
        })
        testListValue = testListValue + testListSecondValue + '</dl>';
    })
    $('#testList').html(testListValue);
    var leftPartValue = '<div class="result"><a onclick="lookResult()"></a></div>' +
        '<div class="info bao-code"> <img class="my-img-block" data-tap-disabled="true" width="100%" ' +
        'height="auto" src="http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fresult_code.png"> </div>' +
        '<div class="info prize"><dl><dt>奖品设置：</dt><dd><span class="rank">一等奖：</span>' +
        'iPhone6s Plus 64G <span class="num">1名</span> </dd> <dd><span class="rank">二等奖：</span> ' +
        '小米空气净化器一台 <span class="num">1名</span> </dd> <dd> <span class="rank">三等奖：</span>' +
        '《好胃口宝宝这样养》全套 <span class="num">5名</span> </dd> <dd> <span class="rank">四等奖：</span> ' +
        '壹家壹站墨鱼仔兑换卷 <span class="num">100名</span> </dd> <dd> <span class="rank">五等奖：</span> ' +
        '宝宝精选绘本 <span class="num">200名</span> </dd> <dd> <span class="rank">集赞奖：</span> ' +
        '凡参与测试，在朋友圈集赞99个，即可参与宝大夫水立方辅食大赛。</dd> </dl> </div> <br><br><br>'
    $('#leftPart').html(leftPartValue)
}

var lookResult = function(result,id){
    var score = 0;
    var result = "";
    for(var i=0; i<testList.length; i++){
        if(testList[i].result==testList[i].answer){
            score++;
        }
        result += ","+ testList[i].result;
    }

    var param = '{"result":"' + result + '"'+ ',"score":'+ score + '}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.post('ap/marketing/saveMarketingActivities',param,
        function(data) {
            if(data.id!=""){
                recordLogs("WYKJG");
                window.location.href = "ap/market#/momNutritionResult/"+score+","+data.id;
            }else{
                alert("提交失败！");
            }
        }, 'json');
}

var recordLogs = function(val){
    $.ajax({
        url:"ap/util/recordLogs",// 跳转到 action
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

var Refresh = function(){
    var share = "http://s22.baodf.com/xiaoerke-wxapp/ap/wechatInfo/fieldwork/wechat/author?url=http://s22.baodf.com/xiaoerke-wxapp/ap/wechatInfo/getUserWechatMenId?url=11&state='FXSY_PYQ'";
    var share2 = "http://s22.baodf.com/xiaoerke-wxapp/ap/wechatInfo/fieldwork/wechat/author?url=http://s22.baodf.com/xiaoerke-wxapp/ap/wechatInfo/getUserWechatMenId?url=11&state='FXSY_PYXX'";
    var share3 = "http://s11.baodf.com/xiaoerke-marketing-webapp/ap/firstPage/momNutritionTest";
    var timestamp;//时间戳
    var nonceStr;//随机字符串
    var signature;//得到的签名
    var appid;//得到的签名
    $.ajax({
        url:"ap/wechatInfo/getConfig",// 跳转到 action
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
                        'onMenuShareQQ',
                        'onMenuShareWeibo',
                        'onMenuShareQZone'
                    ] // 功能列表
                });
                wx.ready(function () {
                    // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                    wx.onMenuShareTimeline({
                        title: '测测你给宝宝的喂养科学吗？参与可得iPhone6s等', // 分享标题
                        link: share, // 分享链接
                        imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fmarketing_share.png', // 分享图标
                        success: function (res) {
                            //记录用户分享文章
                            recordLogs("FXSY_FXPYQ");

                        },
                        fail: function (res) {
                        }
                    });

                    wx.onMenuShareAppMessage({
                        title: '测测你给宝宝的喂养科学吗？参与可得iPhone6s等', // 分享标题
                        desc: '99%的妈妈都不合格！99万的妈妈测试了都说好！', // 分享描述
                        link:share2, // 分享链接
                        imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fmarketing_share.png', // 分享图标
                        success: function (res) {
                            recordLogs("FXSY_FXPY");

                        },
                        fail: function (res) {
                        }
                    });

                    wx.onMenuShareQQ({
                        title: '测测你给宝宝的喂养科学吗？参与可得iPhone6s等', // 分享标题
                        desc: '99%的妈妈都不合格！99万的妈妈测试了都说好！', // 分享描述
                        link: share3, // 分享链接
                        imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fmarketing_share.png', // 分享图标
                        success: function () {
                            // 用户确认分享后执行的回调函数
                            recordLogs("FXSY_FXQQ");
                        },
                        cancel: function () {
                            // 用户取消分享后执行的回调函数
                        }
                    });
                    wx.onMenuShareWeibo({
                        title: '测测你给宝宝的喂养科学吗？参与可得iPhone6s等', // 分享标题
                        desc: '99%的妈妈都不合格！99万的妈妈测试了都说好！', // 分享描述
                        link: share3, // 分享链接
                        imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fmarketing_share.png', // 分享图标
                        success: function () {
                            // 用户确认分享后执行的回调函数
                            recordLogs("FXSY_FXWeibo");
                        },
                        cancel: function () {
                            // 用户取消分享后执行的回调函数
                        }
                    });
                    wx.onMenuShareQZone({
                        title: '测测你给宝宝的喂养科学吗？参与可得iPhone6s等', // 分享标题
                        desc: '99%的妈妈都不合格！99万的妈妈测试了都说好！', // 分享描述
                        link: share3, // 分享链接
                        imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fmarketing_share.png', // 分享图标
                        success: function () {
                            // 用户确认分享后执行的回调函数
                            recordLogs("FXSY_FXQZone");
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