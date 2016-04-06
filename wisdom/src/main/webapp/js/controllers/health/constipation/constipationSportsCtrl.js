angular.module('controllers', ['ionic']).controller('constipationSportsCtrl', [
    '$scope','$state','$stateParams','ConfirmDetail','AppraisalList','PushTicket',
    'GetTasksInfo','$sce','GetUserLoginStatus','$location','$http',
    function ($scope,$state,$stateParams,ConfirmDetail,AppraisalList,PushTicket,
              GetTasksInfo,$sce,GetUserLoginStatus,$location,$http) {

        $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card0.png";
        //初始化一日三餐
        $scope.sportsList =[];
        $scope.sportsImgList =[];
        var amstatus = "";
        var pmstatus= "";
        var amtime = "";
        var pmtime = "";
        var amid="";
        var pmid="";
        var num=0;

        var pData = {logContent:encodeURI("BMGL_52")};
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
                }else{
                    $scope.sportsList =[];
                    $scope.momtime = moment().format('HH:mm');
                    //获取打卡详情
                    ConfirmDetail.get({"planInfoId":parseInt($stateParams.planInfoId),
                        "type":$stateParams.planTaskType}, function (data) {
                        $scope.continue = data.continue;
                        $scope.total = data.total;
                        amtime=data.amtime;
                        pmtime=data.pmtime;
                        amid=data.amid;
                        pmid=data.pmid;
                        if(data.am=="yes"&&data.pm=="yes"){
                            $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card_full.png";
                        }else if(data.am=="no"&&data.pm=="no"){
                            $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card0.png";
                        }else{
                            $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card2_1.png";
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
                    });
                    $scope.pageLoading = true;
                    AppraisalList.get({"id":$stateParams.id,"pageNo":1,"pageSize":2}, function (data){
                        $scope.pageLoading = false;
                        $scope.commentList = data.appraisalList;
                        $scope.commenttotal = data.total;
                    });
                    GetTasksInfo.get({"type":$stateParams.planTaskType},function(data){
                        var amcontent = $sce.trustAsHtml(angular.copy(data.am.content));
                        var pmcontent = $sce.trustAsHtml(angular.copy(data.pm.content));
                        $scope.sportsList.push(data.am);
                        $scope.sportsList.push(data.pm);
                        $scope.sportsList[0].sports=[];
                        $scope.sportsList[1].sports=[];
                        $scope.sportsList[0].mid=amid;
                        $scope.sportsList[0].content=amcontent;
                        $scope.sportsList[0].time=amtime;
                        $scope.sportsList[0].status=amstatus;
                        $scope.sportsList[0].movement="宝宝上午做的运动";
                        $scope.sportsList[0].openImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Farr_green_down.png";
                        $scope.sportsList[0].openImg1="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Farr_green_down.png";
                        $scope.sportsList[0].openImg2="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Farr_green_up.png";
                        $scope.sportsList[0].openText="展开";
                        $scope.sportsList[1].mid=pmid;
                        $scope.sportsList[1].content=pmcontent;
                        $scope.sportsList[1].time=pmtime;
                        $scope.sportsList[1].status=pmstatus;
                        $scope.sportsList[1].movement="宝宝下午做的运动";
                        $scope.sportsList[1].openImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Farr_green_down.png";
                        $scope.sportsList[1].openImg1="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Farr_green_down.png";
                        $scope.sportsList[1].openImg2="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Farr_green_up.png";
                        $scope.sportsList[1].openText="展开";
                    });
                }
            })
        });

        //打卡
        $scope.playCard=function(index,id){
            if($scope.sportsList[index].status=="完成"){
                alert("此项计划今日已完成打卡！");
            }else{
                PushTicket.save({"plan_info_task_id":parseInt(id)},function (data) {
                    if(data.resultCode ==0){
                        num++;
                        $scope.sportsList[index].status="完成";
                        if(num==0){
                            $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card0.png";
                        }else if(num==1){
                            $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card2_1.png";
                        }else if(num==2){
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
            if( $scope.sportsList[index].openImg==$scope.sportsList[index].openImg1){
                $(".open").eq(index).siblings("p").removeClass("select");
                $scope.sportsList[index].openImg=$scope.sportsList[index].openImg2;
                $scope.sportsList[index].openText="收起";
            }
            else{
                $(".open").eq(index).siblings("p").addClass("select");
                $scope.sportsList[index].openImg=$scope.sportsList[index].openImg1;
                $scope.sportsList[index].openText="展开";
            }
        }
    }]);

