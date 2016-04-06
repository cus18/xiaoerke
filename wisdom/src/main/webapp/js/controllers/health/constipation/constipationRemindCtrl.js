angular.module('controllers', ['ionic']).controller('constipationRemindCtrl', [
    '$scope','$state','$stateParams','GetTemplateTasks','UpdatePlanTask','CancelPlan','GetTasksList','GetUserLoginStatus','$location','$http',
    function ($scope,$state,$stateParams,GetTemplateTasks,UpdatePlanTask,CancelPlan,GetTasksList,GetUserLoginStatus,$location,$http) {
        $scope.num = [];
        $scope.task = [];//存放排便、按摩数据
        $scope.taskFood = [];//存放食物数据
        $scope.taskSport = [];//存放运动数据
        $scope.timeLock=false;//选择时间开关
        $scope.openLock1=false;//饮食开关
        $scope.openLock2=false;/*运动开关*/
        $scope.cancelPlanImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png";/*取消计划图片*/
        $scope.openRemindImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png";/*打开全部提醒图片*/
        $scope.result = false;//是否显示取消计划
        $scope.saveResult = false;//保存禁止按钮
        /*时间列表*/
        $scope.timeList=["08:00","08:30","09:00","09:30","10:00","10:30",
            "11:00","11:30","12:00","12:30","13:00",
            "13:30","14:00","14:30","15:00","15:30",
            "16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00"];
        var cancelPlanFlag = 0;//取消计划开关
        var timeId = 0;
        var planInfoId = "";//存放计划id

        var pData = {logContent:encodeURI("BMGL_11")};
        $http({method:'post',url:'util/recordLogs',params:pData});

        $scope.$on('$ionicView.enter', function() {
            $scope.pageLoading = true;
            var routePath = "/ctpBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }
            })

            $scope.tasks = [];
            $scope.task=[];
            $scope.taskFood=[];
            $scope.taskSport=[];
            if($stateParams.flag=="first"){
                $scope.pageLoading = true;
                GetTemplateTasks.save({"type":"constipine"},function (data) {
                    $scope.pageLoading = false;
                    $scope.tasks = data.tasks.PlanTemplate;
                    for(var i=0;i<$scope.tasks.length;i++){
                        $scope.tasks[i].period = data.tasks.period;
                        $scope.tasks[i].period_unit = data.tasks.period_unit;
                        $scope.tasks[i].planTemplateId = data.tasks.PlanTemplateId;
                    }

                    $scope.task.push($scope.tasks[0]);
                    $scope.task[0].name = "便前按摩治疗";
                    $scope.task.push($scope.tasks[1]);
                    $scope.task[1].name = "排便习惯养成";
                    $scope.taskFood.push($scope.tasks[2]);
                    $scope.taskFood[0].name = "宝宝的早餐提醒";
                    $scope.taskFood.push($scope.tasks[3]);
                    $scope.taskFood[1].name = "宝宝的午餐提醒";
                    $scope.taskFood.push($scope.tasks[4]);
                    $scope.taskFood[2].name = "宝宝的晚餐提醒";
                    $scope.taskSport.push($scope.tasks[5]);
                    $scope.taskSport[0].name = "上午运动提醒";
                    $scope.taskSport.push($scope.tasks[6]);
                    $scope.taskSport[1].name = "下午运动提醒";
                });
            }else{
                $scope.result = true;
                $scope.tasks = [];
                GetTasksList.get(function (data) {
                    if(data.task==""){
                        $state.go('constipationLead');
                    }else{
                        planInfoId = data.task.id;
                        $scope.tasks = data.task.planTask;
                        $scope.task.push($scope.tasks[0]);
                        $scope.task[0].name = "便前按摩治疗";
                        $scope.task.push($scope.tasks[1]);
                        $scope.task[1].name = "排便习惯养成";
                        $scope.taskFood.push($scope.tasks[2]);
                        $scope.taskFood[0].name = "宝宝的早餐提醒";
                        $scope.taskFood.push($scope.tasks[3]);
                        $scope.taskFood[1].name = "宝宝的午餐提醒";
                        $scope.taskFood.push($scope.tasks[4]);
                        $scope.taskFood[2].name = "宝宝的晚餐提醒";
                        $scope.taskSport.push($scope.tasks[5]);
                        $scope.taskSport[0].name = "上午运动提醒";
                        $scope.taskSport.push($scope.tasks[6]);
                        $scope.taskSport[1].name = "下午运动提醒";
                        for(var i=0;i<$scope.tasks.length;i++){
                            if($scope.tasks[i].remindMe == false){
                                $scope.openRemindImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png";
                                return;
                            }
                        }
                    }
                });
            }
        });

        /*展开运动和饮食的多个开关*/
        $scope.open = function(type){
            if( type=="food"){
                if($scope.openLock1){
                    $scope.openLock1=false;
                }
                else{
                    $scope.openLock1=true;
                }
            }
            if( type=="sport"){
                if($scope.openLock2){
                    $scope.openLock2=false;
                }
                else{
                    $scope.openLock2=true;
                }
            }
        }

        //选择时间
        $scope.selectTime=function(id){
            $scope.timeLock=true;
            timeId = id;
        }
        //取消选择
        $scope.CancelSelectTime=function(){
            $scope.timeLock=false;
        }
        //更改时间
        $scope.updateTime=function(time){
            $scope.timeLock=false;
            for(var i=0;i<$scope.tasks.length;i++){
                if(timeId==$scope.tasks[i].id){
                    $scope.tasks[i].timeHappen = time;
                }
            }
        }
        /**
         * 更改提醒状态
         */
        $scope.mychecked = function(id){
            $scope.cancelPlanImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png";
            cancelPlanFlag = 0;//不取消计划
            for(var i=0;i<$scope.tasks.length;i++){
                if(id==$scope.tasks[i].id){
                    if($scope.tasks[i].remindMe == true){
                        $scope.tasks[i].remindMe = false;
                        $scope.openRemindImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png";
                    }else{
                        $scope.tasks[i].remindMe = true;
                    }
                    return;
                }
            }
        }

        //取消全部计划
        $scope.cancelPlan=function(){
            if ($scope.cancelPlanImg=="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png"){
                $scope.cancelPlanImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2_cur.png";
                $scope.openRemindImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png";
                cancelPlanFlag = 1;//取消全部计划
                /*for(var i=0;i<$scope.tasks.length;i++){
                    $scope.tasks[i].remindMe = false;
                }*/
            }
            else{
                $scope.cancelPlanImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png";
                cancelPlanFlag = 0;//不取消计划
                /*for(var i=0;i<$scope.tasks.length;i++){
                    $scope.tasks[i].remindMe = true;
                }*/
            }
        }

        //打开全部提醒
        $scope.openRemind=function(){
            if ($scope.openRemindImg== "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png"){
                $scope.openRemindImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2_cur.png";
                $scope.cancelPlanImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png";
                cancelPlanFlag = 0;//不取消计划
                for(var i=0;i<$scope.tasks.length;i++){
                    $scope.tasks[i].remindMe = true;
                }
            }
            else{
                $scope.openRemindImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir2.png";
                for(var i=0;i<$scope.tasks.length;i++){
                    $scope.tasks[i].remindMe = false;
                }
            }
        }

        /**
         * 保存提醒设置
         */
        $scope.save = function(){
            $scope.saveResult = true;
            if(cancelPlanFlag == 1){
                CancelPlan.get({"id":planInfoId}, function (data) {
                    if(data.resultCode == 0){
                        $state.go('constipationIndex');
                    }else{
                        alert("计划取消失败！");
                    }
                })
            }else{
                UpdatePlanTask.save($scope.tasks,function (data) {
                    if(data.resultCode == 0){
                        $state.go('constipationIndex');
                    }else{
                        alert("提醒设置保存失败！");
                    }
                });
            }
        }
        $scope.necessary = function(){
            $scope.tasks = [];
            $scope.task=[];
            $scope.taskFood=[];
            $scope.taskSport=[];
            $state.go('cMotherNecessary');
        }


    }]);

