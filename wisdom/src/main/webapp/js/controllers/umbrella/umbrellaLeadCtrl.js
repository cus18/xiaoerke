angular.module('controllers', ['ionic']).controller('umbrellaLeadCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="宝大夫儿童家庭重疾互助计划";

            /*立即加入*/
            $scope.goJoin=function(){
                window.location.href="http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrella"+$stateParams.status+"_"+ $stateParams.id;
            };
            $scope.$on('$ionicView.enter', function(){
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
                                    link: "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/"+$stateParams.status,
                                    imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                    success: function (res) {
                                        recordLogs("BHS_WDBZ_FXPYQ");
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
                                    link:"http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/"+$stateParams.status,
                                    imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                    success: function (res) {
                                        recordLogs("BHS_WDBZ_FXPY");
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
            });

            
    }]);