angular.module('controllers', ['ionic']).controller('myCardCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','GetCardInfoList','ChooseCard',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,GetCardInfoList,ChooseCard) {
        //获取基本信息接口
        GetCardInfoList.save({},function(res){
            $scope.card=res.cardInfoVo;
        })

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
            console.log(num);
            switch(num){
                case "5.0" : $scope.bigCardImg=$scope.carImgUrl.wu;console.log(5);break;
                case "10.0" : $scope.bigCardImg=$scope.carImgUrl.shi;console.log(10);break;
                case "20.0" : $scope.bigCardImg=$scope.carImgUrl.ershi;console.log(20);break;
                case "30.0" : $scope.bigCardImg=$scope.carImgUrl.sanshi;console.log(30);break;
                case "40.0" : $scope.bigCardImg=$scope.carImgUrl.sishi;console.log(40);break;
            }

        }

        //大图初始化
        $scope.bigCardImg=$scope.carImgUrl.yaoqing;


        //大图显示状态初始化
        $scope.bigCard_status=true;
        $scope.cash_status=false;


        //点击按钮状态初始化
        $scope.invite_friend_status=true;
        $scope.draw_prize_status=false;
        $scope.receive_prize_status=false;
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
            switch ($scope.typeCard){
                case 0:$scope.card.cardBig--;break;
                case 1:$scope.card.cardRuyi--;break;
                case 2:$scope.card.cardYoushan--;break;
                case 3:$scope.card.cardHealth--;break;
                case 4:$scope.card.cardHappy--;break;
                case 5:$scope.card.cardLove--;break;
            }
            ChooseCard.save({openId:$scope.card.openId,id:$scope.card.id,typeCard:$scope.typeCard},function(res){
                if(res.status=="success"){
                    if(res.type==0){
                        //大图显示状态初始化
                        $scope.bigCard_status=true;
                        $scope.cash_status=false;
                        $scope.btn_status(3);
                        console.log(res.moneyCount)
                        $scope.coupon(res.moneyCount)
                    }else if(res.type==1){
                        //大图显示状态初始化
                        $scope.bigCard_status=true;
                        $scope.cash_status=false;
                        $scope.btn_status(4);
                    }
                }else{
                    alert('服务器错误')
                }
            })
        }

        //领取奖品点击事件
        $scope.receive_prize=function(){

        }

    }])
