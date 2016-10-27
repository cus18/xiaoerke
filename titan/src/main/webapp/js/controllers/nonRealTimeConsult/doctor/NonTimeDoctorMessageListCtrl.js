angular.module('controllers', []).controller('NonTimeDoctorMessageListCtrl', [
    '$scope', '$state', '$timeout', '$http', 'GetDoctorLoginStatus','GetDoctorService',
    function ($scope, $state, $timeout, $http, GetDoctorLoginStatus,GetDoctorService) {
        $scope.selectItem = "cur";
        // 选择 头部的当前服务和全部服务
        $scope.selectService = function (item) {
            $scope.selectItem = item;
        };
        $scope.goConversationPage = function (sessionid){
            $state.go('NonTimeDoctorConversation',{"sessionId":sessionid});
        }
        //页面初始化
        $scope.NonTimeDoctorMessageListInit = function () {
            //校验是否登陆
            GetDoctorLoginStatus.save({}, function (data) {
                $scope.pageLoading = false;
                if (data.status == "failure") {
                    window.location.href = "http://127.0.0.1/titan/nonRealTimeConsult#/NonTimeDoctorLogin";
                } else {
                    GetDoctorService.save({serviceType:"currentService"}, function (data) {
                        $scope.curServiceList = data.ListInfo;
                        console.log("curService",data.ListInfo);
                    });
                    GetDoctorService.save({serviceType:"allService"}, function (data) {
                        $scope.allServiceList = data.ListInfo;
                        console.log("allService",data.ListInfo);
                    });

                }
            });
        };

    }]);
