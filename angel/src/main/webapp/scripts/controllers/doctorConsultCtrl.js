angular.module('controllers', ['luegg.directives'])
    .controller('doctorConsultFirstCtrl', ['$scope', '$sce', '$window','GetTodayRankingList',
        'GetOnlineDoctorList','GetAnswerValueList','GetAnswerValueList','GetUserLoginStatus',
        '$location', 'GetCurrentUserHistoryRecord','GetMyAnswerModify','GetCurrentUserConsultListInfo',
        'TransferToOtherCsUser','SessionEnd','GetWaitJoinList','React2Transfer','CancelTransfer','$upload',
        function ($scope, $sce, $window,GetTodayRankingList, GetOnlineDoctorList, GetAnswerValueList,
                  GetAnswerValueList, GetUserLoginStatus, $location, GetCurrentUserHistoryRecord,GetMyAnswerModify,
                  GetCurrentUserConsultListInfo,TransferToOtherCsUser,SessionEnd,GetWaitJoinList,React2Transfer,CancelTransfer,$upload) {
            $scope.info = {
                effect:"true",
                transferRemark:"",
                searchCsUserValue:"",
                role:{
                    "distributor":"接诊员",
                    "consultDoctor":"专业医生"
                }
            }; //初始化info参数
            $scope.socketServer1 = ""; //如果用了两台netty服务器，则需要开启两个socket链接，分别取链接不同的netty服务
            $scope.socketServer2 = "";
            $scope.alreadyJoinPatientConversation = []; //已经加入会话的用户数据，一个医生可以有多个对话的用户，这些用户的数据，都保存在此集合中
            $scope.currentUserConversation = {}; //医生与当前正在进行对话用户的聊天数据，医生在切换不同用户时，数据变更到切换的用户上来。
            $scope.waitJoinNum = 0; //医生待接入的用户数，是动态变化的数
            $scope.glued = true; //angular滚动条的插件预制参数，让对话滚动条，每次都定位底部，当新的聊天数据到达时
            //各个子窗口的开关变量
            $scope.showFlag = {
                rankList: false,
                systemSetup: false,
                waitProcess: false,
                switchOver: false,
                myReplyList: false,
                publicReplyList: false,
                replyContent: true,
                advisoryContent: false
            }
            $scope.searchFlag = false;

            //初始化医生端登录，建立socket链接，获取基本信息
            $scope.doctorConsultInit = function () {
                var routePath = "/doctor/consultBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath: routePath}, function (data) {
                    $scope.pageLoading = false;
                    if (data.status == "9") {
                        window.location.href = data.redirectURL;
                    } else if (data.status == "8") {
                        window.location.href = data.redirectURL + "?targeturl=" + routePath;
                    } else if (data.status == "20") {
                        $scope.doctorId = data.userId;
                        $scope.doctorName = data.userName;
                        $scope.doctorPhone = data.userPhone;
                        $scope.userType = data.userType;

                        $scope.initConsultSocket1();

                        getIframeSrc();
                        //获取通用回复列表
                        GetAnswerValueList.save({"type": "commonAnswer"}, function (data) {
                            if(data.result=="success"){
                                var answerData = JSON.parse(data.answerValue);
                                $scope.commonAnswer = answerData.commonAnswer;
                            }else{
                                $scope.commonAnswer = [];
                            }
                        });

                        //获取我的回复列表
                        GetAnswerValueList.save({"type":"myAnswer"},function(data){
                            if(data.result=="success"){
                                var answerData = JSON.parse(data.answerValue);
                                $scope.myAnswer = answerData.myAnswer;
                            }else{
                                $scope.myAnswer = [];
                            }
                        });

                        $scope.refreshWaitJoinUserList();

                        getAlreadyJoinConsultPatientList();
                    }
                })
            }

            //公共点击按钮，用来触发弹出对应的子窗口
            $scope.tapShowButton = function(type){
                $.each($scope.showFlag,function(key,value){
                    if(key==type){
                        $scope.showFlag[key] = !$scope.showFlag[key];
                        if("replyContent"==type){
                            if($scope.showFlag.replyContent==false){
                                $scope.showFlag.advisoryContent =true;
                            }else{
                                $scope.showFlag.advisoryContent =false;
                            }
                        }
                        else if("advisoryContent"==type){
                            if($scope.showFlag.advisoryContent==false){
                                $scope.showFlag.replyContent=true;
                            }else{
                                $scope.showFlag.replyContent=false;
                            }
                        }
                        if(!$scope.showFlag[key]){
                            if(type == "myReplyList"){
                                $scope.myReplyIndex = -1;
                                $scope.myReplySecondIndex = -1;
                            }else if(type == "publicReplyList"){
                                $scope.publicReplyIndex = -1;
                                $scope.publicReplySecondIndex = -1;
                            }
                        }else{
                            if(type=="rankList"){
                                //已接入会话咨询医生今日排名
                                $scope.refreshRankList();
                            } else if(type == "switchOver"){
                                //获取在线医生列表
                                $scope.refreshOnLineCsUserList();
                            }else if(type=="waitProcess"){
                                //获取待接入用户列表
                                $scope.refreshWaitJoinUserList();
                            }
                        }
                    }
                })
            }

            /**转接功能区**/
                $scope.acceptTransfer = function(){
                    $scope.tapShowButton('waitProcess');
                    var waitJoinChooseUserList = "";
                    $.each($scope.waitJoinUserList,function(index,value){
                       if(value.chooseFlag){
                           waitJoinChooseUserList = waitJoinChooseUserList + value.forwardSessionId + ";"
                       }
                    });
                    React2Transfer.save({operation:"accept",
                        forwardSessionIds:waitJoinChooseUserList},function(data){
                        if(data.result=="success"){
                            //将转接成功的用户，都加入会话列表中来
                            var indexClose = 0;
                            $.each($scope.waitJoinUserList,function(index,value){
                                if(value.chooseFlag){
                                    $scope.currentUserConversation = {};
                                    $scope.currentUserConversation.patientId = value.userId;
                                    $scope.currentUserConversation.source = value.source;
                                    $scope.currentUserConversation.fromServer = value.serverAddress;
                                    $scope.currentUserConversation.sessionId = value.sessionId;
                                    $scope.currentUserConversation.isOnline = true;
                                    $scope.currentUserConversation.dateTime = value.sessionCreateTime;
                                    $scope.currentUserConversation.messageNotSee = true;
                                    $scope.currentUserConversation.patientName = value.userName;
                                    var consultValue = {};
                                    consultValue.type = value.type;
                                    consultValue.content = value.messageContent;
                                    consultValue.dateTime = value.messageDateTime;
                                    consultValue.senderId = value.userId;
                                    consultValue.senderName = value.userName;
                                    consultValue.sessionId = value.sessionId;
                                    filterMediaData(consultValue);
                                    $scope.currentUserConversation.consultValue = [];
                                    $scope.currentUserConversation.consultValue.push(consultValue);
                                    $scope.alreadyJoinPatientConversation.push($scope.currentUserConversation);
                                }
                            });
                            $scope.refreshWaitJoinUserList();
                            $scope.chooseAlreadyJoinConsultPatient($scope.alreadyJoinPatientConversation[0].patientId,
                                $scope.alreadyJoinPatientConversation[0].patientName);
                        }
                    });
                }

                $scope.rejectTransfer = function(){
                    $scope.tapShowButton('waitProcess');
                    var waitJoinChooseUserList = "";
                    $.each($scope.waitJoinUserList,function(index,value){
                        if(value.chooseFlag){
                            waitJoinChooseUserList = waitJoinChooseUserList + value.forwardSessionId + ";"
                        }
                    });
                    React2Transfer.save({operation:"rejected",forwardSessionIds:waitJoinChooseUserList},function(data){
                        if(data.result=="success"){
                            $scope.refreshWaitJoinUserList();
                        }
                    });
                }

                $scope.chooseTransferCsUser = function(csUserId,csUserName){
                    $scope.transferCsUserId = csUserId;
                    $scope.csTransferUserName = csUserName;
                }

                $scope.transferToCsUser = function(){
                    TransferToOtherCsUser.save({doctorId: $scope.transferCsUserId,
                        sessionId:$scope.currentUserConversation.sessionId,
                        remark: $scope.info.transferRemark},function(data){
                        if(data.result=="success"){
                            $scope.tapShowButton('switchOver');
                            //转接请求成功后，在接诊员侧，保留了此会话，只到被转接的医生收到为止，
                            // 才将会话拆除，在此过程中，允许接诊员，取消转接。
                        }else if(data.result=="failure"){
                            alert("转接失败，请转接给其他医生");
                        }
                    });
                }

                $scope.searchCsUser = function(){
                    if($scope.info.searchCsUserValue!=""){
                        $scope.searchFlag = true;
                    }else{
                        $scope.searchFlag = false;
                    }
                }

                $scope.cancelTransfer = function(sessionId,toCsUserId,remark){
                    CancelTransfer.save({sessionId:sessionId,toCsUserId:toCsUserId,remark:remark},function(data){
                        if(data.result=="success"){
                            //删除取消通知
                            var indexClose = 0;
                            $.each($scope.currentUserConversation.consultValue, function (index, value) {
                                if (value.remark == remark && value.toCsUserId==toCsUserId) {
                                    indexClose = index;
                                }
                            })
                            $scope.currentUserConversation.consultValue.splice(indexClose, 1);
                        }
                    })
                }
            /**转接功能区**/

            /**会话操作区**/
                //初始化socket链接
                $scope.initConsultSocket1 = function () {
                    if (!window.WebSocket) {
                        window.WebSocket = window.MozWebSocket;
                    }
                    if (window.WebSocket) {
                        if($scope.userType=="distributor"){
                            $scope.socketServer1 = new ReconnectingWebSocket("ws://101.201.154.201:2048/ws&" +
                                "distributor&" + $scope.doctorId);//cs,user,distributor
                        }else if($scope.userType=="consultDoctor"){
                            $scope.socketServer1 = new ReconnectingWebSocket("ws://101.201.154.201:2048/ws&" +
                                "cs&" + $scope.doctorId);//cs,user,distributor
                        }

                        $scope.socketServer1.onmessage = function (event) {
                            var consultData = JSON.parse(event.data);
                            if(consultData.type==4){
                                processNotifyMessage(consultData);
                            }else{
                                filterMediaData(consultData);
                                processPatientSendMessage(consultData);
                            }
                            $scope.$apply();
                            $scope.triggerVoice();
                        };

                        $scope.socketServer1.onopen = function (event) {
                        };

                        $scope.socketServer1.onclose = function (event) {
                        };
                    } else {
                        alert("你的浏览器不支持！");
                    }
                }

                //处理用户按键事件
                document.onkeydown = function () {
                    var a = window.event.keyCode;
                    if (a == 13) {
                        if($("#consultMessage").val()!=""){
                            $scope.info.consultMessage = $("#consultMessage").val();
                            $scope.sendConsultMessage();
                        }
                        $scope.$apply();
                    }
                };//当onkeydown 事件发生时调用函数

                //向用户发送咨询消息
                $scope.sendConsultMessage = function () {
                    if (!window.WebSocket) {
                        return;
                    }
                    if ($scope.socketServer1.readyState == WebSocket.OPEN) {
                        var consultValMessage = {
                            "type": 0,
                            "content": $scope.info.consultMessage,
                            "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                            "senderId": angular.copy($scope.doctorId),
                            "senderName": angular.copy($scope.doctorName),
                            "sessionId": angular.copy($scope.currentUserConversation.sessionId)
                        };
                        $scope.socketServer1.send(JSON.stringify(consultValMessage));
                        consultValMessage.content =  $sce.trustAsHtml(AnalyticEmotion(angular.copy($scope.info.consultMessage)));
                        $scope.info.consultMessage = "";
                        updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                    } else {
                        alert("连接没有开启.");
                    }
                }
                //向用户发送咨询图片
                $scope.uploadFiles = function($files,fileType) {
                    var dataValue = {
                        "fileType": fileType,
                        "senderId": $scope.patientId
                    };
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
                            var consultValMessage = {
                                "type": 1,
                                "content": data.showFile,
                                "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                                "senderId": angular.copy($scope.doctorId),
                                "senderName": angular.copy($scope.doctorName),
                                "sessionId": angular.copy($scope.currentUserConversation.sessionId)
                            };
                            console.log(consultValMessage.content);
                            if (!window.WebSocket) {
                                return;
                            }
                            if ($scope.socketServer.readyState == WebSocket.OPEN) {
                                $scope.consultContent.push(consultValMessage);
                                $scope.socketServer.send(JSON.stringify(consultValMessage));
                            } else {
                                alert("连接没有开启.");
                            }
                        });
                    }
                };

                //关闭跟某个用户的会话
                $scope.closeConsult = function () {
                    SessionEnd.get({sessionId:$scope.currentUserConversation.sessionId,
                        userId:$scope.currentUserConversation.patientId},function(data){
                        if(data.result=="success"){
                            var indexClose = 0;
                            $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                                if (value.patientId == $scope.chooseAlreadyJoinConsultPatientId) {
                                    indexClose = index;
                                }
                            })
                            $scope.alreadyJoinPatientConversation.splice(indexClose, 1);
                            if($scope.alreadyJoinPatientConversation.length!=0){
                                $scope.chooseAlreadyJoinConsultPatient($scope.alreadyJoinPatientConversation[0].patientId,
                                    $scope.alreadyJoinPatientConversation[0].patientName);
                            }else{
                                $scope.currentUserConversation = {};
                            }
                        }else{
                            alert("会话关闭失败，请重试");
                        }
                    })
                }

                //在通话列表中，选取一个用户进行会话
                $scope.chooseAlreadyJoinConsultPatient = function (patientId, patientName) {
                    $scope.chooseAlreadyJoinConsultPatientId = patientId;
                    $scope.chooseAlreadyJoinConsultPatientName = patientName;
                    getIframeSrc();
                    var updateFlag = false;
                    $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                        if (value.patientId == patientId) {
                            $scope.currentUserConversation = "";
                            $scope.currentUserConversation = value;
                            updateFlag = true;
                        }
                    });
                    if(!updateFlag){
                        GetCurrentUserHistoryRecord.save({
                            userId:patientId,
                            dateTime: moment().format('YYYY-MM-DD HH:mm:ss'),
                            pageSize:100},function(data){
                            if(data.consultDataList!=""){
                                $.each(data.consultDataList,function(index,value){
                                    $scope.currentUserConversation.consultValue.splice(0, 0, value);
                                });
                            }
                        })
                    }
                }

                //触发qq声音
                $scope.triggerVoice = function () {
                    var audio = document.createElement('audio');
                    var source = document.createElement('source');
                    source.type = "audio/mpeg";
                    source.type = "audio/mpeg";
                    source.src = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/18.ogg";
                    source.autoplay = "autoplay";
                    source.controls = "controls";
                    audio.appendChild(source);
                    audio.play();
                }

                $scope.getEmotion = function (){
                    $('#face').SinaEmotion($('.emotion'));
                }

                //查看更多的用户历史消息
                $scope.seeMoreConversationMessage = function(){
                    var mostFarCurrentConversationDateTime = $scope.currentUserConversation.consultValue[0].dateTime;
                    GetCurrentUserHistoryRecord.save({
                        userId:$scope.currentUserConversation.patientId,
                        dateTime:$scope.currentUserConversation.consultValue[0].dateTime,
                        pageSize:10},function(data){
                        if(data.consultDataList!=""){
                            $.each(data.consultDataList,function(index,value){
                                filterMediaData(value);
                                $scope.currentUserConversation.consultValue.splice(0, 0, value);
                            });
                        }
                    })
                }
            /**会话操作区**/


            //更新咨询医生当日咨询用户数的排名列表
            $scope.refreshRankList = function(){
                var currDate = new moment().format("YYYY-MM-DD");
                GetTodayRankingList.save({"rankDate": currDate}, function (data) {
                    $scope.info.rankListValue = data.rankListValue;
                });
            }

            //获取在线的咨询医生列表
            $scope.refreshOnLineCsUserList = function(){
                $scope.searchFlag = false;
                $scope.info.searchCsUserValue = "";
                GetOnlineDoctorList.save({}, function (data) {
                    $scope.info.onLineCsUserList = data.onLineCsUserList;
                });
            }

            //获取待接入会话用户列表
            $scope.refreshWaitJoinUserList = function(){
                GetWaitJoinList.save({csUserId:$scope.doctorId},function(data){
                    $scope.waitJoinNum = data.waitJoinNum;
                    $scope.waitJoinUserList = data.waitJoinList;
                })
            }

            /***回复操作区**/
                //我的回复内容
                $scope.tapMyReplyContent = function (parentIndex) {
                    if($scope.myReplyIndex==parentIndex){
                        $scope.myReplyIndex = -1;
                        $scope.myReplySecondIndex = -1;
                        $scope.info.editGroup = "";
                        $scope.info.editContent = "";
                    }else{
                        $scope.myReplyIndex = parentIndex;
                        $scope.myReplySecondIndex = -1;
                        $scope.info.editGroup = $scope.myAnswer[parentIndex].name;
                        $scope.info.editContent = "";
                    }
                }
                $scope.tapEditMyContent = function(parentIndex, childIndex) {
                    $scope.myReplySecondIndex = childIndex;
                    $scope.info.editContent = $scope.myAnswer[parentIndex].secondAnswer[childIndex].name;
                };
                $scope.chooseMyContent = function(parentIndex, childIndex){
                    $scope.info.consultMessage = angular.copy($scope.myAnswer[parentIndex].secondAnswer[childIndex].name);
                }

                //公共回复内容
                $scope.chooseCommonContent = function(parentIndex, childIndex){
                    $scope.info.consultMessage = angular.copy($scope.commonAnswer[parentIndex].secondAnswer[childIndex].name);
                }
                //公告回复内容
                $scope.tapPublicReplyContent = function (parentIndex){
                    if($scope.publicReplyIndex==parentIndex){
                        $scope.publicReplyIndex = -1;
                        $scope.publicReplySecondIndex = -1;
                    }else{
                        $scope.publicReplyIndex = parentIndex;
                        $scope.publicReplySecondIndex = -1;
                    }
                }
                //编辑公共内容
                $scope.tapEditCommonContent = function(parentIndex, childIndex){
                    $scope.publicReplySecondIndex = childIndex;
                };
                //添加分组
                $scope.add = function() {
                    $scope.info.addGroup = '';
                    $scope.info.addContent = '';
                    if($scope.showFlag.myReplyList){
                        if($scope.myReplyIndex==-1||$scope.myReplyIndex==undefined){
                            $scope.addGroupFlag = true;
                            $scope.addContentFlag = false;
                        }else{
                            $scope.addGroupFlag = false;
                            $scope.addContentFlag = true;
                        }
                    }
                }
                $scope.closeAddGroup = function() {
                    $scope.info.addGroup = '';
                    $scope.info.addContent = '';
                    $scope.addGroupFlag = false;
                }
                $scope.addGroupSubmit = function () {
                    var setGroupContent = {};
                    setGroupContent.name = $scope.info.addGroup;
                    setGroupContent.secondAnswer=[];
                    $scope.myAnswer.push(setGroupContent);
                    saveMyAnswer();
                    $scope.addGroupFlag = false;
                }

                //添加内容
                $scope.closeAddContent = function(){$scope.addContentFlag=false;}
                $scope.addContentSubmit = function () {
                    var setContent = {};
                    setContent.name = $scope.info.addContent;
                    $scope.myAnswer[$scope.myReplyIndex].secondAnswer.push(setContent);
                    saveMyAnswer();
                    $scope.addContentFlag=false;
                }
                //编辑分组
                $scope.closeEditGroup = function(){$scope.editGroupFlag = false;}
                //编辑内容
                $scope.closeEditContent = function(){$scope.editContentFlag = false;}
                $scope.edit = function() {
                    if($scope.showFlag.myReplyList){
                        if($scope.myReplyIndex!=-1&&$scope.myReplyIndex!=undefined){
                            if($scope.myReplySecondIndex==-1||$scope.myReplyIndex==undefined){
                                $scope.editGroupFlag = true;
                                $scope.editContentFlag = false;
                            }else{
                                $scope.editGroupFlag = false;
                                $scope.editContentFlag = true;
                            }
                        }
                    }
                }
                $scope.editGroupSubmit = function () {
                    var setGroup = {};
                    setGroup.name = $scope.info.editGroup;
                    setGroup.secondAnswer=[];
                    $scope.myAnswer.splice($scope.myReplyIndex, 1,setGroup);
                    saveMyAnswer();
                    $scope.editGroupFlag=false;
                }
                $scope.editContentSubmit = function () {
                    var setContent = {};
                    setContent.name = $scope.info.editContent;
                    $scope.myAnswer[$scope.myReplyIndex].secondAnswer.splice($scope.myReplySecondIndex, 1,setContent);
                    saveMyAnswer();
                    $scope.editContentFlag=false;
                }

                //删除
                $scope.remove = function(){
                    if($scope.showFlag.myReplyList){
                        if($scope.myReplyIndex!=-1&&$scope.myReplyIndex!=undefined){
                            if($scope.myReplySecondIndex==-1||$scope.myReplyIndex==undefined){
                                if ($window.confirm("确定要删除该组回复?")) {
                                    $scope.myAnswer.splice($scope.myReplyIndex, 1);
                                    saveMyAnswer();
                                }
                            }else{
                                if($window.confirm("确定要删除该回复?")) {
                                    $scope.myAnswer[$scope.myReplyIndex].secondAnswer.splice($scope.myReplySecondIndex, 1);
                                    saveMyAnswer();
                                }
                            }
                        }
                    }
                };

                //保存我的回复
                var saveMyAnswer = function() {
                    GetMyAnswerModify.save({answer: $scope.myAnswer, answerType: "myAnswer"}, function (data) {
                    });
                }

            /***回复操作区**/

            var getIframeSrc = function(){
                var newSrc = $(".advisory-content").attr("src");
                $(".advisory-content").attr("src","");
                $(".advisory-content").attr("src",newSrc);
            }

            //日期转换
            $scope.transformDate = function(dateTime){
                var dateValue = new moment(dateTime).format("HH:mm");
                return dateValue;
            }

            var getAlreadyJoinConsultPatientList = function () {
                //获取跟医生的会话还保存的用户列表
                GetCurrentUserConsultListInfo.save({csUserId:$scope.doctorId,pageNo:1,pageSize:10000},function(data){
                    if(data.alreadyJoinPatientConversation!=""&&data.alreadyJoinPatientConversation!=undefined){
                        $scope.alreadyJoinPatientConversation = data.alreadyJoinPatientConversation;
                        $.each($scope.alreadyJoinPatientConversation,function(index,value){
                            $.each(value.consultValue,function(index1,value1){
                                filterMediaData(value1);
                            })
                        })
                        var patientId = angular.copy($scope.alreadyJoinPatientConversation[0].patientId);
                        var patientName = angular.copy($scope.alreadyJoinPatientConversation[0].patientName);
                        $scope.chooseAlreadyJoinConsultPatient(patientId,patientName);
                    }
                })
            }


            //处理用户发送过来的会话消息
            var processPatientSendMessage = function(conversationData){
                console.log(conversationData);
                var currentConsultValue = {};
                var chooseFlag = false;
                currentConsultValue.type = conversationData.type;
                currentConsultValue.content = conversationData.content;
                currentConsultValue.dateTime = conversationData.dateTime;
                currentConsultValue.senderId = conversationData.senderId;
                currentConsultValue.senderName = conversationData.senderName;
                currentConsultValue.sessionId = conversationData.sessionId;
                if(JSON.stringify($scope.currentUserConversation)=='{}'){
                    $scope.currentUserConversation.patientId = conversationData.senderId;
                    $scope.currentUserConversation.source = conversationData.source;
                    $scope.currentUserConversation.fromServer = conversationData.fromServer;
                    $scope.currentUserConversation.sessionId = conversationData.sessionId;
                    $scope.currentUserConversation.isOnline = true;
                    $scope.currentUserConversation.dateTime = conversationData.dateTime;
                    $scope.currentUserConversation.messageNotSee = false;
                    $scope.currentUserConversation.patientName = conversationData.senderName;
                    $scope.currentUserConversation.consultValue = [];
                    $scope.currentUserConversation.consultValue.push(currentConsultValue);
                    chooseFlag = true;
                }
                updateAlreadyJoinPatientConversationFromPatient(conversationData);
                if(chooseFlag){
                    $scope.chooseAlreadyJoinConsultPatient(angular.copy(currentConsultValue.senderId),
                        angular.copy(currentConsultValue.senderName));
                    getIframeSrc();
                }
            }

            var updateAlreadyJoinPatientConversationFromPatient = function(conversationData){
                var updateFlag = false;
                $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                    if (value.patientId == conversationData.senderId) {
                        value.consultValue.push(conversationData);
                        updateFlag = true;
                    }
                });
                if(!updateFlag){
                    var consultValue = {};
                    var conversationContent = {};
                    consultValue.type = conversationData.type;
                    consultValue.content = conversationData.content;
                    consultValue.dateTime = conversationData.dateTime;
                    consultValue.senderId = conversationData.senderId;
                    consultValue.senderName = conversationData.senderName;
                    consultValue.sessionId = conversationData.sessionId;
                    conversationContent.patientId = conversationData.senderId;
                    conversationContent.source = conversationData.source;
                    conversationContent.fromServer = conversationData.fromServer;
                    conversationContent.sessionId = conversationData.sessionId;
                    conversationContent.isOnline = true;
                    conversationContent.dateTime = conversationData.dateTime;
                    conversationContent.messageNotSee = false;
                    conversationContent.patientName = conversationData.senderName;
                    conversationContent.consultValue = [];
                    conversationContent.consultValue.push(consultValue);
                    $scope.alreadyJoinPatientConversation.push(conversationContent);
                }
            }

            var updateAlreadyJoinPatientConversationFromDoctor = function(consultValMessage){
                $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                    if (value.patientId == $scope.currentUserConversation.patientId) {
                        value.consultValue.push(consultValMessage);
                    }
                });
            }

            //处理系统发送过来的通知类消息
            var processNotifyMessage = function(notifyData){
                if(notifyData.notifyType=="0009"){
                    //有转接的用户过来
                    $scope.refreshWaitJoinUserList();
                } else if(notifyData.notifyType=="0012"){
                    //取消转接过来的用户
                    $scope.refreshWaitJoinUserList();
                }
                else if(notifyData.notifyType=="0013"){
                    $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                        if (value.patientId == notifyData.session.userId) {
                            $.each(value.consultValue, function (index1, value1) {
                                if (value1.notifyType == "0011") {
                                    value1.notifyType = "0013";
                                }
                            });
                        }
                    });
                }
                else if(notifyData.notifyType=="0014"){
                    $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                        if (value.patientId == notifyData.session.userId){
                            value.consultValue.push(notifyData);
                        }
                    });
                }
                else if(notifyData.notifyType=="0011"){
                    //通知接诊员，转接正在进行中，还未被医生受理
                    $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                        if (value.patientId == notifyData.session.userId) {
                            value.consultValue.push(notifyData);
                        }
                    });
                }
                else if(notifyData.notifyType=="0010"){
                    //通知接诊员，转接的处理情况，rejected为拒绝，accept为转接受理了
                    if(notifyData.operation=="accept"){
                        var indexClose = 0;
                        $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                            if (value.patientId == notifyData.session.userId) {
                                indexClose = index;
                            }
                        })
                        $scope.alreadyJoinPatientConversation.splice(indexClose, 1);
                        if($scope.alreadyJoinPatientConversation.length==0){
                            $scope.currentUserConversation = {};
                        }else{
                            if($scope.currentUserConversation.patientId == notifyData.session.userId){
                                $scope.chooseAlreadyJoinConsultPatient($scope.alreadyJoinPatientConversation[0].patientId,
                                    $scope.alreadyJoinPatientConversation[0].patientName);
                            }
                        }
                    }
                    else if(notifyData.operation=="rejected"){
                        $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                            if (value.patientId == notifyData.session.userId) {
                                value.consultValue.push(notifyData);
                            }
                        });
                    }
                }
            }

            //过滤媒体数据
            var filterMediaData = function (val) {
                if(val.senderId==$scope.doctorId){
                    if (val.type == "0") {
                        val.content = $sce.trustAsHtml(AnalyticEmotion(angular.copy(val.content)));
                    }
                }else{
                    if (val.type == "2"||val.type == "3") {
                        val.content = $sce.trustAsResourceUrl(angular.copy(val.content));
                    }
                }
            }
        }])

    .controller('messageListCtrl', ['$scope', '$log', '$sce', 'GetUserConsultListInfo',
        'GetUserRecordDetail', 'GetCSDoctorList', 'GetMessageRecordInfo','GetUserLoginStatus','$location','CreateDoctorConsultSession',
        function ($scope, $log, $sce, GetUserConsultListInfo, GetUserRecordDetail,
                  GetCSDoctorList, GetMessageRecordInfo,GetUserLoginStatus,$location,CreateDoctorConsultSession) {

            $scope.info = {};

            $scope.searchMessageContent = "";

            $scope.messageType = "";

            $scope.currentClickUserName = "";

            $scope.currentClickUserId = "";

            $scope.searchMessageType = [
                {
                searchType:"user",
                searchContent: "查找客户"
                }
                //,{
                //searchType:"message",
                //searchContent: "查找消息"
                //}
            ];

            $scope.searchDate = [{
                name: "今天",
                value: 0
            }, {
                name: "最近7天",
                value: 7
            }, {
                name: "最近30天",
                value: 30
            },{
                name: "所有时间",
                value: 10000
            }];

            $scope.userConsultListPageSize = 11;

            $scope.userRecordDetailPageSize = 10;

            $scope.recordType = "all";

            $scope.messageListInit = function(){
                var routePath = "/doctor/consultBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath: routePath}, function (data) {
                    if (data.status == "9") {
                        window.location.href = data.redirectURL;
                    } else if (data.status == "8") {
                        window.location.href = data.redirectURL + "?targeturl=" + routePath;
                    } else if (data.status == "20") {
                        //获取会话客户列表（含会话转接过程中，经历过几个客服）
                        $scope.selectedDate = $scope.searchDate[0];
                        $scope.selectedMessage = $scope.searchMessageType[0];
                        $scope.setSearchMessageType("user");
                        GetUserConsultListInfo.save({dateNum: 0,
                            CSDoctorId: "allCS",
                            pageNo: 1, pageSize: $scope.userConsultListPageSize}, function (data) {
                            refreshUserConsultListData(data);
                        })

                        //获取客服医生列表
                        GetCSDoctorList.save({}, function (data) {
                            $scope.CSList = [{"id":"allCS","name":"所有客服"}];
                            $.each(data.CSList,function(index,value){
                                $scope.CSList.push(value);
                            })
                            $scope.selectedCsList = $scope.CSList[0];
                            $scope.dateNumValue = angular.copy($scope.selectedDate.value);
                            $scope.CSDoctorIdValue =angular.copy($scope.selectedCsList.id);
                        });
                    }
                })
            }

            //获取用户的详细聊天记录
            $scope.getUserRecordDetail = function (userName,userId,index) {
                $scope.doctorCreateConsultSessionChoosedUserId = userId;
                $scope.setSessoin = index;
                GetUserRecordDetail.save({pageNo:1,pageSize:$scope.userRecordDetailPageSize,
                    userId:userId,recordType:"all"}, function (data) {
                    $scope.currentClickUserName = userName;
                    $scope.currentClickUserId = userId;
                    $scope.currentUserConsultRecordDetail = data.records;
                    $scope.currentUserRecordDetailPage = data.currentPage;
                    $scope.totalUserRecordDetailPage = data.totalPage;
                    $.each($scope.currentUserConsultRecordDetail,function(index,value){
                        filterMediaData(value);
                    });
                });
            }

            $scope.chooseUserRecordDetail = function(action,recordType){
                var pageNum = 1;
                if(action == "firstPage" ){
                    pageNum = 1;
                }else if(action == "lastPage"){
                    pageNum = $scope.totalUserRecordDetailPage;
                }else if(action == "upPage"){
                    if($scope.currentUserRecordDetailPage>1){
                        pageNum = $scope.currentUserRecordDetailPage-1;
                    }
                }else if(action == "nextPage"){
                    if($scope.currentUserRecordDetailPage<$scope.totalUserRecordDetailPage){
                        pageNum = $scope.currentUserRecordDetailPage+1;
                    }
                }
                GetUserRecordDetail.save({pageNo:pageNum,
                    pageSize:$scope.userRecordDetailPageSize,
                    userId:$scope.currentClickUserId,recordType:recordType}, function (data) {
                    $scope.currentClickUserName = $scope.currentClickUserName;
                    $scope.currentClickUserId = $scope.currentClickUserId;
                    $scope.currentUserConsultRecordDetail = data.records;
                    $scope.currentUserRecordDetailPage = data.currentPage;
                    $scope.totalUserRecordDetailPage = data.totalPage;
                    $.each($scope.currentUserConsultRecordDetail,function(index,value){
                        filterMediaData(value);
                    });
                });
            }

            //查找消息记录（点击全部、图片等）
            $scope.setRecordType = function (searchRecordType) {
                $scope.recordType = searchRecordType;
                $scope.chooseUserRecordDetail("firstPage",$scope.recordType);
            }

            //查询某个客服信息位于某个时间段的信息
            $scope.getCsInfoByUserAndDate = function(Object){
                if (Object == 1000 || Object == 0 || Object == 7 || Object == 30) {
                    $scope.dateNumValue = angular.copy(Object);
                } else {
                    $scope.CSDoctorIdValue =angular.copy(Object);
                }
                GetUserConsultListInfo.save({dateNum: $scope.dateNumValue,
                    CSDoctorId: $scope.CSDoctorIdValue,
                    pageNo: 1, pageSize:$scope.userConsultListPageSize}, function (data) {
                    refreshUserConsultListData(data);
                })
            }

            //查找咨询记录（消息列表右上角的搜索功能）
            $scope.searchMessage = function () {
                if($scope.info.searchMessageContent == '' || $scope.info.searchMessageContent == null){
                    alert('请选择查询内容！')
                    return ;
                }else if($scope.messageType == '' || $scope.messageType == null){
                    alert('请选择查询类型！');
                    return ;
                }else{
                    GetMessageRecordInfo.save({
                        searchInfo: $scope.info.searchMessageContent,
                        searchType: $scope.messageType,
                        pageNo: 1,
                        pageSize: $scope.userConsultListPageSize
                    }, function (data) {
                        refreshUserConsultListData(data);
                    });
                }
            }

            //查找咨询记录
            $scope.setSearchMessageType = function (searchType) {
                $scope.messageType = searchType;
            }

            //左上角的刷新消息
            $scope.refreshUserList = function () {
                GetUserConsultListInfo.save({dateNum: $scope.dateNumValue,
                    CSDoctorId: $scope.CSDoctorIdValue,
                    pageNo: 1,
                    pageSize: $scope.userConsultListPageSize}, function (data) {
                    refreshUserConsultListData(data);
                })
            }

            //右上角的消息刷新
            $scope.refreshCurrentUserRecordDetail = function(){
                $scope.getUserRecordDetail($scope.userConsultListInfo[0].userName,$scope.userConsultListInfo[0].userId,0);
            }

            //医生发起一个针对用户的会话
            $scope.createDoctorConsultSession = function(){
                CreateDoctorConsultSession.save({userId:$scope.doctorCreateConsultSessionChoosedUserId},function(data){
                    console.log(data);
                })
            }

            $scope.chooseUserConsultListDataPage = function(action){
                var pageNum = 1;
                if(action == "firstPage" ){
                    pageNum = 1;
                }else if(action == "lastPage"){
                    pageNum = $scope.totalUserConsultListDataPage;
                }else if(action == "upPage"){
                    if($scope.currentUserConsultListDataPage>1){
                        pageNum = $scope.currentUserConsultListDataPage-1;
                    }
                }else if(action == "nextPage"){
                    if($scope.currentUserConsultListDataPage<$scope.totalUserConsultListDataPage){
                        pageNum = $scope.currentUserConsultListDataPage+1;
                    }
                }
                GetUserConsultListInfo.save({dateNum: $scope.dateNumValue,
                    CSDoctorId: $scope.CSDoctorIdValue,
                    pageNo: pageNum, pageSize: $scope.userConsultListPageSize}, function (data) {
                    refreshUserConsultListData(data);
                })
            }

            var refreshUserConsultListData = function(data){
                $scope.userConsultListInfo = data.userList;
                $scope.totalUserConsultListDataPage = data.totalPage;
                $scope.currentUserConsultListDataPage = data.currentPage;
                if($scope.totalUserConsultListDataPage==0){
                    $scope.currentUserConsultListDataPage = 0;
                }
                if($scope.userConsultListInfo.length!=0){
                    $scope.getUserRecordDetail($scope.userConsultListInfo[0].userName,$scope.userConsultListInfo[0].userId,0);
                }else{
                    $scope.userConsultListInfo = [];
                    $scope.currentClickUserName = "";
                    $scope.currentClickUserId = "";
                    $scope.currentUserConsultRecordDetail = [];
                }
            }

            //过滤媒体数据
            var filterMediaData = function (val) {
                if (val.type == "1"||val.type == "2" || val.type == "3") {
                    val.message = $sce.trustAsResourceUrl(angular.copy(val.message));
                }
            }

        }])

