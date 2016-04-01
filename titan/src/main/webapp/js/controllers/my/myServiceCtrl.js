angular.module('controllers', ['ionic']).controller('myServiceCtrl', [
    '$scope','$state','$stateParams','$http','$ionicPopup',
    'GetUserMemberService','GetUserLoginStatus','$location',
    function ($scope,$state,$stateParams,$http,$ionicPopup,
              GetUserMemberService,GetUserLoginStatus,$location) {
        $scope.title = "我的服务";
        $scope.title0 = "宝大夫（400-623-7120）";
        $scope.selectItemArr= ["当前服务","免费预约券"];
        $scope.selectItemNum=0;
        $scope.selectItem=function(num){
            $scope.selectItemNum=num;
        }
        $scope.goToMemberService = function(){
            $state.go("memberService",{memberType:"week",action:"extend",register_service_id:""});
        }
        $scope.goToMemberServiceDetail = function(indexId){
            var memSrsItemSrsRelId = $scope.memberServiceList[indexId].memSrsItemSrsRelId
            $state.go("memberServiceDetail",{memberServiceId:memSrsItemSrsRelId});
        }

        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/appointBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                } else {
                    GetUserMemberService.save({},function(data){
                        $scope.memberServiceList = data.memberServiceList;
                        if($scope.memberServiceList.length>0){
                            $scope.hasService = "true";
                        }
                        else{
                            $scope.hasService = "false";
                        }
                    })
                }
            })
        });
    }])
