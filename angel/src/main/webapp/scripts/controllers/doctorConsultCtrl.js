angular.module('controllers', ['luegg.directives'])
    .controller('doctorConsultFirstCtrl', ['$scope', '$sce', 'getTodayRankingList', 'getOnlineDoctorList',
        'getCommonAnswerList', 'getMyAnswerList', 'GetUserLoginStatus', '$location', 'GetUserRecordList',
        function ($scope, $sce, getTodayRankingList, getOnlineDoctorList, getCommonAnswerList,
                  getMyAnswerList, GetUserLoginStatus, $location, GetUserRecordList) {
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
                if(data.myAnswer.length==0){
                    $scope.lockScroll="false";
                    console.log("没有添加会话插件");
                } else {
                    $scope.myAnswer = data.myAnswer;
                }
            });
            $scope.alreadyJoinPatientConversationContent = [
                {
                    "patientId":"aaaa","sessionId":4234234234,"fromServer": "120.25.161.33",
                    "patientName": "frank","isOnline":true,"dateTime":"15:20","messageNotSee":true,
                    "consultValue":[{
                        "type": 0,
                        "content": "医生，你好，我有些问题想请教下?",
                        "dateTime": "2016/03/16 15:20:30",
                        "senderId":"aaaa",
                        "senderName":"冯涛",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "你好，请问你现在的情况怎么样，有需要进行帮忙的吗?",
                        "dateTime": "2016/03/16 15:20:31",
                        "senderId":"007ed2077b9c46b7b3efc46470af3136",
                        "senderName":"陈佳科",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "医生，麻烦帮我看下情况，我的肚子很不舒服",
                        "dateTime": "2016/03/16 15:20:32",
                        "senderId":"aaaa",
                        "senderName":"冯涛",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "你好，请问你现在的情况怎么样，有需要进行帮忙的吗?",
                        "dateTime": "2016/03/16 15:20:33",
                        "senderId":"007ed2077b9c46b7b3efc46470af3136",
                        "senderName":"陈佳科",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "医生，麻烦帮我看下情况，我的肚子很不舒服,",
                        "dateTime": "2016/03/16 15:20:34",
                        "senderId":"007ed2077b9c46b7b3efc46470af3136",
                        "senderName":"陈佳科",
                        "sessionId": 4324234423
                    }]
                },
                {
                    "patientId":"wefedfwe","sessionId": 4234234234,"fromServer": "120.25.161.33",
                    "patientName": "frank","isOnline":true,"dateTime":"15:20","messageNotSee":true,
                    "consultValue":[{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 894320432349,
                        "senderId":"fewfewfwe",
                        "senderName":"frank",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 894320432489,
                        "senderId":"fewfewfwe",
                        "senderName":"cjk",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 8943204932489,
                        "senderId":"fewfewfwe",
                        "senderName":"frank",
                        "sessionId": 4324234423
                    }]
                },
                {
                    "patientId":"vewfewf","sessionId": 4234234234,"fromServer": "120.25.161.33",
                    "patientName": "frank","isOnline":true,"dateTime":"15:20","messageNotSee":true,
                    "consultValue":[{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 8943204932489,
                        "senderId":"fewfewfwe",
                        "senderName":"frank",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 8943204932489,
                        "senderId":"fewfewfwe",
                        "senderName":"cjk",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 8943204932489,
                        "senderId":"fewfewfwe",
                        "senderName":"frank",
                        "sessionId": 4324234423
                    }]
                },
                {
                    "patientId":"fwedsfwe","sessionId": 4234234234,"fromServer": "120.25.161.33",
                    "patientName": "frank","isOnline":true,"dateTime":"15:20","messageNotSee":false,
                    "consultValue":[{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 8943204932489,
                        "senderId":"fewfewfwe",
                        "senderName":"frank",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 8943204932489,
                        "senderId":"fewfewfwe",
                        "senderName":"cjk",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 8943204932489,
                        "senderId":"fewfewfwe",
                        "senderName":"frank",
                        "sessionId": 4324234423
                    }]
                },
                {
                    "patientId":"fwefdsfwe","sessionId": 4234234234,"fromServer": "120.25.161.33",
                    "patientName": "frank","isOnline":true,"dateTime":"15:20","messageNotSee":true,
                    "consultValue":[{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 8943204932489,
                        "senderId":"fewfewfwe",
                        "senderName":"frank",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 8943204932489,
                        "senderId":"fewfewfwe",
                        "senderName":"cjk",
                        "sessionId": 4324234423
                    },{
                        "type": 0,
                        "content": "hello,my name is cjk",
                        "dateTime": 8943204932489,
                        "senderId":"fewfewfwe",
                        "senderName":"frank",
                        "sessionId": 4324234423
                    }]
                }
            ];

            $scope.doctorConsultFirst = function () {
                var routePath = "/ap/doctor/consultBBBBBB" + $location.path();
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
                var progress = 1;
                var timeArray = ["2016/03/16 15:20:33",
                    "2016/03/16 15:20:34",
                    "2016/03/16 15:20:35",
                    "2016/03/16 15:20:36",
                    "2016/03/16 15:20:37",
                    "2016/03/16 15:20:38",
                    "2016/03/16 15:20:39",
                    "2016/03/16 15:20:40",
                    "2016/03/16 15:20:41",
                    "2016/03/16 15:20:42"];

                var timer = setTimeout(function(){
                    var val = {
                        "type": 0,
                        "content": "我的测试项目"+progress,
                        "dateTime": timeArray[progress],
                        "senderId":"aaaa",
                        "senderName":"陈佳科",
                        "sessionId": 4324234423
                    };
                    progress++;
                    filterMediaData(val);
                    $scope.alreadyJoinPatientConversationContent[0].consultValue.push(val);
                    $scope.chooseAlreadyJoinConsultPatient($scope.chooseAlreadyJoinConsultPatientId,$scope.chooseAlreadyJoinConsultPatientName);
                    $scope.$apply();
                },1000)
            }

            $scope.chooseAlreadyJoinConsultPatient = function (patientId, patientName, page) {
                $scope.chooseAlreadyJoinConsultPatientId = patientId;
                $scope.chooseAlreadyJoinConsultPatientName = patientName;

                GetUserRecordList.save({
                    recordType: "user",
                    pageNo: "2",
                    pageSize: "100",
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
                    $scope.socketServer1 = new WebSocket("ws://120.25.161.33:2048/ws&distributor&" +
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
            //我的回复
            $scope.myreplyList = {
                show: false
            }
            $scope.myreplyList1 = function () {
                $scope.myreplyList.show = !$scope.myreplyList.show;
            }
            //我的回复内容
            $scope.mreplyContent = {
                show: false
            }
            $scope.mreplyContent1 = function (index) {
                //$scope.mreplyContent.show = !$scope.mreplyContent.show;
                $scope.mreplayindex = index;
            }
            //公共回复
            $scope.pubilcreplyList = {
                show: false
            }
            $scope.pubilcreplyList1 = function () {
                $scope.pubilcreplyList.show = !$scope.pubilcreplyList.show;
            }
            //公告回复内容
            $scope.pubilcreplyContent = {
                show: false
            }
            $scope.pubilcreplyContent1 = function (index) {
                $scope.pubilcreplyindex = index;
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
        }])
    .controller('messageListCtrl', ['$scope', '$log', '$sce', 'userConsultListInfo', 'getUserRecordDetail', 'getCSdoctorList', 'CSInfoByUserId', 'getMessageRecordInfo',
        function ($scope, $log, $sce, userConsultListInfo, getUserRecordDetail, getCSdoctorList, CSInfoByUserId, getMessageRecordInfo) {

            $scope.info = {};

            $scope.searchMessageContent = "";

            $scope.searchMessageType = "";

            $scope.searchDate = [{
                name: "所有时间",
                value: 1000
            }, {
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
            userConsultListInfo.save({pageNo: "1", pageSize: "100"}, function (data) {
                console.log(data);
                $scope.userConsultListInfo = data.userList;
            });

            //获取客服医生列表
            getCSdoctorList.save({}, function (data) {
                console.log(data);
                $scope.CSList = data.CSList;
            });

            //获取用户的详细聊天记录
            $scope.getUserRecordDetail = function (openid) {
                getUserRecordDetail.save({recordType: "doctor", "openid": openid}, function (data) {
                    console.log(data);
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
                var messageType = "";
                $scope.info.searchMessageType == "查找客户" ? messageType = "user" : messageType = "message";
                getMessageRecordInfo.save({
                    searchInfo: $scope.info.searchMessageContent,
                    searchType: messageType,
                    pageNo: "1",
                    pageSize: "100"
                }, function (data) {
                    $scope.userConsultListInfo = data.userList;
                    $scope.currentUserConsultRecordDetail = data.records;
                });
            }
        }])