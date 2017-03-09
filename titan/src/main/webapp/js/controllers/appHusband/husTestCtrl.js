angular.module('controllers', ['ionic','ngDialog']).controller('husTestCtrl', [
    '$scope','$state','$stateParams','$ionicScrollDelegate','CheackAttention',
    function ($scope,$state,$stateParams,$ionicScrollDelegate,CheackAttention) {


        $scope.maskStatus=false;//遮罩层
        $scope.data=[{
           question:'1,情人节你老公会',
           answer:['A、忘记了','B、从来不过','C、送花送礼物','D、为你准备了惊喜'],
           mark:0
       },{
           question:'2,当孩子大哭大闹时，他会',
           answer:['A、当做没听见','B、直接丢给你','C、吓唬孩子不许哭','D、主动哄孩子'],
           mark:0
        },{
           question:'3,当你和婆婆闹矛盾的时候',
           answer:['A、不闻不问','B、和婆婆统一战线','C、和自己统一战线','D、两边说和'],
           mark:0
        },{
           question:'4,当你生病了，你老公怎么会照顾你？',
           answer:['A、让你多喝热水','B、告诉你记得吃药','C、端水喂药伺候你','D、帮你出去买药'],
           mark:0
        },{
           question:'5,当你生孩子的时候，你老公在',
           answer:['A、不知道在哪','B、产房门口玩手机','C、手术室门口团团转','D、主动要求陪产'],
           mark:0
        },{
           question:'6,当他发工资的时候',
           answer:['A、全都自己留着','B、偷偷留点再给你','C、如数上交','D、问你工资到账没'],
           mark:0
        },{
           question:'7,你们一家三口出门，你老公会',
           answer:['A、从来没一起出过门','B、打电话看手机','C、只顾自己扮酷','D、大包小包+抱孩子'],
           mark:0
        }]

        $scope.choose=function(i,item){
            item.mark=(i+1)*5;
        }
        setTimeout(function(){
            $('.answer').find('span').click(function(){
                $(this).addClass('cur').siblings().removeClass('cur');
            })
        },1000);
        $scope.resultNum;
        $scope.sum=0;
        $scope.Submit=function(){

            for(var i=0;i<$scope.data.length;i++){
                if($scope.data[i].mark==0){
                    $scope.sum=0;
                    alert('亲，你还没有完题呢，赶紧把题答完，看结果欧～')
                    return;
                }else{
                    $scope.sum+=$scope.data[i].mark;
                }
            }
            if($scope.sum<=70){
                $scope.resultNum=1;
            }else if($scope.sum>70 && $scope.sum<=100){
                $scope.resultNum=2;
            }else if($scope.sum>100 && $scope.sum<=120){
                $scope.resultNum=3;
            }else if($scope.sum>120){
                $scope.resultNum=4;
            }
            loadShare($scope);
            $scope.maskStatus=true;
            $ionicScrollDelegate.scrollTop();
            $('.view-container').animate({scrollTop:0}, 0);
            $scope.sum=0;
        }
        $scope.shutMask=function(){
            console.log(123)
            $scope.maskStatus=false;
        }
        //分享到朋友圈或者微信
       var loadShare = function($scope){
                var share='http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=54'
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
                                    title: '别以为3.15没曝光就是正品了，好多“假老公”已经被四海八荒的消协特战组带走返厂了，你家那位还好吗？', // 分享标题
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appHusband/test/share.jpg', // 分享图标
                                    success: function (res) {
                                        CheackAttention.get({"type": $scope.resultNum},function (data) {
                                            if("isattention" == data.status){
                                                $state.go("husResult",{"id": $scope.resultNum});
                                            } else {
                                                //提示关注
                                                $state.go("husFollow");
                                            }
                                        })
                                    },
                                    fail: function (res) {
                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title: '晕~我竟嫁了个“假老公”，鬼知道我经历了什么', // 分享标题
                                    desc: '这么多年过去了，我才发现我居然嫁了个“嫁老公”，点击链接，赶快看看你家老公合格吗？',// 分享描述
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appHusband/test/share.jpg', // 分享图标
                                    success: function (res) {
                                        CheackAttention.get({"type": $scope.resultNum},function (data) {
                                            if("isattention" == data.status){
                                                $state.go("husResult",{"id": $scope.resultNum});
                                            } else {
                                                //提示关注
                                                $state.go("husFollow");
                                            }
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
        };
        setTimeout(function(){
            $('.mask').height($('.testBox').height())
        },300)

    }])