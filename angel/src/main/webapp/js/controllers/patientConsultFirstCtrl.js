angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('patientConsultFirstCtrl', ['$scope','$location','$anchorScroll',
        'GetSessionId','GetUserLoginStatus','$upload','$sce',
        function ($scope,$location,$anchorScroll,GetSessionId,GetUserLoginStatus,$upload,$sce) {

            $scope.consultContent = [];
            $scope.info={};
            $scope.upFile = {};
            $scope.sessionId = "";
            $scope.socketServer = "";
            $scope.glued = true;//angular滚动条的插件预制参数，让对话滚动条，当新的聊天数据到达时，每次都定位底部

            $scope.openFileListFlag = false;
            $location.hash("fileInput");
            $anchorScroll();
            $scope.openFileList = function(){
                if($scope.openFileListFlag == true){
                    $scope.openFileListFlag = false;
                    $location.hash("fileInput");
                    $anchorScroll();
                }else{
                    $scope.openFileListFlag = true;
                    $location.hash("fileInputList");
                    $anchorScroll();
                }
            }

            $scope.patientConsultFirst = function(){
                var routePath = "/patient/consultBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    }else if(data.status=="20"){
                        $scope.patientId = data.userId;
                        $scope.patientName = data.userName;
                        $scope.patientPhone = data.userPhone;
                        $scope.initConsultSocket();
                    }
                })
            }

            $scope.sendItem = false;
            $scope.$watch('info.consultInputValue', function(newVal, oldVal) {
                console.log(newVal);
                if(newVal==undefined||newVal == ""){
                    $scope.sendItem = false;
                }else{
                    $scope.sendItem = true;
                }
            });
            //初始化接口
            $scope.initConsultSocket = function(){
                if (!window.WebSocket) {
                    window.WebSocket = window.MozWebSocket;
                }
                if (window.WebSocket) {
                    console.log($scope.patientId);
                    $scope.socketServer = new WebSocket("ws://192.168.191.2:2048/ws&user&"
                        + $scope.patientId +"&h5cxqm");//cs,user,distributor
                    $scope.socketServer.onmessage = function(event) {
                        var consultData = JSON.parse(event.data);
                        if(consultData.type==4){
                            processNotifyMessage(consultData);
                        }else{
                            filterMediaData(consultData);
                            processDoctorSendMessage(consultData);
                        }
                        $scope.apply();
                    };
                    $scope.socketServer.onopen = function(event) {
                        console.log("onopen"+event.data);
                        GetSessionId.get({"userId":$scope.patientId},function(data){
                            if(data.status=="0"){
                                $scope.sessionId = data.sessionId;
                            }else if(data.status=="1"){
                                $scope.sessionId = "";
                                var val = {
                                    "type": 4,
                                    "notifyType": "0000"
                                }
                                $scope.consultContent.push(val);
                            }
                        });
                    };
                    $scope.socketServer.onclose = function(event) {
                        console.log("onclose",event.data);
                    };
                } else {
                    alert("你的浏览器不支持！");
                }
            };
            //处理用户发送过来的消息
            var processDoctorSendMessage = function (conversationData) {
                var doctorValMessage = {
                    'type':conversationData.type,
                    'content':conversationData.content,
                    'dateTime':conversationData.dateTime,
                    'senderId':conversationData.senderId,
                    'senderName':conversationData.senderName,
                    'sessionId':conversationData.sessionId,
                    "avatar":"http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png"
            };
                $scope.consultContent.push(doctorValMessage);
            };
            var processNotifyMessage = function(notifyData){
                if(notifyData.notifyType=="1001"){
                    //有医生或者接诊员在线
                    console.log("有医生或者接诊员在线");
                } else if(notifyData.notifyType=="1002"){
                    //没有医生或者接诊员在线
                    console.log("有医生或者接诊员在线");
                }
            };
            //发送消息
            $scope.sendConsultContent = function(){
                var patientValMessage = {
                    "type": 0,
                    "content": $scope.info.consultInputValue,
                    "dateTime": moment().format("YYYY-MM-DD HH:mm:ss"),
                    "senderId":$scope.patientId,
                    "senderName":$scope.patientName,
                    "sessionId":$scope.sessionId,
                    "avatar":"http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png"
                };
                if (!window.WebSocket) {
                    return;
                }
                if ($scope.socketServer.readyState == WebSocket.OPEN) {
                    $scope.consultContent.push(patientValMessage);
                    $scope.socketServer.send(JSON.stringify(patientValMessage));
                    $scope.info.consultInputValue = "";
                } else {
                    alert("连接没有开启.");
                }
            };
            //提交图片
            $scope.uploadFiles = function($files,fileType) {
                var dataValue = {
                    "fileType": fileType,
                    "senderId": $scope.patientId,
                    "sessionId":$scope.sessionId
                };
                console.log(JSON.stringify(dataValue));
                var dataJsonValue = JSON.stringify(dataValue);
                for (var i = 0; i < $files.length; i++) {
                    var file = $files[i];
                    $scope.upload = $upload.upload({
                        url: 'consult/h5/uploadMediaFile',
                        data: encodeURI(dataJsonValue),
                        file: file
                    }).progress(function(evt) {

                        console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
                    }).success(function(data, status, headers, config){
                        var patientValMessage = {
                            "type": 1,
                            "content": data.showFile,
                            "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                            "senderId": $scope.patientId,
                            "senderName": $scope.senderName,
                            "sessionId":$scope.sessionId,
                            "avatar":"http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png"
                        };
                        console.log(patientValMessage.content);
                        if (!window.WebSocket) {
                            return;
                        }
                        if ($scope.socketServer.readyState == WebSocket.OPEN) {
                            $scope.consultContent.push(patientValMessage);
                            $scope.socketServer.send(JSON.stringify(patientValMessage));
                            $scope.info.consultInputValue = "";
                        } else {
                            alert("连接没有开启.");
                        }
                    });
                }
            };
            //过滤媒体数据
            var filterMediaData = function (val) {
                if(val.senderId==$scope.patientId){
                    if (val.type == "0") {
                    }
                }else{
                    if (val.type == "2"||val.type == "3") {
                    }else if(val.type == "0"){
                    }
                }
            };
            //各个子窗口的开关变量
            $scope.showFlag = {
                magnifyImg:false
            };
            $scope.tapImgButton = function (key,value) {
                $scope.showFlag[key] = !$scope.showFlag[key];
                $scope.imageSrc = value;
            };
            //公共点击按钮，用来触发弹出对应的子窗口
            $scope.tapShowButton = function(type){
                $scope.showFlag[key] = !$scope.showFlag[key];
            };
        }])