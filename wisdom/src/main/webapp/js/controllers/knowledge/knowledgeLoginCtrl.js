angular.module('controllers', ['ionic']).controller('knowledgeLoginCtrl', [
        '$scope','$state','$stateParams','saveBabyEmr','$timeout','$cacheFactory','$http',
        function ($scope,$state,$stateParams,saveBabyEmr,$timeout,$cacheFactory,$http) {
            $scope.info = {};
            $scope.lock='false';
            $scope.defaultLock="true";
            $scope.flag = 0;
            $scope.result = "false";
            var img = document.getElementById('imghead');
            var imgId="";
            /*取消浮层*/
            $scope.cancel=function(){
                $scope.defaultLock="false";
            }

            $scope.$on('$ionicView.enter', function(){
                img = document.getElementById('imghead');
                img.src = "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Flogin_upload_photo.png";

                $scope.babysex = "1";//男

                $("#birthday").mobiscroll().date();
                var currYear = (new Date()).getFullYear();
                //初始化日期控件
                var opt = {
                    preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
                    theme: 'android-ics light', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
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
                    startYear:2006, //开始年份
                    endYear:currYear //结束年份
                    //endYear:2099 //结束年份
                };
                $("#birthday").mobiscroll(opt);
            });

            /**
             * 上传照片拍照或从手机相册中选图
             */
            $scope.chooseImage = function(){
                wx.chooseImage({
                    count: 1, // 默认9
                    sizeType: ['original','compressed'], // 可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: function (res) {
                        img.src = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                        $scope.flag = 1;
                        imgId = res.localIds[0];
                    }
                });
            }

            /**
             * 选择宝宝性别
             */
            $scope.chooseSex = function(sex){
                if(sex=="man"){
                    $scope.babysex = "1";
                }else{
                    $scope.babysex = "0";//女
                }
            }

            /**
             * 保存信息
             */
            $scope.save = function(){
                if($scope.info.name==undefined||$("#birthday").val()==""||$scope.info.name==""){
                    alert("请您输入宝宝的姓名和生日，谢谢！");
                }else{
                        $scope.result = "true";
                        if($scope.flag == 1){
                            //上传图片接口
                            wx.uploadImage({
                                localId:imgId, // 需要上传的图片的本地ID，由chooseImage接口获得
                                isShowProgressTips: 1, // 默认为1，显示进度提示
                                success: function (res) {

                                    //宝宝信息保存
                                    $scope.pageLoading=true;
                                    saveBabyEmr.save({"mediaId":res.serverId,"babyName":$scope.info.name,"gender":$scope.babysex,
                                        "babyBirthday":$("#birthday").val()},function(data){
                                        $scope.pageLoading=false;
                                        window.location.href ="ap/firstPage/knowledge?value=251314";
                                    });
                                }
                            });

                        }else{
                            //宝宝信息保存
                            $scope.pageLoading=true;
                            saveBabyEmr.save({"babyName":$scope.info.name,"gender":$scope.babysex,
                                "babyBirthday":$("#birthday").val()},function(data){
                                $scope.pageLoading=false;
                                window.location.href ="ap/firstPage/knowledge?value=251314";
                            });
                        }
                }
            }

            /**
             * 跳过登录信息
             */
            $scope.TiaoGuo = function(){
                //宝宝信息保存
                $scope.pageLoading=true;
                saveBabyEmr.save({"babyName":"宝宝名字","gender":"1",
                    "babyBirthday":"2011-12-12"},function(data){
                    $scope.pageLoading=false;
                    window.location.href ="ap/firstPage/knowledge?value=251314";
                });
            }


            $scope.doRefresh = function(){
                var timestamp;//时间戳
                var nonceStr;//随机字符串
                var signature;//得到的签名
                var appid;//得到的签名
                $.ajax({
                    url:"ap/wechatInfo/getConfig",// 跳转到 action
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
                                    'chooseImage',
                                    'previewImage',
                                    'uploadImage',
                                    'downloadImage'
                                ] // 功能列表
                            });
                            wx.ready(function () {
                            })
                        }else{
                        }
                    },
                    error : function() {
                    }
                });
            }
    }]);