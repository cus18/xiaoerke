angular.module('controllers', ['ionic']).controller('constipationCommentCtrl', [
    '$scope','$state','$stateParams','AppraisalList','SaveAppraisal','GetTasksList','GetUserLoginStatus','$location',
    function ($scope,$state,$stateParams,AppraisalList,SaveAppraisal,GetTasksList,GetUserLoginStatus,$location) {

        $scope.editLock ="false";
        $scope.info = {};
        var planTemplateId = "";

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
                    $scope.getComment();
                }
            })
        });

        /**
         * 获取评论内容
         */
        $scope.getComment = function(){
            $scope.pageLoading = true;
            GetTasksList.get(function (data) {
                planTemplateId = data.task.planTemplateId;
                AppraisalList.get({"id":planTemplateId,"pageNo":1,"pageSize":30}, function (data){
                    $scope.pageLoading = false;
                    $scope.commentList = data.appraisalList;
                });
            });

        }

        $scope.editComment=function(){
            $scope.editLock ="true";
        }
        $scope.cancel=function(){
            $scope.editLock ="false";
        }

        $scope.save = function(){
            if($scope.info.content == ""||$scope.info.content == undefined){
                alert("评论内容不能为空！");
            }else{
                SaveAppraisal.save({"planTemplateId":planTemplateId,"appraisal":$scope.info.content}, function (data){
                    if(data.resultCode == 0){
                        $scope.editLock ="false";
                        $scope.info.content = "";
                        $scope.getComment();
                    }else{
                        alert("保存评论失败！");
                    }
                });
            }
        }
    }]);

