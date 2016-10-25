angular.module('controllers', []).controller('NonTimeDoctorMessageListCtrl', [
    '$scope', '$state', '$timeout', '$http', 'GetDoctorLoginStatus','GetDoctorService',
    function ($scope, $state, $timeout, $http, GetDoctorLoginStatus,GetDoctorService) {
        $scope.selectItem = "cur";
        $scope.GetMessageList = function (item) {
            GetDoctorService.save({serviceType:item}, function (data) {
                $scope.curService = data.ListInfo;
                console.log("111",data.ListInfo);
            });
        };
        $scope.selectService = function (item) {
            $scope.selectItem = item;
        };
        //页面初始化
        $scope.NonTimeDoctorMessageListInit = function () {
            //校验是否登陆
            GetDoctorLoginStatus.save({}, function (data) {
                $scope.pageLoading = false;
                if (data.status == "failure") {
                    window.location.href = "http://127.0.0.1/titan/nonRealTimeConsult#/NonTimeDoctorLogin";
                } else {
                   // $scope.MessageListInfo = $scope.GetMessageList("currentService");//currentService 当前服务 allService 全部服务
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
