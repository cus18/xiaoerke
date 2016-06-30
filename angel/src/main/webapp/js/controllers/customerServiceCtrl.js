angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('customerServiceCtrl', ['$scope',
        function ($scope) {
            $scope.customerServiceInit = function () {
            }
            $scope.closeCustomerService = function () {
                wx.closeWindow();
            };
        }])

