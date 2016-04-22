angular.module('controllers', ['ionic']).controller('doctorFansCtrl', [
    '$scope','$ionicPopup','$state','$stateParams','$location','GetUserLoginStatus','GetFansList',
    function ($scope,$ionicPopup,$state,$stateParams,$location,GetUserLoginStatus,GetFansList) {
        $scope.title = "粉丝列表";
        $scope.haveMore = true;
        $scope.noMore = false;
        var imgUrl = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/other%2Fliangp.png";
        $scope.fansList =[{"img":"","name":"aaffffaa"},{"img":imgUrl,"name":"aaaa"},{"img":imgUrl,"name":"aaaa"},{"img":"","name":"aaaa"},{"img":"","name":"aaaa"}];
        var num;

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
                    $scope.noMoreFans = true;
                    $scope.FansCon = "点击加载更多";
                    num = 5;
                    getFansList(num);
                }
            })
        });

        //获取更多粉丝
        $scope.getMoreFans = function () {
            num = num+10;
            getFansList(num);
        }

        var getFansList = function (size) {
            GetFansList.save({"doctorId":$stateParams.id,"pageNo":"1","pageSize":size}, function (data) {
                console.log("data",data);
                if(data.fansList.length==0){
                    $scope.haveMore = false;
                    $scope.noMore = true;
                }else if(data.fansList.length<size&&data.fansList.length>0){
                    $scope.haveMore = true;
                    $scope.noMore =false;
                    $scope.FansList = data.fansList;
                    $scope.noMoreFans = false;
                    $scope.FansCon = "已全部显示";
                }else{
                    $scope.haveMore = true;
                    $scope.noMore =false;
                    $scope.FansList = data.fansList;
                    $scope.noMoreFans = true;
                    $scope.FansCon = "点击加载更多";
                }
            });
        }


    }]);



