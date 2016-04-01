angular.module('controllers', ['ionic']).controller('guideConsultCtrl', [
        '$scope','$state',
        function ($scope,$state) {

            $scope.numLock = false;
            $scope.consultQuestions=[
                {
                    num:1,
                    ask:"怎样开始在线咨询？",
                    answerList:[
                        {
                            step: "在 微信中打开“宝大夫”服务号，点击左下角“小键盘”输入文字或语音，系统将自动转接在线医生，开启在线咨询对话。",
                            img: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/guideBook%2Fcon_q1_1.png"
                        }
                    ],
                    answer:"在 微信中打开“宝大夫”服务号，点击左下角“小键盘”输入文字或语音，系统将自动转接在线医生，开启在线咨询对话。",
                    lock:false
                },
                {
                    num:2,
                    ask:"在线咨询的医生是怎样的资质？",
                    answerList:[
                        {
                            step: "宝大夫在线咨询的签约医生全部为有临床5年以上的儿科医生，并承诺3分钟内回复您的问题。"
                        }
                    ],
                    answer:"宝大夫在线咨询的签约医生全部为有临床5年以上的儿科医生，并承诺1分钟内回复您的问题。",
                    lock:false
                },
                {
                    num:3,
                    ask:"如何在专场的时候向指定的医生提问？",
                    answerList:[
                        {
                            step: "当有专家在线时，您可向专家直接提问，在提问的第一句话中加入提问对象，如，“请问梁平医生……”"
                        }
                    ],
                    answer:"当有专家在线时，您可向专家直接提问，在提问的第一句话中加入提问对象，如，“请问梁平医生……”",
                    lock:false
                },
                {
                    num:4,
                    ask:"对医生的回复不满意怎么办？",
                    answerList:[
                        {
                            step: "当您对咨询回复的内容不满意时，可在会话结束后的评价中说出您的感受，我们会及时与您取得联系，并尽快帮您解决问题。"
                        }
                    ],
                    answer:"当您对咨询回复的内容不满意时，可在会话结束后的评价中说出您的感受，我们会及时与您取得联系，并尽快帮您解决问题。",
                    lock:false
                }
            ];

            $scope.selectQuestion=function(index){
                $scope.quesNum = index;
                if($scope.consultQuestions[$scope.quesNum].lock){
                    $scope.consultQuestions[$scope.quesNum].lock=false;
                }
                else{
                    for(var i=0;i<$scope.consultQuestions.length;i++){
                        $scope.consultQuestions[i].lock=false;
                    }
                    $scope.consultQuestions[ $scope.quesNum].lock=true;
                }
            }
    }])
