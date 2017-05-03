angular.module('controllers', ['ngFileUpload']).controller('NonTimeUserFirstConsultCtrl', [
        '$scope','$upload','$state','$stateParams','BabyBaseInfo','CreateSession','GetConsultDoctorHomepageInfo',
    function ($scope,$upload,$state,$stateParams,BabyBaseInfo,CreateSession,GetConsultDoctorHomepageInfo) {
            $scope.nonRealPayPrice="6.6";//doctor price;
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
            }
            recordLogs("FSS_YHD_TWY");
            //初始化数据
            $scope.info = {
                describeIllness:""
            };
            $scope.showPhotoList = [];
            $scope.photoList = [];
            $scope.sexItem = 0;
            $scope.isSelectedB = true;
            $scope.isSelectedG = false;
            $scope.babyId = "";
            //微信js-sdk 初始化接口
            $scope.doRefresh = function(){
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
                                    'previewImage',
                                    'chooseImage',
                                    'uploadImage'
                                ] // 功能列表
                            });
                            wx.ready(function () {
                            })
                        }
                    },
                    error : function() {
                    }
                });
            };

            //页面数据请求
            $scope.NonTimeUserFirstConsultInit = function(){
                $scope.doRefresh();
                // 获取宝宝基本信息
                BabyBaseInfo.save({},function (data) {
                    $scope.babyId = data.babyId+"";
                    if(data.status == "error"){
                       alert (data.msg);
                        return;
                    }
                    if(data.babySex != ""){
                        if("undefined" != data.babySex){
                            $scope.sexItem = data.babySex;
                        }
                        if ($scope.sexItem == 0) {
                            $scope.isSelectedG = true;
                            $scope.isSelectedB = false;
                        }
                        if ($scope.sexItem == 1) {
                            $scope.isSelectedB = true;
                            $scope.isSelectedG = false;
                        }
                    }

                    if(data.babyBirthDay != ""){
                        $("#babyBirthday").val(data.babyBirthDay)
                    }
                    })
            };
            $scope.deletePhoto = function(index){
                $scope.photoList.splice(index, 1);
                $scope.showPhotoList.splice(index, 1);
            };
            $scope.selectSex = function(sex) {
                $scope.sexItem = sex;
                $scope.babyId = "";
                if ($scope.sexItem == 0) {
                    $scope.isSelectedG = true;
                    $scope.isSelectedB = false;
                }
                if ($scope.sexItem == 1) {
                    $scope.isSelectedB = true;
                    $scope.isSelectedG = false;
                }
            };

            $scope.showInput = function() {
                $scope.babyId = "";
                var date = new Date(+new Date() + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '');
                $("#babyBirthday").mobiscroll().date();
                //初始化日期控件
                var opt = {
                    preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
                    theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
                    display: 'modal', //显示方式 ，可选：modal\inline\bubble\top\bottom
                    mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
                    lang: 'zh',
                    dateFormat: 'yyyy-mm-dd', // 日期格式
                    setText: '确定', //确认按钮名称
                    cancelText: '取消', //取消按钮名籍我
                    dateOrder: 'yyyymmdd', //面板中日期排列格式
                    dayText: '日',
                    monthText: '月',
                    yearText: '年', //面板中年月日文字
                    showNow: false,
                    nowText: "今",
                    startYear: 1960, //开始年份
                    //endYear:currYear //结束年份
                    /*minDate: new Date(2002,date.substring(5,7)-1,date.substring(8,10)),*/
                    maxDate: new Date(date.substring(0, 4), date.substring(5, 7) - 1, date.substring(8, 10))
                    //endYear:2099 //结束年份
                };
                $("#babyBirthday").mobiscroll(opt);
                $("#babyBirthday").mobiscroll("show");
            };

            //提交图片
            $scope.uploadFiles = function() {
                wx.chooseImage({
                    count: 1, // 默认9
                    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: function (res) {
                        var localIds = res.localIds[0]; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                        $scope.showPhotoList.push(localIds);
                        $scope.$digest(); // 通知视图模型的变化
                        wx.uploadImage({
                            localId: localIds, // 需要上传的图片的本地ID，由chooseImage接口获得
                            isShowProgressTips: 1, // 默认为1，显示进度提示
                            success: function (res) {
                                var serverId = res.serverId; // 返回图片的服务器端ID
                                $scope.photoList.push(serverId)
                            }
                        });
                    }
                });
            };


            $scope.submit = function(){
                var information = {
                    "csUserId":$stateParams.doctorId,
                    "sex": $scope.sexItem+"",
                    "birthday": $("#babyBirthday").val(),
                    "describeIllness": $scope.info.describeIllness,
                    "babyId": $scope.babyId+"",
                    "imgList":$scope.photoList
                };

                if(information.birthday == ""){
                    alert("请输入宝宝生日");
                    return;
                }
                CreateSession.save(information,function (data) {
                    if(data.status == "error"){
                        alert (data.msg);
                        return;
                    }
                    recordLogs("FSS_YHD_TWY_TW");
                    //$state.go("NonTimeUserConversation",{"sessionId":data.sessionId})
                    /*location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=51,"+data.sessionId;*/
                    location.href = "http://s201.xiaork.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s201.xiaork.com/keeper/wechatInfo/getUserWechatMenId?url=51,"+data.sessionId+","+$scope.nonRealPayPrice;
                })
            };
            $scope.$on('$ionicView.beforeEnter',function() {
                console.log("医生数据信息1111");
                //获取医生图文咨询的价格
               /* GetConsultDoctorHomepageInfo.get({"userId":$stateParams.doctorId},function (data) {
                    console.log("医生数据信息", data);
                    $scope.nonRealPayPrice = data.nonRealPayPrice;//医生价格
                    /!*$scope.nonRealPayPrice=$stateParams.nonRealPayPrice;// get doctor price;*!/
                });*/
            });
    }]);
