angular.module('controllers', ['ionic']).controller('umbrellaJoinCtrl', [
    '$scope','$state','$stateParams','JoinUs','updateActivationTime','ifExistOrder','getNickNameAndRanking',
    function ($scope,$state,$stateParams,JoinUs,updateActivationTime,ifExistOrder,getNickNameAndRanking) {
        $scope.title="宝护伞-宝大夫儿童家庭重疾互助计划";
        $scope.shareLock=false;

        $scope.firstJoin=false;
        $scope.updateJoin=false;
        $scope.addFamily=false;
        $scope.umbrellaMoney=0;
        $scope.num=0;
        $scope.person=0;
        $scope.umbrellaId=0;
        $scope.status="b";
        $scope.pintu=0;

        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('linux') > -1; //g
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        var detailLock = "true";
        var nickName="我真心";

        $scope.shareid=$stateParams.shareid;

        $scope.goDetail=function(){
            recordLogs("BHS_WDBZ_CKXQ");
            if(isAndroid){
                window.location.href = "/wisdom/firstPage/umbrella?status="+$scope.status;
            }else if(isIOS){
                if(detailLock=="true"){
                    detailLock = "false";
                }else{
                    window.location.href = "/wisdom/firstPage/umbrella?status="+$scope.status;
                }
            }
        };
        $scope.goActive=function(){
            recordLogs("BHS_WDBZ_JH");
            // $state.go("umbrellaMemberList",{id:$scope.umbrellaId,status:$scope.status});
            window.location.href = "/wisdom/umbrella#/umbrellaFillInfo/"+$scope.umbrellaId+"/a";
        };
        $scope.goShare=function(){
            $scope.shareLock=true;
            /* 随机分享文案*/
            var shareTextArray=[
                "不怕一万，就怕万一呀～",
                "感觉现在面对重病最大的问题就是高昂治疗费。这个公益项目不错，希望能帮助更多的无助的家庭！",
                "送给别人的一份爱心，更是送给自己的一份保障！这个宝护伞还是挺可以的！",
                "当了妈之后，总是想给孩子最好的，最好的环境、最好的教育，但是健康却总是无法保障的！",
                "墙裂推荐，绝非广告，这个真的是很需要。是对孩子和家庭的负责！我已经加入啦，你还不快来！",
                "有了这个相当于多了个重疾保险，免费加入就能换来40万，一确诊就能给钱，比保险快多了！"
            ];
            var randomNum=parseInt(6*Math.random());//分享文案随机数
            /*   $(".share p").html( shareTextArray[randomNum]);*/
            $scope.shareRandomText=shareTextArray[randomNum];
        };
        $scope.cancelShare=function(){
            $scope.shareLock=false;
        };
        //添加成员
        $scope.addMember=function(){
            // $state.go("umbrellaMemberAdd",{id:$scope.umbrellaId});
            recordLogs("BHS_WDBZ_JTCY");
            window.location.href ="../wisdom/umbrella?value="+new Date().getTime()+"#/umbrellaMemberList/"+$scope.umbrellaId+"/"+$scope.status;
        }
        var compareDate = function (start,end){
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
        $scope.$on('$ionicView.enter', function(){
            $.ajax({
                url:"umbrella/getOpenid",// 跳转到 action
                async:true,
                type:'post',
                cache:false,
                dataType:'json',
                success:function(data) {
                    if(data.openid=="none"){
                        // window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
                        window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa_"+$stateParams.id;
                    }else{
                        $scope.openid=data.openid;
                        console.log("my scope.openid", $scope.openid);
                        getNickNameAndRanking.save({"openid":$scope.openid},function (data) {
                            if(data.nickName!=""){
                                nickName=data.nickName;
                            }
                            console.log("nickName",nickName);

                        });
                        recordLogs("UmbrellaShareJoinPage_"+ $stateParams.id);

                        ifExistOrder.save(function (data) {
                            // $scope.info.phoneNum=data.phone;
                            if (data.result == "1") {
                                //alert("ifExistOrder");
                                window.location.href = "../wisdom/firstPage/umbrella?id=" + $stateParams.id;
                            }else if(data.umbrella.pay_result=="fail"){
                                window.location.href = "http://s251.baodf.com/keeper/wxPay/patientPay.do?serviceType=umbrellaPay&shareId="+$stateParams.id;
                            }
                            if(data.result==2 || data.umbrella.activation_time==null) {
                                $scope.umbrellaMoney = 200000;
                                if (data.result == 3 || data.result == 2) {
                                    $scope.umbrellaId = data.umbrella.id;
                                } else {
                                    $scope.umbrellaId = data.id;
                                }
                                if (data.umbrella.pay_result != "null" && typeof(data.umbrella.pay_result) != "undefined") {
                                    $scope.status = "a";
                                }
                                $scope.loadShare();
                                if(data.result == 2){
                                    $scope.updateJoin=true;
                                }else{
                                    $scope.firstJoin = true;
                                }
                                $scope.num=data.rank+1;
                                updateActivationTime.save({"id": $scope.umbrellaId}, function (data) {
                                    if (data.result != '1') {
                                        alert("未知错误,请尝试刷新页面");
                                    }
                                });
                            }else{
                                $scope.loadShare();
                                $scope.finally=true;
                                $scope.addFamily=true;
                                $scope.umbrellaMoney=data.umbrella.umbrella_money;
                                $scope.num=data.rank+1;
                                if(data.umbrella.pay_result!="null"&&typeof(data.umbrella.pay_result)!="undefined"){
                                    $scope.status="a";
                                }
                                var targetDate = new Date(data.umbrella.activation_time);
                                targetDate.setDate(new Date().getDate() + 180);
                                var targetDateUTC = targetDate.getTime();

                                var selsDate = moment(data.umbrella.activation_time).format("YYYY-MM-DD");
                                var sedd =moment(selsDate).add(180,'days').format("YYYY-MM-DD");
                                var last = moment(sedd).subtract(1,'days').format("YYYY-MM-DD");

                                var day = compareDate(moment().format("YYYY-MM-DD"),last);
                                console.log("targetDateUTC",day);
                                $scope.minusDays = day.toString();
                                $scope.minusDays1 =  $scope.minusDays.substring(0,1);
                                $scope.minusDays2 = $scope.minusDays.substring(1,2);
                                $scope.minusDays3 = $scope.minusDays.substring(2,3);
                                $scope.umbrellaId=data.umbrella.id;
                                $scope.loadShare();
                                // $scope.person=data.umbrella.friendJoinNum<10?10-data.umbrella.friendJoinNum:data.umbrella.friendJoinNum;
                            }
                            $scope.person=data.umbrella.friendJoinNum;
                            $scope.pintu=data.umbrella.friendJoinNum>=10?0:10-data.umbrella.friendJoinNum;
                            recordLogs("BHS_WDBZ");
                            //     JoinUs.save({"shareId":$scope.shareid},function(data){
                            //         if(data.umbrella.activation_time==null){
                            //             $scope.firstJoin=true;
                            //             $scope.umbrellaMoney=200000;
                            //             if(data.result==3){
                            //                 $scope.umbrellaId=data.umbrella.id;
                            //             }else {
                            //                 $scope.umbrellaId = data.id;
                            //             }
                            //             if(data.umbrella.pay_result!="null"&&typeof(data.umbrella.pay_result)!="undefined"){
                            //                 $scope.status="a";
                            //             }
                            //             $scope.loadShare();
                            //
                            //             updateActivationTime.save({"id":$scope.umbrellaId}, function (data){
                            //                 if(data.result!='1'){
                            //                     alert("未知错误,请尝试刷新页面");
                            //                 }
                            //             });
                            //         }else if(data.result==2){
                            //             $scope.updateJoin=true;
                            //             $scope.umbrellaMoney=data.umbrella.umbrella_money;
                            //             $scope.num=data.umbrella.id-120000000;
                            //             $scope.umbrellaId=data.umbrella.id;
                            //             if(data.umbrella.pay_result!="null"&&typeof(data.umbrella.pay_result)!="undefined"){
                            //                 $scope.status="a";
                            //             }
                            //             $scope.loadShare();
                            //         }else if(data.result==3){
                            //             $scope.finally=true;
                            //             $scope.addFamily=true;
                            //             $scope.umbrellaMoney=data.umbrella.umbrella_money;
                            //             $scope.num=data.umbrella.id-120000000;
                            //             if(data.umbrella.pay_result!="null"&&typeof(data.umbrella.pay_result)!="undefined"){
                            //                 $scope.status="a";
                            //             }
                            //             var targetDate = new Date(data.umbrella.activation_time);
                            //                 targetDate.setDate(new Date().getDate() + 180);
                            //             var targetDateUTC = targetDate.getTime();
                            //
                            //             var selsDate = moment(data.umbrella.activation_time).format("YYYY-MM-DD");
                            //             var sedd =moment(selsDate).add(180,'days').format("YYYY-MM-DD");
                            //             var last = moment(sedd).subtract(1,'days').format("YYYY-MM-DD");
                            //
                            //             var day = compareDate(moment().format("YYYY-MM-DD"),last);
                            //             console.log("targetDateUTC",day);
                            //             $scope.minusDays = day.toString();
                            //             $scope.minusDays1 =  $scope.minusDays.substring(0,1);
                            //             $scope.minusDays2 = $scope.minusDays.substring(1,2);
                            //             $scope.minusDays3 = $scope.minusDays.substring(2,3);
                            //             $scope.umbrellaId=data.umbrella.id;
                            //             $scope.loadShare();
                            //         }
                            //         // $scope.person=data.umbrella.friendJoinNum<10?10-data.umbrella.friendJoinNum:data.umbrella.friendJoinNum;
                            //         $scope.person=data.umbrella.friendJoinNum;
                            //         $scope.pintu=data.umbrella.friendJoinNum>=10?0:10-data.umbrella.friendJoinNum;
                            //     });
                        });
                    }
                },
                error : function() {
                }
            });
        });

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

        $scope.loadShare=function() {
            // if($scope.status=="a"){
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
                                title: '限时免费加入宝护伞爱心公益，小孩、大人得了重病都给钱！最高40万！', // 分享标题
                                link:  "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$scope.umbrellaId+"/"+$scope.status,
                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                success: function (res) {
                                    recordLogs("BHS_WDBZ_FXPYQ_"+$scope.umbrellaId);
                                    //记录用户分享文章
                                    $.ajax({
                                        type: 'POST',
                                        url: "umbrella/updateBabyUmbrellaInfoIfShare",
                                        data:"{'id':'"+$scope.umbrellaId+"'}",
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
                                title: '限时免费加入宝护伞爱心公益，小孩、大人得了重病都给钱！最高40万！', // 分享标题
                                desc: "由宝大夫和中国儿童少年基金会联合发起，绝对值得信赖！", // 分享描述
                                link:  "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$scope.umbrellaId+"/"+$scope.status, // 分享链接
                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                success: function (res) {
                                    recordLogs("BHS_WDBZ_FXPY_"+$scope.umbrellaId);
                                    $.ajax({
                                        type: 'POST',
                                        url: "umbrella/updateBabyUmbrellaInfoIfShare",
                                        data:"{'id':'"+$scope.umbrellaId+"'}",
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
            //                         link: "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrella"+$scope.status+"_"+ $scope.umbrellaId, // 分享链接
            //                         imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
            //                         success: function (res) {
            //
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
            //                         link:"http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrella"+$scope.status+"_"+ $scope.umbrellaId, // 分享链接
            //                         imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
            //                         success: function (res) {
            //                             recordLogs("BHS_WDBZ_FXPY");
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
        }

    }]);