angular.module('controllers', []).controller('NonTimeDoctorMessageListCtrl', [
    '$scope', '$state', '$timeout', '$http', 'GetDoctorLoginStatus',
    function ($scope, $state, $timeout, $http, GetDoctorLoginStatus) {

        //校验是否登陆
        GetDoctorLoginStatus.save({}, function (data) {
            $scope.pageLoading = false;
            if (data.status == "failure") {
                window.location.href = "http://localhost/titan/nonRealTimeConsult#/NonTimeDoctorLogin";
            } else {
                $scope.selectItem = "cur";
                $scope.curMessageList = [
                    {
                        pic: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                        date: "10月8日",
                        sex: "男",
                        age: "6岁2个月",
                        message: "肠胃问题",
                        state: "2"

                    },
                    {
                        pic: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                        date: "10月12日",
                        sex: "女",
                        age: "3岁2个月",
                        message: "肠胃问题",
                        state: "1"
                    },
                    {
                        pic: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                        date: "15:12",
                        sex: "男",
                        age: "4岁2个月",
                        message: "肠胃问题",
                        state: "2"
                    },
                ];
                $scope.allMessageList = [
                    {
                        pic: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                        date: "10月10日",
                        sex: "女",
                        age: "2岁2个月",
                        message: "肠胃问题",
                        state: "0"

                    },
                    {
                        pic: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                        date: "10月10日",
                        sex: "男",
                        age: "3岁2个月",
                        message: "肠胃问题",
                        state: "2"
                    },
                    {
                        pic: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                        date: "10月10日",
                        sex: "女",
                        age: "5岁2个月",
                        message: "肠胃问题",
                        state: "1"
                    }
                ];
                $scope.selectService = function (item) {
                    $scope.selectItem = item;
                };
                //页面初始化
                $scope.NonTimeDoctorMessageListInit = function () {
                    document.title = "消息列表"; //页面title

                };
            }
        });

    }]);
