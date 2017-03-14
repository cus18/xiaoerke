angular.module('controllers', ['ionic','ngDialog']).controller('homeCtrl', [
    '$scope','$state','$stateParams',"GetConfig","SharSeConsult",'BabyCoinInit',
    function ($scope,$state,$stateParams,GetConfig,SharSeConsult,BabyCoinInit) {
        $scope.maskStatus=false;
        $scope.content="";
        //安卓手机执行
        $(document).ready(function(){
            var Height=$(window).height();
            $(window).resize(function(){
                if($(window).height()<Height){
                    $('.share_box').find('.con').removeClass('con').addClass('txt')
                }else{
                    $('.share_box').find('.txt').removeClass('txt').addClass('con')
                }
            })
        })

        //苹果手机执行
        $(function() {
            // 解决输入法遮挡
            var timer = null;
            var timer2=null;
            $('#txt').on('focus', function() {
                clearInterval(timer);
                var index = 0;
                timer = setInterval(function() {
                    if(index>5) {
                        $('body').scrollTop(10000);
                        $('.share_box').find('.con').removeClass('con').addClass('txt')

                        clearInterval(timer);
                    }
                    index++;
                }, 50)
            })
            $('#txt').on('blur', function() {
                clearInterval(timer2);
                var index = 0;
                timer2 = setInterval(function() {
                    if(index>5) {
                        $('.share_box').find('.txt').removeClass('txt').addClass('con')
                        clearInterval(timer2);
                    }
                    index++;
                }, 50)
            })
        });



        $scope.agree=true;
        $scope.choose=function(){
            if($scope.agree){
                $scope.agree=false;
            }else{
                $scope.agree=true;
            }
        }
        $scope.maskShow=function(){
            $scope.maskStatus=true;
        }
        $scope.maskHide=function(){
            $scope.maskStatus=false;
        }
        //分享到朋友圈或者微信
       var loadShare = function($scope){
            GetConfig.save({}, function (data) {
                    var share = "http://s201.xiaork.com/titan/share#/haveRecord/"+$stateParams.sessionId;//最后url=41，openid,marketer
                    var timestamp;//时间戳
                    var nonceStr;//随机字符串
                    var signature;//得到的签名
                    var appid;//得到的签名
                BabyCoinInit.save({},function(data){
                    $scope.minename = data.babyCoinVo.nickName;
                    if($scope.minename == undefined || $scope.minename==''){
                        $scope.minename = '您的朋友';
                    }
                });
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
                                    ] // 功能列表
                                });
                                wx.ready(function () {
                                    // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                                    wx.onMenuShareTimeline({
                                        title: '我和可爱亲民的医生聊了会天， 解决了我的难题，你也来试试吧~', // 分享标题
                                        link: share+","+$scope.agree, // 分享链接
                                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                            success: function (res) {
                                                var content = document.getElementById("txt").value;
                                                if($scope.agree){
                                                    SharSeConsult.save({"sessionId":$stateParams.sessionId,"content":content},function (data) {
                                                        
                                                    });
                                                }
                                            
                                        },
                                        fail: function (res) {
                                        }
                                    });
                                    wx.onMenuShareAppMessage({
                                        title: $scope.minename  + '分享给你', // 分享标题
                                        desc: '和可爱亲民的医生聊了会天，解决了的难题~你也来试试吧',// 分享描述
                                        link: share+","+$scope.agree, // 分享链接
                                        imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/invite/patientConsultInvitePage.jpg', // 分享图标
                                        success: function (res) {
                                            var content = document.getElementById("txt").value;
                                            if($scope.agree){
                                                SharSeConsult.save({"sessionId":$stateParams.sessionId,"content":content},function (data) {

                                                });
                                            }
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
            });
        };
        loadShare($scope);
    }])
function lookLength(x){
    console.log(x)
    if(x.value.length>=50){
        alert('最多可输入50个字')
    }
}