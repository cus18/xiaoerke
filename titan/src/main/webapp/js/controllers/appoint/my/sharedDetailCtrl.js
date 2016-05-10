angular.module('controllers', ['ionic']).controller('SharedDetailCtrl', [
        '$scope','$stateParams','MyShare','OrderShareOperation','$state','resolveUserLoginStatus','$location',
        function ($scope,$stateParams,MyShare,OrderShareOperation,$state,resolveUserLoginStatus,$location) {

            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.status= $stateParams.status;
            $scope.returnpay = $stateParams.returnpay
            $scope.IKnow = function() {
                $(".iknow-shadow").css("display","none");
                $(".iknowContent").css("display","none");
                $(".bar-header").css("display","block");
            }

            $scope.doRefresh = function(){
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
                                    'onMenuShareTimeline'
                                ] // 功能列表
                            });
                            wx.ready(function () {
                                // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                                wx.onMenuShareTimeline({
                                    title: '竟然在宝大夫约到了'+$scope.doctorData.hospitalName+
                                    $scope.doctorData.doctorName+'医生。 真是太好了！还在等神马，速速预约吧！',
                                    link: window.location.href.replace("true","false"),
                                    imgUrl: 'http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/4f695bec386944898c423a2f78b579ac',
                                    success: function (res) {
                                        $scope.pageLoading = true;
                                        OrderShareOperation.save({"patient_register_service_id":$stateParams.patient_register_service_id,"type":$stateParams.type,
                                            "action":{"status":"5"}},function(data){
                                            $scope.pageLoading = false;
                                        })
                                        $state.go("shareSuccess");
                                    },
                                    fail: function (res) {
                                        alert(JSON.stringify(res));
                                    }
                                });
                            })
                        }
                        else{
                        }
                    },
                    error : function() {
                    }
                });

            }

            MyShare.save({patientRegisterServiceId:$stateParams.patient_register_service_id,type:$stateParams.type},function(data){
                $scope.doctorData  = data
                $scope.imgUrl = !data.qrcode?("http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/my%2FshareDeatil_code.png"):
                    ("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+data.qrcode)
            })

            //$scope.$on('$ionicView.enter', function(){
            //    resolveUserLoginStatus.events($location.path(),"","","","notGo");
            //});
    }]);
