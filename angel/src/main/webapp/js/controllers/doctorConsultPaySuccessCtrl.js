angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('doctorConsultPaySuccessCtrl', ['$scope',
        function ($scope) {
            $scope.consultTime = "";
            $scope.doctorConsultPaySuccess = function () {
                $scope.consultTime = getQueryString("consultTime");
                if($scope.consultTime == "30")
                    $scope.consultTime = "30分钟";
                else
                    $scope.consultTime = "24小时";

            };
            $scope.closeDoctorConsultPaySuccess = function () {
                wx.closeWindow();
            };

            function getQueryString(name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
                var r = window.location.search.substr(1).match(reg);
                if (r != null) return unescape(r[2]); return null;
            }
        }])

