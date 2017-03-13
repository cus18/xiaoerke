angular.module('controllers', ['ionic','ngDialog']).controller('haveRecordCtrl', [
    '$scope','$state','$stateParams','ConversationRecord','$ionicScrollDelegate',
    function ($scope,$state,$stateParams,ConversationRecord,$ionicScrollDelegate) {
        $scope.btnStatus=false;
        //有聊天记录的状态
        $scope.recordStatus=false;
        //没有聊天记录的状态
        $scope.noRecordStatus=false;

        if($stateParams.status=='true'){
            $scope.recordStatus=true;
            $scope.noRecordStatus=false;
        }else if($stateParams.status=='false'){
            $scope.recordStatus=false;
            $scope.noRecordStatus=true;
        }
        //查看全部的按钮状态
        $scope.lookStatus=true;


        ConversationRecord.save({"sessionId":$stateParams.sessionId,"pageNum":1,"pageSize":10000},function (data) {
            //聊天记录
            $scope.recordList = data.recordList;

            //留言
            $scope.content = data.content;

            //用户昵称
            $scope.nickname=data.attentionInfo.nickname;


            //医生的信息
            $scope.doctorInfo=data.doctorInfoVo;


            //用户的头像
            $scope.headImgUrl=data.headImgUrl;


            //头像判断
            //放到聊天记录里
            for(var i=0;i<$scope.recordList.length;i++){
                if($scope.recordList[i].senderId==$scope.recordList[i].userId){
                    $scope.recordList[i].headurl= $scope.headImgUrl;
                }else{
                    $scope.recordList[i].headurl='http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/'+$scope.doctorInfo.userId;
                }
            }

            $scope.twoRecord=$scope.recordList.slice(0,2)
            $scope.record=$scope.twoRecord;
        });

        $scope.lookAll=function(){
            $scope.record=$scope.recordList;
            $scope.lookStatus=false;
            $scope.btnStatus=true;

        }
        //去底部
        $scope.goBottom=function(){
            $ionicScrollDelegate.scrollBottom([true]);
        }
    }])
