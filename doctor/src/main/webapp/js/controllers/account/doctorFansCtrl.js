angular.module('controllers', ['ionic']).controller('doctorFansCtrl', [
    '$scope','$ionicPopup','$state','$stateParams','$location','GetUserLoginStatus',
    function ($scope,$ionicPopup,$state,$stateParams,$location,GetUserLoginStatus) {
        $scope.title = "粉丝列表";
        $scope.haveMore = true;
        $scope.noMore = false;
        var imgUrl = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/other%2Fliangp.png";
        $scope.fansList =[{"img":"","name":"aaffffaa"},{"img":imgUrl,"name":"aaaa"},{"img":imgUrl,"name":"aaaa"},{"img":"","name":"aaaa"},{"img":"","name":"aaaa"}];


        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/doctorBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{

                }
            })
        });



    }]);



