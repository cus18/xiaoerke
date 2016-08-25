angular.module('controllers', ['ionic']).controller('umbrellaPaySuccessCtrl', [
    '$scope','$state','$stateParams','getUserQRCode','getNickNameAndRanking',
    function ($scope,$state,$stateParams,getUserQRCode,getNickNameAndRanking) {
        $scope.title="宝大夫儿童家庭重疾互助计划";
        $scope.QRCodeURI="";
        var nickName="我真心";
        $scope.$on('$ionicView.enter', function() {
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
            getUserQRCode.save({"id":$stateParams.id}, function (data){
                $scope.QRCodeURI=data.qrcode;
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
                                title: '限时免费加入宝护伞爱心公益，小孩、大人得了重病都给钱！最高40万！', // 分享标题
                                link: "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/a",
                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/umbrella/A8327D229FE265D234984EF57D37EC87.jpg', // 分享图标
                                success: function (res) {
                                    recordLogs("BHS_WDBZ_FXPYQ_"+$stateParams.id);
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
                                title: '限时免费加入宝护伞爱心公益，小孩、大人得了重病都给钱！最高40万！', // 分享标题
                                desc: "由宝大夫和中国儿童少年基金会联合发起，绝对值得信赖！", // 分享描述
                                link:"http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/"+$stateParams.id+"/a",
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
            });
        })
    }]);