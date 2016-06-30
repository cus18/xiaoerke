angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('customerServiceCtrl', ['$scope',
        function ($scope) {
            $scope.customerServiceInit = function () {
            };
            $scope.doctorConsultPay = function () {
                window.location.href="http://localhost:8080/keeper/wxPay/patientPay.do?serviceType=doctorConsultPay"
            };
            $scope.closeCustomerService = function () {
                wx.closeWindow();
            };
        }])

