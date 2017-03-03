angular.module('controllers', ['ionic']).controller('myCardCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','GetCardInfoList','ChooseCard','UpdateRedPackageInfo','GetConfig',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,GetCardInfoList,ChooseCard,UpdateRedPackageInfo,GetConfig) {
        //图片地址
        $scope.carImgUrl={
            yaoqing:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/di_png_03.png',
            fu:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_fu.png',
            big_ru:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_ru.png',
            big_shan:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_shan.png",
            big_kang:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_kang.png',
            big_le:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_le.png",
            big_ai:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_ai.png',
            //红包
            wu:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/5.png',
            shi:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/10.png',
            ershi:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/20.png',
            sanshi:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/30.png',
            sishi:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/40.png'
        }
        //代金券状态
        $scope.coupon=function(num){
            switch(num){
                case "5.0" : $scope.bigCardImg=$scope.carImgUrl.wu;break;
                case "10.0" : $scope.bigCardImg=$scope.carImgUrl.shi;break;
                case "20.0" : $scope.bigCardImg=$scope.carImgUrl.ershi;break;
                case "30.0" : $scope.bigCardImg=$scope.carImgUrl.sanshi;break;
                case "40.0" : $scope.bigCardImg=$scope.carImgUrl.sishi;break;
            }

        }










        //按钮状态的封装；
        $scope.btn_status=function(num){
            switch (num){
                case 1 :(function(){
                    $scope.invite_friend_status=true;
                    $scope.draw_prize_status=false;
                    $scope.receive_prize_status=false;
                })(); break;
                case 2 :(function(){
                    $scope.invite_friend_status=false;
                    $scope.draw_prize_status=true;
                    $scope.receive_prize_status=false;
                })(); break;
                case 3 :(function(){
                    $scope.invite_friend_status=false;
                    $scope.draw_prize_status=false;
                    $scope.receive_prize_status=true;
                })(); break;
                case 4 :(function(){
                    $scope.invite_friend_status=false;
                    $scope.draw_prize_status=false;
                    $scope.receive_prize_status=false;
                })(); break;
            }
        }



        //分享到朋友圈或者微信
        var loadShare = function($scope){
            // redPacketCreate.save({"uuid":$scope.uuid},function (data) {
            //     $scope.uuid = data.uuid;
            // });
            GetConfig.save({}, function (data) {
                $scope.inviteUrlData = data.publicSystemInfo.redPackageShareUrl;

                var share = $scope.inviteUrlData + $scope.openid+","+$scope.market+",";//最后url=41，openid,marketer

                // var share = $scope.inviteUrlData + $scope.openid+","+$scope.marketer+","+ $scope.uuid+",";//最后url=41，openid,marketer
                // if(version=="a"){
                version="a";
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
                                    'onMenuShareAppMessage',
                                    'previewImage'
                                ] // 功能列表
                            });
                            wx.ready(function () {
                                // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                                wx.onMenuShareTimeline({
                                    title: '感恩妈妈节，在这里可以免费咨询三甲医院儿科专家,还有机会赢现金大礼', // 分享标题
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                    success: function (res) {
                                        // redPacketCreate.save({"uuid":$scope.uuid},function (data) {
                                        // });
                                    },
                                    fail: function (res) {
                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title: $scope.minename  + '向你推荐', // 分享标题
                                    desc: '感恩妈妈节，在这里可以免费咨询三甲医院儿科专家,还有机会赢现金大礼 ',// 分享描述
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                    success: function (res) {
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
        };


        //获取基本信息接口
        $scope.card={
            cardRuyi:0,
            cardYoushan:0,
            cardHealth:0,
            cardHappy:0,
            cardLove:0
        }

        //推送消息链接进来后查看的代金券
        $state.cardInfo=$stateParams.cardInfo;

        $scope.init_data=function(){
           // 初始化一些图片和按钮的状态，判断是否从推送夜进来的
            if( $stateParams.type=='' && $stateParams.moneyCount==''){
                //大图初始化
                $scope.bigCardImg=$scope.carImgUrl.yaoqing;
                //大图显示状态初始化
                $scope.bigCard_status=true;
                $scope.cash_status=false;
                //点击按钮状态初始化
                $scope.invite_friend_status=true;
                $scope.draw_prize_status=false;
                $scope.receive_prize_status=false;

            }else{
                if($stateParams.type==0){
                    $scope.btn_status(3);
                    $scope.bigCard_status=true;
                    $scope.cash_status=false;
                    $scope.coupon($stateParams.moneyCount+".0");
                    $scope.quan=$stateParams.moneyCount+".0";
                }else if($stateParams.type==1){
                    $scope.btn_status(4);
                    $scope.bigCard_status=false;
                    $scope.cash_status=true;
                    $scope.cash=$stateParams.moneyCount;
                }
            }
            GetCardInfoList.save({},function(res){
                $scope.card=res.cardInfoVo;
                $scope.openid=res.cardInfoVo.openId;
                $scope.market=res.cardInfoVo.market;
                $scope.minename=res.nickName;
                loadShare($scope);
                $scope.card=res.cardInfoVo;
                if($scope.card.cardRuyi>0 && $scope.card.cardYoushan>0 && $scope.card.cardHealth>0 && $scope.card.cardHappy>0 && $scope.card.cardLove>0 ){
                    $scope.hecheng_status=true;
                    //点击确定合成
                    $scope.sure=function(){
                        $scope.card.cardRuyi--;
                        $scope.card.cardYoushan--;
                        $scope.card.cardHealth--;
                        $scope.card.cardHappy--;
                        $scope.card.cardLove--;
                        UpdateRedPackageInfo.save({id:$scope.card.id,openId:$scope.card.openId},function(res){
                            $scope.card.cardBig++;
                            $scope.hecheng_status=false;
                            //如果可以合成多张的话，继续回调
                            $scope.init_data();
                        })
                    }
                    //点击关闭合成，
                    $scope.close=function(){
                        $scope.hecheng_status=false;
                    }
                }else{
                    $scope.hecheng_status=false;
                }
            })
        };



        setTimeout(function(){
            var  swiper = new Swiper('.swiper-container', {
                pagination: '.swiper-pagination',
                slidesPerView: 5,
                paginationClickable: true,
                observer:true,//修改swiper自己或子元素时，自动初始化swiper
                observeParents:true,//修改swiper的父元素时，自动初始化swiper
                spaceBetween: 15
            });
        },0);





        //初始化卡片状态
        $scope.fuli_status=false;
        $scope.ru_status=false;
        $scope.shan_status=false;
        $scope.kang_status=false;
        $scope.le_status=false;
        $scope.ai_status=false;


        $scope.fuli_click=function(){
            $scope.typeCard=0;
            $scope.fuli_status=true;
            $scope.ru_status=false;
            $scope.shan_status=false;
            $scope.kang_status=false;
            $scope.le_status=false;
            $scope.ai_status=false;
            $scope.bigCardImg= $scope.carImgUrl.fu;
            $scope.btn_status(2);

        }
        $scope.ru_click=function(){
            $scope.typeCard=1;
            $scope.fuli_status=false;
            $scope.shan_status=false;
            $scope.kang_status=false;
            $scope.le_status=false;
            $scope.ai_status=false;
            if($scope.card.cardRuyi==0){
                $scope.bigCardImg=$scope.carImgUrl.yaoqing;
                $scope.btn_status(1);
            }else if($scope.card.cardRuyi>0){
                $scope.ru_status=true;
                $scope.bigCardImg=$scope.carImgUrl.big_ru;
                $scope.btn_status(2);
            }

        }
        $scope.shan_click=function(){
            $scope.typeCard=2;
            $scope.fuli_status=false;
            $scope.ru_status=false;
            $scope.kang_status=false;
            $scope.le_status=false;
            $scope.ai_status=false;
            if($scope.card.cardYoushan==0){
                $scope.bigCardImg=$scope.carImgUrl.yaoqing;
                $scope.btn_status(1);
            }else if($scope.card.cardYoushan>0){
                $scope.shan_status=true;
                $scope.bigCardImg=$scope.carImgUrl.big_shan;
                $scope.btn_status(2);

            }
        }
        $scope.kang_click=function(){
            $scope.typeCard=3;
            $scope.fuli_status=false;
            $scope.ru_status=false;
            $scope.shan_status=false;
            $scope.le_status=false;
            $scope.ai_status=false;
            if($scope.card.cardHealth==0){
                $scope.bigCardImg=$scope.carImgUrl.yaoqing;
                $scope.btn_status(1);
            }else if($scope.card.cardHealth>0){
                $scope.kang_status=true;
                $scope.bigCardImg=$scope.carImgUrl.big_kang;
                $scope.btn_status(2);
            }

        }
        $scope.le_click=function(){
            $scope.typeCard=4;
            $scope.fuli_status=false;
            $scope.ru_status=false;
            $scope.shan_status=false;
            $scope.kang_status=false;
            $scope.ai_status=false;
            if($scope.card.cardHappy==0){
                $scope.bigCardImg=$scope.carImgUrl.yaoqing;
                $scope.btn_status(1);
            }else if($scope.card.cardHappy>0){
                $scope.le_status=true;
                $scope.bigCardImg=$scope.carImgUrl.big_le;
                $scope.btn_status(2);
            }
        }
        $scope.ai_click=function(){
            $scope.typeCard=5;
            $scope.fuli_status=false;
            $scope.ru_status=false;
            $scope.shan_status=false;
            $scope.kang_status=false;
            $scope.le_status=false;
            if($scope.card.cardLove==0){
                $scope.bigCardImg=$scope.carImgUrl.yaoqing;
                $scope.btn_status(1);
            }else if($scope.card.cardLove>0){
                $scope.ai_status=true;
                $scope.bigCardImg=$scope.carImgUrl.big_ai;
                $scope.btn_status(2);
            }
        }




        $scope.friends_share=false;
        // 邀请好友点击事件
        $scope.invite_friend=function(){
            $scope.friends_share=true;
        }
        //关闭好友邀请弹窗
        $scope.share_close=function(){
            $scope.friends_share=false;
        }

        //兑换奖品点击事件
        $scope.draw_prize=function(){

            ChooseCard.save({openId:$scope.card.openId,id:$scope.card.id,typeCard:$scope.typeCard},function(res){
                switch ($scope.typeCard){
                    case 0:$scope.card.cardBig--;break;
                    case 1:$scope.card.cardRuyi--;break;
                    case 2:$scope.card.cardYoushan--;break;
                    case 3:$scope.card.cardHealth--;break;
                    case 4:$scope.card.cardHappy--;break;
                    case 5:$scope.card.cardLove--;break;
                }
                if(res.status=="success"){
                    if(res.type==0){
                        //大图显示状态初始化
                        $scope.bigCard_status=true;
                        $scope.cash_status=false;
                        $scope.btn_status(3);
                        $scope.quan=res.moneyCount;
                        $scope.coupon(res.moneyCount)
                    }else if(res.type==1){
                        //大图显示状态初始化
                        $scope.bigCard_status=true;
                        $scope.cash_status=false;
                        $scope.btn_status(4);
                        $scope.cash=res.moneyCount;
                    }
                }else{
                    alert('服务器错误')
                }
            })
        }

        //领取奖品点击事件
        $scope.receive_prize=function(){
           switch ( $scope.quan){
               case "5.0" : window.location.href='https://h5.youzan.com/v2/ump/promocard/fetch?alias=qmohxwgt';break;
               case "10.0" : window.location.href='https://h5.youzan.com/v2/ump/promocard/fetch?alias=pv3jvvzn';break;
               case "20.0" : window.location.href=' https://h5.youzan.com/v2/ump/promocard/fetch?alias=1h8v2p8hk';break;
               case "30.0" : window.location.href=' https://h5.youzan.com/v2/ump/promocard/fetch?alias=1a5ufjzzs';break;
               case "40.0" : window.location.href='https://h5.youzan.com/v2/ump/promocard/fetch?alias=v6vjlswx';break;
               default :alert('领取失败，请返回首页我的奖品记录里查看奖品')
           }

        }



    }])
