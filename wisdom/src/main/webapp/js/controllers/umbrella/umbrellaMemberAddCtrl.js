﻿﻿angular.module('controllers', ['ionic']).controller('umbrellaMemberAddCtrl', [
        '$scope','$state','$stateParams','addFamily','cheackFamilyMembers',
        function ($scope,$state,$stateParams,addFamily,cheackFamilyMembers) {
            $scope.title="宝护伞-宝大夫儿童家庭重疾互助计划";
            $scope.sexItem = "boy";
            $scope.parentLock = false;//判断之前登录的时候选择的是宝爸还是宝妈
            $scope.info = {}
            $scope.info.babyName = '';

            /*选择性别*/
            $scope.selectSex = function(sex){
                $scope.sexItem=sex;
            };
            /*生日插件*/
            $scope.selectBirthday=function(){
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
                    startYear:1960, //开始年份
                    //endYear:currYear //结束年份
                    /*minDate: new Date(2002,date.substring(5,7)-1,date.substring(8,10)),*/
                    maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10))
                    //endYear:2099 //结束年份
                };
                $("#birthday").mobiscroll(opt);
            };

            $scope.save=function(){
                var birthday=$("#birthday").val();
                var sex=$scope.sexItem;
                var name=$scope.info.babyName;
                if(birthday==''||sex==''||name == ''){
                    alert("资料信息不完整,请完善");
                    return;
                }
                addFamily.save({"sex":sex,"name":name,"birthDay":birthday,"id":$stateParams.id},function(data){
                    if(data.reusltStatus == 1){
                        recordLogs("BHS_TJCY_BC");
                        $state.go("umbrellaMemberList",{id:$stateParams.id,status:$stateParams.status});
                    }else{
                        alert("宝宝信息保存失败");
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
            $scope.initSelect = function () {
                $scope.selectBirthday();
            }

            $scope.$on('$ionicView.enter', function(){
                $("#birthday").val("");
                $scope.sexItem = '';
                $scope.info.babyName = '';
                // $scope.selectBirthday();
                cheackFamilyMembers.save({id:$stateParams.id},function(data){
                    console.log(data)
                    $scope.selectInfo = data;
                });

                // if($stateParams.status=="a"){
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
                                        title: '不敢相信，一根雪糕钱就换来了40万重疾保障!', // 分享标题
                                        link:  "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/"+$stateParams.status, // 分享链接
                                        imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                        success: function (res) {
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
                                        title: '不敢相信，一根雪糕钱就换来了40万重疾保障!', // 分享标题
                                        desc: "宝护伞是由宝大夫联合中国儿童少年基金会发起的非盈利性公益项目！", // 分享描述
                                        link:"http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/"+$stateParams.status,  // 分享链接
                                        imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                        success: function (res) {
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
                //                         link: "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrella"+$stateParams.status+"_"+$stateParams.id, // 分享链接
                //                         imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                //                         success: function (res) {
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
                //                         link:"http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrella"+$stateParams.status+"_"+ $stateParams.id, // 分享链接
                //                         imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                //                         success: function (res) {
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
            });

            
    }]);