angular.module('controllers', ['ionic']).controller('momNutritionTestCtrl', [
    '$scope','$state','$stateParams','SaveMarketingActivities',
    function ($scope,$state,$stateParams,SaveMarketingActivities) {

        $scope.pageLoading = false;
        $scope.score = 0;

        $scope.testList=[
            {
                num:1,
                img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest1.jpg",
                title:"1. 母乳是贫铁的食物吗？",
                questionList:[
                    {   select: "A. 是",
                        checked:false},
                    {   select: "B. 不是",
                        checked:false}
                ],
                result:"no",
                answer:0,
                checked:false
            },
            {
                num:2,
                img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest2.jpg",
                title:"2. 宝宝满6月龄后应优先添加什么辅食呢？",
                questionList:[
                    {
                        select: "A. 富铁食物如强化铁的米粉和肉泥",
                        checked:false
                    },
                    {
                        select: "B. 富含维生素、矿物质的蔬菜、水果泥",
                        checked:false
                    }
                ],
                result:"no",
                answer:0,
                checked:false

            },
            {
                num:3,
                img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest3.jpg",
                title:"3. 宝宝“枕秃”是缺钙引起的吗？",
                questionList:[
                    {
                        select: "A. 是",
                        checked:false
                    },
                    {select:
                        "B. 不一定",
                        checked:false
                    }
                ],
                result:"no",
                answer:1,
                checked:false
            },
            {
                num:4,
                img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest4.jpg",
                title:"4. 怎么给宝宝转奶呢？",
                questionList:[
                    {
                        select: "A. 在旧奶粉中少量添加新奶粉，逐渐加量，直至全部添加新奶粉",
                        checked:false
                    },
                    {
                        select: "B. 旧奶粉需要停一天，然后在新奶粉中添加一些米粉，再逐步去掉",
                        checked:false
                    }
                ],
                result:"no",
                answer:0,
                checked:false
            },
            {
                num:5,
                img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest5.jpg",
                title:"5. 宝宝最初的米粉应该怎么吃呢？",
                questionList:[
                    {
                        select: "A. 做成稀米糊后放进奶瓶里给宝宝吃，让宝宝慢慢适应",
                        checked:false
                    },
                    {
                        select: "B. 调成泥状（由稀到稠），用勺子喂给宝宝吃",
                        checked:false
                    }
                ],
                result:"no",
                answer:1,
                checked:false
            },
            {
                num:6,
                img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest6.jpg",
                title:"6. 宝宝1岁该断奶了吗？",
                questionList:[
                    {
                        select: "A. 是的，母乳6个月后没营养，而且宝宝依恋母乳，没法儿好好吃饭",
                        checked:false
                    },
                    {
                        select: "B. 不是的，母乳喂养应尽量坚持到宝宝满2岁",
                        checked:false
                    }
                ],
                result:"no",
                answer:1,
                checked:false
            },
            {
                num:7,
                img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest7.jpg",
                title:"7. 宝宝到了一定年龄还需要定量补充奶制品吗？",
                questionList:[
                    {
                        select: "A. 需要",
                        checked:false
                    },
                    {
                        select: "B. 不需要",
                        checked:false
                    }
                ],
                result:"no",
                answer:0,
                checked:false
            },
            {
                num:8,
                img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest8.jpg",
                title:"8. 牛奶和富含维C的水果或果汁能一起吃吗？",
                questionList:[
                    {
                        select: "A. 当然可以吃啦，这样会更有消化吸收",
                        checked:false
                    },
                    {
                        select: "B. 最好不要哦，因为两者会产生化学反应影响吸收",
                        checked:false
                    }
                ],
                result:"no",
                answer:0,
                checked:false
            },
            {
                num:9,
                img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest9.jpg",
                title:"9.含钙丰富的食物主要包括哪些？",
                questionList:[
                    {
                        select: "A. 奶制品、豆制品、鱼虾类",
                        checked:false
                    },
                    {
                        select: "B. 牛羊肉类，谷薯类",
                        checked:false
                    }
                ],
                result:"no",
                answer:0,
                checked:false
            },
            {
                num:10,
                img:"http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/market%2FmomNutrition%2Ftest10.jpg",
                title:"10. 以下哪种食物补铁效果好呢？",
                questionList:[
                    {
                        select: "A.猪肝",
                        checked:false
                    },
                    {
                        select: "B. 鸡胸肉",
                        checked:false
                    },
                    {
                        select: "C. 红枣",
                        checked:false
                    }
                ],
                result:"no",
                answer:0,
                checked:false
            }
        ];

        var socre = 0;

        $scope.$on('$ionicView.enter', function(){
            socre = 0;
        });

        $scope.selectAnswer = function(index,parentNum){
            $scope.selectNum = index;
            $scope.parentNum=parentNum+1;
            $scope.testList[parentNum].result=index;
            for(var i=0;i< $scope.testList[parentNum].questionList.length;i++){
                $scope.testList[parentNum].questionList[i].checked=false;
            }
            $scope.testList[parentNum].questionList[index].checked=true;
        }


        $scope.lookResult = function(){
            var result = "";
            for(var i=0;i<$scope.testList.length;i++){
                if($scope.testList[i].result==$scope.testList[i].answer){
                    socre++;
                }
                    result += ","+$scope.testList[i].result;
            }

            SaveMarketingActivities.save({"result":result,"score":socre}, function (data){
                if(data.id!=""){
                    $state.go('momNutritionResult',{result:socre,id:data.id});
                }else{
                    alert("提交失败！");
                }
            });

        };



    }]);

