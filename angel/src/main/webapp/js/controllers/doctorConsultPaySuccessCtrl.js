angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('doctorConsultPaySuccessCtrl', ['$scope','$stateParams',
        function ($scope,$stateParams) {
            $scope.consultTime = $stateParams.consultTime;
            $scope.doctorConsultPaySuccess = function () {
                if($scope.consultTime == "30")
                    $scope.consultTime = "30分钟";
                else
                    $scope.consultTime = "24小时";

            };
            $scope.closeDoctorConsultPaySuccess = function () {
                wx.closeWindow();
            };

        }])

