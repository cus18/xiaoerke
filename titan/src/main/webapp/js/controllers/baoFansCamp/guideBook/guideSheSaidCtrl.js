angular.module('controllers', ['ionic']).controller('guideSheSaidCtrl', [
        '$scope','$state',
        function ($scope,$state) {
            $scope.numLock = false;
            $scope.sheSaidQuestions=[
                {
                    num:1,
                    ask:"这里的内容都是出自郑玉巧本人吗？",
                    answerList:[
                        {
                            step: "宝大夫是郑玉巧医生目前唯一合作的在线咨询医疗机构，【郑玉巧育儿经】中的内容全部为郑玉巧版权知识库内容，宝妈宝爸们可放心查阅使用，并对使用中的问题可在每周三下午2:00-4:00“郑玉巧在线”时间与郑玉巧医生进行直接沟通。"

                        }
                    ],
                    answer:"宝大夫是郑玉巧医生目前唯一合作的在线咨询医疗机构，【郑玉巧育儿经】中的内容全部为郑玉巧版权知识库内容，宝妈宝爸们可放心查阅使用。",
                    lock:false
                },
                {
                    num:2,
                    ask:"从哪里可以获得【郑玉巧育儿经】的内容？",
                    answerList:[
                        {
                            step: "平台每月会推送4次会推送时令的话题，同时，开放【郑玉巧育儿经】菜单入口，宝妈宝爸们可在这里进行相关问题的搜索和查询。",
                            img: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/guideBook%2Fsaid_q2_1.png"
                        }
                    ],
                    answer:"平台每月会推送4次会推送时令的话题，同时，开放【郑玉巧育儿经】菜单入口，宝妈宝爸们可在这里进行相关问题的搜索和查询。",
                    lock:false
                }
            ];
            $scope.selectQuestion=function(index){
                $scope.quesNum = index;
                if($scope.sheSaidQuestions[$scope.quesNum].lock){
                    $scope.sheSaidQuestions[$scope.quesNum].lock=false;
                }
                else{
                    for(var i=0;i<$scope.sheSaidQuestions.length;i++){
                        $scope.sheSaidQuestions[i].lock=false;
                    }
                    $scope.sheSaidQuestions[ $scope.quesNum].lock=true;
                }
            }


    }])
