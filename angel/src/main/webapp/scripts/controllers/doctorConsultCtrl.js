angular.module('controllers', ['luegg.directives'])
    .controller('doctorConsultFirstCtrl', ['$scope', '$sce', '$window','getTodayRankingList', 'getOnlineDoctorList',
        'getCommonAnswerList', 'getMyAnswerList', 'GetUserLoginStatus', '$location', 'GetUserRecordList','getMyAnswerModify',
        function ($scope, $sce, $window,getTodayRankingList, getOnlineDoctorList, getCommonAnswerList,
                  getMyAnswerList, GetUserLoginStatus, $location, GetUserRecordList,getMyAnswerModify) {
            $scope.test = "";
            $scope.info = {};
            $scope.socketServer1 = "";
            $scope.socketServer2 = "";

            var currDate = new moment().format("YYYY-MM-DD");
            getTodayRankingList.save({"rankDate": currDate}, function (data) {
                console.log("getTodayRankingList", data);
                if (data.rankListValue.length == 0) {
                    $scope.lockScroll = "false";
                    console.log("当前没有排名");
                } else {
                    $scope.rankListValue = data.rankListValue;
                }
            });

            getOnlineDoctorList.save({"pageNo": "1", "pageSize": "3"}, function (data) {
                console.log("getOnlineDoctorList", data);
            });

            $scope.alreadyJoinPatientConversationContent = [
                {
                    "patientId":"aaaa",
                    "patientName": "frank",
                },
                {
                    "patientId":"wefedfwe",
                    "patientName": "frank",
                }
            ];

            getCommonAnswerList.save({"type": "commonAnswer"}, function (data) {
                console.log("getCommonAnswerList", data);
                if (data.commonAnswer.length == 0) {
                    $scope.lockScroll = "false";
                    console.log("没有添加会话插件");
                } else {
                    $scope.commonAnswer = data.commonAnswer;
                }
            });
            getMyAnswerList.save({"type":"myAnswer"},function(data){
                console.log("getMyAnswerList",data);
                if(data.myAnswer!=null && data.myAnswer.length==0){
                    $scope.lockScroll="false";
                    console.log("没有添加会话插件");
                } else {
                    $scope.myAnswer = data.myAnswer;
                }
            });
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

                        $scope.initConsultSocket1();
                        //$scope.initConsultSocket2();
                    }
                })

                $scope.getAlreadyJoinConsultPatientList();
                $scope.info.effect = "true";
                $scope.glued = true;
                $scope.chooseAlreadyJoinConsultPatientId = "";
                $scope.chooseAlreadyJoinConsultPatient($scope.alreadyJoinPatientConversationContent[0].patientId,
                    $scope.alreadyJoinPatientConversationContent[0].patientName);
            }

            $scope.chooseAlreadyJoinConsultPatient = function (patientId, patientName, page) {
                $scope.chooseAlreadyJoinConsultPatientId = patientId;
                $scope.chooseAlreadyJoinConsultPatientName = patientName;

                GetUserRecordList.save({
                    recordType: "user",
                    pageNo: 2,
                    pageSize: 100,
                    patientId: patientId,
                    patientName: patientName
                }, function (data) {
                    console.log(data.records)
                    $scope.currentAlreadyJoinConsultContent = data.records;
                });
            }

            $scope.sendConsultMessage = function () {

                var consultValMessage = {
                    "type": 0,
                    "content": angular.copy($scope.info.consultMessage),
                    "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                    "senderId": $scope.doctorId,
                    "senderName": $scope.doctorName,
                    "sessionId": 18538
                };

                if (!window.WebSocket) {
                    return;
                }
                if ($scope.socketServer1.readyState == WebSocket.OPEN
                //&& $scope.socketServer2.readyState == WebSocket.OPEN
                ) {
                    $scope.alreadyJoinPatientConversationContent[0].consultValue.push(consultValMessage);
                    $scope.chooseAlreadyJoinConsultPatient($scope.chooseAlreadyJoinConsultPatientId,
                        $scope.chooseAlreadyJoinConsultPatientName);
                    $scope.info.consultMessage = "";

                    console.log(JSON.stringify(consultValMessage));
                    $scope.socketServer1.send(JSON.stringify(consultValMessage));
                    //$scope.socketServer2.send(JSON.stringify(consultValMessage));
                } else {
                    alert("连接没有开启.");
                }
            }

            var filterMediaData = function (val) {
                if (val.type == "2" || val.type == "3") {
                    val.content = $sce.trustAsResourceUrl(val.content);
                }
            }

            document.onkeydown = function () {
                var a = window.event.keyCode;
                if ((a == 13) && (event.ctrlKey)) {
                    if ($scope.info.consultMessage != "") {
                        $scope.sendConsultMessage();
                    }
                }
            };//当onkeydown 事件发生时调用函数

            $scope.closeConsult = function () {
                console.log($scope.info.effect);
                var indexClose = 0;
                $.each($scope.alreadyJoinPatientConversationContent, function (index, value) {
                    if (value.patientId == $scope.chooseAlreadyJoinConsultPatientId) {
                        indexClose = index;
                    }
                })
                $scope.alreadyJoinPatientConversationContent.splice(indexClose, 1);
                $scope.chooseAlreadyJoinConsultPatient($scope.alreadyJoinPatientConversationContent[0].patientId,
                    $scope.alreadyJoinPatientConversationContent[0].patientName);
            }

            $scope.useImgFace = function () {
            }

            $scope.initConsultSocket1 = function () {
                if (!window.WebSocket) {
                    window.WebSocket = window.MozWebSocket;
                }
                if (window.WebSocket) {
                    $scope.socketServer1 = new WebSocket("ws://localhost:2048/ws&distributor&000a444a67b94ca0af513ab6aea5937d" +
                        $scope.doctorId);//cs,user,distributor
                    $scope.socketServer1.onmessage = function (event) {
                        console.log("onmessage" + event.data);
                    };
                    $scope.socketServer1.onopen = function (event) {
                        console.log("onopen" + event.data);
                    };
                    $scope.socketServer1.onclose = function (event) {
                        console.log("onclose" + event.data);
                    };
                } else {
                    alert("你的浏览器不支持！");
                }
            }

            $scope.initConsultSocket2 = function () {
                if (!window.WebSocket) {
                    window.WebSocket = window.MozWebSocket;
                }
                if (window.WebSocket) {
                    $scope.socketServer2 = new WebSocket("ws://localhost:2048/ws&cs&" + $scope.doctorId);//cs,user,distributor
                    $scope.socketServer2.onmessage = function (event) {
                        var ta = document.getElementById('responseText');
                        ta.value = ta.value + '\n' + event.data
                    };
                    $scope.socketServer2.onopen = function (event) {
                        var ta = document.getElementById('responseText');
                        ta.value = "连接开启!";
                    };
                    $scope.socketServer2.onclose = function (event) {
                        var ta = document.getElementById('responseText');
                        ta.value = ta.value + "连接被关闭";
                    };
                } else {
                    alert("你的浏览器不支持！");
                }
            }

            $scope.getAlreadyJoinConsultPatientList = function () {
            }

            window.onload = function () {
                qqFace();
            }

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
            //查看结果
            function replace_em(str) {
                str = str.replace(/\[em_([0-9]*)\]/g, '<img src="http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/' +
                    'dkf%2Fface%2F$1.gif" border="0" />');
                return str;
            }

            //会话排名
            $scope.rankList = {
                show: false
            }
            $scope.toggleRankList = function () {
                $scope.rankList.show = !$scope.rankList.show;
            }
            //系统设置
            $scope.systemsetup = {
                show: false
            }
            $scope.systemsetup1 = function() {
                $scope.systemsetup.show = !$scope.systemsetup.show;
            }
            //等待接入设置
            $scope.waitprocess = {
                show: false
            }
            $scope.waitprocess1 = function() {
                $scope.waitprocess.show = !$scope.waitprocess.show;
            }
            //转接
            $scope.switchover = {
                show: false
            }
            $scope.switchover1 = function() {
                $scope.switchover.show = !$scope.switchover.show;
            }
            var flag,mark,sign,parentIndex,childIndex;
            //flag标记编辑的时候
            //sign标记添加编辑删除的时候是公共回复还是我的回复
            //mark标记删除的时候
            //我的回复
            $scope.myreplyList = {
                show: false
            }
            $scope.myreplyList1 = function () {
                $scope.myreplyList.show = !$scope.myreplyList.show;
                sign = 0;
                mark = 0;
                if ($scope.myreplyList.show == false){
                    $(".rtitle:eq(0)").css('backgroundPosition','0 0');
                    $(".rtitle:eq(0)").css('color','#333');
                }else if($scope.myreplyList.show == true){
                    $(".rtitle:eq(0)").css('backgroundPosition','0 -42px');
                    $(".rtitle:eq(0)").css('color','#08b7c2');
                }
            }
            //我的回复内容
            $scope.mreplyContent1 = function (parentIndex1) {
                $scope.mreplayindex = parentIndex1;
                mark = 1;
                flag = 0;
                parentIndex = parentIndex1;
                $scope.info.editGroup = $scope.myAnswer[parentIndex].name;
                //console.log("editMyGroup",$scope.info.editGroup)
                $(".myreply .group-title").not(parentIndex1).css('backgroundPosition','0 0');
                $(".myreply .group-title").not(parentIndex1).css('color','#333');
                $(".myreply .group-title").eq(parentIndex1).css('backgroundPosition','0 -42px');
                $(".myreply .group-title").eq(parentIndex1).css('color','#08b7c2');
            }
            $scope.editMyContent = function(parentIndex1, childIndex1) {
                flag = 1;
                parentIndex = parentIndex1;
                childIndex = childIndex1;
                $scope.info.editContent = $scope.myAnswer[parentIndex].secondAnswer[childIndex].name;
                $("nobr").not(childIndex1).css('color','#333')
                $(".myreply .group-title").eq(parentIndex1).siblings("ul").find("nobr").eq(childIndex1).css('color','#08b7c2');
            };
            //公共回复
            $scope.pubilcreplyList = {
                show: false
            }
            //回复列表
            $scope.pubilcreplyList1 = function () {
                $scope.pubilcreplyList.show = !$scope.pubilcreplyList.show;
                sign = 1;
                mark = 0;
                if ($scope.pubilcreplyList.show == false){
                    $(".rtitle:eq(1)").css('backgroundPosition','0 0');
                    $(".rtitle:eq(1)").css('color','#333');
                }else if($scope.pubilcreplyList.show == true){
                    $(".rtitle:eq(1)").css('backgroundPosition','0 -42px');
                    $(".rtitle:eq(1)").css('color','#08b7c2');
                }
            }
            //公告回复内容
            $scope.pubilcreplyContent1 = function (parentIndex1) {
                $scope.pubilcreplyindex = parentIndex1;
                mark = 1;
                flag = 0;
                parentIndex = parentIndex1;
                $scope.info.editGroup = $scope.commonAnswer[parentIndex].name;
                $(".pubilcreply .group-title").not(parentIndex1).css('backgroundPosition','0 0');
                $(".pubilcreply .group-title").not(parentIndex1).css('color','#333');
                $(".pubilcreply .group-title").eq(parentIndex1).css('backgroundPosition','0 -42px');
                $(".pubilcreply .group-title").eq(parentIndex1).css('color','#08b7c2');
            }
            //编辑公共内容
            $scope.editCommonContent = function(parentIndex1, childIndex1) {
                flag = 1;
                parentIndex = parentIndex1;
                childIndex = childIndex1;
                $scope.info.editContent = $scope.commonAnswer[parentIndex].secondAnswer[childIndex].name;
                $("nobr").not(childIndex1).css('color','#333')
                $(".pubilcreply .group-title").eq(parentIndex1).siblings("ul").find("nobr").eq(childIndex1).css('color','#08b7c2');
            };
            //添加分组
            $scope.add = function() {
                if(sign == 0){
                    if (mark == 0) {
                        $scope.addgroup = true;
                    } else if (mark == 1) {
                        $scope.addcontent = true;
                    }
                } else if(sign == 1){
                    if (mark == 0) {
                        $scope.addgroup = true;
                    } else if (mark == 1) {
                        $scope.addcontent = true;
                    }
                }
            }
            $scope.addgroup =  false;
            $scope.closeAddGroup = function() {
                $scope.addgroup = false;
            }
            $scope.addGroupSubmit = function () {
                var setGroupContent = {};
                setGroupContent.name = $scope.info.addGroup;
                setGroupContent.secondAnswer=[];
                console.log('addGroup',setGroupContent);
                if(sign == 0 && mark == 0){
                    $scope.myAnswer.push(setGroupContent);
                    getMyAnswerModify.save({answer:$scope.myAnswer,answerType:"myAnswer"}, function (data) {});
                } else if(sign == 1 && mark == 0){
                    $scope.commonAnswer.push(setGroupContent);
                    getMyAnswerModify.save({answer:$scope.commonAnswer,answerType:"commonAnswer"}, function (data) {});
                }
                $scope.addgroup = false;
            }
            //添加内容
            $scope.addcontent = false;
            $scope.closeAddContent = function() {
                $scope.addcontent=false;
            }

            $scope.addContentSubmit = function () {
                var setContent = {};
                setContent.name = $scope.info.addContent;
                console.log('addContent',setContent);
                if(sign == 0 && mark == 1){
                    $scope.myAnswer[parentIndex].secondAnswer.push(setContent);
                    getMyAnswerModify.save({answer:$scope.myAnswer,answerType:"myAnswer"}, function (data) {});
                } else if(sign == 1 && mark == 1){
                    $scope.commonAnswer[parentIndex].secondAnswer.push(setContent);
                    getMyAnswerModify.save({answer:$scope.commonAnswer,answerType:"commonAnswer"}, function (data) {});
                }
                $scope.addcontent=false;
            }
            //编辑分组
            $scope.editgroup =  false;
            $scope.closeEditGroup = function() {
                $scope.editgroup = false;
            }
            $scope.editGroupSubmit = function () {
                /*var changeGroup = {};*/
                var changeGroup = $scope.info.editGroup;
                console.log('changeContent',changeGroup);
                if(sign == 0 && flag == 0){
                    $scope.myAnswer[parentIndex].name = changeGroup;
                    console.log($scope.myAnswer[parentIndex].name)
                    getMyAnswerModify.save({answer:$scope.myAnswer,answerType:"myAnswer"}, function (data) {});
                } else if(sign == 1 && flag == 0){
                    $scope.commonAnswer[parentIndex].name = changeGroup;
                    getMyAnswerModify.save({answer:$scope.commonAnswer,answerType:"commonAnswer"}, function (data) {});

                }
                $scope.editgroup=false;
            }
            //编辑内容
            $scope.editcontent =  false;
            $scope.closeEditContent = function() {
                $scope.editcontent = false;
            }
            $scope.editContentSubmit = function () {
                var changeContent = $scope.info.editContent;
                console.log('changeContent',changeContent);
                if(sign == 0 && flag == 1){
                    $scope.myAnswer[parentIndex].secondAnswer[childIndex].name = changeContent;
                    getMyAnswerModify.save({answer:$scope.myAnswer,answerType:"myAnswer"}, function (data) {});
                } else if(sign == 1 && flag == 1){
                    $scope.commonAnswer[parentIndex].secondAnswer[childIndex].name = changeContent;
                    getMyAnswerModify.save({answer:$scope.commonAnswer,answerType:"commonAnswer"}, function (data) {});
                }
                $scope.editcontent=false;
            }
            $scope.edit = function() {
                if (sign == 0){
                    if (flag == 0) {
                        $scope.editgroup = true;
                    } else if (flag == 1) {
                        $scope.editcontent = true;
                    }
                }else if(sign == 1){
                    if (flag == 0) {
                        $scope.editgroup = true;
                    } else if (flag == 1) {
                        $scope.editcontent = true;
                    }
                }
            }
            //删除
            $scope.remove = function(){
                if(sign == 0){
                    if (flag == 0 && $window.confirm("确定要删除该组回复?")) {
                        $scope.myAnswer.splice(parentIndex, 1);
                        getMyAnswerModify.save({answer:$scope.myAnswer,answerType:"myAnswer"}, function (data) {});
                    }else if(flag == 1 && $window.confirm("确定要删除该回复?")) {
                        $scope.myAnswer[parentIndex].secondAnswer.splice(childIndex, 1);
                        getMyAnswerModify.save({answer:$scope.myAnswer,answerType:"myAnswer"}, function (data) {});

                    }
                }else if(sign == 1){
                    if (flag == 0 && $window.confirm("确定要删除该组回复?")) {
                        $scope.commonAnswer.splice(parentIndex, 1);
                        getMyAnswerModify.save({answer:$scope.commonAnswer,answerType:"commonAnswer"}, function (data) {});
                    }else if(flag == 1 && $window.confirm("确定要删除该回复?")) {
                        $scope.commonAnswer[parentIndex].secondAnswer.splice(childIndex, 1);
                        getMyAnswerModify.save({answer:$scope.commonAnswer,answerType:"commonAnswer"}, function (data) {});
                    }
                }
            }
        }])

    .controller('messageListCtrl', ['$scope', '$log', '$sce', 'getUserConsultListInfo', 'getUserRecordDetail', 'getCSdoctorList', 'CSInfoByUserId', 'getMessageRecordInfo',
        function ($scope, $log, $sce, getUserConsultListInfo, getUserRecordDetail, getCSdoctorList, CSInfoByUserId, getMessageRecordInfo) {

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
                console.log(data);
                $scope.userConsultListInfo = data.userList;
            });

            //获取客服医生列表
            getCSdoctorList.save({}, function (data) {
                console.log(data);
                $scope.CSList = data.CSList;
            });

            //获取用户的详细聊天记录
            $scope.getUserRecordDetail = function (openid,senderName,fromUserId,toUserId) {
                $scope.currentClickUserName = senderName;
                getUserRecordDetail.save({pageNo:0,pageSize:100,fromUserId:fromUserId,toUserId:toUserId,recordType: "doctor", "openid": openid}, function (data) {
                    console.log(data);
                    $scope.defaultClickUserOpenId = openid;
                    $scope.currentUserConsultRecordDetail = data.records;
                });
            }


            //查询某个客服信息
            $scope.getCSInfoByUserId = function (Object) {

                if (Object == 1000 || Object == 1 || Object == 7 || Object == 30) {
                    CSInfoByUserId.save({dateNum: Object, pageNo: "1", pageSize: "100"}, function (data) {
                        console.log(data);
                        $scope.CSDoctorListInfo = data.records;
                    });
                } else {
                    CSInfoByUserId.save({CSDoctorId: Object, pageNo: "1", pageSize: "100"}, function (data) {
                        console.log(data);
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
                    console.log(data);
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