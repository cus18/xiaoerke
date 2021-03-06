angular.module('controllers', ['ionic']).controller('constipationKneadCtrl', [
    '$scope','$state','$stateParams','ConfirmDetail','AppraisalList','PushTicket','GetUserLoginStatus','$location','$http',
    function ($scope,$state,$stateParams,ConfirmDetail,AppraisalList,PushTicket,GetUserLoginStatus,$location,$http) {

        $scope.checkCardLock =true;//打卡标记
        $scope.checkCardImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcheck_card_no.png";
        $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card0.png";
        $scope.useImg = [
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fknead1.gif",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fknead2.gif",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fknead3.gif",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fknead4.gif",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fknead5.gif",
        ];
        var planTaskId = "";

        var pData = {logContent:encodeURI("BMGL_49")};
        $http({method:'post',url:'util/recordLogs',params:pData});

        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/ctpBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    $scope.momtime = moment().format('HH:mm');
                    //获取打卡详情
                    ConfirmDetail.get({"planInfoId":parseInt($stateParams.planInfoId),"type":$stateParams.planTaskType}, function (data) {
                        $scope.continue = data.continue;
                        $scope.total = data.total;
                        planTaskId = data.allid;
                        if(data.all=="yes"){
                            $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card_full.png";
                            $scope.checkCardLock =false;
                        }
                    });
                    $scope.pageLoading = true;
                    AppraisalList.get({"id":$stateParams.id,"pageNo":1,"pageSize":2}, function (data){
                        $scope.pageLoading = false;
                        $scope.commentList = data.appraisalList;
                        $scope.commenttotal = data.total;
                    });
                }
            })
        });

        $scope.checkCard=function(){
            PushTicket.save({"plan_info_task_id":parseInt(planTaskId)},function (data) {
                if(data.resultCode ==0){
                    $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card_full.png";
                    $scope.checkCardLock =false;
                    $scope.momtime = moment().format('HH:mm');
                }else{
                    alert("打卡失败！");
                }
            });
        }
    }]);

