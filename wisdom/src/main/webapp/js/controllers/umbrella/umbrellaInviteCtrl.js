angular.module('controllers', ['ionic']).controller('umbrellaInviteCtrl', [
        '$scope','$state','$stateParams','ifExistOrder',
        function ($scope,$state,$stateParams,ifExistOrder) {
            $scope.title="邀请提额";
            $scope.shareLock=false;
            $scope.invite=function(){
                $scope.shareLock=true;
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
                        window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa_"+$stateParams.id;
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
                                    title: '5元变成40万,看完我就激动了!', // 分享标题
                                    link:  "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$scope.umbrellaId+"/a",
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
                                    title: '5元变成40万,看完我就激动了!', // 分享标题
                                    desc: "我已成为宝护伞互助公益爱心大使，领到了40万的健康保障，你也快来加入吧！", // 分享描述
                                    link:  "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$scope.umbrellaId+"/a", // 分享链接
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
            }
    }]);