angular.module('controllers', ['ionic']).controller('nutritionIndexCtrl', [
    '$scope','$state','$stateParams','$location','$filter','GetUserLoginStatus','GetBabyInfo','GetRecipes',
    'GetTodayRead','GetEvaluate','SaveManagementInfo','$ionicScrollDelegate','$http',
    function ($scope,$state,$stateParams,$location,$filter,GetUserLoginStatus,GetBabyInfo,GetRecipes,
              GetTodayRead,GetEvaluate,SaveManagementInfo,$ionicScrollDelegate,$http) {

        $scope.footerNum=0;
        $scope.homeImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer1_select.png";
        $scope.assessImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer2.png";
        $scope.necessaryImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer3.png";
        $scope.reportImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer4.png";

        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/ntrBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    $scope.commentImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fdianjiqian_png_03.png";
                    $ionicScrollDelegate.scrollTop();
                    SaveManagementInfo.get({planTemplateId:2},function(data){
                        if(data.resultMsg=="OK"){
                            if(localStorage.getItem("food")==undefined){
                                $scope.firstIntro=true;
                                $scope.resultAssess = "我敢做最强宝宝，你敢不敢来？";
                            }else{
                                $scope.firstIntro=false;
                                getAssessResult();
                            }
                            $scope.Refresh();
                            GetBabyInfo.save({}, function (data){
                                if(data.babyInfo==undefined){
                                    //$state.go("nutritionBabyInfo");
                                    window.location.href = "ntr?value=251335#/nutritionBabyInfo";
                                }else{
                                    var pData = {logContent:encodeURI("YYGL_SY")};
                                    $http({method:'post',url:'util/recordLogs',params:pData});
                                    var babyInfo = data.babyInfo.split(";");
                                    $scope.height = babyInfo[2];
                                    $scope.weight = babyInfo[3];

                                    GetRecipes.get({"birthday":babyInfo[1]}, function (data) {
                                        var currTime = $filter('date')(data.am.serverDate,'HH:mm');
                                        if(currTime<="9:00"){
                                            $scope.foodTitle = data.am.title;
                                            $scope.foodImg = data.am.keywords.split(" ")[0];
                                        }
                                        if(currTime>"9:00"&&currTime<="10:30"){
                                            $scope.foodTitle = data.amp.title;
                                            $scope.foodImg = data.amp.keywords.split(" ")[0];
                                        }
                                        if(currTime>"10:30"&&currTime<="13:30"){
                                            $scope.foodTitle = data.mm.title;
                                            $scope.foodImg = data.mm.keywords.split(" ")[0];
                                            console.log("tupian",$scope.foodImg);
                                        }
                                        if(currTime>"13:30"&&currTime<="16:30"){
                                            $scope.foodTitle = data.mmp.title;
                                            $scope.foodImg = data.mmp.keywords.split(" ")[0];
                                        }
                                        if(currTime>"16:30"&&currTime<="24:00"){
                                            $scope.foodTitle = data.pm.title;
                                            $scope.foodImg = data.pm.keywords.split(" ")[0];
                                        }

                                    });
                                }
                            });

                            //获取文章
                            GetTodayRead.save({}, function (data) {
                                $scope.habit = data.habitArticleMap.title;//饮食习惯
                                $scope.habitId = data.habitArticleMap.id;
                                $scope.habitArticleCategoryId = data.habitArticleMap.habitArticleCategoryId;
                                $scope.safe = data.safeArticleMap.title;//饮食安全
                                $scope.safeId = data.safeArticleMap.id;
                                $scope.safeArticleCategoryId = data.safeArticleMap.safeArticleCategoryId;
                            });
                        }
                        else{
                            window.location.href = "firstPage/healthPlan";
                        }
                    });
                }
            });

        });

        //获取用户评估情况
        function getAssessResult(){
            //evaluate
            GetEvaluate.get({"flag":"index"}, function (data) {
                if(data.td=="yes"){
                    $scope.resultAssess = "我敢做最强宝宝，你敢不敢来？";
                }
                if(data.ytn=="yes"){
                    $scope.resultAssess = "昨天没有做评估，麻麻今天别忘记哦!";
                }
                if(data.yty!=undefined){
                    if(data.yty.fishEggs.split(";")[1]!="just"){
                        $scope.resultAssess = "宝宝昨天有些吃的有点不合理哦，今天呢？";
                        return;
                    }else if(data.yty.meat.split(";")[1]!="just"){
                        $scope.resultAssess = "宝宝昨天有些吃的有点不合理哦，今天呢？";
                        return;
                    }else if(data.yty.milk.split(";")[1]!="just"){
                        $scope.resultAssess = "宝宝昨天有些吃的有点不合理哦，今天呢？";
                        return;
                    }else if(data.yty.millet.split(";")[1]!="just"){
                        $scope.resultAssess = "宝宝昨天有些吃的有点不合理哦，今天呢？";
                        return;
                    }else if(data.yty.oilSalt.split(";")[1]!="just"){
                        $scope.resultAssess = "宝宝昨天有些吃的有点不合理哦，今天呢？";
                        return;
                    }else if(data.yty.potato.split(";")[1]!="just"){
                        $scope.resultAssess = "宝宝昨天有些吃的有点不合理哦，今天呢？";
                        return;
                    }else if(data.yty.vegetables.split(";")[1]!="just"){
                        $scope.resultAssess = "宝宝昨天有些吃的有点不合理哦，今天呢？";
                        return;
                    }else if(data.yty.water.split(";")[1]!="just") {
                        $scope.resultAssess = "宝宝昨天有些吃的有点不合理哦，今天呢？";
                        return;
                    }else{
                        $scope.resultAssess = "宝宝昨天吃的不错哦，再接再厉哦!";
                    }
                }
            })
        }

        /**
         * 取消浮层
         */
        $scope.cancel =function(){
            localStorage.food = "ok";
            $scope.firstIntro=false;
        };

        /**
         * 跳转到健康管理首页
         */
        $scope.goManagement = function(){
            window.location.href = "firstPage/healthPlan?type=second";
        }

        /**
         * 跳转到评估页面
         */
        $scope.goAssess = function(data){
            //$state.go("nutritionAssess",{flag:"noagain"});
            window.location.href = "ntr?value=251348#/nutritionAssess/noagain";
        }

        /**
         * 饮食详情
         */
        $scope.nutritionFood =function(){
            $state.go("nutritionFood");
        };

        /**
         * 阅读文章
         */
        $scope.readArticle = function (id) {
            var pData = {logContent:encodeURI("YYGL_WZ")};
            $http({method:'post',url:'util/recordLogs',params:pData});
            window.location.href = "knowledge?value=251333#/" +
                "knowledgeArticleContent/" +id+",yygl";
        }

        /**
         * 文章查看更多
         */
        $scope.lookMore = function (id,type) {
            if(type==1){
                window.location.href = "knowledge#/todayChoiceNurslingList/"+id+",饮食,习惯";
            }else{
                window.location.href = "knowledge#/todayChoiceNurslingList/"+id+",饮食,贴士";
            }
        }



        /**
         * 妈妈必备
         */
        $scope.nutritionNecessary =function(){
            $state.go("nutritionNecessary");
        };

       /* 底部菜单选择*/
        $scope.menuSelect =function(index){
            $scope.index=index;
            if(index==0){
                $state.go("nutritionIndex");
            }
            else if(index==1){
                //$state.go("nutritionAssess",{flag:"noagain"});
                window.location.href = "ntr?value=251347#/nutritionAssess/noagain";
            }
            else if(index==2){
                //$state.go("nutritionReport",{type:"second"});
                window.location.href = "ntr?value=251333#/nutritionReport/second";
            }
            else if(index==3){
                $state.go("nutritionNecessary");
            }else if(index==4){
                $scope.commentImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fdianjihou_png_03.png";
                $state.go("nutritionAsk");
            }
        };

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
                                title: '宝大夫营养管理', // 分享标题
                                link: window.location.href.replace("true","false"), // 分享链接
                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Findex_baby_info.png', // 分享图标
                                success: function (res) {
                                    //记录用户分享文章

                                },
                                fail: function (res) {
                                }
                            });

                            wx.onMenuShareAppMessage({
                                title: '宝大夫营养管理', // 分享标题
                                desc: '1岁到3岁的营养食谱，专业营养师评估建议，专业营养食谱，营养评估建议，饮食习惯养成，饮食安全注意。', // 分享描述
                                link:window.location.href.replace("true","false"), // 分享链接
                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Findex_baby_info.png', // 分享图标
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

