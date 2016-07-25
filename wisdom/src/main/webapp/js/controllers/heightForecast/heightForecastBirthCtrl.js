angular.module('controllers', ['ionic']).controller('heightForecastBirthCtrl', [
    '$scope','$state','SaveHeightPredictionInfo','$ionicScrollDelegate',
    function ($scope,$state,SaveHeightPredictionInfo,$ionicScrollDelegate) {
        $scope.title ="宝妈爱心接力";
        $scope.info = {
            dadHeight:'',
            mamHeight:''
        };
        $scope.babyDes = '哇塞，我家宝宝居然能长这么高？据说99.8%精准哦！';
        $scope.babyHeight = '';
        $scope.lookResultFloat = false;
        $scope.babyBirthdaySelected = false;
        $scope.dadBirthdaySelected = false;
        $scope.mamBirthdaySelected = false;
        $scope.babyAgeList = [{age:'（1）足月，约9个月'},{age:'（2）不足，约8个月'},{age:'（3）不足，约7个月'},{age:'（4）不足，约6个月'},
            {age:'（5）不足6个月'}];
        $scope.numberB = Math.ceil(Math.random()*5);//随机数
        $scope.numberG = Math.ceil(Math.random()*3);//随机数男宝
        $scope.$on('$ionicView.enter', function(){
            loadShare();
            var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
            $("#babyBirthday").mobiscroll().date();
            $("#dadBirthday").mobiscroll().date();
            $("#mamBirthday").mobiscroll().date();
            //初始化日期控件
            var opt = {
                preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
                theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
                display: 'modal', //显示方式 ，可选：modal\inline\bubble\top\bottom
                mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
                lang:'zh',
                dateFormat: 'yyyy-mm-dd', // 日期格式
                setText: '确定', //确认按钮名称
                cancelText: '取消',//取消按钮名籍我
                dateOrder: 'yyyymmdd', //面板中日期排列格式
                dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
                showNow: false,
                nowText: "今",
                minDate: new Date(1960,date.substring(5,7)-1,date.substring(8,10)),
                maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10)),
                onSelect: function (valueText) {
                    console.log("dd",valueText);
                }
            };
            $("#babyBirthday").mobiscroll(opt);
            $("#dadBirthday").mobiscroll(opt);
            $("#mamBirthday").mobiscroll(opt);

        });
        //出生日期
        $scope.showInput = function (index) {
            if(index=="babyBirthday"){
                $("#babyBirthday").mobiscroll('show');
                $scope.babyBirthdaySelected = true;
            }
            if(index=="dadBirthday"){
                $("#dadBirthday").mobiscroll('show');
                $scope.dadBirthdaySelected = true;
            }
            if(index=="mamBirthday"){
                $("#mamBirthday").mobiscroll('show');
                $scope.mamBirthdaySelected = true;
            }
        };
        //选择孩子性别
        $scope.selectSex = function(sex){
            $scope.sexItem=sex;
            if($scope.sexItem == 0){
                $scope.isSelectedB = true;
                $scope.isSelectedG = false;
            }
            if($scope.sexItem == 1){
                $scope.isSelectedG = true;
                $scope.isSelectedB = false;
            }
        };
        //选择孩子性别
        $scope.selectMon = function(index){
            console.log(index);
            $scope.babyAge = $scope.babyAgeList[index];
            $scope.isSelected = index;

        };
        //判断问题是否为空
        $scope.checkName = function () {
            if($("#babyBirthday").val()==""){
                alert("宝宝生日不能为空！");
                return;
            }
            if($("#dadBirthday").val()==""){
                alert("宝爸生日不能为空！");
                return;
            }
            if($scope.info.dadHeight==""||$scope.info.dadHeight > 300||$scope.info.dadHeight < 100){
                alert("请重新输入宝爸的身高！");
                return;
            }
            if($scope.info.mamHeight==""||$scope.info.mamHeight > 300||$scope.info.mamHeight <100 ){
                alert("请重新输入宝妈的身高！");
                return;
            }
            if($scope.sexItem == 0){
                $scope.resultBoy = (parseInt($scope.info.dadHeight) + parseInt($scope.info.mamHeight) + 13) / 2 + $scope.numberB;
                $scope.resultGirl = 0;
            }
            if($scope.sexItem == 1){
                $scope.resultGirl = (parseInt($scope.info.dadHeight)+ parseInt($scope.info.mamHeight) - 13) / 2 + $scope.numberG;
                $scope.resultBoy = 0;
            }

            if($scope.resultGirl == 0 && $scope.resultBoy != ''){
                $scope.babyHeight = $scope.resultBoy;
                if($scope.babyHeight < 170){
                    $scope.babyDes = '我家男宝的身高居然和何炅一样'+$scope.babyHeight+'，文质彬彬都说帅，你也来测下！';
                }else if($scope.babyHeight < 175){
                    $scope.babyDes = '我家男宝的身高居然和权志龙一样'+$scope.babyHeight+'，气宇轩昂人人夸，你也来测下！';
                }else if($scope.babyHeight < 180){
                    $scope.babyDes = '我家男宝的身高居然和宋仲基一样'+$scope.babyHeight+'，玉树临风全都爱，你也来测下！';
                }else if($scope.babyHeight < 185){
                    $scope.babyDes = '我家男宝的身高居然和吴彦祖一样'+$scope.babyHeight+'，长腿欧巴超羡慕，你也来测下！';
                }else if($scope.babyHeight < 190){
                    $scope.babyDes = '我家男宝的身高居然和吴亦凡一样'+$scope.babyHeight+'，顶天立地大气概，你也来测下！';
                }else{
                    $scope.babyDes = '我家男宝的身高居然和易建联一样'+$scope.babyHeight+'，篮球飞人绝对帅，你也来测下！';
                }
            }
            if($scope.resultGirl != '' && $scope.resultBoy == 0){
                if($scope.babyHeight < 160){
                    $scope.babyDes = '我家女宝的身高居然和蔡依林一样'+$scope.babyHeight+'，小家碧玉惹人爱，你也来测下！';
                }else if($scope.babyHeight < 165){
                    $scope.babyDes = '我家女宝的身高居然和孙俪一样'+$scope.babyHeight+'，大家闺秀人人爱，你也来测下！';
                }else if($scope.babyHeight < 170){
                    $scope.babyDes = '我家女宝的身高居然和杨幂一样'+$scope.babyHeight+'，窈窕淑女君好逑，你也来测下！';
                }else{
                    $scope.babyDes = '我家女宝的身高居然和林志玲一样'+$scope.babyHeight+'，维密超模大赢家，你也来测下！';
                }
            }
            loadShare();
            $ionicScrollDelegate.scrollTop();
            $scope.lookResultFloat = true;
            SaveHeightPredictionInfo.save({
                sexItem:$scope.sexItem,
                babyBirthday:$("#babyBirthday").val(),
                dadBirthday:$("#dadBirthday").val(),
                mamBirthday:$("#mamBirthday").val(),
                dadHeight:$scope.info.dadHeight,
                mamHeight:$scope.info.mamHeight,
                resultGirl:$scope.resultGirl,
                resultBoy:$scope.resultBoy,
                babyAge:$scope.babyAge
            }, function (data) {
            });
            //$state.go("heightForecastResult",{resultBoy:$scope.resultBoy,resultGirl:$scope.resultGirl});
            recordLogs('YYHD_SG_YCS_WYKJG');
        };
        //取消浮层
        $scope.cancelFloat = function () {
            $scope.lookResultFloat = false;
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
                                    $state.go("heightForecastResult",{resultBoy:$scope.resultBoy,resultGirl:$scope.resultGirl});
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
                                    $state.go("heightForecastResult",{resultBoy:$scope.resultBoy,resultGirl:$scope.resultGirl});
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

