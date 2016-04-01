angular.module('controllers', ['ionic']).controller('antiDogIndexCtrl', [
    '$scope','$state','$stateParams','$location','$http','getInsuranceRegisterServiceByOpenid',
    function ($scope,$state,$stateParams,$location,$http,getInsuranceRegisterServiceByOpenid) {

        $scope.num = "";
        $scope.parRemindLock =false;
        $scope.readLock =true;

        // 点击已阅读
        $scope.read = function(){
            if($scope.readLock){
                $scope.readLock =false;
            }
            else{
                $scope.readLock =true;
            }
        };
        //点击页面右下角 赔
        $scope.goPay = function(){
            var pData = {logContent:encodeURI("FQB_FWXQ_PEI")};
        	$http({method:'post',url:'ap/util/recordLogs',params:pData});
            $state.go('antiDogOrderList');
        };


        //点击立即购买
        $scope.goBuy = function(){
        	var pData = {logContent:encodeURI("FQB_FWXQ_LJGM")};
        	$http({method:'post',url:'ap/util/recordLogs',params:pData});
            window.location.href = "/xiaoerke-insurance-webapp/antiDogPay/patientPay.do?" + "&chargePrice=15000";
        };

        $scope.$on('$ionicView.enter', function(){
        	var pData = {logContent:encodeURI("FQB_FWXQ")};
        	$http({method:'post',url:'ap/util/recordLogs',params:pData});
        	getInsuranceRegisterServiceByOpenid.get(function (data){
             	if(data.insurance=="0"){
                 	$("#payOut").hide();
                }
             });
        })
        
        $scope.doRefresh = function(){
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
                                'onMenuShareAppMessage'
                            ] // 功能列表
                        });
                        wx.ready(function () {
                            // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                            wx.onMenuShareTimeline({
                                title: '邻居的狗打过疫苗吗？小区总有人遛狗不牵着，怎么办？', // 分享标题
                                link: window.location.href.replace("true","false"), // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2Findex_banner.png', // 分享图标
                                success: function (res) {
                                    //记录用户分享文章
                                    UpdateMarketingActivities.save({"id":$stateParams.id,"ifShare":"1"}, function (data) {
                                        
                                    });
                                    setLog("FXJG_FXPYQDog");
                                },
                                fail: function (res) {
                                }
                            });

                            wx.onMenuShareAppMessage({
                                title: '', // 分享标题
                                desc: '邻居的狗打过疫苗吗？小区总有人遛狗不牵着，怎么办？', // 分享描述
                                link:window.location.href.replace("true","false"), // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2Findex_banner.png', // 分享图标
                                success: function (res) {
                                    UpdateMarketingActivities.save({"id":$stateParams.id,"ifShare":"1"}, function (data) {

                                    });
                                    setLog("FXJG_FXPYDog");
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

