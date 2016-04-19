angular.module('controllers', ['ionic']).controller('doctorHomeCtrl', [
    '$scope','$ionicPopup','$state','$stateParams','$location','GetUserLoginStatus',
    function ($scope,$ionicPopup,$state,$stateParams,$location,GetUserLoginStatus) {
        $scope.title = "医生信息";
        $scope.commentList =["全部评论","电话咨询","预约挂号"];
        $scope.commentTitle ="全部评论";
        $scope.haveComment = true;
        $scope.noComment = false;

        $scope.$on('$ionicView.enter', function(){
            $scope.moreComment = false;
           // $scope.pageLoading = true;
           /* var routePath = "/doctorBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{

                }
            })*/

        });
        //去粉丝页面
        $scope.goFans = function(){
            $state.go("doctorFans");
        }

        //评论类型选择
        $scope.getComment = function(){
            $scope.moreComment = true;
        }
        //选择具体评论类型
        $scope.setComment = function (index) {
            $scope.moreComment = false;
            $scope.commentTitle = $scope.commentList[index];
        }


    }]);




