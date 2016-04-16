angular.module('controllers', ['luegg.directives'])
    .controller('doctorConsultFirstCtrl', ['$scope', '$sce', '$window','getTodayRankingList',
        'getOnlineDoctorList','getCommonAnswerList','getMyAnswerList','GetUserLoginStatus',
        '$location', 'GetUserRecordList','getMyAnswerModify','getCurrentUserConsultListInfo',
        function ($scope, $sce, $window,getTodayRankingList, getOnlineDoctorList, getCommonAnswerList,
                  getMyAnswerList, GetUserLoginStatus, $location, GetUserRecordList,getMyAnswerModify,
                  getCurrentUserConsultListInfo) {
            $scope.test = "";
            $scope.info = {};
            $scope.socketServer1 = "";
            $scope.socketServer2 = "";
            $scope.alreadyJoinPatientConversation = [];
            $scope.currentUserConversation = {};
            $scope.waitJoinNum = 0;
            $scope.info.effect = "true";
            $scope.glued = true;

            $scope.showFlag = {
                rankList: false,
                systemSetup: false,
                waitProcess: false,
                switchOver: false,
                myReplyList: false,
                publicReplyList: false,
            }

            $scope.tapShowButton = function(type){
                $.each($scope.showFlag,function(key,value){
                    if(key==type){
                        $scope.showFlag[key] = !$scope.showFlag[key];
                        if(!$scope.showFlag[key]){
                            if(type == "myReplyList"){
                                $scope.myReplyIndex = -1;
                                $scope.myReplySecondIndex = -1;
                            }else if(type == "publicReplyList"){
                                $scope.publicReplyIndex = -1;
                                $scope.publicReplySecondIndex = -1;
                            }
                        }
                    }
                })
            }

            ////QQ表情初始化
            //qqFace();

            //初始化医生端登录，建立socket链接，获取基本信息
            $scope.doctorConsultFirst = function () {
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

                        //获取今日咨询排名
                        var currDate = new moment().format("YYYY-MM-DD");
                        getTodayRankingList.save({"rankDate": currDate}, function (data) {
                            $scope.info.rankListlength = data.rankListValue.length;
                            if (data.rankListValue.length == 0) {
                                $scope.lockScroll = "false";
                            } else {
                                $scope.rankListValue = data.rankListValue;
                            }
                        });

                        //获取在线医生列表
                        getOnlineDoctorList.save({"pageNo": "1", "pageSize": "3"}, function (data) {

                        });

                        //获取通用回复列表
                        getCommonAnswerList.save({"type": "commonAnswer"}, function (data) {
                            if (data.commonAnswer.length == 0) {
                                $scope.lockScroll = "false";
                            } else {
                                $scope.commonAnswer = data.commonAnswer;
                            }
                        });

                        getMyAnswerList.save({"type":"myAnswer"},function(data){
                            if(data.myAnswer!=null && data.myAnswer.length==0){
                                $scope.lockScroll="false";
                            } else {
                                $scope.myAnswer = data.myAnswer;
                            }
                        });

                        getAlreadyJoinConsultPatientList();
                        
                    }
                })
            }

            //在通话列表中，选取一个用户进行会话
            $scope.chooseAlreadyJoinConsultPatient = function (patientId, patientName) {
                $scope.chooseAlreadyJoinConsultPatientId = patientId;
                $scope.chooseAlreadyJoinConsultPatientName = patientName;

                var updateFlag = false;
                $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                    if (value.patientId == patientId) {
                        $scope.currentUserConversation = "";
                        $scope.currentUserConversation = angular.copy(value);
                        updateFlag = true;
                    }
                });
                if(!updateFlag){
                    GetUserRecordList.save({
                        recordType: "user",
                        pageNo: 1,
                        pageSize: 100,
                        patientId: patientId,
                        patientName: patientName
                    }, function (data) {
                        $scope.currentUserConversation = "";
                        $scope.currentUserConversation = angular.copy(data.records);
                        $scope.alreadyJoinPatientConversation.push(data.records);
                    });
                }
            }

            //向用户发送咨询消息
            $scope.sendConsultMessage = function () {
                var consultValMessage = {
                    "type": 0,
                    "content": angular.copy($scope.info.consultMessage),
                    "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                    "senderId": angular.copy($scope.doctorId),
                    "senderName": angular.copy($scope.doctorName),
                    "sessionId": angular.copy($scope.currentUserConversation.sessionId)
                };

                if (!window.WebSocket) {
                    return;
                }
                if ($scope.socketServer1.readyState == WebSocket.OPEN) {
                    $scope.info.consultMessage = "";
                    if($scope.currentUserConversation.consultValue!=undefined&&
                        $scope.currentUserConversation.consultValue!=""){
                        $scope.currentUserConversation.consultValue.push(consultValMessage);
                        updateAlreadyJoinPatientConversationFromDoctor(angular.copy(consultValMessage));
                        $scope.socketServer1.send(JSON.stringify(consultValMessage));
                    }
                } else {
                    alert("连接没有开启.");
                }
            }

            //关闭跟某个用户的会话
            $scope.closeConsult = function () {
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
            }

            $scope.useImgFace = function () {}

            //初始化socket链接
            $scope.initConsultSocket1 = function () {
                if (!window.WebSocket) {
                    window.WebSocket = window.MozWebSocket;
                }
                if (window.WebSocket) {
                    if($scope.userType="distributor"){
                        $scope.socketServer1 = new ReconnectingWebSocket("ws://120.25.161.33:2048/ws&" +
                            "distributor&" + $scope.doctorId);//cs,user,distributor
                    }else if($scope.userType="consultDoctor"){
                        $scope.socketServer1 = new ReconnectingWebSocket("ws://120.25.161.33:2048/ws&" +
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
                if ((a == 13) && (event.ctrlKey)) {
                    if ($scope.info.consultMessage != "") {
                        $scope.sendConsultMessage();
                    }
                    $scope.$apply();
                }
            };//当onkeydown 事件发生时调用函数

            $scope.getQQExpression = function () {
                $('.emotion').qqFace({
                    id: 'facebox', //表情盒子的ID
                    assign: 'saytext', //给那个控件赋值
                    path: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fface%2F',//表情存放的路径
                });
            }

            $(".sub_btn").click(function () {
                var shows = document.querySelectorAll('#show');
                for (var i = 0; i < shows.length; i++) {
                    shows[i].innerHTML = replace_em(shows[i].innerHTML);
                }
            });
            //触发qq声音
            $('.lipanpan').click(function() {
                var audio = document.createElement('audio');
                var source = document.createElement('source');
                source.type = "audio/mpeg";
                source.type = "audio/mpeg";
                source.src = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/18.ogg";
                source.autoplay = "autoplay";
                source.controls = "controls";
                audio.appendChild(source);
                audio.play();
            })
            //查看结果
            var replace_em = function(str){
                str = str.replace(/\[em_([0-9]*)\]/g, '<img src="http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/' +
                    'dkf%2Fface%2F$1.gif" border="0" />');
                return str;
            }

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
                $scope.addgroup = false;
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
                $scope.editgroup=false;
            }
            $scope.editContentSubmit = function () {
                var setContent = {};
                setContent.name = $scope.info.editContent;
                $scope.myAnswer[$scope.myReplyIndex].secondAnswer.splice($scope.myReplySecondIndex, 1,setContent);
                saveMyAnswer();
                $scope.editcontent=false;
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

            //日期转换
            $scope.transformDate = function(dateTime){
                var dateValue = new moment(dateTime).format("HH:mm");
                return dateValue;
            }

            var getAlreadyJoinConsultPatientList = function () {
                //获取跟医生的会话还保存的用户列表
                getCurrentUserConsultListInfo.save({doctorId:$scope.doctorId},function(data){
                    if(data.result!=""){
                        $scope.alreadyJoinPatientConversation = data.alreadyJoinPatientConversation;
                        var patientId = angular.copy($scope.alreadyJoinPatientConversation[0].patientId);
                        var patientName = angular.copy($scope.alreadyJoinPatientConversation[0].patientName);
                        $scope.chooseAlreadyJoinConsultPatient(patientId,patientName);
                    }
                })
            }

            //保存我的回复
            var saveMyAnswer = function() {
                getMyAnswerModify.save({answer: $scope.myAnswer, answerType: "myAnswer"}, function (data) {
                });
            }

            //处理用户发送过来的会话消息
            var processPatientSendMessage = function(conversationData){
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
                else if($scope.currentUserConversation.patientId == conversationData.senderId){
                    $scope.currentUserConversation.consultValue.push(currentConsultValue);
                }

                updateAlreadyJoinPatientConversationFromPatient(angular.copy(conversationData));
                if(chooseFlag){
                    $scope.chooseAlreadyJoinConsultPatient(angular.copy(currentConsultValue.senderId),
                        angular.copy(currentConsultValue.senderName));
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
            var processNotifyMessage = function(notifyDate){

            }

            //过滤媒体数据
            var filterMediaData = function (val) {
                if (val.type == "2"||val.type == "3") {
                    val.content = $sce.trustAsResourceUrl(angular.copy(val.content));
                }
            }
        }])

    .controller('messageListCtrl', ['$scope', '$log', '$sce', 'getUserConsultListInfo', 'getUserRecordDetail', 'getCSDoctorList', 'CSInfoByUserId', 'getMessageRecordInfo',
        function ($scope, $log, $sce, getUserConsultListInfo, getUserRecordDetail, getCSDoctorList, CSInfoByUserId, getMessageRecordInfo) {

            $scope.info = {};

            $scope.searchMessageContent = "";

            $scope.messageType = "";

            $scope.currentClickUserName = "";

            $scope.defaultClickUserOpenId = "";

            $scope.searchMessageType = [{
                searchType:"user",
                searchContent: "查找客户"
            },{
                searchType:"message",
                searchContent: "查找消息"
            }];

            $scope.searchDate = [{
                name: "今天",
                value: 1
            }, {
                name: "最近7天",
                value: 7
            }, {
                name: "最近30天",
                value: 30
            }];

            //获取客户列表
            getUserConsultListInfo.save({pageNo: "1", pageSize: "20"}, function (data) {
                $scope.userConsultListInfo = data.userList;
            });

            //获取客服医生列表
            getCSDoctorList.save({}, function (data) {
                $scope.CSList = data.CSList;
            });

            //获取用户的详细聊天记录
            $scope.getUserRecordDetail = function (openid,senderName,fromUserId,toUserId) {
                $scope.currentClickUserName = senderName;
                getUserRecordDetail.save({pageNo:0,pageSize:100,fromUserId:fromUserId,toUserId:toUserId,recordType: "doctor", "openid": openid}, function (data) {
                    $scope.defaultClickUserOpenId = openid;
                    $scope.currentUserConsultRecordDetail = data.records;

                });
            }


            //查询某个客服信息
            $scope.getCSInfoByUserId = function (Object) {

                if (Object == 1000 || Object == 1 || Object == 7 || Object == 30) {
                    CSInfoByUserId.save({dateNum: Object, pageNo: "1", pageSize: "100"}, function (data) {
                        $scope.CSDoctorListInfo = data.records;
                    });
                } else {
                    CSInfoByUserId.save({CSDoctorId: Object, pageNo: "1", pageSize: "100"}, function (data) {
                        $scope.CSDoctorListInfo = data.records;
                    });
                }
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
                    getMessageRecordInfo.save({
                        searchInfo: $scope.info.searchMessageContent,
                        searchType: $scope.messageType,
                        pageNo: "1",
                        pageSize: "100"
                    }, function (data) {
                        $scope.userConsultListInfo = data.userList;
                        $scope.currentUserConsultRecordDetail = data.records;
                    });
                }
            }
            //查找咨询记录
            $scope.setSearchMessageType = function (searchType) {
                $scope.messageType = searchType;
            }

            //查找消息记录（点击全部、图片等）
            $scope.getRecordByOpenId = function (searchRecordType) {
                getUserRecordDetail.save({
                    recordType: searchRecordType,
                    openId:$scope.defaultClickUserOpenId,
                    pageNo: "1",
                    pageSize: "100"
                }, function (data) {
                    $scope.currentUserConsultRecordDetail = data.records;
                });
            }
            //左上角的刷新消息
            $scope.refreshUserList = function () {
                userConsultListInfo.save({pageNo: "1", pageSize: "100"}, function (data) {
                    $scope.userConsultListInfo = data.userList;
                });
            }

            //系统设置
            $scope.systemsetup = {
                show: false
            }
            $scope.systemsetup1 = function() {
                $scope.systemsetup.show = !$scope.systemsetup.show;
            }

        }])