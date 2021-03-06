angular.module('controllers', ['ionic']).controller('heightForecastResultCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {
        $scope.heightImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fbiaoti_bb_png_03.png';
        $scope.babyImg = '';
        $scope.starImg = '';
        $scope.babyHeight = '';
        $scope.boyHeight = '';
        $scope.girlHeight = '';
        $scope.boyImg = '';
        $scope.girlImg = '';
        $scope.babyDes = '';
        $scope.showDouble = false;
        $scope.showOne = false;
        $scope.$on('$ionicView.enter', function(){
            if($stateParams.resultGirl != 0 && $stateParams.resultBoy != 0){
                $scope.heightImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fbiaoti_bb_png_03.png';
                $scope.showDouble = true;
                $scope.babyDes = '哇塞，我家宝宝居然能长这么高？据说99.8%精准哦！';
                $scope.boyHeight = $stateParams.resultBoy ;
                $scope.girlHeight = $stateParams.resultGirl;
                if($scope.boyHeight < 170){
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimages_w_png_03.png';
                }else if($scope.boyHeight < 175){
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_q_png_03.png';
                }else if($scope.boyHeight < 180){
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_y_png_03.png';
                }else if($scope.boyHeight < 185){
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_c_png_03.png';
                }else if($scope.boyHeight < 190){
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_d_png_03.png';
                }else{
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_l_png_03.png';
                }

                if($scope.girlHeight < 160){
                    $scope.girlImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_x_png_03.png';
                }else if($scope.girlHeight < 165){
                    $scope.girlImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_dajiaguixiu_png_03_03.png';
                }else if($scope.girlHeight < 170){
                    $scope.girlImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimages_png_03.png';
                }else{
                    $scope.girlImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_chaoji_03.png';
                }
            }
            if($stateParams.resultGirl == 0 && $stateParams.resultBoy != ''){
                $scope.heightImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fbiaoti_nan_png_03.png';
                $scope.showOne = true;
                $scope.babyHeight = $stateParams.resultBoy;
                if($scope.babyHeight < 170){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimages_w_png_02.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_hejiong_png_03.png';
                    $scope.babyDes = '我家男宝的身高居然和何炅差不多'+$scope.babyHeight+'，文质彬彬都说帅，你也来测下！';
                }else if($scope.babyHeight < 175){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_q_png_02.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_liangchaowei_png_03.png';
                    $scope.babyDes = '我家男宝的身高居然和权志龙差不多'+$scope.babyHeight+'，气宇轩昂人人夸，你也来测下！';
                }else if($scope.babyHeight < 180){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_y_png_02.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_songzhongji_png_03.png';
                    $scope.babyDes = '我家男宝的身高居然和宋仲基差不多'+$scope.babyHeight+'，玉树临风全都爱，你也来测下！';
                }else if($scope.babyHeight < 185){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_c_png_02.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_wuyanzu_png_03.png';
                    $scope.babyDes = '我家男宝的身高居然和吴彦祖差不多'+$scope.babyHeight+'，长腿欧巴超羡慕，你也来测下！';
                }else if($scope.babyHeight < 190){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_d_png_02.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_wuyifan_png_03.png';
                    $scope.babyDes = '我家男宝的身高居然和吴亦凡差不多'+$scope.babyHeight+'，顶天立地大气概，你也来测下！';
                }else{
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_l_png_02.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_yijianlian_png_03.png';
                    $scope.babyDes = '我家男宝的身高居然和易建联差不多'+$scope.babyHeight+'，篮球飞人绝对帅，你也来测下！';
                }
            }
            if($stateParams.resultGirl != '' && $stateParams.resultBoy == 0){
                $scope.heightImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fbwlsg_png_03.png';
                $scope.showOne = true;
                $scope.babyHeight = $stateParams.resultGirl;
                if($scope.babyHeight < 160){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_x_png_02.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_ciayilin_png_03.png';
                    $scope.babyDes = '我家女宝的身高居然和蔡依林差不多'+$scope.babyHeight+'，小家碧玉惹人爱，你也来测下！';
                }else if($scope.babyHeight < 165){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_dajiaguixiu_png_03_02.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_sunli_png_03.png';
                    $scope.babyDes = '我家女宝的身高居然和孙俪差不多'+$scope.babyHeight+'，大家闺秀人人爱，你也来测下！';
                }else if($scope.babyHeight < 170){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimages_png_02.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_yangmi_png_03.png';
                    $scope.babyDes = '我家女宝的身高居然和杨幂差不多'+$scope.babyHeight+'，窈窕淑女君好逑，你也来测下！';
                }else{
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_chaoji_02.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_lin_png_03.png';
                    $scope.babyDes = '我家女宝的身高居然和林志玲差不多'+$scope.babyHeight+'，维密超模大赢家，你也来测下！';
                }
            }
            loadShare();
        });

        $scope.goArticle =function(){
            recordLogs("YYHD_SG_WZLJ");
            window.location.href="http://mp.weixin.qq.com/s?__biz=MzIxMTE2MDAxOA==&mid=2653329561&idx=1&sn=a1c88ef5b0ff719d85e703e2182254ed&scene=1&srcid=0722WM6fGB2ruI9zsWDS2BCL#rd#wechat_redirect"
        };

        //分享到朋友圈或者微信
        var loadShare = function(){
            var share = 'http://s165.baodf.com/wisdom/firstPage/heightForecast';
            version="a";
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
                                title: $scope.babyDes, // 分享标题
                                link: share, // 分享链接
                                imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2FheightForecast.png', // 分享图标
                                success: function (res) {
                                    recordLogs("YYHD_SG_FXPYQ");
                                },
                                fail: function (res) {
                                }
                            });
                            wx.onMenuShareAppMessage({
                                title: '想知道宝宝能长多高，做个测试就知道', // 分享标题
                                desc: $scope.babyDes, // 分享描述
                                link:share, // 分享链接
                                imgUrl: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2FheightForecast.png', // 分享图标
                                success: function (res) {
                                    recordLogs("YYHD_SG_FXPP");
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
        };
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
    }]);

