angular.module('controllers', ['ionic']).controller('constipationIndexCtrl', [
    '$scope','$state','$stateParams','$interval','$filter','AppraisalList','ConfirmDetail','GetConfirmSituation',
    'CancelPlan','GetTasksList','GetTasksInfo','UpdatePlanTask','PushTicket','GetTemplateTasks',
    'GetFoodList','SaveFoodList','GetDietList','TaskListConfirm','SendWechatMessageToUser',
    'GetUserLoginStatus','$location','$http','SaveManagementInfo',
    function ($scope,$state,$stateParams,$interval,$filter,AppraisalList,ConfirmDetail,GetConfirmSituation,
              CancelPlan,GetTasksList,GetTasksInfo,UpdatePlanTask,PushTicket,GetTemplateTasks,
              GetFoodList,SaveFoodList,GetDietList,TaskListConfirm,SendWechatMessageToUser,GetUserLoginStatus,$location,$http,SaveManagementInfo) {

        $scope.scrollNum=0;//评论滚动的初始位置值
        $scope.ulHeight=$("#comment").height();
        $scope.commentList = [];
        $scope.planTasks = [];
        var planTemplateId = "";
        var planInfoId = "";
        var foodNum = 0;
        var sportNum = 0;
        var finishId = [];
        var Ids ="";

        var pData = {logContent:encodeURI("BMGL_1")};
        $http({method:'post',url:'ap/util/recordLogs',params:pData});

        $scope.goManagement = function(){
            window.location.href = "ap/firstPage/healthPlan?type=second";
        }
        //轮播图图片初始化
        $scope.bannerImg = [
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fconstipation_banner1.png",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fconstipation_banner2.png",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fconstipation_banner4.png",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fconstipation_banner5.png"
        ];

        $scope.imgList = [
            {images:[
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_knead1.png",
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_knead2.png",
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_knead3.png"]},
            {images:[
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_defecate1.png",
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_defecate2.png",
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_defecate3.png"]},
            { images:[
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_food1.png",
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_food2.png",
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_food3.png"]},
            {images:[
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_sport1.png",
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_sport2.png",
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_sport3.png"]},
            {images:[
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_necessary1.png",
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_necessary2.png",
                "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Findex_necessary3.png"]}
        ];

        /*
        滚动评论
        **/
        $interval(function() {
            if($scope.scrollNum==40-$scope.ulHeight){
                $scope.scrollNum=0;
            }
            else{
                $scope.scrollNum-=40;
            }
            $("#comment").css("top",+$scope.scrollNum+"px");
            if($scope.commentList.length!=0){
                if($scope.scrollNum==-40*($scope.commentList.length-1)){
                    $scope.scrollNum=40;
                }
            }else{
                if($scope.scrollNum==-40*(10)){
                    $scope.scrollNum=40;
                }
            }

           // $scope.$digest(); // 通知视图模型的变化
        }, 5000);

        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/ap/ctpBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                } else{
                    SaveManagementInfo.get({planTemplateId:2},function(data){
                        if(data.resultMsg=="OK"){
                            //获取各项任务列表
                            $scope.getTaskList();
                        }
                        else{
                            window.location.href = "ap/firstPage/healthPlan";
                        }
                    });
                }
            })
        });

        /**
         * 获取各项任务列表
         */
        $scope.getTaskList = function(){
            $scope.planTasks = [];
            GetTasksList.get(function (data) {
                 if(data.task==""){
                    $state.go('constipationLead');
                 }else{
                     var myTasks = data.task.planTask;
                     var temp = (data.task.server_time-data.task.update_time)/(24*60*60*1000);
                     var currtime = $filter('date')(data.task.server_time,'HH');
                     planTemplateId =parseInt(data.task.planTemplateId);
                     planInfoId = data.task.id;
                     //获取评论
                     $scope.getAppraisalList();
                     myTasks[0].head = "给宝宝按摩";
                     myTasks[0].detail = "按摩详细";
                     myTasks[1].head = "给宝宝排便";
                     myTasks[1].detail = "排便详细";
                     myTasks[2].head = "给宝宝吃早餐";
                     myTasks[2].detail = "饮食详细";
                     myTasks[3].head = "给宝宝吃午餐";
                     myTasks[3].detail = "饮食详细";
                     myTasks[4].head = "给宝宝吃晚餐";
                     myTasks[4].detail = "饮食详细";
                     myTasks[5].head = "给宝宝运动";
                     myTasks[5].detail = "运动详细";
                     myTasks[6].head = "给宝宝运动";
                     myTasks[6].detail = "运动详细";
                     myTasks[7].head = "必备物品采购";
                     myTasks[7].detail = "必备详细";
                     myTasks[7].status = "打卡";
                     TaskListConfirm.get({"planInfoId":planInfoId},function(data){
                         Ids = data.defecateId+","+data.massageId+","+data.foodIds+data.sportIds;
                         if(Ids == ""){
                             finishId = [];
                         }else{
                             finishId = Ids.split(",");
                         }
                         foodNum = data.food;
                         sportNum = data.sport;
                         $scope.planTasks.push(myTasks[0]);
                         $scope.planTasks.push(myTasks[1]);
                         if(currtime<11){
                             $scope.planTasks.push(myTasks[2]);
                             $scope.planTasks.push(myTasks[5]);
                         }else if(currtime>=11&&currtime<=13){
                             $scope.planTasks.push(myTasks[3]);
                             $scope.planTasks.push(myTasks[5]);
                         }else{
                             $scope.planTasks.push(myTasks[4]);
                             $scope.planTasks.push(myTasks[6]);
                         }
                         $scope.planTasks.push(myTasks[7]);
                         $scope.planTasks[0].status = data.defecate;
                         $scope.planTasks[0].days = data.defecateDays;
                         $scope.planTasks[1].status = data.massage;
                         $scope.planTasks[1].days = data.massageDays;
                         if(data.food ==3){
                             $scope.planTasks[2].status = "完成";
                         }else if(data.food ==0){
                             $scope.planTasks[2].status = "打卡";
                         }else{
                             $scope.planTasks[2].status = data.food+"/"+3;
                         }
                         $scope.planTasks[2].days = data.foodDays;
                         if(data.sport ==2){
                             $scope.planTasks[3].status = "完成";
                         }else if(data.sport ==0){
                             $scope.planTasks[3].status = "打卡";
                         }else{
                             $scope.planTasks[3].status = data.sport+"/"+2;
                         }
                         $scope.planTasks[3].days = data.sportDays;

                         if(data.motherMust.split(",").length ==5){
                             $scope.planTasks[4].status = "完成";
                         }else if(data.motherMust.split(",").length ==4){
                             $scope.planTasks[4].status = 3+"/"+4;
                         }else if(data.motherMust.split(",").length ==3){
                             $scope.planTasks[4].status = 2+"/"+4;
                         }else if(data.motherMust.split(",").length ==2){
                             $scope.planTasks[4].status = 1+"/"+4;
                         }else if(data.motherMust.split(",").length ==1){
                             $scope.planTasks[4].status = "打卡";
                         }
                     });
                         if(parseInt(temp)>3){
                             //获取建立任务后打卡情况
                             GetConfirmSituation.get({"planId":data.task.id},function(data){
                                 if(data.resultType=="none"){//任务都没有打卡
                                     $state.go('constipationFollow',{type:"none",planInfoId:planInfoId});
                                 }else if(data.resultType=="part"){//部分任务打卡
                                     $state.go('constipationFollow',{type:"part",planInfoId:planInfoId});
                                 }else if(data.resultType=="all"){//全部任务都打卡
                                     $state.go('constipationFollow',{type:"all",planInfoId:planInfoId});
                                 }
                             });
                         }
                     }
             });
        }
        /**
         * 获取评论列表
         */
        $scope.getAppraisalList = function(){
            $scope.pageLoading = true;
            AppraisalList.get({"id":planTemplateId,"pageNo":1,"pageSize":30}, function (data){
                $scope.pageLoading = false;
                $scope.commentList = data.appraisalList;
            });
        }
        /**
         * 任务打卡
         */
        $scope.playCard = function(id,type,index){
            var flag = 0;
            if(type == "motherMust"){
                var pData = {logContent:encodeURI("BMGL_7")};
                $http({method:'post',url:'ap/util/recordLogs',params:pData});
                $state.go('cMotherNecessary');
            }else{
                if($scope.planTasks[index].status == "完成"){
                    alert("此计划今日已完成打卡！");
                }else{
                    if(finishId.length==0){
                        $scope.getplayCard(id,type,index);
                    }else{
                        for(var i=0;i<finishId.length;i++){
                            if(finishId[i]==id){
                                flag = 1;
                            }
                        }
                        if(flag == 1){
                            alert("此计划今日已完成打卡！");
                        }else{
                            $scope.getplayCard(id,type,index);
                        }
                    }
                }
            }
        }

        $scope.getplayCard = function(id,type,index){
            PushTicket.save({"plan_info_task_id":id},function (data) {
                if(data.resultCode ==0){
                    finishId.push(id);
                    if(type == "defecate"||type == "massage"){
                        $scope.planTasks[index].status ="完成";
                        var pData = {logContent:encodeURI("BMGL_3")};
                        $http({method:'post',url:'ap/util/recordLogs',params:pData});
                    }else{
                        if(type == "food"){
                            var pData = {logContent:encodeURI("BMGL_5")};
                            $http({method:'post',url:'ap/util/recordLogs',params:pData});
                            foodNum++;
                            if(foodNum ==3){
                                $scope.planTasks[index].status = "完成";
                            }else if(foodNum ==0){
                                $scope.planTasks[index].status = "打卡";
                            }else{
                                $scope.planTasks[index].status = foodNum+"/"+3;
                            }
                        }else if(type == "sport"){
                            var pData = {logContent:encodeURI("BMGL_6")};
                            $http({method:'post',url:'ap/util/recordLogs',params:pData});
                            sportNum++;
                            if(sportNum ==2){
                                $scope.planTasks[index].status = "完成";
                            }else if(sportNum ==0){
                                $scope.planTasks[index].status = "打卡";
                            }else{
                                $scope.planTasks[index].status = sportNum+"/"+2;
                            }
                        }
                    }
                }else{
                    alert("此任务打卡失败！");
                }
            });
        }

        /**
         *  点击任务详细
         */
        $scope.selectTask=function(index,type){
            $scope.index=index;
            if($scope.index==0){
                $state.go("constipationKnead",{id:planTemplateId,planInfoId:planInfoId,planTaskType:type});
            }
            if($scope.index==1){
                $state.go("constipationDefecate",{id:planTemplateId,planInfoId:planInfoId,planTaskType:type});
            }
            if($scope.index==2){
                $state.go("constipationFood",{id:planTemplateId,planInfoId:planInfoId,planTaskType:type});
            }
            if($scope.index==3){
                $state.go("constipationSports",{id:planTemplateId,planInfoId:planInfoId,planTaskType:type});
            }
            if($scope.index==4){
                $state.go("cMotherNecessary");
            }
        }

        $scope.zixun = function(){
            var pData = {logContent:encodeURI("BMGL_9")};
            $http({method:'post',url:'ap/util/recordLogs',params:pData});
            SendWechatMessageToUser.save({},function(data){
            });
            WeixinJSBridge.call('closeWindow');
        }

        $scope.yuyue = function () {
            window.location.href = "http://s11.baodf.com//xiaoerke-appoint/ap/firstPage/appoint";
        }


    }]);

