angular.module('controllers', ['ionic']).controller('testV2Ctrl', [
        '$scope','$state',
        function ($scope) {
            $scope.questionNum=0;//获取当前界面显示题页
            $scope.userAnswerRight=false;//错误答案显示
            $scope.userAnswerWrong=false;//正确答案显示
            $scope.first=true;//判断首页显示
            $scope.end=false;//做完以后显示连接
            $scope.scoreAll=0;//答案对应分数
            $scope.score=[0,0,0,0,0];

           //初始化测试题
            $scope.inits=[
                {
                    num:"第一题",
                    title:"以下哪个原因，不会导致宝宝营养过剩？",
                    questionList:[
                        {select: "A.6个月时喂固体食物"},
                        {select: "B.喂食过多、过浓的配方奶"},
                        {select: "C.喂养过于随意"},
                        {select: "D.宝贝没有足够的运动机会"}
                    ],
                    result:"A",
                    answer:0
                },
                {
                    num:"第二题",
                    title:"以下关于“宝宝营养”的观点是正确的？",
                    questionList: [
                        {select: "A.宝宝胖说明宝宝很健壮"},
                        {select: "B.科学提倡按需喂养，所以宝宝一哭就要喂"},
                        {select: "C.早产儿不宜吃母乳"},
                        {select: "D.2岁以内的宝宝尽量不要食用鲜奶"}
                    ],
                    result:"D",
                    answer:3
                },
                {
                    num:"第三题",
                    title:"关于过度喂养，以下哪个选项是正确的？",
                    questionList: [
                        {select: "A.长期用奶瓶喂养，容易导致宝宝过度喂养"},
                        {select: "B.用食物安慰、鼓励婴儿不会导致宝宝过度喂养"},
                        {select: "C.母乳吃得太频繁会导致宝宝过度喂养"},
                        {select: "D.过量喂养一段时间一定会造成宝宝肥胖"}
                    ],
                    result:"A",
                    answer:0
                },
                {
                    num:"第四题",
                    title:"宝宝肥胖，以下哪个说法是错误的？",
                    questionList:[
                        {select: "A.宝宝超重不代表肥胖"},
                        {select: "B.宝宝肥胖跟是否母乳喂养没太大关系"},
                        {select: "C.肥胖的宝宝容易得慢性病"},
                        {select: "D.宝宝小时候胖没关系，大了长高后体重就会正常"}
                    ],
                    result:"D",
                    answer:3

                },
                {
                    num:"第五题",
                    title:"以下哪种食物，不能够帮助宝宝补钙？",
                    questionList:[
                        {select: "A.牛奶"},
                        {select: "B.酸奶"},
                        {select: "C.猪肝"},
                        {select: "D.橙汁"}
                    ],
                    result:"C",
                    answer:2
                }
            ];
            $scope.answers = [];


            //测试题对应的图片
            $scope.imgs=[
                {img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/testV2%2Ftest_ques1.png"},
                {img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/testV2%2Ftest_ques2.png"},
                {img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/testV2%2Ftest_ques3.png"},
                {img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/testV2%2Ftest_ques4.png"},
                {img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/testV2%2Ftest_ques5.png"}
            ];
            $scope.cancel = function(){
                $scope.userAnswerRight=false;
                $scope.userAnswerWrong=false;
            }
            /**
             * 开始答题
             */
            $scope.begin= function () {
                $scope.first=false;
            }

            /**
             * 选择那个答案
             */
            $scope.selectradio = function(selectValue,index){
                $scope.index=index;

                if($scope.answers[$scope.questionNum]!=undefined){
                    alert("此题您已作答，答案为"+$scope.answers[$scope.questionNum]);
                }else{

                    switch (index){
                        case 0:
                            $scope.answers[$scope.questionNum]="A";
                            break;
                        case 1:
                            $scope.answers[$scope.questionNum]="B";
                            break;
                        case 2:
                            $scope.answers[$scope.questionNum]="C";
                            break;
                        case 3:
                            $scope.answers[$scope.questionNum]="D";
                            break;
                    }
                /**
                 * 判断所选题内容是否与答案相符
                 */
                    if($scope.inits[$scope.questionNum].answer==index){
                        $scope.userAnswerRight=true;
                        $scope.userAnswerWrong=false;
                        $scope.score[$scope.questionNum]= 20;
                        console.log("单题分数：","第"+$scope.questionNum+"题", $scope.score[$scope.questionNum]);
                    }
                    else{
                        $scope.userAnswerRight=false;
                        $scope.userAnswerWrong=true;
                        $scope.score[$scope.questionNum]= 0;
                    }
                }
            }
            /**
             * 上一题
             */
            $scope.pre=function(num){
                $scope.questionNum=num;

                $scope.index=null;
                if($scope.questionNum>0){
                    $scope.questionNum--;
                }
                else{
                    $scope.questionNum=0;
                    alert("这是第一题");
                }

            };

            /**
             * 下一题
             */
            $scope.next=function(num){
                $scope.questionNum=num;
                $scope.scoreAll=0;
                $scope.index=null;
                if($scope.questionNum<$scope.inits.length-1){
                    $scope.questionNum++;

                }
                else{
                    $scope.questionNum=$scope.inits.length-1;
                    $scope.end=true;
                    for(var i=0;i<$scope.inits.length;i++){
                        $scope.scoreAll=$scope.scoreAll+$scope.score[i];
                    }
                    console.log("总得分：", $scope.scoreAll);
                }
            };

    }])
