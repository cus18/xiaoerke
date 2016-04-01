angular.module('controllers', ['ionic']).controller('constipationDefecateCtrl', [
    '$scope','$state','$stateParams','ConfirmDetail','AppraisalList','PushTicket','GetUserLoginStatus','$location','$http',
    function ($scope,$state,$stateParams,ConfirmDetail,AppraisalList,PushTicket,GetUserLoginStatus,$location,$http) {

        $scope.checkCardLock =true;//打卡标记
        $scope.checkCardImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcheck_card_no.png";
        $scope.heartImg ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fheart_card0.png";
        $scope.videoLock=false;
        $scope.index1=0;
        $scope.musicLock =false;
        var planTaskId = "";

        var pData = {logContent:encodeURI("BMGL_50")};
        $http({method:'post',url:'util/recordLogs',params:pData});

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
        };

        $scope.slideHasChanged=function(index){
            $scope.index1=index;
        }
        //播放视频
        $scope.playVideo=function() {
            $scope.videoLock=true;
            $("video")[0].play();

        }
        $scope.cancel=function() {
            $scope.videoLock=false;
        }


        //播放音乐
        $scope.playMusic=function(index2) {
            $scope.index2=index2;

           $scope.img= $(".play").eq(index2).attr("src");
            if( $scope.img=="src","http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmusic_on.png"){
                for(var i=0;i<8;i++){
                    $("audio")[i].pause();
                    $(".play").attr("src","src","http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmusic_on.png");
                }
                $(".play").eq(index2).attr("src","http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmusic_off.png");
                $("audio")[index2].play();
            }
            else{
                $(".play").eq(index2).attr("src","http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmusic_on.png");
                $("audio")[index2].pause();
            }

        }

        $scope.$on('$ionicView.enter', function() {
            $scope.pageLoading = true;
            var routePath = "/ap/ctpBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }
            })
            $scope.index1=0;
            $scope.momtime = moment().format('HH:mm');
            //获取打卡详情
            ConfirmDetail.get({"planInfoId":parseInt($stateParams.planInfoId),"type":$stateParams.planTaskType}, function (data) {
                console.log("data",data);
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
        })
    }]);

