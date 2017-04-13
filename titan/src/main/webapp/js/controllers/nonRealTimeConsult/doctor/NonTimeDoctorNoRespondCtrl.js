angular.module('controllers', []).controller('NonTimeDoctorNoRespondCtrl', [
    '$scope', '$state', '$timeout', '$http', 'GetDoctorLoginStatus','GetNoReplytList','GetConfig',
    function ($scope, $state, $timeout, $http, GetDoctorLoginStatus,GetNoReplytList,GetConfig) {

        //页面初始化
        $scope.NonTimeDoctorNoRespondInit = function () {
            //校验是否登陆
            GetDoctorLoginStatus.save({}, function (data) {
                $scope.pageLoading = false;
                // if (!data.status == "failure") {
                    // GetConfig.save({}, function (data) {
                    //     window.location.href = data.publicSystemInfo.nonRealtimeLoginUrl;
                    // })
                // }else if(!data.status == "success"){

                    GetNoReplytList.save({}, function (data) {
                        $scope.allServiceList = data.ListInfo;
                        console.log("allService 数据显示 ",data.ListInfo);
                    });

                // }else {
                //     alert("非系统咨询医生，请联系接诊员！");
                // }
            });
        };

    }]);
