angular.module('controllers', ['ngFileUpload']).controller('NonTimeDoctorConversationCtrl', [
    '$scope', '$state', '$stateParams', '$timeout', '$http', '$upload', 'ConversationDoctorInfo', 'GetDoctorLoginStatus', 'UpdateReCode','GetConfig',
    function ($scope, $state, $stateParams, $timeout, $http, $upload, ConversationDoctorInfo, GetDoctorLoginStatus, UpdateReCode,GetConfig) {
        $scope.info = {};
        $scope.info.content = "";
        $scope.msgType = "text";
        $scope.sendLock = false;
        var picList=[];
        // $scope.previewImage=function(curSrc){
        //     wx.previewImage({
        //         current:curSrc, // 当前显示图片的http链接
        //         urls: picList // 需要预览的图片http链接列表
        //     });
        // };
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
        }

        //添加表情
        $scope.getQQExpression = function () {
            $('#face').qqFace({
                id: 'facebox', //表情盒子的ID
                assign: 'saytext', //给那个控件赋值
                path: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F'	//表情存放的路径
            });
        };


        //提交图片
        $scope.uploadFiles = function() {
            wx.chooseImage({
                count: 1, // 默认9
                sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                success: function (res) {
                    var localIds = res.localIds[0]; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                    $scope.$digest(); // 通知视图模型的变化
                    wx.uploadImage({
                        localId: localIds, // 需要上传的图片的本地ID，由chooseImage接口获得
                        isShowProgressTips: 1, // 默认为1，显示进度提示
                        success: function (res) {
                            var serverId = res.serverId; // 返回图片的服务器端ID
                            $scope.sendMsg("img",serverId);
                        }
                    });
                }
            });
        };

        //发送文本消息
        $scope.sendTextMsg = function(){
            $scope.info.content =  $('#saytext').val();
            $scope.sendMsg("text",$scope.info.content);
        };
        //发送消息
        $scope.sendMsg = function (messageType, content) {
            if(content == ""){
                return;
            }
            $scope.sendLock = true;
            $timeout(function () {
                $scope.sendLock = false;
                $scope.$digest(); // 通知视图模型的变化
            }, 2000);
            var information = {
                "sessionId": $stateParams.sessionId,
                "content": content,
                "msgType": messageType,
                "source": "doctor",
                "doctorId": $scope.doctorId
            };
            UpdateReCode.save(information, function (data) {
                if (data.state == "error") {
                    alert("请重新打开页面提交信息");
                }
                if (data.state == "success") {
                    console.log("更新会话信息", data);
                    $scope.messageList.push(data.conversationData);
                    $scope.info.content = "";
                }
            });
        };


        //页面初始化
        $scope.NonTimeDoctorConversationInit = function () {
            $scope.doRefresh();
            $scope.glued = true;
            //校验是否登陆
            GetDoctorLoginStatus.save({}, function (data) {
                $scope.pageLoading = false;
                if (data.status == "failure") {
                    GetConfig.save({}, function (data) {
                        window.location.href = data.publicSystemInfo.nonRealtimeLoginUrl;
                    })
                } else if (data.status == "success") {
                    ConversationDoctorInfo.save({
                        sessionId: $stateParams.sessionId,
                        doctorId: $scope.doctorId
                    }, function (data) {
                        console.log("会话信息列表", data)
                        $scope.pageData = data;
                        $scope.messageList = data.messageList;
                        $scope.doctorId = $scope.pageData.doctorId;
                        $('.pic-talk').each(function(){
                            picList.push($(this).attr('src'));
                        });
                        console.log("点击要放大聊天图片",picList);
                    })
                } else {
                    alert("非系统咨询医生，请联系接诊员！");
                }
            })
        }
    }]);
