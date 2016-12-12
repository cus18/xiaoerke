angular.module('controllers', ['ionic']).controller('umbrellaInviteCtrl', [
    '$scope','$state','$stateParams','ifExistOrder',
    function ($scope,$state,$stateParams,ifExistOrder) {
        $scope.title="邀请提额";
        $scope.shareLock=false;
        $scope.invite=function(){
            $scope.shareLock=true;
            /* 随机分享文案*/
            var shareTextArray=[
                "有了这个相当于多了个重疾保险，免费加入就能换来40万，一确诊就能给钱，比保险快多了！",
                "墙裂推荐，绝非广告，这个真的是很需要。是对孩子和家庭的负责！我已经加入啦，你还不快来！",
                "我为孩子健康负责，免费领取了40万的大病治疗费，你也来领吧！",
                "我为宝宝健康负责，竟然免费获得了40万的大病治疗费！你需要吗？",
                "领取40万的大病治疗费，万一看病不用愁，限时免费，先到先得啦！",
                "每天都有孩子因没钱治病而死。现在有40万治疗费，送给你！",
                "没什么好送的，40万的大病治疗费，送给你！",
                "最美的妈妈你别走，送你40万，让孩子健康去成长！",
                "如需江湖救急，这有40万的大病治疗费，速速来拿！"
            ];

            var randomNum=parseInt(9*Math.random());//分享文案随机数
            /*   $(".share p").html( shareTextArray[randomNum]);*/
            $scope.shareRandomText=shareTextArray[randomNum];

        };

        $scope.cancelShare=function(){
            $scope.shareLock=false;
        };
        $scope.person=0;
        $scope.umbrellaMoney=0;
        $scope.umbrellaId=1200000;
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
        $scope.$on('$ionicView.enter', function(){
            ifExistOrder.save(function (data) {
                if(data.result=="1"){
                    window.location.href = "http://s201.xiaork.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s201.xiaork.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa_"+$stateParams.id;
                }
                $scope.umbrellaMoney=data.umbrella.umbrella_money/10000;
                $scope.person=data.umbrella.friendJoinNum;
                $scope.umbrellaId=data.umbrella.id;
                recordLogs("BHS_YQTE_"+$scope.umbrellaId);
                $scope.loadShare();
            });
        });

        $scope.loadShare=function() {
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
                                title: '我为孩子健康负责，免费领取了40万的大病治疗费，还有20万送给你！', // 分享标题
                                link:  "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$scope.umbrellaId+"/a",
                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                success: function (res) {
                                    recordLogs("BHS_YQTE_FXPYQ_"+$scope.umbrellaId);
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
                                title: '我为孩子健康负责，免费领取了40万的大病治疗费，还有20万送给你！', // 分享标题
                                desc: "限时免费，手慢无！讲真，这个东西好到让你想给我发红包！！！", // 分享描述
                                link:  "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$scope.umbrellaId+"/a", // 分享链接
                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                success: function (res) {
                                    recordLogs("BHS_YQTE_FXPY_"+$scope.umbrellaId);
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

        }

    }]);