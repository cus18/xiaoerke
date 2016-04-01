angular.module('controllers', ['ionic']).controller('constipationFoodCtrl', [
    '$scope','$state','$stateParams','$location','$ionicScrollDelegate','ConfirmDetail','AppraisalList',
    'PushTicket','GetTasksInfo','$sce','GetUserLoginStatus','$location','$http',
    function ($scope,$state,$stateParams,$location,$ionicScrollDelegate,ConfirmDetail,AppraisalList,
              PushTicket,GetTasksInfo,$sce,GetUserLoginStatus,$location,$http) {

        $scope.heartImg ="ihttp://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card0.png";
        //初始化一日三餐
        $scope.foodList = [];
        var amstatus = "";
        var pmstatus= "";
        var mmstatus= "";
        var amtime = "";
        var pmtime = "";
        var mmtime = "";
        var amid="";
        var pmid="";
        var mmid="";
        var num=0;

        var pData = {logContent:encodeURI("BMGL_51")};
        $http({method:'post',url:'ap/util/recordLogs',params:pData});

        $scope.$on('$ionicView.enter', function() {
            $scope.pageLoading = true;
            var routePath = "/ap/ctpBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    $scope.foodList = [];
                    $scope.momtime = moment().format('HH:mm');
                    var momHours = moment().format('HH');
                    //获取打卡详情
                    ConfirmDetail.get({"planInfoId":parseInt($stateParams.planInfoId),"type":$stateParams.planTaskType}, function (data) {
                        $scope.continue = data.continue;
                        $scope.total = data.total;
                        amtime=data.amtime;
                        pmtime=data.pmtime;
                        mmtime=data.mmtime;
                        amid=data.amid;
                        pmid=data.pmid;
                        mmid=data.mmid;
                        if(data.am=="yes"&&data.pm=="yes"&&data.mm=="yes"){
                            $scope.heartImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card_full.png";
                        }else if(data.am=="no"&&data.pm=="no"&&data.mm=="no"){
                            $scope.heartImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card0.png";
                        }else if(data.am=="yes"){
                            $scope.heartImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card3_1.png";
                        }else if(data.pm=="yes"){
                            $scope.heartImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card3_1.png";
                        }else if(data.mm=="yes"){
                            $scope.heartImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card3_1.png";
                        }else{
                            $scope.heartImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card3_2.png";
                        }
                        if(data.am=="yes"){
                            amstatus="完成";
                            num++;
                        }
                        if(data.am=="no"){
                            amstatus="打卡";
                        }
                        if(data.pm=="yes"){
                            pmstatus="完成";
                            num++;
                        }
                        if(data.pm=="no"){
                            pmstatus="打卡";
                        }
                        if(data.mm=="yes"){
                            mmstatus="完成";
                            num++;
                        }
                        if(data.mm=="no"){
                            mmstatus="打卡";
                        }


                    });
                    //获取评论
                    $scope.pageLoading = true;
                    AppraisalList.get({"id":$stateParams.id,"pageNo":1,"pageSize":2}, function (data){
                        $scope.pageLoading = false;
                        $scope.commentList = data.appraisalList;
                        $scope.commenttotal = data.total;
                    });
                    //获取食材内容
                    GetTasksInfo.get({"type":$stateParams.planTaskType},function(data){
                        var amcontent = $sce.trustAsHtml(angular.copy(data.am.content));
                        var pmcontent = $sce.trustAsHtml(angular.copy(data.pm.content));
                        var mmcontent = $sce.trustAsHtml(angular.copy(data.mm.content));
                        $scope.foodList.push(data.am);
                        $scope.foodList.push(data.mm);
                        $scope.foodList.push(data.pm);
                        $scope.foodList[0].food=[];
                        $scope.foodList[1].food=[];
                        $scope.foodList[2].food=[];
                        $scope.foodList[0].mid = amid;
                        $scope.foodList[0].content = amcontent;
                        $scope.foodList[0].time = amtime;
                        $scope.foodList[0].status = amstatus;
                        $scope.foodList[0].meal="宝宝的早餐";
                        for(var i=1;i<data.am.keywords.split(" ").length;i++){
                            $scope.foodList[0].food.push(data.am.keywords.split(" ")[i]);
                        }
                        $scope.foodList[0].openImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/" +
                            "constipation%2Farr_green_down.png";
                        $scope.foodList[0].openImg1="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/" +
                            "constipation%2Farr_green_down.png";
                        $scope.foodList[0].openImg2="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/" +
                            "constipation%2Farr_green_up.png";
                        $scope.foodList[0].openText="展开";
                        $scope.foodList[1].mid = mmid;
                        $scope.foodList[1].content = mmcontent;
                        $scope.foodList[1].time = mmtime;
                        $scope.foodList[1].status = mmstatus;
                        $scope.foodList[1].meal="宝宝的午餐";
                        for(var i=1;i<data.mm.keywords.split(" ").length;i++){
                            $scope.foodList[1].food.push(data.mm.keywords.split(" ")[i]);
                        }
                        $scope.foodList[1].openImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Farr_green_down.png";
                        $scope.foodList[1].openImg1="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Farr_green_down.png";
                        $scope.foodList[1].openImg2="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Farr_green_up.png";
                        $scope.foodList[1].openText="展开";
                        $scope.foodList[2].mid = pmid;
                        $scope.foodList[2].content = pmcontent;
                        $scope.foodList[2].time = pmtime;
                        $scope.foodList[2].status = pmstatus;
                        $scope.foodList[2].meal="宝宝的晚餐";
                        for(var i=1;i<data.pm.keywords.split(" ").length;i++){
                            $scope.foodList[2].food.push(data.pm.keywords.split(" ")[i]);
                        }
                        $scope.foodList[2].openImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/" +
                            "constipation%2Farr_green_down.png";
                        $scope.foodList[2].openImg1="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/" +
                            "constipation%2Farr_green_down.png";
                        $scope.foodList[2].openImg2="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/" +
                            "constipation%2Farr_green_up.png";
                        $scope.foodList[2].openText="展开";
                    });
                }
            })
        });

        //打卡
        $scope.playCard=function(index,id){
            if($scope.foodList[index].status=="完成"){
                alert("此项计划今日已完成打卡！");
            }else{
                PushTicket.save({"plan_info_task_id":parseInt(id)},function (data) {
                    if(data.resultCode ==0){
                        num++;
                        $scope.foodList[index].status="完成";
                        if(num==0){
                            $scope.heartImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card0.png";
                        }else if(num==1){
                            $scope.heartImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card3_1.png";
                        }else if(num==2){
                            $scope.heartImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card3_2.png";
                        }else{
                            $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card_full.png";
                        }
                        $scope.momtime = moment().format('HH:mm');
                    }else{
                        alert("打卡失败！");
                    }
                });
            }

        }

        //饮食功效 点击更多
        $scope.openMore=function(index){
            $scope.index=index;
            if( $scope.foodList[index].openImg==$scope.foodList[index].openImg1){
                $(".open").eq(index).siblings("p").removeClass("select");
                $scope.foodList[index].openImg=$scope.foodList[index].openImg2;
                $scope.foodList[index].openText="收起";

            }
            else{
                $(".open").eq(index).siblings("p").addClass("select");
                $scope.foodList[index].openImg=$scope.foodList[index].openImg1;
                $scope.foodList[index].openText="展开";
            }

        }


    }]);

