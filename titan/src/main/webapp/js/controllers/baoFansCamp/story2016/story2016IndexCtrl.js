angular.module('controllers', ['ionic']).controller('story2016IndexCtrl', [
    '$scope','$state','$stateParams','getUser2016Data',
    function ($scope,$state,$stateParams,getUser2016Data) {
        $scope.dataInfo = {};
        $scope.attentionOnly = false;
        $scope.story8show = "";
        $scope.story8data = "";


        $scope.$on('$ionicView.enter',function() {
            $(".attention-only").hide();
            getUser2016Data.save({},function (data) {
                console.log("2016数据",data);
                $scope.dataInfo=data;
                //根据用户数据，判断显示或删除那个盒子
                var removeIndex=2;
                if($scope.dataInfo.firstConsultTime=='null'){
                    mySwiper.removeSlide(removeIndex);
                    mySwiper.removeSlide(removeIndex);
                    mySwiper.removeSlide(removeIndex);
                    mySwiper.removeSlide(removeIndex);

                }
                removeIndex = removeIndex + 2;
                if($scope.dataInfo.FirstEvaluationTime=='null'){
                    mySwiper.removeSlide(removeIndex);
                }else{
                    ++removeIndex;
                }
                if($scope.dataInfo.FirstRedPacketTime=='null'){
                    mySwiper.removeSlide(removeIndex);
                }else{
                    ++removeIndex;
                }
                if($scope.dataInfo.joinUmbrellaTime=="null"){
                    mySwiper.removeSlide(removeIndex);
                }else{
                    ++removeIndex;
                }
                if($scope.dataInfo.joinBaoDaiFuForYou=='null'){
                    mySwiper.removeSlide(removeIndex);
                }
                $(".swiper-pagination").show();

                //判断最后一页的属于什么大使
                if( $scope.dataInfo.joinBaoDaiFuForYou!='null'){
                    $scope.story8show="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/story2016/story8_1.png";
                    $scope.story8data= $scope.dataInfo.joinBaoDaiFuForYou+"位"
                }
                if( $scope.dataInfo.joinBaoDaiFuForYou=='null' &&  $scope.dataInfo.FirstRedPacketTime!='null'){
                    $scope.story8show="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/story2016/story8_2.png";
                    $scope.story8data= $scope.dataInfo.consultTitleNumber+"位"
                }
                if($scope.dataInfo.joinBaoDaiFuForYou=='null' &&  $scope.dataInfo.FirstRedPacketTime=='null' &&  $scope.dataInfo.FirstEvaluationTime!='null'){
                    $scope.story8show="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/story2016/story8_3.png";
                    $scope.story8data= $scope.dataInfo.evaluationCount+"次"
                }
                if( $scope.dataInfo.joinBaoDaiFuForYou=='null' &&  $scope.dataInfo.FirstRedPacketTime=='null' &&  $scope.dataInfo.FirstEvaluationTime=='null' &&  $scope.dataInfo.firstConsultTime!='null'){
                    $scope.story8show="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/story2016/story8_4.png";
                    $scope.story8data= $scope.dataInfo.consultTitleNumber+"次"
                }
                if( $scope.dataInfo.joinBaoDaiFuForYou=='null' &&  $scope.dataInfo.FirstRedPacketTime=='null' &&  $scope.dataInfo.FirstEvaluationTime=='null' &&  $scope.dataInfo.firstConsultTime=='null'){
                    $scope.story8show="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/story2016/story_no.png";
                }
                //判断用户除了关注是否有其他操作 （咨询 宝护伞 分享）
                if($scope.dataInfo.firstConsultTime!='null'  ||$scope.dataInfo.joinBaoDaiFuForYou!='null'  || $scope.dataInfo.joinUmbrellaTime!='null'){
                    $(".swiper-container").show();
                    $(".attention-only").hide();
                }
               else{
                    $(".swiper-container").hide();
                    $(".attention-only").show();
                }



            });
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


        });


    }
]);

