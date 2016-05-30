angular.module('controllers', ['ionic']).controller('umbrellaJoinCtrl', [
        '$scope','$state','$stateParams','JoinUs',
        function ($scope,$state,$stateParams,JoinUs) {
            $scope.title="宝护伞";
            $scope.shareLock=false;

            $scope.firstJoin=false;
            $scope.updateJoin=false;
            $scope.finally=false;
            $scope.umbrellaMoney=0;
            $scope.num=0;
            $scope.person=0;
            $scope.umbrellaId=0;

            $scope.goDetail=function(){
                window.location.href = "/wisdom/firstPage/umbrella";
            };
            $scope.goActive=function(){
                $state.go("umbrellaFillInfo",{id:$scope.umbrellaId});
            };
            $scope.goShare=function(){
                $scope.shareLock=true;
            };
            $scope.cancelShare=function(){
                $scope.shareLock=false;
            };
            $scope.$on('$ionicView.enter', function(){
                JoinUs.save(function(data){
                    if(data.result==1){
                        $scope.firstJoin=true;
                        $scope.umbrellaMoney=20000;
                        $scope.umbrellaId=data.id;
                    }else if(data.result==2){
                        $scope.updateJoin=true;
                        $scope.umbrellaMoney=data.umbrella.umbrella_money;
                        $scope.num=data.umbrella.id-120000000;
                        $scope.umbrellaId=data.umbrella.id;
                    }else if(data.result==3){
                        $scope.finally=true;
                        $scope.umbrellaMoney=data.umbrella.umbrella_money;
                        $scope.num=data.umbrella.id-120000000;
                        
                        var targetDate = new Date(data.umbrella.activation_time);
                            targetDate.setDate(new Date().getDate() + 180);
                        var targetDateUTC = targetDate.getTime();
                        var afterDate=	Math.round(new Date().getTime());
                        console.log("targetDateUTC",targetDateUTC);
                        console.log("afterDate",afterDate);
                        $scope.days=Math.ceil((targetDateUTC-afterDate)/1000/60/60/24);
                        $scope.minusDays = $scope.days.toString();
                        $scope.minusDays1 =  $scope.minusDays.substring(0,1);
                        $scope.minusDays2 = $scope.minusDays.substring(1,2);
                        $scope.minusDays3 = $scope.minusDays.substring(2,3);
                        $scope.umbrellaId=data.umbrella.id;
                    }
                    $scope.person=(400000-$scope.umbrellaMoney)/20000;
                });

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
                                    title: '我为您的宝宝领取了最高40万保障金', // 分享标题
                                    link: "http://s2.xiaork.cn/keeper/wechatInfo/fieldwork/wechat/author?url=http://s2.xiaork.cn/keeper/wechatInfo/getUserWechatMenId?url=31&id=0?id="+$scope.umbrellaId, // 分享链接
                                    imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                    success: function (res) {
                                        //记录用户分享文章
                                        recordLogs("Umbrella_shareMoment");
                                    },
                                    fail: function (res) {
                                    }
                                });

                                wx.onMenuShareAppMessage({
                                    title: '我为您的宝宝领取了最高40万保障金', // 分享标题
                                    desc: '前20万用户免费加入即可获取最高40万60种儿童重疾保障', // 分享描述
                                    link:"http://s2.xiaork.cn/keeper/wechatInfo/fieldwork/wechat/author?url=http://s2.xiaork.cn/keeper/wechatInfo/getUserWechatMenId?url=31&id=0?id="+$scope.umbrellaId, // 分享链接
                                    imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                    success: function (res) {
                                        recordLogs("Umbrella_shareFirend");
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