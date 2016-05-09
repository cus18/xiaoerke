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
                        console.log("consultData"+event.data);
                        var consultData = JSON.parse(event.data);
                        console.log("consultData"+JSON.parse(event.data));
                        filterMediaData(consultData);
                        processDoctorSendMessage(consultData);
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
                        console.log("onclose"+event.data);
                    };
                } else {
                    alert("你的浏览器不支持！");
                }
            };
            var processDoctorSendMessage = function (conversationData) {
                var currentConsultValue = {
                    'type':conversationData.type,
                    'content':conversationData.content,
                    'dateTime':conversationData.dateTime,
                    'senderId':conversationData.senderId,
                    'senderName':conversationData.senderName,
                    'sessionId':conversationData.sessionId
                };
                if(JSON.stringify($scope.consultContent)=='{}'){
                    $scope.consultContent = {
                        'doctorId':conversationData.senderId,
                        'source':conversationData.source,
                        'fromServer':conversationData.fromServer,
                        'sessionId':conversationData.sessionId,
                        'isOnline':true,
                        'dateTime':conversationData.dateTime,
                        'messageNotSee':conversationData.messageNotSee,
                        'patientName':conversationData.patientName,
                        'consultValue':[]
                    };
                $scope.consultContent.consultValue.push(currentConsultValue);
                console.log($scope.currentUserConversation);
/*
                    $scope.consultContent.patientId = conversationData.senderId;
                    $scope.consultContent.source = conversationData.source;
                    $scope.consultContent.fromServer = conversationData.fromServer;
                    $scope.consultContent.sessionId = conversationData.sessionId;
                    $scope.consultContent.isOnline = true;
                    $scope.consultContent.dateTime = conversationData.dateTime;
                    $scope.consultContent.messageNotSee = false;
                    $scope.consultContent.patientName = conversationData.senderName;
                    $scope.consultContent.consultValue = [];
                    $scope.consultContent.consultValue.push(currentConsultValue);*/
                }
            };
            $scope.sendConsultContent = function(){
                var patientValMessage = {
                    "type": 0,
                    "content": $scope.info.consultInputValue,
                    "dateTime": moment().format("YYYY-MM-DD HH:mm:ss"),
                    "senderId":$scope.patientId,
                    "senderName":$scope.patientName,
                    "sessionId":$scope.sessionId,
                    "avatar":"http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/jialisan01.png"
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
                    "senderId": $scope.patientId
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
                        console.log(data);
                        var patientValMessage = {
                            "type": 1,
                            "content": data.showFile,
                            "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                            "senderId": data.senderId,
                            "senderName": data.senderName,
                            "sessionId":data.sessionId
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
                        val.content = $sce.trustAsHtml(replace_em(angular.copy(val.content)));
                    }
                }else{
                    if (val.type == "2"||val.type == "3") {
                        val.content = $sce.trustAsResourceUrl(angular.copy(val.content));
                    }else if(val.type == "0"){
                        val.content = $sce.trustAsHtml(replace_em(angular.copy(val.content)));
                    }
                }
            };
        }])