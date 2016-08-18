angular.module('controllers', ['ionic']).controller('umbrellaLeadCtrl', [
    '$scope','$state','$stateParams','getNickNameAndRanking',
    function ($scope,$state,$stateParams,getNickNameAndRanking) {
        $scope.title="宝大夫儿童家庭重疾互助计划";
        $scope.musicLock=false;
        $scope.touchLock=true;
        /*$scope.myActiveSlide =0;*/
        var nickName="我真心";

        /*立即加入*/
        $scope.goJoin=function(){
            recordLogs("BHS_H5_LJJR_5");
            window.location.href="http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/" +
                "author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=" +
                "umbrellaa_"+ $stateParams.id;
        };
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

        //播放视频

        $scope.onTouch=function() {
            if($scope.touchLock&&music.paused){
                music.play();
                $scope.musicLock=true;
            }
            $scope.touchLock=false;

        };
        $scope.playMusic=function() {
            if ($scope.musicLock) { //判读是否播放
                music.pause();
                $scope.musicLock=false;
            }
            else{
                music.play();
                $scope.musicLock=true;
            }
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
                         // window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
                         window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";

                     }
                     else{
                         $scope.openid=data.openid;
                         console.log("my scope.openid", $scope.openid);
                         getNickNameAndRanking.save({"openid":$scope.openid},function (data) {
                             if(data.nickName!=""){
                                 nickName=data.nickName;
                             }
                             console.log("nickName",nickName);

                         });
                     }

                 },

                 error : function() {

                 }

             });

            var music = document.getElementById("music");//获取ID
            if(!music.paused){
                $scope.musicLock=true;
                console.log("111"+!music.paused)
            }
            //页面swipe 初始化
            var arrObj=document.getElementById('array')
            scaleW=window.innerWidth/320;
            scaleH=window.innerHeight/480;
            var resizes = document.querySelectorAll('.resize');
            for (var j=0; j<resizes.length; j++) {
                resizes[j].style.width=parseInt(resizes[j].style.width)*scaleW+'px';
                resizes[j].style.height=parseInt(resizes[j].style.height)*scaleH+'px';
                resizes[j].style.top=parseInt(resizes[j].style.top)*scaleH+'px';
                resizes[j].style.bottom=parseInt(resizes[j].style.bottom)*scaleH+'px';
                resizes[j].style.left=parseInt(resizes[j].style.left)*scaleW+'px';
                resizes[j].style.right=parseInt(resizes[j].style.right)*scaleW+'px';
            }
            var mySwiper = new Swiper ('.swiper-container', {
                direction : 'vertical',
                pagination: '.swiper-pagination',
                //virtualTranslate : true,
                mousewheelControl : true,
                onInit: function(swiper){
                    swiperAnimateCache(swiper);
                    swiperAnimate(swiper);
                },
                onSlideChangeEnd: function(swiper){
                    swiperAnimate(swiper);
                    recordLogs("BHS_H5_LJJR_"+(mySwiper.activeIndex+1));
                    if(mySwiper.activeIndex==4){
                        arrObj.style.display="none";
                    }
                    else{
                        arrObj.style.display="block";
                    }
                },
                onTransitionEnd: function(swiper){
                    swiperAnimate(swiper);
                },
                watchSlidesProgress: true,
                onProgress: function(swiper){
                    for (var i = 0; i < swiper.slides.length; i++){
                        var slide = swiper.slides[i];
                        var progress = slide.progress;
                        var translate = progress*swiper.height/4;
                        scale = 1 - Math.min(Math.abs(progress * 0.5), 1);
                        var opacity = 1 - Math.min(Math.abs(progress/2),0.5);
                        slide.style.opacity = opacity;
                        es = slide.style;
                        es.webkitTransform = es.MsTransform = es.msTransform = es.MozTransform = es.OTransform = es.transform = 'translate3d(0,'+translate+'px,-'+translate+'px) scaleY(' + scale + ')';
                    }
                },
                onSetTransition: function(swiper, speed) {

                    for (var i = 0; i < swiper.slides.length; i++){

                        es = swiper.slides[i].style;

                        es.webkitTransitionDuration = es.MsTransitionDuration = es.msTransitionDuration = es.MozTransitionDuration = es.OTransitionDuration = es.transitionDuration = speed + 'ms';



                    }

                },



            })

            recordLogs("BHS_H5_LJJR_1");

            recordLogs("UmbrellaShareLeadPage_"+ $stateParams.id);

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

                                title: '为了你的孩子，'+nickName+'邀请你加入爱心公益，并赠送40万的现金保障！', // 分享标题

                                link: "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/"+$stateParams.status,

                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标

                                success: function (res) {

                                    recordLogs("BHS_WDBZ_FXPYQ_Lead_"+$stateParams.id);

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

                                title: '为了你的孩子，'+nickName+'邀请你加入爱心公益，并赠送40万的现金保障！', // 分享标题
                                desc: "由宝大夫和中国儿童少年基金会联合发起，绝对值得信赖！", // 分享描述

                                link:"http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/"+$stateParams.status,

                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标

                                success: function (res) {

                                    recordLogs("BHS_WDBZ_FXPY_Lead_"+$stateParams.id);

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

        });





    }]);