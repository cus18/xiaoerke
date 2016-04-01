angular.module('controllers', ['ionic']).controller('nutritionFoodCtrl', [
    '$scope','$state','$stateParams','$sce','$ionicScrollDelegate','GetBabyInfo','GetRecipes','$location','$anchorScroll',
    '$http','GetUserLoginStatus','SaveManagementInfo',
    function ($scope,$state,$stateParams,$sce,$ionicScrollDelegate,GetBabyInfo,GetRecipes,$location,$anchorScroll,
              $http,GetUserLoginStatus,SaveManagementInfo) {

        $scope.scrollNum=0;
        $scope.extraFood=true;// 是否加餐
        $scope.openText="展开";
        $scope.openImg="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png";
        $scope.openImgDown="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png";
        $scope.openImgUp="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_up.png";

        $scope.cancelRemind=function(){
            if($scope.buttonRemind1==true){
                $scope.buttonRemind1=false;// 是否按照食谱照做
                $scope.buttonRemind2=true;// 自己做饭
                localStorage.r1 = "ok";
            }else if($scope.buttonRemind2==true){
                $scope.buttonRemind1=false;// 是否按照食谱照做
                $scope.buttonRemind2=false;// 自己做饭
                localStorage.r2 = "ok";
                $ionicScrollDelegate.scrollTop();
            }
        }


        $scope.openMore=function(index){
            $scope.index=index;
            if( $scope.foodList[index].openImg=="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png"){
                $scope.foodList[index].openImg="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_up.png";
                $scope.foodList[index].openText="收起";
                $(".open").eq(index).siblings("p").removeClass("select");

            }
            else{
                $scope.foodList[index].openImg="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png";
                $scope.foodList[index].openText="展开";
                $(".open").eq(index).siblings("p").addClass("select");
            }

        }
        $scope.nutritionAssessResult =function(){
            var pData = {logContent:encodeURI("YYGL_SY_ZSPZD")};
            $http({method:'post',url:'util/recordLogs',params:pData});
            $state.go("nutritionPyramid");
        };
        $scope.nutritionAssess =function(){
            var pData = {logContent:encodeURI("YYGL_SY_ZJZD")};
            $http({method:'post',url:'util/recordLogs',params:pData});
            $state.go("nutritionAssess",{flag:"noagain"});
        };

        $scope.$on('$ionicView.enter', function(){
            $ionicScrollDelegate.scrollTop();
            $scope.foodList = [] ;
            var food = {};
            var food1 = {};
            var food2 = {};
            var food3 = {};
            var food4 = {};
            var routePath = "/ap/ntrBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    //获取宝宝信息
                    GetBabyInfo.save({}, function (data){
                            if(data.babyInfo==undefined){
                                SaveManagementInfo.get({"planTemplateId":2}, function (data) {
                                    if(data.resultMsg=="OK"){
                                        window.location.href = "ap/ntr?value=251338#/nutritionBabyInfo";
                                    }
                                });
                            }else{
                                setLog("YYGL_SP");
                                var babyInfo = data.babyInfo.split(";");
                                GetRecipes.get({"birthday":babyInfo[1]}, function (data) {
                                    food.title = data.am.title;
                                    food.mealName = [];
                                    for(var i = 1;i<data.am.keywords.split(" ").length;i++){
                                        food.mealName[i-1] = data.am.keywords.split(" ")[i];
                                    }
                                    food.food = data.am.description.split(" ");
                                    food.openText = "展开";
                                    food.openImg="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png";
                                    food.pic="http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/"+data.am.keywords.split(" ")[0],
                                    food.effect = $sce.trustAsHtml(data.am.content);
                                    $scope.foodList[0] = food;
                                    food1.title = data.amp.title;
                                    food1.mealName = [];
                                    for(var i = 1;i<data.amp.keywords.split(" ").length;i++){
                                        food1.mealName[i-1] = data.amp.keywords.split(" ")[i];
                                    }
                                    food1.food = data.amp.description.split(" ");
                                    food1.openText = "展开";
                                    food1.openImg="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png";
                                    food1.pic="http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/"+data.amp.keywords.split(" ")[0],
                                    food1.effect = $sce.trustAsHtml(data.amp.content);
                                    $scope.foodList[1] = food1;
                                    food2.title = data.mm.title;
                                    food2.mealName = [];
                                    for(var i = 1;i<data.mm.keywords.split(" ").length;i++){
                                        food2.mealName[i-1] = data.mm.keywords.split(" ")[i];
                                    }
                                    food2.food = data.mm.description.split(" ");
                                    food2.openText = "展开";
                                    food2.openImg="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png";
                                    food2.pic="http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/"+data.mm.keywords.split(" ")[0],
                                    food2.effect = $sce.trustAsHtml(data.mm.content);
                                    $scope.foodList[2] = food2;
                                    food3.title = data.mmp.title;
                                    food3.mealName = [];
                                    for(var i = 1;i<data.mmp.keywords.split(" ").length;i++){
                                        food3.mealName[i-1] = data.mmp.keywords.split(" ")[i];
                                    }
                                    food3.food = data.mmp.description.split(" ");
                                    food3.openText = "展开";
                                    food3.openImg="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png";
                                    food3.pic="http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/"+data.mmp.keywords.split(" ")[0],
                                    food3.effect = $sce.trustAsHtml(data.mmp.content);
                                    $scope.foodList[3] = food3;
                                    food4.title = data.pm.title;
                                    food4.mealName = [];
                                    for(var i = 1;i<data.pm.keywords.split(" ").length;i++){
                                        food4.mealName[i-1] = data.pm.keywords.split(" ")[i];
                                    }
                                    food4.food = data.pm.description.split(" ");
                                    food4.openText = "展开";
                                    food4.openImg="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png";
                                    food4.pic="http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/"+data.pm.keywords.split(" ")[0],
                                    food4.effect = $sce.trustAsHtml(data.pm.content);
                                    $scope.foodList[4] = food4;
                                    $scope.Refresh();
                                    if(localStorage.getItem("r1")==undefined&&localStorage.getItem("r2")==undefined){
                                        $scope.buttonRemind1=true;// 是否按照食谱照做
                                        $ionicScrollDelegate.scrollBottom();
                                        //$location.hash('bottom');
                                        //$anchorScroll();
                                    }else{
                                        $scope.buttonRemind1=false;// 是否按照食谱照做
                                        $scope.buttonRemind2=false;// 自己做饭
                                    }

                                });
                            }
                        }
                    );
                }});
        });

        function setLog(item){
            var pData = {logContent:encodeURI(item)};
            $http({method:'post',url:'util/recordLogs',params:pData});
        }

        $scope.Refresh = function(){
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
                                title: '宝大夫营养师今日食谱', // 分享标题
                                link: window.location.href.replace("true","false"), // 分享链接
                                imgUrl: $scope.foodList[0].pic, // 分享图标
                                success: function (res) {

                                },
                                fail: function (res) {
                                }
                            });

                            wx.onMenuShareAppMessage({
                                title: '宝大夫营养师今日食谱', // 分享标题
                                desc: '还在为今天该给宝宝做什么饭发愁吗？营养师给你准备好了！赶快点击看一下哦！', // 分享描述
                                link: window.location.href.replace("true","false"), // 分享链接
                                imgUrl: $scope.foodList[0].pic, // 分享图标
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

        }


    }]);

