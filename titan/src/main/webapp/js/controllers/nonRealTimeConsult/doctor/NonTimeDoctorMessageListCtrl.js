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
                    window.location.href = "http://s201.xiaork.com/titan/nonRealTimeConsult#/NonTimeDoctorLogin";
                }else if(data.status == "success"){
                    GetDoctorService.save({serviceType:"currentService",csUserId:data.csUserId}, function (data) {
                        $scope.curServiceList = data.ListInfo;
                        console.log("curService",data.ListInfo);
                    });
                    GetDoctorService.save({serviceType:"allService",csUserId:data.csUserId}, function (data) {
                        $scope.allServiceList = data.ListInfo;
                        console.log("allService",data.ListInfo);
                    });

                }else {
                    alert("非系统咨询医生，请联系接诊员！");
                }
            });
        };

    }]);
