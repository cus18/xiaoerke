﻿angular.module('controllers', ['ionic']).controller('umbrellaMemberListCtrl', [
        '$scope','$state','$stateParams','getFamilyList','ifExistOrder',
        function ($scope,$state,$stateParams,getFamilyList,ifExistOrder) {
            $scope.title="宝护伞-宝大夫儿童家庭重疾互助计划";
            //$scope.id = $stateParams.id;
            $scope.shareLock=false;
            $scope.link=

           /* 分享提示*/
            $scope.goShare=function(){
                $scope.shareLock=true;
            };
            /* 取消提示*/
            $scope.cancelShare=function(){
                $scope.shareLock=false;
            };
            /* 分享提示*/
            $scope.goShare=function(){
                $scope.shareLock=true;
            };
            /* 点击我的保障*/
            $scope.myGuarantee=function(){
                $state.go("umbrellaJoin",{});
            };
            getFamilyList.save({"id":$stateParams.id},function(data){
                console.log(data);
                $scope.familyList =data.familyList;
                for(var i=0;i<data.familyList.length;i++){
                    $scope.familyList[i].birthday = moment(data.familyList[i].birthday).utc().zone(-9).format("YYYY-MM-DD");
                    // $scope.familyList[i].birthday = moment(data.familyList[i].birthday).format("YYYY-MM-DD HH:ss");
                      if($scope.familyList[i].sex==0){
                          $scope.familyList[i].sex = "女宝"
                      }else if($scope.familyList[i].sex==1){
                          $scope.familyList[i].sex = "男宝"
                      }else if($scope.familyList[i].sex==2){
                            $scope.familyList[i].sex = "宝爸"
                        }else{
                            $scope.familyList[i].sex = "宝妈"
                        }
                }
            })

            $scope.addMember=function(){
                // $state.go("umbrellaMemberAdd",{id:$stateParams.id});
                recordLogs("BHS_CYLB_TJCY");
                window.location.href ="../wisdom/umbrella?value="+new Date().getTime()+"#/umbrellaMemberAdd/"+$stateParams.id+"/"+$stateParams.status;
            }

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
            
            $scope.immediateActive=function(){
                $state.go("umbrellaJoin",{id:new Date().getTime()});
            }

            $scope.goActive=function(){
                $state.go("umbrellaFillInfo",{id:$scope.umbrellaId});
            };
            $scope.goShare=function(){
                $scope.shareLock=true;
            };

            $scope.$on('$ionicView.enter', function(){
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
                ifExistOrder.save(function (data) {
                    // $scope.info.phoneNum=data.phone;
                    if (data.result == "1") {
                        window.location.href = "../wisdom/firstPage/umbrella?id=" + $stateParams.id;
                    }
                    if(data.result==2 || data.umbrella.activation_time==null) {
                        window.location.href = "../wisdom/firstPage/umbrella?id=" + $stateParams.id;
                    }else{
                        if(data.umbrella.id!=$stateParams.id) {
                            $scope.umbrellaId = data.umbrella.id;
                            window.location.href = "../wisdom/umbrella?value=" + new Date().getTime() + "#/umbrellaMemberList/" + $scope.umbrellaId + "/a";
                        }
                    }
                });
                
                // if($stateParams.status=="a"){
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
                                        link: "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/"+$stateParams.status, // 分享链接
                                        imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                        success: function (res) {
                                            recordLogs("BHS_WDBZ_FXPY_"+$stateParams.id);
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
                                        title: '5元变成40万,看完我就激动了!', // 分享标题
                                        desc: "我已成为宝护伞互助公益爱心大使，领到了40万的健康保障，你也快来加入吧！", // 分享描述
                                        link:"http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/"+$stateParams.status, // 分享链接
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
                //                         link: "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrella"+$stateParams.status+"_"+ $stateParams.id, // 分享链接
                //                         imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                //                         success: function (res) {
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
                //                         link:"http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrella"+$stateParams.status+"_"+ $stateParams.id, // 分享链接
                //                         imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                //                         success: function (res) {
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
            });

    }]);