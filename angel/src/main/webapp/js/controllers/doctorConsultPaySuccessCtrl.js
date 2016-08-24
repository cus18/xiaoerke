angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('doctorConsultPaySuccessCtrl', ['$scope',
        function ($scope) {
            $scope.doctorConsultPaySuccess = function () {
            };
            $scope.closeDoctorConsultPaySuccess = function () {
                wx.closeWindow();
            };
        }])

