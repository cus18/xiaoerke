angular.module('controllers', ['ionic']).controller('guideAppointmentCtrl', [
        '$scope',
        function ($scope) {
            $scope.numLock = false;
            $scope.appointmentQuestions=[
                {
                    num:1,
                    ask:"如何在宝大夫上“预约挂号”？",
                    answerList:[
                        {
                            step: "第一步 进入宝大夫服务号，点击“预约大夫”菜单",
                            img: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/guideBook%2Fap_q1_1.png"
                        },
                        {
                            step: "第二步 搜索或根据实际情况选择对应的医院、疾病、时间筛选目标医生",
                            img: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/guideBook%2Fap_q1_2.png"
                        },
                        {
                            step: "第三步 选择要挂号的医生，查看相关医院、医生的信息",
                            img: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/guideBook%2Fap_q1_3.png"
                        },
                        {
                            step: "第四步 根据医生的具体信息，选择自己的就诊时间，填写预约单，点击“立即预约”即可完成挂号。",
                            img: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/guideBook%2Fap_q1_4.png"
                        }
                    ],
                    answer:"第一步 进入宝大夫服务号，点击“预约大夫”菜单",
                    lock:false
                },
                {
                    num:2,
                    ask:"挂号后，现场如何就医操作？",
                    answerList:[
                    {
                        step: "挂号成功后，应在预约时间半小时前到达就诊地点，并将预约成功短信出示给预约大夫，即可按医生引导完成就诊。"
                    }
                ],
                    answer:"挂号成功后，应在预约时间半小时前到达就诊地点，并将预约成功短信出示给预约大夫，即可按医生引导完成就诊。",
                    lock:false
                },
                {
                    num:3,
                    ask:"如何取消预约挂号？",
                    answerList:[
                        {
                            step: "取消预约挂号，需先进入“预约大夫”，点击右上角的图标（如图），进入我的预约：",
                            img: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/guideBook%2Fap_q3_1.png"
                        },
                        {
                            step: "在我的账户中查看“我的预约”，即可看到当前订单，按提示即可完成取消预约的操作。",
                            img: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/guideBook%2Fap_q3_2.png"
                        }
                    ],
                    answer:"取消预约挂号，需先进入“预约大夫”，点击右上角的图标（如图），进入我的预约：在我的账户中查看“我的预约”，即可看到当前订单，按提示即可完成取消预约的操作。",
                    lock:false
                },
                {
                    num:4,
                    ask:"就诊后如何进行评价？",
                    answerList:[
                        {
                            step: "就诊后，需先进入“预约大夫”，点击右上角的图标（如图），进入我的预约：",
                            img: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/guideBook%2Fap_q4_1.png"
                        },
                        {
                            step: "在我的账户中查看“待评价”，即可看到待评价订单，按提示即可完成评价的操作。",
                            img: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/guideBook%2Fap_q4_2.png"
                        }
                    ],
                    answer:"当您对咨询回复的内容不满意时，可在会话结束后的评价中说出您的感受，我们会及时与您取得联系，并尽快帮您解决问题。",
                    lock:false
                }
            ];

            $scope.selectQuestion=function(index){
                $scope.quesNum = index;
                if($scope.appointmentQuestions[$scope.quesNum].lock){
                    $scope.appointmentQuestions[$scope.quesNum].lock=false;
                }
                else{
                    for(var i=0;i<$scope.appointmentQuestions.length;i++){
                        $scope.appointmentQuestions[i].lock=false;
                    }
                    $scope.appointmentQuestions[ $scope.quesNum].lock=true;
                }
            }
    }])
