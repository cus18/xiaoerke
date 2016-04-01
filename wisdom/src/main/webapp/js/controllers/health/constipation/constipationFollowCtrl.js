angular.module('controllers', ['ionic']).controller('constipationFollowCtrl', [
    '$scope','$state','$stateParams','$timeout','SendWechatMessageToUser','UpdatePlanInfoForCycleTip','GetQuestion','$http',
    function ($scope,$state,$stateParams, $timeout,SendWechatMessageToUser,UpdatePlanInfoForCycleTip,GetQuestion,$http) {
        $scope.forgetAllLock=false;//第一种（全部未打卡）的情况
        $scope.forgetLock=false;// 第一种之忘了的情况
        $scope.wellLock=false;// 第一种之好了的情况

        $scope.forgetPartLock=false;// 第二种（部分未打卡）的情况
        $scope.forgetPartSetOpen=false;// 第二种（开关是否打开）的情况

        $scope.forgetNoLock=false;// 第三种（全部打卡）的情况
        $scope.noBetterLock=false;// 第三种（全部打卡）之未见好转的情况

        $scope.setLock=false;//去设置提醒
        $scope.praiseLock=false;//点赞提醒
        $scope.follow = [
            { text: "糟糕，忘记了", checked: true },
            { text: "宝宝已经好了", checked: false },
            { text: "宝宝毫无好转", checked: false }
        ];


        var pData = {logContent:encodeURI("BMGL_37")};
        $http({method:'post',url:'util/recordLogs',params:pData});




        $scope.$on('$ionicView.enter', function(){
            if($stateParams.type=="none"){//任务都没有打卡
                $scope.plannone = true;
                $scope.planpart = false;
                $scope.planall = false;
            }else if($stateParams.type=="part"){//部分任务打卡
                $scope.plannone = false;
                $scope.planpart = true;
                $scope.planall = false;
            }else if($stateParams.type=="all"){//全部任务都打卡
                $scope.plannone = false;
                $scope.planpart = false;
                $scope.planall = true;
            }
        });



       //第一种情况，全部未打卡的3个选项
        $scope.forgetAllSelect=function(index){
            if(index==0){
                $scope.forgetLock=true;
            }
            else if(index==1){
                var pData = {logContent:encodeURI("BMGL_38")};
                $http({method:'post',url:'util/recordLogs',params:pData});
                getQuestion("好了");
                $scope.wellLock=true;
            }
            else if(index==2){
                $scope.change();
                var pData = {logContent:encodeURI("BMGL_39")};
                $http({method:'post',url:'util/recordLogs',params:pData});
                getQuestion("无好转");
                $state.go('constipationWShortDeal',{type:"next"});
            }
        }

        //关闭 第一种提示情况
        $scope.close=function(){
            $scope.forgetLock=false;
            $scope.wellLock=false;
            $scope.setLock=false;
            $scope.praiseLock=false;
            $scope.noBetterLock=false;
        }
        $scope.forgetNotDo=function(){
            $state.go('constipationFollowForget',{show:"noDo",planInfoId:$stateParams.planInfoId});
        }
        $scope.forgetDo=function(){
            $state.go('constipationFollowForget',{show:"do",planInfoId:$stateParams.planInfoId});
        }
        //不了（不点赞）
        $scope.noPraise=function(){
            $scope.setLock=true;
        }
        //点赞
        $scope.praise=function(){
            getQuestion("给宝大夫点赞");
            $scope.praiseLock=true;
            $scope.wellLock=false;
            $timeout(function() {
                $scope.praiseLock=false;
                $scope.setLock=true;
            }, 2000);
        }
        // 第三种情况之未见好转
        $scope.noBetter=function(){
            var pData = {logContent:encodeURI("BMGL_39")};
            $http({method:'post',url:'util/recordLogs',params:pData});
            getQuestion("无好转");
            $scope.noBetterLock=true;
        }
        $scope.better=function(){
            var pData = {logContent:encodeURI("BMGL_38")};
            $http({method:'post',url:'util/recordLogs',params:pData});
            getQuestion("好了");
            $scope.wellLock=true;
        }
        //不了（不设置）
        $scope.noSet=function(){
            $scope.change();
            $state.go('constipationIndex');
        }
        //去设置提醒
        $scope.goSet=function(){
            $scope.change();
            $state.go('constipationRemind',{flag:'next'});
        }
        //咨询医生
        $scope.consultDoc = function(){
            $scope.change();
            SendWechatMessageToUser.save({},function(data){
            });
            WeixinJSBridge.call('closeWindow');
    }

        $scope.change = function(){
            UpdatePlanInfoForCycleTip.get({"planInfoId":parseInt($stateParams.planInfoId)},function(data){

            });
        }

        function getQuestion(type){
            GetQuestion.save({"type":type},function(data){

            })
        }

    }]);

