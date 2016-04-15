angular.module('controllers', ['ionic']).controller('phoneConsultFirstCtrl', [
    '$scope','$ionicPopup','$state','$stateParams','$location','GetUserLoginStatus',
    function ($scope,$ionicPopup,$state,$stateParams,$location,GetUserLoginStatus) {
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

                }
            })
        });



    }]);

