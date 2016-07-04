angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('doctorConsultPaySuccessCtrl', ['$scope',
        function ($scope) {
            $scope.doctorConsultJumpFirstInit = function () {
            };
            $scope.closeDoctorConsultPaySuccess = function () {
                wx.closeWindow();
            };
        }])

