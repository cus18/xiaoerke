angular.module('controllers', ['luegg.directives'])
    .controller('doctorConsultFirstCtrl', ['$scope', '$sce', '$window', '$stateParams', 'GetTodayRankingList',
        'GetOnlineDoctorList', 'GetAnswerValueList', 'GetDoctorLoginStatus','GetUserLoginStatus',
        '$location', 'GetCurrentUserHistoryRecord', 'GetMyAnswerModify', 'GetCurrentUserConsultListInfo',
        'TransferToOtherCsUser', 'SessionEnd', 'GetWaitJoinList', 'React2Transfer', 'CancelTransfer', '$upload',
        'GetFindTransferSpecialist', 'GetRemoveTransferSpecialist', 'GetAddTransferSpecialist', 'GetFindAllTransferSpecialist',
        'CreateTransferSpecialist', '$state', 'GetSystemTime', 'GetUserSessionTimesByUserId', 'GetCustomerLogByOpenID', 'SaveCustomerLog',
        'SearchIllnessList', 'ModifyUserConsultNum', 'SearchBabyInfo', 'SaveReturnService','GetConfig',
        function ($scope, $sce, $window, $stateParams, GetTodayRankingList, GetOnlineDoctorList, GetAnswerValueList,
                  GetDoctorLoginStatus,GetUserLoginStatus, $location, GetCurrentUserHistoryRecord, GetMyAnswerModify,
                  GetCurrentUserConsultListInfo, TransferToOtherCsUser, SessionEnd, GetWaitJoinList, React2Transfer, CancelTransfer, $upload,
                  GetFindTransferSpecialist, GetRemoveTransferSpecialist, GetAddTransferSpecialist, GetFindAllTransferSpecialist,
                  CreateTransferSpecialist, $state, GetSystemTime, GetUserSessionTimesByUserId, GetCustomerLogByOpenID, SaveCustomerLog,
                  SearchIllnessList, ModifyUserConsultNum, SearchBabyInfo, SaveReturnService,GetConfig) {
            //初始化info参数
            $scope.info = {
                effect: "true",
                illness: "",//诊断
                show: "",//表现
                result: "",//处理
                birthday: "",//生日
                transferRemark: "",
                searchCsUserValue: "",
                selectedSpecialist: "",
                selectedIllnessList: "",
                selectedBabyName: "",
                role: {
                    "distributor": "接诊员",
                    "consultDoctor": "专业医生"
                }
            };
            $scope.customerssUrl="";
            $scope.systemInfo = {};
            $scope.loadingFlag = false;
            $scope.socketServerFirst = "";
            $scope.socketServerSecond = "";
            GetConfig.save({}, function (data) {
                $scope.systemInfo = data.publicSystemInfo;
                $scope.firstAddress = $scope.systemInfo.firstAddress;
                $scope.secondAddress= $scope.systemInfo.secondAddress;
            })
            $scope.alreadyJoinPatientConversation = []; //已经加入会话的用户数据，一个医生可以有多个对话的用户，这些用户的数据，都保存在此集合中 乱码
            $scope.currentUserConversation = {}; //医生与当前正在进行对话用户的聊天数据，医生在切换不同用户时，数据变更到切换的用户上来。
            $scope.waitJoinNum = 0; //医生待接入的用户数，是动态变化的数
            $scope.glued = true; //angular滚动条的插件预制参数，让对话滚动条，每次都定位底部，当新的聊天数据到达时
            var umbrellaCustomerList = "75cefafe00364bbaaaf7b61089994e22,3b91fe8b7ce143918012ef3ab4baf1e0,00032bd90d724d0sa63a4d6esa0e8dbf";
            //各个子窗口的开关变量
            $scope.showFlag = {
                rankList: false,
                systemSetup: false,
                waitProcess: false,
                switchOver: false,
                myReplyList: false,
                publicReplyList: false,
                diagnosisReplyList: false,
                diagnosticImgReplyList: false,
                replyContent: true,
                advisoryContent: false,
                magnifyImg: false,
                specialistList: false,
                specialistTransfer: false,
                userTable: false,
                recentTable: false,
                addConsultTable: false,
                historyTable: false
            };
            $scope.searchFlag = false;
            $scope.tapImgButton = function (key, value) {
                $scope.showFlag[key] = !$scope.showFlag[key];
                $scope.imageSrc = value;
            };
            $scope.loseConnectionFirstFlag = false;
            $scope.loseConnectionSecondFlag = false;
            $scope.addWordFlag = true;
            $scope.addImgFlag = false;
            var acceptOperationFlag = false;
            var waitProcessLock = false;
            var heartBeatFirstNum = 3;
            var heartBeatSecondNum = 3;

            //初始化医生端登录，建立socket链接，获取基本信息
            $scope.doctorConsultInit = function () {
                // heartBeatCheckPay();
                $scope.getQQExpression();
                //var routePath = "/doctor/consultBBBBBB" + $location.path();
                //GetDoctorLoginStatus.save({routePath: routePath}, function (data) {
                GetDoctorLoginStatus.save({}, function (data) {
                    $scope.pageLoading = false;
                    if (data.status == "failure") {
                        $state.go("doctorConsultLogin");
                        //window.location.href = "localhost";
                    } else if (data.status == "success") {
                        $scope.doctorId = data.userId;
                        $scope.doctorName = data.userName;
                        $scope.doctorPhone = data.userPhone;
                        $scope.userType = data.userType;

                        //创建与平台的socket连接
                        if ($scope.socketServerFirst == "" || $scope.socketServerFirst.readyState != 1) {
                            $scope.initConsultSocketFirst();
                        }
                        if ($scope.socketServerSecond == "" || $scope.socketServerSecond.readyState != 1) {
                            $scope.initConsultSocketSecond();
                        }

                        getIframeSrc();
                        //getHistoryConsultContent();
                        //searchBabyInfo();

                        //获取通用回复列表
                        GetAnswerValueList.save({"type": "commonAnswer"}, function (data) {
                            if (data.result == "success") {
                                var answerData = JSON.parse(data.answerValue);
                                $scope.commonAnswer = answerData.commonAnswer;
                            } else {
                                $scope.commonAnswer = [];
                            }
                        });

                        //获取我的回复列表
                        GetAnswerValueList.save({"type": "myAnswer"}, function (data) {
                            if (data.result == "success") {
                                var answerData = JSON.parse(data.answerValue);
                                $scope.myAnswer = answerData.myAnswer;
                            } else {
                                $scope.myAnswer = [];
                            }
                        });

                        //获取我的诊断列表
                        GetAnswerValueList.save({"type": "diagnosis"}, function (data) {
                            if (data.result == "success") {
                                var answerData = JSON.parse(data.answerValue);
                                $scope.diagnosis = answerData.diagnosis;
                            } else {
                                $scope.diagnosis = [];
                            }
                        });

                        //获取我的诊断图片
                        GetAnswerValueList.save({"type": "diagnosticImg"}, function (data) {
                            if (data.result == "success") {
                                var answerData = JSON.parse(data.answerValue);
                                $scope.diagnosticImg = answerData.diagnosticImg;
                            } else {
                                $scope.diagnosticImg = [];
                            }
                        });
                        //查找所属科室
                        SearchIllnessList.save(function (data) {
                            var addIllness = {
                                'value': 'addIllness',
                                'illness': '添加'
                            };
                            data.illnessList.push(addIllness);
                            $scope.illnessList = data.illnessList;
                        });

                        //查询专科列表
                        GetFindAllTransferSpecialist.save({}, function (data) {
                            $scope.selectedSpecialistType = data.data;
                        });
                        $scope.refreshWaitJoinUserList();

                        if ($stateParams.action == "createUserSession") {
                            var patientId = $stateParams.userId;
                            var patientName = "";
                            GetCurrentUserConsultListInfo.save({
                                userType: $scope.userType, csUserId: $scope.doctorId,
                                pageNo: 1, pageSize: 10000
                            }, function (data) {
                                if (data.alreadyJoinPatientConversation != "" && data.alreadyJoinPatientConversation != undefined) {
                                    $scope.alreadyJoinPatientConversation = data.alreadyJoinPatientConversation;
                                    $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                                        if (value.patientId == patientId) {
                                            patientName = value.patientName;
                                        }
                                        $.each(value.consultValue, function (index1, value1) {
                                            filterMediaData(value1);
                                        })
                                    });
                                    $scope.chooseAlreadyJoinConsultPatient(patientId, patientName);
                                }
                            })
                        } else if ($stateParams.action == "") {
                            getAlreadyJoinConsultPatientList();
                        }
                        setInterval(sessionCheck, 20000);
                    }
                });
            };

            //每20秒，检测一次医生跟平台的会话是否失效
            var sessionCheck = function () {
                //var routePath = "/doctor/consultBBBBBB" + $location.path();
                //GetUserLoginStatus.save({routePath: routePath}, function (data) {
                //    $scope.pageLoading = false;
                //    if (data.status == "9") {
                //        window.location.href = data.redirectURL;
                //    } else if (data.status == "8") {
                //        window.location.href = data.redirectURL + "?targeturl=" + routePath;
                //    }
                //})
            };

            //公共点击按钮，用来触发弹出对应的子窗口
            $scope.tapShowButton = function (type) {
                $.each($scope.showFlag, function (key) {
                    if (key == type) {
                        if (type == "waitProcess") {
                            if (!waitProcessLock) {
                                $scope.showFlag[key] = !$scope.showFlag[key];
                            }
                        } else {
                            $scope.showFlag[key] = !$scope.showFlag[key];
                        }
                        if (type == "replyContent") {
                            if ($scope.showFlag.replyContent == false) {
                                $scope.showFlag.advisoryContent = true;
                            } else {
                                $scope.showFlag.advisoryContent = false;
                            }
                        }
                        else if (type == "advisoryContent") {
                            if ($scope.showFlag.advisoryContent == false) {
                                $scope.showFlag.replyContent = true;
                            } else {
                                $scope.showFlag.replyContent = false;
                            }
                        }
                        if (!$scope.showFlag[key]) {
                            if (type == "myReplyList") {
                                $scope.myReplyIndex = -1;
                                $scope.myReplySecondIndex = -1;
                            } else if (type == "publicReplyList") {
                                $scope.publicReplyIndex = -1;
                                $scope.publicReplySecondIndex = -1;
                            } else if (type == "diagnosisReplyList") {
                                $scope.diagnosisReplyIndex = -1;
                                $scope.diagnosisReplySecondIndex = -1;
                            } else if (type == "diagnosticImgReplyList") {
                                $scope.diagnosticImgReplyIndex = -1;
                                $scope.diagnosticImgReplySecondIndex = -1;
                            }
                        } else {
                            if (type == "rankList") {
                                //已接入会话咨询医生今日排名
                                $scope.refreshRankList();
                            } else if (type == "switchOver") {
                                //获取在线医生列表
                                $scope.refreshOnLineCsUserList();
                            } else if (type == "waitProcess") {
                                //获取待接入用户列表
                                $scope.refreshWaitJoinUserList();
                            } else if (type == "specialistList") {
                                //获取待接入用户列表
                                getFindTransferSpecialist();
                            }
                        }
                    }
                })
            };

            /**转接功能区**/
            $scope.acceptTransfer = function () {
                if (!acceptOperationFlag) {
                    $scope.tapShowButton('waitProcess');
                    var waitJoinChooseUserList = "";
                    $.each($scope.waitJoinUserList, function (index, value) {
                        if (value.chooseFlag) {
                            waitJoinChooseUserList = waitJoinChooseUserList + value.forwardSessionId + ";"
                        }
                    });
                    acceptOperationFlag = true;
                    waitProcessLock = true;
                    React2Transfer.save({
                        operation: "accept",
                        forwardSessionIds: waitJoinChooseUserList
                    }, function (data) {
                        acceptOperationFlag = false;
                        waitProcessLock = false;
                        if (data.result == "success") {
                            //将转接成功的用户，都加入会话列表中来
                            var indexClose = 0;
                            $.each($scope.waitJoinUserList, function (index, value) {
                                if (value.chooseFlag) {
                                    $scope.currentUserConversation = {
                                        'patientId': value.userId,
                                        'source': value.source,
                                        'serverAddress': value.serverAddress,
                                        'sessionId': value.sessionId,
                                        'isOnline': true,
                                        'dateTime': value.sessionCreateTime,
                                        'messageNotSee': true,
                                        'number': value.consultNum,
                                        'patientName': value.userName,
                                        'consultValue': [],
                                        "notifyType": value.notifyType
                                    };
                                    var consultValue = {
                                        'type': value.type,
                                        'content': value.messageContent,
                                        'dateTime': value.messageDateTime,
                                        'senderId': value.userId,
                                        'senderName': value.userName,
                                        'sessionId': value.sessionId,
                                        "notifyType": value.notifyType
                                    };
                                    filterMediaData(consultValue);
                                    $scope.currentUserConversation.consultValue.push(consultValue);
                                    $scope.alreadyJoinPatientConversation.push($scope.currentUserConversation);
                                }
                            });
                            $scope.refreshWaitJoinUserList();
                            $scope.chooseAlreadyJoinConsultPatient($scope.alreadyJoinPatientConversation[0].patientId,
                                $scope.alreadyJoinPatientConversation[0].patientName);
                            $scope.seeMoreConversationMessage(5);
                        }
                    });
                }
            };

            var rejectOperationFlag = false;
            $scope.rejectTransfer = function () {
                if (!rejectOperationFlag) {
                    $scope.tapShowButton('waitProcess');
                    var waitJoinChooseUserList = "";
                    $.each($scope.waitJoinUserList, function (index, value) {
                        if (value.chooseFlag) {
                            waitJoinChooseUserList = waitJoinChooseUserList + value.forwardSessionId + ";"
                        }
                    });
                    rejectOperationFlag = true;
                    React2Transfer.save({
                        operation: "rejected",
                        forwardSessionIds: waitJoinChooseUserList
                    }, function (data) {
                        rejectOperationFlag = false;
                        if (data.result == "success") {
                            $scope.refreshWaitJoinUserList();
                        }
                    });
                }
            };

            //选择转接对象
            $scope.chooseTransferCsUser = function (csUserId, csUserName) {
                $scope.transferCsUserId = csUserId;
                $scope.csTransferUserName = csUserName;
                $scope.transferUserName = angular.copy($scope.currentUserConversation.patientName);
            };

            $scope.transferToCsUser = function () {
                $scope.tapShowButton('switchOver');
                TransferToOtherCsUser.save({
                    doctorId: $scope.transferCsUserId,
                    sessionId: $scope.currentUserConversation.sessionId,
                    remark: $scope.info.transferRemark
                }, function (data) {
                    if (data.result == "success") {
                        //转接请求成功后，在接诊员侧，保留了此会话，只到被转接的医生收到为止，
                        // 才将会话拆除，在此过程中，允许接诊员，取消转接。
                    } else if (data.result == "failure") {
                        alert("转接会话给" + $scope.csTransferUserName + "失败，请转接给其他医生");
                    } else if (data.result == "transferring") {
                        alert("与用户" + $scope.transferUserName + "的会话正在被转接中，不能再次转接");
                    }
                });
            };

            $scope.searchCsUser = function () {
                if ($scope.info.searchCsUserValue != "") {
                    $scope.searchFlag = true;
                } else {
                    $scope.searchFlag = false;
                }
            };

            $scope.cancelTransfer = function (sessionId, toCsUserId, remark) {
                CancelTransfer.save({sessionId: sessionId, toCsUserId: toCsUserId, remark: remark}, function (data) {
                    if (data.result == "success") {
                        //删除取消通知
                        var indexClose = 0;
                        $.each($scope.currentUserConversation.consultValue, function (index, value) {
                            if (value.remark == remark && value.toCsUserId == toCsUserId) {
                                indexClose = index;
                            }
                        });
                        $scope.currentUserConversation.consultValue.splice(indexClose, 1);
                    }
                });
            };

            //按照科室排序
            $scope.getWaitTransferSpecialist = function () {
                $scope.alreadyJoinTransferSpecialist = [];
                GetFindTransferSpecialist.save({sortBy: "order"}, function (data) {
                    $.each(data.data, function (index, value) {
                        value.selectedAll = false;
                        $scope.alreadyJoinTransferSpecialist.push(value);
                    });
                });
            };

            //查询所有的转移列表
            var getFindTransferSpecialist = function () {
                $scope.alreadyJoinTransferSpecialist = [];
                GetFindTransferSpecialist.save({sortBy: "no"}, function (data) {
                    $.each(data.data, function (index, value) {
                        value.selectedAll = false;
                        $scope.alreadyJoinTransferSpecialist.push(value);
                    });
                });
            };

            //添加转接科室确定
            $scope.addTransferSpecialistSubmit = function () {
                $scope.showFlag.specialistTransfer = false;
                //添加转接科室
                var consultData = {
                    sessionId: angular.copy($scope.currentUserConversation.sessionId),
                    department: angular.copy($scope.info.selectedSpecialist.departmentName)
                };
                GetAddTransferSpecialist.save({consultData: consultData}, function (data) {
                    if (data.status == "exist") {
                        alert("用户已添加过转诊");
                    } else if (data.status == "success") {
                        alert("添加转诊成功");
                    }
                });
            };
            $scope.closeSpecialistTransfer = function () {
                $scope.showFlag.specialistTransfer = false;
            };

            //删除转接科室中的一个
            $scope.disposeTransferSpecialist = function (index) {
                $scope.showFlag.specialistList = false;
                var specialistId = [$scope.alreadyJoinTransferSpecialist[index]];
                if ($window.confirm("确定要删除该转诊内容?")) {
                    GetRemoveTransferSpecialist.save({
                        content: specialistId,
                        status: 'ongoing',
                        delFlag: '1'
                    }, function (data) {
                        if (data.status == 'failure') {
                            alert("删除失败");
                        } else if (data.status == "success") {
                            alert("删除成功");
                            $scope.alreadyJoinTransferSpecialist.splice(index, 1);
                            getFindTransferSpecialist();
                        }
                    });
                }

            };

            //分诊员发起一个针对用户的会话
            $scope.createOneSpecialistPatient = function (index, department) {
                $scope.showFlag.specialistList = false;
                CreateTransferSpecialist.save({specialistPatientContent: $scope.alreadyJoinTransferSpecialist[index]}, function (data) {
                    if (data.status == "success") {
                        var patientId = data.userId;
                        var patientName = data.userName;
                        GetCurrentUserConsultListInfo.save({
                            userType: $scope.userType,
                            csUserId: $scope.doctorId,
                            pageNo: 1,
                            pageSize: 10000
                        }, function (data) {
                            if (data.alreadyJoinPatientConversation != "" && data.alreadyJoinPatientConversation != undefined) {
                                $scope.alreadyJoinPatientConversation = data.alreadyJoinPatientConversation;
                                $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                                    if(value.consultNum == 0){
                                        value.messageNotSee = false;
                                    }else if(value.consultNum != 0){
                                        value.messageNotSee = true;
                                    }
                                    value.number = value.consultNum;
                                    if (value.patientId == patientId) {
                                        patientName = value.patientName;
                                        value.transferDepartment = '需要转给' + department;
                                    }
                                    $.each(value.consultValue, function (index1, value1) {
                                        filterMediaData(value1);
                                    })
                                });
                                $scope.chooseAlreadyJoinConsultPatient(patientId, patientName);
                            }
                        });
                        $scope.showFlag.specialistList = false;
                        getFindTransferSpecialist();

                    } else if (data.status == "failure") {
                        if (data.result == "failure") {
                            alert("无法发起会话，请稍后重试");
                        } else if (data.result == "existTransferSession") {
                            alert("此用户正有会话处于转接状态，无法向其发起会话，请稍后重试");
                        } else if (data.result == "noLicenseTransfer") {
                            alert("对不起，你没有权限，抢断一个正在咨询用户的会话");
                        } else if (data.result == "exceed48Hours") {
                            alert("对不起，用户咨询已经超过了48小时，无法再向其发起会话");
                        }
                    } else if (data.status == "ongoing") {
                        alert("此用户正与" + data.csuserName + "会话中，无法向其发起会话，请稍后重试")
                    } else if (data.status == "complete") {
                        alert("当前会话已转出，请刷新列表")
                    }
                })
            };
            /**转接功能区**/

            /**会话操作区**/

                //初始化socket链接
            $scope.initConsultSocketFirst = function () {
                if (!window.WebSocket) {
                    window.WebSocket = window.MozWebSocket;
                }
                if (window.WebSocket) {
                    if ($scope.userType == "distributor") {
                        $scope.socketServerFirst = new WebSocket("ws://" + $scope.firstAddress + ":2048/ws&" +
                            "distributor&" + $scope.doctorId);//cs,user,distributor
                    } else if ($scope.userType == "consultDoctor") {
                        $scope.socketServerFirst = new WebSocket("ws://" + $scope.firstAddress + ":2048/ws&" +
                            "cs&" + $scope.doctorId);//cs,user,distributor
                    }

                    $scope.socketServerFirst.onmessage = function (event) {
                        var consultData = JSON.parse(event.data);
                        if (consultData.type == 4) {
                            processNotifyMessage(consultData);
                            $scope.$apply();
                        } else if (consultData.type == 5) {
                            heartBeatFirstNum = 3;
                        } else {
                            filterMediaData(consultData);
                            processPatientSendMessage(consultData);
                            $scope.triggerqqVoice();
                            $scope.$apply();
                        }
                    };

                    $scope.socketServerFirst.onopen = function (event) {
                        console.log("onopen", event.data);
                        //启动心跳监测
                        heartBeatCheckFirst();
                    };

                    $scope.socketServerFirst.onclose = function (event) {
                        console.log("onclose", event.data);
                    };

                } else {
                    alert("你的浏览器不支持！");
                }
            };
            $scope.initConsultSocketSecond = function () {
                if (!window.WebSocket) {
                    window.WebSocket = window.MozWebSocket;
                }
                if (window.WebSocket) {
                    if ($scope.userType == "distributor") {
                        $scope.socketServerSecond = new WebSocket("ws://" + $scope.secondAddress + ":2048/ws&" +
                            "distributor&" + $scope.doctorId);//cs,user,distributor
                    } else if ($scope.userType == "consultDoctor") {
                        $scope.socketServerSecond = new WebSocket("ws://" + $scope.secondAddress + ":2048/ws&" +
                            "cs&" + $scope.doctorId);//cs,user,distributor
                    }

                    $scope.socketServerSecond.onmessage = function (event) {
                        var consultData = JSON.parse(event.data);
                        if (consultData.type == 4) {
                            processNotifyMessage(consultData);
                            $scope.$apply();
                        } else if (consultData.type == 5) {
                            heartBeatSecondNum = 3;
                        } else {
                            filterMediaData(consultData);
                            processPatientSendMessage(consultData);
                            $scope.triggerqqVoice();
                            $scope.$apply();
                        }
                    };

                    $scope.socketServerSecond.onopen = function (event) {
                        console.log("onopen", event.data);
                        //启动心跳监测
                        heartBeatCheckSecond();
                    };

                    $scope.socketServerSecond.onclose = function (event) {
                    };

                } else {
                    alert("你的浏览器不支持！");
                }
            };
            $scope.messageList = function () {
                clearInterval($scope.heartBeatFirstId);
                clearInterval($scope.heartBeatSecondId);
                $state.go('messageList');
            };
            //心跳
            var heartBeatCheckFirst = function () {
                //启动定时器，周期性的发送心跳信息
                $scope.heartBeatFirstId = setInterval(sendHeartBeatFirst, 2000);
            };
            var sendHeartBeatFirst = function () {
                var heartBeatMessage = {
                    "type": 5,
                    "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                    "csUserId": angular.copy($scope.doctorId)
                };
                heartBeatFirstNum--;
                if (heartBeatFirstNum < 0) {
                    heartBeatFirstNum = 3;
                    $scope.loseConnectionFirstFlag = true;
                    $scope.initConsultSocketFirst();
                } else {
                    $scope.loseConnectionFirstFlag = false;
                    if ($scope.socketServerFirst != "" && $scope.socketServerFirst.readyState == 1) {
                        $scope.socketServerFirst.send(JSON.stringify(heartBeatMessage));
                    }
                }
                $scope.$apply();
            };
            var heartBeatCheckSecond = function () {
                //启动定时器，周期性的发送心跳信息
                $scope.heartBeatSecondId = setInterval(sendHeartBeatSecond, 2000);
            };
            var sendHeartBeatSecond = function () {
                var heartBeatMessage = {
                    "type": 5,
                    "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                    "csUserId": angular.copy($scope.doctorId)
                };
                heartBeatSecondNum--;
                if (heartBeatSecondNum < 0) {
                    heartBeatSecondNum = 3;
                    $scope.loseConnectionSecondFlag = true;
                    $scope.initConsultSocketSecond();
                } else {
                    $scope.loseConnectionSecondFlag = false;
                    if ($scope.socketServerSecond != "" && $scope.socketServerSecond.readyState == 1) {
                        $scope.socketServerSecond.send(JSON.stringify(heartBeatMessage));
                    }
                }
                $scope.$apply();
            };
            //处理用户按键事件
            document.onkeydown = function () {
                if (window.event.keyCode == 13) {
                    if ($("#saytext").val().replace(/\s+/g, "") != "") {
                        $scope.sendConsultMessage();
                    }
                }
            };//当onkeydown 事件发生时调用函数

            //向用户发送咨询消息
            $scope.sendConsultMessage = function () {
                if ($("#saytext").val().replace(/\s+/g, "") != "") {
                    if (!window.WebSocket) {
                        return;
                    }
                    GetSystemTime.save(function (data) {
                        $scope.info.consultMessage = "";
                        var sayTextFlag = processSayTextFlag($("#saytext").val());
                        var consultContent = $("#saytext").val();
                        $("#saytext").val('');
                        if (sayTextFlag != "noFlag") {
                            var valueData = consultContent.split("####");
                            consultContent = valueData[0];
                        }
                        if ($scope.currentUserConversation.serverAddress == "" || $scope.currentUserConversation.serverAddress == null) {
                            $scope.currentUserConversation.serverAddress = $scope.firstAddress;
                            if ($scope.socketServerFirst.readyState != WebSocket.OPEN) {
                                $scope.currentUserConversation.serverAddress = $scope.secondAddress;
                                if ($scope.socketServerSecond.readyState != WebSocket.OPEN) {
                                    alert("连接没有开启！");
                                }
                            }
                        }
                        if ($scope.currentUserConversation.serverAddress == $scope.firstAddress) {
                            if ($scope.socketServerFirst.readyState == WebSocket.OPEN) {
                                var consultValMessage = "";
                                if ($scope.userType == "distributor") {
                                    var consultValMessage = {
                                        "type": 0,
                                        "content": "分诊" + $scope.doctorName + "：" + consultContent,
                                        "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                        "senderId": angular.copy($scope.doctorId),
                                        "senderName": angular.copy($scope.doctorName),
                                        "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                        "consultFlag": sayTextFlag
                                    };
                                } else if ($scope.userType == "consultDoctor") {
                                    if (umbrellaCustomerList.indexOf($scope.doctorId) > -1) {
                                        var consultValMessage = {
                                            "type": 0,
                                            "content": "宝护伞客服：" + consultContent,
                                            "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                            "senderId": angular.copy($scope.doctorId),
                                            "senderName": angular.copy($scope.doctorName),
                                            "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                            "consultFlag": sayTextFlag
                                        };
                                    } else {
                                        var consultValMessage = {
                                            "type": 0,
                                            "content": $scope.doctorName + "医生：" + consultContent,
                                            "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                            "senderId": angular.copy($scope.doctorId),
                                            "senderName": angular.copy($scope.doctorName),
                                            "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                            "consultFlag": sayTextFlag
                                        };
                                    }
                                }
                                $scope.socketServerFirst.send(emotionSendFilter(JSON.stringify(consultValMessage)));
                                consultValMessage.content = $sce.trustAsHtml(replace_em(angular.copy(consultContent)));
                                $("#saytext").val('');
                                updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                            } else {
                                alert("连接没有开启.");
                            }
                        }
                        else if ($scope.currentUserConversation.serverAddress == $scope.secondAddress) {
                            if ($scope.socketServerSecond.readyState == WebSocket.OPEN) {
                                var consultValMessage = "";
                                if ($scope.userType == "distributor") {
                                    var consultValMessage = {
                                        "type": 0,
                                        "content": "分诊" + $scope.doctorName + "：" + consultContent,
                                        "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                        "senderId": angular.copy($scope.doctorId),
                                        "senderName": angular.copy($scope.doctorName),
                                        "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                        "consultFlag": sayTextFlag
                                    };
                                } else if ($scope.userType == "consultDoctor") {
                                    if (umbrellaCustomerList.indexOf($scope.doctorId) > -1) {
                                        var consultValMessage = {
                                            "type": 0,
                                            "content": "宝护伞客服：" + consultContent,
                                            "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                            "senderId": angular.copy($scope.doctorId),
                                            "senderName": angular.copy($scope.doctorName),
                                            "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                            "consultFlag": sayTextFlag
                                        };
                                    } else {
                                        var consultValMessage = {
                                            "type": 0,
                                            "content": $scope.doctorName + "医生：" + consultContent,
                                            "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                            "senderId": angular.copy($scope.doctorId),
                                            "senderName": angular.copy($scope.doctorName),
                                            "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                            "consultFlag": sayTextFlag
                                        };
                                    }
                                }

                                $scope.socketServerSecond.send(emotionSendFilter(JSON.stringify(consultValMessage)));
                                consultValMessage.content = $sce.trustAsHtml(replace_em(angular.copy(consultContent)));
                                $("#saytext").val('');
                                updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                            } else {
                                alert("连接没有开启.");
                            }
                        }
                        else {
                            if ($scope.currentUserConversation.serverAddress == "" || $scope.currentUserConversation.serverAddress == null) {
                                if ($scope.socketServerFirst != "") {
                                    $scope.currentUserConversation.serverAddress = angular.copy($scope.firstAddress);
                                } else if ($scope.socketServerSecond != "") {
                                    $scope.currentUserConversation.serverAddress = angular.copy($scope.secondAddress);
                                }
                                $scope.sendConsultMessage();
                            }
                        }
                    });
                }
            };

            //为无效咨询打标记
            var processSayTextFlag = function (data) {
                var flag = "noFlag";
                if (data.indexOf("####") != -1) {
                    var textValue = data.split("####");
                    flag = textValue[1];
                }
                return flag;
            };

            //向用户发送咨询图片
            $scope.uploadFiles = function ($files, fileType) {
                var dataValue = {
                    "fileType": fileType,
                    "senderId": $scope.doctorId,
                    "sessionId": angular.copy($scope.currentUserConversation.sessionId)
                };
                var dataJsonValue = JSON.stringify(dataValue);
                for (var i = 0; i < $files.length; i++) {
                    var file = $files[i];
                    $scope.upload = $upload.upload({
                        url: 'consult/h5/uploadMediaFile',
                        data: encodeURI(dataJsonValue),
                        file: file
                    }).progress(function (evt) {
                    }).success(function (data, status, headers, config) {
                        if (data.source == "wxcxqm") {
                            var consultValMessage = {
                                "type": 1,
                                "content": data.showFile,
                                "wscontent": data.WS_File,
                                "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                                "senderId": angular.copy($scope.doctorId),
                                "senderName": angular.copy($scope.doctorName),
                                "sessionId": angular.copy($scope.currentUserConversation.sessionId)
                            };
                        } else {
                            var consultValMessage = {
                                "type": 1,
                                "content": data.showFile,
                                "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                                "senderId": angular.copy($scope.doctorId),
                                "senderName": angular.copy($scope.doctorName),
                                "sessionId": angular.copy($scope.currentUserConversation.sessionId)
                            };
                        }
                        if (!window.WebSocket) {
                            return;
                        }
                        if ($scope.currentUserConversation.serverAddress == $scope.firstAddress) {
                            if ($scope.socketServerFirst.readyState == WebSocket.OPEN) {
                                $scope.socketServerFirst.send(JSON.stringify(consultValMessage));
                                $scope.initConsultSocketFirst();
                                updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                            } else {
                                alert("连接没有开启.");
                            }
                        } else if ($scope.currentUserConversation.serverAddress == $scope.secondAddress) {
                            if ($scope.socketServerSecond.readyState == WebSocket.OPEN) {
                                $scope.socketServerSecond.send(JSON.stringify(consultValMessage));
                                $scope.initConsultSocketSecond();
                                updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                            } else {
                                alert("连接没有开启.");
                            }
                        } else {
                            if ($scope.currentUserConversation.serverAddress == "" || $scope.currentUserConversation.serverAddress == null) {
                                if ($scope.socketServerFirst != "") {
                                    if ($scope.socketServerFirst.readyState == WebSocket.OPEN) {
                                        $scope.socketServerFirst.send(JSON.stringify(consultValMessage));
                                        updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                                    } else {
                                        alert("连接没有开启.");
                                    }
                                } else if ($scope.socketServerSecond != "") {
                                    if ($scope.socketServerSecond.readyState == WebSocket.OPEN) {
                                        $scope.socketServerSecond.send(JSON.stringify(consultValMessage));
                                        updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                                    } else {
                                        alert("连接没有开启.");
                                    }
                                }
                            }
                        }

                    });

                }
            };
            //关闭跟某个用户的会话
            var closeConsultLock = false;
            $scope.closeConsult = function () {
                if (!closeConsultLock) {
                    closeConsultLock = true;
                    SessionEnd.get({
                        sessionId: $scope.currentUserConversation.sessionId,
                        userId: $scope.currentUserConversation.patientId
                    }, function (data) {
                        closeConsultLock = false;
                        if (data.result == "success") {
                            var indexClose = 0;
                            $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                                if (value.patientId == $scope.chooseAlreadyJoinConsultPatientId) {
                                    indexClose = index;
                                }
                            });
                            $scope.alreadyJoinPatientConversation.splice(indexClose, 1);
                            $scope.chooseAlreadyJoinConsultPatientSessionTimes = '';

                            if ($scope.alreadyJoinPatientConversation.length != 0) {
                                $scope.chooseAlreadyJoinConsultPatient($scope.alreadyJoinPatientConversation[0].patientId,
                                    $scope.alreadyJoinPatientConversation[0].patientName);
                            } else {
                                $scope.currentUserConversation = {};
                            }
                        } else {
                            alert("会话关闭失败，请重试");
                        }
                    })
                } else {
                    alert("正在关闭当前用户的会话，请稍后，等关闭此用户成功后，再关闭其他用户");
                }
            };
            //在通话列表中，选取一个用户进行会话
            $scope.chooseAlreadyJoinConsultPatient = function (patientId, patientName, sessionId) {
                $scope.chooseAlreadyJoinConsultPatientId = patientId;
                $scope.chooseAlreadyJoinConsultPatientName = patientName;
                $scope.chooseAlreadyJoinConsultPatientsessionId = sessionId;
                GetUserSessionTimesByUserId.get({userId: patientId}, function (data) {
                    $scope.chooseAlreadyJoinConsultPatientSessionTimes = '是' + data.userSessionTimes + '次接入';
                });
                /*消息数置零*/
                ModifyUserConsultNum.save({sessionId:sessionId},function(data){
                    console.log(data.consultNum)
                });
                getIframeSrc();
                var updateFlag = false;
                $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                    if (value.patientId == patientId) {
                        $scope.currentUserConversation = "";
                        $scope.currentUserConversation = value;
                        value.messageNotSee = false;
                        value.number = 0;
                        updateFlag = true;
                    }
                });
                if (!updateFlag) {
                    GetCurrentUserHistoryRecord.save({
                        userId: patientId,
                        dateTime: moment().format('YYYY-MM-DD HH:mm:ss'),
                        pageSize: 100
                    }, function (data) {
                        if (data.consultDataList != "") {
                            $.each(data.consultDataList, function (index, value) {
                                $scope.currentUserConversation.consultValue.splice(0, 0, value);
                            });
                        }
                    })
                }
            };
            //触发转接声音
            $scope.triggerVoice = function () {
                var audio = document.createElement('audio');
                var source = document.createElement('source');
                source.type = "audio/mpeg";
                source.src = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/18.ogg";
                source.autoplay = "autoplay";
                source.controls = "controls";
                audio.appendChild(source);
                audio.play();
            };
            //触发信息声音
            $scope.triggerqqVoice = function () {
                var audio = document.createElement('audio');
                var source = document.createElement('source');
                source.type = "audio/mpeg";
                source.src = "http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2F5097.wav";
                source.autoplay = "autoplay";
                source.controls = "controls";
                audio.appendChild(source);
                audio.play();
            };
            $scope.getQQExpression = function () {
                $('#face').qqFace({
                    id: 'facebox',
                    assign: 'saytext',
                    path: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F'
                });
            };
            var replace_em = function (str) {
                str = str.replace(/\</g, '&lt;');
                str = str.replace(/\>/g, '&gt;');
                str = str.replace(/\n/g, '<br/>');
                str = str.replace(/\[em_([0-9]*)\]/g, '<img src="http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F$1.gif" border="0" />');
                return str;
            };
            //查看更多的用户历史消息
            $scope.seeMoreConversationMessage = function (size) {
                var mostFarCurrentConversationDateTime = moment().format("YYYY-MM-DD HH:mm:ss");
                if ($scope.currentUserConversation.consultValue[0] != undefined) {
                    mostFarCurrentConversationDateTime = $scope.currentUserConversation.consultValue[0].dateTime;
                }
                GetCurrentUserHistoryRecord.save({
                    userId: $scope.currentUserConversation.patientId,
                    dateTime: mostFarCurrentConversationDateTime,
                    pageSize: size
                }, function (data) {
                    if (data.consultDataList != "") {
                        $.each(data.consultDataList, function (index, value) {
                            filterMediaData(value);
                            $scope.currentUserConversation.consultValue.splice(0, 0, value);
                        });
                    }
                })
            };
            /**会话操作区**/
                //更新咨询医生当日咨询用户数的排名列表
            $scope.refreshRankList = function () {
                var currDate = new moment().format("YYYY-MM-DD");
                GetTodayRankingList.save({"rankDate": currDate}, function (data) {
                    $scope.info.rankListValue = data.rankListValue;
                });
            };
            //获取在线的咨询医生列表
            $scope.refreshOnLineCsUserList = function () {
                $scope.searchFlag = false;
                $scope.info.searchCsUserValue = "";
                GetOnlineDoctorList.save({}, function (data) {
                    $scope.info.onLineCsUserList = data.onLineCsUserList;
                });
            };
            //获取待接入会话用户列表t
            $scope.refreshWaitJoinUserList = function () {
                GetWaitJoinList.save({csUserId: $scope.doctorId}, function (data) {
                    $scope.waitJoinNum = data.waitJoinNum;
                    $scope.waitJoinUserList = data.waitJoinList;
                })
            };

            /***回复操作区**/
                //我的回复内容
            $scope.tapMyReplyContent = function (parentIndex) {
                $scope.showFlag.myReplyList = true;
                $scope.showFlag.publicReplyList = false;
                $scope.showFlag.diagnosisReplyList = false;
                $scope.showFlag.diagnosticImgReplyList = false;
                if ($scope.myReplyIndex == parentIndex) {
                    $scope.myReplyIndex = -1;
                    $scope.myReplySecondIndex = -1;
                    $scope.info.editGroup = "";
                    $scope.info.editContent = "";
                } else {
                    $scope.myReplyIndex = parentIndex;
                    $scope.myReplySecondIndex = -1;
                    $scope.info.editGroup = $scope.myAnswer[parentIndex].name;
                    $scope.info.editContent = "";
                }
            };
            $scope.tapEditMyContent = function (parentIndex, childIndex) {
                $scope.myReplySecondIndex = childIndex;
                $scope.info.editContent = $scope.myAnswer[parentIndex].secondAnswer[childIndex].name;
            };
            $scope.chooseMyContent = function (parentIndex, childIndex) {
                $scope.info.consultMessage = angular.copy($scope.myAnswer[parentIndex].secondAnswer[childIndex].name);
            };
            //公共回复内容
            $scope.chooseCommonContent = function (parentIndex, childIndex) {
                $scope.info.consultMessage = angular.copy($scope.commonAnswer[parentIndex].secondAnswer[childIndex].name);
            };
            $scope.tapPublicReplyContent = function (parentIndex) {
                if ($scope.publicReplyIndex == parentIndex) {
                    $scope.publicReplyIndex = -1;
                    $scope.publicReplySecondIndex = -1;
                    $scope.info.editGroup = "";
                    $scope.info.editContent = "";
                } else {
                    $scope.publicReplyIndex = parentIndex;
                    $scope.publicReplySecondIndex = -1;
                    $scope.info.editGroup = $scope.commonAnswer[parentIndex].name;
                    $scope.info.editContent = "";
                }
            };
            $scope.tapEditCommonContent = function (parentIndex, childIndex) {
                $scope.showFlag.myReplyList = false;
                $scope.showFlag.publicReplyList = true;
                $scope.showFlag.diagnosisReplyList = false;
                $scope.showFlag.diagnosticImgReplyList = false;
                $scope.publicReplySecondIndex = childIndex;
                $scope.info.editContent = $scope.commonAnswer[parentIndex].secondAnswer[childIndex].name;
            };
            //诊断回复内容
            $scope.chooseDiagnosisContent = function (parentIndex, childIndex) {
                $scope.info.consultMessage = angular.copy($scope.diagnosis[parentIndex].secondAnswer[childIndex].name);
            };
            $scope.tapDiagnosisReplyContent = function (parentIndex) {
                if ($scope.diagnosisReplyIndex == parentIndex) {
                    $scope.diagnosisReplyIndex = -1;
                    $scope.diagnosisReplySecondIndex = -1;
                    $scope.info.editGroup = "";
                    $scope.info.editContent = "";
                } else {
                    $scope.diagnosisReplyIndex = parentIndex;
                    $scope.diagnosisReplySecondIndex = -1;
                    $scope.info.editGroup = $scope.diagnosis[parentIndex].name;
                    $scope.info.editContent = "";
                }
            };
            $scope.tapEditDiagnosisContent = function (parentIndex, childIndex) {
                $scope.showFlag.myReplyList = false;
                $scope.showFlag.publicReplyList = false;
                $scope.showFlag.diagnosisReplyList = true;
                $scope.showFlag.diagnosticImgReplyList = false;
                $scope.diagnosisReplySecondIndex = childIndex;
                $scope.info.editContent = $scope.diagnosis[parentIndex].secondAnswer[childIndex].name;
            };
            //图片回复内容
            $scope.chooseDiagnosticImgContent = function (parentIndex, childIndex) {
                $scope.info.consultMessage = angular.copy($scope.diagnosticImg[parentIndex].secondAnswer[childIndex].name);
            };
            $scope.tapDiagnosticImgReplyContent = function (parentIndex) {
                if ($scope.diagnosticImgReplyIndex == parentIndex) {
                    $scope.diagnosticImgReplyIndex = -1;
                    $scope.diagnosticImgReplySecondIndex = -1;
                    $scope.info.editGroup = "";
                    $scope.info.editContent = "";
                } else {
                    $scope.diagnosticImgReplyIndex = parentIndex;
                    $scope.diagnosticImgReplySecondIndex = -1;
                    $scope.info.editGroup = $scope.diagnosticImg[parentIndex].name;
                    $scope.info.editContent = "";
                }
            };
            $scope.tapEditDiagnosticImgContent = function (parentIndex, childIndex) {
                $scope.showFlag.myReplyList = false;
                $scope.showFlag.publicReplyList = false;
                $scope.showFlag.diagnosisReplyList = false;
                $scope.showFlag.diagnosticImgReplyList = true;
                $scope.diagnosticImgReplySecondIndex = childIndex;
                $scope.info.editContent = $scope.diagnosticImg[parentIndex].secondAnswer[childIndex].name;
            };
            //添加分组
            $scope.add = function () {
                $scope.info.addGroup = '';
                $scope.info.addContent = '';
                if ($scope.showFlag.myReplyList) {
                    if ($scope.myReplyIndex == -1 || $scope.myReplyIndex == undefined) {
                        $scope.addGroupFlag = true;
                        $scope.addContentFlag = false;
                    } else {
                        $scope.addGroupFlag = false;
                        $scope.addContentFlag = true;
                    }
                }
                if ($scope.showFlag.publicReplyList) {
                    if ($scope.publicReplyIndex == -1 || $scope.publicReplyIndex == undefined) {
                        $scope.addGroupFlag = true;
                        $scope.addContentFlag = false;
                    } else {
                        $scope.addGroupFlag = false;
                        $scope.addContentFlag = true;
                    }
                }
                if ($scope.showFlag.diagnosisReplyList) {
                    if ($scope.diagnosisReplyIndex == -1 || $scope.diagnosisReplyIndex == undefined) {
                        $scope.addGroupFlag = true;
                        $scope.addContentFlag = false;
                    } else {
                        $scope.addWordFlag = true;
                        $scope.addImgFlag = false;
                        $scope.addGroupFlag = false;
                        $scope.addContentFlag = true;
                    }
                }
                if ($scope.showFlag.diagnosticImgReplyList) {
                    if ($scope.diagnosticImgReplyIndex == -1 || $scope.diagnosticImgReplyIndex == undefined) {
                        $scope.addGroupFlag = true;
                        $scope.addContentFlag = false;
                    } else {
                        $scope.addWordFlag = false;
                        $scope.addImgFlag = true;
                        $scope.addGroupFlag = false;
                        $scope.addContentFlag = true;
                    }
                }
            };
            $scope.closeAddGroup = function () {
                $scope.info.addGroup = '';
                $scope.info.addContent = '';
                $scope.addGroupFlag = false;
            };
            $scope.addGroupSubmit = function () {
                var setGroupContent = {};
                setGroupContent.name = $scope.info.addGroup;
                setGroupContent.secondAnswer = [];
                if ($scope.showFlag.myReplyList) {
                    $scope.myAnswer.push(setGroupContent);
                    saveMyAnswer();
                }
                if ($scope.showFlag.publicReplyList) {
                    $scope.commonAnswer.push(setGroupContent);
                    saveCommonAnswer();
                }
                if ($scope.showFlag.diagnosisReplyList) {
                    $scope.diagnosis.push(setGroupContent);
                    saveDiagnosis();
                }
                if ($scope.showFlag.diagnosticImgReplyList) {
                    $scope.diagnosticImg.push(setGroupContent);
                    saveDiagnosticImg();
                }
                $scope.addGroupFlag = false;
            };
            //添加内容
            $scope.closeAddContent = function () {
                $scope.addContentFlag = false;
            };
            $scope.addContentSubmit = function () {
                var setContent = {};
                setContent.name = $scope.info.addContent;
                if ($scope.showFlag.myReplyList) {
                    $scope.myAnswer[$scope.myReplyIndex].secondAnswer.push(setContent);
                    saveMyAnswer();
                }
                if ($scope.showFlag.publicReplyList) {
                    $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer.push(setContent);
                    saveCommonAnswer();
                }
                if ($scope.showFlag.diagnosisReplyList) {
                    $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer.push(setContent);
                    saveDiagnosis();
                }
                if ($scope.showFlag.diagnosticImgReplyList) {
                    $scope.diagnosticImg[$scope.diagnosticImgReplyIndex].secondAnswer.push(setContent);
                    saveDiagnosticImg();
                }
                $scope.addContentFlag = false;
            };
            //编辑分组
            $scope.closeEditGroup = function () {
                $scope.editGroupFlag = false;
            };
            $scope.closeEditContent = function () {
                $scope.editContentFlag = false;
            };
            $scope.edit = function () {
                if ($scope.showFlag.myReplyList) {
                    if ($scope.myReplyIndex != -1 && $scope.myReplyIndex != undefined) {
                        if ($scope.myReplySecondIndex == -1 || $scope.myReplyIndex == undefined) {
                            $scope.editGroupFlag = true;
                            $scope.editContentFlag = false;
                        } else {
                            $scope.editGroupFlag = false;
                            $scope.editContentFlag = true;
                        }
                    }
                }
                if ($scope.showFlag.publicReplyList) {
                    if ($scope.publicReplyIndex != -1 && $scope.publicReplyIndex != undefined) {
                        if ($scope.publicReplySecondIndex == -1 || $scope.publicReplyIndex == undefined) {
                            $scope.editGroupFlag = true;
                            $scope.editContentFlag = false;
                        } else {
                            $scope.editGroupFlag = false;
                            $scope.editContentFlag = true;
                        }
                    }
                }
                if ($scope.showFlag.diagnosisReplyList) {
                    if ($scope.diagnosisReplyIndex != -1 && $scope.diagnosisReplyIndex != undefined) {
                        if ($scope.diagnosisReplySecondIndex == -1 || $scope.diagnosisReplyIndex == undefined) {
                            $scope.editGroupFlag = true;
                            $scope.editContentFlag = false;
                        } else {
                            $scope.editGroupFlag = false;
                            $scope.editContentFlag = true;
                        }
                    }
                }
                if ($scope.showFlag.diagnosticImgReplyList) {
                    if ($scope.diagnosticImgReplyIndex != -1 && $scope.diagnosticImgReplyIndex != undefined) {
                        if ($scope.diagnosticImgReplySecondIndex == -1 || $scope.diagnosticImgReplyIndex == undefined) {
                            $scope.editGroupFlag = true;
                            $scope.editContentFlag = false;
                        } else {
                            $scope.editGroupFlag = false;
                            $scope.editContentFlag = true;
                        }
                    }
                }
            };
            $scope.editGroupSubmit = function () {
                if ($scope.showFlag.myReplyList) {
                    $scope.myAnswer[$scope.myReplyIndex].name = $scope.info.editGroup;
                    saveMyAnswer();
                }
                if ($scope.showFlag.publicReplyList) {
                    $scope.commonAnswer[$scope.publicReplyIndex].name = $scope.info.editGroup;
                    saveCommonAnswer();
                }
                if ($scope.showFlag.diagnosisReplyList) {
                    $scope.diagnosis[$scope.diagnosisReplyIndex].name = $scope.info.editGroup;
                    saveDiagnosis();
                }
                $scope.editGroupFlag = false;
            };
            $scope.editContentSubmit = function () {
                if ($scope.showFlag.myReplyList) {
                    $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex].name = $scope.info.editContent;
                    saveMyAnswer();
                }
                if ($scope.showFlag.publicReplyList) {
                    $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex].name = $scope.info.editContent;
                    saveCommonAnswer();
                }
                if ($scope.showFlag.diagnosisReplyList) {
                    $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex].name = $scope.info.editContent;
                    saveDiagnosis();
                }
                $scope.editContentFlag = false;
            };
            //删除
            $scope.remove = function () {
                if ($scope.showFlag.myReplyList) {
                    if ($scope.myReplyIndex != -1 && $scope.myReplyIndex != undefined) {
                        if ($scope.myReplySecondIndex == -1 || $scope.myReplyIndex == undefined) {
                            if ($window.confirm("确定要删除该组回复?")) {
                                $scope.myAnswer.splice($scope.myReplyIndex, 1);
                            }
                        } else {
                            if ($window.confirm("确定要删除该回复?")) {
                                $scope.myAnswer[$scope.myReplyIndex].secondAnswer.splice($scope.myReplySecondIndex, 1);
                            }
                        }
                        saveMyAnswer();
                    }
                }
                if ($scope.showFlag.publicReplyList) {
                    if ($scope.publicReplyIndex != -1 && $scope.publicReplyIndex != undefined) {
                        if ($scope.publicReplySecondIndex == -1 || $scope.publicReplyIndex == undefined) {
                            if ($window.confirm("确定要删除该组回复?")) {
                                $scope.commonAnswer.splice($scope.publicReplyIndex, 1);
                            }
                        } else {
                            if ($window.confirm("确定要删除该回复?")) {
                                $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer.splice($scope.publicReplySecondIndex, 1);
                            }
                        }
                        saveCommonAnswer();
                    }
                }
                if ($scope.showFlag.diagnosisReplyList) {
                    if ($scope.diagnosisReplyIndex != -1 && $scope.diagnosisReplyIndex != undefined) {
                        if ($scope.diagnosisReplySecondIndex == -1 || $scope.diagnosisReplyIndex == undefined) {
                            if ($window.confirm("确定要删除该组回复?")) {
                                $scope.diagnosis.splice($scope.diagnosisReplyIndex, 1);
                            }
                        } else {
                            if ($window.confirm("确定要删除该回复?")) {
                                $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer.splice($scope.diagnosisReplySecondIndex, 1);
                            }
                        }
                        saveDiagnosis();
                    }
                }
                if ($scope.showFlag.diagnosticImgReplyList) {
                    if ($scope.diagnosticImgReplyIndex != -1 && $scope.diagnosticImgReplyIndex != undefined) {
                        if ($scope.diagnosticImgReplySecondIndex == -1 || $scope.diagnosticImgReplyIndex == undefined) {
                            if ($window.confirm("确定要删除该组回复?")) {
                                $scope.diagnosticImg.splice($scope.diagnosticImgReplyIndex, 1);
                            }
                        } else {
                            if ($window.confirm("确定要删除该回复?")) {
                                $scope.diagnosticImg[$scope.diagnosticImgReplyIndex].secondAnswer.splice($scope.diagnosticImgReplySecondIndex, 1);
                            }
                        }
                        saveDiagnosticImg();
                    }
                }
            };
            //回复的排序
            $scope.moveUp = function () {
                if ($scope.showFlag.myReplyList) {
                    if ($scope.myReplyIndex != -1 && $scope.myReplyIndex != undefined) {
                        if ($scope.myReplySecondIndex > 0) {
                            var changeAnswerContent = $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex - 1];
                            $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex - 1] = $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex];
                            $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex] = changeAnswerContent;
                        } else if ($scope.myReplySecondIndex == -1 && $scope.myReplyIndex > 0) {
                            var changeAnswerGroup = $scope.myAnswer[$scope.myReplyIndex - 1];
                            $scope.myAnswer[$scope.myReplyIndex - 1] = $scope.myAnswer[$scope.myReplyIndex];
                            $scope.myAnswer[$scope.myReplyIndex] = changeAnswerGroup;
                        }
                        saveMyAnswer();
                    }
                }
                if ($scope.showFlag.publicReplyList) {
                    if ($scope.publicReplyIndex != -1 && $scope.publicReplyIndex != undefined) {
                        if ($scope.publicReplySecondIndex > 0) {
                            var changeAnswerContent = $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex - 1];
                            $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex - 1] = $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex];
                            $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex] = changeAnswerContent;
                        } else if ($scope.publicReplySecondIndex == -1 && $scope.publicReplyIndex > 0) {
                            var changeAnswerGroup = $scope.commonAnswer[$scope.publicReplyIndex - 1];
                            $scope.commonAnswer[$scope.publicReplyIndex - 1] = $scope.commonAnswer[$scope.publicReplyIndex];
                            $scope.commonAnswer[$scope.publicReplyIndex] = changeAnswerGroup;
                        }
                        saveCommonAnswer();
                    }
                }
                if ($scope.showFlag.diagnosisReplyList) {
                    if ($scope.diagnosisReplyIndex != -1 && $scope.diagnosisReplyIndex != undefined) {
                        if ($scope.diagnosisReplySecondIndex > 0) {
                            var changeAnswerContent = $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex - 1];
                            $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex - 1] = $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex];
                            $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex] = changeAnswerContent;
                        } else if ($scope.diagnosisReplySecondIndex == -1 && $scope.diagnosisReplyIndex > 0) {
                            var changeAnswerGroup = $scope.diagnosis[$scope.diagnosisReplyIndex - 1];
                            $scope.diagnosis[$scope.diagnosisReplyIndex - 1] = $scope.diagnosis[$scope.diagnosisReplyIndex];
                            $scope.diagnosis[$scope.diagnosisReplyIndex] = changeAnswerGroup;
                        }
                        saveDiagnosis();
                    }
                }
            };
            $scope.moveDown = function () {
                if ($scope.showFlag.myReplyList) {
                    if ($scope.myReplyIndex != -1 && $scope.myReplyIndex != undefined) {
                        if ($scope.myReplySecondIndex >= 0 && $scope.myReplySecondIndex < $scope.myAnswer[$scope.myReplyIndex].secondAnswer.length - 1) {
                            var changeAnswerContent = $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex + 1];
                            $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex + 1] = $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex];
                            $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex] = changeAnswerContent;
                        } else if ($scope.myReplySecondIndex == -1 && $scope.myReplyIndex < $scope.myAnswer.length - 1) {
                            var changeAnswerGroup = $scope.myAnswer[$scope.myReplyIndex + 1];
                            $scope.myAnswer[$scope.myReplyIndex + 1] = $scope.myAnswer[$scope.myReplyIndex];
                            $scope.myAnswer[$scope.myReplyIndex] = changeAnswerGroup;
                        }
                        saveMyAnswer();
                    }
                }
                if ($scope.showFlag.publicReplyList) {
                    if ($scope.publicReplyIndex != -1 && $scope.publicReplyIndex != undefined) {
                        if ($scope.publicReplySecondIndex >= 0 && $scope.publicReplySecondIndex < $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer.length - 1) {
                            var changeAnswerContent = $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex + 1];
                            $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex + 1] = $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex];
                            $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex] = changeAnswerContent;
                        } else if ($scope.publicReplySecondIndex == -1 && $scope.publicReplyIndex < $scope.commonAnswer.length - 1) {
                            var changeAnswerGroup = $scope.commonAnswer[$scope.publicReplyIndex + 1];
                            $scope.commonAnswer[$scope.publicReplyIndex + 1] = $scope.commonAnswer[$scope.publicReplyIndex];
                            $scope.commonAnswer[$scope.publicReplyIndex] = changeAnswerGroup;
                        }
                        saveCommonAnswer();
                    }
                }
                if ($scope.showFlag.diagnosisReplyList) {
                    if ($scope.diagnosisReplyIndex != -1 && $scope.diagnosisReplyIndex != undefined) {
                        if ($scope.diagnosisReplySecondIndex >= 0 && $scope.diagnosisReplySecondIndex < $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer.length - 1) {
                            var changeAnswerContent = $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex + 1];
                            $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex + 1] = $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex];
                            $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex] = changeAnswerContent;
                        } else if ($scope.diagnosisReplySecondIndex == -1 && $scope.diagnosisReplyIndex < $scope.diagnosis.length - 1) {
                            var changeAnswerGroup = $scope.diagnosis[$scope.diagnosisReplyIndex + 1];
                            $scope.diagnosis[$scope.diagnosisReplyIndex + 1] = $scope.diagnosis[$scope.diagnosisReplyIndex];
                            $scope.diagnosis[$scope.diagnosisReplyIndex] = changeAnswerGroup;
                        }
                        saveDiagnosis();
                    }
                }
            };
            //保存我的回复
            var saveMyAnswer = function () {
                GetMyAnswerModify.save({answer: $scope.myAnswer, answerType: "myAnswer"}, function (data) {
                });
            };
            //保存公共回复
            var saveCommonAnswer = function () {
                GetMyAnswerModify.save({answer: $scope.commonAnswer, answerType: "commonAnswer"}, function (data) {
                });
            };
            //保存诊断回复
            var saveDiagnosis = function () {
                GetMyAnswerModify.save({answer: $scope.diagnosis, answerType: "diagnosis"}, function (data) {
                });
            };
            //保存图片回复
            var saveDiagnosticImg = function () {
                GetMyAnswerModify.save({answer: $scope.diagnosticImg, answerType: "diagnosticImg"}, function (data) {
                });
            };
            /***回复操作区**/
            /***咨询服务**/
            /*//根据openid获取历史咨询
             var getHistoryConsultContent = function () {
             $scope.historyConsult = '';
             GetCustomerLogByOpenID.save({openid:$scope.currentUserConversation.patientId}, function (data) {
             $scope.historyConsult = data.logList;
             });
             };
             //初始化宝宝的信息$scope.currentUserConversation.patientId
             $scope.babyNameList=[];
             var searchBabyInfo =function (){
             SearchBabyInfo.save({openid:$scope.currentUserConversation.patientId}, function (data) {
             if(data.babyList == ""){
             var addBabyName = {
             name:'添加',
             value:'addBabyInfo'
             };
             $scope.babyNameList.push(addBabyName);
             }
             $scope.babyNameList = data.babyList;
             console.log("$scope.babyNameList",$scope.babyNameList);
             });
             };
             $scope.backgroundAdd = false;
             $scope.backgroundAddTime = false;
             $scope.recommendDoctorFlag = true;
             //添加诊断记录
             $scope.addDiagnosisRecords = function () {
             if($scope.info.result == ''){
             alert("疾病不能为空！");
             return;
             }
             SaveCustomerLog.save({
             openid:$scope.currentUserConversation.patientId,
             create_date:$scope.todayTime,
             illness:$scope.info.illness,
             sections:$scope.info.selectedIllnessList,
             customerID:$scope.doctorId,
             id:$scope.info.selectedIllnessList.id,
             show:$scope.info.show,
             result:$scope.info.result
             }, function (data) {
             if(data.type == 1){
             $scope.backgroundAdd = true;
             getHistoryConsultContent();
             $scope.info.result='';
             $scope.info.show='';
             $scope.info.illness='';
             }else{
             alertD("保存失败,请联系技术人员");
             }
             });
             };
             //取消保存
             $scope.cancelSave = function () {
             $scope.info.result='';
             $scope.info.show='';
             $scope.info.illness='';
             };
             //添加定时回访
             $scope.addDiagnosisTiming = function () {
             SaveReturnService.save({
             openid:$scope.currentUserConversation.patientId,
             customerID:$scope.doctorId
             }, function (data) {
             if(data.type == 1){
             $scope.backgroundAddTime = true;
             }
             });
             };

             // 获取当前的时间
             $scope.todayTime = '';
             var newTime = function(){
             var d = new Date();
             var a = moment().format();
             $scope.todayTime = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
             };

             $scope.userTableMore = "查看更多";
             $scope.tapUserTable = function (key) {
             $scope.showFlag[key] = !$scope.showFlag[key];
             if($scope.showFlag[key]){
             $scope.userTableMore = "收起更多";
             }else{
             $scope.userTableMore = "查看更多";
             }
             };
             $scope.recentTableMore = "查看更多";
             $scope.tapRecentTable = function (key) {
             $scope.showFlag[key] = !$scope.showFlag[key];
             if($scope.showFlag[key]){
             $scope.recentTableMore = "收起更多";
             }else{
             $scope.recentTableMore = "查看更多";
             }
             };
             $scope.addConsultTableMore = "查看更多";
             $scope.tapAddConsultTable = function (key) {
             newTime();
             $scope.showFlag[key] = !$scope.showFlag[key];
             if($scope.showFlag[key]){
             $scope.addConsultTableMore = "收起更多";
             }else{
             $scope.addConsultTableMore = "查看更多";
             }
             };
             $scope.historyTableMore = "查看更多";
             $scope.tapHistoryTable = function (key) {
             $scope.showFlag[key] = !$scope.showFlag[key];
             if($scope.showFlag[key]){
             $scope.historyTableMore = "收起更多";
             }else{
             $scope.historyTableMore = "查看更多";
             }
             };*/
            /***咨询服务**/
            var getIframeSrc = function () {
                var newSrc = $(".advisory-content").attr("src");
                $(".advisory-content").attr("src", "");
                $(".advisory-content").attr("src", newSrc);
            };
            //日期转换
            $scope.transformDate = function (dateTime) {
                var dateValue = new moment(dateTime).format("HH:mm:ss");
                return dateValue;
            };
            //得到已经加入会话的病人的列表
            var getAlreadyJoinConsultPatientList = function () {
                //获取跟医生的会话还保存的用户列表
                GetCurrentUserConsultListInfo.save({
                    userType: $scope.userType,
                    csUserId: $scope.doctorId,
                    pageNo: 1,
                    pageSize: 10000
                }, function (data) {
                    if (data.alreadyJoinPatientConversation != "" && data.alreadyJoinPatientConversation != undefined) {
                        $scope.alreadyJoinPatientConversation = data.alreadyJoinPatientConversation;
                        $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                            if(value.consultNum == 0){
                                value.messageNotSee = false;
                            }else if(value.consultNum != 0){
                                value.messageNotSee = true;
                            }
                            value.number = value.consultNum;
                            $.each(value.consultValue, function (index1, value1) {
                                filterMediaData(value1);
                            });
                        });
                        var patientId = angular.copy($scope.alreadyJoinPatientConversation[0].patientId);
                        var patientName = angular.copy($scope.alreadyJoinPatientConversation[0].patientName);
                        $scope.chooseAlreadyJoinConsultPatient(patientId, patientName);
                    }
                })
            };

            //处理用户发送过来的会话消息
            var processPatientSendMessage = function (conversationData) {
                var chooseFlag = false;
                var currentConsultValue = {
                    'type': conversationData.type,
                    'content': conversationData.content,
                    'dateTime': conversationData.dateTime,
                    'senderId': conversationData.senderId,
                    'senderName': conversationData.senderName,
                    'sessionId': conversationData.sessionId,
                    'number': conversationData.consultNum
                };
                var messageNotSee;
                if(conversationData.consultNum == 0){
                    messageNotSee = false;
                }else if(conversationData.consultNum != 0){
                    messageNotSee = true;
                }
                if (JSON.stringify($scope.currentUserConversation) == '{}') {
                    $scope.currentUserConversation = {
                        'patientId': conversationData.senderId,
                        'source': conversationData.source,
                        'serverAddress': conversationData.serverAddress,
                        'sessionId': conversationData.sessionId,
                        'messageNotSee': messageNotSee,
                        'isOnline': true,
                        'dateTime': conversationData.dateTime,
                        'notifyType': conversationData.notifyType,
                        'patientName': conversationData.senderName,
                        'consultValue': []
                    };
                    $scope.currentUserConversation.consultValue.push(currentConsultValue);
                    chooseFlag = true;
                }
                updateAlreadyJoinPatientConversationFromPatient(conversationData);
                if (chooseFlag) {
                    $scope.chooseAlreadyJoinConsultPatient(angular.copy(currentConsultValue.senderId),
                        angular.copy(currentConsultValue.senderName));
                    getIframeSrc();
                }
            };
            //启动一个监控消息状态的定时器
            /*var setIntervalTimers = function () {
             $.each($scope.alreadyJoinPatientConversation, function (index, value) {
             var flag = moment().subtract(5, 'minute').isAfter(value.dateTime);
             if (value.notifyType == 1002 && flag) {
             value.notifyType = 1003;
             }
             });
             };
             var heartBeatCheckPay = function () {
             $scope.heartBeatPay = setInterval(setIntervalTimers, 6000);
             };*/
            //病人会话的内容的发送
            var updateAlreadyJoinPatientConversationFromPatient = function (conversationData) {
                var updateFlag = false;
                $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                    if (value.patientId == conversationData.senderId) {
                        value.dateTime = conversationData.dateTime;
                        value.consultValue.push(conversationData);
                        if(conversationData.consultNum != 0){
                            value.messageNotSee = true;
                        }else if(conversationData.consultNum == 0){
                            value.messageNotSee = false;
                        }
                        value.number = conversationData.consultNum;
                        updateFlag = true;
                    }
                });
                if (!updateFlag) {
                    var consultValue = {
                        'type': conversationData.type,
                        'content': conversationData.content,
                        'dateTime': conversationData.dateTime,
                        'senderId': conversationData.senderId,
                        'senderName': conversationData.senderName,
                        'sessionId': conversationData.sessionId,
                        'number': conversationData.consultNum
                    };
                    var messageNotSee;
                    if(conversationData.consultNum != 0){
                        messageNotSee = true;
                    }else if(conversationData.consultNum == 0){
                        messageNotSee = false;
                    }
                    var conversationContent = {
                        'patientId': conversationData.senderId,
                        'source': conversationData.source,
                        'serverAddress': conversationData.serverAddress,
                        'sessionId': conversationData.sessionId,
                        'isOnline': true,
                        'dateTime': conversationData.dateTime,
                        'messageNotSee': messageNotSee,
                        'number': conversationData.consultNum,//显示消息数量
                        'notifyType': conversationData.notifyType,
                        'patientName': conversationData.senderName,
                        'consultValue': []
                    };
                    conversationContent.consultValue.push(consultValue);
                    $scope.alreadyJoinPatientConversation.push(conversationContent);
                }

                if (conversationData.senderId == $scope.currentUserConversation.patientId) {
                    $scope.currentUserConversation.messageNotSee = false;
                    if ($scope.currentUserConversation.createTime == null)$scope.currentUserConversation.createTime = conversationData.dateTime;

                }
            };
            //医生会话的内容的发送
            var updateAlreadyJoinPatientConversationFromDoctor = function (consultValMessage) {
                $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                    if (value.patientId == $scope.currentUserConversation.patientId) {
                        ModifyUserConsultNum.save({sessionId:value.sessionId},function(data){
                            console.log(data);
                            value.consultValue.push(consultValMessage);
                            value.messageNotSee = false;
                            value.number = 0;
                        });
                    }
                });
            };

            //处理系统发送过来的通知类消息
            var processNotifyMessage = function (notifyData) {
                if (notifyData.notifyType == "0009") {
                    //有转接的用户过来
                    $scope.refreshWaitJoinUserList();
                    $scope.triggerVoice();
                } else if (notifyData.notifyType == "0012") {
                    //取消转接过来的用户
                    $scope.refreshWaitJoinUserList();
                }
                else if (notifyData.notifyType == "0013") {
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
                else if (notifyData.notifyType == "0014") {
                    $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                        if (value.patientId == notifyData.session.userId) {
                            value.consultValue.push(notifyData);
                            if (value.patientId != $scope.currentUserConversation.patientId) {
                                value.messageNotSee = true;
                                value.number += 1;
                            }
                        }
                    });
                }
                else if (notifyData.notifyType == "0011") {
                    //通知接诊员，转接正在进行中，还未被医生受理
                    $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                        if (value.patientId == notifyData.session.userId) {
                            value.consultValue.push(notifyData);
                            if (value.patientId != $scope.currentUserConversation.patientId) {
                                value.messageNotSee = true;
                                value.number += 1;
                            }
                        }
                    });
                }
                else if(notifyData.notifyType == "0016"){
                    $window.confirm("您的消息没有发送成功！")
                }
                else if (notifyData.notifyType == "0010") {
                    //通知接诊员，转接的处理情况，rejected为拒绝，accept为转接受理了
                    if (notifyData.operation == "accept") {
                        var indexClose = 0;
                        $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                            if (value.patientId == notifyData.session.userId) {
                                indexClose = index;
                            }
                        });
                        $scope.alreadyJoinPatientConversation.splice(indexClose, 1);
                        if ($scope.alreadyJoinPatientConversation.length == 0) {
                            $scope.currentUserConversation = {};
                        } else {
                            if ($scope.currentUserConversation.patientId == notifyData.session.userId) {
                                $scope.chooseAlreadyJoinConsultPatient($scope.alreadyJoinPatientConversation[0].patientId,
                                    $scope.alreadyJoinPatientConversation[0].patientName);
                            }
                        }
                    }
                    else if (notifyData.operation == "rejected") {
                        $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                            if (value.patientId == notifyData.session.userId) {
                                value.consultValue.push(notifyData);
                                if (value.patientId != $scope.currentUserConversation.patientId) {
                                    value.messageNotSee = true;
                                    value.number += 1;
                                }
                            }
                        });
                    }
                }
                else if (notifyData.notifyType == "3001") {
                    getFindTransferSpecialist();
                }
                // 只咨询客服
                else if (notifyData.notifyType == "1001") {
                    $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                        if (value.sessionId == notifyData.sessionId) {
                            value.consultValue.notifyType = 1001;
                            value.notifyType = 1001;
                        }
                    });
                }
                // 只咨询客服
                else if (notifyData.notifyType == "1003") {
                    $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                        if (value.sessionId == notifyData.sessionId) {
                            value.consultValue.notifyType = 1003;
                            value.notifyType = 1003;
                        }
                    });
                }
                else if (notifyData.notifyType == "0015") {
                    //收到服务器发送过来的心跳消息
                    var heartBeatServerMessage = {
                        "type": 6,
                        "csUserId": angular.copy($scope.doctorId)
                    };
                    if (notifyData.notifyAddress == $scope.firstAddress) {
                        if ($scope.socketServerFirst != "" && $scope.socketServerFirst.readyState == 1) {
                            $scope.socketServerFirst.send(JSON.stringify(heartBeatServerMessage));
                        }
                    } else if (notifyData.notifyAddress == $scope.secondAddress) {
                        if ($scope.socketServerSecond != "" && $scope.socketServerSecond.readyState == 1) {
                            $scope.socketServerSecond.send(JSON.stringify(heartBeatServerMessage));
                        }
                    }
                }
            };
            //过滤媒体数据
            var filterMediaData = function (val) {
                if (val.senderId == $scope.doctorId) {
                    if (val.type == "0") {
                        val.content = $sce.trustAsHtml(replace_em(angular.copy(val.content)));
                    }
                } else {
                    if (val.type == "2" || val.type == "3") {
                        val.content = $sce.trustAsResourceUrl(angular.copy(val.content));
                    } else if (val.type == "0") {
                        val.content = $sce.trustAsHtml(replace_em(emotionReceiveFilter(angular.copy(val.content))));
                    }
                }
            };
            var emotionReceiveFilter = function (val) {
                val = val.replace(/\/::\)/g, '[em_1]');
                val = val.replace(/\/::~/g, '[em_2]');
                val = val.replace(/\/::B/g, '[em_3]');
                val = val.replace(/\/::\|/g, '[em_4]');
                val = val.replace(/\/:8-\)/g, '[em_5]');
                val = val.replace(/\/::</g, '[em_6]');
                val = val.replace(/\/::X/g, '[em_7]');
                val = val.replace(/\/::Z/g, '[em_8]');
                val = val.replace(/\/::</g, '[em_9]');
                val = val.replace(/\/::-\|/g, '[em_10]');
                val = val.replace(/\/::@/g, '[em_11]');
                val = val.replace(/\/::P/g, '[em_12]');
                val = val.replace(/\/::D/g, '[em_13]');
                val = val.replace(/\/::O/g, '[em_14]');
                val = val.replace(/\/::\(/g, '[em_15]');
                val = val.replace(/\/:--b/g, '[em_16]');
                val = val.replace(/\/::Q/g, '[em_17]');
                val = val.replace(/\/::T/g, '[em_18]');
                val = val.replace(/\/:,@P/g, '[em_19]');
                val = val.replace(/\/:,@-D/g, '[em_20]');
                val = val.replace(/\/::d/g, '[em_21]');
                val = val.replace(/\/:,@-o/g, '[em_22]');
                val = val.replace(/\/::g/g, '[em_23]');
                val = val.replace(/\/:\|-\)/g, '[em_24]');
                val = val.replace(/\/::!/g, '[em_25]');
                val = val.replace(/\/::L/g, '[em_26]');
                val = val.replace(/\/::>/g, '[em_27]');
                val = val.replace(/\/::,@/g, '[em_28]');
                val = val.replace(/\/:,@f/g, '[em_29]');
                val = val.replace(/\/::-S/g, '[em_30]');
                val = val.replace(/\/:\?/g, '[em_31]');
                val = val.replace(/\/:,@x/g, '[em_32]');
                val = val.replace(/\/:,@@/g, '[em_33]');
                val = val.replace(/\/::8/g, '[em_34]');
                val = val.replace(/\/:,@!/g, '[em_35]');
                val = val.replace(/\/:xx/g, '[em_36]');
                val = val.replace(/\/:bye/g, '[em_37]');
                val = val.replace(/\/:wipe/g, '[em_38]');
                val = val.replace(/\/:dig/g, '[em_39]');
                val = val.replace(/\/:&-\(/g, '[em_40]');
                val = val.replace(/\/:B-\)/g, '[em_41]');
                val = val.replace(/\/:<@/g, '[em_42]');
                val = val.replace(/\/:@>/g, '[em_43]');
                val = val.replace(/\/::-O/g, '[em_44]');
                val = val.replace(/\/:>-\|/g, '[em_45]');
                val = val.replace(/\/:P-\(/g, '[em_46]');
                val = val.replace(/\/::'\|/g, '[em_47]');
                val = val.replace(/\/:X-\)/g, '[em_48]');
                val = val.replace(/\/::\*/g, '[em_49]');
                val = val.replace(/\/:@x/g, '[em_50]');
                val = val.replace(/\/:8\*/g, '[em_51]');
                val = val.replace(/\/:hug/g, '[em_52]');
                val = val.replace(/\/:moon/g, '[em_53]');
                val = val.replace(/\/:sun/g, '[em_54]');
                val = val.replace(/\/:bome/g, '[em_55]');
                val = val.replace(/\/:!!!/g, '[em_56]');
                val = val.replace(/\/:pd/g, '[em_57]');
                val = val.replace(/\/:pig/g, '[em_58]');
                val = val.replace(/\/:<W>/g, '[em_59]');
                val = val.replace(/\/:coffee/g, '[em_60]');
                val = val.replace(/\/:eat/g, '[em_61]');
                val = val.replace(/\/:heart/g, '[em_62]');
                val = val.replace(/\/:strong/g, '[em_63]');
                val = val.replace(/\/:weak/g, '[em_64]');
                val = val.replace(/\/:share/g, '[em_65]');
                val = val.replace(/\/:v/g, '[em_66]');
                val = val.replace(/\/:@\)/g, '[em_67]');
                val = val.replace(/\/:jj/g, '[em_68]');
                val = val.replace(/\/:ok/g, '[em_69]');
                val = val.replace(/\/:no/g, '[em_70]');
                val = val.replace(/\/:rose/g, '[em_71]');
                val = val.replace(/\/:fade/g, '[em_72]');
                val = val.replace(/\/:showlove/g, '[em_73]');
                val = val.replace(/\/:love/g, '[em_74]');
                val = val.replace(/\/:<L>/g, '[em_75]');
                return val;
            };
            var emotionSendFilter = function (val) {
                val = val.replace(/\[em_1\]/g, '/::)');
                val = val.replace(/\[em_2\]/g, '/::~');
                val = val.replace(/\[em_3\]/g, '/::B');
                val = val.replace(/\[em_4\]/g, '/::|');
                val = val.replace(/\[em_5\]/g, '/:8-)');
                val = val.replace(/\[em_6\]/g, '/::<');
                val = val.replace(/\[em_7\]/g, '/::X');
                val = val.replace(/\[em_8\]/g, '/::Z');
                val = val.replace(/\[em_9\]/g, '/::<');
                val = val.replace(/\[em_10\]/g, '/::-|');
                val = val.replace(/\[em_11\]/g, '/::@');
                val = val.replace(/\[em_12\]/g, '/::P');
                val = val.replace(/\[em_13\]/g, '/::D');
                val = val.replace(/\[em_14\]/g, '/::O');
                val = val.replace(/\[em_15\]/g, '/::(');
                val = val.replace(/\[em_16\]/g, '/:--b');
                val = val.replace(/\[em_17\]/g, '/::Q');
                val = val.replace(/\[em_18\]/g, '/::T');
                val = val.replace(/\[em_19\]/g, '/:,@P');
                val = val.replace(/\[em_20\]/g, '/:,@-D');
                val = val.replace(/\[em_21\]/g, '/::d');
                val = val.replace(/\[em_22\]/g, '/:,@-o');
                val = val.replace(/\[em_23\]/g, '/::g');
                val = val.replace(/\[em_24\]/g, '/:|-');
                val = val.replace(/\[em_25\]/g, '/::!');
                val = val.replace(/\[em_26\]/g, '/::L');
                val = val.replace(/\[em_27\]/g, '/::>');
                val = val.replace(/\[em_28\]/g, '/::,@');
                val = val.replace(/\[em_29\]/g, '/:,@f');
                val = val.replace(/\[em_30\]/g, '/::-S');
                val = val.replace(/\[em_31\]/g, '/:?');
                val = val.replace(/\[em_32\]/g, '/:,@x');
                val = val.replace(/\[em_33\]/g, '/:,@@');
                val = val.replace(/\[em_34\]/g, '/::8');
                val = val.replace(/\[em_35\]/g, '/:,@!');
                val = val.replace(/\[em_36\]/g, '/:xx');
                val = val.replace(/\[em_37\]/g, '/:bye');
                val = val.replace(/\[em_38\]/g, '/:wipe');
                val = val.replace(/\[em_39\]/g, '/:dig');
                val = val.replace(/\[em_40\]/g, '/:&-(');
                val = val.replace(/\[em_41\]/g, '/:B-)');
                val = val.replace(/\[em_42\]/g, '/:<@');
                val = val.replace(/\[em_43\]/g, '/:@>');
                val = val.replace(/\[em_44\]/g, '/::-O');
                val = val.replace(/\[em_45\]/g, '/:<-|');
                val = val.replace(/\[em_46\]/g, '/:P-(');
                val = val.replace(/\[em_47\]/g, '/::"|');
                val = val.replace(/\[em_48\]/g, '/:X-)');
                val = val.replace(/\[em_49\]/g, '/::*');
                val = val.replace(/\[em_50\]/g, '/:@x');
                val = val.replace(/\[em_51\]/g, '/:8*');
                val = val.replace(/\[em_52\]/g, '/:hug');
                val = val.replace(/\[em_53\]/g, '/:moon');
                val = val.replace(/\[em_54\]/g, '/:sun');
                val = val.replace(/\[em_55\]/g, '/:bome');
                val = val.replace(/\[em_56\]/g, '/:!!!');
                val = val.replace(/\[em_57\]/g, '/:pd');
                val = val.replace(/\[em_58\]/g, '/:pig');
                val = val.replace(/\[em_59\]/g, '/:<W>');
                val = val.replace(/\[em_60\]/g, '/:coffee');
                val = val.replace(/\[em_61\]/g, '/:eat');
                val = val.replace(/\[em_62\]/g, '/:heart');
                val = val.replace(/\[em_63\]/g, '/:strong');
                val = val.replace(/\[em_64\]/g, '/:weak');
                val = val.replace(/\[em_65\]/g, '/:share');
                val = val.replace(/\[em_66\]/g, '/:v');
                val = val.replace(/\[em_67\]/g, '/:@)');
                val = val.replace(/\[em_68\]/g, '/:jj');
                val = val.replace(/\[em_69\]/g, '/:ok');
                val = val.replace(/\[em_70\]/g, '/:no');
                val = val.replace(/\[em_71\]/g, '/:rose');
                val = val.replace(/\[em_72\]/g, '/:fade');
                val = val.replace(/\[em_73\]/g, '/:showlove');
                val = val.replace(/\[em_74\]/g, '/:love');
                val = val.replace(/\[em_75\]/g, '/<L>');
                return val;
            };

        }])

    .controller('messageListCtrl', ['$scope', '$log', '$state', '$sce', 'GetUserConsultListInfo',
        'GetUserRecordDetail', 'GetCSDoctorList', 'GetMessageRecordInfo', 'GetDoctorLoginStatus', '$location', 'CreateDoctorConsultSession',
        function ($scope, $log, $state, $sce, GetUserConsultListInfo, GetUserRecordDetail,
                  GetCSDoctorList, GetMessageRecordInfo, GetDoctorLoginStatus, $location, CreateDoctorConsultSession) {

            $scope.info = {};

            $scope.searchMessageContent = "";

            $scope.recordDetailSkipNum = "";

            $scope.userConsultListInfoSkipNum = 0;

            $scope.messageType = "";

            $scope.currentClickUserName = "";

            $scope.currentClickUserId = "";

            $scope.loadingFlag = false;

            $scope.searchMessageType = [
                {
                    searchType: "user",
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
            }, {
                name: "所有时间",
                value: 10000
            }];

            $scope.userConsultListPageSize = 11;

            $scope.userRecordDetailPageSize = 10;

            $scope.recordType = "all";

            $scope.messageListInit = function () {
                GetDoctorLoginStatus.save({}, function (data) {
                    $scope.pageLoading = false;
                    if (data.status == "failure") {
                        $state.go("doctorConsultLogin");
                        //window.location.href = "localhost";
                    } else if (data.status == "success") {
                        //获取会话客户列表（含会话转接过程中，经历过几个客服）
                        $scope.selectedDate = $scope.searchDate[0];
                        $scope.selectedMessage = $scope.searchMessageType[0];
                        $scope.setSearchMessageType("user");
                        GetUserConsultListInfo.save({
                            dateNum: 0,
                            CSDoctorId: "allCS",
                            pageNo: 1, pageSize: $scope.userConsultListPageSize
                        }, function (data) {
                            refreshUserConsultListData(data);
                        });

                        //获取客服医生列表
                        GetCSDoctorList.save({}, function (data) {
                            $scope.CSList = [{"id": "allCS", "name": "所有客服"}];
                            $.each(data.CSList, function (index, value) {
                                $scope.CSList.push(value);
                            });
                            $scope.selectedCsList = $scope.CSList[0];
                            $scope.dateNumValue = angular.copy($scope.selectedDate.value);
                            $scope.CSDoctorIdValue = angular.copy($scope.selectedCsList.id);
                        });
                    }
                })
            };
            //获取用户的详细聊天记录
            $scope.getUserRecordDetail = function (userName, userId, index) {
                $scope.doctorCreateConsultSessionChoosedUserId = userId;
                $scope.doctorCreateConsultSessionChoosedUserName = userName;
                $scope.setSessoin = index;
                $scope.loadingFlag = true;
                GetUserRecordDetail.save({
                    pageNo: 1, pageSize: $scope.userRecordDetailPageSize,
                    userId: userId, recordType: "all"
                }, function (data) {
                    $scope.loadingFlag = false;
                    $scope.currentClickUserName = userName;
                    $scope.currentClickUserId = userId;
                    $scope.currentUserConsultRecordDetail = data.records;
                    $scope.currentUserRecordDetailPage = data.currentPage;
                    $scope.totalUserRecordDetailPage = data.totalPage;
                    $.each($scope.currentUserConsultRecordDetail, function (index, value) {
                        filterMediaData(value);
                    });
                });
            };

            $scope.chooseUserRecordDetail = function (action, recordType) {
                var pageNum = 1;
                if (action == "firstPage") {
                    pageNum = 1;
                } else if (action == "lastPage") {
                    pageNum = $scope.totalUserRecordDetailPage;
                } else if (action == "upPage") {
                    if ($scope.currentUserRecordDetailPage > 1) {
                        pageNum = $scope.currentUserRecordDetailPage - 1;
                    }
                } else if (action == "nextPage") {
                    if ($scope.currentUserRecordDetailPage < $scope.totalUserRecordDetailPage) {
                        pageNum = $scope.currentUserRecordDetailPage + 1;
                    }
                } else if (action == "SkipPage") {
                    if ($scope.info.recordDetailSkipNum > 0) {

                        pageNum = parseInt($scope.info.recordDetailSkipNum);
                    } else {
                        alert("请输入大于0的数字！");
                        return;
                    }
                }
                $scope.loadingFlag = true;
                GetUserRecordDetail.save({
                    pageNo: pageNum,
                    pageSize: $scope.userRecordDetailPageSize,
                    userId: $scope.currentClickUserId, recordType: recordType
                }, function (data) {
                    $scope.loadingFlag = false;
                    $scope.currentClickUserName = $scope.currentClickUserName;
                    $scope.currentClickUserId = $scope.currentClickUserId;
                    $scope.currentUserConsultRecordDetail = data.records;
                    $scope.currentUserRecordDetailPage = data.currentPage;
                    $scope.totalUserRecordDetailPage = data.totalPage;
                    $.each($scope.currentUserConsultRecordDetail, function (index, value) {
                        filterMediaData(value);
                    });
                });
            };

            //查找消息记录（点击全部、图片等）
            $scope.setRecordType = function (searchRecordType) {
                $scope.recordType = searchRecordType;
                $scope.chooseUserRecordDetail("firstPage", $scope.recordType);
            };
            //获取客服医生列表
            $scope.getCSDoctorList = function (name) {
                GetCSDoctorList.save({userName: name}, function (data) {
                    $scope.CSList = [];
                    $.each(data.CSList, function (index, value) {
                        $scope.CSList.push(value);
                    });
                });
                $scope.selectedDoctorList = true;
            };
            //查询某个客服信息位于某个时间段的信息
            $scope.getCsInfoByUserAndDate = function (Object, name) {
                console.log(Object);
                $scope.info.selectedDoctorName = name;
                $scope.selectedDoctorList = false;
                if (Object == 10000 || Object == 0 || Object == 7 || Object == 30) {
                    $scope.dateNumValue = angular.copy(Object);
                } else {
                    $scope.CSDoctorIdValue = angular.copy(Object);
                }
                $scope.loadingFlag = true;
                GetUserConsultListInfo.save({
                    dateNum: $scope.dateNumValue,
                    CSDoctorId: $scope.CSDoctorIdValue,
                    pageNo: 1, pageSize: $scope.userConsultListPageSize
                }, function (data) {
                    $scope.loadingFlag = false;
                    refreshUserConsultListData(data);
                })
            };

            //查找咨询记录（消息列表右上角的搜索功能）
            $scope.searchMessage = function () {
                if ($scope.info.searchMessageContent == '' || $scope.info.searchMessageContent == null) {
                    alert('请选择查询内容！');
                    return;
                } else if ($scope.messageType == '' || $scope.messageType == null) {
                    alert('请选择查询类型！');
                    return;
                } else {
                    $scope.loadingFlag = true;
                    GetMessageRecordInfo.save({
                        searchInfo: $scope.info.searchMessageContent,
                        searchType: $scope.messageType,
                        pageNo: 1,
                        pageSize: $scope.userConsultListPageSize
                    }, function (data) {
                        $scope.loadingFlag = false;
                        refreshUserConsultListData(data);
                    });
                }
            };

            //查找咨询记录
            $scope.setSearchMessageType = function (searchType) {
                $scope.messageType = searchType;
            };

            //左上角的刷新消息
            $scope.refreshUserList = function () {
                $scope.loadingFlag = true;
                GetUserConsultListInfo.save({
                    dateNum: $scope.dateNumValue,
                    CSDoctorId: $scope.CSDoctorIdValue,
                    pageNo: 1,
                    pageSize: $scope.userConsultListPageSize
                }, function (data) {
                    $scope.loadingFlag = false;
                    refreshUserConsultListData(data);
                })
            };

            //右上角的消息刷新
            $scope.refreshCurrentUserRecordDetail = function () {
                $scope.getUserRecordDetail($scope.userConsultListInfo[0].userName, $scope.userConsultListInfo[0].userId, 0);
            };

            //医生发起一个针对用户的会话
            $scope.createDoctorConsultSession = function () {
                CreateDoctorConsultSession.save({
                    userName: $scope.doctorCreateConsultSessionChoosedUserName,
                    userId: $scope.doctorCreateConsultSessionChoosedUserId
                }, function (data) {
                    if (data.result == "failure") {
                        alert("无法发起会话，请稍后重试");
                    } else if (data.result == "existTransferSession") {
                        alert("此用户正有会话处于转接状态，无法向其发起会话，请稍后重试");
                    } else if (data.result == "noLicenseTransfer") {
                        alert("对不起，你没有权限，抢断一个正在咨询用户的会话");
                    } else if (data.result == "exceed48Hours") {
                        alert("对不起，用户咨询已经超过了48小时，无法再向其发起会话");
                    } else if (data.result == "success") {
                        $state.go('doctorConsultFirst', {userId: data.userId, action: 'createUserSession'});
                    }
                })
            };

            $scope.chooseUserConsultListDataPage = function (action) {
                var pageNum = 1;
                if (action == "firstPage") {
                    pageNum = 1;
                } else if (action == "lastPage") {
                    pageNum = $scope.totalUserConsultListDataPage;
                } else if (action == "upPage") {
                    if ($scope.currentUserConsultListDataPage > 1) {
                        pageNum = $scope.currentUserConsultListDataPage - 1;
                    }
                } else if (action == "nextPage") {
                    if ($scope.currentUserConsultListDataPage < $scope.totalUserConsultListDataPage) {
                        pageNum = $scope.currentUserConsultListDataPage + 1;
                    }
                } else if (action == "SkipPage") {
                    if ($scope.info.userConsultListInfoSkipNum > 0) {
                        pageNum = parseInt($scope.info.userConsultListInfoSkipNum);

                        $scope.currentUserConsultListDataPage = $scope.info.userConsultListInfoSkipNum;
                    } else {
                        alert("请输入大于0的数字！");
                        return;
                    }
                }
                $scope.loadingFlag = true;
                GetUserConsultListInfo.save({
                    dateNum: $scope.dateNumValue,
                    CSDoctorId: $scope.CSDoctorIdValue,
                    pageNo: pageNum, pageSize: $scope.userConsultListPageSize
                }, function (data) {
                    $scope.loadingFlag = false;
                    refreshUserConsultListData(data);
                })
            };

            var refreshUserConsultListData = function (data) {
                $scope.userConsultListInfo = data.userList;
                $scope.totalUserConsultListDataPage = data.totalPage;
                $scope.currentUserConsultListDataPage = data.currentPage;
                if ($scope.totalUserConsultListDataPage == 0) {
                    $scope.currentUserConsultListDataPage = 0;
                }
                if ($scope.userConsultListInfo.length != 0) {
                    $scope.getUserRecordDetail($scope.userConsultListInfo[0].userName, $scope.userConsultListInfo[0].userId, 0);
                } else {
                    $scope.userConsultListInfo = [];
                    $scope.currentClickUserName = "";
                    $scope.currentClickUserId = "";
                    $scope.currentUserConsultRecordDetail = [];
                }
            };

            //过滤媒体数据
            var filterMediaData = function (val) {
                if (val.type == "1" || val.type == "2" || val.type == "3") {
                    val.message = $sce.trustAsResourceUrl(angular.copy(val.message));
                }
            };
            //各个子窗口的开关变量
            $scope.showFlag = {
                magnifyImg: false
            };
            $scope.tapImgButton = function (key, value) {
                $scope.showFlag[key] = !$scope.showFlag[key];
                $scope.imageSrc = value;
            };
            //公共点击按钮，用来触发弹出对应的子窗口
            $scope.tapShowButton = function (key) {
                $scope.showFlag[key] = !$scope.showFlag[key];
            };
        }])
    .controller('helpDocsListCtrl', ['$scope', '$log', '$state','GetCategoryList','GetArticleList',
        function ($scope, $log, $state,GetCategoryList,GetArticleList) {
            $scope.info = {};
            $scope.classifyIndex=0;
            $scope.classifyIndex=0;
            $scope.helpDocsClassify= {};
            $scope.helpDocsList={};
            // 选择 左边的分类
            $scope.selectClassify = function (index,id) {
                $scope.classifyIndex = index;
                $scope.classifyId = id;
                //获取某一分类下的文章列表
                GetArticleList.save({"id": $scope.classifyId,"pageNo":1,"pageSize":10},function(data){
                    $scope.helpDocsList=data.articleList;

                });

            };
            // 跳转到 文章详情页
            $scope.goHelpDocsDetail = function (id) {
                $state.go("helpDocsDetail",{articleId:id});
            };
            $scope.helpDocsListInit = function () {
                document.title="帮助文档列表页"; //修改页面title
                //获取左边分类
                GetCategoryList.save({"categoryId":"bc802fafd6db4f3e91894c9932cbf6f6"},function(data){
                    $scope.helpDocsClassify=data.categoryList;
                    $scope.classifyId = $scope.helpDocsClassify[0].categoryId;
                    //初始化第一个分类下的文章
                    GetArticleList.save({"id": $scope.classifyId,"pageNo":1,"pageSize":1000},function(data){
                        $scope.helpDocsList=data.articleList;
                    });
                });
            };

        }])
    .controller('helpDocsDetailCtrl', ['$scope', '$log', '$state','$stateParams','$sce','GetCategoryList','GetArticleDetail',
        function ($scope, $log, $state, $stateParams,$sce,GetCategoryList,GetArticleDetail) {
            $scope.info = {};
            $scope.helpDocsClassify= {};
            $scope.articleDetail={};
            $scope.helpDocsDetailInit = function () {
                document.title="帮助文档详情页"; //修改页面title
                $scope.articleId =$stateParams.articleId;//文章id
                //获取左边分类
                GetCategoryList.save({"categoryId":"bc802fafd6db4f3e91894c9932cbf6f6"},function(data){
                    $scope.helpDocsClassify=data.categoryList;
                });
                //获取文章详情
                GetArticleDetail.save({"id":$stateParams.articleId},function(data){
                    $scope.info.articleContent = $sce.trustAsHtml(angular.copy(data.articleDetail.content));
                    $scope.articleDetail=data.article;
                    $scope.articleDetail.name=data.article.category.name
                });
            };
        }])


    //登陆controller
    .controller('doctorConsultLogin', [
        '$scope', '$state', '$timeout', '$http', 'DoctorBinding',
        function ($scope, $state, $timeout, $http, DoctorBinding) {
            $scope.doctorLock = false;//非系统医生提示开关
            $scope.errorLock = false;//错误提示开关
            $scope.errorRemindText ="";//错误提示内容
            $scope.info = {};
            var countdown = 60;
            //关闭提示
            $scope.close = function () {
                $scope.doctorLock = false;
            };

            $scope.doctorBindingAction = function () {
                DoctorBinding.save({username: $scope.info.phoneNum, password: $scope.info.password}, function (data) {
                    if (data.status == "failure") {
                        $scope.errorLock = true;
                        $scope.errorRemindText ="验证码错误"
                    }else if(data.status == "notConsultDoctor"){
                        $scope.doctorLock = true;
                    }else {
                        $state.go("doctorConsultFirst");
                    }
                });
            }


            $scope.getValidateCode = function () {
                if (countdown < 60) {
                    return;
                }
                else {
                    var partner = /^1[34578]\d{9}$/;
                    if (!partner.exec($scope.info.phoneNum)) {
                        $scope.errorLock = true;
                        $scope.errorRemindText ="手机号格式不对"
                        return;
                    }
                    else {
                        var mydata = '{"userPhone":"'
                            + $scope.info.phoneNum + '"}';
                        $.ajax({
                            url: "util/user/getCode",
                            async: true,
                            type: 'post',
                            data: mydata,
                            cache: false,
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function () {

                            },
                            error: function () {

                            }
                        });
                        $scope.lockValidateCode();
                    }
                }
            }

            $scope.lockValidateCode = function () {
                if (countdown == 0) {
                    $('#validateCode').html("获取验证码");
                    countdown = 60;
                    return;
                } else {
                    $('#validateCode').html("重新发送(" + countdown + ")");
                    countdown--;
                }
                setTimeout(function () {
                        $scope.lockValidateCode()
                    }
                    , 1000)
            };

            $scope.doctorConsultLoginInit=function(){
                $(".tips").css("margin-top",screen.height-380-screen.width*0.25+"px")
            }

        }]);


