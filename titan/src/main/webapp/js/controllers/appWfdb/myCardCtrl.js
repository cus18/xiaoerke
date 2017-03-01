angular.module('controllers', ['ionic']).controller('myCardCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location) {
        //获取的卡片信息
        $scope.card=JSON.parse($stateParams.myCardInfo);
        //图片地址
        $scope.carImgUrl={
            ru:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/ru_bright.png',
            big_ru:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_ru.png',
            shan:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/shan_bright.png',
            big_shan:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_shan.png",
            kang:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/kang_bright.png',
            big_kang:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_kang.png',
            le:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/le_bright.png",
            big_le:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_le.png",
            ai:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/ai_bright.png',
            big_ai:'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/myCard/big_ai.png'
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
        },0)

        $scope.$watch("card",function(newValue,oldValue,scope){

        })
        $scope.sss=function(){
            if( $scope.card.cardBig!=0){
                $scope.card.cardBig--;
            }else{
                $scope.card.cardBig=5;
            }

        }

    }])
