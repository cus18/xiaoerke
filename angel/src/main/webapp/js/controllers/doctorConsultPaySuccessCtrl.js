angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('doctorConsultPaySuccessCtrl', ['$scope','$stateParams',
        function ($scope,$stateParams) {
            $scope.consultTime = $stateParams.consultTime;
            $scope.doctorConsultPaySuccess = function () {
                if($scope.consultTime == "1") {
                    $scope.consultTime = "30分钟";
                }else if ($scope.consultTime == "2") {
                    $scope.consultTime = "24小时";
                }else {
                    $scope.consultTime = "一个月";
                }


            };
            $scope.closeDoctorConsultPaySuccess = function () {
                wx.closeWindow();
            };

        }])

