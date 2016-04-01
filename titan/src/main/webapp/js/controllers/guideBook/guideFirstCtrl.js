angular.module('controllers', ['ionic']).controller('guideFirstCtrl', [
        '$scope','$state','SendWechatMessageToUser',
        function ($scope,$state,SendWechatMessageToUser) {
            $scope.title = "路线"
            $scope.title0 = "宝大夫";
            $scope.quesNumLock = "false";
            $scope.quesNumLock0 = false;
            $scope.quesNumLock1 = false;
            $scope.quesNumLock2 = false;
            $scope.allHospital = ["首都儿科研究所","北京儿童医院","北京友谊医院","北京中日友好医院","中国人民解放军海军总医院",
                "北京中医医院","北京中医药大学附属东方医院","北京鼓楼中医院","北大口腔医院第五门诊部","北京京都儿童医院",
                "北京大学第六医院","第二炮兵总医院","中国人民解放军第306医院","北京首儿李桥儿童医院","北京爱育华妇儿医院",
                "北京市顺义区妇幼保健院","大兴区妇幼保健院"];

            $scope.hotQuestions=[
                {
                    num:1,
                    ask:"1.如何在专场的时候向指定的医生提问？",
                    answer:"当有专家在线时，您可向专家直接提问，在提问的第一句话中加入提问对象，如，“请问梁医生……”"
                },
                {
                    num:2,
                    ask:"2.挂号后，现场如何就医操作？",
                    answer:"挂号成功后，应在预约时间半小时前到达就诊地点，并将预约成功短信出示给预约大夫，即可按医生引导完成就诊。"
                }/*,
                {
                    num:3,
                    ask:"3.这里的内容都是出自郑玉巧本人吗？",
                    answer:"宝大夫是郑玉巧医生目前唯一合作的在线咨询医疗机构，【郑玉巧育儿经】中的内容全部为郑玉巧版权知识库内容，宝妈宝爸们可放心查阅使用，并对使用中的问题可在每周三下午2:00-4:00“郑玉巧在线”时间与郑玉巧医生进行直接沟通。"
                }*/
            ];
            $scope.selectHotQues = function(index){
                $scope.quesNum =index;
                if($scope.quesNum==0){
                    if( $scope.quesNumLock0){
                        $scope.quesNumLock0 = false;
                    }
                    else{
                        $scope.quesNumLock0 = true;
                    }
                    $scope.quesNumLock1 = false;
                    $scope.quesNumLock2 = false;
                }
                if($scope.quesNum==1){
                    if( $scope.quesNumLock1){
                        $scope.quesNumLock1 = false;
                    }
                    else{
                        $scope.quesNumLock1 = true;
                    }
                    $scope.quesNumLock0 = false;
                    $scope.quesNumLock2 = false;
                }
                if($scope.quesNum==2){
                    if( $scope.quesNumLock2){
                        $scope.quesNumLock2= false;
                    }
                    else{
                        $scope.quesNumLock2 = true;
                    }
                    $scope.quesNumLock0 = false;
                    $scope.quesNumLock1 = false;
                }
            }
            $scope.consultDoc = function(){
                SendWechatMessageToUser.save({},function(data){
                });
                WeixinJSBridge.call('closeWindow');
            }
    }])
