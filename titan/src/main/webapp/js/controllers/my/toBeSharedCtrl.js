angular.module('controllers', ['ionic']).controller('ToBeSharedCtrl', [
        '$scope','$state','MyShare','MyselfInfoAppointment','GetUserLoginStatus','$location',
        function ($scope,$state,MyShare,MyselfInfoAppointment,GetUserLoginStatus,$location) {

            $scope.title="待分享"
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.isBlank = false

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
                        $scope.pageLoading = true;
                        MyselfInfoAppointment.save({"pageNo":"1","pageSize":"10","status":"3"},function(data){
                            $scope.pageLoading = false;
                            $scope.appointmentData = data.appointmentData;
                            if( data.appointmentData.length==0){
                                $scope.care1 = "还没有可分享订单哦"
                                $scope.care2 = "让更多的宝宝享受到最优质的待遇"
                                $scope.isBlank = true
                            }
                        });
                    }
                })
            });
        }])