angular.module('controllers', ['luegg.directives'])
    .controller('doctorConsultFirstCtrl', ['$scope', '$sce', '$window','$stateParams','GetTodayRankingList',
        'GetOnlineDoctorList','GetAnswerValueList','GetUserLoginStatus',
        '$location', 'GetCurrentUserHistoryRecord','GetMyAnswerModify','GetCurrentUserConsultListInfo',
        'TransferToOtherCsUser','SessionEnd','GetWaitJoinList','React2Transfer','CancelTransfer','$upload',
        'GetFindTransferSpecialist','GetRemoveTransferSpecialist','GetAddTransferSpecialist','GetFindAllTransferSpecialist',
        'CreateTransferSpecialist','$state','GetSystemTime','GetUserSessionTimesByUserId',
        function ($scope, $sce, $window,$stateParams,GetTodayRankingList, GetOnlineDoctorList, GetAnswerValueList,
                  GetUserLoginStatus, $location, GetCurrentUserHistoryRecord,GetMyAnswerModify,
                  GetCurrentUserConsultListInfo,TransferToOtherCsUser,SessionEnd,GetWaitJoinList,React2Transfer,CancelTransfer,$upload,
                  GetFindTransferSpecialist,GetRemoveTransferSpecialist,GetAddTransferSpecialist,GetFindAllTransferSpecialist,
                  CreateTransferSpecialist,$state,GetSystemTime,GetUserSessionTimesByUserId) {
            //��ʼ��info����
            $scope.info = {
                effect:"true",
                transferRemark:"",
                searchCsUserValue:"",
                selectedSpecialist:"",
                role:{
                    "distributor":"����Ա",
                    "consultDoctor":"רҵҽ��"
                }
            };
            $scope.loadingFlag = false;
            $scope.socketServerFirst = "";
            $scope.socketServerSecond = "";
            $scope.firstAddress = "102.201.154.75";
            $scope.secondAddress = "120.25.161.33";
            $scope.alreadyJoinPatientConversation = []; //�Ѿ�����Ự���û����ݣ�һ��ҽ�������ж���Ի����û�����Щ�û������ݣ��������ڴ˼�����
            $scope.currentUserConversation = {}; //ҽ���뵱ǰ���ڽ��жԻ��û����������ݣ�ҽ�����л���ͬ�û�ʱ�����ݱ�����л����û�������
            $scope.waitJoinNum = 0; //ҽ����������û������Ƕ�̬�仯����
            $scope.glued = true; //angular�������Ĳ��Ԥ�Ʋ������öԻ���������ÿ�ζ���λ�ײ������µ��������ݵ���ʱ
            var umbrellaCustomerList = "75cefafe00364bbaaaf7b61089994e22,3b91fe8b7ce143918012ef3ab4baf1e0,00032bd90d724d0sa63a4d6esa0e8dbf";

            //�����Ӵ��ڵĿ��ر���
            $scope.showFlag = {
                rankList: false,
                systemSetup: false,
                waitProcess: false,
                switchOver: false,
                myReplyList: false,
                publicReplyList: false,
                diagnosisReplyList: false,
                replyContent: true,
                advisoryContent: false,
                magnifyImg:false,
                specialistList:false,
                specialistTransfer:false
            };
            $scope.searchFlag = false;
            $scope.tapImgButton = function (key,value) {
                $scope.showFlag[key] = !$scope.showFlag[key];
                $scope.imageSrc = value;
            };
            $scope.loseConnectionFirstFlag = false;
            $scope.loseConnectionSecondFlag = false;
            var acceptOperationFlag = false;
            var waitProcessLock = false;
            var heartBeatFirstNum = 3;
            var heartBeatSecondNum = 3;

            //��ʼ��ҽ���˵�¼������socket���ӣ���ȡ������Ϣ
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

                        //������ƽ̨��socket����
                        if($scope.socketServerFirst==""||$scope.socketServerFirst.readyState!=1){
                            $scope.initConsultSocketFirst();
                        }
                        if($scope.socketServerSecond==""||$scope.socketServerSecond.readyState!=1){
                            $scope.initConsultSocketSecond();
                        }

                        getIframeSrc();
                        //��ȡͨ�ûظ��б�
                        GetAnswerValueList.save({"type": "commonAnswer"}, function (data) {
                            if(data.result=="success"){
                                var answerData = JSON.parse(data.answerValue);
                                $scope.commonAnswer = answerData.commonAnswer;
                            }else{
                                $scope.commonAnswer = [];
                            }
                        });

                        //��ȡ�ҵĻظ��б�
                        GetAnswerValueList.save({"type":"myAnswer"},function(data){
                            if(data.result=="success"){
                                var answerData = JSON.parse(data.answerValue);
                                $scope.myAnswer = answerData.myAnswer;
                            }else{
                                $scope.myAnswer = [];
                            }
                        });
                        //��ȡ�ҵ�����б�
                        GetAnswerValueList.save({"type":"diagnosis"},function(data){
                            if(data.result=="success"){
                                var answerData = JSON.parse(data.answerValue);
                                $scope.diagnosis = answerData.diagnosis;
                            }else{
                                $scope.diagnosis = [];
                            }
                        });
                        /*GetCurrentDoctorDepartment.save({userId:$scope.doctorId},function(data){
                         if(data.status == 'success'){
                         $scope.department = data.department;
                         }else{
                         $scope.department = 'default';
                         }
                         });*/
                        $scope.refreshWaitJoinUserList();

                        if($stateParams.action == "createUserSession"){
                            var patientId = $stateParams.userId;
                            var patientName = "";
                            GetCurrentUserConsultListInfo.save({csUserId:$scope.doctorId,pageNo:1,pageSize:10000},function(data){
                                if(data.alreadyJoinPatientConversation!=""&&data.alreadyJoinPatientConversation!=undefined){
                                    $scope.alreadyJoinPatientConversation = data.alreadyJoinPatientConversation;
                                    $.each($scope.alreadyJoinPatientConversation,function(index,value){
                                        if(value.patientId==patientId){
                                            patientName = value.patientName;
                                        }
                                        $.each(value.consultValue,function(index1,value1){
                                            filterMediaData(value1);
                                        })
                                    });
                                    $scope.chooseAlreadyJoinConsultPatient(patientId,patientName);
                                }
                            })
                        }else if($stateParams.action == ""){
                            getAlreadyJoinConsultPatientList();
                        }

                        setInterval(sessionCheck,20000);
                    }
                });
            };

            //ÿ20�룬���һ��ҽ����ƽ̨�ĻỰ�Ƿ�ʧЧ
            var sessionCheck = function(){
                var routePath = "/doctor/consultBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath: routePath}, function (data) {
                    $scope.pageLoading = false;
                    if (data.status == "9") {
                        window.location.href = data.redirectURL;
                    } else if (data.status == "8") {
                        window.location.href = data.redirectURL + "?targeturl=" + routePath;
                    }
                })
            };

            //���������ť����������������Ӧ���Ӵ���
            $scope.tapShowButton = function(type){
                $.each($scope.showFlag,function(key,value){
                    if(key==type){
                        if(type=="waitProcess"){
                            if(!waitProcessLock){
                                $scope.showFlag[key] = !$scope.showFlag[key];
                            }
                        }else{
                            $scope.showFlag[key] = !$scope.showFlag[key];
                        }
                        if(type == "replyContent"){
                            if($scope.showFlag.replyContent == false){
                                $scope.showFlag.advisoryContent =true;
                            }else{
                                $scope.showFlag.advisoryContent =false;
                            }
                        }
                        else if(type == "advisoryContent"){
                            if ($scope.showFlag.advisoryContent==false){
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
                            }else if(type == "diagnosisReplyList"){
                                $scope.diagnosisReplyIndex = -1;
                                $scope.diagnosisReplySecondIndex = -1;
                            }
                        }else{
                            if(type=="rankList"){
                                //�ѽ���Ự��ѯҽ����������
                                $scope.refreshRankList();
                            } else if(type == "switchOver"){
                                //��ȡ����ҽ���б�
                                $scope.refreshOnLineCsUserList();
                            }else if(type=="waitProcess"){
                                //��ȡ�������û��б�
                                $scope.refreshWaitJoinUserList();
                            }else if(type=="specialistList"){
                                //��ȡ�������û��б�
                                getFindTransferSpecialist();
                            }
                        }
                    }
                })
            };

            /**ת�ӹ�����**/
            $scope.acceptTransfer = function(){
                if(!acceptOperationFlag){
                    $scope.tapShowButton('waitProcess');
                    var waitJoinChooseUserList = "";
                    $.each($scope.waitJoinUserList,function(index,value){
                        if(value.chooseFlag){
                            waitJoinChooseUserList = waitJoinChooseUserList + value.forwardSessionId + ";"
                        }
                    });
                    acceptOperationFlag = true;
                    waitProcessLock = true;
                    React2Transfer.save({operation:"accept",
                        forwardSessionIds:waitJoinChooseUserList},function(data){
                        acceptOperationFlag = false;
                        waitProcessLock = false;
                        if(data.result=="success"){
                            //��ת�ӳɹ����û���������Ự�б�����
                            var indexClose = 0;
                            $.each($scope.waitJoinUserList,function(index,value){
                                if(value.chooseFlag){
                                    $scope.currentUserConversation = {
                                        'patientId':value.userId,
                                        'source':value.source,
                                        'serverAddress':value.serverAddress,
                                        'sessionId':value.sessionId,
                                        'isOnline':true,
                                        'dateTime':value.sessionCreateTime,
                                        'messageNotSee':true,
                                        'number':1,
                                        'patientName':value.userName,
                                        'consultValue':[]
                                    };
                                    var consultValue = {
                                        'type':value.type,
                                        'content':value.messageContent,
                                        'dateTime':value.messageDateTime,
                                        'senderId':value.userId,
                                        'senderName':value.userName,
                                        'sessionId':value.sessionId
                                    };
                                    filterMediaData(consultValue);
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
            };

            var rejectOperationFlag = false;
            $scope.rejectTransfer = function(){
                if(!rejectOperationFlag){
                    $scope.tapShowButton('waitProcess');
                    var waitJoinChooseUserList = "";
                    $.each($scope.waitJoinUserList,function(index,value){
                        if(value.chooseFlag){
                            waitJoinChooseUserList = waitJoinChooseUserList + value.forwardSessionId + ";"
                        }
                    });
                    rejectOperationFlag = true;
                    React2Transfer.save({operation:"rejected",forwardSessionIds:waitJoinChooseUserList},function(data){
                        rejectOperationFlag = false;
                        if(data.result=="success"){
                            $scope.refreshWaitJoinUserList();
                        }
                    });
                }
            };

            //ѡ��ת�Ӷ���
            $scope.chooseTransferCsUser = function(csUserId,csUserName){
                $scope.transferCsUserId = csUserId;
                $scope.csTransferUserName = csUserName;
                $scope.transferUserName = angular.copy($scope.currentUserConversation.patientName);
            };

            $scope.transferToCsUser = function(){
                $scope.tapShowButton('switchOver');
                TransferToOtherCsUser.save({doctorId: $scope.transferCsUserId,
                    sessionId:$scope.currentUserConversation.sessionId,
                    remark: $scope.info.transferRemark},function(data){
                    if(data.result=="success"){
                        //ת������ɹ����ڽ���Ա�࣬�����˴˻Ự��ֻ����ת�ӵ�ҽ���յ�Ϊֹ��
                        // �Ž��Ự������ڴ˹����У��������Ա��ȡ��ת�ӡ�
                    }else if(data.result=="failure"){
                        alert("ת�ӻỰ��"+$scope.csTransferUserName+"ʧ�ܣ���ת�Ӹ�����ҽ��");
                    }else if(data.result=="transferring"){
                        alert("���û�" + $scope.transferUserName +"�ĻỰ���ڱ�ת���У������ٴ�ת��");
                    }
                });
            };

            $scope.searchCsUser = function(){
                if($scope.info.searchCsUserValue!=""){
                    $scope.searchFlag = true;
                }else{
                    $scope.searchFlag = false;
                }
            };

            $scope.cancelTransfer = function(sessionId,toCsUserId,remark){
                CancelTransfer.save({sessionId:sessionId,toCsUserId:toCsUserId,remark:remark},function(data){
                    if(data.result=="success"){
                        //ɾ��ȡ��֪ͨ
                        var indexClose = 0;
                        $.each($scope.currentUserConversation.consultValue, function (index, value) {
                            if (value.remark == remark && value.toCsUserId==toCsUserId) {
                                indexClose = index;
                            }
                        });
                        $scope.currentUserConversation.consultValue.splice(indexClose, 1);
                    }
                });
            };

            //���տ�������
            $scope.getWaitTransferSpecialist = function () {
                $scope.alreadyJoinTransferSpecialist = [];
                GetFindTransferSpecialist.save({sortBy:"order"},function(data){
                    $.each(data.data, function (index,value) {
                        value.selectedAll = false;
                        $scope.alreadyJoinTransferSpecialist.push(value);
                    });
                });
            };

            //��ѯ���е�ת���б�
            var getFindTransferSpecialist = function () {
                $scope.alreadyJoinTransferSpecialist = [];
                GetFindTransferSpecialist.save({sortBy:"no"},function(data){
                    $.each(data.data, function (index,value) {
                        value.selectedAll = false;
                        $scope.alreadyJoinTransferSpecialist.push(value);
                    });
                });
            };

            //��ѯר���б�
            GetFindAllTransferSpecialist.save({}, function (data) {
                $scope.selectedSpecialistType = data.data;
            });

            //���ת�ӿ���ȷ��
            $scope.addTransferSpecialistSubmit = function () {
                $scope.showFlag.specialistTransfer = false;
                //���ת�ӿ���
                var consultData = {
                    sessionId: angular.copy($scope.currentUserConversation.sessionId),
                    department: angular.copy($scope.info.selectedSpecialist.departmentName)
                };
                GetAddTransferSpecialist.save({consultData:consultData},function(data){
                    if(data.status == "exist"){
                        alert("�û�����ӹ�ת��");
                    }else if (data.status == "success"){
                        alert("���ת��ɹ�");
                    }
                });
            };
            $scope.closeSpecialistTransfer = function(){$scope.showFlag.specialistTransfer = false;};

            //ɾ��ת�ӿ����е�һ��
            $scope.disposeTransferSpecialist = function (index) {
                $scope.showFlag.specialistList = false;
                var specialistId  = [$scope.alreadyJoinTransferSpecialist[index]];
                if ($window.confirm("ȷ��Ҫɾ����ת������?")) {
                    GetRemoveTransferSpecialist.save({content:specialistId,status:'ongoing',delFlag:'1'},function(data){
                        if(data.status == 'failure'){
                            alert("ɾ��ʧ��");
                        }else if(data.status == "success"){
                            alert("ɾ���ɹ�");
                            $scope.alreadyJoinTransferSpecialist.splice(index, 1);
                            getFindTransferSpecialist();
                        }
                    });
                }

            };

            //����Ա����һ������û��ĻỰ
            $scope.createOneSpecialistPatient = function(index,department){
                $scope.showFlag.specialistList = false;
                CreateTransferSpecialist.save({specialistPatientContent:$scope.alreadyJoinTransferSpecialist[index]},function(data){
                    if(data.status == "success"){
                        var patientId = data.userId;
                        var patientName = data.userName;
                        GetCurrentUserConsultListInfo.save({csUserId:$scope.doctorId,pageNo:1,pageSize:10000},function(data){
                            if(data.alreadyJoinPatientConversation!=""&&data.alreadyJoinPatientConversation!=undefined){
                                $scope.alreadyJoinPatientConversation = data.alreadyJoinPatientConversation;
                                $.each($scope.alreadyJoinPatientConversation,function(index,value){
                                    value.messageNotSee = false;
                                    value.number = 0;
                                    if(value.patientId==patientId){
                                        patientName = value.patientName;
                                        value.transferDepartment = '��Ҫת��'+department;
                                    }
                                    $.each(value.consultValue,function(index1,value1){
                                        filterMediaData(value1);
                                    })
                                });
                                $scope.chooseAlreadyJoinConsultPatient(patientId,patientName);
                            }
                        });
                        $scope.showFlag.specialistList = false;
                        getFindTransferSpecialist();

                    }else if(data.status == "failure"){
                        if(data.result == "failure"){
                            alert("�޷�����Ự�����Ժ�����");
                        }else if(data.result == "existTransferSession"){
                            alert("���û����лỰ����ת��״̬���޷����䷢��Ự�����Ժ�����");
                        }else if(data.result == "noLicenseTransfer"){
                            alert("�Բ�����û��Ȩ�ޣ�����һ��������ѯ�û��ĻỰ");
                        }else if(data.result == "exceed48Hours"){
                            alert("�Բ����û���ѯ�Ѿ�������48Сʱ���޷������䷢��Ự");
                        }
                    }else if(data.status == "ongoing"){
                        alert("���û�����"+data.csuserName+"�Ự�У��޷����䷢��Ự�����Ժ�����")
                    }else if(data.status == "complete"){
                        alert("��ǰ�Ự��ת������ˢ���б�")
                    }
                })
            };
            /**ת�ӹ�����**/

            /**�Ự������**/

                //��ʼ��socket����
            $scope.initConsultSocketFirst = function () {
                if (!window.WebSocket) {
                    window.WebSocket = window.MozWebSocket;
                }
                if (window.WebSocket) {
                    if($scope.userType=="distributor"){
                        $scope.socketServerFirst = new WebSocket("ws://" + $scope.firstAddress +":2048/ws&" +
                            "distributor&" + $scope.doctorId);//cs,user,distributor
                    }else if($scope.userType=="consultDoctor"){
                        $scope.socketServerFirst = new WebSocket("ws://" + $scope.firstAddress +":2048/ws&" +
                            "cs&" + $scope.doctorId);//cs,user,distributor
                    }

                    $scope.socketServerFirst.onmessage = function (event) {
                        var consultData = JSON.parse(event.data);
                        if(consultData.type==4){
                            processNotifyMessage(consultData);
                            $scope.$apply();
                        }else if(consultData.type==5){
                            heartBeatFirstNum = 3;
                        }else{
                            filterMediaData(consultData);
                            processPatientSendMessage(consultData);
                            $scope.triggerqqVoice();
                            $scope.$apply();
                        }
                    };

                    $scope.socketServerFirst.onopen = function (event) {
                        console.log("onopen",event.data);
                        //�����������
                        heartBeatCheckFirst();
                    };

                    $scope.socketServerFirst.onclose = function (event) {
                    };

                } else {
                    alert("����������֧�֣�");
                }
            };
            $scope.initConsultSocketSecond = function () {
                if (!window.WebSocket) {
                    window.WebSocket = window.MozWebSocket;
                }
                if (window.WebSocket) {
                    if($scope.userType=="distributor"){
                        $scope.socketServerSecond = new WebSocket("ws://" + $scope.secondAddress +":2048/ws&" +
                            "distributor&" + $scope.doctorId);//cs,user,distributor
                    }else if($scope.userType=="consultDoctor"){
                        $scope.socketServerSecond = new WebSocket("ws://" + $scope.secondAddress +":2048/ws&" +
                            "cs&" + $scope.doctorId);//cs,user,distributor
                    }

                    $scope.socketServerSecond.onmessage = function (event) {
                        var consultData = JSON.parse(event.data);
                        if(consultData.type==4){
                            processNotifyMessage(consultData);
                            $scope.$apply();
                        }else if(consultData.type==5){
                            heartBeatSecondNum = 3;
                        }else{
                            filterMediaData(consultData);
                            processPatientSendMessage(consultData);
                            $scope.triggerqqVoice();
                            $scope.$apply();
                        }
                    };

                    $scope.socketServerSecond.onopen = function (event) {
                        console.log("onopen",event.data);
                        //�����������
                        heartBeatCheckSecond();
                    };

                    $scope.socketServerSecond.onclose = function (event) {
                    };

                } else {
                    alert("����������֧�֣�");
                }
            };
            $scope.messageList = function(){
                clearInterval($scope.heartBeatFirstId);
                clearInterval($scope.heartBeatSecondId);
                $state.go('messageList');
            };
            //����
            var heartBeatCheckFirst = function(){
                //������ʱ���������Եķ���������Ϣ
                $scope.heartBeatFirstId = setInterval(sendHeartBeatFirst,2000);
            };
            var sendHeartBeatFirst = function(){
                var heartBeatMessage = {
                    "type": 5,
                    "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                    "csUserId": angular.copy($scope.doctorId)
                };
                heartBeatFirstNum--;
                if(heartBeatFirstNum < 0){
                    heartBeatFirstNum = 3;
                    $scope.loseConnectionFirstFlag = true;
                    $scope.initConsultSocketFirst();
                }else{
                    $scope.loseConnectionFirstFlag = false;
                    if($scope.socketServerFirst!=""&&$scope.socketServerFirst.readyState==1){
                        $scope.socketServerFirst.send(JSON.stringify(heartBeatMessage));
                    }
                }
                $scope.$apply();
            };
            var heartBeatCheckSecond = function(){
                //������ʱ���������Եķ���������Ϣ
                $scope.heartBeatSecondId = setInterval(sendHeartBeatSecond,2000);
            };
            var sendHeartBeatSecond = function(){
                var heartBeatMessage = {
                    "type": 5,
                    "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                    "csUserId": angular.copy($scope.doctorId)
                };
                heartBeatSecondNum--;
                if(heartBeatSecondNum < 0){
                    heartBeatSecondNum = 3;
                    $scope.loseConnectionSecondFlag = true;
                    $scope.initConsultSocketSecond();
                }else{
                    $scope.loseConnectionSecondFlag = false;
                    if($scope.socketServerSecond!=""&&$scope.socketServerSecond.readyState==1){
                        $scope.socketServerSecond.send(JSON.stringify(heartBeatMessage));
                    }
                }
                $scope.$apply();
            };
            //�����û������¼�
            document.onkeydown = function () {
                if (window.event.keyCode == 13){
                    if($("#saytext").val().replace(/\s+/g,"")!=""){
                        $scope.sendConsultMessage();
                    }
                }
            };//��onkeydown �¼�����ʱ���ú���
            //���û�������ѯ��Ϣ
            $scope.sendConsultMessage = function () {
                if($("#saytext").val().replace(/\s+/g,"")!=""){
                    if (!window.WebSocket) {
                        return;
                    }
                    GetSystemTime.save(function(data){
                        $scope.info.consultMessage = "";
                        var sayTextFlag = processSayTextFlag($("#saytext").val());
                        var consultContent = $("#saytext").val();
                        $("#saytext").val('');
                        if(sayTextFlag!="noFlag"){
                            var valueData = consultContent.split("####");
                            consultContent = valueData[0];
                        }
                        if($scope.currentUserConversation.serverAddress==$scope.firstAddress){
                            if ($scope.socketServerFirst.readyState == WebSocket.OPEN) {
                                var consultValMessage = "";
                                if($scope.userType=="distributor"){
                                    var consultValMessage = {
                                        "type": 0,
                                        "content": "����" +$scope.doctorName+"��"+ consultContent,
                                        "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                        "senderId": angular.copy($scope.doctorId),
                                        "senderName": angular.copy($scope.doctorName),
                                        "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                        "consultFlag": sayTextFlag
                                    };
                                }else if($scope.userType=="consultDoctor"){
                                    if(umbrellaCustomerList.indexOf($scope.doctorId)>-1){
                                        var consultValMessage = {
                                            "type": 0,
                                            "content": "����ɡ�ͷ���" + consultContent,
                                            "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                            "senderId": angular.copy($scope.doctorId),
                                            "senderName": angular.copy($scope.doctorName),
                                            "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                            "consultFlag": sayTextFlag
                                        };
                                    }else{
                                        var consultValMessage = {
                                            "type": 0,
                                            "content": $scope.doctorName + "ҽ����" + consultContent,
                                            "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                            "senderId": angular.copy($scope.doctorId),
                                            "senderName": angular.copy($scope.doctorName),
                                            "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                            "consultFlag": sayTextFlag
                                        };
                                    }
                                }
                                $scope.socketServerFirst.send(emotionSendFilter(JSON.stringify(consultValMessage)));
                                consultValMessage.content =  $sce.trustAsHtml(replace_em(angular.copy(consultContent)));
                                $("#saytext").val('');
                                updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                            } else {
                                alert("����û�п���.");
                            }
                        }
                        else if($scope.currentUserConversation.serverAddress==$scope.secondAddress){
                            if ($scope.socketServerSecond.readyState == WebSocket.OPEN) {
                                var consultValMessage = "";
                                if($scope.userType=="distributor"){
                                    var consultValMessage = {
                                        "type": 0,
                                        "content": "����" +$scope.doctorName+"��"+ consultContent,
                                        "dateTime":  moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                        "senderId": angular.copy($scope.doctorId),
                                        "senderName": angular.copy($scope.doctorName),
                                        "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                        "consultFlag": sayTextFlag
                                    };
                                }else if($scope.userType=="consultDoctor"){
                                    if(umbrellaCustomerList.indexOf($scope.doctorId)>-1){
                                        var consultValMessage = {
                                            "type": 0,
                                            "content": "����ɡ�ͷ���" + consultContent,
                                            "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                            "senderId": angular.copy($scope.doctorId),
                                            "senderName": angular.copy($scope.doctorName),
                                            "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                            "consultFlag": sayTextFlag
                                        };
                                    }else{
                                        var consultValMessage = {
                                            "type": 0,
                                            "content": $scope.doctorName + "ҽ����" + consultContent,
                                            "dateTime": moment(data.dateTime).format('YYYY-MM-DD HH:mm:ss'),
                                            "senderId": angular.copy($scope.doctorId),
                                            "senderName": angular.copy($scope.doctorName),
                                            "sessionId": angular.copy($scope.currentUserConversation.sessionId),
                                            "consultFlag": sayTextFlag
                                        };
                                    }
                                }

                                $scope.socketServerSecond.send(emotionSendFilter(JSON.stringify(consultValMessage)));
                                consultValMessage.content =  $sce.trustAsHtml(replace_em(angular.copy(consultContent)));
                                $("#saytext").val('');
                                updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                            } else {
                                alert("����û�п���.");
                            }
                        }
                        else{
                            if($scope.currentUserConversation.serverAddress==""||$scope.currentUserConversation.serverAddress==null){
                                if($scope.socketServerFirst!=""){
                                    $scope.currentUserConversation.serverAddress = angular.copy($scope.firstAddress);
                                }else if($scope.socketServerSecond!=""){
                                    $scope.currentUserConversation.serverAddress = angular.copy($scope.secondAddress);
                                }
                                $scope.sendConsultMessage();
                            }
                        }
                    });
                }
            };

            var processSayTextFlag = function(data){
                var flag = "noFlag";
                if (data.indexOf("####") != -1) {
                    var textValue = data.split("####");
                    flag = textValue[1];
                }
                return flag;
            }

            //���û�������ѯͼƬ
            $scope.uploadFiles = function($files,fileType) {
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
                    }).progress(function(evt) {
                    }).success(function(data, status, headers, config){
                        if(data.source == "wxcxqm"){
                            var consultValMessage = {
                                "type": 1,
                                "content": data.showFile,
                                "wscontent": data.WS_File,
                                "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                                "senderId": angular.copy($scope.doctorId),
                                "senderName": angular.copy($scope.doctorName),
                                /*"userType": angular.copy($scope.userType),
                                 "department": angular.copy($scope.department),*/
                                "sessionId": angular.copy($scope.currentUserConversation.sessionId)
                            };
                        } else{
                            var consultValMessage = {
                                "type": 1,
                                "content": data.showFile,
                                "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),
                                "senderId": angular.copy($scope.doctorId),
                                "senderName": angular.copy($scope.doctorName),
                                /* "userType": angular.copy($scope.userType),
                                 "department": angular.copy($scope.department),*/
                                "sessionId": angular.copy($scope.currentUserConversation.sessionId)
                            };
                        }
                        if (!window.WebSocket) {
                            return;
                        }
                        if($scope.currentUserConversation.serverAddress==$scope.firstAddress){
                            if ($scope.socketServerFirst.readyState == WebSocket.OPEN) {
                                $scope.socketServerFirst.send(JSON.stringify(consultValMessage));
                                updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                            } else {
                                alert("����û�п���.");
                            }
                        }else if($scope.currentUserConversation.serverAddress==$scope.secondAddress){
                            if ($scope.socketServerSecond.readyState == WebSocket.OPEN) {
                                $scope.socketServerSecond.send(JSON.stringify(consultValMessage));
                                updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                            } else {
                                alert("����û�п���.");
                            }
                        }else{
                            if($scope.currentUserConversation.serverAddress==""||$scope.currentUserConversation.serverAddress==null){
                                if($scope.socketServerFirst!=""){
                                    if ($scope.socketServerFirst.readyState == WebSocket.OPEN) {
                                        $scope.socketServerFirst.send(JSON.stringify(consultValMessage));
                                        updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                                    } else {
                                        alert("����û�п���.");
                                    }
                                }else if($scope.socketServerSecond!=""){
                                    if ($scope.socketServerSecond.readyState == WebSocket.OPEN) {
                                        $scope.socketServerSecond.send(JSON.stringify(consultValMessage));
                                        updateAlreadyJoinPatientConversationFromDoctor(consultValMessage);
                                    } else {
                                        alert("����û�п���.");
                                    }
                                }
                            }
                        }

                    });

                }
            };
            //�رո�ĳ���û��ĻỰ
            var closeConsultLock = false;
            $scope.closeConsult = function () {
                if(!closeConsultLock){
                    closeConsultLock = true;
                    SessionEnd.get({sessionId:$scope.currentUserConversation.sessionId,
                        userId:$scope.currentUserConversation.patientId},function(data){
                        closeConsultLock = false;
                        if(data.result=="success"){
                            var indexClose = 0;
                            $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                                if (value.patientId == $scope.chooseAlreadyJoinConsultPatientId) {
                                    indexClose = index;
                                }
                            });
                            $scope.alreadyJoinPatientConversation.splice(indexClose, 1);
                            $scope.chooseAlreadyJoinConsultPatientSessionTimes ='';

                            if($scope.alreadyJoinPatientConversation.length!=0){
                                $scope.chooseAlreadyJoinConsultPatient($scope.alreadyJoinPatientConversation[0].patientId,
                                    $scope.alreadyJoinPatientConversation[0].patientName);
                            }else{
                                $scope.currentUserConversation = {};
                            }
                        }else{
                            alert("�Ự�ر�ʧ�ܣ�������");
                        }
                    })
                }else{
                    alert("���ڹرյ�ǰ�û��ĻỰ�����Ժ󣬵ȹرմ��û��ɹ����ٹر������û�");
                }
            };
            //��ͨ���б��У�ѡȡһ���û����лỰ
            $scope.chooseAlreadyJoinConsultPatient = function (patientId, patientName,sessionId) {
                $scope.chooseAlreadyJoinConsultPatientId = patientId;
                $scope.chooseAlreadyJoinConsultPatientName = patientName;
                $scope.chooseAlreadyJoinConsultPatientsessionId = sessionId;
                GetUserSessionTimesByUserId.get({userId:patientId},function(data){
                    console.log(data);
                    $scope.chooseAlreadyJoinConsultPatientSessionTimes ='��'+ data.userSessionTimes + '�ν���';
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
            };
            //����ת������
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
            //������Ϣ����
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
                    id: 'facebox', //������ӵ�ID
                    assign: 'saytext', //���Ǹ��ؼ���ֵ
                    path: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F'
                    //�����ŵ�·��
                });
            };
            //�鿴���
            var replace_em = function (str) {
                str = str.replace(/\</g,'&lt;');
                str = str.replace(/\>/g,'&gt;');
                str = str.replace(/\n/g,'<br/>');
                str = str.replace(/\[em_([0-9]*)\]/g,'<img src="http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F$1.gif" border="0" />');
                return str;
            };
            //�鿴������û���ʷ��Ϣ
            $scope.seeMoreConversationMessage = function(){
                var mostFarCurrentConversationDateTime = moment().format("YYYY-MM-DD HH:mm:ss");
                if($scope.currentUserConversation.consultValue[0]!=undefined){
                    var mostFarCurrentConversationDateTime = $scope.currentUserConversation.consultValue[0].dateTime;
                }
                GetCurrentUserHistoryRecord.save({
                    userId:$scope.currentUserConversation.patientId,
                    dateTime:mostFarCurrentConversationDateTime,
                    pageSize:10},function(data){
                    if(data.consultDataList!=""){
                        $.each(data.consultDataList,function(index,value){
                            filterMediaData(value);
                            $scope.currentUserConversation.consultValue.splice(0, 0, value);
                        });
                    }
                })
            };
            /**�Ự������**/
                //������ѯҽ��������ѯ�û����������б�
            $scope.refreshRankList = function(){
                var currDate = new moment().format("YYYY-MM-DD");
                GetTodayRankingList.save({"rankDate": currDate}, function (data) {
                    $scope.info.rankListValue = data.rankListValue;
                });
            };
            //��ȡ���ߵ���ѯҽ���б�
            $scope.refreshOnLineCsUserList = function(){
                $scope.searchFlag = false;
                $scope.info.searchCsUserValue = "";
                GetOnlineDoctorList.save({}, function (data) {
                    $scope.info.onLineCsUserList = data.onLineCsUserList;
                });
            };
            //��ȡ������Ự�û��б�
            $scope.refreshWaitJoinUserList = function(){
                GetWaitJoinList.save({csUserId:$scope.doctorId},function(data){
                    $scope.waitJoinNum = data.waitJoinNum;
                    $scope.waitJoinUserList = data.waitJoinList;
                })
            };

            /***�ظ�������**/
                //�ҵĻظ�����
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
            };
            $scope.tapEditMyContent = function(parentIndex, childIndex) {
                $scope.myReplySecondIndex = childIndex;
                $scope.info.editContent = $scope.myAnswer[parentIndex].secondAnswer[childIndex].name;
            };
            $scope.chooseMyContent = function(parentIndex, childIndex){
                $scope.info.consultMessage = angular.copy($scope.myAnswer[parentIndex].secondAnswer[childIndex].name);
            };
            //�����ظ�����
            $scope.chooseCommonContent = function(parentIndex, childIndex){
                $scope.info.consultMessage = angular.copy($scope.commonAnswer[parentIndex].secondAnswer[childIndex].name);
            };
            $scope.tapPublicReplyContent = function (parentIndex){
                if($scope.publicReplyIndex==parentIndex){
                    $scope.publicReplyIndex = -1;
                    $scope.publicReplySecondIndex = -1;
                    $scope.info.editGroup = "";
                    $scope.info.editContent = "";
                }else{
                    $scope.publicReplyIndex = parentIndex;
                    $scope.publicReplySecondIndex = -1;
                    $scope.info.editGroup = $scope.commonAnswer[parentIndex].name;
                    $scope.info.editContent = "";
                }
            };
            $scope.tapEditCommonContent = function(parentIndex, childIndex){
                $scope.publicReplySecondIndex = childIndex;
                $scope.info.editContent = $scope.commonAnswer[parentIndex].secondAnswer[childIndex].name;
            };
            //��ϻظ�����
            $scope.chooseDiagnosisContent = function(parentIndex, childIndex){
                $scope.info.consultMessage = angular.copy($scope.diagnosis[parentIndex].secondAnswer[childIndex].name);
            };
            $scope.tapDiagnosisReplyContent = function (parentIndex){
                if($scope.diagnosisReplyIndex==parentIndex){
                    $scope.diagnosisReplyIndex = -1;
                    $scope.diagnosisReplySecondIndex = -1;
                    $scope.info.editGroup = "";
                    $scope.info.editContent = "";
                }else{
                    $scope.diagnosisReplyIndex = parentIndex;
                    $scope.diagnosisReplySecondIndex = -1;
                    $scope.info.editGroup = $scope.diagnosis[parentIndex].name;
                    $scope.info.editContent = "";
                }
            };
            $scope.tapEditDiagnosisContent = function(parentIndex, childIndex){
                $scope.diagnosisReplySecondIndex = childIndex;
                $scope.info.editContent = $scope.diagnosis[parentIndex].secondAnswer[childIndex].name;
            };
            //��ӷ���
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
                if($scope.showFlag.publicReplyList){
                    if($scope.publicReplyIndex==-1||$scope.publicReplyIndex==undefined){
                        $scope.addGroupFlag = true;
                        $scope.addContentFlag = false;
                    }else{
                        $scope.addGroupFlag = false;
                        $scope.addContentFlag = true;
                    }
                }
                if($scope.showFlag.diagnosisReplyList){
                    if($scope.diagnosisReplyIndex==-1||$scope.diagnosisReplyIndex==undefined){
                        $scope.addGroupFlag = true;
                        $scope.addContentFlag = false;
                    }else{
                        $scope.addGroupFlag = false;
                        $scope.addContentFlag = true;
                    }
                }
            };
            $scope.closeAddGroup = function() {
                $scope.info.addGroup = '';
                $scope.info.addContent = '';
                $scope.addGroupFlag = false;
            };
            $scope.addGroupSubmit = function () {
                var setGroupContent = {};
                setGroupContent.name = $scope.info.addGroup;
                setGroupContent.secondAnswer=[];
                if($scope.showFlag.myReplyList){
                    $scope.myAnswer.push(setGroupContent);
                    saveMyAnswer();
                }
                if($scope.showFlag.publicReplyList){
                    $scope.commonAnswer.push(setGroupContent);
                    saveCommonAnswer();
                }
                if($scope.showFlag.diagnosisReplyList){
                    $scope.diagnosis.push(setGroupContent);
                    saveDiagnosis();
                }
                $scope.addGroupFlag = false;
            };
            //�������
            $scope.closeAddContent = function(){$scope.addContentFlag=false;};
            $scope.addContentSubmit = function () {
                var setContent = {};
                setContent.name = $scope.info.addContent;
                if($scope.showFlag.myReplyList){
                    $scope.myAnswer[$scope.myReplyIndex].secondAnswer.push(setContent);
                    saveMyAnswer();
                }
                if($scope.showFlag.publicReplyList){
                    $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer.push(setContent);
                    saveCommonAnswer();
                }
                if($scope.showFlag.diagnosisReplyList){
                    $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer.push(setContent);
                    saveDiagnosis();
                }
                $scope.addContentFlag=false;
            };
            //�༭����
            $scope.closeEditGroup = function(){$scope.editGroupFlag = false;};
            $scope.closeEditContent = function(){$scope.editContentFlag = false;};
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
                if($scope.showFlag.publicReplyList){
                    if($scope.publicReplyIndex!=-1&&$scope.publicReplyIndex!=undefined){
                        if($scope.publicReplySecondIndex==-1||$scope.publicReplyIndex==undefined){
                            $scope.editGroupFlag = true;
                            $scope.editContentFlag = false;
                        }else{
                            $scope.editGroupFlag = false;
                            $scope.editContentFlag = true;
                        }
                    }
                }
                if($scope.showFlag.diagnosisReplyList){
                    if($scope.diagnosisReplyIndex!=-1&&$scope.diagnosisReplyIndex!=undefined){
                        if($scope.diagnosisReplySecondIndex==-1||$scope.diagnosisReplyIndex==undefined){
                            $scope.editGroupFlag = true;
                            $scope.editContentFlag = false;
                        }else{
                            $scope.editGroupFlag = false;
                            $scope.editContentFlag = true;
                        }
                    }
                }
            };
            $scope.editGroupSubmit = function () {
                if($scope.showFlag.myReplyList){
                    $scope.myAnswer[$scope.myReplyIndex].name = $scope.info.editGroup;
                    saveMyAnswer();
                }
                if($scope.showFlag.publicReplyList){
                    $scope.commonAnswer[$scope.publicReplyIndex].name = $scope.info.editGroup;
                    saveCommonAnswer();
                }
                if($scope.showFlag.diagnosisReplyList){
                    $scope.diagnosis[$scope.diagnosisReplyIndex].name = $scope.info.editGroup;
                    saveDiagnosis();
                }
                $scope.editGroupFlag=false;
            };
            $scope.editContentSubmit = function () {
                if($scope.showFlag.myReplyList){
                    $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex].name = $scope.info.editContent;
                    saveMyAnswer();
                }
                if($scope.showFlag.publicReplyList){
                    $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex].name = $scope.info.editContent;
                    saveCommonAnswer();
                }
                if($scope.showFlag.diagnosisReplyList){
                    $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex].name = $scope.info.editContent;
                    saveCommonAnswer();
                }
                $scope.editContentFlag=false;
            };
            //ɾ��
            $scope.remove = function(){
                if($scope.showFlag.myReplyList){
                    if($scope.myReplyIndex!=-1&&$scope.myReplyIndex!=undefined){
                        if($scope.myReplySecondIndex==-1||$scope.myReplyIndex==undefined){
                            if ($window.confirm("ȷ��Ҫɾ������ظ�?")) {
                                $scope.myAnswer.splice($scope.myReplyIndex, 1);
                                saveMyAnswer();
                            }
                        }else{
                            if($window.confirm("ȷ��Ҫɾ���ûظ�?")) {
                                $scope.myAnswer[$scope.myReplyIndex].secondAnswer.splice($scope.myReplySecondIndex, 1);
                                saveMyAnswer();
                            }
                        }
                    }
                }
                if($scope.showFlag.publicReplyList){
                    if($scope.publicReplyIndex!=-1&&$scope.publicReplyIndex!=undefined){
                        if($scope.publicReplySecondIndex==-1||$scope.publicReplyIndex==undefined){
                            if ($window.confirm("ȷ��Ҫɾ������ظ�?")) {
                                $scope.commonAnswer.splice($scope.publicReplyIndex, 1);
                                saveCommonAnswer();
                            }
                        }else{
                            if($window.confirm("ȷ��Ҫɾ���ûظ�?")) {
                                $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer.splice($scope.publicReplySecondIndex, 1);
                                saveCommonAnswer();
                            }
                        }
                    }
                }
                if($scope.showFlag.diagnosisReplyList){
                    if($scope.diagnosisReplyIndex!=-1&&$scope.diagnosisReplyIndex!=undefined){
                        if($scope.diagnosisReplySecondIndex==-1||$scope.diagnosisReplyIndex==undefined){
                            if ($window.confirm("ȷ��Ҫɾ������ظ�?")) {
                                $scope.diagnosis.splice($scope.diagnosisReplyIndex, 1);
                                saveDiagnosis();
                            }
                        }else{
                            if($window.confirm("ȷ��Ҫɾ���ûظ�?")) {
                                $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer.splice($scope.diagnosisReplySecondIndex, 1);
                                saveDiagnosis();
                            }
                        }
                    }
                }
            };
            //�ظ�������
            $scope.moveUp = function(){
                if($scope.showFlag.myReplyList){
                    if($scope.myReplyIndex!=-1&&$scope.myReplyIndex!=undefined){
                        if($scope.myReplySecondIndex > 0){
                            var changeAnswerContent = $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex - 1];
                            $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex - 1] = $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex];
                            $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex] = changeAnswerContent;
                        }else if($scope.myReplySecondIndex == -1){
                            var changeAnswerGroup = $scope.myAnswer[$scope.myReplyIndex - 1];
                            $scope.myAnswer[$scope.myReplyIndex - 1] = $scope.myAnswer[$scope.myReplyIndex];
                            $scope.myAnswer[$scope.myReplyIndex] = changeAnswerGroup;
                        }
                        saveMyAnswer();
                    }
                }
                if($scope.showFlag.publicReplyList){
                    if($scope.publicReplyIndex!=-1&&$scope.publicReplyIndex!=undefined){
                        if($scope.publicReplySecondIndex > 0){
                            var changeAnswerContent = $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex - 1];
                            $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex - 1] = $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex];
                            $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex] = changeAnswerContent;
                        }else if($scope.publicReplySecondIndex == -1){
                            var changeAnswerGroup = $scope.commonAnswer[$scope.publicReplyIndex - 1];
                            $scope.commonAnswer[$scope.publicReplyIndex - 1] = $scope.commonAnswer[$scope.publicReplyIndex];
                            $scope.commonAnswer[$scope.publicReplyIndex] = changeAnswerGroup;
                        }
                        saveCommonAnswer();
                    }
                }
                if($scope.showFlag.diagnosisReplyList){
                    if($scope.diagnosisReplyIndex!=-1&&$scope.diagnosisReplyIndex!=undefined){
                        if($scope.diagnosisReplySecondIndex > 0){
                            var changeAnswerContent = $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex - 1];
                            $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex - 1] = $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex];
                            $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex] = changeAnswerContent;
                        }else if($scope.diagnosisReplySecondIndex == -1){
                            var changeAnswerGroup = $scope.commonAnswer[$scope.diagnosisReplyIndex - 1];
                            $scope.diagnosis[$scope.diagnosisReplyIndex - 1] = $scope.diagnosis[$scope.diagnosisReplyIndex];
                            $scope.diagnosis[$scope.diagnosisReplyIndex] = changeAnswerGroup;
                        }
                        saveDiagnosis();
                    }
                }
            };
            $scope.moveDown = function(){
                if($scope.showFlag.myReplyList){
                    if($scope.myReplyIndex!=-1&&$scope.myReplyIndex!=undefined){
                        if($scope.myReplySecondIndex >= 0 && $scope.myReplySecondIndex < $scope.myAnswer[$scope.myReplyIndex].secondAnswer.length){
                            var changeAnswerContent = $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex + 1];
                            $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex + 1] = $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex];
                            $scope.myAnswer[$scope.myReplyIndex].secondAnswer[$scope.myReplySecondIndex] = changeAnswerContent;
                        }else if($scope.myReplySecondIndex == -1 && $scope.myReplyIndex < $scope.myAnswer.length){
                            var changeAnswerGroup = $scope.myAnswer[$scope.myReplyIndex + 1];
                            $scope.myAnswer[$scope.myReplyIndex + 1] = $scope.myAnswer[$scope.myReplyIndex];
                            $scope.myAnswer[$scope.myReplyIndex] = changeAnswerGroup;
                        }
                        saveMyAnswer();
                    }
                }
                if($scope.showFlag.publicReplyList){
                    if($scope.publicReplyIndex!=-1&&$scope.publicReplyIndex!=undefined){
                        if($scope.publicReplySecondIndex >= 0 && $scope.publicReplySecondIndex < $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer.length){
                            var changeAnswerContent = $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex - 1];
                            $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex - 1] = $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex];
                            $scope.commonAnswer[$scope.publicReplyIndex].secondAnswer[$scope.publicReplySecondIndex] = changeAnswerContent;
                        }else if($scope.publicReplySecondIndex == -1 && $scope.publicReplyIndex < $scope.commonAnswer.length){
                            var changeAnswerGroup = $scope.commonAnswer[$scope.publicReplyIndex - 1];
                            $scope.commonAnswer[$scope.publicReplyIndex - 1] = $scope.commonAnswer[$scope.publicReplyIndex];
                            $scope.commonAnswer[$scope.publicReplyIndex] = changeAnswerGroup;
                        }
                        saveCommonAnswer();
                    }
                }
                if($scope.showFlag.diagnosisReplyList){
                    if($scope.diagnosisReplyIndex!=-1&&$scope.diagnosisReplyIndex!=undefined){
                        if($scope.diagnosisReplySecondIndex >= 0 && $scope.diagnosisReplySecondIndex < $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer.length){
                            var changeAnswerContent = $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex + 1];
                            $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex + 1] = $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex];
                            $scope.diagnosis[$scope.diagnosisReplyIndex].secondAnswer[$scope.diagnosisReplySecondIndex] = changeAnswerContent;
                        }else if($scope.diagnosisReplySecondIndex == -1 && $scope.diagnosisReplyIndex < $scope.diagnosis.length){
                            var changeAnswerGroup = $scope.diagnosis[$scope.diagnosisReplyIndex + 1];
                            $scope.diagnosis[$scope.diagnosisReplyIndex + 1] = $scope.diagnosis[$scope.diagnosisReplyIndex];
                            $scope.diagnosis[$scope.diagnosisReplyIndex] = changeAnswerGroup;
                        }
                        saveDiagnosis();
                    }
                }
            };
            //�����ҵĻظ�
            var saveMyAnswer = function() {
                GetMyAnswerModify.save({answer: $scope.myAnswer, answerType: "myAnswer"}, function (data) {
                });
            };
            //���湫���ظ�
            var saveCommonAnswer = function() {
                GetMyAnswerModify.save({answer: $scope.commonAnswer, answerType: "commonAnswer"}, function (data) {
                });
            };
            //������ϻظ�
            var saveDiagnosis = function() {
                GetMyAnswerModify.save({answer: $scope.diagnosis, answerType: "diagnosis"}, function (data) {
                });
            };
            /***�ظ�������**/
            var getIframeSrc = function(){
                var newSrc = $(".advisory-content").attr("src");
                $(".advisory-content").attr("src","");
                $(".advisory-content").attr("src",newSrc);
            };
            //����ת��
            $scope.transformDate = function(dateTime){
                var dateValue = new moment(dateTime).format("HH:mm:ss");
                return dateValue;
            };
            //�õ��Ѿ�����Ự�Ĳ��˵��б�
            var getAlreadyJoinConsultPatientList = function () {
                //��ȡ��ҽ���ĻỰ��������û��б�
                GetCurrentUserConsultListInfo.save({csUserId:$scope.doctorId,pageNo:1,pageSize:10000},function(data){
                    if(data.alreadyJoinPatientConversation!=""&&data.alreadyJoinPatientConversation!=undefined){
                        $scope.alreadyJoinPatientConversation = data.alreadyJoinPatientConversation;
                        $.each($scope.alreadyJoinPatientConversation,function(index,value){
                            value.messageNotSee = false;
                            value.number = 0;
                            $.each(value.consultValue,function(index1,value1){
                                filterMediaData(value1);
                            });
                        });
                        var patientId = angular.copy($scope.alreadyJoinPatientConversation[0].patientId);
                        var patientName = angular.copy($scope.alreadyJoinPatientConversation[0].patientName);
                        $scope.chooseAlreadyJoinConsultPatient(patientId,patientName);
                    }
                })
            };
            //�����û����͹����ĻỰ��Ϣ
            var processPatientSendMessage = function(conversationData){
                var chooseFlag = false;
                var currentConsultValue = {
                    'type':conversationData.type,
                    'content':conversationData.content,
                    'dateTime':conversationData.dateTime,
                    'senderId':conversationData.senderId,
                    'senderName':conversationData.senderName,
                    'sessionId':conversationData.sessionId
                };
                if(JSON.stringify($scope.currentUserConversation)=='{}'){
                    $scope.currentUserConversation = {
                        'patientId':conversationData.senderId,
                        'source':conversationData.source,
                        'serverAddress':conversationData.serverAddress,
                        'sessionId':conversationData.sessionId,
                        'messageNotSee':true,
                        'isOnline':true,
                        'dateTime':conversationData.dateTime,
                        'patientName':conversationData.senderName,
                        'consultValue':[]
                    };
                    $scope.currentUserConversation.consultValue.push(currentConsultValue);
                    chooseFlag = true;
                }
                updateAlreadyJoinPatientConversationFromPatient(conversationData);
                if(chooseFlag){
                    $scope.chooseAlreadyJoinConsultPatient(angular.copy(currentConsultValue.senderId),
                        angular.copy(currentConsultValue.senderName));
                    getIframeSrc();
                }
            };
            //���˻Ự�����ݵķ���
            var updateAlreadyJoinPatientConversationFromPatient = function(conversationData){
                var updateFlag = false;
                $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                    if (value.patientId == conversationData.senderId) {
                        value.dateTime = conversationData.dateTime;
                        value.consultValue.push(conversationData);
                        value.messageNotSee = true;
                        value.number += 1;
                        updateFlag = true;
                    }
                });
                if(!updateFlag){
                    var consultValue = {
                        'type':conversationData.type,
                        'content':conversationData.content,
                        'dateTime':conversationData.dateTime,
                        'senderId':conversationData.senderId,
                        'senderName':conversationData.senderName,
                        'sessionId':conversationData.sessionId
                    };
                    var conversationContent = {
                        'patientId':conversationData.senderId,
                        'source':conversationData.source,
                        'serverAddress':conversationData.serverAddress,
                        'sessionId':conversationData.sessionId,
                        'isOnline':true,
                        'dateTime':conversationData.dateTime,
                        'messageNotSee':true,
                        'number':1,//��ʾ��Ϣ����
                        'patientName':conversationData.senderName,
                        'consultValue':[]
                    };
                    conversationContent.consultValue.push(consultValue);
                    $scope.alreadyJoinPatientConversation.push(conversationContent);
                }

                if(conversationData.senderId==$scope.currentUserConversation.patientId){
                    $scope.currentUserConversation.messageNotSee = false;
                }
            };
            //ҽ���Ự�����ݵķ���
            var updateAlreadyJoinPatientConversationFromDoctor = function(consultValMessage){
                $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                    if (value.patientId == $scope.currentUserConversation.patientId) {
                        value.consultValue.push(consultValMessage);
                        value.messageNotSee = false;
                        value.number = 0;
                    }
                });
            };

            //����ϵͳ���͹�����֪ͨ����Ϣ
            var processNotifyMessage = function(notifyData){
                if(notifyData.notifyType=="0009"){
                    //��ת�ӵ��û�����
                    $scope.refreshWaitJoinUserList();
                    $scope.triggerVoice();
                } else if(notifyData.notifyType=="0012"){
                    //ȡ��ת�ӹ������û�
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
                            if(value.patientId!=$scope.currentUserConversation.patientId){
                                value.messageNotSee = true;
                                value.number += 1;
                            }
                        }
                    });
                }
                else if(notifyData.notifyType=="0011"){
                    //֪ͨ����Ա��ת�����ڽ����У���δ��ҽ������
                    $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                        if (value.patientId == notifyData.session.userId) {
                            value.consultValue.push(notifyData);
                            if(value.patientId!=$scope.currentUserConversation.patientId){
                                value.messageNotSee = true;
                                value.number += 1;
                            }
                        }
                    });
                }
                else if(notifyData.notifyType=="0010"){
                    //֪ͨ����Ա��ת�ӵĴ��������rejectedΪ�ܾ���acceptΪת��������
                    if(notifyData.operation=="accept"){
                        var indexClose = 0;
                        $.each($scope.alreadyJoinPatientConversation, function (index, value) {
                            if (value.patientId == notifyData.session.userId) {
                                indexClose = index;
                            }
                        });
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
                                if(value.patientId!=$scope.currentUserConversation.patientId){
                                    value.messageNotSee = true;
                                    value.number += 1;
                                }
                            }
                        });
                    }
                }
                else if(notifyData.notifyType=="3001"){
                    getFindTransferSpecialist();
                }
                else if(notifyData.notifyType=="0015"){
                    //�յ����������͹�����������Ϣ
                    var heartBeatServerMessage = {
                        "type": 6,
                        "csUserId": angular.copy($scope.doctorId)
                    };
                    if(notifyData.notifyAddress == $scope.firstAddress){
                        if($scope.socketServerFirst!=""&&$scope.socketServerFirst.readyState==1){
                            $scope.socketServerFirst.send(JSON.stringify(heartBeatServerMessage));
                        }
                    }else if(notifyData.notifyAddress == $scope.secondAddress){
                        if($scope.socketServerSecond!=""&&$scope.socketServerSecond.readyState==1){
                            $scope.socketServerSecond.send(JSON.stringify(heartBeatServerMessage));
                        }
                    }
                }
            };
            //����ý������
            var filterMediaData = function (val) {
                if(val.senderId==$scope.doctorId){
                    if (val.type == "0") {
                        val.content = $sce.trustAsHtml(replace_em(angular.copy(val.content)));
                    }
                }else{
                    if (val.type == "2"||val.type == "3") {
                        val.content = $sce.trustAsResourceUrl(angular.copy(val.content));
                    }else if(val.type == "0"){
                        val.content = $sce.trustAsHtml(replace_em(emotionReceiveFilter(angular.copy(val.content))));
                    }
                }
            };
            var emotionReceiveFilter = function(val){
                val = val.replace(/\/::\)/g, '[em_1]');val = val.replace(/\/::~/g, '[em_2]');val = val.replace(/\/::B/g, '[em_3]');val = val.replace(/\/::\|/g, '[em_4]');
                val = val.replace(/\/:8-\)/g, '[em_5]');val = val.replace(/\/::</g, '[em_6]');val = val.replace(/\/::X/g, '[em_7]');val = val.replace(/\/::Z/g, '[em_8]');
                val = val.replace(/\/::</g, '[em_9]');val = val.replace(/\/::-\|/g, '[em_10]');val = val.replace(/\/::@/g, '[em_11]');val = val.replace(/\/::P/g, '[em_12]');
                val = val.replace(/\/::D/g, '[em_13]');val = val.replace(/\/::O/g, '[em_14]');val = val.replace(/\/::\(/g, '[em_15]');val = val.replace(/\/:--b/g, '[em_16]');
                val = val.replace(/\/::Q/g, '[em_17]');val = val.replace(/\/::T/g, '[em_18]');val = val.replace(/\/:,@P/g, '[em_19]');val = val.replace(/\/:,@-D/g, '[em_20]');
                val = val.replace(/\/::d/g, '[em_21]');val = val.replace(/\/:,@-o/g, '[em_22]');val = val.replace(/\/::g/g, '[em_23]');val = val.replace(/\/:\|-\)/g, '[em_24]');
                val = val.replace(/\/::!/g, '[em_25]');val = val.replace(/\/::L/g, '[em_26]');val = val.replace(/\/::>/g, '[em_27]');val = val.replace(/\/::,@/g, '[em_28]');
                val = val.replace(/\/:,@f/g, '[em_29]');val = val.replace(/\/::-S/g, '[em_30]');val = val.replace(/\/:\?/g, '[em_31]');val = val.replace(/\/:,@x/g, '[em_32]');
                val = val.replace(/\/:,@@/g, '[em_33]');val = val.replace(/\/::8/g, '[em_34]');val = val.replace(/\/:,@!/g, '[em_35]');val = val.replace(/\/:xx/g, '[em_36]');
                val = val.replace(/\/:bye/g, '[em_37]');val = val.replace(/\/:wipe/g, '[em_38]');val = val.replace(/\/:dig/g, '[em_39]');val = val.replace(/\/:&-\(/g, '[em_40]');
                val = val.replace(/\/:B-\)/g, '[em_41]');val = val.replace(/\/:<@/g, '[em_42]');val = val.replace(/\/:@>/g, '[em_43]');val = val.replace(/\/::-O/g, '[em_44]');
                val = val.replace(/\/:>-\|/g, '[em_45]');val = val.replace(/\/:P-\(/g, '[em_46]');val = val.replace(/\/::'\|/g, '[em_47]');val = val.replace(/\/:X-\)/g, '[em_48]');
                val = val.replace(/\/::\*/g, '[em_49]');val = val.replace(/\/:@x/g, '[em_50]');val = val.replace(/\/:8\*/g, '[em_51]');val = val.replace(/\/:hug/g, '[em_52]');
                val = val.replace(/\/:moon/g, '[em_53]');val = val.replace(/\/:sun/g, '[em_54]');val = val.replace(/\/:bome/g, '[em_55]');val = val.replace(/\/:!!!/g, '[em_56]');
                val = val.replace(/\/:pd/g, '[em_57]');val = val.replace(/\/:pig/g, '[em_58]');val = val.replace(/\/:<W>/g, '[em_59]');val = val.replace(/\/:coffee/g, '[em_60]');
                val = val.replace(/\/:eat/g, '[em_61]');val = val.replace(/\/:heart/g, '[em_62]');val = val.replace(/\/:strong/g, '[em_63]');val = val.replace(/\/:weak/g, '[em_64]');
                val = val.replace(/\/:share/g, '[em_65]');val = val.replace(/\/:v/g, '[em_66]');val = val.replace(/\/:@\)/g, '[em_67]');val = val.replace(/\/:jj/g, '[em_68]');
                val = val.replace(/\/:ok/g, '[em_69]');val = val.replace(/\/:no/g, '[em_70]');val = val.replace(/\/:rose/g, '[em_71]');val = val.replace(/\/:fade/g, '[em_72]');
                val = val.replace(/\/:showlove/g, '[em_73]');val = val.replace(/\/:love/g, '[em_74]');val = val.replace(/\/:<L>/g, '[em_75]');
                return val;
            };
            var emotionSendFilter = function(val){
                val = val.replace(/\[em_1\]/g, '/::)');val = val.replace(/\[em_2\]/g, '/::~');val = val.replace(/\[em_3\]/g, '/::B');val = val.replace(/\[em_4\]/g, '/::|');
                val = val.replace(/\[em_5\]/g, '/:8-)');val = val.replace(/\[em_6\]/g, '/::<');val = val.replace(/\[em_7\]/g, '/::X');val = val.replace(/\[em_8\]/g, '/::Z');
                val = val.replace(/\[em_9\]/g, '/::<');val = val.replace(/\[em_10\]/g, '/::-|');val = val.replace(/\[em_11\]/g, '/::@');val = val.replace(/\[em_12\]/g, '/::P');
                val = val.replace(/\[em_13\]/g, '/::D');val = val.replace(/\[em_14\]/g, '/::O');val = val.replace(/\[em_15\]/g, '/::(');val = val.replace(/\[em_16\]/g, '/:--b');
                val = val.replace(/\[em_17\]/g, '/::Q');val = val.replace(/\[em_18\]/g, '/::T');val = val.replace(/\[em_19\]/g, '/:,@P');val = val.replace(/\[em_20\]/g, '/:,@-D');
                val = val.replace(/\[em_21\]/g, '/::d');val = val.replace(/\[em_22\]/g, '/:,@-o');val = val.replace(/\[em_23\]/g, '/::g');val = val.replace(/\[em_24\]/g, '/:|-');
                val = val.replace(/\[em_25\]/g, '/::!');val = val.replace(/\[em_26\]/g, '/::L');val = val.replace(/\[em_27\]/g, '/::>');val = val.replace(/\[em_28\]/g, '/::,@');
                val = val.replace(/\[em_29\]/g, '/:,@f');val = val.replace(/\[em_30\]/g, '/::-S');val = val.replace(/\[em_31\]/g, '/:?');val = val.replace(/\[em_32\]/g, '/:,@x');
                val = val.replace(/\[em_33\]/g, '/:,@@');val = val.replace(/\[em_34\]/g, '/::8');val = val.replace(/\[em_35\]/g, '/:,@!');val = val.replace(/\[em_36\]/g, '/:xx');
                val = val.replace(/\[em_37\]/g, '/:bye');val = val.replace(/\[em_38\]/g, '/:wipe');val = val.replace(/\[em_39\]/g, '/:dig');val = val.replace(/\[em_40\]/g, '/:&-(');
                val = val.replace(/\[em_41\]/g, '/:B-)');val = val.replace(/\[em_42\]/g, '/:<@');val = val.replace(/\[em_43\]/g, '/:@>');val = val.replace(/\[em_44\]/g, '/::-O');
                val = val.replace(/\[em_45\]/g, '/:<-|');val = val.replace(/\[em_46\]/g, '/:P-(');val = val.replace(/\[em_47\]/g, '/::"|');val = val.replace(/\[em_48\]/g, '/:X-)');
                val = val.replace(/\[em_49\]/g, '/::*');val = val.replace(/\[em_50\]/g, '/:@x');val = val.replace(/\[em_51\]/g, '/:8*');val = val.replace(/\[em_52\]/g, '/:hug');
                val = val.replace(/\[em_53\]/g, '/:moon');val = val.replace(/\[em_54\]/g, '/:sun');val = val.replace(/\[em_55\]/g, '/:bome');val = val.replace(/\[em_56\]/g, '/:!!!');
                val = val.replace(/\[em_57\]/g, '/:pd');val = val.replace(/\[em_58\]/g, '/:pig');val = val.replace(/\[em_59\]/g, '/:<W>');val = val.replace(/\[em_60\]/g, '/:coffee');
                val = val.replace(/\[em_61\]/g, '/:eat');val = val.replace(/\[em_62\]/g, '/:heart');val = val.replace(/\[em_63\]/g, '/:strong');val = val.replace(/\[em_64\]/g, '/:weak');
                val = val.replace(/\[em_65\]/g, '/:share');val = val.replace(/\[em_66\]/g, '/:v');val = val.replace(/\[em_67\]/g, '/:@)');val = val.replace(/\[em_68\]/g, '/:jj');
                val = val.replace(/\[em_69\]/g, '/:ok');val = val.replace(/\[em_70\]/g, '/:no');val = val.replace(/\[em_71\]/g, '/:rose');val = val.replace(/\[em_72\]/g, '/:fade');
                val = val.replace(/\[em_73\]/g, '/:showlove');val = val.replace(/\[em_74\]/g, '/:love');val = val.replace(/\[em_75\]/g, '/<L>');
                return val;
            };
        }])

    .controller('messageListCtrl', ['$scope', '$log', '$state','$sce', 'GetUserConsultListInfo',
        'GetUserRecordDetail', 'GetCSDoctorList', 'GetMessageRecordInfo','GetUserLoginStatus','$location','CreateDoctorConsultSession',
        function ($scope, $log, $state,$sce, GetUserConsultListInfo, GetUserRecordDetail,
                  GetCSDoctorList, GetMessageRecordInfo,GetUserLoginStatus,$location,CreateDoctorConsultSession) {

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
                    searchType:"user",
                    searchContent: "���ҿͻ�"
                }
                //,{
                //searchType:"message",
                //searchContent: "������Ϣ"
                //}
            ];

            $scope.searchDate = [{
                name: "����",
                value: 0
            }, {
                name: "���7��",
                value: 7
            }, {
                name: "���30��",
                value: 30
            },{
                name: "����ʱ��",
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
                        //��ȡ�Ự�ͻ��б����Ựת�ӹ����У������������ͷ���
                        $scope.selectedDate = $scope.searchDate[0];
                        $scope.selectedMessage = $scope.searchMessageType[0];
                        $scope.setSearchMessageType("user");
                        GetUserConsultListInfo.save({dateNum: 0,
                            CSDoctorId: "allCS",
                            pageNo: 1, pageSize: $scope.userConsultListPageSize}, function (data) {
                            refreshUserConsultListData(data);
                        });

                        //��ȡ�ͷ�ҽ���б�
                        GetCSDoctorList.save({}, function (data) {
                            $scope.CSList = [{"id":"allCS","name":"���пͷ�"}];
                            $.each(data.CSList,function(index,value){
                                $scope.CSList.push(value);
                            });
                            $scope.selectedCsList = $scope.CSList[0];
                            $scope.dateNumValue = angular.copy($scope.selectedDate.value);
                            $scope.CSDoctorIdValue =angular.copy($scope.selectedCsList.id);
                        });
                    }
                })
            };

            //��ȡ�û�����ϸ�����¼
            $scope.getUserRecordDetail = function (userName,userId,index) {
                $scope.doctorCreateConsultSessionChoosedUserId = userId;
                $scope.doctorCreateConsultSessionChoosedUserName = userName;
                $scope.setSessoin = index;
                $scope.loadingFlag = true;
                GetUserRecordDetail.save({pageNo:1,pageSize:$scope.userRecordDetailPageSize,
                    userId:userId,recordType:"all"}, function (data) {
                    $scope.loadingFlag = false;
                    $scope.currentClickUserName = userName;
                    $scope.currentClickUserId = userId;
                    $scope.currentUserConsultRecordDetail = data.records;
                    $scope.currentUserRecordDetailPage = data.currentPage;
                    $scope.totalUserRecordDetailPage = data.totalPage;
                    $.each($scope.currentUserConsultRecordDetail,function(index,value){
                        filterMediaData(value);
                    });
                });
            };

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
                }else if(action == "SkipPage"){
                    if($scope.info.recordDetailSkipNum > 0){

                        pageNum = parseInt($scope.info.recordDetailSkipNum);
                    }else{
                        alert("���������0�����֣�");
                        return;
                    }
                }
                $scope.loadingFlag = true;
                GetUserRecordDetail.save({pageNo:pageNum,
                    pageSize:$scope.userRecordDetailPageSize,
                    userId:$scope.currentClickUserId,recordType:recordType}, function (data) {
                    $scope.loadingFlag = false;
                    $scope.currentClickUserName = $scope.currentClickUserName;
                    $scope.currentClickUserId = $scope.currentClickUserId;
                    $scope.currentUserConsultRecordDetail = data.records;
                    $scope.currentUserRecordDetailPage = data.currentPage;
                    $scope.totalUserRecordDetailPage = data.totalPage;
                    $.each($scope.currentUserConsultRecordDetail,function(index,value){
                        filterMediaData(value);
                    });
                });
            };

            //������Ϣ��¼�����ȫ����ͼƬ�ȣ�
            $scope.setRecordType = function (searchRecordType) {
                $scope.recordType = searchRecordType;
                $scope.chooseUserRecordDetail("firstPage",$scope.recordType);
            };

            //��ѯĳ���ͷ���Ϣλ��ĳ��ʱ��ε���Ϣ
            $scope.getCsInfoByUserAndDate = function(Object){
                if (Object == 10000 || Object == 0 || Object == 7 || Object == 30) {
                    $scope.dateNumValue = angular.copy(Object);
                } else {
                    $scope.CSDoctorIdValue =angular.copy(Object);
                }
                $scope.loadingFlag = true;
                GetUserConsultListInfo.save({dateNum: $scope.dateNumValue,
                    CSDoctorId: $scope.CSDoctorIdValue,
                    pageNo: 1, pageSize:$scope.userConsultListPageSize}, function (data) {
                    $scope.loadingFlag = false;
                    refreshUserConsultListData(data);
                })
            };

            //������ѯ��¼����Ϣ�б����Ͻǵ��������ܣ�
            $scope.searchMessage = function () {
                if($scope.info.searchMessageContent == '' || $scope.info.searchMessageContent == null){
                    alert('��ѡ���ѯ���ݣ�');
                    return ;
                }else if($scope.messageType == '' || $scope.messageType == null){
                    alert('��ѡ���ѯ���ͣ�');
                    return ;
                }else{
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

            //������ѯ��¼
            $scope.setSearchMessageType = function (searchType) {
                $scope.messageType = searchType;
            };

            //���Ͻǵ�ˢ����Ϣ
            $scope.refreshUserList = function () {
                $scope.loadingFlag = true;
                GetUserConsultListInfo.save({dateNum: $scope.dateNumValue,
                    CSDoctorId: $scope.CSDoctorIdValue,
                    pageNo: 1,
                    pageSize: $scope.userConsultListPageSize}, function (data) {
                    $scope.loadingFlag = false;
                    refreshUserConsultListData(data);
                })
            };

            //���Ͻǵ���Ϣˢ��
            $scope.refreshCurrentUserRecordDetail = function(){
                $scope.getUserRecordDetail($scope.userConsultListInfo[0].userName,$scope.userConsultListInfo[0].userId,0);
            };

            //ҽ������һ������û��ĻỰ
            $scope.createDoctorConsultSession = function(){
                CreateDoctorConsultSession.save({userName:$scope.doctorCreateConsultSessionChoosedUserName,
                    userId:$scope.doctorCreateConsultSessionChoosedUserId},function(data){
                    if(data.result == "failure"){
                        alert("�޷�����Ự�����Ժ�����");
                    }else if(data.result == "existTransferSession"){
                        alert("���û����лỰ����ת��״̬���޷����䷢��Ự�����Ժ�����");
                    }else if(data.result == "noLicenseTransfer"){
                        alert("�Բ�����û��Ȩ�ޣ�����һ��������ѯ�û��ĻỰ");
                    }else if(data.result == "exceed48Hours"){
                        alert("�Բ����û���ѯ�Ѿ�������48Сʱ���޷������䷢��Ự");
                    }else if(data.result == "success"){
                        $state.go('doctorConsultFirst', {userId:data.userId,action:'createUserSession'});
                    }
                })
            };

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
                }else if(action == "SkipPage" ){
                    if($scope.info.userConsultListInfoSkipNum > 0){
                        pageNum = parseInt($scope.info.userConsultListInfoSkipNum);

                        $scope.currentUserConsultListDataPage = $scope.info.userConsultListInfoSkipNum;
                    }else{
                        alert("���������0�����֣�");
                        return;
                    }
                }
                $scope.loadingFlag = true;
                GetUserConsultListInfo.save({dateNum: $scope.dateNumValue,
                    CSDoctorId: $scope.CSDoctorIdValue,
                    pageNo: pageNum, pageSize: $scope.userConsultListPageSize}, function (data) {
                    $scope.loadingFlag = false;
                    refreshUserConsultListData(data);
                })
            };

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
            };

            //����ý������
            var filterMediaData = function (val) {
                if (val.type == "1"||val.type == "2" || val.type == "3") {
                    val.message = $sce.trustAsResourceUrl(angular.copy(val.message));
                }
            };
            //�����Ӵ��ڵĿ��ر���
            $scope.showFlag = {
                magnifyImg:false
            };
            $scope.tapImgButton = function (key,value) {
                $scope.showFlag[key] = !$scope.showFlag[key];
                $scope.imageSrc = value;
            };
            //���������ť����������������Ӧ���Ӵ���
            $scope.tapShowButton = function(key){
                $scope.showFlag[key] = !$scope.showFlag[key];
            };
        }])

