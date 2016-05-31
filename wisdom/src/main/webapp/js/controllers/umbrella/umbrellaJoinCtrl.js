angular.module('controllers', ['ionic']).controller('umbrellaJoinCtrl', [
        '$scope','$state','$stateParams','JoinUs','updateActivationTime',
        function ($scope,$state,$stateParams,JoinUs,updateActivationTime) {
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
                JoinUs.save(function(data){
                    if(data.umbrella.activation_time==null){
                        $scope.firstJoin=true;
                        $scope.umbrellaMoney=20000;
                        $scope.umbrellaId=data.id;
                        $scope.loadShare();
                        // updateActivationTime.save({"id":$scope.umbrellaId},function(data){
                        //     if(data.result!="1"){
                        //       alert("未知错误,请尝试刷新页面");
                        //     }
                        // });
                        console.log("id",$scope.umbrellaId);
                        updateActivationTime.save({"id":$scope.umbrellaId}, function (data){
                            if(data.result!='1'){
                                alert("未知错误,请尝试刷新页面");
                            }
                        });
                    }else if(data.result==2){
                        $scope.updateJoin=true;
                        $scope.umbrellaMoney=data.umbrella.umbrella_money;
                        $scope.num=data.umbrella.id-120000000;
                        $scope.umbrellaId=data.umbrella.id;
                        $scope.loadShare();
                    }else if(data.result==3){
                        $scope.finally=true;
                        $scope.umbrellaMoney=data.umbrella.umbrella_money;
                        $scope.num=data.umbrella.id-120000000;

                        
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
                    }
                    $scope.person=(400000-$scope.umbrellaMoney)/20000;
                });

               


            });

            $scope.loadShare=function() {
                var timestamp;//时间戳
                var nonceStr;//随机字符串
                var signature;//得到的签名
                var appid;//得到的签名
                $.ajax({
                    url: "wechatInfo/getConfig",// 跳转到 action
                    async: true,
                    type: 'get',
                    data: {url: location.href.split('#')[0]},//得到需要分享页面的url
                    cache: false,
                    dataType: 'json',
                    success: function (data) {
                        if (data != null) {
                            timestamp = data.timestamp;//得到时间戳
                            nonceStr = data.nonceStr;//得到随机字符串
                            signature = data.signature;//得到签名
                            appid = data.appid;//appid
                            //微信配置
                            wx.config({
                                debug: false,
                                appId: appid,
                                timestamp: timestamp,
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
                                    title: '我已为宝宝免费领取一份40万的大病保障，你也赶紧加入吧!', // 分享标题
                                    link: "http://s2.xiaork.cn/keeper/wechatInfo/fieldwork/wechat/author?url=http://s2.xiaork.cn/keeper/wechatInfo/getUserWechatMenId?url=umbrella" + $scope.umbrellaId, // 分享链接
                                    imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                    success: function (res) {
                                        //记录用户分享文章
                                        recordLogs("Umbrella_shareMoment");
                                    },
                                    fail: function (res) {
                                    }
                                });

                                wx.onMenuShareAppMessage({
                                    title: '我已为宝宝免费领取一份40万的大病保障，你也赶紧加入吧!', // 分享标题
                                    desc: "现在加入即可免费获取最高40万60种儿童重疾保障，还等什么，妈妈们 let's go！", // 分享描述
                                    link: "http://s2.xiaork.cn/keeper/wechatInfo/fieldwork/wechat/author?url=http://s2.xiaork.cn/keeper/wechatInfo/getUserWechatMenId?url=umbrella" + $scope.umbrellaId, // 分享链接
                                    imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                    success: function (res) {
                                        recordLogs("Umbrella_shareFirend");
                                    },
                                    fail: function (res) {
                                    }
                                });

                            })
                        } else {
                        }
                    },
                    error: function () {
                    }
                });
            }
            
    }]);