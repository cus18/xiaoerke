angular.module('controllers', ['ionic']).controller('story2016IndexCtrl', [
    '$scope','$state','$stateParams','getUser2016Data',
    function ($scope,$state,$stateParams,getUser2016Data) {
        $scope.dataInfo = {};


        $scope.$on('$ionicView.enter',function() {

            var scaleW=window.innerWidth/320;
            var scaleH=window.innerHeight/480;
            var resizes = document.querySelectorAll('.resize');
            for (var j=0; j<resizes.length; j++) {
                resizes[j].style.width=parseInt(resizes[j].style.width)*scaleW+'px';
                resizes[j].style.height=parseInt(resizes[j].style.height)*scaleH+'px';
                resizes[j].style.top=parseInt(resizes[j].style.top)*scaleH+'px';
                resizes[j].style.bottom=parseInt(resizes[j].style.bottom)*scaleH+'px';
                resizes[j].style.left=parseInt(resizes[j].style.left)*scaleW+'px';
                resizes[j].style.right=parseInt(resizes[j].style.right)*scaleW+'px';
                resizes[j].style.right=parseInt(resizes[j].style.fontSize)*scaleW+'px';
            }
            var mySwiper = new Swiper ('.swiper-container', {
                direction : 'horizontal',
                pagination: '.swiper-pagination',
                onInit: function(swiper){ //Swiper2.x的初始化是onFirstInit
                    swiperAnimateCache(swiper); //隐藏动画元素
                    swiperAnimate(swiper); //初始化完成开始动画
                },
                onSlideChangeEnd: function(swiper){
                    swiperAnimate(swiper); //每个slide切换结束时也运行当前slide动画
                    if(mySwiper.isEnd){
                        $(".swiper-pagination").hide();
                        $(".remind-arr").hide();
                        $(".remind-save").show();
                    }else{
                        $(".swiper-pagination").show();
                        $(".remind-arr").show();
                        $(".remind-save").hide();
                    }
                }
            })

            getUser2016Data.save({},function (data) {
                console.log("2016数据",data);
                $scope.dataInfo=data;
                if($scope.dataInfo.firstConsultTime=='null'){
                    mySwiper.removeSlide(2);
                    mySwiper.removeSlide(3);

                }
                if($scope.dataInfo.FirstEvaluationTime=='null'){
                    mySwiper.removeSlide(4);
                }
                if($scope.dataInfo.FirstRedPacketTime=='null'){
                    mySwiper.removeSlide(5);
                }

                if($scope.dataInfo.joinUmbrellaTime=="null"){
                    mySwiper.removeSlide(6);
                }
                if($scope.dataInfo.joinBaoDaiFuForYou=='null'){
                    mySwiper.removeSlide(7);
                }
                $(".swiper-pagination").show();
              /* if(mySwiper.activeIndex==mySwiper.slides.length){
                   $(".swiper-pagination").hide();
                }*/



            });
        });


    }
]);

