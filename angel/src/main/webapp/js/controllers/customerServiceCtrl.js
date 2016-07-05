angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('customerServiceCtrl',['$scope','ConsultCustomOnly',
        function ($scope,ConsultCustomOnly) {
            $scope.customerServiceInit = function () {
            };
            $scope.doctorConsultPay = function () {
                window.location.href="http://localhost:8080/keeper/wxPay/patientPay.do?serviceType=doctorConsultPay"
            };
            $scope.closeCustomerService = function () {

                ConsultCustomOnly.get({openId:"o8gDqvuFoAJA8WBgfL0H3ZlApe3w"},function () {
                    wx.closeWindow();
                })
            };
        }])

