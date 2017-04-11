var app = angular.module('controllers', ['ngFileUpload']).controller('NonTimeUserConversationCtrl', [
        '$scope','$state','$stateParams','$upload','ConversationInfo','UpdateReCode','$sce',
        function ($scope,$state,$stateParams,$upload,ConversationInfo,UpdateReCode,$sce) {

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


            $scope.glued = true;
            $scope.msgType= "text";
            $scope.content = "";
            $scope.info = [];
            $scope.info.content = "";
            $scope.messageList = [];

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
            }


            $scope.lookBig=function(item2,arr){
                wx.previewImage({
                    current: item2, // 当前显示图片的http链接
                    urls: arr // 需要预览的图片http链接列表
                });
            }
            $scope.NonTimeUserConversationInit = function(){
                $scope.doRefresh();
                $scope.getQQExpression();
                $scope.getQQExpression();
                ConversationInfo.save({sessionId:$stateParams.sessionId},function (data) {
                    if(data.state == "error"){
                        alert("请重新打开页面提交信息");
                    }
                    $scope.pageData = data;
                    $scope.mindPath = data.mindPath
                    for(var i=0;i<data.messageList.length;i++){
                        if("url" == data.messageList[i].messageType){
                            data.messageList[i].message = $sce.trustAsHtml(angular.copy(data.messageList[i].message));
                        }
                    }
                    $scope.messageList = $scope.pageData.messageList;
                    console.log(" $scope.messageList 数据显示", $scope.messageList);
                    console.log(" $scope.pageData.messageList; 数据显示", $scope.pageData.messageList);
                })
            };
            //发送消息
            $scope.sendMsg = function(messageType,content){
                if(content == ""){
                    return;
                }
                var information = {
                    "sessionId":$stateParams.sessionId,
                    "content": content,
                    "source": "user",
                    "msgType": messageType
                };
                UpdateReCode.save(information,function (data) {
                    if(data.state == "error"){
                        alert("请重新打开页面提交信息");
                    }
                    if(data.state == "success"){
                        $scope.messageList.push(data.conversationData);
                        $scope.info.content = "";
                    }
                });
            };
            //再次咨询
            $scope.againConsulting = function(){
                recordLogs("FSS_YHD_WTXQ_ZCZX");
                $state.go("NonTimeUserFirstConsult",{"doctorId":$scope.pageData.doctorId});
            };
            //送心意
            $scope.giveMind = function(){
                recordLogs("FSS_YHD_WTXQ_SXY");
                //$state.go("NonTimeUserFirstConsult",{"doctorId":$scope.pageData.doctorId});
                location.href = $scope.mindPath;
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

            //发送消息
            $scope.sendTextMsg = function(){
                $scope.info.content =  $('#saytext').val();
                if($scope.info.content.indexOf("http")>-1){
                    $scope.sendMsg("url",$scope.info.content);
                }else{
                    $scope.sendMsg("text",$scope.info.content);
                }

            };
            //发送表情
            $scope.getQQExpression = function () {
                $('#face').qqFace({
                    id: 'facebox',
                    assign: 'saytext',
                    path: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F'
                });
            };
        }]);
