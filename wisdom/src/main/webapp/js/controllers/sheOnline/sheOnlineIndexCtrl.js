angular.module('controllers', ['ionic']).controller('sheOnlineIndexCtrl', [
        '$scope','$state','$stateParams','$ionicScrollDelegate','SendWechatMessageToUser','ListHospitalDepartmentDoctor',
    'GetCategoryList','GetArticleList','SendWechatMessageToUserOnline',
        function ($scope,$state,$stateParams,$ionicScrollDelegate,SendWechatMessageToUser,ListHospitalDepartmentDoctor,GetCategoryList,
                  GetArticleList,SendWechatMessageToUserOnline) {

            var firstList=[];
            var num=10;

            /**
             * 在线咨询
             */
            $scope.zixun = function () {
                SendWechatMessageToUserOnline.save({},function(data){
                });
                WeixinJSBridge.call('closeWindow');
            }

            /**
             * 预约挂号
             */
            $scope.yuyue = function() {
                if ($scope.doctorDataVo.length == 0) {
                    alert("郑玉巧医生暂时没有可约时间，请预约其它医生！");
                    $state.go('appointmentFirst');
                } else {
                    $scope.num = $scope.doctorDataVo[0].visitInfo[0].available_time;
                    $state.go('doctorAppointment', {
                        doctorId: "cb7b260a6a3d42cd9f2c12558e6621f2", available_time: $scope.num,
                        hospitalName: "第二炮兵总医院", location: "门诊楼3层3D儿科诊区7诊室", position: "西城区新街口外大街16号",
                        mark: "dateAvailable", location_id: "48a06bfca48e4467840d5fb04395e9f1",attentionDoctorId:""
                    });
                }
            }

            /**
             * 加载更多
             */
            $scope.more = function(){
                $scope.pageLoading=true;
                GetArticleList.save({"pageNo":1,"pageSize":20,"id":firstList[6].firstMenuId},function(data){
                    $scope.pageLoading=false;
                    $scope.questionList=data.articleList;
                    $scope.lockMore="false";
                    $scope.lockMore2="true";
                });
            }

            /**
             *上拉加载更多
             */
            $scope.loadMoreWithDrawlsList = function(){
                GetArticleList.save({"pageNo":1,"pageSize":20+num,"id":firstList[6].firstMenuId},function(data){
                    if(data.articleList.length==$scope.questionList.length){
                        $scope.lockMore2="false";
                        $ionicScrollDelegate.scrollBottom();
                    }else{
                        $scope.questionList=data.articleList;
                        num=num+10;
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    }
                });
            }

            $scope.doRefresh = function(){
                var webname="http://www.baodf.com";
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
                                    title: '重磅消息：郑玉巧独家合作宝大夫平台，为宝爸、宝妈们提供免费在线咨询及面诊预约！', // 分享标题
                                    link: window.location.href.replace("true","false"), // 分享链接
                                    imgUrl: webname+'http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/sheOnline%2Fzhengyuqiao.png', // 分享图标
                                    success: function (res) {

                                        //记录用户分享文章
                                        ArticleShareRecord.save({"contentId":"郑玉巧在线"},function(data){

                                        })
                                    },
                                    fail: function (res) {
                                    }
                                });

                                wx.onMenuShareAppMessage({
                                    title: '重磅消息：郑玉巧独家合作宝大夫平台，为宝爸、宝妈们提供免费在线咨询及面诊预约！', // 分享标题
                                    desc: '每周三，名医郑玉巧与您相约宝大夫微信公众号，为您提供免费在线咨询，同时您还可预约面诊时间...', // 分享描述
                                    link: window.location.href.replace("true","false"), // 分享链接
                                    imgUrl: webname+'http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/sheOnline%2Fzhengyuqiao.png', // 分享图标
                                    success: function (res) {
                                        //记录用户分享文章
                                        ArticleShareRecord.save({"contentId":"郑玉巧在线"},function(data){

                                        })
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

            $scope.$on('$ionicView.enter', function() {
                $scope.lockMore="true";
                $scope.lockMore2="false";
                //获取郑老师号源日期
                $scope.pageLoading=true;
                ListHospitalDepartmentDoctor.save({
                    "pageNo": "1", "pageSize": "10", "orderBy": "0",
                    "hospitalId": "caf1677c20b048c98fe0e0de97f09dac", "departmentLevel1Name": "儿科"
                }, function (data) {
                    $scope.pageLoading=false;
                    $scope.doctorDataVo=data.doctorDataVo;
                });

                //获取病例列表
                $scope.pageLoading=true;
                GetCategoryList.save({"module":"article"},function(data){
                    firstList = data.categoryList;   //获取一级菜单
                    GetArticleList.save({"pageNo":1,"pageSize":10,"id":firstList[6].firstMenuId},function(data){
                        $scope.pageLoading=false;
                        $scope.questionList=data.articleList;
                    });
                });
            });
        }]);