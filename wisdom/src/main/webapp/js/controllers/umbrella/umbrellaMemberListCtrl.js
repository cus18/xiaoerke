angular.module('controllers', ['ionic']).controller('umbrellaMemberListCtrl', [

    '$scope','$state','$stateParams','getFamilyList','ifExistOrder','getNickNameAndRanking',

    function ($scope,$state,$stateParams,getFamilyList,ifExistOrder,getNickNameAndRanking) {

        $scope.title="宝护伞-宝大夫儿童家庭重疾互助计划";

        $scope.shareLock=false;

        $scope.link=



            /* 分享提示*/

            $scope.goShare=function(){

                $scope.shareLock=true;

            };

        /* 取消提示*/

        $scope.cancelShare=function(){

            $scope.shareLock=false;

        };

        /* 分享提示*/

        $scope.goShare=function(){

            $scope.shareLock=true;

        };

        /* 点击我的保障*/

        $scope.myGuarantee=function(){

            $state.go("umbrellaJoin",{});

        };



        getFamilyList.save({"id":$stateParams.id},function(data){

            console.log(data);

            $scope.familyList =data.familyList;

            for(var i=0;i<data.familyList.length;i++){

                $scope.familyList[i].birthday = moment(data.familyList[i].birthday).utc().zone(-9).format("YYYY-MM-DD");

                // $scope.familyList[i].birthday = moment(data.familyList[i].birthday).format("YYYY-MM-DD HH:ss");

                if($scope.familyList[i].sex==0){

                    $scope.familyList[i].sex = "女宝"

                }else if($scope.familyList[i].sex==1){

                    $scope.familyList[i].sex = "男宝"

                }else if($scope.familyList[i].sex==2){

                    $scope.familyList[i].sex = "宝爸"

                }else{

                    $scope.familyList[i].sex = "宝妈"

                }

            }

        })



        $scope.addMember=function(){

            recordLogs("BHS_CYLB_TJCY");

            window.location.href ="../wisdom/umbrella?value="+new Date().getTime()+"#/umbrellaMemberAdd/"+$stateParams.id+"/"+$stateParams.status;

        }



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



        $scope.immediateActive=function(){

            $state.go("umbrellaJoin",{id:new Date().getTime()});

        }



        $scope.goActive=function(){

            $state.go("umbrellaFillInfo",{id:$scope.umbrellaId});

        };



        $scope.goShare=function(){

            $scope.shareLock=true;

            /* 随机分享文案*/

            var shareTextArray=[

                "有了这个相当于多了个重疾保险，免费加入就能换来40万，一确诊就能给钱，比保险快多了！",

                "墙裂推荐，绝非广告，这个真的是很需要。是对孩子和家庭的负责！我已经加入啦，你还不快来！",

                "我为孩子健康负责，免费领取了40万的大病治疗费，你也来领吧！",

                "我为宝宝健康负责，竟然免费获得了40万的大病治疗费！你需要吗？",

                "领取40万的大病治疗费，万一看病不用愁，限时免费，先到先得啦！",

                "每天都有孩子因没钱治病而死。现在有40万治疗费，送给你！",

                "没什么好送的，40万的大病治疗费，送给你！",

                "最美的妈妈你别走，送你40万，让孩子健康去成长！",

                "如需江湖救急，这有40万的大病治疗费，速速来拿！"



            ];

            var randomNum=parseInt(9*Math.random());//分享文案随机数

            /*   $(".share p").html( shareTextArray[randomNum]);*/

            $scope.shareRandomText=shareTextArray[randomNum];

        };



        $scope.$on('$ionicView.enter', function(){
            var nickName="我真心";
            $.ajax({

                url:"umbrella/getOpenid",// 跳转到 action

                async:true,

                type:'post',

                cache:false,

                dataType:'json',

                success:function(data) {

                    if(data.openid=="none"){
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

            ifExistOrder.save(function (data) {

                if (data.result == "1") {

                    window.location.href = "../wisdom/firstPage/umbrella?id=" + $stateParams.id;

                }

                if(data.result==2 || data.umbrella.activation_time==null) {

                    window.location.href = "../wisdom/firstPage/umbrella?id=" + $stateParams.id;

                }else{

                    if(data.umbrella.id!=$stateParams.id) {

                        $scope.umbrellaId = data.umbrella.id;

                        window.location.href = "../wisdom/umbrella?value=" + new Date().getTime() + "#/umbrellaMemberList/" + $scope.umbrellaId + "/a";

                    }

                }

            });



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

                                link: "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/"+$stateParams.status, // 分享链接

                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标

                                success: function (res) {

                                    recordLogs("BHS_WDBZ_FXPY_"+$stateParams.id);

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


                                link:"http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/"+$stateParams.status, // 分享链接

                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标

                                success: function (res) {

                                    recordLogs("BHS_WDBZ_FXPY_"+$stateParams.id);

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

            });});

    }]);