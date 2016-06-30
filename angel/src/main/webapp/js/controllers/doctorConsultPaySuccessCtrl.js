angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('doctorConsultPaySuccessCtrl', ['$scope',
        function ($scope) {
            $scope.doctorConsultJumpFirstInit = function () {
                console.log('aaaaaaa')
            }
            $scope.doctorConsultPay = function () {
                window.location.href="http://localhost:8080/keeper/wxPay/patientPay.do?serviceType=doctorConsultPay"
            };
        }])

