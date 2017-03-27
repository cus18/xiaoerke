function getHour(time){
    if(time==null||time==''){
        return '';
    }else{
        var date=new Date(parseInt(time));
        var dYear = date.getFullYear();
        var dMonth = date.getMonth()+1;
        var dDate = date.getDate();
        var dHours = date.getHours();
        var dMinutes = date.getMinutes();
        var dSeconds = date.getSeconds();
        if(dMonth<10){
            dMonth='0'+dMonth
        };
        if(dDate<10){
            dDate='0'+dDate
        };
        if(dHours<10){
            dHours='0'+dHours
        };
        if(dMinutes<10){
            dMinutes='0'+dMinutes
        };
        if(dSeconds<10){
            dSeconds='0'+dSeconds
        };
        return dHours + ':' + dMinutes
    }
}

angular.module('controllers', ['ionic','ngDialog']).controller('signHomeCtrl', [
    '$scope','$state','$stateParams',
    'OrderPayMemberServiceOperation','GetUserMemberService','$ionicScrollDelegate','$location','ngDialog','GetPunchCardPage','TakePunchCardActivity','PayPunchCardCash','GetConfig','FindPunchCardBySelf','GetPunchCardRewards',
    function ($scope,$state,$stateParams,OrderPayMemberServiceOperation,GetUserMemberService,$ionicScrollDelegate,$location,ngDialog,GetPunchCardPage,TakePunchCardActivity,PayPunchCardCash,GetConfig,FindPunchCardBySelf,GetPunchCardRewards) {

        //参加打卡按钮的状态
        $scope.goJoinStatus=false;
        //我要打卡按钮的状态
        $scope.goSignStatus=false;



        //启动状态判断
        $scope.start_status=false;
        //支付状态判断
        $scope.pay_status=false;
        //打卡时间提示状态
        $scope.time_status=false;
        //打卡成功提示状态
        $scope.sign_status=false;
        //分享弹窗状态
        $scope.share_status=false;

        if($stateParams.id=='false'){
            $scope.pay_status=false;
        }else if($stateParams.id=='true'){
            $scope.pay_status=true;
            //参加打卡按钮的状态
            $scope.goJoinStatus=false;
            //我要打卡按钮的状态
            $scope.goSignStatus=true;
        }
        //加入的点击事件
        $scope.goJoin=function(){
            $ionicScrollDelegate.scrollTop();
            $scope.start_status=true;
        }
        //支付按钮点击事件
        $scope.goPay=function(){
            $ionicScrollDelegate.scrollTop();
            window.location.href='http://s201.xiaork.com/keeper/wxPay/patientPay.do?serviceType=payPunchCardCash';
        }
        //喊朋友一起来参加
        $scope.goShare=function(){
            $ionicScrollDelegate.scrollTop();
            $scope.share_status=true;
            //启动状态判断
            $scope.start_status=false;
            //支付状态判断
            $scope.pay_status=false;
            //打卡时间提示状态
            $scope.time_status=false;
            //打卡成功提示状态
            $scope.sign_status=false;
        }
        //关闭分享弹窗
        $scope.close_share=function(){
            $scope.share_status=false;
        }

        //我要打卡
        $scope.goSign=function(){
            TakePunchCardActivity.save({openId:$scope.openId},function(res){
                if(res.status=="failure"){
                    $ionicScrollDelegate.scrollTop();
                    $scope.time_status=true;
                }else{
                    $ionicScrollDelegate.scrollTop();
                    $scope.sign_status=true;
                    $scope.goJoinStatus=true;
                    $scope.goSignStatus=false;
                }
            })
        }
        //关闭按钮
        $scope.close=function(){
            //启动状态判断
            $scope.start_status=false;
            //支付状态判断
            $scope.pay_status=false;
            //打卡时间提示状态
            $scope.time_status=false;
            //打卡成功提示状态
            $scope.sign_status=false;
        }

        //查看更多记录
        $scope.lookMore=function(){
            GetPunchCardRewards.save({pageNo:$scope.pageNum++,pageSize:10},function(res){
                $scope.dataList=$scope.dataList.concat(res.personRewardsList);
                for(var i=0;i<$scope.dataList.length;i++){
                    $scope.dataList[i].hour=getHour($scope.dataList[i].updateTime);
                    if($scope.dataList[i].headImgUrl==''||$scope.dataList[i].headImgUrl==null){
                        $scope.dataList[i].headImgUrl='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf/consult/yonghumoren.png';
                    }
                }
            })
        }

        //获取本页初始化数据
        GetPunchCardPage.save({pageNo:0,pageSize:10},function(res){
            if(res.resultCode==9999){
                alert('服务器错误')
            }else{
                if(res.headImgUrl==''||res.headImgUrl==null){
                    $scope.headImgUrl='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf/consult/yonghumoren.png';
                }else{
                    $scope.headImgUrl=res.headImgUrl;
                }
                //判断数据
                if(res.punchCardSwitch=='on'){
                    $scope.pageNum=0;
                }else if(res.punchCardSwitch=='off'){
                    $scope.pageNum=1;
                }
                $scope.oData=res;
                $scope.openId=res.openId;
                $scope.market=res.marketer;
                $scope.minename=res.nickName;
                $scope.dataList=res.personRewardsList;
                for(var i=0;i<$scope.dataList.length;i++){
                    $scope.dataList[i].hour=getHour($scope.dataList[i].updateTime);
                    if($scope.dataList[i].headImgUrl==''||$scope.dataList[i].headImgUrl==null){
                        $scope.dataList[i].headImgUrl='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf/consult/yonghumoren.png';
                    }
                }
                if(res.isOrNotPay=='no'){
                    $scope.goJoinStatus=true;
                    $scope.goSignStatus=false;
                }else if(res.isOrNotPay=='yes'){
                    $scope.goJoinStatus=false;
                    $scope.goSignStatus=true;
                }
                //前往个人中心
                $scope.goCenter=function(){
                    $state.go('signRecord',{openId:$scope.openId})
                }
                $scope.doRefresh();
            }
        })
        //初始化微信
         $scope.doRefresh = function () {
            GetConfig.save({}, function (data) {
                $scope.inviteUrlData = data.publicSystemInfo.redPackageShareUrl;
                var share = $scope.inviteUrlData + $scope.openId + "," + $scope.market + ",";//最后url=41，openid,marketer

                // var share = $scope.inviteUrlData + $scope.openid+","+$scope.marketer+","+ $scope.uuid+",";//最后url=41，openid,marketer
                // if(version=="a"){
                version = "a";
                var timestamp;//时间戳
                var nonceStr;//随机字符串
                var signature;//得到的签名
                var appid;//得到的签名
                $.ajax({
                    url: "wechatInfo/getConfig",// 跳转到 action
                    async: true,
                    type: 'get',
                    data: {url: location.href.split('#')[0]},//得到需要分享页面的url
                    cache: false,
                    dataType: 'json',
                    success: function (data) {
                        if (data != null) {
                            timestamp = data.timestamp;//得到时间戳
                            nonceStr = data.nonceStr;//得到随机字符串
                            signature = data.signature;//得到签名
                            appid = data.appid;//appid
                            //微信配置
                            wx.config({
                                debug: false,
                                appId: appid,
                                timestamp: timestamp,
                                nonceStr: nonceStr,
                                signature: signature,
                                jsApiList: [
                                    'onMenuShareTimeline',
                                    'onMenuShareAppMessage',
                                    'previewImage'
                                ] // 功能列表
                            });
                            wx.ready(function () {
                                // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                                wx.onMenuShareTimeline({
                                    title: '早起挑战，大波现金等你来领走～在这里可以免费咨询三甲医院儿科专家', // 分享标题
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appSign/111.png', // 分享图标
                                    success: function (res) {
                                        //recordLogs("ZXYQ_YQY_SHARE");
                                        // redPacketCreate.save({"uuid":$scope.uuid},function (data) {
                                        // });
                                    },
                                    fail: function (res) {

                                    }
                                });
                                wx.onMenuShareAppMessage({
                                    title: $scope.minename + '向你推荐', // 分享标题
                                    desc: '早起挑战，大波现金等你来领走～在这里可以免费咨询三甲医院儿科专家 ',// 分享描述
                                    link: share, // 分享链接
                                    imgUrl: 'http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appSign/111.png', // 分享图标
                                    success: function (res) {
                                    },
                                    fail: function (res) {
                                    }
                                });
                            })
                        } else {
                        }
                    },
                    error: function () {
                    }
                });

            });
        }
    }])