angular.module('controllers', ['ionic']).controller('momNutritionResultCtrl', [
    '$scope','$state','$stateParams','GetMarketingActivitiesByOpenid','UpdateMarketingActivities','$http',
    function ($scope,$state,$stateParams,GetMarketingActivitiesByOpenid,UpdateMarketingActivities,$http) {

        $scope.goShareLock =false;
        $scope.lookResultLock =false;
        $scope.momRank = [
            {
                title:"天才妈妈",
                text:[
                    "我的天呐，打败了99%的妈妈！",
                    "在您的培养下，宝宝未来的前途绝对不可限量呦！"
                ],
                share:[
                    "分享到朋友圈，帮助更多宝妈重视起来宝宝的营养问题吧！" ,
                    "顺便发张截图还可以抽个大奖哟！",
                    "（满分的获奖率特别高！）"
                ],
                remind:"天才的您是否在答题过程中存在犹豫？"
            },
            {
                title:"不合格宝妈 ",
                text:[
                    "宝宝心里苦啊，打败40%-50%的妈妈！",
                    "宝宝营养的每一步都不可掉以轻心哦！"
                ],
                share:[
                    "分享到朋友圈，帮助更多宝妈重视起来宝宝的营养问题吧！顺便发张截图还可以抽个大奖哟！"
                ],
                remind:"想知道自己哪个问题做错了？"


            },
            {
                title:"逗比宝妈",
                text:[
                    "负分，吓死宝宝了，打败@&%的妈妈",
                    "对于宝宝来说，有一种妈妈叫别人家的妈妈！"
                ],
                share:[
                    "分享到朋友圈，帮助更多宝妈重视起来宝宝的营养问题吧！顺便发张截图还可以抽个大奖哟！"
                ],
                remind:"想知道为什么错的这么离谱？"
            }
        ];
        var sharecontent = "";

        //点击查看结果按钮
        $scope.lookResult = function(){
            $scope.lookResultLock=true;
            setLog("WYKDA");
        };
        $scope.$on('$ionicView.enter', function(){
            if($stateParams.result==10){
                $scope.momRankNum = 0;
                sharecontent = "经测试，我是喂养宝宝的天才宝妈！你呢？参与可得iPhone6s";
            }else if($stateParams.result>=5&&$stateParams.result<=9){
                $scope.momRankNum = 1;
                sharecontent = "经测试，我的宝宝喂养知识要加强！你呢？参与可得iPhone6s";
                GetMarketingActivitiesByOpenid.save({"score":$stateParams.result}, function (data) {
                    $scope.momRank[1].text[0] = "宝宝心里苦啊，打败"+data.accounting+"的妈妈！";
                });
            }else{
                $scope.momRankNum = 2;
                sharecontent = "经测试，我是应该当爸爸的宝妈，哭！你呢？参与可得iPhone6s";
            }
            $scope.Refresh();
            setLog("HDJGY");
        });

        //分享提示
        $scope.goShare = function(){
            $scope.goShareLock =true;
            setLog("WYFX");

        };
        //取消提示
        $scope.cancelRemind = function(){
            $scope.goShareLock =false;
            $scope.lookResultLock =false;
        };

        function setLog(parmes){
            var pData = {logContent:encodeURI(parmes)};
            $http({method:'post',url:'util/recordLogs',params:pData});
        }

        $scope.Refresh = function(){
            var share = "http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/fieldwork/wechat/author?url=http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/getUserWechatMenId?url=11&state='FXJG_PYQ'";
            var share2 = "http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/fieldwork/wechat/author?url=http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/getUserWechatMenId?url=11&state='FXJG_PYXX'";
            var share3 = "http://s11.baodf.com/xiaoerke-marketing-webapp/firstPage/momNutritionTest";
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
                                'onMenuShareQQ',
                                'onMenuShareWeibo',
                                'onMenuShareQZone'
                            ] // 功能列表
                        });
                        wx.ready(function () {
                            // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                            wx.onMenuShareTimeline({
                                title: sharecontent, // 分享标题
                                link: share, // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fmarketing_share.png', // 分享图标
                                success: function (res) {
                                    //记录用户分享文章
                                    UpdateMarketingActivities.save({"id":$stateParams.id,"ifShare":"1"}, function (data) {
                                        
                                    });
                                    setLog("FXJG_FXPYQ");
                                },
                                fail: function (res) {
                                }
                            });

                            wx.onMenuShareAppMessage({
                                title: '测测你给宝宝的喂养科学吗？参与可得iPhone6s等', // 分享标题
                                desc: '99%的妈妈都不合格！99万的妈妈测试了都说好！', // 分享描述
                                link:share2, // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fmarketing_share.png', // 分享图标
                                success: function (res) {
                                    UpdateMarketingActivities.save({"id":$stateParams.id,"ifShare":"1"}, function (data) {

                                    });
                                    setLog("FXJG_FXPY");
                                },
                                fail: function (res) {
                                }
                            });
                            wx.onMenuShareQQ({
                                title: '测测你给宝宝的喂养科学吗？参与可得iPhone6s等', // 分享标题
                                desc: '99%的妈妈都不合格！99万的妈妈测试了都说好！', // 分享描述
                                link: share3, // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fmarketing_share.png', // 分享图标
                                success: function () {
                                    // 用户确认分享后执行的回调函数
                                    setLog("FXJG_FXQQ");
                                },
                                cancel: function () {
                                    // 用户取消分享后执行的回调函数
                                }
                            });
                            wx.onMenuShareWeibo({
                                title: '测测你给宝宝的喂养科学吗？参与可得iPhone6s等', // 分享标题
                                desc: '99%的妈妈都不合格！99万的妈妈测试了都说好！', // 分享描述
                                link: share3, // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fmarketing_share.png', // 分享图标
                                success: function () {
                                    // 用户确认分享后执行的回调函数
                                    setLog("FXJG_FXWeibo");
                                },
                                cancel: function () {
                                    // 用户取消分享后执行的回调函数
                                }
                            });
                            wx.onMenuShareQZone({
                                title: '测测你给宝宝的喂养科学吗？参与可得iPhone6s等', // 分享标题
                                desc: '99%的妈妈都不合格！99万的妈妈测试了都说好！', // 分享描述
                                link: share3, // 分享链接
                                imgUrl: 'http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Fmarketing_share.png', // 分享图标
                                success: function () {
                                    // 用户确认分享后执行的回调函数
                                    setLog("FXJG_FXQZone");
                                },
                                cancel: function () {
                                    // 用户取消分享后执行的回调函数
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

