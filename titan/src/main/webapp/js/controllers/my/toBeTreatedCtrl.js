angular.module('controllers', ['ionic']).controller('ToBeTreatedCtrl', [
        '$scope','MyselfInfoAppointment','$state','GetUserLoginStatus','$rootScope','$location',
        function ($scope,MyselfInfoAppointment,$state,GetUserLoginStatus,$rootScope,$location) {

            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.title = "待就诊"
            $scope.isBlank = false;

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/ap/appointBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    } else {
                        MyselfInfoAppointment.save({"pageNo":"1","pageSize":"100","status":"1",
                            "unBindUserPhoneNum":$rootScope.unBindUserPhoneNum},function(data){
                            $scope.pageLoading = false;
                            $scope.appointmentData = data.appointmentData;
                            if( data.appointmentData.length==0){
                                $scope.care1 = "及时就诊，万分健康";
                                $scope.care2 = "宝宝的健康是一家人的幸福";
                                $scope.isBlank = true
                            }
                        });
                    }
                })
            })
        }])
