angular.module('controllers', ['ionic']).controller('recruitShopkeeperCtrl', [
    '$scope','$state','$stateParams','getUser2016Data',
    function ($scope,$state,$stateParams,getUser2016Data) {
        $scope.dataShopkeeper = {};//店长填写的信息
        $scope.dataShopkeeper = {};//店长填写的信息
        $scope.remindLock = false;//提示完成 的框是否显示
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
        }

        // 点击 立即申请
        $scope.commitInfo =function(){
            console.log("点击提交时 店长填写信息显示", $scope.dataShopkeeper);
            if($scope.dataShopkeeper.name==" " || $scope.dataShopkeeper.name==undefined){
                alert("请填写 联系人姓名");
            }
            else if($scope.dataShopkeeper.tel==" "|| $scope.dataShopkeeper.tel==undefined){
                alert("请填写 手机号");
            }
            else if($scope.dataShopkeeper.wechat==""|| $scope.dataShopkeeper.wechat==undefined){
                alert("请填写 微信名");
            }
            else{
                $(".c-shadow").fadeIn(500,function(){
                });
                $(".remind-finish").fadeIn(800,function(){
                });
                setTimeout(myFadeOut, 5000);

                console.log("全部填写完 店长填写信息显示", $scope.dataShopkeeper);
                recordLogs("dianzhang_"+$scope.dataShopkeeper.name+"|"+$scope.dataShopkeeper.tel+"|"+$scope.dataShopkeeper.wechat);
            }

        };
        var myFadeOut=function(){
            $(".remind-finish").fadeOut(500,function(){});
            $(".c-shadow").fadeOut(500,function(){});
        }
        // 输入框 获取焦点事件
        $scope.skipTop =function(){
            $(".slide61").hide();
            $(".swiper-pagination").hide();

        };
        // 输入框数 失去焦点事件
        $scope.skipOriginal =function(){
            $(".slide61").show();
            $(".swiper-pagination").show();
        };


        $scope.$on('$ionicView.enter',function() {

            var pageHeight=$(window).height();
             $(".swiper-container").css("height",pageHeight+"px");
            console.log("手机屏幕高度",pageHeight);

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

                }
            })


        });


    }
]);

