angular.module('controllers', ['ionic']).controller('cMotherNecessaryCtrl', [
    '$scope','$state','$stateParams','GetUserLoginStatus','$location','$http',
    function ($scope,$state,$stateParams,GetUserLoginStatus,$location,$http) {

        var pData = {logContent:encodeURI("BMGL_31")};
        $http({method:'post',url:'ap/util/recordLogs',params:pData});

        $scope.necessaryImg = [
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_necessary1.png",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_necessary5.png",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_necessary3.png",
            "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fmother_necessary4.png"
            ];
        $scope.necessaryDetail=function(index){
            $scope.index=index;
            $state.go('cMotherNecessaryDetail',{necessaryNum:$scope.index});
        }

        $scope.$on('$ionicView.enter', function(){
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
        })
    }]);

