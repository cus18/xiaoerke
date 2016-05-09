angular.module('controllers', ['ionic']).controller('phoneConsultDetailsCtrl', [
    '$scope','$state','$stateParams','$location','GetUserLoginStatus','GetIllnessDetail','$filter','$http',
    function ($scope,$state,$stateParams,$location,GetUserLoginStatus,GetIllnessDetail,$filter,$http) {
        $scope.title = "病情资料";



        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/phoneConsultDoctorBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    var pData = {logContent:encodeURI("WXZJB_DHZX_CKBQZL")};
                    $http({method:'post',url:'util/recordLogs',params:pData});
                    GetIllnessDetail.save({"sys_phoneConsult_id":$stateParams.id,"doctorId":$stateParams.doctorId},function(data){
                        console.log("data",data);
                        $scope.serverTime = $filter('limitTo')(data.consultDate, 10);
                        var time = $filter('limitTo')(data.consultDate, -10);
                        $scope.serverTimeBegin = $filter('limitTo')(time, 5);
                        $scope.serverTimeEnd = $filter('limitTo')(time, -5);
                        $scope.serverPrice = data.price;
                        $scope.serverLength = data.serverLength;
                        $scope.name = data.illnessInfo.name;
                        $scope.age = data.illnessInfo.age;
                        $scope.illContent = data.illnessInfo.content;
                    });
                }
            })
        });



    }]);

