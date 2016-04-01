angular.module('controllers', ['ionic']).controller('knowledgeCommentCtrl', [
        '$scope','$state','$stateParams','listComment','saveComment','$ionicScrollDelegate',
        function ($scope,$state,$stateParams,listComment,saveComment,$ionicScrollDelegate) {
            $scope.info = {};
            $scope.lock='false';
            $scope.editLock = "false";

            var num=10;
            $scope.$on('$ionicView.enter', function() {
                $scope.lockScroll="false";
                listComment.get({"articleId":$stateParams.id,"pageNo":1,"pageSize":20},function(data){
                    $scope.commentList = data.commentList;
                });
            });

            $scope.editComment = function(){
                $scope.editLock = "true";
            }

            $scope.cancel = function(){
                $scope.editLock = "false";
            }

            /**
             * 保存
             */
            $scope.save = function(){
                $scope.editLock = "false";
                if($scope.info.content!=undefined){
                    $scope.pageLoading=true;
                    saveComment.save({"articleId":$stateParams.id,"commentContent":$scope.info.content},function(data){
                        if(data.resultMsg==undefined){
                            $scope.info.content="";
                            listComment.get({"articleId":$stateParams.id,"pageNo":1,"pageSize":10},function(data){
                                $scope.pageLoading=false;
                                $scope.commentList = data.commentList;

                            });
                        }else{
                            alert("评论提交失败！");
                        }
                    });
                }else{
                    alert("评论内容不能为空！");
                }
            }
    }]);