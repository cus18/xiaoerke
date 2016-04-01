angular.module('controllers', ['ionic']).controller('acceptRemindCtrl', [
    '$scope','$filter','$state','$stateParams','AcceptRemind','GetDoctorInfo','CheckBind','$location','GetUserLoginStatus',
    function ($scope,$filter,$state,$stateParams,AcceptRemind,GetDoctorInfo,CheckBind,$location,GetUserLoginStatus) {
        $scope.tile0="接诊提醒";
        $scope.lock=false;
        $scope.tog="today";
        var currentTime="";//获取当前时间
        var tomorrowTime="";//获取明天时间
        var doctorId="";//当前医生id

        $scope.togDate = function(data){
            if(data=="today"){
                $scope.tog="today";
                //获取当前时间医生接诊情况
                $scope.pageLoading = true;
                AcceptRemind.save({"doctorId":doctorId,"status":"1","date":currentTime},function(data){
                    $scope.pageLoading = false;
                    $scope.location=data.orders;
                })
            }
            if(data=="tomorrow"){
                $scope.tog="tomorrow"
                //获取当前时间医生接诊情况
                $scope.pageLoading = true;
                AcceptRemind.save({"doctorId":doctorId,"status":"1","date":tomorrowTime},function(data){
                    $scope.pageLoading = false;
                    $scope.location=data.orders;
                })
            }
        }

        /**
         * 时间转换
         */
        var changeTime = function(time){
            var tomorrow = time + 60*60*24*1000;
            tomorrowTime = $filter('date')(tomorrow, 'yyyy-MM-dd');
        }

        $scope.pageLoading = true;
        GetDoctorInfo.save({},function(data){
            $scope.pageLoading = false;
            if(data.doctorId!=undefined){
                currentTime = $filter('date')(data.currentTime, 'yyyy-MM-dd');//获取当前时间
                doctorId = data.doctorId;
                changeTime(data.currentTime);//调用时间转换方法

                //获取当前时间医生接诊情况
                $scope.pageLoading = true;
                AcceptRemind.save({"doctorId":doctorId,"status":"1","date":currentTime},function(data){
                    $scope.pageLoading = false;
                    $scope.location=data.orders;
                })
            }else{
                $state.go("bindDoctorPhone",{"redirect":"acceptRemind"});
            }
        });

        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/ap/doctorBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }
            })
        });

    }]);
