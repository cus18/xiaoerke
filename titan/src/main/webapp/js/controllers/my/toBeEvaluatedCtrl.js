angular.module('controllers', ['ionic']).controller('ToBeEvaluatedCtrl', [
        '$scope','MyselfInfoAppointment','$state','GetUserLoginStatus','$location',
        function ($scope,MyselfInfoAppointment,$state,GetUserLoginStatus,$location) {

            $scope.title="待评价"
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.evaluate = "去评价"
            $scope.delete = "删除"
            $scope.care1 = "一句真心的话语能打动千千万万的人"
            $scope.care2 = "粑粑妈妈们需要你们真心的反馈哦"
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
                        $scope.pageLoading =true;
                        MyselfInfoAppointment.save({"pageNo":"1","pageSize":"10","status":"2"},function(data){
                            $scope.pageLoading = false;
                            $scope.appointmentData = data.appointmentData;
                            if( data.appointmentData.length==0){
                                $scope.isBlank = true
                            }
                        });
                    }
                })
            });
     }])