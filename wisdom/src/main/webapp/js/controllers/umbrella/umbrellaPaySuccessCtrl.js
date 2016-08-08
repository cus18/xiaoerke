angular.module('controllers', ['ionic']).controller('umbrellaPaySuccessCtrl', [

    '$scope','$state','$stateParams','getUserQRCode',

    function ($scope,$state,$stateParams,getUserQRCode) {

        $scope.title="宝大夫儿童家庭重疾互助计划";





        $scope.QRCodeURI="";



        $scope.$on('$ionicView.enter', function() {



            $.ajax({

                url:"umbrella/getOpenid",// 跳转到 action

                async:true,

                type:'post',

                cache:false,

                dataType:'json',

                success:function(data) {

                    if(data.openid=="none"){

                        window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";





                    }

                },

                error : function() {

                }

            });



            getUserQRCode.save({"id":$stateParams.id}, function (data){

                $scope.QRCodeURI=data.qrcode;

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

                                title: '我为孩子健康负责，免费领取了40万的大病治疗费，还有20万送给你！', // 分享标题

                                link: "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/a",

                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标

                                success: function (res) {

                                    recordLogs("BHS_WDBZ_FXPYQ_"+$stateParams.id);

                                    //记录用户分享文章

                                    $.ajax({

                                        type: 'POST',

                                        url: "umbrella/updateBabyUmbrellaInfoIfShare",

                                        data:"{'id':'"+$stateParams.id+"'}",

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

                                link:"http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/a",

                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标

                                success: function (res) {

                                    recordLogs("BHS_WDBZ_FXPY_"+$stateParams.id);

                                    $.ajax({

                                        type: 'POST',

                                        url: "umbrella/updateBabyUmbrellaInfoIfShare",

                                        data:"{'id':'"+$stateParams.id+"'}",

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



        })





    }]);