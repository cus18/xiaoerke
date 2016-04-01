angular.module('controllers', ['ionic']).controller('RouteCtrl', [
        '$scope','$state','$stateParams','AppointmentStatus','$location',
        function ($scope,$state,$stateParams,AppointmentStatus,$location) {
            $scope.title = "详细路线";
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.routeInfo = {}
            $scope.pageLoading = true;

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/appointBBBBBB" + $location.path();
                AppointmentStatus.save({register_service_id:$stateParams.sys_regist_id,
                    routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }
                    else{
                        $scope.pageLoading = false;
                        $scope.routeInfo = data.root
                    }
                })
            })
    }])
